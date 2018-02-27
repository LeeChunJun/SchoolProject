package com.leechunjun.school.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.leechunjun.school.R;

import android.content.Context;

public class CET2Html {
	private String result = "";// 返回的html格式数据
	private Context context;

	public CET2Html(Context context) {
		super();
		this.context = context;
	}

	public String parseGrade(String id, String name) throws Exception {
		Connection connection = Jsoup.connect("http://www.chsi.com.cn/cet/query");
		connection.header("Host", "www.chsi.com.cn");
		connection.header("Connection", "keep-alive");
		connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		connection.header("Upgrade-Insecure-Requests", "1");
		connection.header("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 UBrowser/6.0.1471.914 Safari/537.36");
		connection.header("Referer", "http://www.chsi.com.cn/cet/");
		connection.header("Accept-Encoding", "gzip, deflate");
		connection.header("Accept-Language", "zh-CN,zh;q=0.8");
		connection.header("Cookie",
				"JSESSIONID=852702ECF7FD6390836FB4C5584E08B3; acw_tc=AQAAALsghWdnugEAkApstt6V/C2rGw2u; __utmt=1; __utma=65168252.337226662.1487725796.1487738498.1487747592.3; __utmb=65168252.1.10.1487747592; __utmc=65168252; __utmz=65168252.1487725796.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
		connection.data("zkzh", id);
		connection.data("xm", name);
		connection.timeout(8000).method(Method.POST);
		Response response = connection.execute();
		result = fetchContent(response.parse());
		if(!result.contains("写作和翻译")){
			result = "";
		} else {
			String htmlStart = getFromRaw();
			String htmlEnd = "</body></html>";
			result = htmlStart + result + htmlEnd;
		}
		
		return result;
	}

	public String fetchContent(Document document) {
		Element content = document.select("div.m_cnt_m").first();
		content.select("div.psTxt.marginT20.alignC").remove();
//		content.select("div.marginT20.alignC").remove();
		return content.html();
	}

	public String getFromRaw() throws Exception {
		InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(R.raw.htmlstart), "utf-8");
		BufferedReader read = new BufferedReader(in);
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = read.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

}
