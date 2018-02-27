package com.leechunjun.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leechunjun.school.activity.DeliveryActivity;
import com.leechunjun.school.activity.WeatherActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class Campus2Fragment extends Fragment {
//	private static final String TAG = "Campus2Fragment";

	private Context context;
	private View rootView;// 缓存Fragment view
	
	private GridView gview;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;
	// 图片封装为一个数组
	private int[] icon = { R.drawable.weather, R.drawable.delivery};
	private String[] iconName = { "天气", "快递"};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.campus2, container, false);
			context = rootView.getContext();
			gview = (GridView) rootView.findViewById(R.id.life_gview);
			// 新建List
			data_list = new ArrayList<Map<String, Object>>();
			// 获取数据
			getData();
			// 新建适配器
			String[] from = { "image", "text" };
			int[] to = { R.id.grid_item_image, R.id.grid_item_text };
			sim_adapter = new SimpleAdapter(rootView.getContext(), data_list,
					R.layout.grid_item, from, to);
			// 配置适配器
			gview.setAdapter(sim_adapter);
			gview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (position == 0) {
						Intent intent = WeatherActivity.newIntent(context);
						startActivity(intent);
					} else if (position == 1) {
						Intent intent = DeliveryActivity.newIntent(context);
						startActivity(intent);
					}
				}
			});
			
		} else {
			// 缓存的rootView需要判断是否已经被加过parent，
			// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		
		return rootView;
	}
	
	public List<Map<String, Object>> getData() {
		// cion和iconName的长度是相同的，这里任选其一都可以
		for (int i = 0; i < icon.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[i]);
			map.put("text", iconName[i]);
			data_list.add(map);
		}
		return data_list;
	}
}
