package com.chinachip.book.cartoon.listener;

import com.chinachip.TextReader.MenuBkmk;
import com.chinachip.book.cartoon.CTMenuBkmk;

import android.view.View;
import android.view.View.OnClickListener;


public class CTMenuBkmkBottunListClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	static CTMenuBkmk funObj;
	
	public CTMenuBkmkBottunListClick(CTMenuBkmk ctMenuBkmk){
		funObj = ctMenuBkmk;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		funObj.bkmkListEvent();
	}
}
