package com.leechunjun.school.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.leechunjun.school.bean.WeatherItem;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class WeatherFetcher {
	private static final String TAG = "WeatherFetcher";
	
	private static final String APPKEY = "228bda021e72489dbf6d851c3e00e3d6";
	
	private String city;
	private NetResponse netResponse;
	@SuppressWarnings("unused")
	private Context context;
	
	public WeatherFetcher(String city, NetResponse netResponse, Context context) {
		super();
		this.city = city;
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
	
	public WeatherItem fetchItems(){
		WeatherItem weatherItem = new WeatherItem();
		
		try {
			String url = Uri.parse("https://free-api.heweather.com/v5/weather").buildUpon()
					.appendQueryParameter("city", city)
					.appendQueryParameter("key", APPKEY)
					.build().toString();
			String jsonString = getUrlString(url);
			Log.i(TAG, "Received JSON: " + jsonString);
			JSONObject jsonBody = new JSONObject(jsonString);
			parseItems(weatherItem, jsonBody);
		} catch (IOException ioe) {
			Log.e(TAG, "Failed to fetch items", ioe);
			netResponse.onFinished("网络连接故障", 1);
		} catch (JSONException je){
			Log.e(TAG, "Failed to parse JSON", je);
			// netResponse.onFinished("解析数据故障", 2);
		}
		
		return weatherItem;
	}

	private void parseItems(WeatherItem weatherItem, JSONObject jsonBody) throws JSONException {
		JSONArray jsonArray = jsonBody.getJSONArray("HeWeather5");
		JSONObject messageJson = jsonArray.getJSONObject(0);
		
		String response = messageJson.getString("status");
		if(response.equals("ok")){
			netResponse.onFinished(response, 0);
			parseItemsSub(weatherItem, messageJson);
		} else {
			netResponse.onFinished(response, -1);
		}
		
	}

	private void parseItemsSub(WeatherItem weatherItem, JSONObject messageJson) throws JSONException {
		JSONObject basicObject = messageJson.getJSONObject("basic");
		weatherItem.setCity(basicObject.getString("city"));//city
		
		JSONObject updateObject = basicObject.getJSONObject("update");
		weatherItem.setLoc(updateObject.getString("loc"));//loc
		
		JSONArray hourly_forecastArray = messageJson.getJSONArray("hourly_forecast");
		JSONObject hourly_forecastObject = hourly_forecastArray.getJSONObject(0);
		weatherItem.setTmp(hourly_forecastObject.getString("tmp"));//tmp
		
		JSONObject condObject = hourly_forecastObject.getJSONObject("cond");
		weatherItem.setCode("http://files.heweather.com/cond_icon/"+condObject.getString("code")+".png");
		weatherItem.setTxt(condObject.getString("txt"));//txt,code
		
		JSONArray daily_forecastArray = messageJson.getJSONArray("daily_forecast");
		
		JSONObject data1 = daily_forecastArray.getJSONObject(0);
		JSONObject data1astro = data1.getJSONObject("astro");
		weatherItem.setData1_mr(data1astro.getString("mr"));
		weatherItem.setData1_ms(data1astro.getString("ms"));
		weatherItem.setData1_sr(data1astro.getString("sr"));
		weatherItem.setData1_ss(data1astro.getString("ss"));
		JSONObject data1cond = data1.getJSONObject("cond");
		weatherItem.setData1_code_d("http://files.heweather.com/cond_icon/"+data1cond.getString("code_d")+".png");
		weatherItem.setData1_code_n("http://files.heweather.com/cond_icon/"+data1cond.getString("code_n")+".png");
		weatherItem.setData1_txt_d(data1cond.getString("txt_d"));
		weatherItem.setData1_txt_n(data1cond.getString("txt_n"));
		JSONObject data1tmp = data1.getJSONObject("tmp");
		weatherItem.setData1_tmp(data1tmp.getString("min")+"℃～"+data1tmp.getString("max")+"℃");
		weatherItem.setData1_data(data1.getString("date"));
		
		JSONObject data2 = daily_forecastArray.getJSONObject(1);
		JSONObject data2astro = data2.getJSONObject("astro");
		weatherItem.setData2_mr(data2astro.getString("mr"));
		weatherItem.setData2_ms(data2astro.getString("ms"));
		weatherItem.setData2_sr(data2astro.getString("sr"));
		weatherItem.setData2_ss(data2astro.getString("ss"));
		JSONObject data2cond = data2.getJSONObject("cond");
		weatherItem.setData2_code_d("http://files.heweather.com/cond_icon/"+data2cond.getString("code_d")+".png");
		weatherItem.setData2_code_n("http://files.heweather.com/cond_icon/"+data2cond.getString("code_n")+".png");
		weatherItem.setData2_txt_d(data2cond.getString("txt_d"));
		weatherItem.setData2_txt_n(data2cond.getString("txt_n"));
		JSONObject data2tmp = data2.getJSONObject("tmp");
		weatherItem.setData2_tmp(data2tmp.getString("min")+"℃～"+data2tmp.getString("max")+"℃");
		weatherItem.setData2_data(data2.getString("date"));
		
		JSONObject data3 = daily_forecastArray.getJSONObject(2);
		JSONObject data3astro = data3.getJSONObject("astro");
		weatherItem.setData3_mr(data3astro.getString("mr"));
		weatherItem.setData3_ms(data3astro.getString("ms"));
		weatherItem.setData3_sr(data3astro.getString("sr"));
		weatherItem.setData3_ss(data3astro.getString("ss"));
		JSONObject data3cond = data3.getJSONObject("cond");
		weatherItem.setData3_code_d("http://files.heweather.com/cond_icon/"+data3cond.getString("code_d")+".png");
		weatherItem.setData3_code_n("http://files.heweather.com/cond_icon/"+data3cond.getString("code_n")+".png");
		weatherItem.setData3_txt_d(data3cond.getString("txt_d"));
		weatherItem.setData3_txt_n(data3cond.getString("txt_n"));
		JSONObject data3tmp = data3.getJSONObject("tmp");
		weatherItem.setData3_tmp(data3tmp.getString("min")+"℃～"+data3tmp.getString("max")+"℃");
		weatherItem.setData3_data(data3.getString("date"));
	}

}
