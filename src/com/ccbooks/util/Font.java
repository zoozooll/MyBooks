package com.ccbooks.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.graphics.Paint;
import android.graphics.Typeface;





public class Font {

	public Paint mPaint;
	
	public String ttfPath;
	
	public Typeface typeface;
	
	public String sysPath = "/system/fonts/";
	
	public String userPath = "/local/cctemp/fonts/";
	
	ArrayList<FontListItem> fontListItem = null;
	
	
	public ArrayList<FontListItem> getFontListItem() {
		return fontListItem;
	}

	public Font(int fontSiz,String ttfFileName) {
		mPaint = new Paint();
		mPaint.setTextSize(fontSiz);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(5);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		initFont();
		for(int i = 0; i < fontListItem.size();i++){
			if(fontListItem.get(i).getTtfFileName().equals(ttfFileName)){
				String ttfFilePath = fontListItem.get(i).getTtfFilePath();
				typeface = Typeface.createFromFile(ttfFilePath);
				mPaint.setTypeface(typeface);
			}
		}
		
		
	}
	
	public void setFontSize(int fontSize){
		mPaint.setTextSize(fontSize);
	}
	
	public Paint getPaint(){
		return mPaint;
	}
	
	public void initFont(){
		searchSysFontList();
		searchUserFontList();
	}
	
	public void setFont(String ttfFileName){
		for(int i = 0; i < fontListItem.size();i++){
			if(fontListItem.get(i).getTtfFileName().equals(ttfFileName)){
				String ttfFilePath = fontListItem.get(i).getTtfFilePath();
				typeface = Typeface.createFromFile(ttfFilePath);
				mPaint.setTypeface(typeface);
			}
		}
		
	}

	public ArrayList<FontListItem> searchSysFontList(){
		fontListItem = new ArrayList<FontListItem>();
		FontListItem fontItem = null;
		File  sysFont = new File(sysPath);
		Map<String,String> sysFontMap = initSysFont();
		File[] listFile = sysFont.listFiles();
		for(int i = 0; i < listFile.length; i++){
			fontItem = new FontListItem();
			String filePath = listFile[i].getPath();
			String ttfName = filePath.substring(filePath.lastIndexOf("/")+1, filePath.lastIndexOf(".ttf"));
			String rettfName = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
			if(sysFontMap.containsKey(ttfName)){
				
				fontItem.setTtfFileName(sysFontMap.get(ttfName));
				fontItem.setTtfFilePath(filePath);
				fontListItem.add(fontItem);
			}else if(rettfName.lastIndexOf("ttf") > 0){
				ttfName = rettfName.substring(0, rettfName.lastIndexOf("ttf")-1);
				fontItem.setTtfFileName(ttfName);
				fontItem.setTtfFilePath(filePath);
				fontListItem.add(fontItem);
			}else if(rettfName.lastIndexOf("TTF") > 0){
				ttfName = rettfName.substring(0, rettfName.lastIndexOf("TTF")-1);
				fontItem.setTtfFileName(ttfName);
				fontItem.setTtfFilePath(filePath);
				fontListItem.add(fontItem);
			}
		}
	
		return fontListItem;
	}
	
	public ArrayList<FontListItem> searchUserFontList(){
		
		FontListItem fontItem = null;
		File  sysFont = new File(userPath);
		if(!sysFont.exists()){
		
				sysFont.mkdir();
			
			return fontListItem;
		}else{
			File[] listFile = sysFont.listFiles();
			if(listFile.length == 0){
				return fontListItem;
			}
			for(int i = 0; i < listFile.length; i++){
				fontItem = new FontListItem();
				String filePath = listFile[i].getPath();
				String ttfName = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
				if((ttfName.lastIndexOf("ttf") > 0)){
					ttfName = ttfName.substring(0, ttfName.lastIndexOf("ttf")-1);
					fontItem.setTtfFileName(ttfName);
					fontItem.setTtfFilePath(filePath);
					fontListItem.add(fontItem);
				}else if(ttfName.lastIndexOf("TTF") > 0){
					ttfName = ttfName.substring(0, ttfName.lastIndexOf("TTF")-1);
					fontItem.setTtfFileName(ttfName);
					fontItem.setTtfFilePath(filePath);
					fontListItem.add(fontItem);
				}
			}
		}
		
	
		return fontListItem;
	}
	
	private Map<String, String> initSysFont(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("DroidSans-Bold", "粗体无衬线（拉丁字母等）");
		map.put("DroidSans", "常规无衬线（拉丁字母等）");
		map.put("DroidSansFallback", "常规无衬线（中文字符等）");
		map.put("DroidSansMono", "等宽无衬线（拉丁字母等）");
		map.put("DroidSerif-Bold", "粗体衬线（拉丁字母等）");
		map.put("DroidSerif-BoldItalic", "粗体+斜体衬线（拉丁字母等）");
		map.put("DroidSerif-Italic", "斜体衬线（拉丁字母等）");
		map.put("DroidSerif-Regular", "常规衬线（拉丁字母等）");

		return map;
	} 

	
	
}

