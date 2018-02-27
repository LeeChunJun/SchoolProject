package com.leechunjun.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leechunjun.school.activity.CalenderActivity;
import com.leechunjun.school.activity.FinalExamActivity;
import com.leechunjun.school.activity.GetCETActivity;
import com.leechunjun.school.activity.GetGradeActivity;
import com.leechunjun.school.activity.LessonNoteActivity;
import com.leechunjun.school.data.SharedPreferences;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Study2Fragment extends Fragment {
	// private static final String TAG = "Study2Fragment";

	private Context context;
	private View rootView;// ����Fragment view

	private GridView gview;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;
	// ͼƬ��װΪһ������
	// R.drawable.timer,"����ʱ",
	private int[] icon = { R.drawable.school_calendar, R.drawable.get_cet, R.drawable.lesson_note,
			R.drawable.final_exam, R.drawable.get_grade };
	private String[] iconName = { "У��", "������", "���ñʼ�", "���԰���", "��ĩ�ɼ�" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.study2, container, false);
			context = rootView.getContext();
			gview = (GridView) rootView.findViewById(R.id.study_gview);
			// �½�List
			data_list = new ArrayList<Map<String, Object>>();
			// ��ȡ����
			getData();
			// �½�������
			String[] from = { "image", "text" };
			int[] to = { R.id.grid_item_image, R.id.grid_item_text };
			sim_adapter = new SimpleAdapter(rootView.getContext(), data_list, R.layout.grid_item, from, to);
			// ����������
			gview.setAdapter(sim_adapter);
			gview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (position == 0) {
						Intent intent = CalenderActivity.newIntent(context);
						startActivity(intent);
					}
					
					if (position == 1) {
						Intent intent = GetCETActivity.newIntent(view.getContext());
						startActivity(intent);
					}
					
					if (position == 2) {
						Intent intent = LessonNoteActivity.newIntent(view.getContext());
						startActivity(intent);
					}
					
					if (position == 3) {
						String jwAcc = new SharedPreferences(context).getStoredString("JWAccount");
						if(jwAcc!=null){
							Intent intent = FinalExamActivity.newIntent(context);
							startActivity(intent);
						} else {
							Toast.makeText(view.getContext(), "������������˺�", Toast.LENGTH_SHORT).show();
						}
					}
					
					if (position == 4) {
						String jwAcc = new SharedPreferences(context).getStoredString("JWAccount");
						if(jwAcc!=null){
							Intent intent = GetGradeActivity.newIntent(context);
							startActivity(intent);
						} else {
							Toast.makeText(view.getContext(), "������������˺�", Toast.LENGTH_SHORT).show();
						}
					}
					

				}
			});
		} else {
			// �����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent��
			// �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}

		return rootView;
	}
	
	public List<Map<String, Object>> getData() {
		// cion��iconName�ĳ�������ͬ�ģ�������ѡ��һ������
		for (int i = 0; i < icon.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[i]);
			map.put("text", iconName[i]);
			data_list.add(map);
		}

		return data_list;
	}
}
