package com.leechunjun.school.data;

import com.leechunjun.school.util.MyApplication;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPreferences {
	private Context context;

	public SharedPreferences(Context context) {
		super();
		this.context = MyApplication.getContext();
	}

	public String getStoredString(String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
	}

	public void setStoredString(String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
	}
	
	public void removeStoredString(String key) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).apply();
	}

}
