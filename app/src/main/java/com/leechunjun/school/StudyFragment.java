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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class StudyFragment extends Fragment{
	
	private ViewPager viewPager;
	private FragmentPagerAdapter adapter;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	
	private TextView tv_study_library;
	private TextView tv_study_edu;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.study, container, false);
		viewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
		Fragment study1Fragment = new Study1Fragment();
		Fragment study2Fragment = new Study2Fragment();
		fragments.add(study1Fragment);
		fragments.add(study2Fragment);
		adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), (ArrayList<Fragment>) fragments);
		viewPager.setAdapter(adapter);
		
		initViews(view);
		
		return view;
	}
	
	public void initViews(View parentView){
		tv_study_library = (TextView) parentView.findViewById(R.id.tv_study_library);
		tv_study_edu = (TextView) parentView.findViewById(R.id.tv_study_edu);
		tv_study_library.setOnClickListener(new MyOnClickListener());
		tv_study_edu.setOnClickListener(new MyOnClickListener());
		tv_study_library.setSelected(true);
		
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			tv_study_library.setSelected(false);
			tv_study_edu.setSelected(false);
			switch (v.getId()) {
			case R.id.tv_study_library:
				viewPager.setCurrentItem(0);
				tv_study_library.setSelected(true);
				break;
			case R.id.tv_study_edu:
				viewPager.setCurrentItem(1);
				tv_study_edu.setSelected(true);
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
			tv_study_library.setSelected(false);
			tv_study_edu.setSelected(false);
			int currentItem = viewPager.getCurrentItem();
			if(currentItem==0){
				tv_study_library.setSelected(true);
			}if(currentItem==1){
				tv_study_edu.setSelected(true);
			}
		}
		
	}
}
