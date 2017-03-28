 package com.japantv.setting;

import java.util.ArrayList;                      
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.japantv.model.BroadcastList;
import com.japantv.model.Constant;
import com.japantv.model.ShoppingList;
import com.japantv.settop.CoverFlow;
import com.japantv.settop.DetailDramaActivity;
import com.japantv.settop.DetailShowActivity;
import com.japantv.settop.MovieListActivity;
import com.japantv.settop.shopping.DetailShoppingActivity;
import com.japantv.settop.shopping.ShoppingCoverFlow;
import com.japantv.settop1.R;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HelpActivity extends Activity {

	private ShoppingCoverFlow coverFlow;
	
	private ArrayList<ShoppingList> broadcastList = null;
	private ImageAdapter imageAdapter = null;
	private Context mContext = null;
	private ViewNextTask viewNextTask = null;
	private TextView coverTextView = null;
	private ViewTextTask viewTextTask = null;
	private int nowCoverFlowIdx = 0;
	private boolean isDestroy = false;
	private ImageView ivTel = null;
	//modify
	//private EditText searchText = null;
	//private Button searchButton = null;
	private Toast noSearchToast = null;
	
	private ImageView btnLeft = null;
	private ImageView btnRight = null;
	
	private RelativeLayout rlMenu = null;
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	
	private boolean menuVisible = false;
	
	public HelpActivity(){
		broadcastList = new ArrayList<ShoppingList>();
		nowCoverFlowIdx = 0;
	}
	
	ImageView btnMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.shoppingcoverflow);

	    mContext = this.getBaseContext();

		
		btnMenu = (ImageView)findViewById(R.id.menu);	
		btnMenu.setVisibility(View.INVISIBLE);
		
		rlMenu = (RelativeLayout)findViewById(R.id.rlMenu);
		
		imgLock = (ImageView)findViewById(R.id.imgLock);
		if(!Util.getChildset(mContext))
			imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
		else
        	imgLock.setBackgroundResource(R.drawable.image_main_lock_on);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        
		
		btnLeft = (ImageView) findViewById(R.id.arrowleft);
		btnRight = (ImageView) findViewById(R.id.arrowright);
    	
		btnRight.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				nowCoverFlowIdx = coverFlow.getSelectedItemPosition() + 1;
				if(nowCoverFlowIdx >= broadcastList.size())
					nowCoverFlowIdx = broadcastList.size() - 1;
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
				coverFlow.setSelection(nowCoverFlowIdx);
				return false;
			}
		});
		
		//modify
		//searchButton = (Button)findViewById(R.id.searchButton);
		//searchText = (EditText)findViewById(R.id.searchText);
		ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		//ivTel.setImageResource(R.drawable.image_main_logo);
		imageDownloader.download(Constant.mainUrl + "/image/image_main_logo.png", (ImageView)ivTel);
		coverTextView = (TextView) findViewById(R.id.coverflowtext);
		Button expireText = (Button) findViewById(R.id.imgExpireDate);
		Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        
		GetTask getTask = new GetTask(mContext);
		getTask.execute();
		
		noSearchToast = Toast.makeText(this, getString(R.string.nosearch), Toast.LENGTH_SHORT);
		//modify
		/*searchButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				boolean searched = false;
				// TODO Auto-generated method stub
				if(!searchText.getText().toString().equals("")){
					for(int i =0; i < broadcastList.size(); i++){
						if(broadcastList.get(i).getTitle() != null && searchText.getText().toString() != null){
							if(broadcastList.get(i).getTitle().indexOf(searchText.getText().toString()) != -1){
								searched = true;
								coverFlow.setSelection(i);
								break;
							}
							if(!searched)
								noSearchToast.show();
						}
					}
				}
			}
		});*/
	}
	
	
	
	@Override
	protected void onDestroy() {
		isDestroy = true;
		// TODO Auto-generated method stub
		Log.e(null, "destroy");
		if(viewNextTask != null){
			viewNextTask.cancel(true);
			viewNextTask = null;
		}
		if(viewTextTask != null){
			viewTextTask.cancel(true);
			viewTextTask = null;
		}
		recycleAllBitmap();
		super.onDestroy();
	}

	//커버플로우에 넣어줄 이미지 데이터를 데이터구조에 넣어 둠
	private void getImages(){
		int startIdx = 0;
		int endIdx = 29999;
		broadcastList.clear();
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
		nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(endIdx)));
		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/help.php", nameValuePairs);
		JSONArray jArray = null;
		try{
			//JSONObject json = new JSONObject(strJson);
			jArray = new JSONArray(strJson);
			for(int i = 0; i < jArray.length(); i++){
				ShoppingList tempList = new ShoppingList();
				JSONObject json_data = jArray.getJSONObject(i); 
				tempList.setIdx(json_data.getString("idx"));
				tempList.setImage(json_data.getString("list_image"));
				tempList.setTitle(json_data.getString("title"));
				tempList.setDetail_image(Constant.mainUrl2 + "/post/" + json_data.getString("detail_image"));
				tempList.setSub_image1(json_data.getString("sub_image1"));
				tempList.setSub_image2(json_data.getString("sub_image2"));
				tempList.setSub_image3(json_data.getString("sub_image3"));
				tempList.setSub_image4(json_data.getString("sub_image4"));
				tempList.setSub_image5(json_data.getString("sub_image5"));
				tempList.setSub_image6(json_data.getString("sub_image6"));
				tempList.setSub_image7(json_data.getString("sub_image7"));
				tempList.setSub_image8(json_data.getString("sub_image8"));
				tempList.setSub_image9(json_data.getString("sub_image9"));
				tempList.setSub_image10(json_data.getString("sub_image10"));
				tempList.setSub_image11(json_data.getString("sub_image11"));
				tempList.setSub_image12(json_data.getString("sub_image12"));
				tempList.setSub_image13(json_data.getString("sub_image13"));
				tempList.setSub_image14(json_data.getString("sub_image14"));
				tempList.setSub_image15(json_data.getString("sub_image15"));
				tempList.setSub_image16(json_data.getString("sub_image16"));
				tempList.setSub_image17(json_data.getString("sub_image17"));
				tempList.setSub_image18(json_data.getString("sub_image18"));
				tempList.setSub_image19(json_data.getString("sub_image19"));
				tempList.setSub_image20(json_data.getString("sub_image20"));
				broadcastList.add(i, tempList);
			}
		}catch(JSONException e){
			Log.e(null, e.toString()); 
		}
		/*
		for(int i = 0; i < 5000; i++){
			BroadcastList tempList = new BroadcastList();
			broadcastList.add(tempList);
		}*/
	}
	
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}
	
	//커버플로우 설정
	private void setCoverFlow(){
		coverFlow = (ShoppingCoverFlow) findViewById(R.id.coverflow);
	    imageAdapter = new ImageAdapter(this);
		coverFlow.setAdapter(imageAdapter);
		
		coverFlow.setSpacing(-8);
		coverFlow.setAnimationDuration(1000);
		
		viewNextTask = new ViewNextTask(mContext, broadcastList, coverFlow.getSelectedItemPosition()-11, 
				coverFlow.getSelectedItemPosition()+11);
		viewNextTask.execute();

		coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		     @Override
		     public void onItemSelected(AdapterView<?> arg0, View arg1,
		       int arg2, long arg3) {
		    	 //coverFlow.requestFocus();
		    	 viewNextTask = new ViewNextTask(mContext, broadcastList, (int)arg3-11, 
		    			 (int)arg3+11);
		 		 viewNextTask.execute();
		    	 Log.e(null, broadcastList.get((int)arg3).getTitle());
		    	 viewTextTask = new ViewTextTask(mContext, broadcastList, broadcastList.get((int)arg3).getTitle());
		    	 viewTextTask.execute();
		    	 //if(arg3%10 == 0){
		    		// viewNextTask = new ViewNextTask(mContext, broadcastList, (int)arg3-10, (int)arg3+10);
		    		// viewNextTask.execute();
		    	 //}
		    	 nowCoverFlowIdx = (int)arg3;
		       }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
		coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				goDetailShoppingActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getDetail_image(), broadcastList.get(position).getSub_image1(), broadcastList.get(position).getSub_image2(), 
						 broadcastList.get(position).getSub_image3(),  broadcastList.get(position).getSub_image4(), 
						 broadcastList.get(position).getSub_image5(),
						 broadcastList.get(position).getSub_image6(), broadcastList.get(position).getSub_image7(), 
						 broadcastList.get(position).getSub_image8(),  broadcastList.get(position).getSub_image9(), 
						 broadcastList.get(position).getSub_image10(),
						 broadcastList.get(position).getSub_image11(), broadcastList.get(position).getSub_image12(), 
						 broadcastList.get(position).getSub_image13(),  broadcastList.get(position).getSub_image14(), 
						 broadcastList.get(position).getSub_image15(),
						 broadcastList.get(position).getSub_image16(), broadcastList.get(position).getSub_image17(), 
						 broadcastList.get(position).getSub_image18(),  broadcastList.get(position).getSub_image19(), 
						 broadcastList.get(position).getSub_image20());
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {

		}else if(keyCode == KeyEvent.KEYCODE_MENU) {
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void goDetailShoppingActivity(String idx,String sub1, String sub2, String sub3, String sub4, String sub5
			,String sub6, String sub7, String sub8, String sub9, String sub10
			,String sub11, String sub12, String sub13, String sub14, String sub15
			,String sub16, String sub17, String sub18, String sub19, String sub20, String sub21){
		Intent intent = new Intent(HelpActivity.this, DetailShoppingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle myData = new Bundle();	
		myData.putString("type", "help");
		myData.putString("idx", idx);
		myData.putString("sub1", sub1);
		myData.putString("sub1", sub1);
		myData.putString("sub1", sub1);
        myData.putString("sub2", sub2);
        myData.putString("sub3", sub3);
        myData.putString("sub4", sub4);
        myData.putString("sub5", sub5);
        myData.putString("sub6", sub6);
        myData.putString("sub7", sub7);
        myData.putString("sub8", sub8);
        myData.putString("sub9", sub9);
        myData.putString("sub10", sub10);
        myData.putString("sub11", sub11);
        myData.putString("sub12", sub12);
        myData.putString("sub13", sub13);
        myData.putString("sub14", sub14);
        myData.putString("sub15", sub15);
        myData.putString("sub16", sub16);
        myData.putString("sub17", sub17);
        myData.putString("sub18", sub18);
        myData.putString("sub19", sub19);
        myData.putString("sub20", sub20);
        myData.putString("sub21", sub21);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	//서버에서 이미지를 받아 옴
	 class ViewNextTask extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<ShoppingList> list = null;
    	private int startIdx;
    	private int endIdx;
    	//private Context mContext;
    	
    	public ViewNextTask(Context context, ArrayList<ShoppingList> list, int start, int end) {
    		if(start < 0)
    			start = 0;
    		this.list = list;
    		startIdx = start;
    		endIdx = end;
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
    		UrlImageLoader urlImageload = new UrlImageLoader();
    		if(startIdx < 0)
    			startIdx = 0;
    		if(endIdx > broadcastList.size())
    			endIdx = broadcastList.size();
    		for(int i = startIdx;i < endIdx; i++){
    			if(broadcastList.get(i).getBitmap() == null){
    				broadcastList.get(i).setBitmap(urlImageload.getUrlImage(Constant.mainUrl2 + "/post/" + list.get(i).getImage()));
    				Log.e(null, "wow" + Constant.mainUrl2 + "/post/" + list.get(i).getImage());
    			}
    			if(i % 10 == 0)
    				publishProgress(1);
    		}
	    		//				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		if( startIdx/100 > 1)
    			for(int i = 0; i < startIdx - (startIdx%100) - 10; i++){
    				if(broadcastList.get(i).getBitmap() != null){
    					broadcastList.get(i).recyleBitmap();
    			}
    		}
    		if( endIdx/100 < broadcastList.size()/100-1){
    			for(int i = endIdx + 10; i < broadcastList.size(); i++){
	    			if(broadcastList.get(i).getBitmap() != null){
	    				broadcastList.get(i).recyleBitmap();
	    			}
    			}
    		}
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		imageAdapter.notifyDataSetChanged();
    	}
    
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    		imageAdapter.notifyDataSetChanged();
    	}
    
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    	}	
    }
	
	 class ViewTextTask extends AsyncTask<String, Integer, Long> {
	    	private  ArrayList<BroadcastList> list = null;
	    	private String text;
	    	private Context mContext;
	    	
	    	public ViewTextTask(Context context, ArrayList<ShoppingList> list, String text) {
	    		this.text = text;
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
	    		try{
	    		Thread.sleep(700);
	    		}catch(Exception e){}
	    		return 0L;
	    	}
	    
	    	@Override
	    	protected void onPostExecute(Long result) {
	    		// TODO Auto-generated method stub
	    		coverTextView.setText(text);
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
	 public class ImageAdapter extends BaseAdapter {
			int itemBackground;
			private Context mContext;

			public ImageAdapter(Activity c) {
				mContext = c;
			}

			public int getCount() {
				return broadcastList.size();
			}

			public Object getItem(int position) {
				return broadcastList.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView i = new ImageView(mContext);
				i.setScaleType(ImageView.ScaleType.FIT_XY);
				i.setLayoutParams(new CoverFlow.LayoutParams(360, 520)); 
				i.setPadding(5, 5, 5, 5);
				
				i.setBackgroundResource(R.drawable.listborder);
				i.setScaleType(ImageView.ScaleType.FIT_XY);
				i.setImageBitmap(broadcastList.get(position).getBitmap());

				BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
				drawable.setAntiAlias(true);
				
				return i;
			}

			public float getScale(boolean focused, int offset) {
				return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
			}
		}
	 
	 private void recycleAllBitmap(){
		for(int i = 0; i <broadcastList.size(); i++)
			broadcastList.get(i).recyleBitmap();
	}

	//맥어드레스 가져오기
	private String getMacaddress() {
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("macaddress", "");
	}
	
	private String getAuth(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("auth", "");
	}
				
	private ProgressDialog mProgress;
	class GetTask extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<BroadcastList> list = null;

    	
    	public GetTask(Context context) {
    		mContext = context;
    	}
    
    	 @Override
    	 protected final void onPreExecute() {
    		 showProgress();
    	  }
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		//while(true) {
    			//break;
    		//}
    		broadcastList = new ArrayList<ShoppingList>();
    		getImages();
	    		//				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		//imageAdapter
    		//publishProgress(1);
    		return 0L;
    	}
    	
    	 
    	protected void showProgress() {
    		Log.e(null, "dddd");
    		mProgress = new ProgressDialog(HelpActivity.this);
    		mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                	GetTask.this.cancel(true);
                }
            });

            String message = "처리 중입니다.";
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
    		dismissProgress();

    		setCoverFlow();
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
