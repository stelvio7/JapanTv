package com.japantv.setting;

import java.util.ArrayList;    

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.japantv.settop1.R;
import com.japantv.settop1.R.drawable;
import com.japantv.settop1.R.id;
import com.japantv.settop1.R.layout;
import com.japantv.settop1.R.string;
import com.japantv.model.Constant;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.Util;

import android.view.View;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RechargeActivity extends Activity {
    /** Called when the activity is first created. */
	private String strRechargeNumber;
	private EditText regargeNum1;
	private EditText regargeNum2;
	private EditText regargeNum3;
	private EditText regargeNum4;
	private SendRechargeTask sendRechargeTask= null;
	private ProgressDialog mProgress;
	private Button expireText;
	private ImageView ivTel = null;
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	private Context mContext = null;
	private Button expireText2;
	public RechargeActivity(){
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge);
        
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
        
        regargeNum1 =  (EditText) findViewById(R.id.rechargeNum1);
        regargeNum2 =  (EditText) findViewById(R.id.rechargeNum2);
        regargeNum3 =  (EditText) findViewById(R.id.rechargeNum3);
        regargeNum4 =  (EditText) findViewById(R.id.rechargeNum4);
        expireText = (Button) findViewById(R.id.imgExpireDate);
        regargeNum1.requestFocus();
        expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        regargeNum1.setInputType(InputType.TYPE_CLASS_NUMBER
        		 |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        regargeNum1.setOnKeyListener(new OnKeyListener() {           
            public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {      
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  clickOkButton();
                	  return true;           
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
        regargeNum2.setInputType(InputType.TYPE_CLASS_NUMBER
        		 |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        regargeNum2.setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  clickOkButton();
                	  return true;           
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
        regargeNum3.setInputType(InputType.TYPE_CLASS_NUMBER
        		 |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        regargeNum3.setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  clickOkButton();
                	  return true;           
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
        regargeNum4.setInputType(InputType.TYPE_CLASS_NUMBER
        		 |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        regargeNum4.setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  clickOkButton();
                	  return true;           
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
		if(regargeNum1 != null)
			regargeNum1 = null;
		if(regargeNum2 != null)
			regargeNum2 = null;
		if(regargeNum3 != null)
			regargeNum3 = null;
		if(regargeNum4 != null)
			regargeNum4 = null;
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
		if(regargeNum1.getText().toString().length() < 4 || regargeNum2.getText().toString().length() < 4 
				 ||regargeNum3.getText().toString().length() < 4 || regargeNum4.getText().toString().length() < 4 ){
			/*AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage(R.string.check_recharge);
		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
		    AlertDialog alert = alt_bld.create();
		    alert.show();*/
		 }else{
			 strRechargeNumber = regargeNum1.getText().toString() + "-" + regargeNum2.getText().toString() + "-" + 
					 regargeNum3.getText().toString() + "-" + regargeNum4.getText().toString();
			 
			 sendRechargeTask = new SendRechargeTask(getBaseContext());
		     sendRechargeTask.execute();
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
    		mProgress = ProgressDialog.show(RechargeActivity.this, "", getResources().getString(R.string.wait), true, true);
    		//mProgress.setCancelable(false);
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		String strJson = "";
    		PostHttp postmake = new PostHttp();
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
    		nameValuePairs.add(new BasicNameValuePair("number", strRechargeNumber));
    		strJson = postmake.httpConnect(
    				Constant.mainUrl +  "/module/tv/recharge.php", nameValuePairs);
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
    		if(result.equals("0")){
    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(RechargeActivity.this);
    		    alt_bld.setMessage(R.string.check_recharge_complete);
    		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	                dialog.dismiss();
    	            }
    	        });
    		    AlertDialog alert = alt_bld.create();
    		    alert.show();
    		    
    		    expireText = (Button) findViewById(R.id.imgExpireDate);
    	        expireText2 = (Button) findViewById(R.id.imgExpireDate2);
    	        if(!getExpireDate().equals("")){
    	        	expireText.setText(getNetExpireDate(getMacaddress()).substring(0, 2));
    	        	expireText2.setText(getNetExpireDate(getMacaddress()).substring(2, 4));
    	        }
    	        
    	      //만기일 저장
    	        if(getNetExpireDate(getMacaddress()) != null){
    		        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
    				SharedPreferences.Editor editor = sp.edit();
    				editor.putString("expiredate", getNetExpireDate(getMacaddress()));
    				editor.commit();
    	        }
    	        saveAuth(true);
    		}else if(result.equals("1")){
    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(RechargeActivity.this);
    		    alt_bld.setMessage(R.string.check_recharge_diff);
    		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	                dialog.dismiss();
    	            }
    	        });
    		    AlertDialog alert = alt_bld.create();
    		    alert.show();
    		}else if(result.equals("2")){
    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(RechargeActivity.this);
    		    alt_bld.setMessage(R.string.check_recharge_aleady);
    		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	                dialog.dismiss();
    	            }
    	        });
    		    AlertDialog alert = alt_bld.create();
    		    alert.show();
    		}else if(result.equals("3")){
    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(RechargeActivity.this);
    		    alt_bld.setMessage(R.string.check_recharge_error);
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
	
	 private String getNetExpireDate(String macAdress){
		String expireDate = "";
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//macAdress = "123456789";
		Log.e(null, "mac:" + macAdress);
		nameValuePairs.add(new BasicNameValuePair("id", macAdress));
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/expiredate.php", nameValuePairs);
		try{
			JSONObject json_data = new JSONObject(strJson);
			expireDate = json_data.getString("date");
			
		}catch(JSONException e){
			Log.e(null, e.toString()); 
		}
		String[] st = new String[3];
		if(expireDate != null)
			st = expireDate.split("-");
		if(st.length > 1)
			expireDate = st[1] + st[2];
		return expireDate;
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
	
	private void saveAuth(boolean isAuth){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		if(isAuth)
			editor.putString("auth", "ok");
		else
			editor.putString("auth", "");
		editor.commit();
	}
}

