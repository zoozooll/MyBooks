package com.ccbooks.listener;

import com.ccbooks.view.BookContentView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class BtnLightClick implements OnClickListener{
	// 所有控件；
	private BookContentView bcv;

	
	private LinearLayout btnLightEnter; //亮度调节悬浮框；
	private FrameLayout btnFontEnter; // 字体调节悬浮框；
	public BtnLightClick(BookContentView bcv, LinearLayout btnLightEnter, FrameLayout btnFontEnter) {
		super();
		this.bcv = bcv;
		this.btnLightEnter = btnLightEnter;
		this.btnFontEnter = btnFontEnter;

	}

	// 点击亮度按钮；
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			bcv.focus = BookContentView.LIGHTFOCUS;
			switch (bcv.showSettingView) {
				 case BookContentView.BCV : 
			       int newNess;
			       if(bcv.ness <=1)
			          newNess =  (int)(bcv.ness *255);
			       else
			    	   newNess =  (int)(bcv.ness);
			       System.out.println("bcv.ness == "+bcv.ness);
			       System.out.println("newness == "+newNess);
					//if (bcv.ness<=0.2f){
					//	bcv.ness=0.2f;
			        bcv.btnLightBar.setProgress(newNess);
				 	btnLightEnter.setVisibility(View.VISIBLE);
					btnFontEnter.setVisibility(View.GONE);
					bcv.showSettingView = BookContentView.LIGHT;
					break; 
			 case BookContentView.LIGHT : 
				 	btnLightEnter.setVisibility(View.GONE);
					btnFontEnter.setVisibility(View.GONE);
					bcv.showSettingView = BookContentView.BCV;
					break; 
			 case 3 : 
					break; 
			 default : 
				 	break; 
			}

		}	
}
