package com.leechunjun.school.data;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class News2Html {
	private String result = "";// ���ص�html��ʽ����

	public String parseArticle(String htmlurl) throws IOException {
		Document document = Jsoup.connect(htmlurl).timeout(8000).get();

		result = fetchContent(document);
		
		String htmlStart = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Insert title here</title></head><body>";
		String htmlEnd = "</body></html>";
		
		return htmlStart+result+htmlEnd;
	}

	public String fetchContent(Document document) {
		Element content = document.select("div.content").first();

		// �ҵ�����src���Ե�img��ǩ
		Elements imgElements = content.select("img[src]");
		// �Ҳ���ʱ��
		if (imgElements == null) {
			return content.html();
		}
		// �������е�img��ǩ
		for (Element img : imgElements) {
			if (img.tagName().equals("img")) {
				// ���img��ǩ������real_src
				// ����img��ǩ��src��WIDTHֵ
				img.attr("WIDTH", "100%");
			}
		}

		return content.html();
	}
	
}
