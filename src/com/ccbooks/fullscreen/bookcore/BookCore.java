package com.ccbooks.fullscreen.bookcore;


import android.graphics.Paint;

import com.chinachip.books.plugin.PluginUtil;
import com.chinachip.ccbooks.engine.Engine;

public class BookCore extends Engine {
	private Paint mPaint;
	private int pageWidth = 1600;
	private int pageHeight = 480;
	private int fontSize = 18;
	private int lineSpace = 0;
	private int textColor;
	
	public BookCore() {
		mPaint = new Paint();
		textColor = 0xff565656;
		mPaint.setColor(textColor);
	    mPaint.setAntiAlias(true);
	    mPaint.setStrokeWidth(5);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    fontSize = 18;
	    mPaint.setTextSize(fontSize);
	}
	
	public int getLineCount() {
    	int lineCount;
    	if(fontSize+lineSpace == 0)
    		lineCount = 18;
    	else
    		lineCount = pageHeight/(fontSize+lineSpace);
		return lineCount;
	}

	public int getPageWidth() {
		return pageWidth; 
	}

	public void setPageWidth(int pageWidth) {
		this.pageWidth = pageWidth;
	}
	
	public int getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(int pageHeight) {
		this.pageHeight = pageHeight;
	}

	public int getFontsize() {
		return fontSize;
	}

	public void setFontsize(int fontsize) {
		this.fontSize = fontsize;
		mPaint.setTextSize(this.fontSize);
	}
	
	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
		mPaint.setColor(textColor);
	}
	
	public int getLineSpace() {
		return lineSpace;
	}

	public void setLineSpace(int lineSpace) {
		this.lineSpace = lineSpace;
	}	
	
	public  int getCharWidth(String str){
		MeasureText mt = new MeasureText(fontSize);
		return (int) mt.getCharWidth(str);
	}

	public String getTempPath() {
		String tempPath =  "/local/cctemp/";
		return tempPath;
	}

	public String getTempEX() {
		String tempEX = ".cctmp";
		return tempEX;
	}

	public int getCacheCount() {
		return 5;
	}

	public void setPaint(Paint pt) {
		// TODO Auto-generated method stub
		mPaint = pt;
	}	
	
	@Override
	public Paint getPaint() {
		// TODO Auto-generated method stub
		return mPaint;
	}	

	@Override
	public void pluginInit() {
		// TODO Auto-generated method stub
		PluginUtil.PluginInit(pm);
	}	
}
