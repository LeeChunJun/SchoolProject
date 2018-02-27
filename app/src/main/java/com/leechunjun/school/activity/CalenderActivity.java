package com.leechunjun.school.activity;

import com.leechunjun.school.data.CalenderFetcher;
import com.leechunjun.school.data.NetResponse;
import com.squareup.picasso.Picasso;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

public class CalenderActivity extends AppCompatActivity {
//	private static final String TAG = "Campus3Fragment";

	private Context context;
	private ImageView calImageView;
	private String netReason;// ������վ�ظ�
	private int netCode;// ������վ�ظ�
	private Dialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calender);
		context = this;
		calImageView = (ImageView) findViewById(R.id.image_view_cal);
		
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.load_dialog);
		progressDialog.setCancelable(true);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	
		new FetchItemsTask().execute();
	}
	
	public void showProgressDialog() {
		progressDialog.show();
	}

	public void cancelProgressDialog() {
		progressDialog.dismiss();
	}
	
	public static Intent newIntent(Context context){
		Intent intent = new Intent(context,CalenderActivity.class);
		return intent;
	}
	
	private class FetchItemsTask extends AsyncTask<Void, Void, String> implements NetResponse{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			return new CalenderFetcher(context,this).fetchItems();
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (netCode != 0) {
				Toast.makeText(context, netReason, Toast.LENGTH_SHORT).show();
				netCode = 0;// �ı�netCodeֵ������һֱ�������ԭ��
			}
			if(!result.equals("")){
				Picasso.with(context).load(result).placeholder(R.drawable.ic_item_space).into(calImageView);
			}
			cancelProgressDialog();
		}

		@Override
		public void onFinished(String response, int code) {
			netReason = response;
			netCode = code;
		}
		
	}

}
