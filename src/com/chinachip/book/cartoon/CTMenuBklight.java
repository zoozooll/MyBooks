package com.chinachip.book.cartoon;

import com.ccbooks.fullscreen.listener.MenuBklightSeekBarChange;
import com.ccbooks.fullscreen.listener.MenuClick;
import com.ccbooks.fullscreen.listener.MenuFontSeekBarChange;
import com.ccbooks.view.CartoonReader;
import com.ccbooks.view.R;
import com.chinachip.book.cartoon.listener.CTMenuBklightSeekBarChange;
import com.chinachip.book.cartoon.listener.CTMenuClick;
import com.chinachip.ccbooks.core.TextSizeUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class CTMenuBklight{
	private CartoonReader menuParent;
	private LinearLayout layoutLight;
	public TextView textInfo;
	public SeekBar seeBar;
	public WindowManager.LayoutParams bl;

	public static float ness ; 		
	
	public CTMenuBklight(CartoonReader tmObj){
		menuParent = tmObj;
		layoutLight = menuParent.layoutLight;
		bl = menuParent.getWindow().getAttributes();
		textInfo = (TextView)menuParent.findViewById(R.id.ct_bklight_info);
		
		seeBar = (SeekBar)menuParent.findViewById(R.id.ct_seebar_bklight);
		SharedPreferences sp = menuParent.getSharedPreferences("config", Activity.MODE_PRIVATE);
		ness = sp.getFloat("ness", 0.6f);

		seeBar.setProgress((int)ness);
		brightnessMax(ness*255);
		CTMenuBklightSeekBarChange seebarlistener = new CTMenuBklightSeekBarChange(this);
		seeBar.setOnSeekBarChangeListener(seebarlistener);	
		CTMenuClick menuclick = new CTMenuClick();
		layoutLight.setOnClickListener(menuclick);
	}

	public void progressChangeEvent(int size){
		if(size < 10)size=10;
		if(size > 255)size=255;
		bl.screenBrightness = ((float)size)/(float)255;
		
		ness = size;
		int brightness = (int)(bl.screenBrightness*100);
		
		textInfo.setText(String.valueOf(brightness)+"%");
		menuParent.getWindow().setAttributes(bl);
		
	}
	
	public LinearLayout getLayoutFont() {
		return layoutLight;
	}	

	public void setlayoutFun(LinearLayout layoutLight) {
		this.layoutLight = layoutLight;
	}
	
	public boolean isShowed(){
		if(View.VISIBLE == layoutLight.getVisibility())
			return true;
		else 
			return false;
	}
	
	public boolean show(){
		if(View.VISIBLE == layoutLight.getVisibility())return false;
		bl = menuParent.getWindow().getAttributes();
		String text = String.valueOf((int)(bl.screenBrightness*100))+"%";
		
		textInfo.setText(text);
		SharedPreferences sp = menuParent.getSharedPreferences("config", Activity.MODE_PRIVATE);
		ness = sp.getFloat("ness", 0.5f);
		seeBar.setProgress((int)(ness));
		layoutLight.setVisibility(View.VISIBLE);
		return true;
	}
	
	public boolean hide(){
		if(View.GONE == layoutLight.getVisibility())return false;
		layoutLight.setVisibility(View.GONE);
		SharedPreferences sp = menuParent.getSharedPreferences("config", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat("ness", ness);
		editor.commit();
		return true;
	}
	private void brightnessMax(float ness) {
		ness = ness/255;
		WindowManager.LayoutParams lp = menuParent.getWindow().getAttributes();
		if(ness>0.02)
		{
			lp.screenBrightness = ness;
		}
		else
		{
			lp.screenBrightness = 0.02f;
		}
			
		menuParent.getWindow().setAttributes(lp);
	}
}
