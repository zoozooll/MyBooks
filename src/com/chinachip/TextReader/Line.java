package com.chinachip.TextReader;

import java.util.ArrayList;
import java.util.List;

import com.chinachip.ccbooks.engine.LineIndex;

import android.graphics.Color;
import android.graphics.PointF;

public class Line {
	public static final int INITWITDH = 3;//画笔默认宽度
	private int color = Color.RED;
	private int width = INITWITDH;
	private float maxY = Float.MIN_VALUE;
	private float minY = Float.MAX_VALUE;
	private float linePaintOffset;
	private List<PointF> coordinateList = new ArrayList<PointF>();//存储每条连续线的坐标
	private int fontSize;
	private int file;

	private LineIndex startLineIndex = new LineIndex();
	private LineIndex endLineIndex = new LineIndex();
	

	
	public Line() {
		this(Color.RED);
		// TODO Auto-generated constructor stub
	}
	
	public Line(int color){
		this(color,INITWITDH);
	}
	
	public Line(int color,int width){
		this.color = color;
		this.width = width;
	}
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public float getMaxY() {
		return maxY;
	}
	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}
	public float getMinY() {
		return minY;
	}
	public void setMinY(float minY) {
		this.minY = minY;
	}
	public List<PointF> getCoordinateList() {
		return coordinateList;
	}
	public void setCoordinateList(List<PointF> coordinateList) {
		this.coordinateList = coordinateList;
	}
	public static int getInitwitdh() {
		return INITWITDH;
	}
	
	
	
	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public LineIndex getStartLineIndex() {
		return startLineIndex;
	}

	public void setStartLineIndex(LineIndex startLineIndex) {
		this.startLineIndex = startLineIndex;
	}

	public void setStartLineIndex_start(long startLineIndex_start) {
		this.startLineIndex.setStart(startLineIndex_start);
	}
	
	public void setStartLineIndex_top(int startLineIndex_top) {
		this.startLineIndex.setTopIndex(startLineIndex_top);
	}
	
	public void setEndLineIndex_start(long endLineIndex_start) {
		this.endLineIndex.setStart(endLineIndex_start);
	}
	
	public void setEndLineIndex_top(int endLineIndex_top) {
		this.endLineIndex.setTopIndex(endLineIndex_top);
	}
	
	public LineIndex getEndLineIndex() {
		return endLineIndex;
	}

	public void setEndLineIndex(LineIndex endLineIndex) {
		this.endLineIndex = endLineIndex;
	}

	public float getLinePaintOffset() {
		return linePaintOffset;
	}

	public void setLinePaintOffset(float linePaintOffset) {
		this.linePaintOffset = linePaintOffset;
	}

	public int getFile(){
		return file;
	}
	
	public void setFile(int file){
		this.file = file;
	}
	
	
	@Override
	public String toString() {
		return super.toString()+"Line [color=" + color + ", width=" + width + ", maxY=" + maxY
				+ ", minY=" + minY + "]";
	}
	
	
}
