package com.ccbooks.fullscreen.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ccbooks.fullscreen.bookcore.BookCore;
import com.ccbooks.view.TextReader;
import com.chinachip.TextReader.Line;
import com.chinachip.ccbooks.engine.FontUtil;
import com.chinachip.ccbooks.engine.LineIndex;
import com.chinachip.pageeffect.ThreeDimFlipper;

public class TextThreeDim {
	public TextReader actParent;
	private ViewGroup viewParent;
	public ThreeDimFlipper pageViews;
	
	private boolean pageViews1Using;
	private TextRender pageViews2;

	public BookCore engine = null;
	private int textColor = 0xff000000;
	private Paint mPaint = null;  
	private Context mContext;  
    private int textSize;
    private int lineSpace;
	private int screenWidth;
	private int screenHeight;


    private Paint touchPaint;

    private Paint mBitmapPaint;
    private Canvas mCanvas;
	private PointF autoDragPoint = new PointF();
	private PointF autoDragStart = new PointF();
	//
    private  Rect pageRect = new Rect();
    private  float w=0;
    private  float h=0;
    
	private boolean isTurning = false;
	private int autoDragStep = 0;
	private boolean turnR = false;
	private boolean fgTouch;
	private boolean slideFg;
	private FontUtil fu = null;
	int ws = 800;
	int hs = 480;
	public BitmapMgr bitmap1 = null;
	public BitmapMgr bitmap2 = null;
	

    
	private final static int SIZE = 800*480;
	public int [] transpixel;
	
	public ArrayList<LineIndex> lineIndexs = null;
	
	private boolean mItemCanFoucs;
	
	public long initCurrIndex = 0;
	public long initEndIndex = 0;
	public long currIndex = 0;
	private ArrayList<String> currStringArray = null;
	public long markPoint = 0;
	
    private Line line;
    private PointF globalPoint = new PointF();
    private Path mPath;
//    private Paint touchPaint;
    private Bitmap paintBitmap;
//    private Paint mBitmapPaint;
//    private Canvas mCanvas;
    private long offsetY;
    
	public TextThreeDim(TextReader actObj, BookCore eng, ViewGroup viewObj, int textColor,long markPoint)  
    	{  
	    	actParent = actObj;
	    	engine = eng;
	    	viewParent = viewObj;
	    	this.markPoint = markPoint;
	    	this.textColor=textColor;
	    	initTouchPaint();
	    	initViews(markPoint);
	    	
    	}
	  public boolean initViews(long markPoint) {
		  WindowManager windowManager = actParent.getWindowManager();    
	    	Display display = windowManager.getDefaultDisplay();    	
	    	screenWidth = display.getWidth();    
	    	screenHeight = display.getHeight();
		  	bitmap1 = new BitmapMgr(screenWidth,screenHeight);
			bitmap1.isDraw = true;
			bitmap2 = new BitmapMgr(screenWidth,screenHeight);
			bitmap2.isDraw = false;
			fu = new FontUtil(engine.getFontsize());
		  	this.setPaint(TextReader.bc.getPaint());
	    	this.setTextSize(engine.getFontsize());
	    	this.setLineSpace(engine.getLineSpace());
	    	this.setTextColor(engine.getTextColor());

	 
	    
	    	pageViews = new ThreeDimFlipper(actParent,this,markPoint);
	    	pageViews.setPadding(10, 0, 10, 0);
		  	pageViews.initResource();
	    
	    	viewParent.addView(pageViews);
	    	
	    	return true;
	    } 
	  	public void reparseText() {
	    	reflashTextAttr();
	    	turnToCurPage();
	    }
	    
	    public void reloadText() {
	    	reflashTextAttr();
	    	turnToCurPage();
	    }
	    
	    public void onConfigChange() {
	    	reflashOnConfigChange();
	    	turnToCurPage();
	    }
	    
	    void reflashOnConfigChange() {
	    	this.setPaint(TextReader.bc.getPaint());
	    	this.setTextSize(engine.getFontsize());
	    	this.setLineSpace(engine.getLineSpace());
	    	this.setTextColor(engine.getTextColor());
	    	WindowManager windowManager = actParent.getWindowManager();    
	    	Display display = windowManager.getDefaultDisplay();    	
	    	screenWidth = display.getWidth();    
	    	screenHeight = display.getHeight();
	    	bitmap1.mbmpCurr.recycle();
	    	bitmap2.mbmpCurr.recycle();
	    	System.gc();
	    	bitmap1 = new BitmapMgr(screenWidth,screenHeight);
			bitmap1.isDraw = true;
			bitmap2 = new BitmapMgr(screenWidth,screenHeight);
			bitmap2.isDraw = false;
			
	    	
	    }
	    
