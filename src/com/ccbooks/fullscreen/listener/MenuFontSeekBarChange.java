package com.ccbooks.fullscreen.listener;

import com.chinachip.TextReader.MenuSetting;

import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MenuFontSeekBarChange implements OnSeekBarChangeListener{
	static final String DEBUG_TAG = "jf";
	static MenuSetting funObj;
	
	public MenuFontSeekBarChange(MenuSetting mfObj){
		funObj = mfObj;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "---------------------MenuFontSeekBarChange onProgressChanged-------------------------");
		funObj.fontProgressChangeEvent(progress);
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
