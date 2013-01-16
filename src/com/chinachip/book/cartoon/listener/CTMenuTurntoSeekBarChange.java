package com.chinachip.book.cartoon.listener;

import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ccbooks.view.CartoonReader;
import com.chinachip.TextReader.MenuTurnto;
import com.chinachip.book.cartoon.CTMenuTurnto;

public class CTMenuTurntoSeekBarChange implements OnSeekBarChangeListener{
	static final String DEBUG_TAG = "jf";
	static CTMenuTurnto funObj;
	static CartoonReader tf;
	
	public CTMenuTurntoSeekBarChange(CTMenuTurnto mfObj,CartoonReader tf){
		funObj = mfObj;
		this.tf = tf;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		funObj.file = tf.file;
		Log.d(DEBUG_TAG, "---------------------MenuFontSeekBarChange onProgressChanged-------------------------");
		if(funObj.menuParent.menuTurnto.isShowed()){
			funObj.progressChangeEvent(progress);
		}
	
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}
