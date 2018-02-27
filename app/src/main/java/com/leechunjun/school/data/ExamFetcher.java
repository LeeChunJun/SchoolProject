package com.leechunjun.school.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.leechunjun.school.bean.Exam;

import android.content.Context;
import android.util.Log;

public class ExamFetcher {
private static final String TAG = "ExamFetcher";
	
	@SuppressWarnings("unused")
	private Context context;
	private String id;
	private String password;
	private String time;
	private NetResponse netResponse;
	
	public ExamFetcher(Context context, String id, String password, String time, NetResponse netResponse) {
		super();
		this.context = context;
		this.id = id;
		this.password = password;
		this.time = time;
		this.netResponse = netResponse;
	}
	
	public String setCookies(String id, String password, String time)
			throws MalformedURLException, IOException, ProtocolException, UnsupportedEncodingException {
		String url1 = "http://jwc.jxnu.edu.cn/Default_Login.aspx?preurl=";
		URL url = new URL(url1);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.addRequestProperty("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
		connection.addRequestProperty("Referer", "http://jwc.jxnu.edu.cn/Default_Login.aspx?preurl=");
		connection.addRequestProperty("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
		connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.addRequestProperty("Accept-Encoding", "gzip, deflate");

		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");

		out.write(
				"__VIEWSTATE=%2FwEPDwUJNjk1MjA1MTY0D2QWAgIBD2QWBAIBD2QWBGYPEGRkFgFmZAIBDxAPFgYeDURhdGFUZXh0RmllbGQFDOWNleS9jeWQjeensB4ORGF0YVZhbHVlRmllbGQFCeWNleS9jeWPtx4LXyFEYXRhQm91bmRnZBAVPQnkv53ljavlpIQJ6LSi5Yqh5aSEEui0ouaUv%2BmHkeiejeWtpumZohLln47luILlu7rorr7lrabpmaIS5Yid562J5pWZ6IKy5a2m6ZmiDOS8oOaSreWtpumZoifliJvmlrDliJvkuJrmlZnogrLnoJTnqbbkuI7mjIflr7zkuK3lv4MJ5qGj5qGI6aaGFeWcsOeQhuS4jueOr%2BWig%2BWtpumZojDlj5HlsZXop4TliJLlip7lhazlrqTvvIjnnIHpg6jlhbHlu7rlip7lhazlrqTvvIkP6auY562J56CU56m26ZmiReWbvemZheWQiOS9nOS4juS6pOa1geWkhOOAgeaVmeiCsuWbvemZheWQiOS9nOS4jueVmeWtpuW3peS9nOWKnuWFrOWupBLlm73pmYXmlZnogrLlrabpmaIw5Zu95a625Y2V57OW5YyW5a2m5ZCI5oiQ5bel56iL5oqA5pyv56CU56m25Lit5b%2BDEuWMluWtpuWMluW3peWtpumZojDln7rlu7rnrqHnkIblpITvvIjlhbHpnZLmoKHljLrlu7rorr7lip7lhazlrqTvvIkb6K6h566X5py65L%2Bh5oGv5bel56iL5a2m6ZmiEue7p%2Be7reaVmeiCsuWtpumZohvmsZ%2Fopb%2Fnu4%2FmtY7lj5HlsZXnoJTnqbbpmaIP5pWZ5biI5pWZ6IKy5aSECeaVmeWKoeWkhAzmlZnogrLlrabpmaIP5pWZ6IKy56CU56m26ZmiHuWGm%2BS6i%2BaVmeeglOmDqO%2B8iOatpuijhemDqO%2B8iTnnp5HmioDlm63nrqHnkIblip7lhazlrqTvvIjnp5HmioDlm63lj5HlsZXmnInpmZDlhazlj7jvvIkP56eR5a2m5oqA5pyv5aSEEuenkeWtpuaKgOacr%2BWtpumZohLnprvpgIDkvJHlt6XkvZzlpIQb5Y6G5Y%2By5paH5YyW5LiO5peF5ri45a2m6ZmiFemprOWFi%2BaAneS4u%2BS5ieWtpumZogznvo7mnK%2FlrabpmaIS5YWN6LS55biI6IyD55Sf6ZmiNumEsemYs%2Ba5lua5v%2BWcsOS4jua1geWfn%2BeglOeptuaVmeiCsumDqOmHjeeCueWunumqjOWupB7pnZLlsbHmuZbmoKHljLrnrqHnkIblip7lhazlrqQJ5Lq65LqL5aSEDOi9r%2BS7tuWtpumZognllYblrabpmaIP56S%2B5Lya56eR5a2m5aSEEueUn%2BWRveenkeWtpuWtpumZoj%2FluIjotYTln7norq3kuK3lv4PvvIjmsZ%2Fopb%2FnnIHpq5jnrYnlrabmoKHluIjotYTln7norq3kuK3lv4PvvIkz5a6e6aqM5a6k5bu66K6%2B5LiO566h55CG5Lit5b%2BD44CB5YiG5p6Q5rWL6K%2BV5Lit5b%2BDG%2BaVsOWtpuS4juS%2FoeaBr%2BenkeWtpuWtpumZogzkvZPogrLlrabpmaIJ5Zu%2B5Lmm6aaGD%2BWkluWbveivreWtpumZojPnvZHnu5zljJbmlK%2FmkpHova%2Fku7blm73lrrblm73pmYXnp5HmioDlkIjkvZzln7rlnLAP5paH5YyW56CU56m26ZmiCeaWh%2BWtpumZoi3ml6DmnLrohpzmnZDmlpnlm73lrrblm73pmYXnp5HmioDlkIjkvZzln7rlnLAb54mp55CG5LiO6YCa5L%2Bh55S15a2Q5a2m6ZmiGOeOsOS7o%2BaVmeiCsuaKgOacr%2BS4reW%2Fgwzlv4PnkIblrabpmaIS5L%2Bh5oGv5YyW5Yqe5YWs5a6kD%2BWtpuaKpeadguW%2Fl%2Bekvh7lrabnlJ%2FlpITvvIjlrabnlJ%2Flt6XkvZzpg6jvvIk856CU56m255Sf6Zmi77yI5a2m56eR5bu66K6%2B5Yqe5YWs5a6k44CB56CU56m255Sf5bel5L2c6YOo77yJDOmfs%2BS5kOWtpumZog%2Fmi5vnlJ%2FlsLHkuJrlpIQM5pS%2F5rOV5a2m6ZmiHui1hOS6p%2Be7j%2BiQpeaciemZkOi0o%2BS7u%2BWFrOWPuBjotYTkuqfkuI7lkI7li6TnrqHnkIblpIQVPQgxODAgICAgIAgxNzAgICAgIAg2ODAwMCAgIAg2MzAwMCAgIAg4MjAwMCAgIAg2NDAwMCAgIAg4OTAwMCAgIAgxMDkgICAgIAg0ODAwMCAgIAgxMzYgICAgIAgxMzAgICAgIAgxNjAgICAgIAg2OTAwMCAgIAgzNjUgICAgIAg2MTAwMCAgIAgxNDQgICAgIAg2MjAwMCAgIAg0NTAgICAgIAgzMjQgICAgIAgyNTAgICAgIAgyNDAwMCAgIAg1MDAwMCAgIAgzOTAgICAgIAgzNzAwMCAgIAgxMzIgICAgIAgxNDAgICAgIAg4MTAwMCAgIAgxMDQgICAgIAg1ODAwMCAgIAg0NjAwMCAgIAg2NTAwMCAgIAg1NzAwMCAgIAgzMjAgICAgIAg0MDIgICAgIAgxNTAgICAgIAg2NzAwMCAgIAg1NDAwMCAgIAgzNjAgICAgIAg2NjAwMCAgIAgzMTAgICAgIAgxMDYgICAgIAg1NTAwMCAgIAg1NjAwMCAgIAgyOTAgICAgIAg1MjAwMCAgIAgzMDAgICAgIAgzNTAgICAgIAg1MTAwMCAgIAgzODAwMCAgIAg2MDAwMCAgIAgzNjEgICAgIAg0OTAwMCAgIAgzMDQgICAgIAg0MjAgICAgIAgxMTAgICAgIAgxOTAgICAgIAg1MzAwMCAgIAg0NDAgICAgIAg1OTAwMCAgIAgzMzAgICAgIAg4NzAwMCAgIBQrAz1nZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZGQCAw8PFgIeB1Zpc2libGVoZGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFClJlbWVuYmVyTWVyAzsNdL%2FkQ7iQYWFH4AJLjIM1nwEU%2FoBPMW22pDTNuA%3D%3D"
						+ "&__EVENTVALIDATION=%2FwEWSAKZ6MD0CwLr6%2B%2FkCQK3yfbSBAKDspbeCQL21fViApC695MMAsjQmpEOAsjQpo4OAv3S2u0DAv3S9t4DAqPW8tMDAv3S6tEDAqPW3ugDArWVmJEHAr%2FR2u0DAqrwhf4KAsjQtoIOAsjQooMOAv3S3ugDArfW7mMC%2FdL%2B0AMCvJDK9wsC%2FdLy0wMCr9GugA4C8pHSiQwC6dGugA4C%2BdHq0QMC3NH61QMCjtCenA4CntDm2gMCxrDmjQ0CyNCqhQ4Co9b%2B0AMCvJDaiwwC3NHa7QMCv9Hi3wMC%2FdLu3AMC3NHm2gMCjtCyhw4CpbHqgA0CyNCugA4C%2FdLm2gMC3NHq0QMCjtCigw4C%2FdLi3wMCjtC%2BhA4CqvCJ9QoC3NHu3AMC3NHi3wMC6dGenA4C3NHy0wMCjtC6mQ4CjtCugA4C3NH%2B0AMCntDa7QMC%2FdL61QMCw5bP%2FgICv9He6AMC8pHaiwwCr9Gyhw4CyNC%2BhA4CyNCenA4C3NH23gMCr9GqhQ4C3NHe6AMCjtC2gg4Co9bm2gMC%2BeuUqg4C2tqumwgC0sXgkQ8CuLeX%2BQECj8jxgAqZ6y%2BdHV1ejSxRnkBAdFDjB6a5Yy1hFOsWG1nrs4Jz4w%3D%3D"
						+ "&rblUserType=Student" + "&ddlCollege=180+++++" + "&StuNum=" + id + "&TeaNum="
						+ "&Password=" + password + "&login=%E7%99%BB%E5%BD%95");
		out.flush();
		out.close();
		connection.connect();

		InputStream in = connection.getInputStream();
		StringBuilder retStr = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String temp = br.readLine();
		while (temp != null) {
			retStr.append(temp);
			temp = br.readLine();
		}
		br.close();
		in.close();

		String cookies = connection.getHeaderField("Set-Cookie");
		return getExams(cookies);
	}
	
	public String getExams(String cookies) throws IOException{
		String urlPath = "http://jwc.jxnu.edu.cn/User/default.aspx?&code=129&&uctl=MyControl\\xfz_test_schedule.ascx";
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("GET");
		connection.addRequestProperty("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
		connection.addRequestProperty("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
		connection.addRequestProperty("Accept-Encoding", "gzip, deflate");
		connection.addRequestProperty("Cookie", cookies);
		connection.connect();

		InputStream in = connection.getInputStream();
		StringBuilder retStr = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String temp = br.readLine();
		while (temp != null) {
			retStr.append(temp);
			temp = br.readLine();
		}
		br.close();
		in.close();
		
		return retStr.toString();
		
	}

	public List<Exam> fetchItem(){
		List<Exam> examList = new ArrayList<Exam>();
		try {
			String examHtmlString = setCookies(id, password, time);
			parseItem(examList,examHtmlString);
		}catch (Exception e) {
			Log.e(TAG, "获取考试安排失败: "+e);
			netResponse.onFinished("获取考试安排失败!", -1);
		}
		
		return examList;
	}
	
	private void parseItem(List<Exam> examList, String examHtmlString) {
		Document doc = Jsoup.parse(examHtmlString);
		Element table = doc.getElementById("_ctl1_dgContent");
		
		Elements trs = table.getElementsByTag("tr");
		int k = 0;
		String[] c1 = new String[]{"","","",""};
        for(int i = 1;i<trs.size();i++){
        	Exam exam = new Exam();
        	k = 0;
            Elements tds = trs.get(i).select("td");
            for(int j = 0;j<tds.size();j++){
                String text = tds.get(j).text();
                if(j == 1){
                	c1[k++] = text;
                }else if(j==3){
                	c1[k++] = text;
                }else if(j==4){
                	c1[k++] = text;
                }else if(j==5){
                	c1[k++] = text;
                }
            }
            int q = 0;
            exam.setExamName(c1[q++]);
            exam.setExamTime(c1[q++]);
            exam.setExamClass(c1[q++]);
            exam.setExamSeat(c1[q++]);
            
            examList.add(exam);
        }
        netResponse.onFinished("获取考试安排成功!", 0);
	}

}
