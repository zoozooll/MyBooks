package com.ccbooks.listener;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.ccbooks.adapter.CatelogAdapter;
import com.ccbooks.bo.BookMarkBo;
import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.FileBo;
import com.ccbooks.dao.BooksDao;
import com.ccbooks.dao.DBHelper;
import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;
import com.ccbooks.vo.Book;
import com.chinachip.TextReader.TextMenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings.System;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class delMark implements OnTouchListener {

	private Context context;
	private String bookname;
	private int id;
	private CatelogAdapter adapter; 
	private ImageView delsel;
	
	
	public delMark(Context context,CatelogAdapter adapter, String bookname, int id) {
		super();
		
		this.context = context;
		this.bookname = bookname;
		this.id = id;
		this.adapter = adapter;
		
		
	}





	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if ((event.getAction() == MotionEvent.ACTION_DOWN )||(event.getAction() == 2 )) {
			// 按下的处理
			v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.content_marksel));
			Log.i("zzq","down");
		}
		else
			v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.content_delmark));
		if (event.getAction() == MotionEvent.ACTION_UP) {
				android.content.DialogInterface.OnClickListener diol =
						new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch (which) {
							case AlertDialog.BUTTON_POSITIVE:
								BookMarkBo  bmo = new BookMarkBo(context); 
								bmo.delMarkByDb(bookname, id);
								
								//bcv.mark = null;
								//BookMarkBo.isMark(bcv);
								dialog.cancel();
								adapter.items = BookMarkBo.marks;
								adapter.notifyDataSetChanged();
								break;
							case AlertDialog.BUTTON_NEGATIVE:
								dialog.cancel();
								break;
							default:
								break;
							}
						}
					
				};
				
				AlertDialog adg = new AlertDialog.Builder(context).
					setTitle("   删除书签?     ")
					.setPositiveButton("删除", diol)
					.setNegativeButton("取消", diol)
					.create();
				adg.show();
				
	}
	return true;
}
}
