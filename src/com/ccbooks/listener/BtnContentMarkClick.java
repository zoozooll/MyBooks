package com.ccbooks.listener;

import java.util.ArrayList;
import java.util.List;

import com.ccbooks.adapter.CatelogAdapter;
import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.vo.CatelogItem;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class BtnContentMarkClick implements OnClickListener {

	private BookCatalogView bcv;
	private Button btnContentCatelog;
	private Button btnContentMark;

	// 点击书签按钮；

	public BtnContentMarkClick(BookCatalogView bcv, Button btnContentCatelog,
			Button btnContentMark) {
		super();
		this.bcv=bcv;
		this.btnContentCatelog = btnContentCatelog;
		this.btnContentMark = btnContentMark;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (bcv.showCatelogType != 2) {
			bcv.showCatelogType = 2;
			bcv.showing();
			btnContentMark.setBackgroundDrawable(bcv.getResources().getDrawable(
					R.drawable.bookcatalog_right_selected));
			btnContentCatelog.setBackgroundDrawable(bcv.getResources().getDrawable(
					R.drawable.bookcatalog_left_unselected));
		}
	}

}
