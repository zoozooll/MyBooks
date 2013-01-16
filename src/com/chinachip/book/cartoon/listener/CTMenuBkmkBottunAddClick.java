package com.chinachip.book.cartoon.listener;

import com.chinachip.book.cartoon.CTMenuBkmk;

import android.view.View;
import android.view.View.OnClickListener;




public class CTMenuBkmkBottunAddClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	static CTMenuBkmk funObj;
	
	public CTMenuBkmkBottunAddClick(CTMenuBkmk mfObj){
		funObj = mfObj;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		funObj.bkmkAddEvent();
	}
}
