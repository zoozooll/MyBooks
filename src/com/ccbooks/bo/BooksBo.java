package com.ccbooks.bo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.ccbooks.dao.BooksDao;
import com.ccbooks.dao.DBHelper;
import com.ccbooks.myUtil.StringTool;
import com.ccbooks.util.BookUtil;
import com.ccbooks.util.StringConfig;
import com.ccbooks.vo.Book;
import com.ccbooks.vo.CatelogItem;

public class BooksBo implements Runnable{

	public boolean reflesh;
	public Context context;
	public static List<Book> books;
	public String limit;
	
	public BooksBo(Context context,boolean reflesh){
		this.reflesh=reflesh;
		this.context=context;
	}
	/**
	 * 
	 * @return
	 * @date 2011-4-22下午05:25:19
	 * @author Lee Chok Kong
	 */
	public  List<Book> firstShellBooks() {
		if (books == null || books.size() == 0) {
			searchBooks(context,null);
		} else if (reflesh) {
			books.clear();
			searchBooks(context,null);
		}
		return books;
	}
	
	/**
	 * 
	 * @return
	 * @date 2011-4-22下午05:25:03
	 * @author Lee Chok Kong
	 */
	public  List<Book> firstBookList(){
		if (books == null || books.size() == 0) {
			searchBooks(context,null);
		} else if (reflesh) {
			searchBooks(context,null);
		}
		return books;
	}
	
    public void updateBooks() throws Exception{
   	 if(BookUtil.bookSize(books)<BookUtil.getCount(context)){
   		 	limit=BookUtil.bookSize(books)+",20";
   		 	searchBooks(context,limit);
   		    updateBooks();
     }
   }

