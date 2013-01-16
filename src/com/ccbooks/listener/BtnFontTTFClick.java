package com.ccbooks.listener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;

import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;

public class BtnFontTTFClick implements OnClickListener {

	// 所有控件；
	// private LinearLayout llyContentMain;

	private Button btnFontTTF;

	private BookContentView bcv;

	private int id;
	private boolean flag;

	// 点击目录按钮

	public BtnFontTTFClick(int id, boolean flag, Button btnFontTTF,
			BookContentView bcv) {
		super();
		this.btnFontTTF = btnFontTTF;
		this.bcv = bcv;
		this.flag = flag;
		this.id = id;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		bcv.btnFontEnter.setVisibility(View.GONE);
		bcv.turnToFontTTFList();	
	
	}
}
