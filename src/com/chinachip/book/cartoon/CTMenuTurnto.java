package com.chinachip.book.cartoon;

import com.ccbooks.util.BookUtil;
import com.ccbooks.view.CartoonReader;
import com.ccbooks.view.R;
import com.chinachip.book.cartoon.listener.CTMenuClick;
import com.chinachip.book.cartoon.listener.CTMenuTurntoBottunCancleClick;
import com.chinachip.book.cartoon.listener.CTMenuTurntoBottunOKClick;
import com.chinachip.book.cartoon.listener.CTMenuTurntoSeekBarChange;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class CTMenuTurnto{
	static final String DEBUG_TAG = "jf";
	public CartoonReader menuParent;
	private LinearLayout layoutTurnto;
	public TextView textInfo;
	public SeekBar seeBar;
	public int file;


	private long curPercent;

	private Button btnOK;
	private Button btnCancle;
	
	public CTMenuTurnto(CartoonReader tmObj){
		menuParent = tmObj;
		layoutTurnto = menuParent.layoutTurnto;
		
		
		textInfo = (TextView)menuParent.findViewById(R.id.ct_turnto_info);
		
		seeBar = (SeekBar)menuParent.findViewById(R.id.ct_seebar_turnto);
		//seeBar.setProgress(fontSize);
		CTMenuTurntoSeekBarChange seebarlistener = new CTMenuTurntoSeekBarChange(this,menuParent);
		seeBar.setOnSeekBarChangeListener(seebarlistener);	
		CTMenuClick menuclick = new CTMenuClick();
		layoutTurnto.setOnClickListener(menuclick);
	
    	
	}
	
	public void progressChangeEvent(int percent){

		if(curPercent != percent){
			curPercent = percent;
		
			if(percent !=0){
				String temp = BookUtil.tansDecimal(percent);
				textInfo.setText(temp+"%");
				menuParent.setCurPostion((int)percent*menuParent.endFile/10000);
				Log.d(DEBUG_TAG, String.valueOf(percent*menuParent.endFile/10000));
			}else{
				String temp = BookUtil.tansDecimal(percent);
				textInfo.setText(temp+"%");
				menuParent.setCurPostion(1);
			}
			
			
		}
	}	
	
	public void okEvent(){
		file = (int)seeBar.getProgress()*menuParent.endFile/10000;
		hide();
	}
	
	public void cancleEvent(){
		menuParent.setCurPostion(file);
	
		hide();
	}
	
	public LinearLayout getLayoutFont() {
		return layoutTurnto;
	}
	
	public void setlayoutFun(LinearLayout layoutTurnto) {
		this.layoutTurnto = layoutTurnto;
	}
	
	public boolean isShowed(){
		if(View.VISIBLE == layoutTurnto.getVisibility())
			return true;
		else 
			return false;
	}
	
	public boolean show(){
		if(View.VISIBLE == layoutTurnto.getVisibility())return false;
		file = menuParent.file;
		
		curPercent = file*10000/menuParent.headFile;
	
		seeBar.setProgress((int)(file*10000/menuParent.headFile));
		layoutTurnto.setVisibility(View.VISIBLE);
		return true;
	}
	
	public boolean hide(){
		if(View.GONE == layoutTurnto.getVisibility())return false;
		layoutTurnto.setVisibility(View.GONE);
		return true;
	}
}
