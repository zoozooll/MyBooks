package com.ccbooks.dialog;

import com.ccbooks.view.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class LightBarDialog extends Dialog {

	private Activity activity;

	public LightBarDialog(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.book_content1_light);
		SeekBar btnLightBar = (SeekBar) findViewById(R.id.btnLightBar);
		btnLightBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				float a = (float) progress / 255.0f;
				if (a >= 0.2f) {
					brightnessMax(a);
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

		});
	}

	/** 调节亮度 */
	private void brightnessMax(float ness) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = ness;
		activity.getWindow().setAttributes(lp);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		LinearLayout bookContentLightDia = (LinearLayout) findViewById(R.id.bookContentLightDia);
		if (event.getX() < 0 || event.getY() < 0
				|| event.getX() > bookContentLightDia.getWidth()
				|| event.getY() > bookContentLightDia.getHeight()) {
			this.cancel();
		}
		return super.onTouchEvent(event);
	}

}
