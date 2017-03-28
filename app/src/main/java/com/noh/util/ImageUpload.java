package com.noh.util;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

public class ImageUpload {
	
    private FileInputStream mFileInputStream = null;
    private URL connectUrl = null;
    
	public String HttpFileUpload(String urlString, String fileName, String sendResult) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String result = "";
    	String addFileName;
    	
        try {           	
            mFileInputStream = new FileInputStream(fileName);            
            connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream is " + mFileInputStream);
            
            //open connection 
            HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();            
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            
            addFileName = fileName + "pubcap" + sendResult;
            //write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + addFileName+"\"" + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"id\";filename=\"" + 1 +"\"" + lineEnd); 
            dos.writeBytes(lineEnd);
            
            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            
            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            
            //Log.e("Test", "image byte is " + bytesRead);
            
            //read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }    
            
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            
            //close streams
           // Log.e("Test" , "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...            
            
            //get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }
            result = b.toString().trim(); 
            Log.e("Test", "result = " + result);
            dos.close();            
            
        } catch (Exception e) {
            //Log.d("Test", "exception " + e.getMessage());
            //TODO: handle exception
        }
        return result;
    }
}

