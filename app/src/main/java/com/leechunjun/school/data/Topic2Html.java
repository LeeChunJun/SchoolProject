package com.leechunjun.school.data;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Topic2Html {
	private String result = "";// ���ص�html��ʽ����

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
		
		// �ҵ�����real_src���Ե�img��ǩ
		Elements imgElements = content.select("img[real_src]");
		// �Ҳ���ʱ��
		if (imgElements == null) {
			return content.html();
		}
		// �������е�img��ǩ
		for (Element img : imgElements) {
			if (img.tagName().equals("img")) {
				// ���img��ǩ������real_src
				// ����img��ǩ��src��WIDTHֵ
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
		// �Ҳ���ʱ��
		if (imgElements == null) {
			return phoneContent.html();
		}
		// �������е�img��ǩ
		for (Element img : imgElements) {
			if (img.tagName().equals("img")) {
				// ����img��ǩ��src��WIDTHֵ
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
