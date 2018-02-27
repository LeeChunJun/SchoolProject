package com.leechunjun.school;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.leechunjun.school.bean.Course;
import com.leechunjun.school.data.CourseFetcher;
import com.leechunjun.school.data.FileStorages;
import com.leechunjun.school.data.HttpReply;
import com.leechunjun.school.data.SharedPreferences;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Home1Fragment extends Fragment {
	private static final String TAG = "Home1Fragment";
	private static final String CourseFile = "CourseJsonFile";

	private Context context;
	private View rootView;// 缓存Fragment view
	private Spinner spCourse;
	private int week;//今天周几
	private ListView lvCourse;
	private ImageView ivNotice;
	private ImageView ivFlash;
	
	private Dialog dialog;
	private ImageButton btnOK;
	private TextView tvCourseNum;
	private TextView tvCourseName;
	private TextView tvCourseTeacher;
	private TextView tvCourseClass;
	private View dialogCourse;
	private List<Course> datas;
	private boolean leisure = false;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:
				parseJSON((String)msg.obj,week);
				if(leisure){
					ivNotice.setImageResource(R.drawable.study_free);
					lvCourse.setEmptyView(ivNotice);
				} 
				lvCourse.setAdapter(new CourseAdapter(context, datas));
				break;
			case 0x02:
				ivNotice.setImageResource(R.drawable.connect_broken);
				lvCourse.setEmptyView(ivNotice);
				ivNotice.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						downLoadCourse();
					}
				});
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.home1, container, false);
			context = rootView.getContext();
			/*
			 * 设置显示下拉列表
			 */
			spCourse = (Spinner) rootView.findViewById(R.id.sp_course);
			String[] spItems = getResources().getStringArray(R.array.week);
			ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spItems);
			spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spCourse.setAdapter(spAdapter);
			week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;//1是周日
			spCourse.setSelection(week);
			spCourse.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					week = position;
					try {
						String response = new FileStorages(context).load(CourseFile);
						Message message = new Message();
						message.what = 0x01;
						message.obj = response;
						handler.sendMessage(message);
					} catch (IOException e) {
						Log.e(TAG, "Fail to get CourseJson "+e);
						handler.sendEmptyMessage(0x02);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
			/*
			 * 设置显示课程表
			 */
			lvCourse = (ListView) rootView.findViewById(R.id.lv_course);
			ivNotice = (ImageView) rootView.findViewById(R.id.iv_notice);
			ivFlash = (ImageView) rootView.findViewById(R.id.iv_flash);
			ivFlash.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					downLoadCourse();
				}
			});
			downLoadCourse();
			/*
			 * 设置单击item之后的自定义对话框
			 */
			dialog = new Dialog(context, R.style.load_dialog);
			dialogCourse = LayoutInflater.from(context).inflate(R.layout.dialog_course, null);
	        dialog.setContentView(dialogCourse);
			dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 	
			dialog.setCancelable(false);
			btnOK = (ImageButton) dialogCourse.findViewById(R.id.btn_course_ok);
	        btnOK.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
	        /*
	         * listview的单击事件
	         */
	        lvCourse.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					tvCourseNum = (TextView) dialogCourse.findViewById(R.id.tv_course_num);
					tvCourseNum.setText(datas.get(position).getmNumber());
					tvCourseName = (TextView) dialogCourse.findViewById(R.id.tv_course_name);
					tvCourseName.setText(datas.get(position).getmCourse());
					tvCourseTeacher = (TextView) dialogCourse.findViewById(R.id.tv_course_teacher);
					tvCourseTeacher.setText(datas.get(position).getmTeacher());
					tvCourseClass = (TextView) dialogCourse.findViewById(R.id.tv_course_class);
					tvCourseClass.setText(datas.get(position).getmClass());
					dialog.show();
				}
			});
			
		} else {
			// 缓存的rootView需要判断是否已经被加过parent，
			// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}

		return rootView;
	}
	
	private void downLoadCourse() {
		new CourseFetcher().sendRequest(new SharedPreferences(context).getStoredString("JWAccount"), new SharedPreferences(context).getStoredString("JWPassword"), "", new HttpReply() {
			
			@Override
			public void onFinished(String response) {
				new FileStorages(context).save(CourseFile, response);
				Message message = new Message();
				message.what = 0x01;
				message.obj = response;
				handler.sendMessage(message);
			}
			
			@Override
			public void onError(Exception e) {
				Log.e(TAG, "Fail to download CourseJson "+e);
				try {
					String response = new FileStorages(context).load(CourseFile);
					Message message = new Message();
					message.what = 0x01;
					message.obj = response;
					handler.sendMessage(message);
				} catch (IOException ioe) {
					Log.e(TAG, "Fail to get CourseJson "+ioe);
					handler.sendEmptyMessage(0x02);
				}
			}
		});
	}

	private void parseJSON(String jsonData,int todayWeek) {
		datas = new ArrayList<Course>();
		String[] weekName = new String[]{"周日","周一","周二","周三","周四","周五","周六"};
		try {
			JSONArray jsonArray = new JSONArray(jsonData);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if(jsonObject.getString("week").equals(weekName[todayWeek])){
					Course course = new Course(jsonObject.getString("week"),jsonObject.getString("time"), jsonObject.getString("course"), jsonObject.getString("classroom"), jsonObject.getString("number"), jsonObject.getString("teacher"), jsonObject.getString("class"));
					datas.add(course);
					leisure = false;
				} else {//今天没课的情况
					leisure = true;
				}
				
			}
		} catch (JSONException e) {
			Log.e(TAG, "Fail to parse JSON: "+e);
		}
	}
	
	private class CourseAdapter extends BaseAdapter{
		
		private Context context;
		private List<Course> datas;

		public CourseAdapter(Context context, List<Course> datas) {
			super();
			this.context = context;
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView==null){
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_course_list, null);
				viewHolder.mTime = (TextView) convertView.findViewById(R.id.tv_course_time);
				viewHolder.mCourse = (TextView) convertView.findViewById(R.id.tv_course_name);
				viewHolder.mClassroom = (TextView) convertView.findViewById(R.id.tv_course_location);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.mTime.setText(datas.get(position).getmTime());
			viewHolder.mCourse.setText(datas.get(position).getmCourse());
			viewHolder.mClassroom.setText(datas.get(position).getmClassroom());
			
			return convertView;
		}
		
		private class ViewHolder {
			public TextView mTime;
			public TextView mCourse;
			public TextView mClassroom;
		}

	}
	
	
}
