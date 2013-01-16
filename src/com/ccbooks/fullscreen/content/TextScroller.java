package com.ccbooks.fullscreen.content;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.ccbooks.fullscreen.bookcore.BookCore;
import com.ccbooks.fullscreen.content.TextControler.TextCache;
import com.ccbooks.view.TextReader;
import com.chinachip.TextReader.Line;
import com.chinachip.ccbooks.engine.LineIndex;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

public class TextScroller extends View{  
	static final String DEBUG_TAG = "jf";
    private Paint mPaint = null;  
    private int textSize;
    private int lineSpace;
	private int textColor;
	private TextControler textCtrler;
	private ArrayList<String> curTextArray;
	private ArrayList<LineIndex> curLineIndexArray;
	private MoveControler moveCtrler;
    private float yTouchDown;
    private float yTouchMove;
    private float yPrintStart;
    private BookCore engine;
    private TextReader actParent;
    private boolean fgTouch;
    private boolean slideFg;
    private Timer myTimer;
    private MoveTimer myTimerTask;
    private Handler mHandler;
    private Scroller mScroll;
    private VelocityTracker mVelocityTracker;
    private float mLastMotionY;
    private boolean mItemCanFoucs;
    private boolean mMoveEnable;
    private int moveCount;
    private boolean isScroll = true;
    
    private Line line;
    private PointF globalPoint = new PointF();
    private Path mPath;
    private Paint touchPaint;

	
//	public List<Line> lineList;
    
    public TextScroller(Context context) {  
        super(context);  
        actParent = (TextReader)context;
        engine = actParent.getEngine();
        intiPaint();
		initTouchPaint();
    }
    
    public TextScroller(Context context,AttributeSet attr) {  
        super(context,attr);
        actParent = (TextReader)context;
        engine = actParent.getEngine();
        intiPaint();
		initTouchPaint();
    }
    
