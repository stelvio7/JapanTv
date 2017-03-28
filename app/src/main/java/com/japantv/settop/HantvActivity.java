package com.japantv.settop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.japantv.settop1.R;
import com.japantv.model.BroadcastList;
import com.japantv.model.Constant;
import com.japantv.setting.HelpActivity;
import com.noh.util.PostHttp;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class HantvActivity extends Activity{
    /** Called when the activity is first created. */
	private UrlImageLoader urlImageload;
	private String mExpiredate = null;
	private boolean isLoaded = false;
	private ImageView ivTel = null;
	private Calendar nowCal = null;
	private Calendar expireCal = null;
	private ImageView imgViewNotice = null;
	private ViewNoticeTask viewNoticeTask = null;
	private ViewExpireDateTask viewExpireDateTask = null;
	protected boolean hasUpdate = false;
	private TextView versionName;
	public HantvActivity(){
		nowCal = Calendar.getInstance();
        expireCal = Calendar.getInstance();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	urlImageload = new UrlImageLoader();
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);

        if(getMacaddress().equals("")){
        	Util.setMacAddress(this.getBaseContext());
        }
        //맥어드레스 저장
        macaddress_save();

		versionName = (TextView)findViewById(R.id.versionName);
		versionName.setText("ver " + getVersionName(getBaseContext()));

    	viewExpireDateTask = new ViewExpireDateTask(getBaseContext());
    	viewExpireDateTask.execute();
        ivTel = (ImageView)findViewById(R.id.tel);
       
        //ivTel.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        
        
        imgViewNotice = (ImageView) findViewById(R.id.ImgViewNotice);


		checkVersion();
        
        isLoaded = true;
        
        if(getChildNum().equals("")){
        	saveChildNum("0000");
        	saveChildSet(false);
        }
        
       /* if(!Util.isNetworkConnectionAvailable(this.getBaseContext())){
        	AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage(R.string.videocancel);
		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	                Intent intent = new Intent(HantvActivity.this, HelpActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
	            }
	        });
		    AlertDialog alert = alt_bld.create();
		    alert.show();
        }*/
        if(!Util.checkNetwordState(this.getBaseContext())){
        	AlertDialog.Builder alt_bld = new AlertDialog.Builder(HantvActivity.this);
		    alt_bld.setMessage(R.string.nonet);
		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	                //Intent intent = new Intent(HantvActivity.this, MainMenuActivity.class);
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//startActivity(intent);
					finish();
	            }
	        });
		    AlertDialog alert = alt_bld.create();
		    alert.show();
			return;
        }
    }
    
    private void saveChildSet(boolean passwd){
		 SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		 SharedPreferences.Editor editor = sp.edit();
		 editor.putBoolean("chilset", passwd);
		 editor.commit();
	 }
	 
	 private boolean getChildset(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getBoolean("chilset", false);
	}
	 
	 private void saveChildNum(String passwd){
		 SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		 SharedPreferences.Editor editor = sp.edit();
		 editor.putString("childnum", passwd);
		 editor.commit();
	 }
	
	 private String getChildNum(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("childnum", "");
	}
	 
	
	 private String getMacaddress(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("macaddress", "");
	}
	
	private void macaddress_save(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("macaddress", Util.getMacAddress(getApplicationContext()));
		editor.commit();
		//Log.e(null, "" +Util.getMacAddress(getApplicationContext()));
	}
	
	private void saveAuth(boolean isAuth){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		if(isAuth)
			editor.putString("auth", "ok");
		else
			editor.putString("auth", "");
		editor.commit();
	}
	
	private void saveExpireDate(){
        if(mExpiredate != null){
	        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("expiredate", mExpiredate);
			editor.commit();
        }
	}
	
	private String getExpireDate(String macAdress){
		String expireDate = "";
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//macAdress = "123456789";
		nameValuePairs.add(new BasicNameValuePair("id", macAdress));
		
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/expiredate.php", nameValuePairs);
		try{
			JSONObject json_data = new JSONObject(strJson);
			expireDate = json_data.getString("date");
			
		}catch(JSONException e){
			//Log.e(null, e.toString()); 
		}
		String[] st = new String[3];
		st = expireDate.split("-");
		if(!expireDate.equals("")){
			expireCal.set(Integer.valueOf(st[0]), Integer.valueOf(st[1])-1, Integer.valueOf(st[2]));
		}else{
			expireCal.set(0, 0, 1);
		}
		if(st.length > 1)
			expireDate = st[1] + st[2];
		return expireDate;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(urlImageload != null)
			urlImageload = null;
		if(viewNoticeTask != null){
			viewNoticeTask.cancel(true);
			viewNoticeTask = null;
		}
		if(viewExpireDateTask != null){
			viewExpireDateTask.cancel(true);
			viewExpireDateTask = null;
		}
		
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	boolean go = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(isLoaded){
			Intent intent = new Intent(HantvActivity.this, MainMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if(!go){
				go = true;
				startActivity(intent);
				this.finish();
			}
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
  	  
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
			if(isLoaded){
				 Log.e(null, "sdfsdfasdf3");
				Intent intent = new Intent(HantvActivity.this, MainMenuActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void checkVersion(){
		VersionTask task = new VersionTask(getBaseContext());
		task.execute();
	}

	class VersionTask extends AsyncTask<String, Integer, Boolean> {
		private Context mContext;
		boolean success = false;

		private String appver;
		private String marketUrl;
		private String showUpdateText;

		public VersionTask(Context context) {
			mContext = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean loginSuccess = false;
			String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			strJson = postmake.httpConnect(
    				/*Constant.mainUrl + */Constant.mainUrl + "/module/tv/version_check.php", nameValuePairs);
			try{
				JSONObject json_data = new JSONObject(strJson);
				String items = json_data.getString("item");
				JSONObject items_data = new JSONObject(items);

				if(items_data.getString("resultCode").equals("0")){
					appver = items_data.getString("marketAppVer");
					marketUrl = items_data.getString("marketUrl");
					loginSuccess = true;
				}else if(items_data.getString("resultCode").equals("1")){
					loginSuccess = false;
				}
			}catch(JSONException e){

			}
			if(loginSuccess)
				return true;
			else
				return false;
		}


		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub

			if (result) {
				if(Integer.valueOf(appver) > getVersionCode(HantvActivity.this)){
					hasUpdate = true;
					AlertDialog.Builder alt_bld = new AlertDialog.Builder(HantvActivity.this);
					alt_bld.setMessage(R.string.update);
					alt_bld.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							UpdateApp atualizaApp = new UpdateApp();
							atualizaApp.setContext(getApplicationContext());
							atualizaApp.execute(marketUrl);
						}
					});
					alt_bld.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					AlertDialog alert = alt_bld.create();
					alert.show();
					return;
				}else{
					//handler =  new Handler();
					//handler.postDelayed(irun, 1500);
					//deviceInfo();
				}
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			//	mImageView.scrollTo(mScrollStep++, 0);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();

		}
	}

	public String getVersionName(Context context) {
		String name = "";
		if(context != null) {
			try {
				PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				name = pi.versionName;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			};
		}
		return name;
	}

	public int getVersionCode(Context context) {
		int code = 0;
		if(context != null) {
			try {
				PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				code = pi.versionCode;
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			};
		}
		return code;
	}

	public class UpdateApp extends AsyncTask<String,Void,Long>{
		private Context context;
		public void setContext(Context contextf){
			context = contextf;

		}

		@Override
		protected final void onPreExecute() {
			showProgress();
		}

		@Override
		protected Long doInBackground(String... arg0) {
			try {
				URL url = new URL(arg0[0]);
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();

				String PATH = "/mnt/sdcard/Download/";
				File file = new File(PATH);
				file.mkdirs();
				File outputFile = new File(file, "update.apk");
				if(outputFile.exists()){
					outputFile.delete();
				}
				FileOutputStream fos = new FileOutputStream(outputFile);

				InputStream is = c.getInputStream();

				byte[] buffer = new byte[1024];
				int len1 = 0;
				while ((len1 = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len1);
				}
				fos.close();
				is.close();

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/update.apk")), "application/vnd.android.package-archive");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
				context.startActivity(intent);


			} catch (Exception e) {
				Log.e("UpdateAPP", "Update error! " + e.getMessage());
			}
			return null;
		}

		private ProgressDialog mProgress;
		protected void showProgress() {
			mProgress = new ProgressDialog(HantvActivity.this);
			mProgress.setCancelable(true);
			mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					UpdateApp.this.cancel(true);
				}
			});


			String message = getString(R.string.str_wait);
			mProgress.setMessage(message);
			mProgress.show();
		}

		protected void dismissProgress() {

			if (mProgress != null) {
				mProgress.dismiss();
			}
		}

		protected void showCancelMessage() {
			dismissProgress();
			//Toast.makeText(context, mTaskCancelledMessage, Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			dismissProgress();
		}
	}
	
	class ViewExpireDateTask extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<BroadcastList> list = null;
    	private String text;
    	private Context mContext;
    	private Bitmap image;
    	
    	public ViewExpireDateTask(Context context) {
    		mContext = context;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		//맥어드레스를 통한 만기일 값 가져오기
    		if(getMacaddress() != null)
            	mExpiredate = getExpireDate(getMacaddress());
    		image = urlImageload.getUrlImage(Constant.mainUrl + "/image/image_main_logo.png");
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		if(image != null)
    			ivTel.setImageBitmap(image);
    		//ivTel.setImageResource(R.drawable.image_main_logo);
            //만기일 저장
        	saveExpireDate();
        	
        	if(expireCal.after(nowCal)){    // 날짜를 비교한다.
        		saveAuth(true);
        	}else{
        		saveAuth(false);
        	}
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    		//	mImageView.scrollTo(mScrollStep++, 0);
    	}
    	
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    	}	
   }
	
	class ViewNoticeTask extends AsyncTask<String, Integer, Long> {
    	private String imageUrl;
    	private Context mContext;
    	private Bitmap image;
    	
    	public ViewNoticeTask(Context context) {
    		mContext = context;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		//while(true) {
    			//break;
    		//}
    		String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("", ""));
			strJson = postmake.httpConnect(
					Constant.mainUrl + "/module/tv/main_notice.php", nameValuePairs);
			JSONArray jArray = null;
			try{
				jArray = new JSONArray(strJson);
				JSONObject json_data = jArray.getJSONObject(0); 
				//detailTitle = json_data.getString("title");
				imageUrl = json_data.getString("image");
				image = urlImageload.getUrlImage(imageUrl);
			}catch(JSONException e){
			}
			return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		if(imageUrl == null){
    			return;
    			/*AlertDialog.Builder alt_bld = new AlertDialog.Builder(HantvActivity.this);
			    alt_bld.setMessage(R.string.nonet);
			    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		                Intent intent = new Intent(HantvActivity.this, MainMenuActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
		            }
		        });
			    AlertDialog alert = alt_bld.create();
			    alert.show();
    			return;*/
    		}
    		if(imageUrl.equals("-1")){
				Intent intent = new Intent(HantvActivity.this, MainMenuActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}else{
				if(image != null)
					imgViewNotice.setImageBitmap(image);
	            imgViewNotice.setScaleType(ImageView.ScaleType.CENTER_CROP);	
			}
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    		//	mImageView.scrollTo(mScrollStep++, 0);
    	}
    	
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    	}	
   }
}
