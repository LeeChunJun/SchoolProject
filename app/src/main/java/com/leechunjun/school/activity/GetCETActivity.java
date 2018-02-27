package com.leechunjun.school.activity;

import com.leechunjun.school.R;
import com.leechunjun.school.data.CET2Html;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GetCETActivity extends AppCompatActivity {
	private static final String TAG = "GetCETActivity";

	private WebView cetActivityWebView;
	private ProgressBar progressBar;

	private Context context;
	private Dialog dialog = null;
	private ImageButton btnOK;
	private ImageButton btnCancel;
	private TextView tvTitle;
	private EditText evAccount;
	private EditText evName;
	private View dialogSetting;
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_cet);
		context = this;

		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.load_dialog);
		progressDialog.setCancelable(true);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		progressBar = (ProgressBar) findViewById(R.id.cet_activity_progress_bar);
		progressBar.setMax(100);

		cetActivityWebView = (WebView) findViewById(R.id.cet_activity_web_view);
		cetActivityWebView.getSettings().setJavaScriptEnabled(true);
		cetActivityWebView.getSettings().setUserAgentString(
				"User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 UBrowser/6.0.1471.813 Safari/537.36");
		cetActivityWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}
		});

		dialogSetting = LayoutInflater.from(context).inflate(R.layout.dialog_account, null);
		tvTitle = (TextView) dialogSetting.findViewById(R.id.dialog_account_title);
		evAccount = (EditText) dialogSetting.findViewById(R.id.ev_account);
		evName = (EditText) dialogSetting.findViewById(R.id.ev_pwd);
		btnOK = (ImageButton) dialogSetting.findViewById(R.id.btn_account_ok);
		btnCancel = (ImageButton) dialogSetting.findViewById(R.id.btn_account_cancel);
		tvTitle.setText("四六级成绩查询");
		evAccount.setHint("请输入15位笔试或口试准考证号");
		evName.setHint("姓名超过3个字，可只输入前3个");
		evName.setInputType(InputType.TYPE_CLASS_TEXT);
		dialog = new Dialog(context, R.style.load_dialog);
		dialog.setContentView(dialogSetting);
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(false);
		dialog.show();
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String account = evAccount.getText().toString();
				String name = evName.getText().toString();
				if (!account.equals("") && !name.equals("")) {
					new FetchCET().execute(account, name);
					dialog.dismiss();
				} else {
					Toast.makeText(context, "准考证号或姓名不能为空", Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	public void showProgressDialog() {
		progressDialog.show();
	}

	public void cancelProgressDialog() {
		progressDialog.dismiss();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, GetCETActivity.class);
		return intent;
	}

	private class FetchCET extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			try {
				result = new CET2Html(context).parseGrade(params[0], params[1]);
				if(result.equals("")){
					result = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>错误页面</title></head><body><p>准考证或密码错误！</p></body></html>";
				}
			} catch (Exception e) {
				Log.e(TAG, "解析网页故障！" + e);
				result = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>错误页面</title></head><body><p>获取网页失败！</p></body></html>";
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			cetActivityWebView.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
			cancelProgressDialog();
		}
	}

}
