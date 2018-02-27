package com.leechunjun.school;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.leechunjun.school.activity.TopicActivity;
import com.leechunjun.school.bean.TopicItem;
import com.leechunjun.school.data.FileStorages;
import com.leechunjun.school.data.NetResponse;
import com.leechunjun.school.data.SharedPreferences;
import com.leechunjun.school.data.TopicFetcher;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Campus1Fragment extends Fragment implements OnClickListener{
	private static final String TAG = "Campus1Fragment";
	private static final String CacheFile = "TopicCacheFile";
	
	private Context context;
	private View rootView;// 缓存Fragment view
	private RecyclerView recyclerView;
	private List<TopicItem> mItems = new ArrayList<TopicItem>();
	private String netReason;// 链接网站回复
	private int netCode;// 链接网站回复

	private Dialog progressDialog;
	private ImageView ibshowProgressDialog;
	private boolean hasInitData = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.campus1, container, false);
			context = rootView.getContext();
			recyclerView = (RecyclerView) rootView.findViewById(R.id.campus_topic_recycler_view);
			recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
			
			ibshowProgressDialog = (ImageView) rootView.findViewById(R.id.campus_topic_ib_showProgressDialog);
			ibshowProgressDialog.setOnClickListener(this);
			
			progressDialog = new Dialog(context, R.style.progress_dialog);
			progressDialog.setContentView(R.layout.load_dialog);
			progressDialog.setCancelable(true);
			progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

			setupAdapter();
			//判断缓存里面是否存在json文件
			try {
				new FileStorages(context).load(CacheFile);
				hasInitData = true;
			} catch (IOException e) {
				hasInitData = false;
			}
			if(!hasInitData){
				new FetchItemsTask().execute();
			}
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

	private void setupAdapter() {
		if (mItems.size() == 0) {
			try {
				new TopicFetcher().fetchItemsFromCache(mItems);
			} catch (Exception e) {
				Log.e(TAG, "Failed to load Data", e);
			}
		}
		recyclerView.setAdapter(new TopicAdapter(mItems));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.campus_topic_ib_showProgressDialog) {
			new FetchItemsTask().execute();
		}
	}
	
	@Override
	public void onDestroyView() {
		if(new SharedPreferences(context).getStoredString(CacheFile)!=null){
			new SharedPreferences(context).removeStoredString(CacheFile);
		}
		super.onDestroyView();
	}
	
	public void showProgressDialog() {
		progressDialog.show();
	}

	public void cancelProgressDialog() {
		progressDialog.dismiss();
	}
	
	private class TopicHolder extends RecyclerView.ViewHolder implements OnClickListener{
		private TextView title;
		private TextView time;
		private String selectItemUrl;// 选定一项时候记录其文章地址
		
		public TopicHolder(View itemView) {
			super(itemView);
			
			title = (TextView) itemView.findViewById(R.id.item_topic_title);
			time = (TextView) itemView.findViewById(R.id.item_topic_time);
			itemView.setOnClickListener(this);
		}
		
		public void bindTopicItem(TopicItem topicItem){
			title.setText(topicItem.getTitle());
			time.setText(topicItem.getTime());
			selectItemUrl = topicItem.getUrl();
		}

		@Override
		public void onClick(View v) {
			Intent intent = TopicActivity.newIntent(context);
			intent.putExtra("selectItemUrl", selectItemUrl);
			startActivity(intent);
		}
		
	}
	
	private class TopicAdapter extends RecyclerView.Adapter<TopicHolder>{
		private List<TopicItem> topicItems;
		
		public TopicAdapter(List<TopicItem> topicItems) {
			super();
			this.topicItems = topicItems;
		}

		@Override
		public int getItemCount() {
			return topicItems.size();
		}

		@Override
		public void onBindViewHolder(TopicHolder topicHolder, int position) {
			TopicItem topicItem = topicItems.get(position);
			topicHolder.bindTopicItem(topicItem);
		}

		@Override
		public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.item_topic, parent, false);
			return new TopicHolder(view);
		}
		
	}
	
	private class FetchItemsTask extends AsyncTask<Void, Void, List<TopicItem>> implements NetResponse {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(hasInitData){
				showProgressDialog();
			}
		}
		
		@Override
		protected List<TopicItem> doInBackground(Void... params) {
			return new TopicFetcher(this, context).fetchItems();
		}
		
		@Override
		protected void onPostExecute(List<TopicItem> items) {
			mItems = items;
			if (netCode != 0) {
				Toast.makeText(context, netReason, Toast.LENGTH_SHORT).show();
				netCode = 0;// 改变netCode值，以免一直报错误的原因
			}
			setupAdapter();

			if(hasInitData){
				cancelProgressDialog();
			} else {
				hasInitData = true;
			}
		}
		
		@Override
		public void onFinished(String response, int code) {
			netReason = response;
			netCode = code;
		}
		
	}
}
