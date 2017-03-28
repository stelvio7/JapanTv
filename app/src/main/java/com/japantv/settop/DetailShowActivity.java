package com.japantv.settop;

import java.util.ArrayList;      

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.japantv.model.BroadcastList;
import com.japantv.model.Constant;
import com.japantv.model.DramaContent;
import com.japantv.settop.MovieListActivity.ViewDetailTask;
import com.japantv.settop.MovieListActivity.WishTask;
import com.japantv.settop1.R;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.Util;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;

public class DetailShowActivity extends Activity{
    /** Called when the activity is first created. */
	private int type = 0;		//0 :드라마, 1: 오락, 2: 케이블
	private ArrayList<DramaContent> nowShowList = null;

	private ArrayList<String> numberList = null;
	private ArrayList<Button> numberBtn = null;
	private Context mContext = null;
	
	private int nowPosition;
	
	private String mainid = null;
	protected String subid = null;		//현재 카테고리
	private String title = null;		//현재 컨텐츠 제목
	private String idx = null;			//현재 컨텐츠 아이디
	private String image= null;
	private String id = null;			//맥어드레스 
	
	private String date = null;
	private String age = null;
	private String director = null;
	private String actor = null;
	private String story = null;
	
	private TextView txtDate = null;
	private TextView txtAge = null;
	private TextView txtDirector = null;
	private TextView txtCast = null;
	private TextView txtStory = null;
	private ImageView imgPop = null;
	
	private int maxPageIdx;		//페이지
	private int remainder;	
	private int pageIdx;		//현재 페이지
	private TextView menuTitle;
	private ImageView ivTel = null;
	private boolean isClicked = false;
	private int idxBtn = -1;
	private ProgressDialog mProgress;
	private Detail2DialogActivity mCustomPopup2;
	
	private ImageView bottom = null;
	private String back = "";
	
	private int[] numberBtnName = {
			R.id.detailshowbtn0, R.id.detailshowbtn1, R.id.detailshowbtn2, R.id.detailshowbtn3, R.id.detailshowbtn4,
			R.id.detailshowbtn5, R.id.detailshowbtn6, R.id.detailshowbtn7, R.id.detailshowbtn8, R.id.detailshowbtn9,
			R.id.detailshowbtn10, R.id.detailshowbtn11, R.id.detailshowbtn12, R.id.detailshowbtn13, R.id.detailshowbtn14
		};

