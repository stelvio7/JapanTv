package com.noh.util; 

import java.io.BufferedReader;  
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.japantv.settop1.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
public class Util {
	
	/*public static String getMacAddress(Context context) {
		WifiManager wifiman = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);        
		WifiInfo wifiinfo = wifiman.getConnectionInfo();  

		String strMacAddress = "";
		if (wifiinfo.getMacAddress() != null)  {
		    strMacAddress = wifiinfo.getMacAddress();
		}
		return strMacAddress;
	}*/
	public static ImageLoaderConfiguration getConfig(Context context){
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .diskCacheExtraOptions(720, 1280, null)
        .threadPoolSize(3) // default
        .threadPriority(Thread.NORM_PRIORITY - 2) // default
        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
        .denyCacheImageMultipleSizesInMemory()
        .memoryCache(new LruMemoryCache(30 * 1024 * 1024))
        .memoryCacheSize(30 * 1024 * 1024)
        .memoryCacheSizePercentage(13) // default
        .diskCacheSize(200 * 1024 * 1024)
        .diskCacheFileCount(800)
        .build();
		return config;
	}
	
	public static DisplayImageOptions getImageLoaderOption(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.bgimage) // 로딩중 이미지 설정
        .showImageForEmptyUri(R.drawable.bgimage) // Uri주소가 잘못되었을경우(이미지없을때)
        .showImageOnFail(R.drawable.bgimage) // 로딩 실패시
        .resetViewBeforeLoading(false)  // 로딩전에 뷰를 리셋하는건데 false로 하세요 과부하!
       // .delayBeforeLoading(0) // 로딩전 딜레이라는데 필요한일이 있을까요..?ㅋㅋ
        .cacheInMemory(true) // 메모리케시 사용여부
        .cacheOnDisk(true) // 디스크캐쉬를 사용여부(사용하세요왠만하면)
        .considerExifParams(false) // 사진이미지의 회전률 고려할건지
        .build();
		
		return options;
	}
	

	
	public static boolean isNetworkConnectionAvailable(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null)
			return false;
		State network = info.getState();
		return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
	}
	
	//LCD 가로사이즈
	public static int getWidth(Context c){
		Display defaultDisplay = 
			 ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		return defaultDisplay.getWidth();                           // 가로
	}
	
	//LCD 가로사이즈
	public static int getHeight(Context c){
		Display defaultDisplay = 
			 ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		return defaultDisplay.getHeight();                           // 가로
	}
	
	public static boolean getChildset(Context context){
		SharedPreferences sp = context.getSharedPreferences(Util.getApplicationName(context), Context.MODE_PRIVATE);
		return sp.getBoolean("chilset", false);
	}
	 
	public static String loadFileAsString(String filePath) throws java.io.IOException{ 
	    StringBuffer fileData = new StringBuffer(1000); 
	    BufferedReader reader = new BufferedReader(new FileReader(filePath)); 
	    char[] buf = new char[1024]; 
	    int numRead=0; 
	    while((numRead=reader.read(buf)) != -1){ 
	        String readData = String.valueOf(buf, 0, numRead); 
	        fileData.append(readData); 
	    } 
	    reader.close(); 
	    return fileData.toString(); 
	} 
	

	public static boolean checkNetwordState(Context context) {

	    ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo state_3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    NetworkInfo state_wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    NetworkInfo state_net = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
	    
	    if(state_wifi.isAvailable()){
	      return true;
	    }
	    else if(state_3g.isAvailable()){
	      return true;
	    }
	    if(state_net != null){
		    if(state_net.isAvailable()){
		      return true;
		    }
	    }
	   return false;
	}

	 
	/* 
	 * Get the STB MacAddress 
	 */ 
	public static String getMacAddress(Context context){ 
		SharedPreferences sp = context.getSharedPreferences(Util.getApplicationName(context), Context.MODE_PRIVATE);
		return sp.getString("uuid", "");
	} 
	
	public static void setMacAddress(Context context){ 
	    /*try { 
	        return loadFileAsString("/sys/class/net/eth0/address") 
	            .toUpperCase().substring(0, 17); 
	    } catch (IOException e) { 
	        e.printStackTrace(); 
	        return null; 
	    } */
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        
        SharedPreferences sp = context.getSharedPreferences(Util.getApplicationName(context), Context.MODE_PRIVATE);
		 SharedPreferences.Editor editor = sp.edit();
		 editor.putString("uuid", deviceId);
		 editor.commit();
		 
        //return "00000000-5622-0d5c-9268-258d0033c587";
        //return "00000000-3c99-ab0f-0167-e3460ea74814";

	} 

	
	public static String getApplicationName(Context context) {
        String name = "?";
        if(context != null) {
        	try {
                PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                name = context.getString(pi.applicationInfo.labelRes);
            } catch (PackageManager.NameNotFoundException e) {
            };
        }
        
        return name;
	}
	
	private static void showSystemBar() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{
                    "am","startservice","-n","com.android.systemui/.SystemUIService"});
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
	} 
	
	 private static void hideSystemBar() {
		 Process proc = null;
		 try {
		  proc = Runtime.getRuntime().exec("service call activity 42 s16 com.android.systemui");
		 } catch (IOException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		 }
	
	 }

}
