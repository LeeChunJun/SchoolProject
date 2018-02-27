package com.leechunjun.school.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.util.Log;

public class CalenderFetcher {
	private static final String TAG = "CalenderFetcher";
	
	@SuppressWarnings("unused")
	private Context context;
	private NetResponse netResponse;
	public CalenderFetcher(Context context, NetResponse netResponse) {
		super();
		this.context = context;
		this.netResponse = netResponse;
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

	public String fetchItems(){
		String retPicUrl = "";
		
		try {
			String calenderHtml = getUrlString("http://www.jxnu.edu.cn/s/2/t/690/54/b4/info87220.htm");
			retPicUrl = parseItem(calenderHtml);
		} catch (Exception e) {
			Log.e(TAG, "获取校历失败: "+e);
			netResponse.onFinished("获取校历失败!", -1);
		}
		
		return retPicUrl;
	}

	private String parseItem(String calenderHtml) {
		String result = "http://www.jxnu.edu.cn";
		
		Document doc = Jsoup.parse(calenderHtml);
		Elements pngs = doc.select("img[src$=.png]");
		Element png = pngs.first();
		String picUrl = result+png.attr("src");
		
		Log.i(TAG, "pngUrl: "+picUrl);
		netResponse.onFinished("获取校历成功!", 0);
		return picUrl;
	}
}
