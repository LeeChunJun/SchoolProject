package com.leechunjun.school;

import com.leechunjun.school.data.AccountCheck;
import com.leechunjun.school.data.SharedPreferences;
import com.leechunjun.school.util.ActivityCollector;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingFragment extends Fragment implements OnClickListener {
	private static final String TAG = "SettingFragment";

	private Context context;
	private View rootView;// 缓存Fragment view
	private LinearLayout exitApp;
	private LinearLayout JWAccount;
	private LinearLayout TUAccount;

	private Dialog dialog = null;
	private ImageButton btnOK;
	private ImageButton btnCancel;
	private TextView tvTitle;
	private EditText evAccount;
	private EditText evPWD;
	private int accountType = 0;// 0=>教务账号,1=>图书馆账号
	private View dialogSetting;
	
	private TextView jwAccountTV;
	private TextView tuAccountTV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.setting, container, false);
			context = rootView.getContext();
			exitApp = (LinearLayout) rootView.findViewById(R.id.ll_exit_app);
			JWAccount = (LinearLayout) rootView.findViewById(R.id.ll_jw_count);
			TUAccount = (LinearLayout) rootView.findViewById(R.id.ll_tu_count);
			jwAccountTV = (TextView) rootView.findViewById(R.id.tv_jw_account);
			tuAccountTV = (TextView) rootView.findViewById(R.id.tv_tu_account);
			
			String jwAcc = new SharedPreferences(context).getStoredString("JWAccount");
			if(jwAcc!=null){
				jwAccountTV.setText(jwAcc);
			}
			String tuAcc = new SharedPreferences(context).getStoredString("TUAccount");
			if(tuAcc!=null){
				tuAccountTV.setText(tuAcc);
			}
			
			exitApp.setOnClickListener(this);
			JWAccount.setOnClickListener(this);
			TUAccount.setOnClickListener(this);

			/*
			 * 设置单击item之后的自定义对话框
			 */
			dialogSetting = LayoutInflater.from(context).inflate(R.layout.dialog_account, null);
			tvTitle = (TextView) dialogSetting.findViewById(R.id.dialog_account_title);
			evAccount = (EditText) dialogSetting.findViewById(R.id.ev_account);
			evPWD = (EditText) dialogSetting.findViewById(R.id.ev_pwd);
			btnOK = (ImageButton) dialogSetting.findViewById(R.id.btn_account_ok);
			btnCancel = (ImageButton) dialogSetting.findViewById(R.id.btn_account_cancel);

			dialog = new Dialog(context, R.style.load_dialog);
			dialog.setContentView(dialogSetting);
			dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			dialog.setCancelable(false);

			btnOK.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String account = evAccount.getText().toString();
					String pwd = evPWD.getText().toString();
					if(!account.equals("")&&!pwd.equals("")){
						new CheckAccount().execute(""+accountType,account,pwd);
						dialog.dismiss();
					} else {
						Toast.makeText(context, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
					}
				}
			});
			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}

		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_exit_app:
			Log.i(TAG, "exitApp");
			ActivityCollector.finishAll();
			break;
		case R.id.ll_jw_count:
			Log.i(TAG, "JWAccount");
			tvTitle.setText("教务账号");
			accountType = 0;
			dialog.show();
			evAccount.setText(null);
			evPWD.setText(null);
			break;
		case R.id.ll_tu_count:
			Log.i(TAG, "TUAccount");
			tvTitle.setText("图书馆账号");
			accountType = 1;
			dialog.show();
			evAccount.setText(null);
			evPWD.setText(null);
			break;
		default:
			break;
		}
	}

	private class CheckAccount extends AsyncTask<String, Void, Boolean>{
		private boolean result;
		
		@Override
		protected Boolean doInBackground(String... params) {
			for (String parameter : params) {
				Log.i(TAG, "Receiver parameter: "+parameter);
			}
			if(params[0].equals("0")){
				result = AccountCheck.checkJWAccount(params[1], params[2]);
			} else {
				result = AccountCheck.checkTUAccount(params[1], params[2]);
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result!=true){
				Toast.makeText(context, "账号或密码输入错误", Toast.LENGTH_SHORT).show();
			} else {
				if(accountType==0){
					new SharedPreferences(context).setStoredString("JWAccount", evAccount.getText().toString());
					new SharedPreferences(context).setStoredString("JWPassword", evPWD.getText().toString());
					jwAccountTV.setText(evAccount.getText().toString());
				} else {
					new SharedPreferences(context).setStoredString("TUAccount", evAccount.getText().toString());
					new SharedPreferences(context).setStoredString("TUPassword", evPWD.getText().toString());
					tuAccountTV.setText(evAccount.getText().toString());
				}
			}
		}

		
	}
}
