package com.ccbooks.listener.contentView1;

import com.ccbooks.view.BookContentView1;
import com.ccbooks.view.R;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BtnBackColorListener implements OnClickListener {

	private BookContentView1 bcv;
	private TextView tvMainContent ;
	private FrameLayout fltBookContent1;
	
	public BtnBackColorListener(BookContentView1 bcv,FrameLayout fltBookContent1,TextView tvMainContent) {
		super();
		this.bcv = bcv;
		this.tvMainContent = tvMainContent;
		this.fltBookContent1 = fltBookContent1;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(bcv.dayNight==1){
			bcv.dayNight = 0;
			fltBookContent1.setBackgroundColor(Color.BLACK);
			tvMainContent.setTextColor(Color.LTGRAY);
		}else {
			bcv.dayNight = 1;
			fltBookContent1.setBackgroundDrawable(bcv.getResources().getDrawable(R.drawable.bookpagebg));
			tvMainContent.setTextColor(Color.BLACK);
		}
	}

}
