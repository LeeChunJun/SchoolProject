package com.leechunjun.school;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.leechunjun.school.activity.NewsActivity;
import com.leechunjun.school.bean.NewsItem;
import com.leechunjun.school.data.FileStorages;
import com.leechunjun.school.data.NetResponse;
import com.leechunjun.school.data.NewsFetcher;
import com.leechunjun.school.data.SharedPreferences;
import com.squareup.picasso.Picasso;

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

public class Campus3Fragment extends Fragment implements OnClickListener {
	private static final String TAG = "Campus3Fragment";
	private static final String CacheFile = "NewsCacheFile";

	private Context context;
	private View rootView;// 缓存Fragment view
	private RecyclerView recyclerView;
	private List<NewsItem> mItems = new ArrayList<NewsItem>();
	private String netReason;// 链接网站回复
	private int netCode;// 链接网站回复

	private Dialog progressDialog;
	private ImageView ibshowProgressDialog;
	private boolean hasInitData = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.campus3, container, false);
			context = rootView.getContext();
			recyclerView = (RecyclerView) rootView.findViewById(R.id.daily_news_recycler_view);
			recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

			ibshowProgressDialog = (ImageView) rootView.findViewById(R.id.ib_showProgressDialog);
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_showProgressDialog) {
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

	private void setupAdapter() {
		if (mItems.size() == 0) {
			try {
				new NewsFetcher().fetchItemsFromCache(mItems);
			} catch (Exception e) {
				Log.e(TAG, "Failed to load Data", e);
			}
		}
		recyclerView.setAdapter(new NewsAdapter(mItems));

	}

	private class NewsHolder extends RecyclerView.ViewHolder implements OnClickListener{
		private TextView title;
		private TextView time;
		private ImageView pic;
		private String selectItemUrl;// 选定一项时候记录其文章地址
		
		public NewsHolder(View itemView) {
			super(itemView);
			
			title = (TextView) itemView.findViewById(R.id.item_news_title);
			time = (TextView) itemView.findViewById(R.id.item_news_date);
			pic = (ImageView) itemView.findViewById(R.id.item_news_pic);
			itemView.setOnClickListener(this);
		}
		
		public void bindNewsItem(NewsItem newsItem){
			title.setText(newsItem.getTitle());
			time.setText(newsItem.getTime());
			selectItemUrl = newsItem.getUrl();
			if(!newsItem.getPic().equals("")){
				Picasso.with(context).load(newsItem.getPic()).placeholder(R.drawable.ic_item_space).into(pic);
			}
		}

		@Override
		public void onClick(View v) {
			Intent intent = NewsActivity.newIntent(context);
			intent.putExtra("selectItemUrl", selectItemUrl);
			startActivity(intent);
		}
		
	}
	
	private class NewsAdapter extends RecyclerView.Adapter<NewsHolder>{
		private List<NewsItem> newsItems;
		
		public NewsAdapter(List<NewsItem> newsItems) {
			super();
			this.newsItems = newsItems;
		}

		@Override
		public int getItemCount() {
			return newsItems.size();
		}

		@Override
		public void onBindViewHolder(NewsHolder newsHolder, int position) {
			NewsItem newsItem = newsItems.get(position);
			newsHolder.bindNewsItem(newsItem);
		}

		@Override
		public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.item_news, parent, false);
			return new NewsHolder(view);
		}
		
	}
	
	private class FetchItemsTask extends AsyncTask<Void, Void, List<NewsItem>> implements NetResponse {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(hasInitData){
				showProgressDialog();
			}
		}
		
		@Override
		protected List<NewsItem> doInBackground(Void... params) {
			return new NewsFetcher(this, context).fetchItems();
		}
		
		@Override
		protected void onPostExecute(List<NewsItem> items) {
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
