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

import com.leechunjun.school.bean.TopicItem;

import android.content.Context;
import android.util.Log;

public class TopicFetcher {
	private static final String TAG = "TopicFetcher";
	private static final String CacheFile = "TopicCacheFile";
	private String PAGE = "";

	private NetResponse netResponse;
	private Context context;

	public TopicFetcher() {
		super();
	}

	public TopicFetcher(NetResponse netResponse, Context context) {
		super();
		this.netResponse = netResponse;
		this.context = context;
		
		PAGE = new SharedPreferences(context).getStoredString(CacheFile);
		if(PAGE==null){
			PAGE = "1";
		} else {
			PAGE = String.valueOf(Integer.valueOf(PAGE)+1);
		}
		new SharedPreferences(context).setStoredString(CacheFile, PAGE);
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

	public List<TopicItem> fetchItems() {
		List<TopicItem> items = new ArrayList<TopicItem>();
		
		try {
			String url = "http://blog.sina.com.cn/s/articlelist_1649586363_0_"+PAGE+".html";
			String html = getUrlString(url);
			String jsonString = Topic2Json.parseTopic(html);
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
				netResponse.onFinished("�������ӹ���", 1);
			}
			return items;

		} catch (JSONException je) {
			Log.e(TAG, "Failed to parse JSON", je);
			netResponse.onFinished("�������ݹ���", 2);
		}
		
		return items;
	}

	private void parseItems(List<TopicItem> items, String jsonString) throws IOException, JSONException {
		if(!jsonString.equals("")){
			netResponse.onFinished("��ȡ���ݳɹ�", 0);
			parseItemsSub(items, jsonString);
		} 
	}

	private void parseItemsSub(List<TopicItem> items, String jsonString) throws JSONException {
		JSONArray topicJsonArray = new JSONArray(jsonString);

		for (int i = 0; i < topicJsonArray.length(); i++) {
			JSONObject topicObject = topicJsonArray.getJSONObject(i);

			TopicItem item = new TopicItem();
			item.setTitle(topicObject.getString("title"));
			item.setTime(topicObject.getString("time"));
			item.setUrl(topicObject.getString("url"));
			
			items.add(item);
		}
	}
	
	public void fetchItemsFromCache(List<TopicItem> items) throws Exception{
		String jsonStringOld = new FileStorages(context).load(CacheFile);
		parseItemsSub(items, jsonStringOld);
	}
}
