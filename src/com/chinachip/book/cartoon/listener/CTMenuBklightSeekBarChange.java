package com.chinachip.book.cartoon.listener;


import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


import com.chinachip.book.cartoon.CTMenuBklight;

public class CTMenuBklightSeekBarChange implements OnSeekBarChangeListener{

	static CTMenuBklight funObj;
	
	public CTMenuBklightSeekBarChange(CTMenuBklight mfObj){
		funObj = mfObj;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
	
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
