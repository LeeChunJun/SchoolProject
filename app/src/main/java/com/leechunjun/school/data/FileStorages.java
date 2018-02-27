package com.leechunjun.school.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.leechunjun.school.util.MyApplication;

import android.content.Context;

public class FileStorages {

	private Context context;

	public FileStorages(Context context) {
		super();
		this.context = MyApplication.getContext();
	}

	public void save(String file, String data) {
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = context.openFileOutput(file, Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String load(String file) throws IOException {
		FileInputStream in = null;
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			in = context.openFileInput(file);
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content.toString();
	}
}
