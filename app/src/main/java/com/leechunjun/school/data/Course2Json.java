package com.leechunjun.school.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.leechunjun.school.bean.Course;


public class Course2Json {

	public static String parseCourse(String html){
		List<Course> bean = new ArrayList<Course>();
		
		String[] time = {"08:00-09:30","09:40-10:20","10:30-11:10","11:20-12:00","14:00-15:30","15:40-17:10","19:00-20:30"};
		String[] week = {"周一","周二","周三","周四","周五","周六","周日"};
    	
    	Document document = Jsoup.parse(html);
    	
    	Element table1 = document.getElementById("_ctl1_NewKcb").getElementsByTag("TABLE").first();
    	Element table2 = document.getElementById("_ctl1_dgStudentLesson");
    	
    	//课表教室----------------------
    	Elements course1 = table1.getElementsByTag("tr");
    	//12节课
    	Element course12 = course1.get(1);
    	getCourse(bean, time, week, course12, 0, 1);
    	//3节课
    	Element course3 = course1.get(2);
    	getCourse(bean, time, week, course3, 1, 1);
    	//4节课
    	Element course4 = course1.get(3);
    	getCourse(bean, time, week, course4, 2, 1);
    	//5节课
    	Element course5 = course1.get(4);
    	getCourse(bean, time, week, course5, 3, 1);
    	//67节课
    	Element course67 = course1.get(6);
    	getCourse(bean, time, week, course67, 4, 2);
    	//89节课
    	Element course89 = course1.get(7);
    	getCourse(bean, time, week, course89, 5, 1);
    	//晚上
    	Element course10 = course1.get(8);
    	getCourse(bean, time, week, course10, 6, 2);
    	
    	//课程详细------------------------------------------
    	Elements course2 = table2.getElementsByTag("tr");
    	for (int i = 1; i < course2.size(); i++) {
    		Element line = course2.get(i);
    		Elements line_tds = line.getElementsByTag("td");
    		Element number_td = line_tds.get(0);
    		Element course_td = line_tds.get(1);
    		Element teacher_td = line_tds.get(3);
    		
    		String number_td_string = number_td.text();
    		String course_td_string = course_td.text();
    		String teacher_td_string = teacher_td.text();
    		
    		//取出课程的课程号和老师
    		for (Iterator<Course> iterator = bean.iterator(); iterator.hasNext();) {
    			Course cb = (Course) iterator.next();
    			if(cb.getmCourse().equals(course_td_string)){
    				cb.setmNumber(number_td_string);
    				cb.setmTeacher(teacher_td_string);
    			}
    		}
    		
    	}
    	//将信息合成json数据格式
    	StringBuilder jsonString = new StringBuilder();
    	jsonString.append("[");
    	for (Iterator<Course> iterator = bean.iterator(); iterator.hasNext();) {
			Course cb = (Course) iterator.next();
			
			jsonString.append("{\"week\":\""+cb.getmWeek()+"\""+",");
			jsonString.append("\"time\":\""+cb.getmTime()+"\""+",");
			jsonString.append("\"course\":\""+cb.getmCourse()+"\""+",");
			jsonString.append("\"classroom\":\""+cb.getmClassroom()+"\""+",");
			jsonString.append("\"number\":\""+cb.getmNumber()+"\""+",");
			jsonString.append("\"teacher\":\""+cb.getmTeacher()+"\""+",");
			jsonString.append("\"class\":\""+cb.getmClass()+"\""+"},");
			
		}
    	jsonString.deleteCharAt(jsonString.length()-1);
    	jsonString.append("]");
    	
    	return jsonString.toString();
    }

	public static void getCourse(List<Course> bean, String[] time,
			String[] week, Element course12, int nowTime, int init) {
		Elements course_tds = course12.select("div[align=center]");
    	for (int i = init; i < course_tds.size(); i++) {
			Element course_td = course_tds.get(i);
			String temp = course_td.html();
			if(temp.contains("<br>")){
				Course cb= new Course();
				cb.setmTime(time[nowTime]);
				cb.setmWeek(week[i-init]);

				String t = temp.substring(0,temp.length()-7);
				String[] tt = t.split("<br>");
				
				tt[0] = tt[0].trim();
				tt[1] = tt[1].trim();
				tt[2] = tt[2].trim();
				
				cb.setmCourse(tt[0]);
				cb.setmClassroom(tt[1]);
				cb.setmClass(tt[2]);
				bean.add(cb);
			}
			
		}
	}

}
