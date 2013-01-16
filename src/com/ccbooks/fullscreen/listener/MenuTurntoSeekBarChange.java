package com.ccbooks.fullscreen.listener;

import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.chinachip.TextReader.MenuTurnto;

public class MenuTurntoSeekBarChange implements OnSeekBarChangeListener{
	static final String DEBUG_TAG = "jf";
	static MenuTurnto funObj;
	
	public MenuTurntoSeekBarChange(MenuTurnto mfObj){
		funObj = mfObj;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "---------------------MenuFontSeekBarChange onProgressChanged-------------------------");
		funObj.progressChangeEvent(progress);
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
