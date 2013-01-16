package com.ccbooks.dialog;

import com.ccbooks.view.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FontDialog extends Dialog {

	TextView tvContentView;
	Button btnFonta, btnFontA;

	public FontDialog(Context context, TextView tvContentView) {
		super(context);
		// TODO Auto-generated constructor stub
		this.tvContentView = tvContentView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.book_conetent1_fontctrl);
		btnFonta = (Button) findViewById(R.id.btnFonta);
		btnFontA = (Button) findViewById(R.id.btnFontA);
		btnFonta.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				float size = tvContentView.getTextSize();
				if (size > 12) {
					size -= 2.0f;
					tvContentView.setTextSize(size);
				}
			}

		});

		btnFontA.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				float size = tvContentView.getTextSize();
				if (size < 40.0f) {
					size += 12.0f;
					tvContentView.setTextSize(size);
				}
			}

		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		LinearLayout bookContentFontDia = (LinearLayout) findViewById(R.id.bookContentFontDia);
		if (event.getX() < 0 || event.getY() < 0
				|| event.getX() > bookContentFontDia.getWidth()
				|| event.getY() > bookContentFontDia.getHeight()) {
			this.cancel();
		}
		return super.onTouchEvent(event);
	}
}
