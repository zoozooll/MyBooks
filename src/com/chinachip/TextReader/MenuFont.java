package com.chinachip.TextReader;


import java.util.ArrayList;

import com.ccbooks.fullscreen.listener.MenuClick;
import com.ccbooks.fullscreen.listener.MenuFontSeekBarChange;
import com.ccbooks.view.R;
import com.chinachip.ccbooks.engine.LineIndex;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFont  extends MenuFun{
	final int FONTZISE_MIN = 18;
	private TextMenu menuParent;
	private LinearLayout layoutFun;
	private TextView textFont;
	private SeekBar seeBar;
	private int fontSize;
	
	public MenuFont(TextMenu tmObj){
		menuParent = tmObj;
		layoutFun = (LinearLayout)menuParent.getLayoutMenu().findViewById(R.id.fun_font);
		fontSize = menuParent.getActParent().getEngine().getFontsize();
		textFont = (TextView)menuParent.getLayoutMenu().findViewById(R.id.fun_font_info);
		textFont.setText(String.valueOf(fontSize));
		seeBar = (SeekBar)menuParent.getLayoutMenu().findViewById(R.id.seebar_font);
		seeBar.setProgress(fontSize-FONTZISE_MIN);
	//	MenuFontSeekBarChange seebarlistener = new MenuFontSeekBarChange(this);
	//	seeBar.setOnSeekBarChangeListener(seebarlistener);	
    	MenuClick menuclick = new MenuClick();
    	layoutFun.setOnClickListener(menuclick);
	}
	
	public TextMenu getMenuParent() {
		return menuParent;
	}

	public void setMenuParent(TextMenu menuParent) {
		this.menuParent = menuParent;
	}

	public void progressChangeEvent(int size){
		fontSize = size*2+FONTZISE_MIN;
		textFont.setText(String.valueOf(fontSize));		
		menuParent.getActParent().getEngine().setFontsize(fontSize);
		menuParent.getActParent().reloadText();
/*		if(menuParent.getActParent().getCommentView().getLineList().size() > 0){
			ArrayList<Line> getIndexlist = (ArrayList<Line>) menuParent.getActParent().getCommentView().getLineList();
			for(Line tempLine :getIndexlist){
				if(tempLine.getFontSize() == fontSize){
					Toast.makeText(menuParent.getActParent(), "字体"+fontSize+"下有批注", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}*/
		if(menuParent.getActParent().getIsComment()){
//			menuParent.getActParent().getCommentView().scrollPaint();
			Toast.makeText(menuParent.getActParent(), R.string.changeFontTips, Toast.LENGTH_SHORT).show();
			menuParent.getActParent().getCommentView().clearView();
			menuParent.getActParent().setIsComment(false);
		}
		if(menuParent.getActParent().getCommentView().getLineListFont().get(fontSize+"") != null){
			Toast.makeText(menuParent.getActParent(), "字体"+fontSize+"下有批注", Toast.LENGTH_SHORT).show();
		}
	//	menuParent.getActParent().CommentPaint();
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
		textFont.setText(String.valueOf(fontSize));
		//seeBar.setProgress(fontSize);
		layoutFun.setVisibility(View.VISIBLE);
		return true;
	}
	
	public boolean hide(){
		if(View.VISIBLE != layoutFun.getVisibility())return false;
		layoutFun.setVisibility(View.GONE);
		return true;
	}
	
}
