package com.leechunjun.school.activity;

import java.io.IOException;

import com.leechunjun.school.R;
import com.leechunjun.school.data.History2Html;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class HistoryActivity extends AppCompatActivity {
	private static final String TAG = "HistoryActivity";

	private String historyActivityUrl;
	private WebView historyActivityWebView;
	private ProgressBar progressBar;

	private Dialog progressDialog;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		context = this;
		
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.load_dialog);
		progressDialog.setCancelable(true);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		historyActivityUrl = getIntent().getStringExtra("selectItemUrl");
		progressBar = (ProgressBar) findViewById(R.id.history_activity_progress_bar);
		progressBar.setMax(100);
		
		historyActivityWebView = (WebView) findViewById(R.id.history_activity_web_view);
		historyActivityWebView.getSettings().setJavaScriptEnabled(true);
		historyActivityWebView.getSettings().setUserAgentString("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 UBrowser/6.0.1471.813 Safari/537.36");
		
		historyActivityWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}
		});
		new FetchItemsTask().execute();
	}
	
	public void showProgressDialog() {
		progressDialog.show();
	}

	public void cancelProgressDialog() {
		progressDialog.dismiss();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, HistoryActivity.class);
		return intent;
	}
	
	private class FetchItemsTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result = "";
			try {
				result = new History2Html().parseArticle(historyActivityUrl);
			} catch (IOException e) {
				Log.e(TAG, "Ω‚ŒˆÕ¯“≥π ’œ£°"+e);
				result = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>¥ÌŒÛ“≥√Ê</title></head><body><p>ªÒ»°Õ¯“≥ ß∞‹£°</p></body></html>";
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			historyActivityWebView.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
			cancelProgressDialog();
		}
		
	}

}
