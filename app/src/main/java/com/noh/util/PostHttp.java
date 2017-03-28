package com.noh.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;


public class PostHttp {
	private String result;		
	
	public String httpConnect(String url, ArrayList<NameValuePair> nameValuePairs)
	{
		InputStream is = null;
		result = "";
		HttpClient httpclient = new DefaultHttpClient();
		
		String log = url + "?";
		for(int i=0; i <nameValuePairs.size();i ++){
			log += nameValuePairs.get(i).getName();
			log += "=";
			log += nameValuePairs.get(i).getValue();
			log += "&";
		}
		Log.e(null, log);
		try {
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
		    HttpConnectionParams.setSoTimeout(params, 10000);
	
			HttpPost httppost = new HttpPost(url);
			UrlEncodedFormEntity entityRequest = 
					new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			httppost.setEntity(entityRequest);
			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entityResponse = response.getEntity();
			is = entityResponse.getContent();
			
			/** convert response to string */
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
		
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			is.close();
			result = sb.toString().trim();

		} catch (IOException e) {
			//Toast.makeText(make, "��Ʈ��ũ������ ���� �ʾҽ��ϴ�", Toast.LENGTH_LONG).show(); 
			//e.printStackTrace();
		} catch (Exception e) {
			//Toast.makeText(make, "��Ʈ��ũ������ ���� �ʾҽ��ϴ�", LENGTH_TO_SHOW).show(); 
			//e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
}