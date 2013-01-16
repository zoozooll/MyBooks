package com.ccbooks.fullscreen.listener;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinachip.TextReader.MenuFun;
import com.chinachip.TextReader.TextMenu;

public class MenuThemeClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	private TextMenu menu;
	private MenuFun funObj;
	
	public MenuThemeClick(TextMenu tmObj, MenuFun llObj){
		menu = tmObj;
		funObj = llObj;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "---------------------MenuFontClick onClick-------------------------");
		if(funObj.isShowed()){
			funObj.hide();
		} else{
			menu.setAllLevel2Gone();
			funObj.show();
		}
	}
}
