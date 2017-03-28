package com.noh.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class MyLog {
	private static final String TAG = "MyApplication";
	
	/**
	* Logs to logcat Debug
	* @param text - log message
	*/
	public static void l(String text) {
		Log.d(TAG, text);
	}
	
	/**
	* Logs to a Toast window
	* @param c - context in which this Toast view will be created
	* @param text - log message
	*/
	public static void t(Context c, String text) {
		Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
	}
	
	/**
	* Logs to a file (/sdcard/mylog.txt) - once it's created it keeps appending
	* @param text - log message
	*/
	public static void f(String text) {
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File f = new File(Environment.getExternalStorageDirectory(), "mylog.txt");
			try {
				if(!f.exists()) {
					f.createNewFile();
				}
				FileWriter fw = new FileWriter(f, true);
				fw.append(new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date()) + "\t" + text + "\r\n");
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

