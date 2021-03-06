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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

@SuppressWarnings("deprecation")
public class CourseFetcher {
//	private static final String TAG = "CourseFetcher";

	public void sendRequest(final String id, final String password, final String time, final HttpReply httpReply) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String temp = setCookies(id, password, time);
					if (httpReply != null) {
						httpReply.onFinished(temp);
					}
				} catch (Exception e) {
					httpReply.onError(e);
				}
			}
		}).start();
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
						+ "&rblUserType=Student" + "&ddlCollege=180+++++" + "&StuNum=" + id + "&TeaNum=" + "&Password="
						+ password + "&login=%E7%99%BB%E5%BD%95");
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
		return getCourse(cookies, time);
	}

	public String getCourse(String cookies, String time) throws IOException {
		String urlPath = "http://jwc.jxnu.edu.cn/User/default.aspx?&&code=111&uctl=MyControl%5cxfz_kcb.ascx&MyAction=Personal";
		HttpPost request = new HttpPost(urlPath);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("_ctl1:btnSearch", "ȷ��"));
		// params.add(new BasicNameValuePair("_ctl1:ddlSterm", time));
		// params.add(new BasicNameValuePair("__EVENTVALIDATION",
		// "/wEWCwKOmc73CQKKhuW9AQLeg/vmBAL9g+OSAgLeg4+HCQL9g/eyBwLItunkDwLvttGQDQLItv0EAu+25bAOAubhijO/nyHFDzD8yrnRvjR6vyINo/Cnck3AHqvPUMuRj6xBdg=="));
		// params.add(new BasicNameValuePair("__VIEWSTATE",
		// "/wEPDwUJNzIzMTk0NzYzD2QWAgIBD2QWCgIBDw8WAh4EVGV4dAUfMjAxNuW5tDnmnIg25pelIOaYn+acn+S6jCZuYnNwO2RkAgUPDxYCHwAFGOW9k+WJjeS9jee9ru+8muivvueoi+ihqGRkAgcPDxYCHwAFLSAgIOasoui/juaCqO+8jCgxMjA4MDY1MDQxLFN0dWRlbnQpIOadjuaYpeS/imRkAgoPZBYEAgEPDxYCHghJbWFnZVVybAVDLi4vTXlDb250cm9sL0FsbF9QaG90b1Nob3cuYXNweD9Vc2VyTnVtPTEyMDgwNjUwNDEmVXNlclR5cGU9U3R1ZGVudGRkAgMPFgIfAAWbITxkaXYgaWQ9J21lbnVQYXJlbnRfMCcgY2xhc3M9J21lbnVQYXJlbnQnIG9uY2xpY2s9J21lbnVHcm91cFN3aXRjaCgwKTsnPuaIkeeahOS/oeaBrzwvZGl2PjxkaXYgaWQ9J21lbnVHcm91cDAnIGNsYXNzPSdtZW51R3JvdXAnPjxEaXYgY2xhc3M9J21lbnVJdGVtT24nIHRpdGxlPSfor77nqIvooagnPjxhIGhyZWY9ImRlZmF1bHQuYXNweD8mY29kZT0xMTEmJnVjdGw9TXlDb250cm9sXHhmel9rY2IuYXNjeCZNeUFjdGlvbj1QZXJzb25hbCIgdGFyZ2V0PSdwYXJlbnQnPuivvueoi+ihqDwvYT48L2Rpdj48RGl2IGNsYXNzPSdtZW51SXRlbScgdGl0bGU9J+WfuuacrOS/oeaBryc+PGEgaHJlZj0iLi5cTXlDb250cm9sXFN0dWRlbnRfSW5mb3JDaGVjay5hc3B4IiB0YXJnZXQ9J19ibGFuayc+5Z+65pys5L+h5oGvPC9hPjwvZGl2PjxEaXYgY2xhc3M9J21lbnVJdGVtJyB0aXRsZT0n5L+u5pS55a+G56CBJz48YSBocmVmPSJkZWZhdWx0LmFzcHg/JmNvZGU9MTEwJiZ1Y3RsPU15Q29udHJvbFxwZXJzb25hbF9jaGFuZ2Vwd2QuYXNjeCIgdGFyZ2V0PSdwYXJlbnQnPuS/ruaUueWvhueggTwvYT48L2Rpdj48RGl2IGNsYXNzPSdtZW51SXRlbScgdGl0bGU9J+WtpuexjemihOitpic+PGEgaHJlZj0iamF2YXNjcmlwdDpPcGVuV2luZG93KCd4ZnpfYnlzaC5hc2N4JkFjdGlvbj1QZXJzb25hbCcpOyIgdGFyZ2V0PScnPuWtpuexjemihOitpjwvYT48L2Rpdj48RGl2IGNsYXNzPSdtZW51SXRlbScgdGl0bGU9J+aWsOeUn+WvvOW4iCc+PGEgaHJlZj0iZGVmYXVsdC5hc3B4PyZjb2RlPTIxNCYmdWN0bD1NeUNvbnRyb2xcc3R1ZGVudF9teXRlYWNoZXIuYXNjeCIgdGFyZ2V0PSdwYXJlbnQnPuaWsOeUn+WvvOW4iDwvYT48L2Rpdj48RGl2IGNsYXNzPSdtZW51SXRlbScgdGl0bGU9J+ivvueoi+aIkOe7qSc+PGEgaHJlZj0iamF2YXNjcmlwdDpPcGVuV2luZG93KCd4ZnpfY2ouYXNjeCZBY3Rpb249UGVyc29uYWwnKTsiIHRhcmdldD0nJz7or77nqIvmiJDnu6k8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPScyMDE25bm05LiK5Y2K5bm057uT5Lia6KGl6ICD5oql5ZCNJz48YSBocmVmPSJqYXZhc2NyaXB0Ok9wZW5XaW5kb3coJ0pZQktfWFNfSW5kZXguYXNjeCcpOyIgdGFyZ2V0PScnPjIwMTblubTkuIrljYrlubTnu5PkuJrooaXogIPmiqXlkI08L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSflrrbplb/nmbvlvZUnPjxhIGhyZWY9ImRlZmF1bHQuYXNweD8mY29kZT0yMDMmJnVjdGw9TXlDb250cm9sXEp6X3N0dWRlbnRzZXR0aW5nLmFzY3giIHRhcmdldD0ncGFyZW50Jz7lrrbplb/nmbvlvZU8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSflj4zkuJPkuJrlj4zlrabkvY3or77nqIvlronmjpLooagnPjxhIGhyZWY9Ii4uXE15Q29udHJvbFxEZXp5X2tiLmFzcHgiIHRhcmdldD0nX2JsYW5rJz7lj4zkuJPkuJrlj4zlrabkvY3or77nqIvlronmjpLooag8L2E+PC9kaXY+PC9kaXY+PGRpdiBpZD0nbWVudVBhcmVudF8xJyBjbGFzcz0nbWVudVBhcmVudCcgb25jbGljaz0nbWVudUdyb3VwU3dpdGNoKDEpOyc+5YWs5YWx5pyN5YqhPC9kaXY+PGRpdiBpZD0nbWVudUdyb3VwMScgY2xhc3M9J21lbnVHcm91cCc+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSfln7nlhbvmlrnmoYgnPjxhIGhyZWY9ImRlZmF1bHQuYXNweD8mY29kZT0xMDQmJnVjdGw9TXlDb250cm9sXGFsbF9qeGpoLmFzY3giIHRhcmdldD0ncGFyZW50Jz7ln7nlhbvmlrnmoYg8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSfor77nqIvkv6Hmga8nPjxhIGhyZWY9ImRlZmF1bHQuYXNweD8mY29kZT0xMTYmJnVjdGw9TXlDb250cm9sXGFsbF9jb3Vyc2VzZWFyY2guYXNjeCIgdGFyZ2V0PSdwYXJlbnQnPuivvueoi+S/oeaBrzwvYT48L2Rpdj48RGl2IGNsYXNzPSdtZW51SXRlbScgdGl0bGU9J+W8gOivvuWuieaOkic+PGEgaHJlZj0iLi5cTXlDb250cm9sXFB1YmxpY19La2FwLmFzcHgiIHRhcmdldD0nX2JsYW5rJz7lvIDor77lronmjpI8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSflrabnlJ/kv6Hmga8nPjxhIGhyZWY9ImRlZmF1bHQuYXNweD8mY29kZT0xMTkmJnVjdGw9TXlDb250cm9sXGFsbF9zZWFyY2hzdHVkZW50LmFzY3giIHRhcmdldD0ncGFyZW50Jz7lrabnlJ/kv6Hmga88L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSfmlZnlt6Xkv6Hmga8nPjxhIGhyZWY9ImRlZmF1bHQuYXNweD8mY29kZT0xMjAmJnVjdGw9TXlDb250cm9sXGFsbF90ZWFjaGVyLmFzY3giIHRhcmdldD0ncGFyZW50Jz7mlZnlt6Xkv6Hmga88L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSfnn63kv6HlubPlj7AnPjxhIGhyZWY9ImRlZmF1bHQuYXNweD8mY29kZT0xMjImJnVjdGw9TXlDb250cm9sXG1haWxfbGlzdC5hc2N4IiB0YXJnZXQ9J3BhcmVudCc+55+t5L+h5bmz5Y+wPC9hPjwvZGl2PjxEaXYgY2xhc3M9J21lbnVJdGVtJyB0aXRsZT0n5pWZ5a6k5pWZ5a2m5a6J5o6SJz48YSBocmVmPSIuLlxNeUNvbnRyb2xccHVibGljX2NsYXNzcm9vbS5hc3B4IiB0YXJnZXQ9J19ibGFuayc+5pWZ5a6k5pWZ5a2m5a6J5o6SPC9hPjwvZGl2PjxEaXYgY2xhc3M9J21lbnVJdGVtJyB0aXRsZT0n5pyf5pyr5oiQ57up5p+l6K+iJz48YSBocmVmPSJqYXZhc2NyaXB0Ok9wZW5XaW5kb3coJ3hmel9UZXN0X2NqLmFzY3gnKTsiIHRhcmdldD0nJz7mnJ/mnKvmiJDnu6nmn6Xor6I8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSfmnJ/mnKvmiJDnu6nmn6XliIbnlLPor7cnPjxhIGhyZWY9ImphdmFzY3JpcHQ6T3BlbldpbmRvdygnQ2ZzcV9TdHVkZW50LmFzY3gnKTsiIHRhcmdldD0nJz7mnJ/mnKvmiJDnu6nmn6XliIbnlLPor7c8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSfooaXnvJPogIPlronmjpInPjxhIGhyZWY9ImphdmFzY3JpcHQ6T3BlbldpbmRvdygneGZ6X1Rlc3RfQkhLLmFzY3gnKTsiIHRhcmdldD0nJz7ooaXnvJPogIPlronmjpI8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSflrabkuaDpl67nrZQnPjxhIGhyZWY9ImRlZmF1bHQuYXNweD8mY29kZT0xNTkmJnVjdGw9TXlDb250cm9sXEFsbF9TdHVkeV9MaXN0LmFzY3giIHRhcmdldD0ncGFyZW50Jz7lrabkuaDpl67nrZQ8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSflj4zlrabkvY3or77nqIvmiJDnu6knPjxhIGhyZWY9ImphdmFzY3JpcHQ6T3BlbldpbmRvdygnZGV6eV9jai5hc2N4JkFjdGlvbj1QZXJzb25hbCcpOyIgdGFyZ2V0PScnPuWPjOWtpuS9jeivvueoi+aIkOe7qTwvYT48L2Rpdj48RGl2IGNsYXNzPSdtZW51SXRlbScgdGl0bGU9J+avleS4mueUn+WbvuWDj+mHh+mbhuS/oeaBr+agoeWvuSc+PGEgaHJlZj0iLi5cTXlDb250cm9sXFRYQ0pfSW5mb3JDaGVjay5hc3B4IiB0YXJnZXQ9J19ibGFuayc+5q+V5Lia55Sf5Zu+5YOP6YeH6ZuG5L+h5oGv5qCh5a+5PC9hPjwvZGl2PjwvZGl2PjxkaXYgaWQ9J21lbnVQYXJlbnRfMicgY2xhc3M9J21lbnVQYXJlbnQnIG9uY2xpY2s9J21lbnVHcm91cFN3aXRjaCgyKTsnPuaVmeWtpuS/oeaBrzwvZGl2PjxkaXYgaWQ9J21lbnVHcm91cDInIGNsYXNzPSdtZW51R3JvdXAnPjxEaXYgY2xhc3M9J21lbnVJdGVtJyB0aXRsZT0n572R5LiK6K+E5pWZJz48YSBocmVmPSJqYXZhc2NyaXB0Ok9wZW5XaW5kb3coJ3BqX3N0dWRlbnRfaW5kZXguYXNjeCcpOyIgdGFyZ2V0PScnPue9keS4iuivhOaVmTwvYT48L2Rpdj48RGl2IGNsYXNzPSdtZW51SXRlbScgdGl0bGU9J+aVmeWKoeaEj+ingeeusSc+PGEgaHJlZj0iLi4vRGVmYXVsdC5hc3B4P0FjdGlvbj1BZHZpc2UiIHRhcmdldD0nX2JsYW5rJz7mlZnliqHmhI/op4HnrrE8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSfmnJ/mnKvogIPor5XlronmjpInPjxhIGhyZWY9ImRlZmF1bHQuYXNweD8mY29kZT0xMjkmJnVjdGw9TXlDb250cm9sXHhmel90ZXN0X3NjaGVkdWxlLmFzY3giIHRhcmdldD0ncGFyZW50Jz7mnJ/mnKvogIPor5XlronmjpI8L2E+PC9kaXY+PERpdiBjbGFzcz0nbWVudUl0ZW0nIHRpdGxlPSfovoXkv67lj4zkuJPkuJrlj4zlrabkvY3miqXlkI0nPjxhIGhyZWY9ImphdmFzY3JpcHQ6T3BlbldpbmRvdygnRGV6eV9ibS5hc2N4Jyk7IiB0YXJnZXQ9Jyc+6L6F5L+u5Y+M5LiT5Lia5Y+M5a2m5L2N5oql5ZCNPC9hPjwvZGl2PjwvZGl2PmQCDA9kFgJmD2QWDAIBDw8WAh8ABR7msZ/opb/luIjojIPlpKflrablrabnlJ/or77ooahkZAIDDw8WAh8ABYsB54+t57qn5ZCN56ew77yaPFU+MTLnuqforqHnrpfmnLrnp5HlrabmioDmnK/vvIjova/ku7bmnI3liqHlpJbljIXvvIkx54+tPC9VPuOAgOOAgOWtpuWPt++8mjxVPjEyMDgwNjUwNDE8L3U+44CA44CA5aeT5ZCN77yaPHU+5p2O5pil5L+KPC91PmRkAgUPEA8WBh4NRGF0YVRleHRGaWVsZAUM5a2m5pyf5ZCN56ewHg5EYXRhVmFsdWVGaWVsZAUM5byA5a2m5pel5pyfHgtfIURhdGFCb3VuZGdkEBUIDzE2LTE356ysMeWtpuacnw8xNS0xNuesrDLlrabmnJ8PMTUtMTbnrKwx5a2m5pyfDzE0LTE156ysMuWtpuacnw8xNC0xNeesrDHlrabmnJ8PMTMtMTTnrKwy5a2m5pyfDzEzLTE056ysMeWtpuacnw8xMi0xM+esrDLlrabmnJ8VCBAyMDE2LzkvMSAwOjAwOjAwEDIwMTYvMy8xIDA6MDA6MDAQMjAxNS85LzEgMDowMDowMBAyMDE1LzMvMSAwOjAwOjAwEDIwMTQvOS8xIDA6MDA6MDAQMjAxNC8zLzEgMDowMDowMBAyMDEzLzkvMSAwOjAwOjAwEDIwMTMvMy8xIDA6MDA6MDAUKwMIZ2dnZ2dnZ2dkZAIJDw8WAh4HVmlzaWJsZWhkZAIKDzwrAAsBAA8WCB4IRGF0YUtleXMWAB4LXyFJdGVtQ291bnQC/////w8eFV8hRGF0YVNvdXJjZUl0ZW1Db3VudAL/////Dx4JUGFnZUNvdW50ZmRkAgsPPCsACwEADxYIHwYWAB8HAv////8PHwgC/////w8fCWZkZGRhub84fByK5BoNU3Ic3ehUqsMyaVNMIw1X2fZoW0z9gw=="));

		request.setHeader("Cookie", cookies);
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);

		int temp = response.getStatusLine().getStatusCode();
		StringBuffer sb = new StringBuffer();

		if (temp == 200) {
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String data = "";
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}

		}
		return Course2Json.parseCourse(sb.toString());
	}

	

}
