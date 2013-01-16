package com.ccbooks.listener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;

public class BtnFullScreenClick implements OnClickListener {

	// 所有控件；
	// private LinearLayout llyContentMain;

	private Button btnFullScreen;

	private BookContentView bcv;

	private int id;
	private boolean flag;

	// 点击目录按钮

	public BtnFullScreenClick(int id, boolean flag, Button btnFullScreen,
			BookContentView bcv) {
		super();
		this.btnFullScreen = btnFullScreen;
		this.bcv = bcv;
		this.flag = flag;
		this.id = id;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (bcv.showFullScreen != 1) {
			bcv.showFullScreen = 1;

			btnFullScreen.setBackgroundDrawable(bcv.getResources().getDrawable(
					R.drawable.content_btn_fullscreen));

			System.out.println("BtnCatlog click");
			if (flag) {
				((BookContentView) bcv).hiddenOpenWins();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("bookId", id);
				bundle.putLong("markPoint", bcv.markPoint);
				intent.putExtras(bundle);
				intent.setClass(bcv, TextReader.class);
				bcv.startActivityForResult(intent, id);
				SharedPreferences sp = ((BookContentView) bcv).getSharedPreferences("config", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				BookShelfView.readerMode = 2;
				editor.putInt("readerMode", 2);
				editor.commit();
				bcv.finish();
			} else {
				bcv.setResult(-2);
				bcv.finish();
			}
		}
	}
}
