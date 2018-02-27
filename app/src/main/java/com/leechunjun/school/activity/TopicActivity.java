package com.leechunjun.school.activity;

import java.io.IOException;

import com.leechunjun.school.R;
import com.leechunjun.school.data.Topic2Html;

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

public class TopicActivity extends AppCompatActivity {
	private static final String TAG = "TopicActivity";
	
	private String topicActivityUrl;
	private WebView topicActivityWebView;
	private ProgressBar progressBar;
	
	private Dialog progressDialog;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);
		context = this;
		
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.load_dialog);
		progressDialog.setCancelable(true);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		topicActivityUrl = getIntent().getStringExtra("selectItemUrl");
		progressBar = (ProgressBar) findViewById(R.id.topic_activity_progress_bar);
		progressBar.setMax(100);
		
		topicActivityWebView = (WebView) findViewById(R.id.topic_activity_web_view);
		topicActivityWebView.getSettings().setJavaScriptEnabled(true);
		topicActivityWebView.getSettings().setUserAgentString("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 UBrowser/6.0.1471.813 Safari/537.36");
//		topicActivityWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放
//		topicActivityWebView.getSettings().setLoadWithOverviewMode(true);
		
//		topicActivityWebView.setWebChromeClient(new WebChromeClient(){
//			@Override
//			public void onProgressChanged(WebView view, int newProgress) {
//				super.onProgressChanged(view, newProgress);
//				if(newProgress==100){
//					progressBar.setVisibility(View.GONE);
//				} else {
//					progressBar.setVisibility(View.VISIBLE);
//					progressBar.setProgress(newProgress);
//				}
//			}
//			
//			@Override
//			public void onReceivedTitle(WebView view, String title) {
//				super.onReceivedTitle(view, title);
//				getSupportActionBar().setSubtitle(title);
//			}
//		});
		
		topicActivityWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}
		});
//		topicActivityWebView.loadUrl(topicActivityUrl);
		new FetchItemsTask().execute();
	}
	
	public void showProgressDialog() {
		progressDialog.show();
	}

	public void cancelProgressDialog() {
		progressDialog.dismiss();
	}

	public static Intent newIntent(Context context){
		Intent intent = new Intent(context,TopicActivity.class);
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
				result = new Topic2Html().parseArticle(topicActivityUrl);
			} catch (IOException e) {
				Log.e(TAG, "解析网页故障！"+e);
				result = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>错误页面</title></head><body><p>获取网页失败！</p></body></html>";
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			topicActivityWebView.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
			cancelProgressDialog();
		}
		
	}
}
