package com.ccbooks.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TvMainContentTouch implements OnTouchListener {
	
	public boolean onTouch(View v, MotionEvent event) {
		float statrX = 0.0F, startY = 0.0F, endX = 0.0F, endY = 0.0F;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 按下的处理
			
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// 放开时的的处理
			
		}
		//return super.onTouch(v, event);//再送回去让系统自己处理
		return false;
	}
}