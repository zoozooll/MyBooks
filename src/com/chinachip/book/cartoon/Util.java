package com.chinachip.book.cartoon;

import android.R;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class Util {
	public static void setFullNoTitleScreen(Activity context){
		context.setTheme(R.style.Theme_Black_NoTitleBar_Fullscreen);
		context.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
