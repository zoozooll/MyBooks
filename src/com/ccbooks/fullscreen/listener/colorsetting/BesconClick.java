package com.ccbooks.fullscreen.listener.colorsetting;

import com.chinachip.TextReader.MenuFun;
import com.chinachip.TextReader.MenuSetting;
import com.chinachip.TextReader.MenuTheme;
import com.chinachip.TextReader.TextMenu;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class BesconClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	static TextMenu menu;
	static MenuTheme funObj;
	
	public BesconClick(TextMenu tmObj, MenuTheme llObj){
		menu = tmObj;
		funObj = llObj;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
			funObj.changeBescon();
	
	}
}
