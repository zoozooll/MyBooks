package com.chinachip.TextReader;

import com.ccbooks.fullscreen.listener.MenuBklightSeekBarChange;
import com.ccbooks.fullscreen.listener.MenuClick;
import com.ccbooks.fullscreen.listener.MenuFontSeekBarChange;
import com.ccbooks.view.R;
import com.chinachip.ccbooks.core.TextSizeUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MenuBklight extends MenuFun{
	private TextMenu menuParent;
	private LinearLayout layoutFun;
	private TextView textInfo;
	private SeekBar seeBar;
	private WindowManager.LayoutParams bl;
	private int blLevel;
	public static float ness ; 		
	
	public MenuBklight(TextMenu tmObj){
		menuParent = tmObj;
		layoutFun = (LinearLayout)menuParent.getLayoutMenu().findViewById(R.id.fun_bklight);
		bl = menuParent.getActParent().getWindow().getAttributes();
		textInfo = (TextView)menuParent.getLayoutMenu().findViewById(R.id.fun_bklight_info);
		seeBar = (SeekBar)menuParent.getLayoutMenu().findViewById(R.id.seebar_bklight);
		SharedPreferences sp = menuParent.getActParent().getSharedPreferences("config", Activity.MODE_PRIVATE);
		ness = sp.getFloat("ness", 0.5f);
		seeBar.setProgress((int)ness);
		brightnessMax(ness);
//		MenuBklightSeekBarChange seebarlistener = new MenuBklightSeekBarChange(this);
//		seeBar.setOnSeekBarChangeListener(seebarlistener);	
    	MenuClick menuclick = new MenuClick();
    	layoutFun.setOnClickListener(menuclick);
	}

	public void progressChangeEvent(int size){
		if(size < 10)size=10;
		if(size > 255)size=255;
		bl.screenBrightness = ((float)size)/(float)255;
		
		ness = size;
		textInfo.setText(String.valueOf((int)(bl.screenBrightness*100))+"%");
		menuParent.getActParent().getWindow().setAttributes(bl);
		
	}
	
	public LinearLayout getLayoutFont() {
		return layoutFun;
	}	

	public void setlayoutFun(LinearLayout layoutFun) {
		this.layoutFun = layoutFun;
	}
	
	public boolean isShowed(){
		if(View.VISIBLE == layoutFun.getVisibility())
			return true;
		else 
			return false;
	}
	
	public boolean show(){
		if(View.VISIBLE == layoutFun.getVisibility())return false;
		bl = menuParent.getActParent().getWindow().getAttributes();
		textInfo.setText(String.valueOf((int)(bl.screenBrightness*100))+"%");
		SharedPreferences sp = menuParent.getActParent().getSharedPreferences("config", Activity.MODE_PRIVATE);
		ness = sp.getFloat("ness", 0.5f);
		seeBar.setProgress((int)(ness));
		layoutFun.setVisibility(View.VISIBLE);
		return true;
	}
	
	public boolean hide(){
		if(View.GONE == layoutFun.getVisibility())return false;
		layoutFun.setVisibility(View.GONE);
		SharedPreferences sp = menuParent.getActParent().getSharedPreferences("config", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat("ness", ness);
		editor.commit();
		return true;
	}
	private void brightnessMax(float ness) {
		ness = ness/255;
		WindowManager.LayoutParams lp = menuParent.getActParent().getWindow().getAttributes();
		if(ness>0.02)
		{
			lp.screenBrightness = ness;
		}
		else
		{
			lp.screenBrightness = 0.02f;
		}
			
		menuParent.getActParent().getWindow().setAttributes(lp);
	}
}
