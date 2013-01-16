package com.ccbooks.dialog;



import com.ccbooks.view.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.LinearLayout;

public class CharDialog extends Dialog {

	private Activity activity;

	public CharDialog(Activity activity,LinearLayout liner) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(liner);
	
	}
		

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		LinearLayout bookContentLightDia = (LinearLayout) findViewById(R.id.chardialog);
		if (event.getX() < 0 || event.getY() < 0
				|| event.getX() > bookContentLightDia.getWidth()
				|| event.getY() > bookContentLightDia.getHeight()) {
			this.cancel();
		}
		return super.onTouchEvent(event);
	}

}
