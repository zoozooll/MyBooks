package com.ccbooks.fullscreen.listener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;
import com.ccbooks.vo.Book;
import com.chinachip.TextReader.MenuFun;
import com.chinachip.TextReader.TextMenu;

public class MenuBtnTransModeClick implements OnClickListener {

	// 所有控件；
	// private LinearLayout llyContentMain;

	private Button btnTransMode;

	private TextReader tr;
	
	private boolean flag;
	
	private int id;

	private TextMenu menu;
	private MenuFun funObj;
	
	public MenuBtnTransModeClick(TextMenu tmObj, MenuFun llObj,boolean flag,int id,TextReader tr){
		this.menu = tmObj;
		this.funObj = llObj;
		this.flag = flag;
		this.id = id;
		this.tr = tr;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
			if(funObj.isShowed()){
				funObj.hide();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("bookId", id);
				intent.putExtras(bundle);
				intent.setClass(tr, BookContentView.class);
				tr.startActivityForResult(intent, id);
				SharedPreferences sp = tr.getSharedPreferences("config", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putInt("readerMode", 1);
				editor.commit();
				tr.finish();
			} else{
				menu.setAllLevel2Gone();
				funObj.show();
				tr.finish();
			}
			
		
		
	}
}
