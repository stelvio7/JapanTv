package com.japantv.setting;

import java.util.ArrayList;       

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.japantv.settop1.R;
import com.japantv.model.Constant;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.Util;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener; 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ChildActivity extends Activity {
    /** Called when the activity is first created. */
	private String strPasswd;
	private String strPasswdConfirm;
	private EditText editPasswd;
	private EditText editPasswdModify;
	
	private boolean safeMode;
	
	private ImageButton btnChild;
	private ImageButton btnSend;
	
	private SendRechargeTask sendRechargeTask= null;
	private ProgressDialog mProgress;
	private Button expireText;
	private ImageView ivTel = null;
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	private Context mContext = null;
	
	public ChildActivity(){
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child);
        
        mContext = this.getBaseContext();
        
        ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		//ivTel.setImageResource(R.drawable.image_main_logo);
		imageDownloader.download(Constant.mainUrl + "/image/image_main_logo.png", (ImageView)ivTel);
        
		imgLock = (ImageView)findViewById(R.id.imgLock);
		if(!Util.getChildset(mContext))
			imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
		else
        	imgLock.setBackgroundResource(R.drawable.image_main_lock_on);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        
        expireText = (Button) findViewById(R.id.imgExpireDate);
        Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        
        
        
        editPasswd =  (EditText) findViewById(R.id.editPasswd);
        editPasswdModify =  (EditText) findViewById(R.id.editPasswdModify);
        editPasswd.requestFocus();
        
        //editPasswd.setInputType(InputType.TYPE_CLASS_NUMBER
       // 		 |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editPasswd.setOnKeyListener(new OnKeyListener() {           
            public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {      
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  //clickOkButton();
                	  return false;           
                  } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DEL
                		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
                	  return false;   
                  } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
                	  finish();
                	  return false;   
                  }
                }
                return false;   
              }           
          });   
       // editPasswdModify.setInputType(InputType.TYPE_CLASS_NUMBER
        //		 |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editPasswdModify.setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	 // clickOkButton();
                	  return false;           
                  } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
                		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
                	  return false;   
                  } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
                	  finish();
                	  return false;   
                  }
                }
                return false;   
              }                  
          });  
       
        
        
        btnChild = (ImageButton) findViewById(R.id.btnChild);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(btnChild.getWindowToken(), 0);
		
		safeMode =  getChildset();
        if(safeMode){
        	btnChild.setBackgroundResource(R.drawable.button_safe_on);
        }else{
        	btnChild.setBackgroundResource(R.drawable.button_safe_off);
        }
        
        btnChild.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(safeMode){
					safeMode = false;
					btnChild.setBackgroundResource(R.drawable.button_safe_off);
				}else{
					safeMode = true;
					btnChild.setBackgroundResource(R.drawable.button_safe_on);
				}
			}           
        	             
          });  
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm2.hideSoftInputFromWindow(btnSend.getWindowToken(), 0);
        btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton();
			}           
          });  
    }
    
    
	@Override
	protected void onDestroy() {
		if(sendRechargeTask != null){
			sendRechargeTask = null;
		}
		if(mProgress != null){
			mProgress = null;
		}
		
		// TODO Auto-generated method stub
		if(editPasswd != null)
			editPasswd = null;
		if(editPasswdModify != null)
			editPasswdModify = null;
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
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
			clickOkButton();
		}
  	  return super.onKeyDown(keyCode, event); 
	}
	
	private void clickOkButton(){
		//텍스트 필드를 모두 안채웠을때 
		if(editPasswd.getText().toString().length() < 4 || (editPasswdModify.getText().toString().length() < 4 && editPasswdModify.getText().toString().length() > 0)){
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage(R.string.check_child_4);
		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
		    AlertDialog alert = alt_bld.create();
		    alert.show();
		 }else{
			 strPasswd = editPasswd.getText().toString();
			 strPasswdConfirm = editPasswdModify.getText().toString();
			 
			 if(getChildNum().equals(strPasswd)){
				 sendRechargeTask = new SendRechargeTask(getBaseContext());
			     sendRechargeTask.execute();
			 }else{
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(ChildActivity.this);
    		    alt_bld.setMessage(R.string.check_child_no);
    		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	                dialog.dismiss();
    	            }
    	        });
    		    AlertDialog alert = alt_bld.create();
    		    alert.show();
			 }
			 //getMacaddress();
		 }
	}
	
	//충전하기
	 class SendRechargeTask extends AsyncTask<String, Integer, Long> {
    	private Context mContext;
    	private String result = "";
    	
    	public SendRechargeTask(Context context) {
    		mContext = context;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		mProgress = ProgressDialog.show(ChildActivity.this, "", getResources().getString(R.string.wait), true, true);
    		//mProgress.setCancelable(false);
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		String strJson = "";
    		PostHttp postmake = new PostHttp();
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
    		if(strPasswdConfirm.equals("")){
    			strPasswdConfirm = editPasswd.getText().toString().trim();
    		}
			nameValuePairs.add(new BasicNameValuePair("adult_pwd", strPasswdConfirm));
			String srtMode = "Y";
			if(safeMode)
				srtMode = "Y";
			else 
				srtMode = "N";
			Log.e(null, "adult:"  + srtMode);
			nameValuePairs.add(new BasicNameValuePair("adult_mode", srtMode));
    		
    		strJson = postmake.httpConnect(
    				Constant.mainUrl +  "/module/tv/member_proc.php", nameValuePairs);
    		try{
    			JSONObject json = new JSONObject(strJson);
    			//jArray = new JSONArray(strJson);
    			result = json.getString("result");
    			
    		}catch(JSONException e){
    			Log.e(null, e.toString()); 
    		}
    		//publishProgress(1);
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long lresult) {
    		// TODO Auto-generated method stub
    		mProgress.dismiss();
    		if(result.equals("Y")){
    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(ChildActivity.this);
    		    alt_bld.setMessage(R.string.check_child_complete);
    		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	                dialog.dismiss();
    	            }
    	        });
    		    AlertDialog alert = alt_bld.create();
    		    alert.show();

    		    saveChildSet(safeMode);
    		    if(!Util.getChildset(mContext))
    		    	imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
    		    else
    		    	imgLock.setBackgroundResource(R.drawable.image_main_lock_on);
    		    saveChildNum(strPasswdConfirm);
    		    editPasswd.setText("");
    			editPasswdModify.setText("");
    		}else if(result.equals("N")){
    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(ChildActivity.this);
    		    alt_bld.setMessage(R.string.check_child_modify);
    		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	                dialog.dismiss();
    	            }
    	        });
    		    AlertDialog alert = alt_bld.create();
    		    alert.show();
    		}
    	}
    
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    	}
    
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
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
		return sp.getString("childnum", "0000");
	}
			
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}
		
	//맥어드레스 가져오기
	private String getMacaddress() {
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("macaddress", "");
	}
	
}

