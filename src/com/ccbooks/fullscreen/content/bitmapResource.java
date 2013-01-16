package com.ccbooks.fullscreen.content;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.GestureDetector;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.ccbooks.fullscreen.bookcore.BookCore;
import com.ccbooks.view.TextReader;
import com.chinachip.pageeffect.BitmapInforNation;
import com.chinachip.pageeffect.ThreeDimFlipper;

public class bitmapResource extends BitmapInforNation{

	private TextThreeDim ttd = null;

	private boolean isStart = false;
	
	private boolean isFinish = false;
	
	public bitmapResource(TextThreeDim ttd){
		this.ttd = ttd;
	
	}
	@Override
	public void getInitializationBitmap() {
		long offset = ttd.markPoint;
		// TODO Auto-generated method stub
		ttd.pageViews.mCurPageBitmap = ttd.getCurrPage(offset);
		
//		if(offset == 0){
//			setStart(true);
//		}
		ttd.pageViews.mNextPageBitmap = ttd.bitmap2.mbmpCurr;
		
	}
	
	public void currInitializationBitmap() {
		
		ttd.pageViews.mCurPageBitmap = ttd.getCurrPage();
//		long offset = 0;
//		try {
//			offset = ttd.engine.getCurrIndexAt();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(offset == 0){
//			setStart(true);
//		}
		setStart(false);
		setFinish(false);
		ttd.pageViews.mNextPageBitmap = ttd.bitmap2.mbmpCurr;
	
	}
	

	@Override
	public Bitmap getNextBitmap() {
		// TODO Auto-generated method stub
		setStart(false);
		return ttd.getNextPage();
	}

	@Override
	public Bitmap getbeforeBitmap() {
		setFinish(false);
		// TODO Auto-generated method stub
		return ttd.getPrevPage();
	}

	@Override
	public boolean isFinish() {
		// TODO Auto-generated method stub
		if(isFinish){
			return true;
		}
		return false;
	}

	@Override
	public boolean isStart() {
		// TODO Auto-generated method stub
		if(isStart){
			return true;
		}
		return false;
	}
	 
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

}
