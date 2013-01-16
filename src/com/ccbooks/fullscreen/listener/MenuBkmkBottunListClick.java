package com.ccbooks.fullscreen.listener;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.chinachip.TextReader.MenuBkmk;

public class MenuBkmkBottunListClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	static MenuBkmk funObj;
	
	public MenuBkmkBottunListClick(MenuBkmk mfObj){
		funObj = mfObj;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		funObj.bkmkListEvent();
	}
}
