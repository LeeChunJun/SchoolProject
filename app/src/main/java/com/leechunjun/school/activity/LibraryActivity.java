package com.leechunjun.school.activity;

import java.util.ArrayList;
import java.util.List;

import com.leechunjun.school.R;
import com.leechunjun.school.bean.BookItem;
import com.leechunjun.school.data.LibraryFetcher;
import com.leechunjun.school.data.NetResponse;
import com.leechunjun.school.data.SharedPreferences;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LibraryActivity extends AppCompatActivity {
	// private static final String TAG = "LibraryActivity";

	private RecyclerView recyclerView;
	private Context context;
	private List<BookItem> mBookList = new ArrayList<BookItem>();
	private String netReason;// 链接网站回复
	private int netCode;// 链接网站回复

	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library);
		context = this;

		recyclerView = (RecyclerView) findViewById(R.id.recycler_view_library_book);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.load_dialog);
		progressDialog.setCancelable(true);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		new FetchItemsTask().execute();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, LibraryActivity.class);
		return intent;
	}
	
	private void setupAdapter() {
		recyclerView.setAdapter(new BookAdapter(mBookList));
	}
	
	public void showProgressDialog() {
		progressDialog.show();
	}

	public void cancelProgressDialog() {
		progressDialog.dismiss();
	}
	
	private class BookHolder extends RecyclerView.ViewHolder{
		private TextView bookNum;
		private TextView bookName;
		private TextView bookStart;
		private TextView bookEnd;
		private TextView bookTimes;
		private TextView bookPlace;

		public BookHolder(View itemView) {
			super(itemView);
			bookNum = (TextView) itemView.findViewById(R.id.tv_study_library_num);
			bookName = (TextView) itemView.findViewById(R.id.tv_study_library_name);
			bookStart = (TextView) itemView.findViewById(R.id.tv_study_library_start);
			bookEnd = (TextView) itemView.findViewById(R.id.tv_study_library_end);
			bookTimes = (TextView) itemView.findViewById(R.id.tv_study_library_times);
			bookPlace = (TextView) itemView.findViewById(R.id.tv_study_library_place);
		}
		
		public void bindBook(BookItem item){
			bookNum.setText("条码号："+item.getNum());
			bookName.setText("书名："+item.getName());
			bookStart.setText("借阅日期："+item.getStartTime());
			bookEnd.setText("应还日期："+item.getEndTime());
			bookTimes.setText("续借次数："+item.getTimes());
			bookPlace.setText("馆藏地："+item.getPlace());
		}
		
	}
	
	private class BookAdapter extends RecyclerView.Adapter<BookHolder>{
		private List<BookItem> bookList;

		@Override
		public int getItemCount() {
			return bookList.size();
		}
		
		public BookAdapter(List<BookItem> bookList) {
			this.bookList = bookList;
		}

		@Override
		public void onBindViewHolder(BookHolder bookHolder, int position) {
			BookItem item = bookList.get(position);
			bookHolder.bindBook(item);
		}

		@Override
		public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			View view = layoutInflater.inflate(R.layout.item_library, parent, false);
			return new BookHolder(view);
		}
	}

	private class FetchItemsTask extends AsyncTask<Void, Void, List<BookItem>> implements NetResponse {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}
		
		@Override
		protected List<BookItem> doInBackground(Void... params) {
			String id = new SharedPreferences(context).getStoredString("TUAccount");
			String password = new SharedPreferences(context).getStoredString("TUPassword");
			return new LibraryFetcher(context, this, id, password).fetchItems();
		}
		
		@Override
		protected void onPostExecute(List<BookItem> result) {
			mBookList = result;
			if (netCode != 0) {
				Toast.makeText(context, netReason, Toast.LENGTH_SHORT).show();
				netCode = 0;// 改变netCode值，以免一直报错误的原因
			}
			setupAdapter();
			
			cancelProgressDialog();
		}
		
		@Override
		public void onFinished(String response, int code) {
			netReason = response;
			netCode = code;
		}
		
	}

}
