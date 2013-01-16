package com.ccbooks.fullscreen.listener;

import android.content.Intent;
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
import com.chinachip.TextReader.MenuSetting;
import com.chinachip.TextReader.TextMenu;

public class MenuBtnCharactorSetClick implements OnClickListener {

	// 所有控件；
	// private LinearLayout llyContentMain;


	private MenuSetting ms;

	
	public MenuBtnCharactorSetClick(MenuSetting ms){
		this.ms = ms;
	
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ms.bkmkListEvent();
	}
}
