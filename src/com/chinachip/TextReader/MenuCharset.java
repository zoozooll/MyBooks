package com.chinachip.TextReader;

import com.ccbooks.adapter.CharactorSetAdapter;
import com.ccbooks.fullscreen.listener.MenuCharactorSetClick;
import com.ccbooks.util.StringConfig;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MenuCharset extends MenuFun{
	private TextMenu menuParent;
	private LinearLayout layoutFun;
	CharactorSetAdapter charactorSetAdapter;
	
	public MenuCharset(TextMenu tmObj){
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
		String charset = menuParent.getActParent().bc.getEnc();
		TextReader.bo.updateChartset(TextReader.book.id, charset);
		String[] charList = StringConfig.CHARSETS;
		int selected = 0;
		for(int i = 0; i< charList.length; i++ )
		{
			if(charList[i].equals(charset))
				selected = i;
		}
		charactorSetAdapter = new CharactorSetAdapter(menuParent.getActParent(),StringConfig.CHARSETS,selected);
		charactorSetAdapter.setMode(2);
		ListView list = (ListView)bkmkListLayout.findViewById(R.id.charactor_list);
		list.setAdapter(charactorSetAdapter);
	//	list.setOnItemClickListener(new MenuCharactorSetClick(this));
		AlertDialog aDCharset = new AlertDialog.Builder(menuParent.getActParent())
				.setTitle(R.string.charactor_list)
				.setView(bkmkListLayout)
				.create();
		aDCharset.setCancelable(true);
		aDCharset.setCanceledOnTouchOutside(true);
		aDCharset.show();
		return true;
	}
	
	public void changeCharsetByID(int id) {
		charactorSetAdapter.selected = id;
		String charSet = StringConfig.CHARSETS[id];
		long tempoffset = menuParent.getActParent().getCurPostion();
		menuParent.getActParent().bo.updateChartset(menuParent.getActParent().book.id, charSet);
		menuParent.getActParent().bc.setCharset(charSet);

		menuParent.getActParent().setCurPostion(tempoffset);
		menuParent.getActParent().reloadText();
        menuParent.turntoBookRequest();
		charactorSetAdapter.notifyDataSetChanged();
	}
}
