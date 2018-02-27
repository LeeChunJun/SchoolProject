package com.leechunjun.school.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.leechunjun.school.R;
import com.leechunjun.school.bean.WeatherItem;
import com.leechunjun.school.data.NetResponse;
import com.leechunjun.school.data.WeatherFetcher;
import com.squareup.picasso.Picasso;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends AppCompatActivity {
	private static final String TAG = "WeatherActivity";

	private Context context;
	private String cityJson;
	private List<String> cityList = new ArrayList<String>();
	private Map<String, String> map = new HashMap<String, String>();

	private String mQuery;
	private SearchView searchView;
	private ListView listView;
	private RelativeLayout rlayout;

	private String netReason;// 链接网站回复
	private int netCode;// 链接网站回复
	private Dialog progressDialog;
	private WeatherItem weatherItem = new WeatherItem();

	private TextView city;
	private TextView loc;
	private TextView tmp;
	private TextView txt;
	private ImageView code;
	private TextView data1_mr;
	private TextView data1_ms;
	private TextView data1_sr;
	private TextView data1_ss;
	private ImageView data1_code_d;
	private ImageView data1_code_n;
	private TextView data1_txt_d;
	private TextView data1_txt_n;
	private TextView data1_tmp;
	private TextView data1_data;
	
	private TextView data2_mr;
	private TextView data2_ms;
	private TextView data2_sr;
	private TextView data2_ss;
	private ImageView data2_code_d;
	private ImageView data2_code_n;
	private TextView data2_txt_d;
	private TextView data2_txt_n;
	private TextView data2_tmp;
	private TextView data2_data;
	
	private TextView data3_mr;
	private TextView data3_ms;
	private TextView data3_sr;
	private TextView data3_ss;
	private ImageView data3_code_d;
	private ImageView data3_code_n;
	private TextView data3_txt_d;
	private TextView data3_txt_n;
	private TextView data3_tmp;
	private TextView data3_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		context = this;
		searchView = (SearchView) findViewById(R.id.searchView_weather);
		listView = (ListView) findViewById(R.id.listView_weather);
		rlayout = (RelativeLayout) findViewById(R.id.rl_response_weather);
		cityJson = initData();
		parseJson(cityJson, map, cityList);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,
				cityList);
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// String query = cityList.get(position);
		// mQuery = map.get(query);
		// listView.setVisibility(8);//GONE
		// Toast.makeText(context, mQuery, Toast.LENGTH_SHORT).show();
		// if(rlayout.getVisibility()==8){
		// rlayout.setVisibility(0);
		// }
		//// new FetchItemsTask().execute(mQuery);
		// }
		// });
		searchView.setQueryHint("请输入查询城市");
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				if (!query.equals("") && map.containsKey(query)) {
					mQuery = map.get(query);
					listView.clearTextFilter();
					listView.setVisibility(8);// GONE
					Log.i(TAG, "查询的城市代码: "+mQuery);
					if (rlayout.getVisibility() == 8) {
						rlayout.setVisibility(0);
					}
					new FetchItemsTask().execute(mQuery);
				} else {
					Toast.makeText(context, "无法查找信息，请重新输入", Toast.LENGTH_SHORT).show();
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (!TextUtils.isEmpty(newText)) {
					listView.setVisibility(0);// 0->VISIBLE,4->INVISIBLE,8->GONE
					listView.setFilterText(newText);
				} else {
					listView.clearTextFilter();
					listView.setVisibility(8);// GONE
				}
				return false;
			}
		});

		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.load_dialog);
		progressDialog.setCancelable(true);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		city = (TextView) findViewById(R.id.textView_city_weather);
		loc = (TextView) findViewById(R.id.textView_loc_weather);
		tmp = (TextView) findViewById(R.id.textView_tmp_weather);
		txt = (TextView) findViewById(R.id.textView_txt_weather);
		code = (ImageView) findViewById(R.id.imageView_code_weather);
		
		data1_mr = (TextView) findViewById(R.id.textView_data1_mr_weather);
		data1_ms = (TextView) findViewById(R.id.textView_data1_ms_weather);
		data1_sr = (TextView) findViewById(R.id.textView_data1_sr_weather);
		data1_ss = (TextView) findViewById(R.id.textView_data1_ss_weather);
		data1_code_d = (ImageView) findViewById(R.id.imageView_data1_code_d_weather);
		data1_code_n = (ImageView) findViewById(R.id.imageView_data1_code_n_weather);
		data1_txt_d = (TextView) findViewById(R.id.textView_data1_txt_d_weather);
		data1_txt_n = (TextView) findViewById(R.id.textView_data1_txt_n_weather);
		data1_tmp = (TextView) findViewById(R.id.textView_data1_tmp_weather);
		data1_data = (TextView) findViewById(R.id.textView_data1_date_weather);
		
		data2_mr = (TextView) findViewById(R.id.textView_data2_mr_weather);
		data2_ms = (TextView) findViewById(R.id.textView_data2_ms_weather);
		data2_sr = (TextView) findViewById(R.id.textView_data2_sr_weather);
		data2_ss = (TextView) findViewById(R.id.textView_data2_ss_weather);
		data2_code_d = (ImageView) findViewById(R.id.imageView_data2_code_d_weather);
		data2_code_n = (ImageView) findViewById(R.id.imageView_data2_code_n_weather);
		data2_txt_d = (TextView) findViewById(R.id.textView_data2_txt_d_weather);
		data2_txt_n = (TextView) findViewById(R.id.textView_data2_txt_n_weather);
		data2_tmp = (TextView) findViewById(R.id.textView_data2_tmp_weather);
		data2_data = (TextView) findViewById(R.id.textView_data2_date_weather);
		
		data3_mr = (TextView) findViewById(R.id.textView_data3_mr_weather);
		data3_ms = (TextView) findViewById(R.id.textView_data3_ms_weather);
		data3_sr = (TextView) findViewById(R.id.textView_data3_sr_weather);
		data3_ss = (TextView) findViewById(R.id.textView_data3_ss_weather);
		data3_code_d = (ImageView) findViewById(R.id.imageView_data3_code_d_weather);
		data3_code_n = (ImageView) findViewById(R.id.imageView_data3_code_n_weather);
		data3_txt_d = (TextView) findViewById(R.id.textView_data3_txt_d_weather);
		data3_txt_n = (TextView) findViewById(R.id.textView_data3_txt_n_weather);
		data3_tmp = (TextView) findViewById(R.id.textView_data3_tmp_weather);
		data3_data = (TextView) findViewById(R.id.textView_data3_date_weather);
	}

	public void showProgressDialog() {
		progressDialog.show();
	}

	public void cancelProgressDialog() {
		progressDialog.dismiss();
	}

	public void parseJson(String cityJson, Map<String, String> map, List<String> cityList) {
		try {
			JSONArray jsonArray = new JSONArray(cityJson);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String cityZh = jsonObject.getString("cityZh");
				cityList.add(cityZh);
				map.put(cityZh, jsonObject.getString("id"));

			}
		} catch (JSONException e) {
			Log.e(TAG, "解析城市列表失败 : " + e);
		}
	}

	public String initData() {
		AssetManager assetManager = context.getAssets();
		StringBuilder stringBuilder = new StringBuilder();
		try {
			InputStream is = assetManager.open("china-city-list.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
			String str = null;
			while ((str = br.readLine()) != null) {
				stringBuilder.append(str);
			}
		} catch (IOException e) {
			Log.e(TAG, "加载城市列表失败 : " + e);
		}
		return stringBuilder.toString();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, WeatherActivity.class);
		return intent;
	}

	private class FetchItemsTask extends AsyncTask<String, Void, WeatherItem> implements NetResponse {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}

		@Override
		protected WeatherItem doInBackground(String... params) {
			return new WeatherFetcher(mQuery, this, context).fetchItems();
		}

		@Override
		protected void onPostExecute(WeatherItem result) {
			weatherItem = result;
			if (netCode != 0) {
				Toast.makeText(context, netReason, Toast.LENGTH_SHORT).show();
				netCode = 0;// 改变netCode值，以免一直报错误的原因
			}
			cancelProgressDialog();
			initView();
		}

		@Override
		public void onFinished(String response, int code) {
			netReason = response;
			netCode = code;
		}
	}

	public void initView() {
		city.setText(weatherItem.getCity());
		loc.setText(weatherItem.getLoc());
		tmp.setText(weatherItem.getTmp()+"℃");
		txt.setText(weatherItem.getTxt());
		Picasso.with(context).load(weatherItem.getCode()).placeholder(R.drawable.ic_item_space).into(code);
	
		data1_mr.setText("月升时间:"+weatherItem.getData1_mr());
		data1_ms.setText("月落时间:"+weatherItem.getData1_ms());
		data1_sr.setText("日出时间:"+weatherItem.getData1_sr());
		data1_ss.setText("日落时间:"+weatherItem.getData1_ss());
		Picasso.with(context).load(weatherItem.getData1_code_d()).placeholder(R.drawable.ic_item_space).into(data1_code_d);
		Picasso.with(context).load(weatherItem.getData1_code_n()).placeholder(R.drawable.ic_item_space).into(data1_code_n);
		data1_txt_d.setText("白天:"+weatherItem.getData1_txt_d());
		data1_txt_n.setText("夜间:"+weatherItem.getData1_txt_n());
		data1_tmp.setText(weatherItem.getData1_tmp());
		data1_data.setText(weatherItem.getData1_data());
		
		data2_mr.setText("月升时间:"+weatherItem.getData2_mr());
		data2_ms.setText("月落时间:"+weatherItem.getData2_ms());
		data2_sr.setText("日出时间:"+weatherItem.getData2_sr());
		data2_ss.setText("日落时间:"+weatherItem.getData2_ss());
		Picasso.with(context).load(weatherItem.getData2_code_d()).placeholder(R.drawable.ic_item_space).into(data2_code_d);
		Picasso.with(context).load(weatherItem.getData2_code_n()).placeholder(R.drawable.ic_item_space).into(data2_code_n);
		data2_txt_d.setText("白天:"+weatherItem.getData2_txt_d());
		data2_txt_n.setText("夜间:"+weatherItem.getData2_txt_n());
		data2_tmp.setText(weatherItem.getData2_tmp());
		data2_data.setText(weatherItem.getData2_data());
		
		data3_mr.setText("月升时间:"+weatherItem.getData3_mr());
		data3_ms.setText("月落时间:"+weatherItem.getData3_ms());
		data3_sr.setText("日出时间:"+weatherItem.getData3_sr());
		data3_ss.setText("日落时间:"+weatherItem.getData3_ss());
		Picasso.with(context).load(weatherItem.getData3_code_d()).placeholder(R.drawable.ic_item_space).into(data3_code_d);
		Picasso.with(context).load(weatherItem.getData3_code_n()).placeholder(R.drawable.ic_item_space).into(data3_code_n);
		data3_txt_d.setText("白天:"+weatherItem.getData3_txt_d());
		data3_txt_n.setText("夜间:"+weatherItem.getData3_txt_n());
		data3_tmp.setText(weatherItem.getData3_tmp());
		data3_data.setText(weatherItem.getData3_data());
	}

}
