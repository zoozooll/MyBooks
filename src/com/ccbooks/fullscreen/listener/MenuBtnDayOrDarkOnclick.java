package com.ccbooks.fullscreen.listener;

import com.ccbooks.view.TextReader;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuBtnDayOrDarkOnclick implements OnClickListener {

	TextReader tr;
	
	public MenuBtnDayOrDarkOnclick(TextReader tr) {
		super();
		this.tr = tr;
	}

	@Override
	public void onClick(View v) {
		Button b = (Button) v;
		if (tr.getDayOrNight() == TextReader.DAY_LIGHT) {
			tr.setDayOrNight(TextReader.DARK_LIGHT);
			tr.setDayOrDark(TextReader.DARK_LIGHT);
			b.setText("白天");
			
		} else if (tr.getDayOrNight() == TextReader.DARK_LIGHT) {
			tr.setDayOrNight(TextReader.DAY_LIGHT);
			tr.setDayOrDark(TextReader.DAY_LIGHT);
			b.setText("黑夜");
		}
		tr.bc.setTextColor(tr.getFontColor());
    	tr.reloadText();
	}

}
