package com.leechunjun.school.data;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class News2Html {
	private String result = "";// 返回的html格式数据

	public String parseArticle(String htmlurl) throws IOException {
		Document document = Jsoup.connect(htmlurl).timeout(8000).get();

		result = fetchContent(document);
		
		String htmlStart = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Insert title here</title></head><body>";
		String htmlEnd = "</body></html>";
		
		return htmlStart+result+htmlEnd;
	}

	public String fetchContent(Document document) {
		Element content = document.select("div.content").first();

		// 找到带有src属性的img标签
		Elements imgElements = content.select("img[src]");
		// 找不到时候
		if (imgElements == null) {
			return content.html();
		}
		// 遍历所有的img标签
		for (Element img : imgElements) {
			if (img.tagName().equals("img")) {
				// 获得img标签的属性real_src
				// 设置img标签的src与WIDTH值
				img.attr("WIDTH", "100%");
			}
		}

		return content.html();
	}
	
}
