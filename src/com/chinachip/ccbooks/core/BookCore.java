package com.chinachip.ccbooks.core;

import java.io.Serializable;

import android.graphics.Color;
import android.graphics.Paint;

import com.ccbooks.util.StringConfig;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginTxt;
import com.chinachip.books.plugin.PluginUtil;
import com.chinachip.ccbooks.engine.Engine;
import com.chinachip.ccbooks.engine.FontUtil;

public class BookCore extends Engine  {
	private int lineCount = 10;
	private int lineSpacing = 0;
	private int pageLeftPadding = 0;
	private int pageTopPadding = 0;
	private int portraint = 0;
	private int pageWidth;
	private int pageHeight;
	public FontUtil fu = new FontUtil(22,Color.WHITE);

	public FontUtil getFu() {
		return fu;
	}
	public BookCore()
	{
		super();
	
	}
	public BookCore(int screenWidth,int screenHeight)
	{
		super();
		setPageSize(screenWidth,screenHeight);
	}
	public BookCore(int fontsize,int screenWidth,int screenHeight)
	{
		super();
		fu.setFontSize(fontsize);
		setPageSize(screenWidth,screenHeight);
		pluginInit();
	}
	
	public BookCore(int fontsize,int linespacing,int screenWidth,int screenHeight)
	{
		super();
		fu.setFontSize(fontsize);
		setLineSpacing(linespacing);
		setPageSize(screenWidth,screenHeight);
		pluginInit();
	}
	
	public int getLineSpacing()
	{
		return lineSpacing;
	}
	
	public void setLineSpacing(int linespacing)
	{
		lineSpacing = linespacing;
	}
	
	
	
	
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public int getFontSize() {
		return fu.getFontSize();
	}

	public void setFontSize(int fontsize) {
		fu.setFontSize(fontsize);
	}


	//tell engine the size of a page
	public void setPageSize(int pageWidth,int pageHeight) {
		this.pageWidth = (pageWidth/getFontSize())*getFontSize();
		this.pageHeight = (pageHeight/(getFontSize()+2))*(getFontSize()+2);
		// calc line number of one page
		lineCount = pageHeight/(getFontSize()+2);
		pageLeftPadding = (pageWidth - this.pageWidth)/2;
		pageTopPadding = (pageHeight - lineCount * getFontSize() - lineCount * 2)/2;
	}

	public void setFu(FontUtil fu) {
		this.fu = fu;
	}

	public Paint getPaint(){
		return fu.getPaint();
	}

	

	public int getPageWidth(){
		return pageWidth;
	}

	
	public int getLineCount() {
		switch (portraint) {
		case 1:
			lineCount= pageHeight/(getFontSize()+2);
			break;
		case 2:
			lineCount= ((int)(pageHeight/(getFontSize()+2)))*2;
			break;
		default:
			break;
		}
		return lineCount;
	}
	

	public String getTempPath() {
		
		String tempPath =  "/sdcard/cctemp/";
		return tempPath;
		
	}

	
	public String getTempEX() {
		String tempEX = ".cctmp";
		return tempEX;
	}


	public int getCacheCount() {
	
		return 5;
	}
	public int getPortraint() {
		return portraint;
	}

	public void setPortraint(int portraint) {
		this.portraint = portraint;
	}

	public void pluginInit()
	{
		PluginUtil.PluginInit(pm);
	}
}
