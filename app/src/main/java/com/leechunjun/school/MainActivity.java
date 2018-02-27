package com.leechunjun.school;

import com.leechunjun.school.util.BaseActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnCheckedChangeListener {

	private RadioGroup rg_tab_bar;
	private RadioButton rb_home;
	
	private Fragment homeFragment;
	private Fragment campusFragment;
	private Fragment studyFragment;
	private Fragment settingFragment;
	
	private FragmentManager fm = getSupportFragmentManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
		rg_tab_bar.setOnCheckedChangeListener(this);
		rb_home = (RadioButton) findViewById(R.id.rb_home);
		rb_home.setChecked(true);
		
		setSelect(0);
		
		if(!isNetworkAvailableAndConnected(this)){
			Toast.makeText(this, "ÎÞÍøÂçÁ¬½Ó", Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean isNetworkAvailableAndConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

		boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
		boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

		return isNetworkConnected;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_home:
			setSelect(0);
			break;
		case R.id.rb_campus:
			setSelect(1);
			break;
		case R.id.rb_study:
			setSelect(2);
			break;
		case R.id.rb_setting:
			setSelect(3);
			break;
		default:
			break;
		}
	}
	
	public void setSelect(int i){
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragment(transaction);
		switch (i) {
		case 0:
			if(homeFragment==null){
				homeFragment = new HomeFragment();
				transaction.add(R.id.id_content,homeFragment);
			}else{
				transaction.show(homeFragment);
			}
			break;
		case 1:
			if(campusFragment==null){
				campusFragment = new CampusFragment();
				transaction.add(R.id.id_content,campusFragment);
			}else{
				transaction.show(campusFragment);
			}
			break;
		case 2:
			if(studyFragment==null){
				studyFragment = new StudyFragment();
				transaction.add(R.id.id_content,studyFragment);
			}else{
				transaction.show(studyFragment);
			}
			break;
		case 3:
			if(settingFragment==null){
				settingFragment = new SettingFragment();
				transaction.add(R.id.id_content,settingFragment);
			}else{
				transaction.show(settingFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}

	public void hideFragment(FragmentTransaction transaction) {
		if(homeFragment!=null){
			transaction.hide(homeFragment);
		}
		if(campusFragment!=null){
			transaction.hide(campusFragment);
		}
		if(studyFragment!=null){
			transaction.hide(studyFragment);
		}
		if(settingFragment!=null){
			transaction.hide(settingFragment);
		}
	}

}
