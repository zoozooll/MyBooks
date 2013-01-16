package com.ccbooks.fullscreen.listener;

import com.chinachip.TextReader.MenuFun;
import com.chinachip.TextReader.MenuSetting;
import com.chinachip.TextReader.TextMenu;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuFontTTFClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	private TextMenu menu;
	private MenuSetting funObj;
	
	public MenuFontTTFClick(TextMenu tmObj, MenuSetting llObj){
		menu = tmObj;
		funObj = llObj;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "---------------------MenuFontClick onClick-------------------------");
		
			funObj.ttfShow();
	
	}

}
