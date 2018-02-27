package com.leechunjun.school.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leechunjun.school.R;
import com.leechunjun.school.bean.Grade;
import com.leechunjun.school.data.GradeFetcher;
import com.leechunjun.school.data.NetResponse;
import com.leechunjun.school.data.SharedPreferences;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GetGradeActivity extends AppCompatActivity {
//	private static final String TAG = "GetGradeActivity";
	
	private RecyclerView recyclerView;
	private Context context;
	private List<Grade> mGradeList = new ArrayList<Grade>();
	private String netReason;// 链接网站回复
	private int netCode;// 链接网站回复
	
	private Dialog progressDialog;
	
	private Dialog dialog;
	private ImageButton btnOK;
	private TextView tvGradeTime;
	private TextView tvGradeNo;
	private TextView tvGradeName;
	private TextView tvGradePoint;
	private TextView tvGradeGrade;
	private TextView tvGradeAS;
	private TextView tvGradeSS;
	private View dialogGrade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_grade);
		context = this;
		
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view_study_grade);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.load_dialog);
		progressDialog.setCancelable(true);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		new FetchItemsTask().execute();
		/*
		 * 设置单击item之后的自定义对话框
		 */
		dialog = new Dialog(context, R.style.load_dialog);
		dialogGrade = LayoutInflater.from(context).inflate(R.layout.dialog_grade, null);
        dialog.setContentView(dialogGrade);
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 	
		dialog.setCancelable(false);
		btnOK = (ImageButton) dialogGrade.findViewById(R.id.btn_grade_ok);
        btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
	private void setupAdapter() {
		recyclerView.setAdapter(new GradeAdapter(mGradeList));
	}
	
	public void showProgressDialog() {
		progressDialog.show();
	}

	public void cancelProgressDialog() {
		progressDialog.dismiss();
	}
	
	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, GetGradeActivity.class);
		return intent;
	}
	
	private class GradeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		private TextView tvCourse;
		private TextView tvGrade;
		private Grade grade;
		
		public GradeHolder(View itemView) {
			super(itemView);
			
			tvCourse = (TextView) itemView.findViewById(R.id.tv_study_course_name);
			tvGrade = (TextView) itemView.findViewById(R.id.tv_study_course_grade);
			itemView.setOnClickListener(this);
		}
		
		public void bindGrade(Grade grade) {
			this.grade = grade;
			tvCourse.setText(grade.getCourseName());
			tvGrade.setText(grade.getExamScore());
		}

		@Override
		public void onClick(View v) {
			tvGradeTime = (TextView) dialogGrade.findViewById(R.id.tv_grade_time);
			tvGradeTime.setText(grade.getExamTime());
			tvGradeNo = (TextView) dialogGrade.findViewById(R.id.tv_grade_no);
			tvGradeNo.setText(grade.getCourseNo());
			tvGradeName = (TextView) dialogGrade.findViewById(R.id.tv_grade_course);
			tvGradeName.setText(grade.getCourseName());
			tvGradePoint = (TextView) dialogGrade.findViewById(R.id.tv_grade_point);
			tvGradePoint.setText(grade.getExamPoint());
			tvGradeGrade = (TextView) dialogGrade.findViewById(R.id.tv_grade_grade);
			tvGradeGrade.setText(grade.getExamScore());
			tvGradeAS = (TextView) dialogGrade.findViewById(R.id.tv_grade_AS);
			tvGradeAS.setText(grade.getExamAgainScore());
			tvGradeSS = (TextView) dialogGrade.findViewById(R.id.tv_grade_SS);
			tvGradeSS.setText(grade.getExamStandardScore());
			dialog.show();
		}
		
	}
	
	private class GradeAdapter extends RecyclerView.Adapter<GradeHolder> {
		private List<Grade> gradeList;
		
		public GradeAdapter(List<Grade> gradeList) {
			super();
			Collections.reverse(gradeList);
			this.gradeList = gradeList;
		}

		@Override
		public int getItemCount() {
			return gradeList.size();
		}

		@Override
		public void onBindViewHolder(GradeHolder gradeHolder, int position) {
			Grade grade = gradeList.get(position);
			gradeHolder.bindGrade(grade);
		}

		@Override
		public GradeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			View view = layoutInflater.inflate(R.layout.item_grade, parent, false);
			return new GradeHolder(view);
		}
		
	}

	private class FetchItemsTask extends AsyncTask<Void, Void, List<Grade>> implements NetResponse {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}
		
		@Override
		protected List<Grade> doInBackground(Void... params) {
			String id = new SharedPreferences(context).getStoredString("JWAccount");
			String password = new SharedPreferences(context).getStoredString("JWPassword");
			return new GradeFetcher(context, id, password, "", this).fetchItem();
		}
		
		@Override
		protected void onPostExecute(List<Grade> result) {
			mGradeList = result;
			if (netCode != 0) {
				Toast.makeText(context, netReason, Toast.LENGTH_SHORT).show();
				netCode = 0;// 改变netCode值，以免一直报错误的原因
			}
			setupAdapter();
			
			cancelProgressDialog();
		}
		
		@Override
		public void onFinished(String response, int code) {
			netReason = response;
			netCode = code;
		}
	}

}
