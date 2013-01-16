package com.ccbooks.listener;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.FileBo;
import com.ccbooks.dao.BooksDao;
import com.ccbooks.dao.DBHelper;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.vo.Book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;

public class DelListener implements OnClickListener {
	private Context context;
	private Book book;
	private BaseAdapter adapter;

	public DelListener(Context context,BaseAdapter adapter,Book book) {
		this.context=context;
		this.book=book;
		this.adapter=adapter;
	}
	
	@Override
	public void onClick(View v) {
		android.content.DialogInterface.OnClickListener diol =
			new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case AlertDialog.BUTTON_POSITIVE:
						deletingFile();
						dialog.cancel();
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
			setTitle(com.ccbooks.view.R.string.deletetitle)
			.setPositiveButton("删除", diol)
			.setNegativeButton("取消", diol)
			.create();
		adg.show();
	}
	
	public void deletingFile(){
		try {
			FileBo.delFile(book.bookpath);
			BooksDao dao= BooksDao.getDao(context);
			dao.delete(DBHelper.TABLE_BOOK,book.id);
			BooksBo.books.remove(book);
			adapter.notifyDataSetChanged();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
