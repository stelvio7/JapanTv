package com.japantv.settop;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.japantv.model.BroadcastList;
import com.japantv.model.Constant;
import com.japantv.setting.ChildActivity;
import com.japantv.setting.HelpActivity;
import com.japantv.setting.RechargeActivity;
import com.japantv.setting.RestoreActivity;
import com.japantv.setting.SearchActivity;
import com.japantv.settop.BroadcastListActivity.GetTask;
import com.japantv.settop.BroadcastListActivity.GetTask2;
import com.japantv.settop.delivery.DeliveryListActivity;
import com.japantv.settop.live.LiveListActivity;
import com.japantv.settop.shopping.ShoppingListActivity;
import com.japantv.settop1.R;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainMenuActivity extends Activity{
	
	private AnimationListener animationListener = null;
	private AnimationListener animationListener2 = null;
	private Context mContext = null;
	
	private int mainIdx;
	private int subIdx;
	private String mainid = null;
	private String subid = null;
	
	private ImageView imgLiveTop = null;
	private ImageView imgBroadcastTop = null;
	private ImageView imgMovieTop = null;
	private ImageView imgPaymentTop = null;
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	
	private ImageView btnLive = null;
	private ImageView btnBroadcast = null;
	private ImageView btnMovie = null;
	//private ImageView btnShopmall = null;
	//private ImageView btnFood = null;
	private ImageView btnSetting = null;
	
	private ImageView btnLiveGround = null;
	private ImageView btnLiveTotal = null;
	private ImageView btnLiveSports = null;
	private ImageView btnLiveCable = null;
	private ImageView btnLiveBaby = null;
	private ImageView btnLiveDacu = null;
	private ImageView btnLiveNews = null;
	private ImageView btnBroadcastUpdate = null;
	private ImageView btnBroadcastBox = null;
	private ImageView btnBroadcastCable = null;
	private ImageView btnBroadcastDrama = null;
	private ImageView btnBroadcastEdu = null;
	private ImageView btnBroadcastShow = null;
	private ImageView btnBroadcastCulture = null;
	private ImageView btnMovieUpdate = null;
	private ImageView btnMovieFor = null;
	private ImageView btnMovieKor = null;
	private ImageView btnMovieAda = null;
	private ImageView btnMovieChi = null;
	private ImageView btnMovieOld = null;
	private ImageView btnShopDigital = null;
	private ImageView btnShopFood = null;
	private ImageView btnShopLife = null;
	private ImageView btnShopWear = null;
	private ImageView btnShopLeisure = null;
	private ImageView btnShopGroup = null;
	private ImageView btnShopUsed = null;
	private ImageView btnShopEtc = null;
	private ImageView btnFoodKorea = null;
	private ImageView btnFoodChina = null;
	private ImageView btnFoodFlour = null;
	private ImageView btnFoodJapan = null;
	private ImageView btnFoodChicken = null;
	private ImageView btnFoodGrocery = null;
	private ImageView btnFoodBakery = null;
	private ImageView btnFoodMart = null;
	private ImageView btnSettingFind = null;
	private ImageView btnSettingHelp = null;
	private ImageView btnSettingChild = null;
	private ImageView btnSettingPayment = null;
	private ImageView btnUpdate = null;
	//private ImageView btnSettingPayment = null;
	
	private LinearLayout mainLayout = null;
	private LinearLayout LayoutLive = null;
	private LinearLayout LayoutBroadcast = null;
	private LinearLayout LayoutMovie = null;
	//private LinearLayout LayoutShoping = null;
	//private LinearLayout LayoutFood = null;
	private LinearLayout LayoutSetting = null;
	private Button expireText = null;
	private Button expireText2 = null;
	private ImageView ivTel = null;
	
	/** Called when the activity is first created. */
	public MainMenuActivity(){
		mainIdx = -1;
		subIdx = -1;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
       
        setContentView(R.layout.mainmenu);
    
        mContext = this.getBaseContext();
        
       
        
        imgLock = (ImageView)findViewById(R.id.imgLock);
        if(!Util.getChildset(mContext))
        	imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        else
        	imgNet.setBackgroundResource(R.drawable.image_main_net_on);
        expireText = (Button) findViewById(R.id.imgExpireDate);
        expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        
        ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		//ivTel.setImageResource(R.drawable.image_main_logo);
		imageDownloader.download(Constant.mainUrl + "/image/image_main_logo.png", (ImageView)ivTel);
        
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout); 
        setSubButtons();
        setMainButtons();
        setMenuopenAnimation();
        setMainMenuopenAnimation();
        setMainButtonListener();
        setSubButtonsFocusEvent();
        
	    //if(!mainLayout.isShown())	
	    	//return;
        mainLayout.setVisibility(View.VISIBLE);
	    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.mainmenu_sliding_open);
		animation.setAnimationListener(animationListener2);
		mainLayout.startAnimation(animation);	
		
    }
    
    
    
    private void setSubButtonsFocusEvent(){
    	btnLiveGround.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 0;
                }
            }
        });
    	btnLiveGround.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "live";
				subid = "138";
				goLive();
				return false;
			}
		});
    	
    	btnLiveTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 1;
                }
            }
        });
    	btnLiveTotal.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "live";
				subid = "139";
				goLive();
				return false;
			}
		});
    	
    	btnLiveSports.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 2;
                }
            }
        });
    	btnLiveSports.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "live";
				subid = "140";
				goLive();
				return false;
			}
		});
    	btnLiveCable.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 3;
                }
            }
        });
    	btnLiveCable.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "live";
				subid = "125";
				goLive();
				return false;
			}
		});
    	btnLiveBaby.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 4;
                }
            }
        });
    	btnLiveBaby.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "live";
				subid = "126";
				goLive();
				return false;
			}
		});
    	
    	btnLiveDacu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 5;
                }
            }
        });
    	btnLiveDacu.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "live";
				subid = "131";
				goLive();
				return false;
			}
		});
    	btnLiveNews.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 6;
                }
            }
        });
    	btnLiveNews.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "live";
				subid = "127";
				goLive();
				return false;
			}
		});
    	
		
    	btnBroadcastUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 0;
                }
            }
        });
    	btnBroadcastUpdate.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "broadcast";
				subid = "update";
				goMovie();
				return false;
			}
		});
    	btnBroadcastDrama.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 1;
                }
            }
        });
    	btnBroadcastDrama.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "broadcast";
				subid = "002";
				goBroadcast();
				return false;
			}
		});
    	btnBroadcastShow.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 2;
                }
            }
        });
    	btnBroadcastShow.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "broadcast";
				subid = "006";
				goBroadcast();
				return false;
			}
		});
    	btnBroadcastCulture.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 3;
                }
            }
        });
    	btnBroadcastCulture.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "broadcast";
				subid = "116";	//컬쳐
				goBroadcast();
				return false;
			}
		});
    	
    	
    	btnBroadcastEdu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 4;
                }
            }
        });
    	btnBroadcastEdu.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "broadcast";
				subid = "026";
				goBroadcast();
				return false;
			}
		});
    	btnBroadcastCable.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 5;
                }
            }
        });
    	btnBroadcastCable.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "broadcast";
				subid = "007";
				goBroadcast();
				return false;
			}
		});
    	btnBroadcastBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 6;
                }
            }
        });
    	btnBroadcastBox.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "broadcast";
				subid = "026";
				goBroadcast();
				return false;
			}
		});

    	btnMovieUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 0;
                }
            }
        });
    	btnMovieUpdate.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "movie";
				subid = "update";
				goMovie();
				return false;
			}
		});
    	btnMovieKor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 1;
                }
            }
        });
    	btnMovieKor.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "movie";
				subid = "034";
				goMovie();
				return false;
			}
		});
    	btnMovieFor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 2;
                }
            }
        });
    	btnMovieFor.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "movie";
				subid = "035";
				goMovie();
				return false;
			}
		});

    	btnMovieAda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 3;
                }
            }
        });
    	btnMovieAda.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "movie";
				subid = "036";
				goMovie();
				return false;
			}
		});
    	btnMovieChi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 4;
                }
            }
        });
    	btnMovieChi.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "movie";
				subid = "038";
				goMovie();
				return false;
			}
		});
    	btnMovieOld.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 5;
                }
            }
        });
    	btnMovieOld.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "movie";
				subid = "038";
				goMovie();
				return false;
			}
		});
 
    	/*btnShopDigital.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 0;
                }
            }
        });
    	btnShopDigital.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "shopping";
				subid = "digital";
				goShopping();
				return false;
			}
		});
    	btnShopFood.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 1;
                }
            }
        });
    	btnShopFood.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "shopping";
				subid = "food";
				goShopping();
				return false;
			}
		});
    	btnShopLife.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 2;
                }
            }
        });
    	btnShopLife.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "shopping";
				subid = "life";
				goShopping();
				return false;
			}
		});
    	btnShopWear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 3;
                }
            }
        });
    	btnShopWear.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "shopping";
				subid = "wear";
				goShopping();
				return false;
			}
		});
    	
    	btnShopLeisure.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 4;
                }
            }
        });
    	btnShopLeisure.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "shopping";
				subid = "leisure";
				goShopping();
				return false;
			}
		});
    	btnShopGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 5;
                }
            }
        });
    	btnShopGroup.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "shopping";
				subid = "group";
				goShopping();
				return false;
			}
		});
    	btnShopUsed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 6;
                }
            }
        });
    	btnShopUsed.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "shopping";
				subid = "used";
				goShopping();
				return false;
			}
		});
    	btnShopEtc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 7;
                }
            }
        });
    	btnShopEtc.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "shopping";
				subid = "other";
				goShopping();
				return false;
			}
		});
    	btnFoodKorea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 0;
                }
            }
        });
    	btnFoodKorea.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "food";
				subid = "korea";
				goDelivery();
				return false;
			}
		});
    	btnFoodChina.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 1;
                }
            }
        });
    	btnFoodChina.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "food";
				subid = "china";
				goDelivery();
				return false;
			}
		});
    	btnFoodFlour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 2;
                }
            }
        });
    	btnFoodFlour.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "food";
				subid = "flour";
				goDelivery();
				return false;
			}
		});
    	btnFoodJapan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 3;
                }
            }
        });
    	btnFoodJapan.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "food";
				subid = "japan";
				goDelivery();
				return false;
			}
		});
    	btnFoodChicken.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 4;
                }
            }
        });
    	btnFoodChicken.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "food";
				subid = "chicken";
				goDelivery();
				return false;
			}
		});
    	btnFoodGrocery.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 5;
                }
            }
        });
    	btnFoodGrocery.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "food";
				subid = "grocery";
				goDelivery();
				return false;
			}
		});
    	btnFoodBakery.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 6;
                }
            }
        });
    	btnFoodBakery.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "food";
				subid = "bakery";
				goDelivery();
				return false;
			}
		});
    	btnFoodMart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 7;
                }
            }
        });
    	btnFoodMart.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "food";
				subid = "mart";
				goDelivery();
				return false;
			}
		});*/
    	
    	btnSettingFind.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 0;
                }
            }
        });
    	btnSettingFind.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "setting";
				subid = "find";
				goFind();
				return false;
			}
		});
    	btnSettingHelp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 1;
                }
            }
        });
    	btnSettingHelp.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "setting";
				subid = "help";
				goHelp();
				return false;
			}
		});
    	btnSettingChild.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 2;
                }
            }
        });
    	btnSettingChild.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "setting";
				subid = "child";
				goChild();
				return false;
			}
		});
    	btnSettingPayment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 3;
                }
            }
        });
    	btnSettingPayment.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "setting";
				subid = "payment";
				goRecharge();
				return false;
			}
		});

    	btnUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	subIdx = 4;
                }
            }
        });
    	btnUpdate.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mainid = "setting";
				subid = "wifi";
				goWiFi();
				return false;
			}
		});
    	

    }

    
    private void setMenuopenAnimation(){
    	animationListener = new AnimationListener()
	    {
			public void onAnimationEnd(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationStart(Animation animation) {}
	    };
    }
    
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}
	
    private void setMainMenuopenAnimation(){
    	animationListener2 = new AnimationListener()
	    {
			public void onAnimationEnd(Animation animation) {
				btnLive.requestFocus();
				btnLiveGround.requestFocus();
			}
			public void onAnimationRepeat(Animation animation) {btnLive.requestFocus();btnLiveGround.requestFocus();}
			public void onAnimationStart(Animation animation) {}
	    };
    }
    
    private void setSubButtons(){
    	LayoutLive = (LinearLayout) findViewById(R.id.LayoutLive);
    	LayoutBroadcast = (LinearLayout) findViewById(R.id.LayoutBroadcast);
    	LayoutMovie = (LinearLayout) findViewById(R.id.LayoutMovie);
    	LayoutSetting = (LinearLayout) findViewById(R.id.LayoutSetting);
    	
    	//배열로 바꿔줄 것임
    	btnLiveGround = (ImageView) findViewById(R.id.btnLiveGround);
    	btnLiveTotal = (ImageView) findViewById(R.id.btnLiveTotal);
    	btnLiveSports = (ImageView) findViewById(R.id.btnLiveSports);
    	btnLiveCable = (ImageView) findViewById(R.id.btnLiveCable);
    	btnLiveBaby = (ImageView) findViewById(R.id.btnLiveBaby);
    	btnLiveDacu = (ImageView) findViewById(R.id.btnLiveDacu);
    	btnLiveNews = (ImageView) findViewById(R.id.btnLiveNews);
    	btnBroadcastUpdate = (ImageView) findViewById(R.id.btnBroadcastUpdate);
    	btnBroadcastCable = (ImageView) findViewById(R.id.btnBroadcastCable);
    	btnBroadcastDrama = (ImageView) findViewById(R.id.btnBroadcastDrama);
    	btnBroadcastShow = (ImageView) findViewById(R.id.btnBroadcastShow);
    	btnBroadcastCulture = (ImageView) findViewById(R.id.btnBroadcastCulture);
    	
    	btnBroadcastEdu = (ImageView) findViewById(R.id.btnBroadcastEdu);
    	btnBroadcastBox = (ImageView) findViewById(R.id.btnBroadcastBox);
    	btnMovieUpdate = (ImageView) findViewById(R.id.btnMovieUpdate);
    	btnMovieKor = (ImageView) findViewById(R.id.btnMovieKor);
    	btnMovieFor = (ImageView) findViewById(R.id.btnMovieFor);
    	btnMovieChi = (ImageView) findViewById(R.id.btnMovieChi);
    	btnMovieOld = (ImageView) findViewById(R.id.btnMovieOld);
    	btnMovieAda = (ImageView) findViewById(R.id.btnMovieAda);
    	
    	
    	btnSettingFind = (ImageView) findViewById(R.id.btnSettingFind);
    	btnSettingHelp = (ImageView) findViewById(R.id.btnSettingHelp);
    	btnSettingChild = (ImageView) findViewById(R.id.btnSettingChild);
    	btnSettingPayment = (ImageView) findViewById(R.id.btnSettingPayment);
    	btnUpdate = (ImageView) findViewById(R.id.btnUpdate);
    	//btnSettingPayment = (ImageView) findViewById(R.id.btnSettingPayment);
    }
    
    private void setMainButtons(){
    	
    	imgLiveTop = (ImageView) findViewById(R.id.imageLiveTop);
        imgBroadcastTop = (ImageView) findViewById(R.id.imageBroadcastTop);
        imgMovieTop = (ImageView) findViewById(R.id.imageMovieTop);
        imgPaymentTop = (ImageView) findViewById(R.id.imagePaymentTop);
        
   	 	btnLive = (ImageView) findViewById(R.id.btnLive);
        btnBroadcast = (ImageView) findViewById(R.id.btnBroadcast);
        btnMovie = (ImageView) findViewById(R.id.btnMovie);
        //btnShopmall = (ImageView) findViewById(R.id.btnShopmall);
        //btnFood = (ImageView) findViewById(R.id.btnFood);
        btnSetting = (ImageView) findViewById(R.id.btnSetting);
        
   }
    
    private void setMainButtonListener(){
        btnLive.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mainIdx = 0;
                	subIdx = -1;
                	
                	btnLive.setBackgroundResource(R.drawable.button_main_live_on);
                	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
                	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                    btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                    
                	//imgLiveTop.setPadding(v.getLeft()-2, 0, 0, 0);
                	imgLiveTop.setVisibility(View.VISIBLE);
                	LayoutLive.setPadding(v.getLeft(), 0, 0, 0);
                	LayoutLive.setVisibility(View.VISIBLE);
        		    if(LayoutLive != null) {
        		    	if(!LayoutLive.isShown())	return;
        		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
        			    animation.setAnimationListener(animationListener);
        	
        			    LayoutLive.startAnimation(animation);
        		    }		    
        		    imgBroadcastTop.setVisibility(View.INVISIBLE);
                	LayoutBroadcast.setVisibility(View.GONE);
                	imgMovieTop.setVisibility(View.INVISIBLE);
                	LayoutMovie.setVisibility(View.GONE);
                	//LayoutShoping.setVisibility(View.GONE);
                	//LayoutFood.setVisibility(View.GONE);
                	LayoutSetting.setVisibility(View.GONE);
                	imgPaymentTop.setVisibility(View.INVISIBLE);
                }
            }
        });
        
        btnLive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainIdx = 0;
            	subIdx = -1;
            	
            	btnLive.setBackgroundResource(R.drawable.button_main_live_on);
            	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
            	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                
            	//imgLiveTop.setPadding(v.getLeft()-2, 0, 0, 0);
                imgLiveTop.setVisibility(View.VISIBLE);
            	LayoutLive.setPadding(v.getLeft(), 0, 0, 0);
            	LayoutLive.setVisibility(View.VISIBLE);
    		    if(LayoutLive != null) {
    		    	if(!LayoutLive.isShown())	return;
    		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
    			    animation.setAnimationListener(animationListener);
    	
    			    LayoutLive.startAnimation(animation);
    		    }		    
    		    imgBroadcastTop.setVisibility(View.INVISIBLE);
            	LayoutBroadcast.setVisibility(View.GONE);
            	imgMovieTop.setVisibility(View.INVISIBLE);
            	LayoutMovie.setVisibility(View.GONE);
            	//LayoutShoping.setVisibility(View.GONE);
            	//LayoutFood.setVisibility(View.GONE);
            	LayoutSetting.setVisibility(View.GONE);
            	imgPaymentTop.setVisibility(View.INVISIBLE);
			}
		});
        
    	btnBroadcast.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mainIdx = 1;
                	subIdx = -1;
                	
                	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
                	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_on);
                	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                    btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                    
                	//imgBroadcastTop.setPadding(v.getLeft()-2, 0, 0, 0);
                	imgBroadcastTop.setVisibility(View.VISIBLE);
                	LayoutBroadcast.setPadding(v.getLeft(), 0, 0, 0);
                	LayoutBroadcast.setVisibility(View.VISIBLE);
                	if(LayoutBroadcast != null) {
        		    	if(!LayoutBroadcast.isShown())	return;
        		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
        			    animation.setAnimationListener(animationListener);
        	
        			    LayoutBroadcast.startAnimation(animation);
        		    }
                	imgLiveTop.setVisibility(View.INVISIBLE);
                	LayoutLive.setVisibility(View.GONE);
                	imgMovieTop.setVisibility(View.INVISIBLE);
                	LayoutMovie.setVisibility(View.GONE);
                	//LayoutShoping.setVisibility(View.GONE);
                	//LayoutFood.setVisibility(View.GONE);
                	LayoutSetting.setVisibility(View.GONE);
                	imgPaymentTop.setVisibility(View.INVISIBLE);
                }
            }
        });
    	
    	btnBroadcast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainIdx = 1;
            	subIdx = -1;
            	
            	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
            	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_on);
            	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                
            	//imgBroadcastTop.setPadding(v.getLeft()-2, 0, 0, 0);
            	imgBroadcastTop.setVisibility(View.VISIBLE);
            	LayoutBroadcast.setPadding(v.getLeft(), 0, 0, 0);
            	LayoutBroadcast.setVisibility(View.VISIBLE);
            	if(LayoutBroadcast != null) {
    		    	if(!LayoutBroadcast.isShown())	return;
    		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
    			    animation.setAnimationListener(animationListener);
    	
    			    LayoutBroadcast.startAnimation(animation);
    		    }
            	imgLiveTop.setVisibility(View.INVISIBLE);
            	LayoutLive.setVisibility(View.GONE);
            	imgMovieTop.setVisibility(View.INVISIBLE);
            	LayoutMovie.setVisibility(View.GONE);
            	//LayoutShoping.setVisibility(View.GONE);
            	//LayoutFood.setVisibility(View.GONE);
            	LayoutSetting.setVisibility(View.GONE);
            	imgPaymentTop.setVisibility(View.INVISIBLE);
			}
		});
    	
        btnMovie.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mainIdx = 2;
                	subIdx = -1;
                	
                	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
                	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
                	btnMovie.setBackgroundResource(R.drawable.button_main_movie_on);
                    btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                    
                	//imgMovieTop.setPadding(v.getLeft()-2, 0, 0, 0);
                	imgMovieTop.setVisibility(View.VISIBLE);
                	LayoutMovie.setPadding(v.getLeft(), 0, 0, 0);
                	LayoutMovie.setVisibility(View.VISIBLE);
                	if(LayoutMovie != null) {
        		    	if(!LayoutMovie.isShown())	return;
        		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
        			    animation.setAnimationListener(animationListener);
        	
        			    LayoutMovie.startAnimation(animation);
        		    }
                	imgLiveTop.setVisibility(View.INVISIBLE);
                	LayoutLive.setVisibility(View.GONE);
                	imgBroadcastTop.setVisibility(View.INVISIBLE);
                	LayoutBroadcast.setVisibility(View.GONE);
                	//LayoutShoping.setVisibility(View.GONE);
                	//LayoutFood.setVisibility(View.GONE);
                	LayoutSetting.setVisibility(View.GONE);
                	imgPaymentTop.setVisibility(View.INVISIBLE);
                } 
            }
        });
        
        btnMovie.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainIdx = 2;
            	subIdx = -1;
            	
            	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
            	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
            	btnMovie.setBackgroundResource(R.drawable.button_main_movie_on);
                btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                
            	//imgMovieTop.setPadding(v.getLeft()-2, 0, 0, 0);
            	imgMovieTop.setVisibility(View.VISIBLE);
            	LayoutMovie.setPadding(v.getLeft(), 0, 0, 0);
            	LayoutMovie.setVisibility(View.VISIBLE);
            	if(LayoutMovie != null) {
    		    	if(!LayoutMovie.isShown())	return;
    		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
    			    animation.setAnimationListener(animationListener);
    	
    			    LayoutMovie.startAnimation(animation);
    		    }
            	imgLiveTop.setVisibility(View.INVISIBLE);
            	LayoutLive.setVisibility(View.GONE);
            	imgBroadcastTop.setVisibility(View.INVISIBLE);
            	LayoutBroadcast.setVisibility(View.GONE);
            	//LayoutShoping.setVisibility(View.GONE);
            	//LayoutFood.setVisibility(View.GONE);
            	LayoutSetting.setVisibility(View.GONE);
            	imgPaymentTop.setVisibility(View.INVISIBLE);
			}
		});
        
        /*btnShopmall.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mainIdx = 3;
                	subIdx = -1;
                	
                	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
                	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
                	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                    btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                    
                	//imgMovieTop.setPadding(v.getLeft()-2, 0, 0, 0);
                	imgMovieTop.setVisibility(View.VISIBLE);
                    LayoutShoping.setPadding(v.getLeft(), 0, 0, 0);
                    LayoutShoping.setVisibility(View.VISIBLE);
                	if(LayoutShoping != null) {
        		    	if(!LayoutShoping.isShown())	return;
        		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
        			    animation.setAnimationListener(animationListener);
        	
        			    LayoutShoping.startAnimation(animation);
        		    }
                	imgLiveTop.setVisibility(View.GONE);
                	LayoutLive.setVisibility(View.GONE);
                	imgBroadcastTop.setVisibility(View.GONE);
                	LayoutBroadcast.setVisibility(View.GONE);
                	LayoutMovie.setVisibility(View.GONE);
                	LayoutFood.setVisibility(View.GONE);
                	LayoutSetting.setVisibility(View.GONE);
                	imgPaymentTop.setVisibility(View.GONE);
                } 
            }
        });
        
        btnShopmall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainIdx = 3;
            	subIdx = -1;
            	
            	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
            	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
            	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                
            	//imgMovieTop.setPadding(v.getLeft()-2, 0, 0, 0);
            	imgMovieTop.setVisibility(View.VISIBLE);
                LayoutShoping.setPadding(v.getLeft(), 0, 0, 0);
                LayoutShoping.setVisibility(View.VISIBLE);
            	if(LayoutShoping != null) {
    		    	if(!LayoutShoping.isShown())	return;
    		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
    			    animation.setAnimationListener(animationListener);
    	
    			    LayoutShoping.startAnimation(animation);
    		    }
            	imgLiveTop.setVisibility(View.GONE);
            	LayoutLive.setVisibility(View.GONE);
            	imgBroadcastTop.setVisibility(View.GONE);
            	LayoutBroadcast.setVisibility(View.GONE);
            	LayoutMovie.setVisibility(View.GONE);
            	LayoutFood.setVisibility(View.GONE);
            	LayoutSetting.setVisibility(View.GONE);
            	imgPaymentTop.setVisibility(View.GONE);
			}
		});
        
        btnFood.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mainIdx = 4;
                	subIdx = -1;
                	
                	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
                	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
                	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                    btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                    
                	//imgMovieTop.setPadding(v.getLeft()-2, 0, 0, 0);
                	imgMovieTop.setVisibility(View.VISIBLE);
                	LayoutFood.setPadding(v.getLeft(), 0, 0, 0);
                	LayoutFood.setVisibility(View.VISIBLE);
                	if(LayoutFood != null) {
        		    	if(!LayoutFood.isShown())	return;
        		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
        			    animation.setAnimationListener(animationListener);
        	
        			    LayoutFood.startAnimation(animation);
        		    }
                	imgLiveTop.setVisibility(View.GONE);
                	LayoutLive.setVisibility(View.GONE);
                	imgBroadcastTop.setVisibility(View.GONE);
                	LayoutBroadcast.setVisibility(View.GONE);
                	LayoutShoping.setVisibility(View.GONE);
                	LayoutMovie.setVisibility(View.GONE);
                	LayoutSetting.setVisibility(View.GONE);
                	//imgPaymentTop.setVisibility(View.GONE);
                } 
            }
        });
        
        btnFood.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainIdx = 4;
            	subIdx = -1;
            	
            	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
            	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
            	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                btnSetting.setBackgroundResource(R.drawable.button_main_setting_off);
                
            	//imgMovieTop.setPadding(v.getLeft()-2, 0, 0, 0);
            	imgMovieTop.setVisibility(View.VISIBLE);
                LayoutFood.setPadding(v.getLeft(), 0, 0, 0);
                LayoutFood.setVisibility(View.VISIBLE);
            	if(LayoutFood != null) {
    		    	if(!LayoutFood.isShown())	return;
    		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
    			    animation.setAnimationListener(animationListener);
    	
    			    LayoutFood.startAnimation(animation);
    		    }
            	imgLiveTop.setVisibility(View.GONE);
            	LayoutLive.setVisibility(View.GONE);
            	imgBroadcastTop.setVisibility(View.GONE);
            	LayoutBroadcast.setVisibility(View.GONE);
            	LayoutShoping.setVisibility(View.GONE);
            	LayoutMovie.setVisibility(View.GONE);
            	LayoutSetting.setVisibility(View.GONE);
            	imgPaymentTop.setVisibility(View.GONE);
			}
		});
        */
        btnSetting.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mainIdx = 5;
                	subIdx = -1;
                	//imgPaymentTop.setPadding(v.getLeft()-2, 0, 0, 0);
                	//imgPaymentTop.setVisibility(View.VISIBLE);
                	
                	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
                	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
                	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                    btnSetting.setBackgroundResource(R.drawable.button_main_setting_on);
                    
                    //imgPaymentTop.setPadding(v.getLeft()-2, 0, 0, 0);
                    imgPaymentTop.setVisibility(View.VISIBLE);
                    LayoutSetting.setPadding(v.getLeft(), 0, 0, 0);
                    LayoutSetting.setVisibility(View.VISIBLE);
                	if(LayoutSetting != null) {
        		    	if(!LayoutSetting.isShown())	return;
        		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
        			    animation.setAnimationListener(animationListener);
        	
        			    LayoutSetting.startAnimation(animation);
        		    }
                	
                	imgLiveTop.setVisibility(View.INVISIBLE);
                	LayoutLive.setVisibility(View.GONE);
                	imgBroadcastTop.setVisibility(View.INVISIBLE);
                	imgMovieTop.setVisibility(View.INVISIBLE);
                	LayoutMovie.setVisibility(View.GONE);
                	LayoutBroadcast.setVisibility(View.GONE);
                	//LayoutShoping.setVisibility(View.GONE);
                	//LayoutFood.setVisibility(View.GONE);
                	
                	
                } 
            }
        });
    	
        btnSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainIdx = 5;
            	subIdx = -1;
            	//imgPaymentTop.setPadding(v.getLeft()-2, 0, 0, 0);
            	//imgPaymentTop.setVisibility(View.VISIBLE);
            	
            	btnLive.setBackgroundResource(R.drawable.button_main_live_off);
            	btnBroadcast.setBackgroundResource(R.drawable.button_main_broadcast_off);
            	btnMovie.setBackgroundResource(R.drawable.button_main_movie_off);
                btnSetting.setBackgroundResource(R.drawable.button_main_setting_on);
                
                //imgPaymentTop.setPadding(v.getLeft()-2, 0, 0, 0);
                imgPaymentTop.setVisibility(View.VISIBLE);
                LayoutSetting.setPadding(v.getLeft(), 0, 0, 0);
                LayoutSetting.setVisibility(View.VISIBLE);
            	if(LayoutSetting != null) {
    		    	if(!LayoutSetting.isShown())	return;
    		    	Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.menu_sliding_open);
    			    animation.setAnimationListener(animationListener);
    	
    			    LayoutSetting.startAnimation(animation);
    		    }
            	
            	imgLiveTop.setVisibility(View.INVISIBLE);
            	LayoutLive.setVisibility(View.GONE);
            	imgBroadcastTop.setVisibility(View.INVISIBLE);
            	imgMovieTop.setVisibility(View.INVISIBLE);
            	LayoutMovie.setVisibility(View.GONE);
            	LayoutBroadcast.setVisibility(View.GONE);
            	//LayoutShoping.setVisibility(View.GONE);
            	//LayoutFood.setVisibility(View.GONE);
            	
            	
			}
		});
    }
    
	@Override
	protected void onDestroy() {
		if(imgLiveTop != null)
			imgLiveTop = null;
		if(imgBroadcastTop != null)
			imgBroadcastTop = null;
		if(imgMovieTop != null)
			imgMovieTop = null;
		if(imgPaymentTop != null)
			imgPaymentTop = null;
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        else
        	imgNet.setBackgroundResource(R.drawable.image_main_net_on);
        if(!Util.getChildset(mContext))
        	imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        else
        	imgLock.setBackgroundResource(R.drawable.image_main_lock_on);
		super.onResume();
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
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			//finish();
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			//finish();
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
			switch(mainIdx){
				case 0:
					//라이브
					mainid = "live";
					switch(subIdx){
						case 0:
							subid = "138";
							break;
						case 1:
							subid = "139";
							break;
						case 2:
							subid = "140";
							break;
						/*case 3:
							subid = "125";
							break;
						case 4:
							subid = "126";
							break;
						case 5:
							subid = "131";
							break;
						case 6:
							subid = "127";
							break;*/
					}
					if(subIdx >= 0)
						goLive();
					break;
				case 1:
					//브로드캐스트
					mainid = "broadcast";
					switch(subIdx){
						case 0:
							subid = "update";
							break;
						case 1:
							subid = "002";
							break;
						case 2:
							subid = "006";
							break;
						case 3:
							subid = "116";
							break;
						case 4:
							subid = "026";
							break;
						/*case 5:
							subid = "026";
							break;
						case 6:	//컬쳐
							subid = "116";
							break;*/
					}
					if(subIdx > 0)
						goBroadcast();
					else if(subIdx == 0)
						goMovie();
					break;
				case 2:
					mainid = "movie";
					switch(subIdx){
						case 0:
							subid = "update";
							break;
						case 1:
							subid = "034";
							break;
						case 2:
							subid = "035";
							break;
						case 3:
							subid = "036";
							break;
						case 4:
							subid = "038";
							break;
						/*case 5:
							subid = "038";
							break;*/
					}
					if(subIdx >= 0)
						goMovie();
				break;
				case 3:
					mainid = "shopping";
					switch(subIdx){
						case 0:
							subid = "digital";
							break;
						case 1:
							subid = "food";
							break;
						case 2:
							subid = "life";
							break;
						case 3:
							subid = "wear";
							break;
						case 4:
							subid = "leisure";
							break;
						case 5:
							subid = "group";
							break;
						case 6:
							subid = "used";
							break;
						case 7:
							subid = "other";
							break;
					}
					if(subIdx >= 0)
						goShopping();
				break;
				case 4:
					mainid = "food";
					switch(subIdx){
						case 0:
							subid = "korea";
							break;
						case 1:
							subid = "china";
							break;
						case 2:
							subid = "flour";
							break;
						case 3:
							subid = "japan";
							break;
						case 4:
							subid = "chicken";
							break;
						case 5:
							subid = "grocery";
							break;
						case 6:
							subid = "bakery";
							break;
						case 7:
							subid = "mart";
							break;
					}
					if(subIdx >= 0)
						goDelivery();
				break;
				case 5:
					mainid = "setting";
					switch(subIdx){
						case 0:
							subid = "find";
							goFind();
							break;
						case 1:
							subid = "help";
							goHelp();
							break;
						case 2:
							subid = "child";
							goChild();
							break;
						case 3:
							subid = "payment";
							goRecharge();
							break;
						case 4:
							subid = "wifi";
							goWiFi();
							break;
						
					}
					//if(subIdx >= 0)
						//goMovie();
				break;
			}
		}
  	  return super.onKeyDown(keyCode, event);  	  
  }
	
	private void goFind(){
		Intent intent = new Intent(MainMenuActivity.this, RestoreActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private void goHelp(){
		Intent intent = new Intent(MainMenuActivity.this, HelpActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private void goChild(){
		Intent intent = new Intent(MainMenuActivity.this, ChildActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private void goNet(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
		startActivity(intent);
	}
	
	private void goWiFi(){
		//GetUpdate getTask = new GetUpdate(mContext);
		//getTask.execute();

		checkVersion();
		/*Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);*/
	}

	private void goLive(){
		Intent intent = new Intent(MainMenuActivity.this, LiveListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle myData = new Bundle();	
        myData.putString("mainid", mainid);
        myData.putString("subid", subid);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	private void goBroadcast(){
		Intent intent = new Intent(MainMenuActivity.this, BroadcastListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle myData = new Bundle();	
        myData.putString("mainid", mainid);
        myData.putString("subid", subid);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	private void goMovie(){
		Intent intent = new Intent(MainMenuActivity.this, MovieListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle myData = new Bundle();	
        myData.putString("mainid", mainid);
        myData.putString("subid", subid);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	private void goShopping(){
		Intent intent = new Intent(MainMenuActivity.this, ShoppingListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle myData = new Bundle();	
        myData.putString("mainid", mainid);
        myData.putString("subid", subid);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	private void goDelivery(){
		Intent intent = new Intent(MainMenuActivity.this, DeliveryListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle myData = new Bundle();	
        myData.putString("mainid", mainid);
        myData.putString("subid", subid);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	private void goRecharge(){
		Intent intent = new Intent(MainMenuActivity.this, RechargeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	class GetUpdate extends AsyncTask<String, Integer, Long> {
    	private String url;

    	
    	public GetUpdate(Context context) {
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

    		strJson = postmake.httpConnect(
    				Constant.mainUrl + "/module/tv/app_update.php", nameValuePairs);
    		try{
    			JSONObject json = new JSONObject(strJson);
    			//jArray = new JSONArray(strJson);
    			url = json.getString("url");
    			
    		}catch(JSONException e){
    			Log.e(null, e.toString()); 
    		}
    		//publishProgress(1);
    		return 0L;
    	}
    	
    	private ProgressDialog mProgress;
    	protected void showProgress() {
    		mProgress = new ProgressDialog(MainMenuActivity.this);
    		mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                	GetUpdate.this.cancel(true);
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
    		dismissProgress();
    		Intent intent = new Intent(Intent.ACTION_VIEW ,Uri.parse(url));
    	    startActivity(intent); 
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

	private void checkVersion(){
		VersionTask task = new VersionTask(getBaseContext());
		task.execute();
	}

	public class UpdateApp extends AsyncTask<String,Void,Void>{
		private Context context;
		public void setContext(Context contextf){
			context = contextf;
		}

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				URL url = new URL(arg0[0]);
				Log.e("url", url.getPath());
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
		}}


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
				if(Integer.valueOf(appver) > getVersionCode(mContext)){

					AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainMenuActivity.this);
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
					AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainMenuActivity.this);
					alt_bld.setMessage(R.string.noupdate);
					alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					AlertDialog alert = alt_bld.create();
					alert.show();
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

	public int getVersionCode(Context context) {
		int code = 0;
		if(context != null) {
			try {
				PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				code = pi.versionCode;
			} catch (PackageManager.NameNotFoundException e) {

			};
		}
		return code;
	}
}
