package com.chinachip.book.cartoon.listener;

import com.chinachip.TextReader.MenuTurnto;
import com.chinachip.book.cartoon.CTMenuTurnto;

import android.view.View;
import android.view.View.OnClickListener;

public class CTMenuTurntoBottunCancleClick implements OnClickListener{
	static final String DEBUG_TAG = "jf";
	static CTMenuTurnto funObj;
	
	public CTMenuTurntoBottunCancleClick(CTMenuTurnto mfObj){
		funObj = mfObj;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		funObj.cancleEvent();
	}

}
