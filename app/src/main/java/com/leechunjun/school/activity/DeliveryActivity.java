package com.leechunjun.school.activity;

import com.leechunjun.school.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DeliveryActivity extends Activity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_delivery);

		webView = (WebView) findViewById(R.id.delivery_webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		webView.loadUrl("http://m.kuaidi100.com/index_all.html");
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, DeliveryActivity.class);
		return intent;
	}
}
