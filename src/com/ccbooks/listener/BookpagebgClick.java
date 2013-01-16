package com.ccbooks.listener;

import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class BookpagebgClick implements OnClickListener{
	
	private BookContentView bcv;
	private LinearLayout btnLightEnter; //亮度调节悬浮框；
	private FrameLayout btnFontEnter;  //字体调节悬浮框；

	public BookpagebgClick(BookContentView bcv,LinearLayout btnLightEnter,
			FrameLayout btnFontEnter) {
		super();
		this.bcv=bcv;
		this.btnLightEnter = btnLightEnter;
		this.btnFontEnter = btnFontEnter;
	}

	
	
	// 点击背景按钮；
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(-1 == bcv.judgeFocus(BookContentView.BCVFOCUS,1)){
				return ;
			}
			if (bcv.portraint == 1) {
				btnFontEnter.setVisibility(View.GONE);
				btnLightEnter.setVisibility(View.GONE);
				
			} else if (bcv.portraint == 2) {
				btnFontEnter.setVisibility(View.GONE);
				btnLightEnter.setVisibility(View.GONE);	 
				
			}
		}
			

		

	
}
