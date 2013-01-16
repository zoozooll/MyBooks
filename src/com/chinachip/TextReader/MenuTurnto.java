package com.chinachip.TextReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.ccbooks.fullscreen.listener.MenuClick;
import com.ccbooks.fullscreen.listener.MenuFontSeekBarChange;
import com.ccbooks.fullscreen.listener.MenuTurntoBottunCancleClick;
import com.ccbooks.fullscreen.listener.MenuTurntoBottunOKClick;
import com.ccbooks.fullscreen.listener.MenuTurntoSeekBarChange;
import com.ccbooks.util.BookUtil;
import com.ccbooks.view.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MenuTurnto extends MenuFun{
	static final String DEBUG_TAG = "jf";
	public TextMenu menuParent;
	private LinearLayout layoutFun;
	private TextView textInfo;
	public SeekBar seeBar;
	private long curPos;
	private int curLine;
	private float curOffset;
	private long curPercent;
	private long fileLength;
	private Button btnOK;
	private Button btnCancle;
	
	public MenuTurnto(TextMenu tmObj){
		menuParent = tmObj;
		layoutFun = (LinearLayout)menuParent.getLayoutMenu().findViewById(R.id.fun_turnto);
		
		textInfo = (TextView)menuParent.getLayoutMenu().findViewById(R.id.fun_turnto_info);
		
		seeBar = (SeekBar)menuParent.getLayoutMenu().findViewById(R.id.seebar_turnto);
		//seeBar.setProgress(fontSize);
		MenuTurntoSeekBarChange seebarlistener = new MenuTurntoSeekBarChange(this);
		seeBar.setOnSeekBarChangeListener(seebarlistener);	
		
		
	
		
    	MenuClick menuclick = new MenuClick();
    	layoutFun.setOnClickListener(menuclick);
	}
	
	public void progressChangeEvent(int percent){
		if(curPercent != percent){
			curPercent = percent;
			String temp = BookUtil.tansDecimal((int)curPercent);
			textInfo.setText(temp+"%");
			menuParent.getActParent().setCurPostion(percent*fileLength/10000);
			Log.d(DEBUG_TAG, String.valueOf(percent*fileLength/10000));
			menuParent.getActParent().reParseText();
			if(menuParent.getActParent().getIsComment()){
//				menuParent.getActParent().getCommentView().scrollPaint();
				Toast.makeText(menuParent.getActParent(), R.string.turnToTips, Toast.LENGTH_SHORT).show();
				menuParent.getActParent().getCommentView().clearView();
				menuParent.getActParent().setIsComment(false);
			}
			
		}
	}	
	
	public void okEvent(){
		hide();
	}
	
	public void cancleEvent(){
		menuParent.getActParent().setCurPostion(curPos);
		menuParent.getActParent().setPageLineOffset(curLine);
		menuParent.getActParent().setLinePrintOffset(curOffset);
		menuParent.getActParent().reParseText();
		hide();
	}
	
	public LinearLayout getLayoutFont() {
		return layoutFun;
	}
	
	public void setlayoutFun(LinearLayout layoutFun) {
		this.layoutFun = layoutFun;
	}
	
	public boolean isShowed(){
		if(View.VISIBLE == layoutFun.getVisibility())
			return true;
		else 
			return false;
	}
	
	public boolean show(){
		if(View.VISIBLE == layoutFun.getVisibility())return false;
		curPos = menuParent.getActParent().getCurPostion();
		curLine = menuParent.getActParent().getPageLineOffset();
		curOffset = menuParent.getActParent().getLinePrintOffset();
		fileLength = menuParent.getActParent().getFileLength();
		curPercent = curPos*10000/fileLength;
		String temp = BookUtil.tansDecimal((int)curPercent);
		textInfo.setText(temp+"%");
		seeBar.setProgress((int)(curPos*10000/fileLength));
		layoutFun.setVisibility(View.VISIBLE);
		return true;
	}
	
	public boolean hide(){
		if(View.GONE == layoutFun.getVisibility())return false;
		layoutFun.setVisibility(View.GONE);
		return true;
	}
}
