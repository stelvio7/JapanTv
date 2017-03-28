package com.japantv.settop;


import java.util.ArrayList;       

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.japantv.settop1.R;
import com.japantv.model.BroadcastList;
import com.japantv.model.Constant;
import com.japantv.setting.SearchActivity;
import com.noh.util.PostHttp;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoViewActivity extends Activity implements OnClickListener{
	private ProgressDialog dlg = null;
	private VideoView vv = null;
	private String url = null;
	private AlertDialog aDlg = null;
	private ViewBannerTask viewBannerTask = null;
	private ImageView bannerView = null;
	private String mainid = null;
	protected String subid = null;		//현재 카테고리
	private Context mContext = null;
	private boolean isPlaying = false;
	private String idx = null;
	private boolean isClicked = false;
	LinearLayout llMenu;
	RelativeLayout slmenu;
	boolean menuVisible = false;
	private boolean first = true;
	private ArrayList<BroadcastList> liveList = null;
	
	private boolean menuShowable = false;
	
	public static final int MENU_ADD = Menu.FIRST;
	public static final int MENU_DELETE = Menu.FIRST + 1;
	
	   
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		for(int i = 0; i < liveList.size();i++){
			if(arg0 == liveList.get(i).getBtns()){
				if(!isClicked){
					if(vv != null){
						if(vv.isPlaying()){
							vv.stopPlayback();
							vv = null;
						}
						vv = null;
					}
					isClicked = true;
					Log.e(null, "position=" + i);
					ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, i);
					viewDetailTask.execute();
				}
			}
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.videopage);
		
		liveList = new ArrayList<BroadcastList>();
		
		mContext = this.getBaseContext();
		isPlaying = false;
		bannerView = (ImageView)findViewById(R.id.imageview);
		
		Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		idx = myBundle.getString("idx");
		mainid = myBundle.getString("mainid");
		subid = myBundle.getString("subid");
		url = myBundle.getString("videourl");
		Log.e(null, "url : " +url );
		Log.e(null, "subid : " +subid );
		
		if(dlg==null){
			dlg =  ProgressDialog.show(VideoViewActivity.this, "", getString(R.string.wait), true);
			dlg.setCancelable(true);
			dlg.show();
		}
		
		if(mainid.equals("live")){
			GetTask getTask = new GetTask(mContext);
			getTask.execute();
		}
		
		
		
		if(!Constant.isTest){
			//ViewBannerTask viewBannerTask = new ViewBannerTask(mContext);
			//viewBannerTask.execute();
		}
		

		try{
			vv = (VideoView)findViewById(R.id.videoview);
	        MediaController mc =  new MediaController(this);
	        mc.setAnchorView(vv);
	        vv.setVideoURI(Uri.parse(url));
	        vv.setMediaController(mc);
	        
	        vv.requestFocus();
	        
		}catch(Exception e){
			//finish();
		}
		
		aDlg = new  AlertDialog.Builder(this)
	    	.setMessage(R.string.novideo)         
		    .setCancelable(true)  
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {    
			     @Override
			     public void onClick(DialogInterface dialog, int which) {
			    	 dialog.dismiss();
			      // TODO Auto-generated method stub
			    	 finish();
			    	 
			     }
	    })
	    .create(); // 마지막 create()에서 다 만든 AlertDialog가 returne된다
		
		dlg.setOnCancelListener(new OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if(vv != null){
					vv.stopPlayback();
					vv = null;
				}
				//finish();
			}
		});
		
		vv.setOnErrorListener(new OnErrorListener(){

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				  aDlg.show();
				//finish();
				return true;
			}
		});
		
        vv.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer arg0) {
            	 isPlaying = true;
                if(dlg!=null && dlg.isShowing()){
        			dlg.dismiss();
        			dlg=null;
        		}
               
            }
        });
        
        vv.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				finish();
			}
        });
        
        if(vv != null)
			vv.start();
        
	}
	
	void setMenu(){
		llMenu = (LinearLayout)findViewById(R.id.llMenu);
		slmenu = (RelativeLayout)findViewById(R.id.slmenu);
		for(int i = 0; i < liveList.size(); i++){
			Button button = new Button(this);
	        button.setBackgroundResource(R.drawable.live_select_button);
	        button.setText(liveList.get(i).getTitle());
	        button.setTextColor(Color.WHITE);
	        button.setTextSize(23);
	        button.setOnClickListener(this);
	        //button.setFocusable(true);
	        //button.requestFocus();
	        liveList.get(i).setBtns(button);
	        //button01.setLayoutParams(params);
	        llMenu.addView(button);
		}
	}
    
    
	@Override
	protected void onDestroy() {
		if(dlg != null){
			dlg.dismiss();
			dlg = null;
		}
		if(aDlg != null){
			aDlg.dismiss();
			aDlg = null;
		}
		if(vv != null){
			if(vv.isPlaying()){
				vv.stopPlayback();
				vv = null;
			}
			vv = null;
		}
		if(viewBannerTask != null){
			viewBannerTask.cancel(true);
			viewBannerTask = null;
		}
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void getImages(){
		int startIdx = 0;
		int endIdx = 29999;
		

		String[] subids = {"138", "139", "140"};;
		for(int j = 0; j < subids.length; j++){
			String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
			nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(endIdx)));
			nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
			
			nameValuePairs.add(new BasicNameValuePair("code2", subids[j]));
			
			strJson = postmake.httpConnect(
					Constant.mainUrl + "/module/tv/live.php", nameValuePairs);

			JSONArray jArray = null;
			try{
				//JSONObject json = new JSONObject(strJson);
				jArray = new JSONArray(strJson);
				//Log.e(null, strJson);
				for(int i = 0; i < jArray.length(); i++){
					BroadcastList tempList = new BroadcastList();
					JSONObject json_data = jArray.getJSONObject(i); 
					tempList.setIdx(json_data.getString("idx"));
					tempList.setVod_code(json_data.getString("vod_code"));
					tempList.setImage(json_data.getString("image"));
					tempList.setSubid(subids[j]);
					tempList.setTitle(json_data.getString("title"));
					tempList.setPu_no(json_data.getString("pu_no"));
					//list.add(tempList);
					liveList.add(tempList);
				}
			}catch(JSONException e){
				Log.e(null, e.toString()); 
			}
		}
	}
	
	private String getMacaddress() {
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("macaddress", "");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		isPlaying = true;
		super.onStop();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			//finish();
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			//finish();
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
			
		} else if(keyCode == KeyEvent.KEYCODE_BACK){
			if(vv != null){
				if(vv != null){
					vv.stopPlayback();
					vv = null;
				}
				vv = null;
			}
			finish();
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			//return true;
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			//return true;
		}else if(keyCode == KeyEvent.KEYCODE_MENU || keyCode == 29) {
			if(mainid.equals("live")){
				if(menuShowable){
					if(menuVisible){
						slmenu.setVisibility(View.GONE);
						menuVisible = false;
					}else{
						if(liveList.size() > 0){
							first = false;
							liveList.get(0).getBtns().setFocusable(true);
							liveList.get(0).getBtns().requestFocus();
						}
						slmenu.setVisibility(View.VISIBLE);
						menuVisible = true;
					}
				}
				
				return true;
			}
		}
  	  return super.onKeyDown(keyCode, event);  	  
	}
	
	class ViewBannerTask extends AsyncTask<String, Integer, Long> {
    	private String imageUrl;
    	private Context mContext;
    	private Bitmap image;
    	
    	public ViewBannerTask(Context context) {
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
			String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			if(mainid.equals("live"))
				//nameValuePairs.add(new BasicNameValuePair("code1", "114"));
				nameValuePairs.add(new BasicNameValuePair("menu1", "114"));
			else
				nameValuePairs.add(new BasicNameValuePair("menu1", mainid));
			if(!mainid.equals("")){
				if(subid.equals("update"))
					nameValuePairs.add(new BasicNameValuePair("menu2", subid));
				else
					//nameValuePairs.add(new BasicNameValuePair("code2", subid));
					nameValuePairs.add(new BasicNameValuePair("menu2", subid));
				if(mainid.equals("live"))
					nameValuePairs.add(new BasicNameValuePair("menu3", idx));
				strJson = postmake.httpConnect(
						Constant.mainUrl + "/module/tv/playerbanner.php", nameValuePairs);
				JSONArray jArray = null;
				try{
					jArray = new JSONArray(strJson);
					JSONObject json_data = jArray.getJSONObject(0); 
					//detailTitle = json_data.getString("title");
					imageUrl = json_data.getString("image");
					Log.e(null, "imageUrl" + imageUrl);
				}catch(JSONException e){
				}
				UrlImageLoader urlImageload = new UrlImageLoader();
				image = urlImageload.getUrlImage(imageUrl);
			}
			publishProgress();
			try{
				Thread.sleep(10000);
			}catch(Exception e){
			}
			if(vv != null)
				vv.start();
			while(!isPlaying){
				try{
					Thread.sleep(1000);
				}catch(Exception e){
				}
			}
    		return 0L;
    	}
    	
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		if(!isPlaying)
    			dlg.show();
    		bannerView.setVisibility(View.GONE);
    		if(image != null){
    			image.recycle();
    			image = null;
    		}
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    		//	mImageView.scrollTo(mScrollStep++, 0);
    		if(image != null){
    			if(dlg != null)
    				dlg.dismiss();
    			bannerView.setImageBitmap(image);
    			bannerView.setVisibility(View.VISIBLE);
        		bannerView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    		}
    	}
    	
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    	}	
   }
	
	class ViewDetailTask extends AsyncTask<String, Integer, Long> {
    	private String detailUrl;
    	private String playUrl;
    	private Context mContext;
    	private int position;
    	//private String detailTitle;
    	private String detailP_code;
    	private String detailVod_type;
    	private String detailVod_code;
    	private String result_code;
    	private String vod_url;
    	
    	public ViewDetailTask(Context context, int position) {
    		mContext = context;
    		this.position = position;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
			String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if(!mainid.equals("broadcast")){
				nameValuePairs.add(new BasicNameValuePair("menu", subid));
				
				nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
				if(liveList.size() > 0)
					nameValuePairs.add(new BasicNameValuePair("idx", liveList.get(position).getIdx()));
				strJson = postmake.httpConnect(
						Constant.mainUrl + "/module/tv/detail.php", nameValuePairs);
				//Log.e(null, "json" + broadcastList.get(position).getIdx());
				JSONArray jArray = null;
				try{
					jArray = new JSONArray(strJson);
					JSONObject json_data = jArray.getJSONObject(0); 
					//detailTitle = json_data.getString("title");
					detailP_code = json_data.getString("p_code");
					detailVod_type = json_data.getString("vod_type");
					detailVod_code = json_data.getString("vod_code");
					Log.e(null, json_data.getString("p_code"));
				}catch(JSONException e){
					
				}
			}
			
			strJson = "";
			nameValuePairs = null;
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
			if(mainid.equals("broadcast")){
				nameValuePairs.add(new BasicNameValuePair("p_code", liveList.get(position).getIdx()));
				nameValuePairs.add(new BasicNameValuePair("vod_type", "L"));
				nameValuePairs.add(new BasicNameValuePair("vod_code", liveList.get(position).getVod_code()));
			}else{
				nameValuePairs.add(new BasicNameValuePair("p_code", detailP_code));
				nameValuePairs.add(new BasicNameValuePair("vod_type", "L"));
				nameValuePairs.add(new BasicNameValuePair("vod_code", detailVod_code));
			}
			if(!Util.getChildset(mContext))
				nameValuePairs.add(new BasicNameValuePair("adult_pwd", getChildNum()));
			
			if(liveList.size() > 0)
				nameValuePairs.add(new BasicNameValuePair("idx", liveList.get(position).getIdx()));
			strJson = postmake.httpConnect(
					Constant.mainUrl + "/module/tv/play.php", nameValuePairs);
			try{
				JSONObject json_data = new JSONObject(strJson); 
				//detailTitle = json_data.getString("title");
				result_code = json_data.getString("result");
				vod_url = json_data.getString("vod_url");
				//Log.e(null, vod_url);
			}catch(JSONException e){
				Log.e(null, e.toString()); 
			}
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		Log.e(null, "url" + vod_url + " :idx:"+ liveList.get(position).getIdx());
    		if(result_code != null)
    			checkPlay(result_code, vod_url, liveList.get(position).getIdx(), liveList.get(position).getSubid());
    		isClicked = false;
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
	
	 private String getChildNum(){
			SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), Context.MODE_PRIVATE);
			return sp.getString("childnum", "0000");
		}
	 
	private void checkPlay(String result, String vod_url, String idx, String gsubid){
		if(Constant.isTest){
			 goLivePlay(vod_url, idx, gsubid);
			 return;
		}
		 if(getAuth().equals("ok")){
			 if(result.equals("OK")){
				 goLivePlay(vod_url, idx, gsubid);
			 }
			 else if(result.equals("CANCEL")){
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			    alt_bld.setMessage(R.string.videocancel);
			    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		            }
		        });
			    AlertDialog alert = alt_bld.create();
			    alert.show();
			 }
			 else if(result.equals("CHILD_SAFE")){
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				    alt_bld.setMessage(R.string.safechild);
				    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        });
				    AlertDialog alert = alt_bld.create();
				    alert.show();
			 }
			 else if(result.equals("AGE_UNDER")){
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				    alt_bld.setMessage(R.string.ageunder);
				    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        });
				    AlertDialog alert = alt_bld.create();
				    alert.show();
			 }
			 else if(result.equals("MEMBER_M")){
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				    alt_bld.setMessage(R.string.memberm);
				    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        });
				    AlertDialog alert = alt_bld.create();
				    alert.show();
			 }
			 else if(result.equals("POINT_UNDERSUPPLY")){
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				    alt_bld.setMessage(R.string.nopoint);
				    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        });
				    AlertDialog alert = alt_bld.create();
				    alert.show();
			 }
		 }else{
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(VideoViewActivity.this);
			    alt_bld.setMessage(R.string.memberm);
			    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		            }
		        });
			    AlertDialog alert = alt_bld.create();
			    alert.show();
			}
	 }
	
	private void goLivePlay(String strUrl, String idx, String gsubid){
		Intent intent = new Intent(VideoViewActivity.this, VideoViewActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle myData = new Bundle();	
		//Log.e(null, strUrl);
		myData.putString("idx", idx);
        myData.putString("videourl", strUrl);
        myData.putString("mainid", mainid);
        Log.e(null, "gsubid" + gsubid);
        myData.putString("subid", gsubid);
        intent.putExtras(myData);
		startActivity(intent);
		finish();
	}
	
	private String getAuth(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("auth", "");
	}
	
	
	class GetTask extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<BroadcastList> list = null;

    	
    	public GetTask(Context context) {
    		mContext = context;
    	}
    
    	 @Override
    	 protected final void onPreExecute() {
    	  }
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		getImages();
    		return 0L;
    	}
    	
   
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		setMenu();
    		menuShowable = true;
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
