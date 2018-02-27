package com.leechunjun.school;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<Fragment> fragments;
	
	
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

}
