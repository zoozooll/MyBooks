package com.ccbooks.listener;

import com.ccbooks.flip.SinglePage;
import com.ccbooks.util.StringConfig;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

//白色背景图片
public class BtnColorBlackClick implements OnClickListener{

	private BookContentView bcv;
	private FrameLayout bookpagebg;  //竖屏书背景；	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (bcv.portraint == 1) {
			if(bcv.backgroundColor != StringConfig.WHITE_BACKGROUP_COLOR){

				bookpagebg.setBackgroundResource(R.drawable.bookpagebg);
			}
		} else if (bcv.portraint != StringConfig.WHITE_BACKGROUP_COLOR) {
			if(bcv.backgroundColor!=StringConfig.WHITE_BACKGROUP_COLOR){
				bookpagebg.setBackgroundResource(R.drawable.book_page_bg_horizontal);
			}
		}
		bcv.backgroundColor=StringConfig.WHITE_BACKGROUP_COLOR;
		for(SinglePage sp : bcv.bookpagebg.pageList){
			sp.setBcColor(bcv.backgroundColor);
		}
		bcv.setBtnBackgroundColor();
		SharedPreferences sp = bcv.getSharedPreferences("config", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("colorBackground", bcv.backgroundColor);
		editor.commit();
	}




	public BtnColorBlackClick(BookContentView bcv,FrameLayout bookpagebg,
			Button btnFontEnterButtona1, Button btnFontEnterButtonA2,
			Button btnFontEnterColorBrown) {
		super();
		this.bcv = bcv;
		this.bookpagebg = bookpagebg;

	}
	
}
