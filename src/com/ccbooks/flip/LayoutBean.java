package com.ccbooks.flip;

import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

public class LayoutBean {

	public static final LayoutParams FF = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
	
	public static final LayoutParams FW = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
	
	public static final LayoutParams WW = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	
	public static LayoutParams  HorizontalParmas ;
	
	static {
		MarginLayoutParams mlp = new MarginLayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		mlp.setMargins(50, 20, 25, 20);
		HorizontalParmas = new LayoutParams(mlp);
	}
}
