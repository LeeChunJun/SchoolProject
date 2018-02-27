package com.leechunjun.school.bean;

public class TopicItem {
	public String title;
	public String time;
	public String url;
	
	public TopicItem() {
		super();
	}

	public TopicItem(String title, String time, String url) {
		super();
		this.title = title;
		this.time = time;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
