package com.leechunjun.school.activity;

import java.util.ArrayList;
import java.util.List;

import com.leechunjun.school.R;
import com.leechunjun.school.bean.Exam;
import com.leechunjun.school.data.ExamFetcher;
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
import android.widget.TextView;
import android.widget.Toast;

public class FinalExamActivity extends AppCompatActivity {
//	private static final String TAG = "FinalExamActivity";

	private RecyclerView recyclerView;
	private Context context;
	private List<Exam> mExamList = new ArrayList<Exam>();
	private String netReason;// 链接网站回复
	private int netCode;// 链接网站回复
	
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final_exam);
		context = this;

		recyclerView = (RecyclerView) findViewById(R.id.recycler_view_final_exam);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.load_dialog);
		progressDialog.setCancelable(true);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		new FetchItemsTask().execute();
	}
	
	private void setupAdapter() {
		recyclerView.setAdapter(new ExamAdapter(mExamList));
	}
	
	public void showProgressDialog() {
		progressDialog.show();
	}

	public void cancelProgressDialog() {
		progressDialog.dismiss();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, FinalExamActivity.class);
		return intent;
	}
	
	private class ExamHolder extends RecyclerView.ViewHolder{
		private TextView examName;
		private TextView examTime;
		private TextView examClass;
		private TextView examSeat;

		public ExamHolder(View itemView) {
			super(itemView);
			
			examName = (TextView) itemView.findViewById(R.id.tv_study_exam_name);
			examTime = (TextView) itemView.findViewById(R.id.tv_study_exam_time);
			examClass = (TextView) itemView.findViewById(R.id.tv_study_exam_class);
			examSeat = (TextView) itemView.findViewById(R.id.tv_study_exam_seat);
		}
		
		public void bindExam(Exam exam){
			examName.setText(exam.getExamName());
			examTime.setText("时间："+exam.getExamTime());
			examClass.setText("考场："+exam.getExamClass());
			examSeat.setText("座位："+exam.getExamSeat());
		}
		
	}
	
	private class ExamAdapter extends RecyclerView.Adapter<ExamHolder>{
		private List<Exam> examList;

		@Override
		public int getItemCount() {
			return examList.size();
		}
		
		public ExamAdapter(List<Exam> examList) {
			this.examList = examList;
		}

		@Override
		public void onBindViewHolder(ExamHolder examHolder, int position) {
			Exam exam = examList.get(position);
			examHolder.bindExam(exam);
		}

		@Override
		public ExamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			View view = layoutInflater.inflate(R.layout.item_exam, parent, false);
			return new ExamHolder(view);
		}
	}

	private class FetchItemsTask extends AsyncTask<Void, Void, List<Exam>> implements NetResponse {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}
		
		@Override
		protected List<Exam> doInBackground(Void... params) {
			String id = new SharedPreferences(context).getStoredString("JWAccount");
			String password = new SharedPreferences(context).getStoredString("JWPassword");
			return new ExamFetcher(context, id, password, "", this).fetchItem();
		}
		
		@Override
		protected void onPostExecute(List<Exam> result) {
			mExamList = result;
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
