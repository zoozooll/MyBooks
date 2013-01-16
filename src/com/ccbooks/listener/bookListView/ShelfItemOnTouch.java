/**
 * 
 */
package com.ccbooks.listener.bookListView;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.FileBo;
import com.ccbooks.dao.BooksDao;
import com.ccbooks.dao.DBHelper;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.CartoonReader;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;
import com.ccbooks.vo.Book;
import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginUtil;
import com.kang.test.TestClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;

/**
 * 打开书本时候的特效;
 * @date 2011-5-17 下午04:09:51
 * @author Lee Chok Kong
 */
public class ShelfItemOnTouch implements OnTouchListener {

	private Context context;
	private Book book;
	private PluginMgr pm;
	private Plugin plugin;
	private Drawable da;
	private int itemIndex;
	private int filehandle;
	
	public ShelfItemOnTouch(Context context ,Book book,Drawable da ,int itemIndex){
		this.context = context;
		this.book = book;
		this.da = da;
		this.itemIndex = itemIndex;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if (event.getAction() == MotionEvent.ACTION_UP && context instanceof BookShelfView) {
			//判断是否有锁住监听
			if (!((BookShelfView)context).isListenerEnable()) {
				return false;
			}
			
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
				TestClass.getMemoryInfo(context);
				((BookShelfView)context).sfa.selectItem = itemIndex;
				//((BookShelfView)context).sfa.notifyDataSetChanged();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("bookId", book.id);
				
				intent.putExtras(bundle);
				pm = new PluginMgr();
		        PluginUtil.PluginInit(pm);
				plugin = pm.getPlugin(book.bookpath);
				filehandle = pm.open(plugin, book.bookpath);
				Param param = new Param();
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
				
				float itemX = event.getRawX() - event.getX();
				float itemY = event.getRawY() - event.getY() -10;
				if (context instanceof BookShelfView) {
					((BookShelfView)context).flOpenBookAnim.showItemView((int)itemX, (int)itemY , da);
				}
				//context.startActivity(intent);
				((BookShelfView) context).transItemsWithAnimation(intent,
						R.anim.push_left_in,
						R.anim.push_left_out, 900, false);	
			}	
		}
		
		
		return false;
	}
	
	
	private int getReaderMode(){
		SharedPreferences sp = ((BookShelfView)context).getSharedPreferences("config", Activity.MODE_PRIVATE);
		int readerMode = sp.getInt("readerMode", 1);
		BookShelfView.readerMode = readerMode;
		return readerMode;
	}
	
	public void deletingFile(){
		try {
			FileBo.delFile(book.bookpath);
			BooksDao dao= BooksDao.getDao(context);
			dao.delete(DBHelper.TABLE_BOOK,book.id);
			BooksBo.books.remove(book);
			((BookShelfView)context).sfa.notifyDataSetChanged();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
