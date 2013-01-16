package com.ccbooks.listener;

import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BtnCatalogueClick implements OnClickListener {

	private Activity bcv;
	private int id;
	private boolean flag;
	// 所有控件；
	public BtnCatalogueClick(Activity bcv,int id,boolean flag) {
		super();
		this.bcv = bcv;
		this.flag = flag;
		this.id = id;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("BtnCatlog click");
		if(flag){
			((BookContentView) bcv).hiddenOpenWins();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
		
			bundle.putInt("bookId", id);
			
			intent.putExtras(bundle);
			

			intent.setClass(bcv, BookCatalogView.class);
			bcv.startActivityForResult(intent, id);
		}else{
			bcv.setResult(-2);
			bcv.finish();
		}
	}

}
