package com.ccbooks.listener;


import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ccbooks.bo.BookContentThread;

import com.chinachip.books.plugin.Plugin;
import com.chinachip.ccbooks.core.TextSizeUtil;
import com.ccbooks.util.BookUtil;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;

public class SbrMainContentOnChangeListener implements OnSeekBarChangeListener {

	private BookContentView bcv;
	private boolean mTouchSeek = false;//判断是否点击进度条
	private long fileLength;
	private float betweenValue;
	private static final int HORIZONTAL = 2;
	private static final int VERTICAL = 1; 
	public SbrMainContentOnChangeListener(BookContentView bcv){
		this.bcv = bcv;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(-1 == bcv.judgeFocus(bcv.BCVFOCUS,0)){
			return;
		}
		if(!bcv.mShowing){
			bcv.mShowing = true;
		}
		//计算mDialog的padding
		int seekBarWidth = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
		if(HORIZONTAL == bcv.portraint){//横屏
			betweenValue = (float) (seekBarWidth*seekBar.getProgress()/10000)-5;
		}else if(VERTICAL == bcv.portraint){//竖屏
			betweenValue = (float) (seekBarWidth*seekBar.getProgress()/10000)-5;
		}
		/*设置mDialogLayout的padding以达到跟随的效果
		 * 基本算法：betweenValue+初始的偏移值，即0%时候的偏移值；
		 */
		bcv.mDialogLayout.setPadding((int)betweenValue+bcv.mDialogPaddingLeft, 0, 0,bcv.mDialogPaddingBottom);
		if(bcv.mShowing && mTouchSeek)
		{
			//点击进度条且有变化才显示
			bcv.mDialogText.setVisibility(View.VISIBLE);
		}
		else
		{
			bcv.mDialogText.setVisibility(View.INVISIBLE);
		}
		int progresses = seekBar.getProgress();
		String temp = BookUtil.tansDecimal(progresses);
		bcv.mDialogText.setText(temp+"%");
		if(bcv.bookpagebg.rightShowPage!=null)
			bcv.bookpagebg.rightShowPage.mySeekBarProgress(seekBar.getProgress());
		if(bcv.bookpagebg.leftShowPage!=null)
			bcv.bookpagebg.leftShowPage.mySeekBarProgress(seekBar.getProgress());
//		bcv.mHandler.postDelayed(bcv.mRemoveWindow, 500);
		//自定義虛擬拖動子跟著拖動
		/*for (SinglePage sp: bcv.bookpagebg.pageList){
			sp.mySeekBarProgress(seekBar.getProgress());
		}*/
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if(-1 == bcv.judgeFocus(bcv.BCVFOCUS,0)){
			return;
		}
		mTouchSeek = true;
		//自定義虛擬拖動子跟著拖動
		bcv.bookpagebg.rightShowPage.mySeekBarProgress(seekBar.getProgress());
		bcv.bookpagebg.leftShowPage.mySeekBarProgress(seekBar.getProgress());
		/*for (SinglePage sp: bcv.bookpagebg.pageList){
			sp.mySeekBarProgress(seekBar.getProgress());
		}*/
	}

	public long getFileLength(){
		long fileLength = 0;
		Plugin plugin = bcv.bc.pm.getDefaultPlugin();
		fileLength = bcv.bc.pm.length(plugin, bcv.bc.pm.filehandle);
		
		if(fileLength > 0) return fileLength;
		else return 0;
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if(-1 == bcv.judgeFocus(bcv.BCVFOCUS,0)){
			return;
		}
		bcv.showDialog();
		bcv.isMove = true;
		fileLength = getFileLength();
			
		int percent = seekBar.getProgress(); 
		
		int fontSize  = TextSizeUtil.fontSize;
		TextSizeUtil.fontSize=fontSize;
		bcv.lineCount = TextSizeUtil.getLineCount(fontSize, bcv.portraint);
		float temppersent = percent / 10000.0f;
		float tempnumber = fileLength * temppersent;
		long skipNumber = (long)Math.round(tempnumber);
		
		if (bcv.portraint==1){
			if (percent == 0){
				bcv.isFileEnd = false;
				bcv.bct.what = BookContentThread.FIRST_VIRI;
				
			}else if (percent >0 && percent<10000){
				bcv.isFileEnd = false;
				bcv.bct.skipNumber =skipNumber;
				bcv.bct.what = BookContentThread.THREAD_VIRI;
				
			}else if (percent == 10000) {
				bcv.isFileEnd = true;
				bcv.bct.skipNumber =skipNumber;
				bcv.bct.what = BookContentThread.END_VIRI;
			}
			
		}else if (bcv.portraint==2) {
			if (percent == 0){
				bcv.isFileEnd = false;
				bcv.bct.what = BookContentThread.FIRST_HORI;
				
			}else if (percent >0 && percent<10000){
				bcv.isFileEnd = false;
				bcv.bct.skipNumber =skipNumber;
				bcv.bct.what = BookContentThread.THREAD_HORI;
				
			}else if (percent == 10000){
				bcv.isFileEnd = true;
				bcv.bct.skipNumber =skipNumber;
				bcv.bct.what = BookContentThread.END_HORI;
			}	
		}
		if(bcv.isRunningThread == false){
			new Thread(bcv.bct).start();
		}	
		if(bcv.mark!=null){
			bcv.btnBookMark.setBackgroundResource(R.drawable.content_btn_onmark);
		}else{
			bcv.btnBookMark.setBackgroundResource(R.drawable.content_btn_outmark);
		}
	
		//退出隐藏进度显示
		mTouchSeek = false;
		bcv.mHandler.removeCallbacks(bcv.mRemoveWindow);
		bcv.mHandler.post(bcv.mRemoveWindow);
		//自定義虛擬拖動子跟著拖動
		//bcv.bookpagebg.rightShowPage.mySeekBarProgress(seekBar.getProgress());
		//bcv.bookpagebg.leftShowPage.mySeekBarProgress(seekBar.getProgress());
	}

}
