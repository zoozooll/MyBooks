package com.ccbooks.fullscreen.content;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.ccbooks.fullscreen.bookcore.BookCore;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;
import com.chinachip.ccbooks.engine.LineIndex;

import android.app.Activity;
import android.content.Context;  
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;  
import android.graphics.Paint;  
import android.util.AttributeSet;  
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;  
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class TextFlipper implements OnGestureListener {  
	static final int FLING_MIN_DISTANCE = 120;
	static final String DEBUG_TAG = "jf";
	private TextReader actParent;
	private ViewGroup viewParent;
	private ViewFlipper flipperMain;
	private GestureDetector detectorMain;
	private TextRender pageViews1;
	private boolean pageViews1Using;
	private TextRender pageViews2;
	private ArrayList<String> texts = null;
	private ArrayList<LineIndex> lineIndexs = null;
	private BookCore engine;
	private int textColor = 0xff000000;
	private boolean mItemCanFoucs;
	public GestureDetector detector = new GestureDetector(this);
	
	
    public TextFlipper(TextReader actObj, BookCore eng, ViewGroup viewObj, int textColor)  
    {  
    	actParent = actObj;
    	engine = eng;
    	viewParent = viewObj;
    	this.textColor=textColor;
    	intiFlipper();
    }
    
    public void intiFlipper(){
    	initViews();
    }
    
    public boolean initViews() {
    	pageViews1 = new TextRender(actParent);
    	pageViews1.setPaint(TextReader.bc.getPaint());
    	pageViews1.setTextSize(engine.getFontsize());
    	pageViews1.setLineSpace(engine.getLineSpace());
    	pageViews1.setTextColor(textColor);
    	pageViews1.setPadding(10, 0, 10, 0);
    	pageViews2 = new TextRender(actParent);
    	pageViews2.setPaint(TextReader.bc.getPaint());
    	pageViews2.setTextSize(engine.getFontsize());
    	pageViews2.setLineSpace(engine.getLineSpace());
    	pageViews2.setTextColor(textColor);
    	pageViews2.setPadding(10, 0, 10, 0);
    	pageViews1Using = true;
    	
    	flipperMain = new ViewFlipper(actParent);
    	flipperMain.addView(pageViews1);
    	flipperMain.addView(pageViews2);
    	viewParent.addView(flipperMain);
    	

    	
    	detectorMain = new GestureDetector(this);
    	mItemCanFoucs = true;
    	return true;
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
	}
	
    public GestureDetector getDetector() {
    	return detectorMain;
    }
    
    public ViewFlipper getFillperView() {
    	return flipperMain;
    }
    
    public ArrayList<String> getCurTextArray() {
    	return getPageViewsUsing().getTextArray();
    }
    
    public ArrayList<LineIndex> getCurLinesIndex() {
    	return lineIndexs;
    }
    
    public boolean turnToCurPage() {
    	if(addCurPageToFipper()){
    		flipperMain.invalidate();
    		return true;
    	} else{
    		return false;
    	}    	
    }
    
    public boolean turnToNextPage() {
    	if(addNextPageToFipper()){
    		flipperMain.showNext();
    		return true;
    	} else{
    		return false;
    	}
    }
 
    public boolean turnToPrePage() {
    	
		if(addPrePageToFipper()){
			flipperMain.showNext();
			return true;
		} else{
			return false;
		}
		
	}    
    
    public void reparseText() {
    	reflashTextAttr();
    	turnToCurPage();
    }
    
    public void reloadText() {
    	reflashTextAttr();
    	turnToCurPage();
    }
    
    void reflashTextAttr(){
    	pageViews1.setPaint(TextReader.bc.getPaint());
    	pageViews1.setTextSize(engine.getFontsize());
    	pageViews1.setTextColor(engine.getTextColor());
    	pageViews1.setLineSpace(engine.getLineSpace());
    	pageViews2.setPaint(TextReader.bc.getPaint());
    	pageViews2.setTextSize(engine.getFontsize());
    	pageViews2.setTextColor(engine.getTextColor());
    	pageViews2.setLineSpace(engine.getLineSpace());    	
    }
    
    public boolean addCurPageToFipper() {
    	Log.d(DEBUG_TAG, "---------------------TextFlip addCurPageToFipper-------------------------");
        try {
        	ArrayList<String> ts = engine.getCurrPage();
        	if(ts.size() > 0) {
        		texts = ts;
        		lineIndexs = engine.getLineIndex();
        	}
        	else
        		return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return addToPageViews(getPageViewsUsing());
    }
    
    public boolean addNextPageToFipper() {
        try {
			long start=System.currentTimeMillis();
			ArrayList<String> ts = engine.getNextPage();
        	if(ts.size() > 0) {
        		texts = ts;
        		lineIndexs = engine.getLineIndex();
        	}
        	else
        		return false;
        	start=System.currentTimeMillis() - start;
        	Log.i(DEBUG_TAG, Long.toString(start));
        } catch (IOException e) {
			e.printStackTrace();
		}
    	return addToPageViews(getPageViewsNotUsing());
    }
   
    public boolean addPrePageToFipper() {
        try {
        	ArrayList<String> ts = engine.getPrevPage();
			if(ts.size() > 0) {
        		texts = ts;
        		lineIndexs = engine.getLineIndex();
        	}
        	else
        		return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return addToPageViews(getPageViewsNotUsing());
    }
     
    public boolean addToPageViews(TextRender pageviews) {
    	if(texts.size() >0){
    		pageviews.setTextArray(texts);
    		setPageViewsUsing(pageviews);
    		pageviews.scrollPaint();
    		return true;
    	} else{
    		Log.d(DEBUG_TAG, "---------------------TextFlip TextFlip texts.size()=0-------------------------");
    		return false;
    	}
    }
  
    public TextRender getPageViewsUsing(){
    	if(pageViews1Using){
    		return pageViews1;
    	} else{
    		return pageViews2;
    	}
    }
    
    public TextRender getPageViewsNotUsing() {
    	if(pageViews1Using){
    		return pageViews2;
    	} else{
    		return pageViews1;
    	}
    }
    
    public void setPageViewsUsing(TextRender pageviews) {
    	if(pageviews == pageViews1){
    		pageViews1Using = true;
    	} else{
    		pageViews1Using = false;
    	}
    }
    
	public TextRender getPageViews1() {
		return pageViews1;
	}

	public TextRender getPageViews2() {
		return pageViews2;
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d(DEBUG_TAG, "---------------------TextFlip onFling-------------------------");
		if(e1.getX() - e2.getX() >= FLING_MIN_DISTANCE){
			this.flipperMain.setInAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_left_in));
			this.flipperMain.setOutAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_left_out));
			if(turnToNextPage() == false) {
				Toast lastToast = null;
				if(actParent.file < actParent.endFile) {
					lastToast = Toast.makeText(actParent, "本节完!", Toast.LENGTH_SHORT);
				} else {
					lastToast = Toast.makeText(actParent, "全书完!", Toast.LENGTH_SHORT);
				}
				lastToast.show();
			}
			return true;
		}
		else if(e2.getX() - e1.getX() >= FLING_MIN_DISTANCE){
			this.flipperMain.setInAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_right_in));
			this.flipperMain.setOutAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_right_out));
			if(turnToPrePage() == false) {
				Toast lastToast = Toast.makeText(actParent, "已到最前页!", Toast.LENGTH_SHORT);
				lastToast.show();
			}
			return true;
		}
		//���ε����򻬶�
		/*
		else if(e1.getY() - e2.getY() >= FLING_MIN_DISTANCE){
			this.flipperMain.setInAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_up_in));
			this.flipperMain.setOutAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_up_out));
			turnToNextPage();
			return true;
		}
		else if(e2.getY() - e1.getY() >= FLING_MIN_DISTANCE){
			this.flipperMain.setInAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_down_in));
			this.flipperMain.setOutAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_down_out));
			turnToPrePage();
			return true;
		}	
		*/			
		else
			return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
			actParent.getMenu().show();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		int screenWidth = engine.getPageWidth();
		if(e.getX() > screenWidth/2) {
			this.flipperMain.setInAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_left_in));
			this.flipperMain.setOutAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_left_out));

			if(turnToNextPage() == false) {
				Toast lastToast = null;
				if(actParent.file < actParent.endFile) {
					lastToast = Toast.makeText(actParent, "本节完!", Toast.LENGTH_SHORT);
				} else {
					lastToast = Toast.makeText(actParent, "全书完!", Toast.LENGTH_SHORT);
				}
				lastToast.show();
			}
			return true;
		} else {
			this.flipperMain.setInAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_right_in));
			this.flipperMain.setOutAnimation(AnimationUtils.loadAnimation(actParent,
				R.anim.push_right_out));
			if(turnToPrePage() == false) {
				Toast lastToast = Toast.makeText(actParent, "已到最前页!", Toast.LENGTH_SHORT);
				lastToast.show();
			}
			return true;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
} 
