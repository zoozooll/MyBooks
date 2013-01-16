package com.chinachip.TextReader;

import java.sql.Date;

public class BookMarkInfo {
	private int percent;
	private long postion;
	private int curLine;
	private float offset;
	private String textTips;
	private Date date;
	
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	public long getPostion() {
		return postion;
	}
	public void setPostion(long postion) {
		this.postion = postion;
	}
	
	public int getCurLine() {
		return curLine;
	}
	public void setCurLine(int curLine) {
		this.curLine = curLine;
	}	
	
	public float getOffset() {
		return offset;
	}
	public void setOffset(float offset) {
		this.offset = offset;
	}	
	
	public String getTextTips() {
		return textTips;
	}
	public void setTextTips(String textTips) {
		this.textTips = textTips;
	}	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}	
}