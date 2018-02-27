package com.leechunjun.school.data;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class News2Json {
	private static String result = "";// 返回的json格式数据
	private static String[] NewsTitle = new String[25];
	private static String[] NewsTime = new String[25];
	private static String[] NewsPic = new String[25];
	private static String[] NewsURL = new String[25];
	
	public static String parseNews(String html) throws IOException{
		Document document = Jsoup.parse(html);
		Element content = document.select("div.tlist.newslist").first();
		
		Elements lis = content.select("li.limg");
		int i = 0;
		for (Element li : lis) {
			NewsTime[i] = li.getElementsByClass("poh").first().text();
			Element link = li.select("a").first();
			if (link.attr("href").contains("\"")) {
				NewsURL[i] = process(link.attr("href"));
			} else {
				NewsURL[i] = link.attr("href");
			}
			NewsTitle[i] = link.text();
			Element img = li.select("img").first();
			NewsPic[i] = img.attr("data-original");
			i++;
		}
		
		StringBuilder jsonString = new StringBuilder();
		for (int j = 0; j < 25; j++) {
			jsonString.append("{\"title\":\"" + NewsTitle[j]
					+ "\"" + ",");
			jsonString.append("\"time\":\"" + NewsTime[j]
					+ "\"" + ",");
			jsonString.append("\"pic\":\"" + NewsPic[j]
					+ "\"" + ",");
			jsonString.append("\"url\":\"" + NewsURL[j] + "\""
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
