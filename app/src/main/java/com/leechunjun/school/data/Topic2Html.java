package com.leechunjun.school.data;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Topic2Html {
	private String result = "";// 返回的html格式数据

	public String parseArticle(String htmlurl) throws IOException {
		Document document = Jsoup.connect(htmlurl).timeout(8000).get();

		result = fetchContent(document);
		
		String htmlStart = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Insert title here</title></head><body>";
		String htmlEnd = "</body></html>";
		
		return htmlStart+result+htmlEnd;
	}

	public String fetchContent(Document document) {
		Element content = document.getElementById("sina_keyword_ad_area2");

		if(content==null){
			return fetchPhone1(document)+fetchPhone2(document);
		}
		
		// 找到带有real_src属性的img标签
		Elements imgElements = content.select("img[real_src]");
		// 找不到时候
		if (imgElements == null) {
			return content.html();
		}
		// 遍历所有的img标签
		for (Element img : imgElements) {
			if (img.tagName().equals("img")) {
				// 获得img标签的属性real_src
				// 设置img标签的src与WIDTH值
				img.attr("src", img.attr("real_src"));
				img.attr("WIDTH", "100%");
			}
		}

		return content.html();
	}
	
	public String fetchPhone1(Document document) {
		Elements phoneContent = document
				.select("div.content.b-txt1");
		Elements imgElements = phoneContent.select("img");
		// 找不到时候
		if (imgElements == null) {
			return phoneContent.html();
		}
		// 遍历所有的img标签
		for (Element img : imgElements) {
			if (img.tagName().equals("img")) {
				// 设置img标签的src与WIDTH值
				img.attr("WIDTH", "100%");
			}
		}
		
		return phoneContent.html();
	}
	
	public String fetchPhone2(Document document) {
		Elements phoneContent = document
				.select("div.item_hide");
		return phoneContent.html();
	}


}
