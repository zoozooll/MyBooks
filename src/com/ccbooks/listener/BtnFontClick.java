package com.ccbooks.listener;

import com.ccbooks.view.BookContentView;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class BtnFontClick implements OnClickListener{

	private BookContentView bcv ;
	private LinearLayout btnLightEnter; //亮度调节悬浮框；
	private FrameLayout btnFontEnter;  //字体调节悬浮框；

	public BtnFontClick(BookContentView bcv ,LinearLayout btnLightEnter, FrameLayout btnFontEnter) {
		super();
		this.bcv = bcv ;
		this.btnLightEnter = btnLightEnter;
		this.btnFontEnter = btnFontEnter;
	}


	
	
	
	// 点击字体按钮；
	

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			bcv.focus = BookContentView.FONTFOCUS;
		switch (bcv.showSettingView) {
			 case BookContentView.BCV : 
				btnFontEnter.setVisibility(View.VISIBLE);
				btnLightEnter.setVisibility(View.GONE);
				bcv.showSettingView = BookContentView.FONT;
				bcv.charactorList.setVisibility(View.GONE);
				
				bcv.btncharactorL.setVisibility(View.VISIBLE);
				bcv.btncharactor.setVisibility(View.VISIBLE);
				
					break; 
			 case BookContentView.FONT : 
				btnLightEnter.setVisibility(View.GONE);
				btnFontEnter.setVisibility(View.GONE);
				bcv.showSettingView = BookContentView.BCV;
				
				
				bcv.btncharactorL.setVisibility(View.VISIBLE);
				bcv.btncharactor.setVisibility(View.VISIBLE);
				bcv.charactorList.setVisibility(View.GONE);
					break; 
			 case 3 : 
					break; 
			 
			 default : 
				 	break; 
			}

		}
	
}
