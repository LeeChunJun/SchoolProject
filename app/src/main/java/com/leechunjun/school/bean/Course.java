package com.leechunjun.school.bean;

public class Course {
	
	private String mWeek;
	private String mTime;
	private String mCourse;
	private String mClassroom;
	private String mNumber;
	private String mTeacher;
	private String mClass;
	
	public Course() {
		
	}

	public Course(String mWeek, String mTime, String mCourse,
			String mClassroom, String mNumber, String mTeacher, String mClass) {
		super();
		this.mWeek = mWeek;
		this.mTime = mTime;
		this.mCourse = mCourse;
		this.mClassroom = mClassroom;
		this.mNumber = mNumber;
		this.mTeacher = mTeacher;
		this.mClass = mClass;
	}

	public String getmWeek() {
		return mWeek;
	}

	public void setmWeek(String mWeek) {
		this.mWeek = mWeek;
	}

	public String getmTime() {
		return mTime;
	}

	public void setmTime(String mTime) {
		this.mTime = mTime;
	}

	public String getmCourse() {
		return mCourse;
	}

	public void setmCourse(String mCourse) {
		this.mCourse = mCourse;
	}

	public String getmClassroom() {
		return mClassroom;
	}

	public void setmClassroom(String mClassroom) {
		this.mClassroom = mClassroom;
	}

	public String getmNumber() {
		return mNumber;
	}

	public void setmNumber(String mNumber) {
		this.mNumber = mNumber;
	}

	public String getmTeacher() {
		return mTeacher;
	}

	public void setmTeacher(String mTeacher) {
		this.mTeacher = mTeacher;
	}

	public String getmClass() {
		return mClass;
	}

	public void setmClass(String mClass) {
		this.mClass = mClass;
	}
	
	
	
	
	
}