	public DetailShowActivity(){
		numberBtn = new ArrayList<Button>();
		nowShowList = new ArrayList<DramaContent>();
		
		pageIdx = 0;
		remainder = 0;
		maxPageIdx = 0;
	}
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	private final ImageDownloader imageDownloader = new ImageDownloader();
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailshow);
        
        mContext = getBaseContext();
        Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		mainid = myBundle.getString("mainid");
		subid = myBundle.getString("menu");
		idx = myBundle.getString("idx");
		image = myBundle.getString("image");
		title = myBundle.getString("title");
		if(myBundle.getString("back") != null)
			back = myBundle.getString("back");
		
		ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		//ivTel.setImageResource(R.drawable.image_main_logo);
		imageDownloader.download(Constant.mainUrl + "/image/image_main_logo.png", (ImageView)ivTel);
        
		imgLock = (ImageView)findViewById(R.id.imgLock);
		if(!Util.getChildset(mContext))
			imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        
		menuTitle = (TextView) findViewById(R.id.menuTitle);
		menuTitle.setText(title);
        Button expireText = (Button) findViewById(R.id.imgExpireDate);
        Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }

        txtDate = (TextView) findViewById(R.id.txtDate);
		txtAge = (TextView) findViewById(R.id.txtAge);
		txtDirector = (TextView) findViewById(R.id.txtDirector);
		txtCast = (TextView) findViewById(R.id.txtCast);
		txtStory = (TextView) findViewById(R.id.txtStory);
		imgPop = (ImageView) findViewById(R.id.imgPop);
		imageDownloader.download(image, (ImageView)imgPop);
		
		getDetailDatas();
        setNumberBtn();
        if(nowShowList != null){
        	maxPageIdx = nowShowList.size()/15;
        	remainder = nowShowList.size()%15;
        }
        if(remainder > 0)
        	maxPageIdx++;
        
        refreshPage();
        
        for(int i = 0; i < numberBtn.size(); i++){
	        numberBtn.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int position = -1;
					for(int i = 0; i < numberBtn.size(); i++){
						if(v.getId() == numberBtn.get(i).getId()){
							position = i;
							idxBtn = position;
						}
					}
					if(position != -1){
						if(back.equals("back")){
							if(!isClicked){
								isClicked = true;
								ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, idxBtn);
								viewDetailTask.execute();
								if(mCustomPopup2 != null)
									mCustomPopup2.dismiss();
							}
						}else{
							if(mCustomPopup2 != null)
								mCustomPopup2.dismiss();
							 mCustomPopup2 = new Detail2DialogActivity(DetailShowActivity.this, xPop2PlayClickListener, xPop2PlayKeyListener, xPop2RestoreClickListener, xPop2RestoreKeyListener, "movie");
							 mCustomPopup2.requestWindowFeature(Window.FEATURE_NO_TITLE);
							 mCustomPopup2.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); 
							 mCustomPopup2.show();
						}
					}
				}
			});
        }
        
       /* bottom = (ImageView)findViewById(R.id.bottom);
        bottom.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(pageIdx < maxPageIdx-1)
            	  	pageIdx++;
				else 
					pageIdx = 0;
            	  refreshPage();
				return false;
			}
		});*/
        
        numberBtn.get(0).setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
                	  if(pageIdx > 0)
                		  pageIdx--;
                	  refreshPage();
                	  return true;   
                  }
                }
                return false;   
              }                  
          });  
        
        numberBtn.get(14).setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
                	  if(pageIdx < maxPageIdx-1)
                	  	pageIdx++;
                	  refreshPage();
                	  return true;   
                  }
                }
                return false;   
              }                  
          });  
    }
    
    
	private View.OnKeyListener xPop2PlayKeyListener = new View.OnKeyListener() {
		 @Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			 if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
				if(!isClicked){
					isClicked = true;
					ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, idxBtn);
					viewDetailTask.execute();
					if(mCustomPopup2 != null)
						mCustomPopup2.dismiss();
				}
			}
			return false;
		}
  };
  
  private View.OnClickListener xPop2PlayClickListener = new View.OnClickListener() {
		 @Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!isClicked){
				isClicked = true;
				ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, idxBtn);
				viewDetailTask.execute();
				if(mCustomPopup2 != null)
					mCustomPopup2.dismiss();
			}
		}
 };
 
 private View.OnKeyListener xPop2RestoreKeyListener = new View.OnKeyListener() {
		 @Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			 if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
				//if(!isClicked){
				//	isClicked = true;
					if(mCustomPopup2 != null)
						mCustomPopup2.dismiss();
					WishTask wishTask = new WishTask(mContext);
					wishTask.execute();
				//}
			}
			return false;
		}
	};
	
	private View.OnClickListener xPop2RestoreClickListener = new View.OnClickListener() {
			 @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//if(!isClicked){
					//isClicked = true;
					if(mCustomPopup2 != null)
						mCustomPopup2.dismiss();
					WishTask wishTask = new WishTask(mContext);
					wishTask.execute();
				//}
			}
	};
    
    /*private String getMenuTitle(String subid){
    	String title = "";
		if(subid.equals("update")) {
			title = getString(R.string.menuupdate);
		}else if(subid.equals("drama")){
			title = getString(R.string.menudrama);
		}else if(subid.equals("show")){
			title = getString(R.string.menushow);
		}else if(subid.equals("edu")){
			title = getString(R.string.menuedu);
		}else if(subid.equals("cable")){
			title = getString(R.string.menucable);
		}else if(subid.equals("box")){
			title = getString(R.string.menubox);
		}
		return title;
    }*/
    
    private void getDetailDatas(){
		int startIdx = 0;
		
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//nameValuePairs.add(new BasicNameValuePair("menu", subid));
		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
		nameValuePairs.add(new BasicNameValuePair("idx", idx));
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/detail.php", nameValuePairs);
		Log.e(null, "json:" + strJson);
		try{
			JSONArray jsonArray = new JSONArray(strJson);
			JSONObject json = jsonArray.getJSONObject(0);
			//title = json.getString("genre"); 
			//title = json.getString("p_star"); 
			age = json.getString("p_age"); 
			date = json.getString("p_svc_date"); 
			director = json.getString("p_director"); 
			actor = json.getString("p_acter"); 
			story = json.getString("p_stroy"); 
			
			txtDate.setText(date);
			txtAge.setText(age);
			txtDirector.setText(director);
			if(actor.length() > 20){
				actor = actor.substring(0, 20);
				actor += "..";
		   	}
	   	   	txtCast.setText(actor);
	   	   	if(story.length() > 210){
	   	   		story = story.substring(0, 210);
	   	   		story += "..";
	   	   	}
	   	   	txtStory.setText(story);
			JSONArray contentsArray = json.getJSONArray("content");
			for(int i = 0; i < contentsArray.length(); i++){
				JSONObject json_data = contentsArray.getJSONObject(i); 
				
				DramaContent dramaContent = new DramaContent();
				dramaContent.setTitle(json_data.getString("title"));
				dramaContent.setP_code(json_data.getString("p_code"));
				dramaContent.setVod_type(json_data.getString("vod_type"));
				dramaContent.setVod_code(json_data.getString("vod_code"));
				dramaContent.setWatch(json_data.getString("watch"));

				nowShowList.add(dramaContent);
			}
		}catch(JSONException e){
			Log.e(null, e.toString()); 
		}
    }
    
  //다음 페이지로 이동한 후 ui를 갱신시켜 줌
    private void refreshPage(){
    	int numberidx = nowShowList.size() - pageIdx*15;
        for(int i = 0; i < 15; i++){
        	if(numberBtn != null){
        		numberidx--;
        		if(0 <= numberidx){
        			Log.e(null, String.valueOf(numberidx)); 
        			numberBtn.get(i).setText(nowShowList.get(numberidx).getTitle());
        		}
        		numberBtn.get(i).setVisibility(View.VISIBLE);
        		if(numberidx < 0)
        			numberBtn.get(i).setVisibility(View.INVISIBLE);
        	}
        }
    }
    
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}

    
    private void setNumberBtn(){
    	for(int i = 0; i < 15; i++){
        	Button tempBtn = (Button) findViewById(numberBtnName[i]);
        	numberBtn.add(tempBtn);
        }
    }
    
	@Override
	protected void onDestroy() {
		if(mCustomPopup2 != null)
			mCustomPopup2.dismiss();
		// TODO Auto-generated method stub
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			//finish();
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			//finish();
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			//finish();
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			//finish();
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Intent intent = new Intent(DetailShowActivity.this, MainMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		//else
  			//finish();
		return super.onKeyDown(keyCode, event);
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
    	private int nowPosition;
    	
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

    		nowPosition = nowShowList.size() - pageIdx*25 - position-1;
    		
    		PostHttp postmake = new PostHttp();
			String strJson = "";
			ArrayList<NameValuePair> nameValuePairs = null;
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
			nameValuePairs.add(new BasicNameValuePair("p_code", nowShowList.get(nowPosition).getP_code()));
			nameValuePairs.add(new BasicNameValuePair("vod_type", nowShowList.get(nowPosition).getVod_type()));
			nameValuePairs.add(new BasicNameValuePair("vod_code", nowShowList.get(nowPosition).getVod_code()));
			if(!Util.getChildset(mContext))
				nameValuePairs.add(new BasicNameValuePair("adult_pwd", getChildNum()));
			//if(nowShowList.size() > 0)
				//nameValuePairs.add(new BasicNameValuePair("idx", nowShowList.get(position).getIdx()));
			strJson = postmake.httpConnect(
					Constant.mainUrl + "/module/tv/play.php", nameValuePairs);
			
			try{
				JSONObject json_data = new JSONObject(strJson); 
				//detailTitle = json_data.getString("title");
				result_code = json_data.getString("result");
				vod_url = json_data.getString("vod_url");
				Log.e(null, vod_url);
			}catch(JSONException e){
				Log.e(null, e.toString()); 
				isClicked = false;
			}
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		Log.i(null, "url" + vod_url);
    		checkPlay(result_code, vod_url, nowShowList.get(nowPosition).getP_code());
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
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("childnum", "0000");
	}
	
	 private void checkPlay(String result, String vod_url, String idx){
		 if(Constant.isTest){
			 goLivePlay(vod_url, idx);
			 return;
		}
		 if(getAuth().equals("ok")){
			 if(result.equals("OK")){
				 goLivePlay(vod_url, idx);
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
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(DetailShowActivity.this);
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
	 
	 private void goLivePlay(String strUrl, String idx){
			Intent intent = new Intent(DetailShowActivity.this, VideoViewActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle myData = new Bundle();	
			myData.putString("idx", idx);
	        myData.putString("videourl", strUrl);
	        myData.putString("mainid", mainid);
	        myData.putString("subid", subid);
	        intent.putExtras(myData);
			startActivity(intent);
		}
	 
	 private String getMacaddress() {
			SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
			return sp.getString("macaddress", "");
		}
	 
		private String getAuth(){
			SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
			return sp.getString("auth", "");
		}
		
		class WishTask extends AsyncTask<String, Integer, Long> {
	    	private  ArrayList<BroadcastList> list = null;
	    	private int position;
	    	
	    	public WishTask(Context context) {
	    		mContext = context;
	    	}
	    
	    	 @Override
	    	 protected final void onPreExecute() {
	    		 showProgress();
	    	  }
	    	@Override
	    	protected Long doInBackground(String... params) {
	    		// TODO Auto-generated method stub
	    		String strJson = "";
				PostHttp postmake = new PostHttp();
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nowPosition = nowShowList.size() - pageIdx*25 - position-1;
				
				nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
				nameValuePairs.add(new BasicNameValuePair("ptype", "write"));
				nameValuePairs.add(new BasicNameValuePair("p_code", nowShowList.get(nowPosition).getP_code()));
				nameValuePairs.add(new BasicNameValuePair("pu_idx", nowShowList.get(nowPosition).getVod_code()));

				strJson = postmake.httpConnect(
						Constant.mainUrl + "/module/tv/wishlist_proc.php", nameValuePairs);
				try{
					JSONObject json_data = new JSONObject(strJson); 
					//detailTitle = json_data.getString("title");
					//result_code = json_data.getString("result");
					//vod_url = json_data.getString("vod_url");
				}catch(JSONException e){
					isClicked = false;
					Log.e(null, e.toString()); 
				}
	    		return 0L;

	    	}
	    	
	    	 
	    	protected void showProgress() {
	    		mProgress = new ProgressDialog(DetailShowActivity.this);
	    		mProgress.setCancelable(true);
	            mProgress.setOnCancelListener(new OnCancelListener() {
	                public void onCancel(DialogInterface dialog) {
	                	WishTask.this.cancel(true);
	                }
	            });

	            String message = mContext.getString(R.string.str_wait);
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

	    	
	    	 protected void showError(Context context, Throwable t) {
	    	        dismissProgress();

	    	        //TODO exception ���� ��� ������ �����޽��� ����
	    	        String errorMessage = context.getString(R.string.str_network_error);

	    	        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
	    	    }
	    
	    	@Override
	    	protected void onPostExecute(Long result) {
	    		// TODO Auto-generated method stub
	    		isClicked = false;
	    		dismissProgress();
	    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(DetailShowActivity.this);
				    alt_bld.setMessage("ライブラリに保存されました。");
				    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        });
				    AlertDialog alert = alt_bld.create();
				    alert.show();

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
	    		showCancelMessage();
	    	}	
	    }
}