    public void intiPaint() {
		mPaint = new Paint();
		textColor = 0xff00ff00;
		reflashTextAttr();
		mPaint.setColor(textColor);
	    mPaint.setAntiAlias(true);
	    mPaint.setStrokeWidth(5);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setTextSize(textSize);
	    yPrintStart = 0;
	    textCtrler = new TextControler(engine);
	    initMoveCtrler();
	    mItemCanFoucs = true;
	    mMoveEnable = true;
    }

    
    public void initMoveCtrler() {
    	moveCtrler = new MoveControler(0,0.004);
    	mScroll = new Scroller(this.getContext());
    	mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(!fgTouch && moveCtrler.getCurrentVelocity() != 0) {
					final int y = (int)moveCtrler.getPosIn(System.currentTimeMillis(), false);
					final int deltaY = (int) (mLastMotionY - y);
	            	mLastMotionY = y;
	            	Log.d(DEBUG_TAG, "--handleMessage----deltaY-------------"+deltaY);
	            	scrollBy(0, deltaY);
					TextScroller.this.postInvalidate();
					sendEmptyMessageDelayed(1, 10);
				}
			}
    	};
    }
    
    void reflashTextAttr() {
        textSize = engine.getFontsize();
        mPaint = TextReader.bc.getPaint();
        mPaint.setTextSize(textSize);
        textColor = engine.getTextColor();
        mPaint.setColor(textColor);
        lineSpace = engine.getLineSpace();    	
    }

    
    public void setFoucs(boolean fg) {
    	mItemCanFoucs = fg;
    }
 
    public boolean getFoucs() {
    	return mItemCanFoucs;
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
	
    public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
		mPaint.setTextSize(textSize);
	}
	
	public void setTouchFlag(boolean fg) {
		fgTouch = fg;
	}
	
	public TextControler getControler() {
		return textCtrler;
	}	
    // ��ȡҪ��ʾ���ַ�
    public ArrayList<String> getCurTextArray(){
    	return curTextArray;
    }
    
    public ArrayList<LineIndex> getCurLinesIndex(){
    	return curLineIndexArray;
    }
    
    
    public long getCurPos() {
    	return textCtrler.getCurPos();
    }

    public float getCurOffset() {
		return yPrintStart;
    }
 
    
    public float getyPrintStart() {
		return yPrintStart;
	}

	public void setyPrintStart(float yPrintStart) {
		this.yPrintStart = yPrintStart;
	}

    public void turntoCurPage(){
    	try {
			textCtrler.reloadCache();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.invalidate();
    }
 
    public void turntoCurPage(long pos){
    	try {
			textCtrler.reloadCache(pos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.invalidate();
    }
    
    public void setIsScroll(boolean isScroll){
    	this.isScroll = isScroll;
    	invalidate();
    }
    
    public boolean getIsScroll(){
    	return isScroll;
    }
    
    public void reparseText() {
        setyPrintStart(0);
        textCtrler.setCurLine(0);
        long ppos = 0;
        reflashTextAttr();
		try {
			ppos = engine.getCurrIndexAt();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        turntoCurPage(ppos);
    }
    
    public void reloadText() {
    	reflashTextAttr();
    	turntoCurPage();
    }
    
    @Override  
    protected void onDraw(Canvas canvas) {  
        int w = this.getWidth();
       	int h = this.getHeight();
        renderBefore(canvas,w,h);
        renderExec(canvas,w,h);  
        renderAfter(canvas,w,h);
        
//        Log.i("TextScroller", "w="+w+",h="+h);
        super.onDraw(canvas); 
    }  

    protected void renderExec(Canvas canvas,int w,int h) {
		curTextArray = textCtrler.getCurTextArray();
		curLineIndexArray = textCtrler.getCurLinesIndex();
    	if(curTextArray == null)
    		return;
		int top = this.getTop()+this.getPaddingTop();
		int left = this.getLeft()+this.getPaddingLeft();
		for(int i = 0;i<curTextArray.size();i++) {
    		canvas.drawText(curTextArray.get(i),left,
    				yPrintStart+top+(textSize+lineSpace)*i+textSize, mPaint);
    	}
		isScroll = true;
    }
    
    // ��Ⱦ����ǰ����
    public void renderBefore(Canvas canvas,int w,int h){
    	//δʵ�ֹ���
    }
    
    // ��Ⱦ���ֺ���
    public void renderAfter(Canvas canvas,int w,int h){  	
//    	ArrayList<TextCache> tc = textCtrler.getCacheList();
//    	LineIndex li = tc.get(1).topLineIndex;
//    	Log.i("garmen", li.getStart() +" RenderAfter "+li.getTopIndex() );
//    	textCtrler.getCurLinesIndex();
    	if(isScroll && !actParent.getIsComment()){
    		isScroll = false;
    		restoreView(canvas,h);
    		
    	}	

    		
    }

    /**
     * 把批注画到屏幕上，批注内容由commentView提供
     * @param canvas 要画的桌布
     * @param h 屏幕的高度
     * @author lamkaman
     * Created on 2011-5-17  上午09:59:12
     */
    public void restoreView(Canvas canvas,int h){
		PointF getPoint = new PointF();
		int systemTextSize = actParent.getTextSize();
		int filenum = actParent.file;
		
    	if(actParent.getCommentView().getLineListFont() == null){
    		return;
    	}
    	ArrayList<Line> lineList = (ArrayList<Line>) actParent.getCommentView().getLineListFont().get(systemTextSize+"");//获得批注信息
		if(lineList == null || lineList.size() == 0){
			return;
		}

		for(int n = 0;n<lineList.size();n++){//这里可以优化
			line = lineList.get(n);
			if(line.getFile() == filenum){
				touchPaint.setColor(line.getColor());
				touchPaint.setStrokeWidth(line.getWidth());
				int newLine = actParent.calculateLinesOffsetByIndex(line.getStartLineIndex());
			    long offsetY;
				if(newLine < 0){
					newLine = actParent.calculateLinesOffsetByIndex(line.getEndLineIndex())+1;
					if(newLine <= 0){
						continue;
					}
						newLine = newLine - actParent.bc.getLineCount();
						offsetY = (long)(newLine*
							(getTextSize()+getLineSpace())+actParent.getLinePrintOffset());
				}else{
						offsetY = (long) ((newLine)*
							(getTextSize()+getLineSpace())+actParent.getLinePrintOffset());
				}	
				if(line.getMaxY()+offsetY-line.getLinePaintOffset() > 0 && line.getMaxY()+offsetY-line.getLinePaintOffset()<h
						|| line.getMinY()+offsetY-line.getLinePaintOffset() > 0 && line.getMinY()+offsetY-line.getLinePaintOffset()<h){
					for(int i = 0; i < line.getCoordinateList().size();i++){//这里开始画线
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
		line=null;
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
		mPath = new Path();
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
    
    
    public void postionCheck() throws IOException{
    	Log.d(DEBUG_TAG, "yPrintStart="+String.valueOf(yPrintStart));
    	if(yPrintStart <= 0-(textSize+lineSpace)) {
    		if(true == textCtrler.nextLineRequest()) {
    			yPrintStart = 0;
    			mMoveEnable = true;
    		}
    		else {
    			yPrintStart = 0 - (textSize+lineSpace);
    			if(mMoveEnable) {
    				Toast lastToast = null;
    				if(actParent.file < actParent.endFile) {
    					lastToast = Toast.makeText(actParent, "本节完!", Toast.LENGTH_SHORT);
    				} else {
    					lastToast = Toast.makeText(actParent, "全书完!", Toast.LENGTH_SHORT);
    				}
    				lastToast.show();
    				mMoveEnable = false;
    			}
    		}	
		}
		else if(yPrintStart > 0) {
			if(true == textCtrler.preLineRequest()) {
				yPrintStart = -(textSize+lineSpace);
				mMoveEnable = true;
			}
			else {
				yPrintStart = 0;
				if(mMoveEnable) {
					Toast lastToast = Toast.makeText(actParent, "已到最前页!", Toast.LENGTH_SHORT);
					lastToast.show();
					mMoveEnable = false;
				}
			}
		}

    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mItemCanFoucs == false)return false;
		if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            // Don't handle edge touches immediately -- they may actually belong to one of our
            // descendants.
            return false;
        }
		if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        
        float y = event.getY();
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if (!mScroll.isFinished()) {
				mScroll.abortAnimation();
            }
			fgTouch = true;
			moveCount = 0;
			mLastMotionY = y;
			moveCtrler.resetMove();
			moveCtrler.addNewMovePoint(System.currentTimeMillis(), mLastMotionY);
		} else if(event.getAction() == MotionEvent.ACTION_MOVE){
			final int deltaY = (int) (mLastMotionY - y);
			fgTouch = true;
			if(!actParent.getMenu().isMenuShowed() && (deltaY > 5  || deltaY < -5))//判断为滑屏动作
				moveCount = -1;
			if(moveCount >=0) {
				moveCount++;
				if(moveCount > 15) {
						actParent.getMenu().show();
				}
			} else {
				mLastMotionY = y;
				scrollBy(0, deltaY);
				moveCtrler.addNewMovePoint(System.currentTimeMillis(), mLastMotionY);
			}
		} else if(event.getAction() == MotionEvent.ACTION_UP){
			fgTouch = false;
			if(moveCount > 15) {
				mItemCanFoucs = false;
			}
			else
				mHandler.sendEmptyMessage(1);
			//final VelocityTracker velocityTracker = mVelocityTracker;
			//velocityTracker.computeCurrentVelocity(1000, 1000);
			//int initialVelocity = (int) velocityTracker.getYVelocity();
           // Log.d(DEBUG_TAG, "---initialVelocity--="+initialVelocity);
           // mScroll.fling(0, (int) yPrintStart, 0, -1000, 0, 0, 0, 100);
           //mScroll.startScroll(0, (int) yPrintStart, 0, 480);
			
		}
		return true;
	}
	
	//没用到
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroll.computeScrollOffset()) {
			//final int y = mScroll.getCurrY();
			if(moveCtrler.getCurrentVelocity() == 0) {
				if (!mScroll.isFinished()) {
					mScroll.abortAnimation();
	            }
			}
			else {
				final int y = (int)moveCtrler.getPosIn(System.currentTimeMillis(), false);
				final int deltaY = (int) (mLastMotionY - y);
            	mLastMotionY = y;
            	Log.d(DEBUG_TAG, "------deltaY-------------"+deltaY);
            	scrollBy(0, deltaY);
			}
		}
	}
	
	@Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
		yPrintStart -= y;
		try {
			postionCheck();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isScroll = true;
		invalidate();
	}
	
	
	
	//没用到
	public void moveLooper() {
		while(slideFg){
			//float s = moveCtrler.getPos(10, false) - yTouchDown;
			long t0 = System.currentTimeMillis();
			long tAdd = moveCtrler.getTime(System.currentTimeMillis(), false);
			if(tAdd != 0) {
				while(System.currentTimeMillis() < t0+tAdd);
				yPrintStart=yPrintStart+1;
				invalidate();
			}
			else
				slideFg = false;
		}
	}
	//没用到
	class MoveTimer extends TimerTask{
		@Override
        public void run() {
			float s = moveCtrler.getPosBy(2, false) - yTouchDown;
			if(yPrintStart != s){
				yPrintStart = s;
				Message msg = new Message();
				Bundle  mBundle = new Bundle();
				mBundle.putFloat("distance", s);
				msg.setData(mBundle);
				mHandler.sendMessage(msg);
			}
			else {
				slideFg = false;
				this.cancel();
			}
		}
	}

}



