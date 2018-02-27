package com.leechunjun.school.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.leechunjun.school.bean.HistoryItem;

import android.content.Context;
import android.util.Log;

public class HistoryFetcher {
	private static final String TAG = "HistoryFetcher";
	private static final String CacheFile = "HistorysCacheFile";

	private NetResponse netResponse;
	private Context context;

	public HistoryFetcher() {
		super();
	}

	public HistoryFetcher(NetResponse netResponse, Context context) {
		super();
		this.netResponse = netResponse;
		this.context = context;
	}

	public byte[] getUrlBytes(String urlSpec) throws IOException {
		URL url = new URL(urlSpec);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = connection.getInputStream();
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
			}
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			return out.toByteArray();
		} finally {
			connection.disconnect();
		}
	}

	public String getUrlString(String urlSpec) throws IOException {
		return new String(getUrlBytes(urlSpec));
	}

	public List<HistoryItem> fetchItems() {
		List<HistoryItem> items = new ArrayList<HistoryItem>();
		
		try {
			String url = "http://www.todayonhistory.com/";
			String html = getUrlString(url);
			String jsonString = History2Json.parseHistory(html);
			Log.i(TAG, "Received JSON: " + jsonString);
			new FileStorages(context).save(CacheFile, jsonString);
			Log.i(TAG, "Write JSON To CacheFile: " + CacheFile);
			parseItems(items, jsonString);
		} catch (IOException ioe) {
			Log.e(TAG, "Failed to fetch items", ioe);

			try {
				String jsonStringOld = new FileStorages(context).load(CacheFile);
				parseItems(items, jsonStringOld);
			} catch (Exception e) {
				netResponse.onFinished("网络连接故障", 1);
			}
			return items;

		} catch (JSONException je) {
			Log.e(TAG, "Failed to parse JSON", je);
			netResponse.onFinished("解析数据故障", 2);
		}
		
		return items;
	}

	private void parseItems(List<HistoryItem> items, String jsonString) throws IOException, JSONException {
		if(!jsonString.equals("")){
			netResponse.onFinished("获取数据成功", 0);
			parseItemsSub(items, jsonString);
		} 
	}

	private void parseItemsSub(List<HistoryItem> items, String jsonString) throws JSONException {
		JSONArray topicJsonArray = new JSONArray(jsonString);

		for (int i = 0; i < topicJsonArray.length(); i++) {
			JSONObject topicObject = topicJsonArray.getJSONObject(i);

			HistoryItem item = new HistoryItem();
			item.setTitle(topicObject.getString("title"));
			item.setTime(topicObject.getString("time"));
			item.setUrl(topicObject.getString("url"));
			
			items.add(item);
		}
	}
	
	public void fetchItemsFromCache(List<HistoryItem> items) throws Exception{
		String jsonStringOld = new FileStorages(context).load(CacheFile);
		parseItemsSub(items, jsonStringOld);
	}
}
