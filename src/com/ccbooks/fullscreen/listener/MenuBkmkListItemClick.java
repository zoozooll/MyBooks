package com.ccbooks.fullscreen.listener;

import com.chinachip.TextReader.MenuBkmk;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuBkmkListItemClick implements OnItemClickListener{
	static final String DEBUG_TAG = "jf";
	private MenuBkmk funObj;
	
	public MenuBkmkListItemClick(MenuBkmk mfObj){
		funObj = mfObj;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.i(DEBUG_TAG, "---------------------MenuBkmkListItemClick onItemClick-------------------------"+String.valueOf(arg3));
		funObj.bkmkListItemClickEvent(arg2);
	}

}
