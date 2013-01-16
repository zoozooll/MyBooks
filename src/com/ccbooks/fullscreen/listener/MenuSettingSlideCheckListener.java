package com.ccbooks.fullscreen.listener;

import com.chinachip.TextReader.MenuSetting;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MenuSettingSlideCheckListener implements OnCheckedChangeListener{
	static final String DEBUG_TAG = "jf";
	static MenuSetting funObj;
	
	public MenuSettingSlideCheckListener(MenuSetting msObj) {
		funObj = msObj;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked)
			Log.d(DEBUG_TAG, "---------------------MenuTurntoClick onClick-----------111--------------");
		else
			Log.d(DEBUG_TAG, "---------------------MenuTurntoClick onClick----------222---------------");
		funObj.slideModeChangeEvent();
	}
}
