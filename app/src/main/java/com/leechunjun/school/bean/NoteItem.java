package com.leechunjun.school.bean;
public class NoteItem {
	public String title;
	public String content;
	public String time;
	public NoteItem() {
		super();
	}
	public NoteItem(String title, String content, String time) {
		super();
		this.title = title;
		this.content = content;
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
