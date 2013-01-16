package com.ccbooks.fullscreen.listener;

import com.chinachip.TextReader.MenuFun;
import com.chinachip.TextReader.TextMenu;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuBkmkClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	static TextMenu menu;
	static MenuFun funObj;
	
	public MenuBkmkClick(TextMenu tmObj, MenuFun llObj){
		menu = tmObj;
		funObj = llObj;
	}	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "---------------------MenuBkmkClick onClick-------------------------");
		if(funObj.isShowed()){
			funObj.hide();
		} else{
			menu.setAllLevel2Gone();
			funObj.show();
		}
	}
}
