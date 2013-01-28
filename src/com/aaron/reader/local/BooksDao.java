/**
 * 
 */
package com.aaron.reader.local;

import java.util.List;

import com.aaron.reader.vo.Book;
import static com.aaron.reader.local.DBConfig.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;

/**
 * 
 * 管理所有有关数据库操作查询的方法
 * @author Aaron Lee
 *
 *  writed at 上午10:50:26 2013-1-21
 */
public class BooksDao {
	private Context context;
	private DBHelper helper;
	private SQLiteDatabase db;

	private static BooksDao dao;

	private BooksDao(Context contenxt) {
		this.context = contenxt;
	}

	/**
	 * 获得数据库连接类Dao唯一方法。本类已实现单例模式
	 * 
	 * @param context
	 * @return
	 * @date 2011-4-3上午09:59:27
	 * @author Lee Chok Kong
	 */
	public static BooksDao getDao(Context context) {
		if (dao == null) {
			dao = new BooksDao(context);
		}
		return dao;
	}
	
	/**
	 * 打开数据库并连接 ，本方法已先做判断
	 * 
	 * @throws SQLException
	 * @date 2011-4-3上午10:00:08
	 * @author Lee Chok Kong
	 */
	public void open() throws SQLException {
		helper = DBHelper.getHelper(context);
		if (db == null || !db.isOpen()) {
			db = helper.getWritableDatabase();
		}
	}
	
	/**
	 * 关闭数据库;
	 * 
	 * @throws SQLException
	 * @date 2011-4-3上午10:00:47
	 * @author Lee Chok Kong
	 */
	public void close() throws SQLException {
		if (helper != null) {
			helper.close();
		}
		dao = null;
	}
	
	/**
	 * 查看书本是否已经存在
	 * @param absolutePath
	 * @return
	 * @author writed by Aaron Lee
	 * writed at 下午9:19:22 2013-1-22
	 */
	public boolean isBookExits(String absolutePath) {
		boolean flag = false;
		Cursor cursor = null;
		try {
			if (!db.isOpen()){
				open();
			}
			cursor = db.query(DBConfig.TableBooks.TABLE_NAME, 
					new String[]{ BaseColumns._ID }, 
					DBConfig.TableBooks.COL_BOOKPATH + " = ? ", 
					new String[] {absolutePath}, 
					null, 
					null, 
					null,
					" 0 , 1");
			if (cursor.getCount() > 0) {
				flag  = true;
			}
		} catch (SQLException e) {
			flag  = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 
	 * @param books 要加入的实体书本对象
	 * @author writed by Aaron Lee
	 * writed at 下午7:57:57 2013-1-27
	 */
	public synchronized void insertBooks(List<Book> books) {
		try {
			if (!db.isOpen()) {
				open();
			} 
			ContentValues values;
			for (Book b : books) {
				values  = new ContentValues();
				values.put(TableBooks.COL_BOOKNAME, b.getBookname());
				values.put(TableBooks.COL_AUTHOR, b.getAuthor());
				values.put(TableBooks.COL_PUBLISHER, b.getPublisher());
				values.put(TableBooks.COL_SELLER, b.getSeller());
				values.put(TableBooks.COL_CATEGORY, b.getCategory());
				values.put(TableBooks.COL_BOOKTYPE, b.getBooktype());
				values.put(TableBooks.COL_LANGUAGE, b.getLanguage());
				values.put(TableBooks.COL_IMGPATH, b.getBookpath());
				values.put(TableBooks.COL_BOOKPATH, b.getBookpath());
				values.put(TableBooks.COL_DESCRIPT, b.getBookpath());
				values.put(TableBooks.COL_FILE_EX, b.getExt());
				db.insertOrThrow(TableBooks.TABLE_NAME, null, values);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} 
	
	public synchronized void insertSingleBook(Book b) {
		try {
			if (!db.isOpen()) {
				open();
			} 
			ContentValues values  = new ContentValues();
			values.put(TableBooks.COL_BOOKNAME, b.getBookname());
			values.put(TableBooks.COL_AUTHOR, b.getAuthor());
			values.put(TableBooks.COL_PUBLISHER, b.getPublisher());
			values.put(TableBooks.COL_SELLER, b.getSeller());
			values.put(TableBooks.COL_CATEGORY, b.getCategory());
			values.put(TableBooks.COL_BOOKTYPE, b.getBooktype());
			values.put(TableBooks.COL_LANGUAGE, b.getLanguage());
			values.put(TableBooks.COL_IMGPATH, b.getBookpath());
			values.put(TableBooks.COL_BOOKPATH, b.getBookpath());
			values.put(TableBooks.COL_DESCRIPT, b.getBookpath());
			values.put(TableBooks.COL_FILE_EX, b.getExt());
			db.insertOrThrow(TableBooks.TABLE_NAME, null, values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} 
}
