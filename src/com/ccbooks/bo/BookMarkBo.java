package com.ccbooks.bo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.ccbooks.dao.BooksDao;
import com.ccbooks.dao.DBHelper;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.vo.CatelogItem;
/**
 * 
 * @author Administrator
 * 
 * 2011-3-27 下午09:57:06
 */
public class BookMarkBo {

	private Context context;
	public static List<CatelogItem> marks;
	private BooksDao dao;
	
	public BookMarkBo(Context context){
		this.context=context;
		dao = BooksDao.getDao(context);
	}

	/**
	 * 从数据库里面获得信息
	 * @param bookname 书名
	 * @return List<CatelogItem> 目录列表
	 * 2011-3-25下午04:49:13
	 */
	public List<CatelogItem> readMarksByDb(String bookname) {
		
		Cursor cursor=null;
		marks = new ArrayList<CatelogItem>();
		CatelogItem item = null;
		try{
			cursor = dao.select(true,DBHelper.TABLE_MARKS,null,CatelogItem.BOOK_NAME+"=?",new String[]{String.valueOf(bookname)},CatelogItem.DATE,null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				item = new CatelogItem();
				item.id = cursor.getInt(0);
				item.bookname = cursor.getString(cursor
						.getColumnIndexOrThrow(CatelogItem.BOOK_NAME));
				item.pageIndex = cursor.getInt(cursor
						.getColumnIndexOrThrow(CatelogItem.PAGEINDEX));
				item.title = cursor.getString(cursor
						.getColumnIndexOrThrow(CatelogItem.TITLE));
				item.date = cursor.getLong(cursor
						.getColumnIndexOrThrow(CatelogItem.DATE));
				item.lastEnter = cursor.getInt(cursor
						.getColumnIndexOrThrow(CatelogItem.LAST_ENTER));
				item.percent = cursor.getInt(cursor
						.getColumnIndexOrThrow(CatelogItem.PERCENT));
				item.curLine = cursor.getInt(cursor
						.getColumnIndexOrThrow(CatelogItem.CURLINE));
				item.offset = cursor.getInt(cursor
						.getColumnIndexOrThrow(CatelogItem.LINEOFFSET));
				item.file = cursor.getInt(cursor
						.getColumnIndexOrThrow(CatelogItem.FILE));
				marks.add(item);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				cursor.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return marks;
	}

	/**
	 * 添加书签
	 * @param bookname
	 * @param pageIndex
	 * @param title
	 * @param lastEnter
	 * @param percent
	 * @param curline
	 * @param lineOffset
	 * @param file
	 * @return void
	 * @since 2011-3-25下午04:44:02
	 */
	public void addMarksByDb(String bookname,int pageIndex,String title,int lastEnter, int percent, int curline, int lineOffset,int file){
		Cursor cursor = null;
		try{
			cursor = dao.select(false, DBHelper.TABLE_MARKS,
					new String[] { String.valueOf(DBHelper.ID) },
					CatelogItem.PAGEINDEX + "=? and "
						+ CatelogItem.BOOK_NAME +" = ? and "+CatelogItem.FILE+" = ?",
					new String[] { String.valueOf(pageIndex), bookname, String.valueOf(file)}, null, "0,1");
			if (!cursor.isAfterLast()){
				
			} else {
				ContentValues cv = new ContentValues();
				cv.put(CatelogItem.PAGEINDEX, pageIndex);
				cv.put(CatelogItem.BOOK_NAME, bookname);
				cv.put(CatelogItem.FILE, file);
				cv.put(CatelogItem.TITLE,title);
				cv.put(CatelogItem.DATE, System.currentTimeMillis()/1000);
				//Log.i("zkli", "timestamp:"+new Date().getTime());
				//cv.put(CatelogItem.LAST_ENTER, lastEnter);
				cv.put(CatelogItem.PERCENT, percent);
				cv.put(CatelogItem.CURLINE, curline);
				cv.put(CatelogItem.LINEOFFSET, lineOffset);
				try{
					this.dao.insert(DBHelper.TABLE_MARKS, cv);
				}catch(SQLException e){
					e.printStackTrace();
				}
				marks=readMarksByDb(bookname);
			}
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			try{
				cursor.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	/**
	 * 删除书签
	 * @param bookname 书名（不是路径名）
	 * @param id 书id
	 * @return void
	 * @since 2011-3-25下午04:43:39
	 */
	public void delMarkByDb(String bookname,int id){
		try {
			dao.delete(DBHelper.TABLE_MARKS, id);
		} catch (SQLException e) {
		}
		marks=readMarksByDb(bookname);
	}
	
	/**
	 * 判断书签
	 * @param bcv
	 * @return void
	 * 2011-3-25下午04:39:23
	 */
	public static void isMark(BookContentView bcv){
		bcv.mark = null;
		BooksDao dao = null;
		Cursor cursor = null;
		String sql = null;
		try {
			dao = BooksDao.getDao(bcv);
			sql = sourceCodeQuery(bcv.book.bookname, (int)(bcv.markPoint), (int)(bcv.nextPoint),(int)(bcv.file));
			cursor = dao.exec(sql);
			
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				bcv.mark = new CatelogItem();
				bcv.mark.id = cursor.getInt(0);
				bcv.mark.file = cursor.getInt(cursor.getColumnIndexOrThrow(CatelogItem.FILE));
				bcv.mark.pageIndex = cursor.getInt(cursor
						.getColumnIndexOrThrow(CatelogItem.PAGEINDEX));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try{
				cursor.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		try {
			if (bcv.mark != null) {
				bcv.bookpagebg.rightShowPage.myBookMarkOnClick(true);
			} else {
				bcv.bookpagebg.rightShowPage.myBookMarkOnClick(false);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		sql = sourceCodeQuery(bcv.book.bookname, (int)(bcv.preventPoint), (int)(bcv.markPoint),(int)(bcv.file));
		try {
			cursor = dao.exec(sql);
			if (!cursor.isAfterLast()) {
				bcv.bookpagebg.leftMaskPage.myBookMarkOnClick(true);
			}else{
				bcv.bookpagebg.leftMaskPage.myBookMarkOnClick(false);
			}
		} catch (SQLException e){
			e.printStackTrace();
		}  catch(NullPointerException e){
			e.printStackTrace();
		}finally {
			try{
				cursor.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		sql = sourceCodeQuery(bcv.book.bookname, (int)(bcv.nextPoint), (int)(bcv.nextEnd),(int)(bcv.file));
		cursor = dao.exec(sql);
		try {
			if (bcv.mark != null) {
				bcv.bookpagebg.rightShowPage.myBookMarkOnClick(true);
			} else {
				bcv.bookpagebg.rightShowPage.myBookMarkOnClick(false);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		try{
			cursor.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**构造书签的sql语句
	 * 
	 * @param bookName 书名
	 * @param startPoint 开始点，通常为一页的开头索引
	 * @param endPoint 结束点 ，通常为一页书的结尾
	 * @param file 章节
	 * @return
	 * @return String
	 * 2011-3-25下午05:03:54
	 */
	public static String sourceCodeQuery(String bookName,int startPoint,int endPoint,int file){
		bookName = bookName.replace("'", "''");
		String sql = "select * from "
			+DBHelper.TABLE_MARKS+" where "
			+CatelogItem.BOOK_NAME+"='"+bookName+"' and "
			+CatelogItem.FILE+"="+file ;
		if (startPoint<0){
			sql += " and page>="+ 0 ;
		} else {
			sql += " and page>="+startPoint;
		}
		if (endPoint>0){
			sql += " and page<"+endPoint;
		}
		sql +=" limit 0,1";
		return sql;
	}
}
