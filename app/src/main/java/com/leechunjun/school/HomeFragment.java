package com.leechunjun.school;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment{
	private ViewPager viewPager;
	private FragmentPagerAdapter adapter;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	
	private TextView tv_home_course;
	private TextView tv_home_history;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home, container, false);
		
		viewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
		Fragment home1Fragment = new Home1Fragment();
		Fragment home2Fragment = new Home2Fragment();
		fragments.add(home1Fragment);
		fragments.add(home2Fragment);
		adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), (ArrayList<Fragment>) fragments);
		viewPager.setAdapter(adapter);
		
		initViews(view);
		
		return view;
	}
	
	public void initViews(View parentView){
		tv_home_course = (TextView) parentView.findViewById(R.id.tv_home_course);
		tv_home_history = (TextView) parentView.findViewById(R.id.tv_home_history);
		tv_home_course.setOnClickListener(new MyOnClickListener());
		tv_home_history.setOnClickListener(new MyOnClickListener());
		tv_home_course.setSelected(true);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			tv_home_course.setSelected(false);
			tv_home_history.setSelected(false);
			switch (v.getId()) {
			case R.id.tv_home_course:
				viewPager.setCurrentItem(0);
				tv_home_course.setSelected(true);
				break;
			case R.id.tv_home_history:
				viewPager.setCurrentItem(1);
				tv_home_history.setSelected(true);
				break;
			default:
				break;
			}
		}

	}
	
	
	
	private class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			tv_home_course.setSelected(false);
			tv_home_history.setSelected(false);
			int currentItem = viewPager.getCurrentItem();
			if(currentItem==0){
				tv_home_course.setSelected(true);
			}if(currentItem==1){
				tv_home_history.setSelected(true);
			}
		}
		
	}
}
