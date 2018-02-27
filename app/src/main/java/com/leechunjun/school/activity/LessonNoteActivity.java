package com.leechunjun.school.activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leechunjun.school.R;
import com.leechunjun.school.bean.NoteItem;
import com.leechunjun.school.data.FileStorages;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LessonNoteActivity extends AppCompatActivity {
	private static final String TAG = "LessonNoteActivity";
	private static final String CacheFile = "LessonNoteFile";
	
	private Context context;
	private List<NoteItem> mList;
	private ListView listView;
	private NoteAdapter mAdapter;
	private ImageButton imageButtonAdd;
	@SuppressWarnings("unused")
	private ImageView imageViewNotice;
	
	private Gson mGson = new Gson();
	private Type mlistType = new TypeToken<List<NoteItem>>(){}.getType();
	
	private Dialog dialog = null;
	private View dialogSetting;
	private EditText evTitle;
	private EditText evContent;
	private ImageButton btnOK;
	private ImageButton btnCancel;
	
	private int mPosition;
	private boolean isAdd = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson_note);
		context = LessonNoteActivity.this;
		
		imageButtonAdd = (ImageButton) findViewById(R.id.lesson_add_note_image_button);
		imageViewNotice = (ImageView) findViewById(R.id.note_notice_image_view);
		listView = (ListView) findViewById(R.id.lesson_note_list_view);
		listView.setDivider(null);
		/*
		 * 设置单击item之后的自定义对话框
		 */
		dialogSetting = LayoutInflater.from(context).inflate(R.layout.dialog_lesson_note, null);
		btnOK = (ImageButton) dialogSetting.findViewById(R.id.btn_note_ok);
		btnCancel = (ImageButton) dialogSetting.findViewById(R.id.btn_note_cancel);
		evTitle = (EditText) dialogSetting.findViewById(R.id.dialog_note_add_title);
		evContent = (EditText) dialogSetting.findViewById(R.id.dialog_note_add_content);
		dialog = new Dialog(context, R.style.load_dialog);
		dialog.setContentView(dialogSetting);
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(false);
		
		mList = initData();
		setupAdapter();
		
		imageButtonAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.show();
				evTitle.setText(null);
				evContent.setText(null);
				isAdd = true;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mPosition = position;//当前查看第position项
				dialog.show();
				NoteItem item = mList.get(position);
				evTitle.setText(item.getTitle());
				evContent.setText(item.getContent());
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				mList.remove(position);
				mAdapter.DataSetReflash();
				String jsonNote = mGson.toJson(mList,mlistType);//mList序列化
				new FileStorages(context).save(CacheFile, jsonNote);
				Log.i(TAG, "save jsonNote to "+ CacheFile +": "+ jsonNote);
				return true;
			}
		});
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String title = evTitle.getText().toString();
				String content = evContent.getText().toString();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				String currentTime = df.format(new Date());// new Date()为获取当前系统时间
				if(!title.equals("")&&!content.equals("")){
					if(isAdd){//增加模式
						NoteItem item = new NoteItem();
						item.setTitle(title);
						item.setContent(content);
						item.setTime(currentTime);
						mList.add(0,item);
						mAdapter.DataSetReflash();
						String jsonNote = mGson.toJson(mList,mlistType);//mList序列化
						new FileStorages(context).save(CacheFile, jsonNote);
						Log.i(TAG, "save jsonNote to "+ CacheFile +": "+ jsonNote);
						dialog.dismiss();
						isAdd = false;
					} else {//查看模式
						mList.remove(mPosition);
						mAdapter.DataSetReflash();
						NoteItem item = new NoteItem();
						item.setTitle(title);
						item.setContent(content);
						item.setTime(currentTime);
						mList.add(0, item);
						mAdapter.DataSetReflash();
						String jsonNote = mGson.toJson(mList,mlistType);//mList序列化
						new FileStorages(context).save(CacheFile, jsonNote);
						Log.i(TAG, "save jsonNote to "+ CacheFile +": "+ jsonNote);
						dialog.dismiss();
					}
				} else {
					Toast.makeText(context, "标题或内容均不能为空", Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isAdd){
					isAdd = false;
				}
				dialog.dismiss();
			}
		});
	}
	
	
	private List<NoteItem> initData() {
		List<NoteItem> list = new ArrayList<NoteItem>();
		try {
			String jsonNote = new FileStorages(context).load(CacheFile);
			Log.i(TAG, "Read JSON From"+ CacheFile +": " + jsonNote);
			list = mGson.fromJson(jsonNote, mlistType);
		} catch (IOException e) {
			Log.e(TAG, "Fail to JSON From"+ CacheFile +": " + e);
		}
		return list;
	}


	private void setupAdapter() {
		mAdapter = new NoteAdapter(context, mList);
//		if(mList.size()==0){
//			imageViewNotice.setImageResource(R.drawable.empty_activity);
//			listView.setEmptyView(imageViewNotice);
//		} else {
			listView.setAdapter(mAdapter);
//		}
	}
	


	private class NoteAdapter extends BaseAdapter{
		private Context context;
		private List<NoteItem> list;
		
		
		public NoteAdapter(Context context, List<NoteItem> list) {
			super();
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public NoteItem getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public void DataSetReflash(){
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NoteHolder noteHolder = null;
			if(convertView==null){
				noteHolder = new NoteHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_note, null);
				noteHolder.noteTitle = (TextView) convertView.findViewById(R.id.item_note_title);
				noteHolder.noteTime = (TextView) convertView.findViewById(R.id.item_note_time);
				convertView.setTag(noteHolder);
			} else {
				noteHolder = (NoteHolder) convertView.getTag();
			}
			
			noteHolder.noteTitle.setText(list.get(position).getTitle());
			noteHolder.noteTime.setText(list.get(position).getTime());
			
			return convertView;
		}
		
		private class NoteHolder{
			public TextView noteTitle;
			public TextView noteTime;
		}
		
	}

	public static Intent newIntent(Context context){
		Intent intent = new Intent(context,LessonNoteActivity.class);
		return intent;
	}
}
