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

public class DetailDialogActivity extends Dialog{
 
	 private ImageView imgPop;
	 private TextView txtTitle;
	 private TextView txtGenre;
	 private TextView txtPoint;
	 private TextView txtAge;
	 private TextView txtDate;
	 private TextView txtDirector;
	 private TextView txtCast;
	 private TextView txtStory;
    //private TextView mContentView;
   // private Button mLeftButton;
   // private Button mRightButton;
   // private Button mXButton;
   // private String mTitle;
   // private String mContent;
     
    //private View.OnClickListener mLeftClickListener;
    //private View.OnClickListener mRightClickListener;
    private View.OnKeyListener mXClickListener;
    private View.OnClickListener mClickListener;
    private String imgUrl;
    private String title;
    private String genre;
    private String point;
    private String age;
    private String date;
    private String director;
    private String cast;
    private String story;
    
    private RelativeLayout rl;
    
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
        
        setContentView(R.layout.dialogpopup);
        
        setLayout();
        setClickListener(mXClickListener, mClickListener);
        
        imageDownloader.download(imgUrl, (ImageView)imgPop);
        txtTitle.setText(title);
        txtGenre.setText(genre);
   	   	txtPoint.setText(point);
   	   	txtAge.setText(age);
   	   	txtDate.setText(date);
   	   	txtDirector.setText(director);
   	   	if(cast.length() > 20){
	   		cast = cast.substring(0, 20);
	   		cast += "..";
	   	}
   	   	txtCast.setText(cast);
   	   	if(story.length() > 210){
   	   		story = story.substring(0, 210);
   	   		story += "..";
   	   	}
   	   	txtStory.setText(story);
        
    }

    public DetailDialogActivity(){
        super(null);
    }

    public DetailDialogActivity(Context context,
                                View.OnClickListener clickListener, View.OnKeyListener xListener, String title, String imgUrl,
                                String genre, String point, String age, String date, String director, String cast, String story) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mClickListener = clickListener;
        this.mXClickListener = xListener;
        this.imgUrl = imgUrl;
        this.title = title;
        this.genre = genre;
        this.point = point;
        this.age = age;
        this.date = date;
        this.director = director;
        this.cast = cast;
        this.story = story;
    }
     
    
    private void setClickListener(View.OnKeyListener x, View.OnClickListener x2){
    	imgPop.setOnKeyListener(x);
    	imgPop.setOnClickListener(x2);
    	rl.setOnKeyListener(x);
    	rl.setOnClickListener(x2);
    }
     
	/*
     * Layout
     */
    private void setLayout(){
    	rl = (RelativeLayout)findViewById(R.id.rl);
    	imgPop = (ImageView) findViewById(R.id.imgPop);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtGenre = (TextView) findViewById(R.id.txtGenre);
        txtPoint = (TextView) findViewById(R.id.txtPoint);
        txtAge = (TextView) findViewById(R.id.txtAge);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDirector = (TextView) findViewById(R.id.txtDirector);
        txtCast = (TextView) findViewById(R.id.txtCast);
        txtStory = (TextView) findViewById(R.id.txtStory);
       // mXButton = (Button) findViewById(R.id.btnDialogX);
       // mLeftButton = (Button) findViewById(R.id.btnDialogOk);
        
    }
     
}
