package com.leechunjun.school.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Topic2Json {
	private static String result = "";// 返回的json格式数据
	private static String[] TopicTitle = new String[50];
	private static String[] TopicTime = new String[50];
	private static String[] TopicURL = new String[50];
	
	public static String parseTopic(String html){
		Document document = Jsoup.parse(html);
		String topicList = document
				.select("meta[name=description]").get(0)
				.attr("content");
		String[] topics = topicList.split("【");
		for (int i = 1; i < topics.length; i++) {
			String temp = topics[i];
			temp="【"+temp;
			if(i!=topics.length-1){
				temp=temp.substring(0, temp.length()-1);
			}
			if (temp.contains("\"")) {
				topics[i] = process(temp);
			}
			TopicTitle[i - 1] = temp;
		}
		fetchContent(document);
		

		StringBuilder jsonString = new StringBuilder();
		for (int j = 0; j < 50; j++) {
			jsonString.append("{\"title\":\"" + TopicTitle[j]
					+ "\"" + ",");
			jsonString.append("\"time\":\"" + TopicTime[j]
					+ "\"" + ",");
			jsonString.append("\"url\":\"" + TopicURL[j] + "\""
					+ "},");
		}
		jsonString.deleteCharAt(jsonString.length() - 1);
		jsonString.insert(0, '[');
		jsonString.append(']');
		result = jsonString.toString();
		return result;
	}
	
	public static void fetchContent(Document document) {
		Elements elements = document
				.select("div.articleCell.SG_j_linedot1");
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);

			getHrefText(element, i);

			getTimeIsPic(element, i);
		}

	}
	
	public static void getTimeIsPic(Element element, int i) {
		Element Pic = element.select(".atc_ic_b").get(0);
		@SuppressWarnings("unused")
		boolean isPic = true;
		if (Pic.select("img").attr("title").equals("")) {
			isPic = false;
		}
		String time = element.select("span.atc_tm.SG_txtc")
				.get(0).text();
		// System.out.println(time+"||"+isPic);
		TopicTime[i] = time;
	}
	
	public static void getHrefText(Element element, int i) {
		Elements links = element.getElementsByTag("a");
		Element link = links.get(links.size() - 1);
		String linkHref = link.attr("href");
		@SuppressWarnings("unused")
		String linkText = link.text();//省略的标题
		// System.out.println(linkHref+"||"+linkText);
		TopicURL[i] = linkHref;
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
