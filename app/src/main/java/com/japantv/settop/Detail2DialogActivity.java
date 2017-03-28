package com.japantv.settop;
import com.japantv.settop1.R; 
import com.noh.util.ImageDownloader;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Detail2DialogActivity extends Dialog{
 
    //private TextView mContentView;
   // private Button mLeftButton;
   // private Button mRightButton;
   // private Button mXButton;
   // private String mTitle;
   // private String mContent;
     
    //private View.OnClickListener mLeftClickListener;
    //private View.OnClickListener mRightClickListener;
    private View.OnKeyListener mXClickPlayListener;
    private View.OnClickListener mClickPlayListener;
    private View.OnKeyListener mXClickRestoreListener;
    private View.OnClickListener mClickRestoreListener;
    private Button movieadd;
    private Button movieplay;
    private String type;
    
	private final ImageDownloader imageDownloader = new ImageDownloader();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        requestWindowFeature(Window.FEATURE_NO_TITLE); 


		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        
        if(type.equals("delete"))
        	setContentView(R.layout.dialogpopupdelete);
        else
        	setContentView(R.layout.dialogpopupmovie);
        
        setLayout();
        setClickListener(mXClickPlayListener, mClickPlayListener, mXClickRestoreListener, mClickRestoreListener);
        
       
        
    }
     
    

     
    public Detail2DialogActivity(Context context, 
            View.OnClickListener mClickPlayListener, View.OnKeyListener mXClickPlayListener ,View.OnClickListener mClickRestoreListener, View.OnKeyListener mXClickRestoreListener, String type) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mClickPlayListener = mClickPlayListener;
        this.mXClickPlayListener = mXClickPlayListener;
        this.mClickRestoreListener = mClickRestoreListener;
        this.mXClickRestoreListener = mXClickRestoreListener;
        this.type = type;
    }
     
    
    private void setClickListener(View.OnKeyListener x, View.OnClickListener x2, View.OnKeyListener x3, View.OnClickListener x4){
    	
    	movieplay.setOnKeyListener(x);
    	movieplay.setOnClickListener(x2);
    	movieadd.setOnKeyListener(x3);
    	movieadd.setOnClickListener(x4);
    }
     
	/*
     * Layout
     */
    private void setLayout(){
    	
    	movieplay = (Button) findViewById(R.id.movieplay);
        movieadd = (Button) findViewById(R.id.movieadd);
        
    }
     
}
