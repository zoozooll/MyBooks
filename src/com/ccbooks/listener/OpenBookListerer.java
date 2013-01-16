package com.ccbooks.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.FileBo;
import com.ccbooks.dao.BooksDao;
import com.ccbooks.dao.DBHelper;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.BookContentView1;
import com.ccbooks.view.BookListView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.CartoonReader;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;
import com.ccbooks.vo.Book;
import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class OpenBookListerer implements OnClickListener {

	private Context context;
	private Book book;
	private BaseAdapter adapter;
	private PluginMgr pm;
	private Plugin plugin;
	private int filehandle;
	
	public OpenBookListerer(Context context,Book book,BaseAdapter adapter) {
		this.book=book;
		this.context=context;
		this.adapter = adapter;
	}

	@Override
	public void onClick(View v) {
		if (context instanceof BookShelfView) {
			if(	((BookShelfView)context).isEdit)
			{
				//编辑状态点不了书
				//Toast.makeText(context, "请先退出编辑模式", Toast.LENGTH_SHORT).show();
				android.content.DialogInterface.OnClickListener diol =
					new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
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
			else
			{
//				v.setBackgroundDrawable(context.getResources()
//						.getDrawable(R.drawable.btn_ppressed_color));
				
				
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("bookId", book.id);
			//	bundle.putInt("file", book.file);
				
				
				intent.putExtras(bundle);
				pm = new PluginMgr();
		        PluginUtil.PluginInit(pm);
				plugin = pm.getPlugin(book.bookpath);
				Param param = new Param();
				filehandle = pm.open(plugin, book.bookpath);
				pm.get(plugin, PluginUtil.BOOK_TYPE, param);
				pm.closeAll();
				if(param.i == PluginUtil.BOOK_TYPE_COMIC){
					intent.setClass(context, CartoonReader.class);
				}else if(getReaderMode() == 1)
				{
					intent.setClass(context, BookContentView.class);
				}else if(getReaderMode() == 2)
				{
					intent.setClass(context, TextReader.class);
				}
				
				context.startActivity(intent);
			}	
		}
		else
		{	
			v.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.btn_ppressed_color));
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("bookId", book.id);
		//	bundle.putInt("file", book.file);
			intent.putExtras(bundle);
			pm = new PluginMgr();
	        PluginUtil.PluginInit(pm);
			plugin = pm.getPlugin(book.bookpath);
			Param param = new Param();
			filehandle = pm.open(plugin, book.bookpath);
			pm.get(plugin, PluginUtil.BOOK_TYPE, param);
			pm.closeAll();
			if(param.i == PluginUtil.BOOK_TYPE_COMIC){
				intent.setClass(context, CartoonReader.class);
			}else if(getReaderMode() == 1)
			{
				intent.setClass(context, BookContentView.class);
			}else if(getReaderMode() == 2)
			{
				intent.setClass(context, TextReader.class);
			}
			context.startActivity(intent);
		}	
		if (context instanceof BookShelfView) {
			
		}
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
	
	private int getReaderMode(){
		SharedPreferences sp = ((BookShelfView)context).getSharedPreferences("config", Activity.MODE_PRIVATE);
		int readerMode = sp.getInt("readerMode", 1);
		BookShelfView.readerMode = readerMode;
		return readerMode;
	}

	
}
