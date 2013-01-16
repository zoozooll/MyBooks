package com.chinachip.TextReader;

import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MenuBookList extends MenuFun{
	private TextMenu menuParent;
	private LinearLayout layoutFun;
	
	public MenuBookList(TextMenu tmObj){
		menuParent = tmObj;
	}
	
	@Override
	public LinearLayout getLayoutFont() {
		return null;
	}

	@Override
	public boolean hide() {
		return false;
	}

	@Override
	public boolean isShowed() {
		return false;
	}

	@Override
	public boolean show() {
		turntoBookList();
		return false;
	}
	
	private void turntoBookList() {
		menuParent.getActParent().finish();
	}
}
