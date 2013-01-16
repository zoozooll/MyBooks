package com.ccbooks.fullscreen.listener;

import com.chinachip.TextReader.MenuTurnto;

import android.view.View;
import android.view.View.OnClickListener;

public class MenuTurntoBottunOKClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	static MenuTurnto funObj;
	
	public MenuTurntoBottunOKClick(MenuTurnto mfObj){
		funObj = mfObj;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		funObj.okEvent();
	}

}
