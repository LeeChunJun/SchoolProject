package com.leechunjun.school.data;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.leechunjun.school.bean.BookItem;

import android.content.Context;
import android.util.Log;

@SuppressWarnings("deprecation")
public class LibraryFetcher {
	private static final String TAG = "LibraryFetcher";
	private static final String urlBase = "http://219.229.250.138:8080/reader";
	private static final String Cookie = "ASPSESSIONIDCARSRTTQ=MJFOGHLAHHEFFDGPHBGHMLNK; PHPSESSID=edmd5ua5k33hl1ec7mn65flak7";
	private static final String User_Agent = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";
	
	@SuppressWarnings("unused")
	private Context context;
	private NetResponse netResponse;
	private String id;
	private String password;
	
	public LibraryFetcher(Context context, NetResponse netResponse, String id, String password) {
		super();
		this.context = context;
		this.netResponse = netResponse;
		this.id = id;
		this.password = password;
	}
	
	public List<BookItem> fetchItems() {
		List<BookItem> booklist = new ArrayList<BookItem>();

		String html = "";
		try {
			boolean isLogin = grab1(id, password);
			if (isLogin) {
				html = grab2();
				parseItem(booklist, html);
			}
		} catch (IOException e) {
			netResponse.onFinished("ÏÂÔØÍøÒ³³ö´í!", -1);
			Log.e(TAG, "½âÎöÍøÒ³³ö´í!"+e);
		} catch (NullPointerException e) {
			netResponse.onFinished("½èÔÄÊé¼®Îª¿Õ!", -2);
			Log.e(TAG, "½èÔÄÊé¼®Îª¿Õ!"+e);
		}

		return booklist;
	}

	private void parseItem(List<BookItem> booklist, String html) throws NullPointerException{
		Document document = Jsoup.parse(html);
		Elements books = document.getElementById("mylib_content").select("table").first().select("tr");
		for (int i = 1; i < books.size(); i++) {
			Element book = books.get(i);
			Elements bookContent = book.select("td");
			
			BookItem bookitem = new BookItem();
			bookitem.setNum(bookContent.get(0).text());
			bookitem.setName(bookContent.get(1).text());
			bookitem.setStartTime(bookContent.get(2).text());
			bookitem.setEndTime(bookContent.get(3).text());
			bookitem.setTimes(bookContent.get(4).text());
			bookitem.setPlace(bookContent.get(5).text());
			booklist.add(bookitem);
		}

	}

	private boolean grab1(String number, String passwd) throws IOException {
		boolean result = false;
		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("number", number));
		formparams.add(new BasicNameValuePair("passwd", passwd));
		formparams.add(new BasicNameValuePair("select", "cert_no"));
		formparams.add(new BasicNameValuePair("returnUrl", ""));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,"utf-8");
		HttpPost httppost = new HttpPost(urlBase + "/redr_verify.php");
		httppost.setEntity(entity);
		httppost.setHeader("Cookie", Cookie);
		httppost.setHeader("User-Agent", User_Agent);
		HttpResponse response = httpclient.execute(httppost);
		if (response.getStatusLine().getStatusCode() == 200) {
			httpclient.getConnectionManager().shutdown();
			result = true;
		} else {
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

	private String grab2() throws IOException {
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(urlBase + "/book_lst.php");
		httpget.setHeader("Cookie", Cookie);
		httpget.setHeader("User-Agent", User_Agent);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream in = entity.getContent();
			BufferedReader read = new BufferedReader(new InputStreamReader(in, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = read.readLine()) != null) {
				sb.append(line);
			}
			result = sb.toString();
		}
		httpclient.getConnectionManager().shutdown();
		return result;
	}
	
	

}
