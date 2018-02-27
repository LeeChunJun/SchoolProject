package com.leechunjun.school.data;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class History2Json {
	private static String result = "";// 返回的json格式数据
	private static String[] HistoryTitle = new String[40];
	private static String[] HistoryTime = new String[40];
	private static String[] HistoryURL = new String[40];
	
	public static String parseHistory(String htmlurl) throws IOException{
		Document document = Jsoup.parse(htmlurl);
		
		Elements lis1 = document.select("div.t");
		int i = 0;
		for (Element li : lis1) {
			Element span = li.select("span").first();
			HistoryTime[i] = span.text();
			Element link = li.select("a").first();
			if (link.text().contains("\"")) {
				HistoryTitle[i] = process(link.text());
			} else {
				HistoryTitle[i] = link.text();
			}
			HistoryURL[i] = link.attr("href");
			i++;
		}
		
		Elements lis2 = document.select("div.text.pr");
		for (Element li : lis2) {
			Element span = li.select("span").first();
			HistoryTime[i] = span.text();
			Element link = li.select("a").first();
			HistoryTitle[i] = link.text();
			HistoryURL[i] = link.attr("href");
			i++;
		}
		
		StringBuilder jsonString = new StringBuilder();
		for (int j = 0; j < 40; j++) {
			jsonString.append("{\"title\":\"" + HistoryTitle[j]
					+ "\"" + ",");
			jsonString.append("\"time\":\"" + HistoryTime[j]
					+ "\"" + ",");
			jsonString.append("\"url\":\"" + HistoryURL[j] + "\""
					+ "},");
		}
		jsonString.deleteCharAt(jsonString.length() - 1);
		jsonString.insert(0, '[');
		jsonString.append(']');
		result = jsonString.toString();
		return result;
	}
	
	/**
	 * 将content中的英文引号换成中文的
	 * @param content
	 * @return
	 */
	private static String process(String content) {
		String regex = "\"([^\"]*)\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		String reCT = content;

		while (matcher.find()) {
			String itemMatch = "“" + matcher.group(1) + "”";
			reCT = reCT.replace("\"" + matcher.group(1) + "\"",
					itemMatch);
		}

		return reCT;
	}
	
}
