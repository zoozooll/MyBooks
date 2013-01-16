package com.chinachip.TextReader;

import com.ccbooks.view.BookContentView;
import com.ccbooks.view.TextReader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MenuPageMode extends MenuFun{

	private TextMenu menuParent;
	private LinearLayout layoutFun;
	
	public MenuPageMode(TextMenu tmObj) {
		menuParent = tmObj;
	}
	
	@Override
	public LinearLayout getLayoutFont() {
		return null;
	}

	@Override
	public boolean hide() {
		return true;
	}

	@Override
	public boolean isShowed() {
		return false;
	}

	@Override
	public boolean show() {
		menuParent.getActParent().turnToPageMode();
		return true;
	}
}