	    void reflashTextAttr() {
	    	this.setPaint(TextReader.bc.getPaint());
	    	this.setTextSize(engine.getFontsize());
	    	this.setLineSpace(engine.getLineSpace());
	    	this.setTextColor(engine.getTextColor());
	    	
			
	    	
	    }
	    public boolean turnToCurPage() {
	    	if(addCurPageToFipper()){
	    		pageViews.invalidate();
	    		return true;
	    	} else{
	    		return false;
	    	}    	
	    }
	    public boolean addCurPageToFipper() {
	       
	        	pageViews.CurrResource();
	        	
	        	return true;
	    }
	    
	 
	    
	  
	  
	    public int getTextColor() {
			return textColor;
		}
	
		public void setTextColor(int textColor) {
			this.textColor = textColor;
		}
		public int getLineSpace() {
			return lineSpace;
		}

		public void setLineSpace(int lineSpace) {
			this.lineSpace = lineSpace;
		}   
		
	    public int getTextSize() {
			return textSize;
		}

		public void setTextSize(int textSize) {
			this.textSize = textSize;
			mPaint.setTextSize(textSize);
		}     
		public Paint getPaint() {
			return mPaint;
		}

		public void setPaint(Paint mPaint) {
			this.mPaint = mPaint;
		}	
		public ThreeDimFlipper getPageViews() {
			return pageViews;
		}
		public void setPageViews(ThreeDimFlipper pageViews) {
			this.pageViews = pageViews;
		}
		public Bitmap getCurrPage(long offset){
			ArrayList<String> contentList = null;
		    Canvas canvasTemp = null;
			try {
				contentList = engine.getCurrPage(offset);
				lineIndexs = engine.getLineIndex();
				initCurrIndex = engine.getCurrIndexAt();
				initEndIndex = engine.getNextIndexAt();
				currIndex = initCurrIndex;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(offset == 0){
				pageViews.br.setStart(true);
			
			}
			Bitmap temp = null;
			if(bitmap1.isDraw){
				temp = bitmap1.mbmpCurr;
				transpixel = new int[screenWidth*screenHeight];
				bitmap1.stringArray = contentList;
				temp.setPixels(transpixel, 0, screenWidth, 0, 0, screenWidth, screenHeight);
				bitmap1.isDraw = false;
				bitmap2.isDraw = true;
				canvasTemp = new Canvas(temp);
			}else{
				temp = bitmap2.mbmpCurr;
				bitmap2.stringArray = contentList;
				temp.setPixels(transpixel, 0, screenWidth, 0, 0, screenWidth, screenHeight);
				bitmap1.isDraw = true;
				bitmap2.isDraw = false;
				canvasTemp = new Canvas(temp);
			}
			
	         
	         
	 		int top = pageViews.getTop()+pageViews.getPaddingTop();
	 		int left = pageViews.getLeft()+pageViews.getPaddingLeft();

	       
	    
	        
	     	for(int i = 0;i<contentList.size();i++)
	     	{
	     		canvasTemp.drawText(contentList.get(i),left, top+textSize*(i+1)+lineSpace*i, mPaint);
	     	}
	     	restoreView(canvasTemp, screenHeight);
	         
	     	return temp;
		}
		
		
		public Bitmap getCurrPage(){
		    Canvas canvasTemp = null;
			ArrayList<String> contentList = null;
			 try {
				contentList = engine.getCurrPage();
				lineIndexs = engine.getLineIndex();
				initCurrIndex = engine.getCurrIndexAt();
				initEndIndex = engine.getNextIndexAt();
				currIndex = initCurrIndex;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(contentList.size() == 0){
				pageViews.br.setFinish(true);
				return bitmap1.mbmpCurr;
			}
			transpixel = new int[screenWidth*screenHeight];
			Bitmap temp = null;
			if(bitmap1.isDraw){
				temp = bitmap1.mbmpCurr;
				bitmap1.stringArray = contentList;
				temp.setPixels(transpixel, 0, screenWidth, 0, 0, screenWidth, screenHeight);
				bitmap1.isDraw = false;
				bitmap2.isDraw = true;
				canvasTemp = new Canvas(temp);
			}else{
				temp = bitmap2.mbmpCurr;
				bitmap2.stringArray = contentList;
				temp.setPixels(transpixel, 0, screenWidth, 0, 0, screenWidth, screenHeight);
				bitmap1.isDraw = true;
				bitmap2.isDraw = false;
				canvasTemp = new Canvas(temp);
			}
			
	         
	         
	 		int top = pageViews.getTop()+pageViews.getPaddingTop();
	 		int left = pageViews.getLeft()+pageViews.getPaddingLeft();

	       
	    
	        
	     	for(int i = 0;i<contentList.size();i++)
	     	{
	     		canvasTemp.drawText(contentList.get(i),left, top+textSize*(i+1)+lineSpace*i, mPaint);
	     	}
	     
	     	restoreView(canvasTemp, screenHeight);
	         
	     	return temp;
		}
		
		public Bitmap getNextPage(){
		    Canvas canvasTemp = null;
			ArrayList<String> contentList = null;
			 try {
				contentList = engine.getNextPage();
				if(contentList.size() > 0){
					lineIndexs = engine.getLineIndex();
				}	
				initCurrIndex = engine.getCurrIndexAt();
				initEndIndex = engine.getNextIndexAt();
				currIndex = initCurrIndex;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(contentList.size() == 0){
				pageViews.br.setFinish(true);
				return bitmap1.mbmpCurr;
			}
			Bitmap temp = null;
			if(bitmap1.isDraw){
				transpixel = new int[screenWidth*screenHeight];
				temp = bitmap1.mbmpCurr;
				bitmap1.stringArray = contentList;
				temp.setPixels(transpixel, 0, screenWidth, 0, 0, screenWidth, screenHeight);
				bitmap1.isDraw = false;
				bitmap2.isDraw = true;
				canvasTemp = new Canvas(temp);
			}else{
				temp = bitmap2.mbmpCurr;
				bitmap2.stringArray = contentList;
				temp.setPixels(transpixel, 0, screenWidth, 0, 0, screenWidth, screenHeight);
				bitmap1.isDraw = true;
				bitmap2.isDraw = false;
				canvasTemp = new Canvas(temp);
			}
			
	         
	         
	 		int top = pageViews.getTop()+pageViews.getPaddingTop();
	 		int left = pageViews.getLeft()+pageViews.getPaddingLeft();

	       
	    
	        
	     	for(int i = 0;i<contentList.size();i++)
	     	{
	     		canvasTemp.drawText(contentList.get(i),left, top+textSize*(i+1)+lineSpace*i, mPaint);
	     	}
	     	restoreView(canvasTemp, screenHeight);
	         
	     	return temp;
		}
		
		
		public Bitmap getPrevPage(){
		    Canvas canvasTemp = null;
			ArrayList<String> contentList = null;
			 try {
				contentList = engine.getPrevPage();
				if(contentList.size() > 0){
					lineIndexs = engine.getLineIndex();
				}	
				initCurrIndex = engine.getCurrIndexAt();
				initEndIndex = engine.getNextIndexAt();
				currIndex = initCurrIndex;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(contentList.size() == 0){
				pageViews.br.setStart(true);
				return bitmap1.mbmpCurr;
			}
			transpixel = new int[screenWidth*screenHeight];
			Bitmap temp = null;
			if(bitmap1.isDraw){
				temp = bitmap1.mbmpCurr;
				bitmap1.stringArray = contentList;
				temp.setPixels(transpixel, 0, screenWidth, 0, 0, screenWidth, screenHeight);
				bitmap1.isDraw = false;
				bitmap2.isDraw = true;
				canvasTemp = new Canvas(temp);
			}else{
				temp = bitmap2.mbmpCurr;
				bitmap2.stringArray = contentList;
				temp.setPixels(transpixel, 0, screenWidth, 0, 0, screenWidth, screenHeight);
				bitmap1.isDraw = true;
				bitmap2.isDraw = false;
				canvasTemp = new Canvas(temp);
			}
	        
	         
	 		int top = pageViews.getTop()+pageViews.getPaddingTop();
	 		int left = pageViews.getLeft()+pageViews.getPaddingLeft();

	       
	    
	        
	     	for(int i = 0;i<contentList.size();i++)
	     	{
	     		canvasTemp.drawText(contentList.get(i),left, top+textSize*(i+1)+lineSpace*i, mPaint);
	     	}
	     		
	     	restoreView(canvasTemp, screenHeight);
	     	return temp;
		}
		
		public void setFoucs(boolean fg) {
		    mItemCanFoucs = fg;
		}
		 
	    public boolean getFoucs() {
	    	return mItemCanFoucs;
	    }
		public ArrayList<String> getCurrStringArray() {
			if(bitmap1.isDraw){
				currStringArray = bitmap2.stringArray;
			}else{
				currStringArray = bitmap1.stringArray;
			}
			return currStringArray;
		}
		
		public void menuOnOff(){
			if(actParent.menuMain.isMenuShowed()){
				
				actParent.menuMain.hide();
				
			}else{
				
				actParent.menuMain.show();
				
			}
		}
		
		public void menuOff(){
			if(actParent.menuMain.isMenuShowed()){
				actParent.menuMain.hide();
			}
		}
	
		public void restoreView(Canvas canvas,int h){
	    	List<Line> lineList = actParent.getCommentView().getLineList();
	    	int systemTextSize = actParent.bc.getFontsize();
			int filenum = actParent.file;
	    	
			if(lineList == null){
				return;
			}
			if(actParent.getCommentView().getLineListFont() == null){
	    		return;
	    	}
			
			ArrayList<Line> lineListFont = (ArrayList<Line>) actParent.getCommentView().getLineListFont().get(systemTextSize+"");//获得批注信息
			if(lineListFont == null || lineListFont.size() == 0){
				return;
			}
			
			mPath = new Path();
			PointF getPoint = new PointF();
			for(int n = 0;n<lineListFont.size();n++){
				line = lineListFont.get(n);
				if(line.getFile() == filenum){
					touchPaint.setColor(line.getColor());
					touchPaint.setStrokeWidth(line.getWidth());
					if(getTextSize() == line.getFontSize()){   //字体相等才画线
	
						for(int i = 0; i < line.getCoordinateList().size();i++){
	
							int newLine = calculateLinesOffsetByIndex(line.getStartLineIndex());
	
							if(newLine < 0){
								newLine = calculateLinesOffsetByIndex(line.getEndLineIndex())+1;
								if(newLine <= 0){
									break;
								}
									newLine = newLine - actParent.bc.getLineCount();
									offsetY = (long)(newLine*(getTextSize()+getLineSpace()));
							}else{
									offsetY = (long) ((newLine)*(getTextSize()+getLineSpace()));
							}	
							if(line.getMaxY()+offsetY-line.getLinePaintOffset() > 0 && line.getMaxY()+offsetY-line.getLinePaintOffset()<h
								|| line.getMinY()+offsetY-line.getLinePaintOffset() > 0 && line.getMinY()+offsetY-line.getLinePaintOffset()<h){
								//这里开始画线
								if(i == 0){
									getPoint.x = line.getCoordinateList().get(i).x;
									getPoint.y = line.getCoordinateList().get(i).y-line.getLinePaintOffset()+offsetY;
									restore_start(getPoint.x, getPoint.y);
								}else if(i == line.getCoordinateList().size()-1){
									globalPoint.x = line.getCoordinateList().get(i).x;
									globalPoint.y = line.getCoordinateList().get(i).y-line.getLinePaintOffset()+offsetY;
									restore_up(canvas);
								}else{
									getPoint.x = line.getCoordinateList().get(i).x;
									getPoint.y = line.getCoordinateList().get(i).y-line.getLinePaintOffset()+offsetY;
									restore_move(getPoint.x, getPoint.y);
								}
							}	
						}
	
					}	
				}
			}	
			line=null;
	    }

		private void restore_start(float x, float y){
			mPath.reset();
			mPath.moveTo(x, y);
			globalPoint.x = x;
			globalPoint.y = y;
		}
		
		private void restore_up(Canvas canvas){
			mPath.lineTo(globalPoint.x, globalPoint.y);
			canvas.drawPath(mPath, touchPaint);
			mPath.reset();
		}
		
		private void restore_move(float x,float y){
			mPath.quadTo(globalPoint.x, globalPoint.y, (x+globalPoint.x)/2, (y+globalPoint.y)/2);
			globalPoint.x = x;
			globalPoint.y = y;
		}
		
	    public int calculateLinesOffsetByIndex(LineIndex lineIndex_old) {
//	    	if(readMode == 1){
//		    	if(ArrLong.size()<bc.getLineCount()+1){
//		    		for(int i=ArrLong.size();i<bc.getLineCount()+1;i++){
//		    			LineIndex tempLine = new LineIndex();
//		    			ArrLong.add(tempLine);
//		    		}
//		    	}
//	    	}	
//	   	
////			printArrLong(ArrLong);
			
	    	for(int i = 0;i<lineIndexs.size();i++) {
	    		if(lineIndexs.get(i).compareTo(lineIndex_old))
	    			return i;
	    	}
	    	return -1;
	    }
	    
		private void initTouchPaint() {
			touchPaint = new Paint();
			touchPaint.setAntiAlias(true);
			touchPaint.setDither(true);
			touchPaint.setColor(Color.RED);
			touchPaint.setStyle(Paint.Style.STROKE);
			touchPaint.setStrokeCap(Paint.Cap.ROUND);
			touchPaint.setStrokeJoin(Paint.Join.ROUND);
			touchPaint.setStrokeWidth(Line.INITWITDH);
			
		}
		
}
