package com.japantv.settop.shopping;

import java.util.ArrayList;  

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.japantv.model.Constant;
import com.japantv.settop.BroadcastListActivity;
import com.japantv.settop.CoverFlow;
import com.japantv.settop1.R;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;
 
public class DetailShoppingActivity extends Activity{
    /** Called when the activity is first created. */
	
	private String[] subImgs = new String[21];
	private String idx;
	private String type;
	
	private ImageAdapter imageAdapter = null;
	private Context mContext;
	private ImageView ivTel = null;
	private int nowCoverFlowIdx = 0;
	private ShoppingDetailCoverFlow coverFlow;

	private ImageView btnLeft = null;
	private ImageView btnRight = null;
	
	private ViewNextTask viewNextTask = null;
	
	public DetailShoppingActivity(){
		nowCoverFlowIdx = 0;
		bitmap = new Bitmap[21];
	}
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	private Bitmap[] bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingdetailcoverflow);
        
	    Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		type = myBundle.getString("type");
		idx = myBundle.getString("idx");
		subImgs[0] = myBundle.getString("sub1");
		subImgs[1] = myBundle.getString("sub2");
		subImgs[2] = myBundle.getString("sub3");
		subImgs[3] = myBundle.getString("sub4");
		subImgs[4] = myBundle.getString("sub5");
		subImgs[5] = myBundle.getString("sub6");
		subImgs[6] = myBundle.getString("sub7");
		subImgs[7] = myBundle.getString("sub8");
		subImgs[8] = myBundle.getString("sub9");
		subImgs[9] = myBundle.getString("sub10");
		subImgs[10] = myBundle.getString("sub11");
		subImgs[11] = myBundle.getString("sub12");
		subImgs[12] = myBundle.getString("sub13");
		subImgs[13] = myBundle.getString("sub14");
		subImgs[14] = myBundle.getString("sub15");
		subImgs[15] = myBundle.getString("sub16");
		subImgs[16] = myBundle.getString("sub17");
		subImgs[17] = myBundle.getString("sub18");
		subImgs[18] = myBundle.getString("sub19");
		subImgs[19] = myBundle.getString("sub20");
		//subImg5 = "http://114.207.113.188/4fan/cms/vote/3_banner_a.png";
		
		
		
		btnLeft = (ImageView) findViewById(R.id.arrowleft);
		btnRight = (ImageView) findViewById(R.id.arrowright);
		btnRight.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				nowCoverFlowIdx = coverFlow.getSelectedItemPosition() + 1;
				if(nowCoverFlowIdx >= 5)
					nowCoverFlowIdx = 5 - 1;
				if(coverFlow != null)
					coverFlow.setSelection(nowCoverFlowIdx);
				return false;
			}
		});
		
		btnLeft.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				nowCoverFlowIdx = coverFlow.getSelectedItemPosition() - 1;
				if(nowCoverFlowIdx < 0)
					nowCoverFlowIdx = 0;
				if(coverFlow != null)
					coverFlow.setSelection(nowCoverFlowIdx);
				return false;
			}
		});
		
        mContext = this.getBaseContext();
        ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		ivTel.setImageResource(R.drawable.image_main_logo);
		//imageDownloader.download(Constant.mainUrl + "/image/image_main_logo.png", (ImageView)ivTel);
        
		imgLock = (ImageView)findViewById(R.id.imgLock);
		if(!Util.getChildset(mContext))
			imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        
        Button expireText = (Button) findViewById(R.id.imgExpireDate);
        Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        
        viewNextTask = new ViewNextTask(mContext);
        viewNextTask.execute();
        
        setCoverFlow();
    }
    
  
    
    
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}
    
    protected String getMacaddress(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("macaddress", "");
	}
    
    private void setCoverFlow(){
		coverFlow = (ShoppingDetailCoverFlow) findViewById(R.id.coverflow);
	    imageAdapter = new ImageAdapter(this);
		
	    coverFlow.setAdapter(imageAdapter);
		coverFlow.setSpacing(10);
		coverFlow.setAnimationDuration(1000);
	}
    
	@Override
	protected void onDestroy() {
		recycleAllBitmap();
		if(viewNextTask != null){
			viewNextTask.cancel(true);
			viewNextTask = null;
		}
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
	
	
	public class ImageAdapter extends BaseAdapter {
		int itemBackground;
		private Context mContext;
		
		public ImageAdapter(Activity c) {
			mContext = c;
		}
		
		public int getCount() {
			if(subImgs[0].equals(""))
				return 0;
			if(subImgs[1].equals(""))
				return 1;
			if(subImgs[2].equals(""))
				return 2;
			if(subImgs[3].equals(""))
				return 3;
			if(subImgs[4].equals(""))
				return 4;
			if(subImgs[5].equals(""))
				return 5;
			if(subImgs[6].equals(""))
				return 6;
			if(subImgs[7].equals(""))
				return 7;
			if(subImgs[8].equals(""))
				return 8;
			if(subImgs[9].equals(""))
				return 9;
			if(subImgs[10].equals(""))
				return 10;
			if(subImgs[11].equals(""))
				return 11;
			if(subImgs[12].equals(""))
				return 12;
			if(subImgs[13].equals(""))
				return 13;
			if(subImgs[14].equals(""))
				return 14;
			if(subImgs[15].equals(""))
				return 15;
			if(subImgs[16].equals(""))
				return 16;
			if(subImgs[17].equals(""))
				return 17;
			if(subImgs[18].equals(""))
				return 18;
			if(subImgs[19].equals(""))
				return 19;
			return 21;
		}

		public Object getItem(int position) {
			return bitmap[position];
		}

		public long getItemId(int position) {
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			i.setLayoutParams(new CoverFlow.LayoutParams(Util.getWidth(mContext)-80, Util.getHeight(mContext)-20)); 
			i.setPadding(0, 0, 0, 0);
			//i.setBackgroundResource(R.drawable.listborder);
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			ImageDownloader imageDownloader = new ImageDownloader();
			if(bitmap[position] != null){
				//imageDownloader.download(subImgs[position], (ImageView)i);
				i.setImageBitmap(bitmap[position]);
			}
			
			BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
			if(drawable != null)
			drawable.setAntiAlias(true);
			
			return i;
		}

		public float getScale(boolean focused, int offset) {
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}
	}
 
	private ProgressDialog mProgress;
	class ViewNextTask extends AsyncTask<String, Integer, Long> {
	 	
	 	public ViewNextTask(Context context) {
	 		
	 	}
	 
	 	@Override
	 	protected void onPreExecute() {
	 		// TODO Auto-generated method stub
	 		showProgress();
	 		super.onPreExecute();
	 	}
	 
	 	@Override
	 	protected Long doInBackground(String... params) {
	 		// TODO Auto-generated method stub
	 		UrlImageLoader urlImageload = new UrlImageLoader();
	 		for(int i = 0; i < 20; i++){
	 			if(!subImgs[i].equals(""))
			 		bitmap[i] = urlImageload.getUrlImage(subImgs[i]);
    			//if(i % 10 == 0)
    			publishProgress(i);
    		}
	 			
	 		String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("idx", idx));
			nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
			if(type.equals("shopping")){
				strJson = postmake.httpConnect(
					Constant.mainUrl + "/module/tv/shopping_hit.php", nameValuePairs);
			}else if(type.equals("delivery")){
				strJson = postmake.httpConnect(
						Constant.mainUrl + "/module/tv/delivery_hit.php", nameValuePairs);
			}
	 		return 0L;
	 	}
	 	
	 	@Override
	 	protected void onPostExecute(Long result) {
	 		// TODO Auto-generated method stub
	 		dismissProgress();
	 		imageAdapter.notifyDataSetChanged();
	 	}
	 
	 	@Override
	 	protected void onProgressUpdate(Integer... values) {
	 		// TODO Auto-generated method stub
	 		if(values[0] == 0)
	 			dismissProgress();
	 		imageAdapter.notifyDataSetChanged();
	 	}
	 
	 	protected void showProgress() {
    		mProgress = new ProgressDialog(DetailShoppingActivity.this);
    		mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                	ViewNextTask.this.cancel(true);
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
	 	protected void onCancelled() {
	 		// TODO Auto-generated method stub
	 		showCancelMessage();
	 		super.onCancelled();
	 	}	
	}
	
 	private void recycleAllBitmap(){
 		for(int i = 0; i < 20; i++){
 			if(bitmap[i] != null){
 				bitmap[i].recycle();
 				bitmap[i] = null;
 			}
 		}
 	}
}