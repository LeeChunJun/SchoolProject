package com.leechunjun.school;

import java.util.ArrayList;
import java.util.List;

import com.leechunjun.school.activity.HistoryActivity;
import com.leechunjun.school.bean.HistoryItem;
import com.leechunjun.school.data.HistoryFetcher;
import com.leechunjun.school.data.NetResponse;

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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Home2Fragment extends Fragment {
	private static final String TAG = "Home2Fragment";

	private Context context;
	private View rootView;// 缓存Fragment view
	private RecyclerView recyclerView;
	private List<HistoryItem> mItems = new ArrayList<HistoryItem>();
	private String netReason;// 链接网站回复
	private int netCode;// 链接网站回复

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.home2, container, false);
			context = rootView.getContext();
			recyclerView = (RecyclerView) rootView.findViewById(R.id.home_history_recycler_view);
			recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

			new FetchItemsTask().execute();
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
				new HistoryFetcher().fetchItemsFromCache(mItems);
			} catch (Exception e) {
				Log.e(TAG, "Failed to load Data", e);
			}
		}
		recyclerView.setAdapter(new HistorysAdapter(mItems));

	}

	private class HistorysHolder extends RecyclerView.ViewHolder implements OnClickListener{
		private TextView title;
		private TextView time;
		private String selectItemUrl;// 选定一项时候记录其文章地址
		
		public HistorysHolder(View itemView) {
			super(itemView);
			
			title = (TextView) itemView.findViewById(R.id.item_history_title);
			time = (TextView) itemView.findViewById(R.id.item_history_time);
			itemView.setOnClickListener(this);
		}
		
		public void bindHistoryItem(HistoryItem historyItem){
			title.setText(historyItem.getTitle());
			time.setText(historyItem.getTime());
			selectItemUrl = historyItem.getUrl();
		}

		@Override
		public void onClick(View v) {
			Intent intent = HistoryActivity.newIntent(context);
			intent.putExtra("selectItemUrl", selectItemUrl);
			startActivity(intent);
		}
	}
	
	private class HistorysAdapter extends RecyclerView.Adapter<HistorysHolder> {
		List<HistoryItem> historyItems;
		
		public HistorysAdapter(List<HistoryItem> historyItems) {
			super();
			this.historyItems = historyItems;
		}

		@Override
		public int getItemCount() {
			return historyItems.size();
		}

		@Override
		public void onBindViewHolder(HistorysHolder historysHolder, int position) {
			HistoryItem historyItem = historyItems.get(position);
			historysHolder.bindHistoryItem(historyItem);
		}

		@Override
		public HistorysHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.item_historys, parent, false);
			return new HistorysHolder(view);
		}
		
	}

	private class FetchItemsTask extends AsyncTask<Void, Void, List<HistoryItem>> implements NetResponse {

		@Override
		protected List<HistoryItem> doInBackground(Void... params) {
			return new HistoryFetcher(this, context).fetchItems();
		}
		
		@Override
		protected void onPostExecute(List<HistoryItem> items) {
			mItems = items;
			if (netCode != 0) {
				Toast.makeText(context, netReason, Toast.LENGTH_SHORT).show();
				netCode = 0;// 改变netCode值，以免一直报错误的原因
			}
			setupAdapter();
		}
		
		@Override
		public void onFinished(String response, int code) {
			netReason = response;
			netCode = code;
		}
	}

}
