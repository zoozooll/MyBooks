package com.ccbooks.listener;

import com.ccbooks.flip.SinglePage;
import com.ccbooks.util.StringConfig;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class BtnColorBrownClick implements OnClickListener{

	private BookContentView bcv;
	private FrameLayout bookpagebg;  //竖屏书背景；
	private Button btnFontEnterColorBlack; //白背景按钮；

	//选择棕色按钮；

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (bcv.portraint == 1) {
			if(bcv.backgroundColor!=StringConfig.BROWN_BACKGROUP_COLOR){
				bookpagebg.setBackgroundResource(R.drawable.bookpagebg_brown);
			}
		} else if (bcv.portraint == 2) {
			if(bcv.backgroundColor!=StringConfig.BROWN_BACKGROUP_COLOR){
				bookpagebg.setBackgroundResource(R.drawable.book_page_bg_horizontal_brown);
			}
		}
		bcv.backgroundColor=StringConfig.BROWN_BACKGROUP_COLOR;
		for(SinglePage sp : bcv.bookpagebg.pageList){
			sp.setBcColor(bcv.backgroundColor);
		}
		bcv.setBtnBackgroundColor();
		SharedPreferences sp = bcv.getSharedPreferences("config", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("colorBackground", bcv.backgroundColor);
		editor.commit();
		
		
	}




	public BtnColorBrownClick(BookContentView bcv,FrameLayout bookpagebg,
			Button btnFontEnterButtona1, Button btnFontEnterButtonA2,
			Button btnFontEnterColorBlack) {
		super();
		this.bcv = bcv;
		this.bookpagebg = bookpagebg;
		this.btnFontEnterColorBlack = btnFontEnterColorBlack;
	}
	
	
}