	public List<Book> searchBooks(Context context,String limit) {
		
		BooksDao dao=null;
		Cursor cursor=null;
		List<Book> list=null;
		try {
			dao = BooksDao.getDao(context);
			cursor = dao.select(DBHelper.TABLE_BOOK,limit);
			// cursor.moveToFirst();
		
			Book book;
			list = new ArrayList<Book>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				book = new Book();
				book.id = cursor.getInt(0);
				book.bookname = cursor.getString(cursor
						.getColumnIndex(DBHelper.BOOKNAME));
				book.author = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.AUTHOR));
				book.bookpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKPATH));
				book.imgpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.IMGPATH));
				book.booktype = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKTYPE));
				book.charset = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.CHARSET));
				book.file = cursor.getInt(cursor
						.getColumnIndexOrThrow(DBHelper.FILE));
				book.font = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.FONT));
				list.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(cursor != null)
					cursor.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		if( list!=null && list.size()>0){
			if(books==null){
				books=new ArrayList<Book>();
			}
			books.addAll(list);
		}
		return list;
	}
	
	/**
	 * 模糊匹配搜索条件
	 * @param bookname 书名
	 * @param author 作者
	 * @param type 类别
	 * @return 返回书列表
	 * @date 2011-4-22下午06:01:40
	 * @author Lee Chok Kong
	 */
	public List<Book> searchBooks(String bookname, String author, String type){
		BooksDao dao=null;
		Cursor cursor=null;
		List<Book> list=null;
		try {
			dao = BooksDao.getDao(context);
			StringBuffer selectionBuffer = new StringBuffer(" 1=1 ");
			List<String> selectionArgsList = new ArrayList<String>();
			if (bookname!=null && !bookname.equals("")){
				selectionBuffer.append(" and ")
					.append(DBHelper.BOOKNAME)
					.append(" like ? ");
				selectionArgsList.add("%"+bookname+"%");
			}
			if (author!=null && !author.equals("")) {
				selectionBuffer.append(" and ")
					.append(DBHelper.AUTHOR)
					.append(" like ? ");
				selectionArgsList.add("%"+author+"%");
			}
			if (author!=null && !type.equals("")) {
				selectionBuffer.append(" and ")
					.append(DBHelper.AUTHOR)
					.append(" like ? ");
				selectionArgsList.add("%"+type+"%");
			}
			if (selectionArgsList.size()==0){
				return null;
			}
			String orderBy = null;
			cursor = dao.select(true, DBHelper.TABLE_BOOK, null, selectionBuffer.toString(), selectionArgsList.toArray(new String[selectionArgsList.size()]), orderBy, null);
			Book book;
			list = new ArrayList<Book>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				book = new Book();
				book.id = cursor.getInt(0);
				book.bookname = cursor.getString(cursor
						.getColumnIndex(DBHelper.BOOKNAME));
				book.author = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.AUTHOR));
				book.bookpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKPATH));
				book.imgpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.IMGPATH));
				book.booktype = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKTYPE));
				book.charset = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.CHARSET));
				book.file = cursor.getInt(cursor
						.getColumnIndexOrThrow(DBHelper.FILE));
				book.font = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.FONT));
				list.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				cursor.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/** 根据id查找单项;
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @date 2011-4-22下午05:25:33
	 * @author Lee Chok Kong
	 */

	
	public Book getOne(int id) throws SQLException,NullPointerException {
		BooksDao dao=null;
		Book book=null;
		dao = BooksDao.getDao(context);
		String where = DBHelper.ID + "=?";
		String[] selectionArgs = { Integer.toString(id) };
		Cursor cursor = null;
		try{
			cursor = dao.select(true,DBHelper.TABLE_BOOK, null, where, selectionArgs, null, "0,1");
			
			cursor.moveToFirst();
			if ( !cursor.isAfterLast()) {
				book = new Book();
				book.id = cursor.getInt(0);
				book.bookname = cursor.getString(cursor
						.getColumnIndex(DBHelper.BOOKNAME));
				book.author = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.AUTHOR));
				book.bookpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKPATH));
				book.imgpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.IMGPATH));
				book.charset = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.CHARSET));
				book.booktype = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKTYPE));
				book.curIndex = cursor.getInt(cursor.
						getColumnIndexOrThrow(DBHelper.CURINDEX));
				book.file = cursor.getInt(cursor.
						getColumnIndexOrThrow(DBHelper.FILE));
				book.font = cursor.getString(cursor.
						getColumnIndexOrThrow(DBHelper.FONT));
			}else{
				dao.delete(DBHelper.TABLE_BOOK,id);
			}
			if(book == null ){
				throw new  NullPointerException("找不到文件");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			if (cursor!=null){
				cursor.close();
			}
		}

		return book;
	}
	
	public void addCurIndex(int id,int curIndex,int nextEnter,int file){
		BooksDao dao=null;
		Book book=null;
		try {
			dao = BooksDao.getDao(context);
			ContentValues cv = new ContentValues();
			cv.put(DBHelper.CURINDEX, curIndex);
			cv.put(DBHelper.NEXT_ENTER,nextEnter);
			cv.put(DBHelper.FILE, file);
			dao.update(DBHelper.TABLE_BOOK, id, cv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**添加历史记录*/
	public void saveOrUpdateHistory(String bookname,int page,String title){
		BooksDao dao=null;
		Cursor cursor = null;
		try {
			dao = BooksDao.getDao(context);
			cursor = dao.select(false, DBHelper.TABLE_HISTORY,
					new String[] { CatelogItem.ID }, CatelogItem.BOOK_NAME+" = ? ",
					new String[] { bookname }, null, "0,1"); //查询数据库是否存在该本书的历史记录；
			if (cursor.isAfterLast()) {	//如果不存在历史记录，则增加一条
				ContentValues cv = new ContentValues();
				cv.put("bookname", bookname);
				cv.put(CatelogItem.PAGEINDEX, page);
				cv.put(CatelogItem.TITLE, title);
				cv.put(CatelogItem.DATE, System.currentTimeMillis()/1000);
				dao.insert(DBHelper.TABLE_HISTORY, cv);
			} else {	//如果已经存在有该记录，则修改该记录内容;
				ContentValues cv = new ContentValues();
				cv.put(CatelogItem.PAGEINDEX, page);
				cv.put(CatelogItem.TITLE,title);
				cv.put(CatelogItem.DATE, System.currentTimeMillis()/1000);
				dao.update(DBHelper.TABLE_HISTORY, CatelogItem.BOOK_NAME, bookname, cv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor!=null){
				try{
					cursor.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**查询历史记录*/
	public List<CatelogItem> getHistoryList(){
		BooksDao dao=null;
		Cursor cursor=null;
		List<CatelogItem> list=null;
		try {
			dao = BooksDao.getDao(context);
			cursor = dao.select(DBHelper.TABLE_HISTORY,limit);
			CatelogItem history = null;
			list = new ArrayList<CatelogItem>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				history = new CatelogItem();
				history.id = cursor.getInt(0);
				history.bookname = cursor.getString(cursor.getColumnIndex(CatelogItem.BOOK_NAME));
				history.pageIndex = cursor.getInt(cursor.getColumnIndexOrThrow(CatelogItem.PAGEINDEX));
				history.title = cursor.getString(cursor.getColumnIndexOrThrow(CatelogItem.TITLE));
				history.date = cursor.getLong(cursor.getColumnIndexOrThrow(CatelogItem.DATE));
				list.add(history);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				cursor.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return list;
		
	} 
	
	/**查询历史记录书列表*/
	public List<Book> getHistoryBookList(){
		BooksDao dao=null;
		Cursor cursor=null;
		List<Book> list=null;
		try {
			dao = BooksDao.getDao(context);
			cursor = dao.exec("select b."
					+DBHelper.ID+",b."+DBHelper.BOOKNAME
					+",b."+DBHelper.AUTHOR
					+",b."+DBHelper.BOOKPATH
					+",b."+DBHelper.IMGPATH+",b."
					+DBHelper.BOOKTYPE
					+" from "+DBHelper.TABLE_BOOK+" b,"
					+DBHelper.TABLE_HISTORY+" h "
					+" where b.bookname=h.bookname"
					+" order by h."+CatelogItem.DATE +" desc");
			// cursor.moveToFirst();
			Book book;
			list = new ArrayList<Book>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				book = new Book();
				book.id = cursor.getInt(0);
				book.bookname = cursor.getString(cursor
						.getColumnIndex(DBHelper.BOOKNAME));
				book.author = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.AUTHOR));
				book.bookpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKPATH));
				book.imgpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.IMGPATH));
				book.booktype = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKTYPE));
			
				list.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				cursor.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 删除一本书的历史记录
	 * @param bookname
	 * @date 2011-4-21下午05:36:53
	 * @author Lee Chok Kong
	 */
	public void deleteHistoryBookItem(String bookname){
		bookname = bookname.replace("'", "''");
		BooksDao dao = BooksDao.getDao(context);
		dao.delete(DBHelper.TABLE_HISTORY, CatelogItem.BOOK_NAME +" = ? ", new String[]{bookname});
	}
	
	
	/**修改这本书的查看编码*/
	public void updateChartset(int id,String chartset){
		BooksDao dao=null;
		try {
			dao = BooksDao.getDao(context);
			ContentValues cv = new ContentValues();
			cv.put(DBHelper.CHARSET, chartset);
			dao.update(DBHelper.TABLE_BOOK, id, cv);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**修改这本书的字体*/
	public void updateFont(int id,String font){
		BooksDao dao=null;
		try {
			dao = BooksDao.getDao(context);
			ContentValues cv = new ContentValues();
			cv.put(DBHelper.FONT, font);
			dao.update(DBHelper.TABLE_BOOK, id, cv);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
  	public synchronized void  insertDB(Book bookInfo){
  		BooksDao dao = null;
  		Cursor cursor = null;
  		try {
			dao = BooksDao.getDao(context);
			ContentValues cv = new ContentValues();
			cv.put(DBHelper.BOOKNAME, bookInfo.bookname);
			cv.put(DBHelper.BOOKPATH,bookInfo.bookpath);
			cv.put(DBHelper.BOOKTYPE, bookInfo.booktype);
			dao.insert(DBHelper.TABLE_BOOK,cv);
			cursor = dao.exec("select last_insert_rowid() from "+DBHelper.TABLE_BOOK +" limit 0,1");
			cursor.moveToFirst();
			int id = cursor.getInt(0);
			bookInfo.id = id;
			if(BooksBo.books != null)
				BooksBo.books.add(bookInfo);
  		} catch (SQLException e) {
  			e.printStackTrace();
  		} finally {
  			try{
  				if (cursor!=null)
  				cursor.close();
  			}catch (SQLException e) {
  				
  			}
  			
  		}
	}

  	public void deletFile(int position){
		try {
			FileBo.delFile(BooksBo.books.get(position).bookpath);
			BooksDao dao= BooksDao.getDao(context);
			dao.delete(DBHelper.TABLE_BOOK,BooksBo.books.get(position).id);
			BooksBo.books.remove(BooksBo.books.get(position));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			updateBooks();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
