package com.ccbooks.fullscreen.listener;

import com.chinachip.TextReader.MenuFun;
import com.chinachip.TextReader.TextMenu;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuFontClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	private TextMenu menu;
	private MenuFun funObj;
	
	public MenuFontClick(TextMenu tmObj, MenuFun llObj){
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
