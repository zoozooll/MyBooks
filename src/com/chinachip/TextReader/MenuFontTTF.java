package com.chinachip.TextReader;

import java.util.ArrayList;

import com.ccbooks.adapter.CharactorSetAdapter;
import com.ccbooks.adapter.FontAdapter;
import com.ccbooks.fullscreen.listener.MenuCharactorSetClick;
import com.ccbooks.fullscreen.listener.MenuFontTTFClick;
import com.ccbooks.fullscreen.listener.MenuFontTTFSetClick;
import com.ccbooks.util.FontListItem;
import com.ccbooks.util.StringConfig;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFontTTF extends MenuFun{
	private TextMenu menuParent;
	private FontAdapter fontAdapter;
	private ArrayList<FontListItem> fontList =null;
	
	public MenuFontTTF(TextMenu tmObj){
		menuParent = tmObj;
	}
	
	public TextMenu getMenuParent() {
		return menuParent;
	}

	public void setMenuParent(TextMenu menuParent) {
		this.menuParent = menuParent;
	}	
	
	@Override
	public LinearLayout getLayoutFont() {
		return null;
	}

	@Override
	public boolean hide() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isShowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean show() {
		turnToCharsetList();
		return true;
	}
	
	public boolean turnToCharsetList(){
		LinearLayout bkmkListLayout = (LinearLayout)menuParent.getActParent().getLayoutInflater().inflate(R.layout.fs_charactor_list, null);
		String fontName = menuParent.getActParent().ttfFileName;
		TextReader.bo.updateFont(TextReader.book.id, fontName);
		fontList = menuParent.getActParent().font.getFontListItem();
		int selected = 0;
		for(int i = 0; i< fontList.size(); i++ )
		{
			if(fontList.get(i).getTtfFileName().equals(fontName))
				selected = i;
		}
		fontAdapter = new FontAdapter(menuParent.getActParent(),fontList,selected);
		fontAdapter.setMode(2);
		ListView list = (ListView)bkmkListLayout.findViewById(R.id.charactor_list);
		list.setAdapter(fontAdapter);
	//	list.setOnItemClickListener(new MenuFontTTFSetClick(this));
		AlertDialog aDCharset = new AlertDialog.Builder(menuParent.getActParent())
				.setTitle(R.string.fontTTF_list)
				.setView(bkmkListLayout)
				.create();
		aDCharset.setCancelable(true);
		aDCharset.setCanceledOnTouchOutside(true);
		aDCharset.show();
		return true;
	}
	
	public void changeCharsetByID(int id) {
		fontAdapter.selected = id;
		String fontName = fontList.get(id).getTtfFileName();
		long tempoffset = menuParent.getActParent().getCurPostion();
		menuParent.getActParent().bo.updateFont(menuParent.getActParent().book.id, fontName);
		menuParent.getActParent().font.setFont(fontName);
		menuParent.getActParent().bc.setPaint(menuParent.getActParent().font.mPaint);
		menuParent.getActParent().ttfFileName = fontName;
		SharedPreferences sp = menuParent.getActParent().getSharedPreferences("config", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("ttfFileName", fontName);
		editor.commit();
//		TextView textFont.setText(String.valueOf(fontSize));		
//		menuParent.getActParent().getEngine().setFontsize(fontSize);
//		menuParent.getActParent().reloadText();
//		
		menuParent.getActParent().setCurPostion(tempoffset);
		menuParent.getActParent().reloadText();
        menuParent.turntoBookRequest();
		fontAdapter.notifyDataSetChanged();
	}
}
