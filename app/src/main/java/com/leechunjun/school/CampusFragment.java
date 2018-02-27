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

public class CampusFragment extends Fragment{
	
	private ViewPager viewPager;
	private FragmentPagerAdapter adapter;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	
	private TextView tv_campus_topics;
	private TextView tv_campus_life;
	private TextView tv_campus_news;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.campus, container, false);
		viewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
		Fragment campus1Fragment = new Campus1Fragment();
		Fragment campus2Fragment = new Campus2Fragment();
		Fragment campus3Fragment = new Campus3Fragment();
		fragments.add(campus1Fragment);
		fragments.add(campus2Fragment);
		fragments.add(campus3Fragment);
		adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), (ArrayList<Fragment>) fragments);
		viewPager.setAdapter(adapter);
		
		initViews(view);
		
		
		return view;
	}
	
	public void initViews(View parentView){
		tv_campus_topics = (TextView) parentView.findViewById(R.id.tv_campus_topics);
		tv_campus_life = (TextView) parentView.findViewById(R.id.tv_campus_life);
		tv_campus_news = (TextView) parentView.findViewById(R.id.tv_campus_news);
		
		tv_campus_topics.setOnClickListener(new MyOnClickListener());
		tv_campus_life.setOnClickListener(new MyOnClickListener());
		tv_campus_news.setOnClickListener(new MyOnClickListener());
		tv_campus_topics.setSelected(true);
		
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			tv_campus_topics.setSelected(false);
			tv_campus_life.setSelected(false);
			tv_campus_news.setSelected(false);
			switch (v.getId()) {
			case R.id.tv_campus_topics:
				viewPager.setCurrentItem(0);
				tv_campus_topics.setSelected(true);
				break;
			case R.id.tv_campus_life:
				viewPager.setCurrentItem(1);
				tv_campus_life.setSelected(true);
				break;
			case R.id.tv_campus_news:
				viewPager.setCurrentItem(2);
				tv_campus_news.setSelected(true);
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
			tv_campus_topics.setSelected(false);
			tv_campus_life.setSelected(false);
			tv_campus_news.setSelected(false);
			int currentItem = viewPager.getCurrentItem();
			if(currentItem==0){
				tv_campus_topics.setSelected(true);
			}if(currentItem==1){
				tv_campus_life.setSelected(true);
			}if(currentItem==2){
				tv_campus_news.setSelected(true);
			}
		}
		
	}
}
