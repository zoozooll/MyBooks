package com.ccbooks.dao;

import com.ccbooks.vo.CatelogItem;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper helper;

	public static final String DATABASE_NAME = "books.db";
	public static final int DATEBASE_VERSION = 1;
	
	// books�и��ֶ���
	public static final String TABLE_BOOK = "ccb_book";
	public static final String TABLE_MARKS="ccb_marks";
	public static final String  TABLE_HISTORY = "ccb_history";
	
	public static final String ID = "_id";
	public static final String BOOKNAME = "bookname";
	public static final String AUTHOR = "author";
	public static final String PUBLISHER = "publisher";
	public static final String SELLER = "seller";
	public static final String CATEGORY = "category";
	public static final String BOOKTYPE = "booktype";
	public static final String LANGUAGE = "language";
	public static final String IMGPATH = "imgpath";
	public static final String BOOKPATH = "bookpath";
	public static final String DESCRIPT = "description";
	public static final String BOOKMARK = "bookmark";
	public static final String FINALPAGE = "finalpage";
	public static final String FILE = "file";
	public static final String FONT = "font";

	public static final String FILE_EX = "fileex";
	public static final String CURINDEX = "curIndex";
	public static final String NEXT_ENTER= "next_enter";
	public static final String CHARSET= "charset";
	
	private DBHelper(Context context, int versionCode ) {
		super(context, DATABASE_NAME, null, versionCode);
	}

	public static DBHelper getHelper(Context context) {
		if (helper == null) {
			/*-	数据库版本跟软件版本统一，无需每次都卸载再安装
			 * 
			 * */
			
			PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = null;
			try {
				pi = pm.getPackageInfo(context.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}  
	        int versionCode = pi.versionCode ; 
			helper = new DBHelper(context, versionCode);
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String createTableBooks = "CREATE TABLE "+TABLE_BOOK 
			+" ( "+ID+" integer primary key AUTOINCREMENT , "
			+BOOKNAME+" text NOT NULL, "
			+AUTHOR+" text DEFAULT NULL, "
			+PUBLISHER+" text DEFAULT NULL,  "
			+SELLER+" text DEFAULT NULL, "
			+CATEGORY+"	text DEFAULT NULL, "
			+BOOKTYPE+" text DEFAULT NULL, "
			+LANGUAGE+" text DEFAULT NULL, "
			+IMGPATH+" text DEFAULT NULL, "
			+BOOKPATH+" text DEFAULT NULL,  "
			+DESCRIPT+" text DEFAULT 	NULL,  "
			+BOOKMARK+" text,"
			+FINALPAGE+" text,"
			+FILE_EX+" text,"
			+CURINDEX+" integer,"
			+FILE+" integer,"
			+FONT+" text,"
			+NEXT_ENTER+" integer," +
			CHARSET+" text) ";
		String createTableMark = "CREATE TABLE "+TABLE_MARKS+"("
			+ID+" integer primary key AUTOINCREMENT ,"
			+CatelogItem.TITLE+" text DEFAULT NULL,"
			+CatelogItem.PAGEINDEX+" numeric," +
			CatelogItem.DATE+" integer," +
			CatelogItem.BOOK_NAME+" text," +
			CatelogItem.LAST_ENTER+" integer," +
			CatelogItem.PERCENT+" integer," +
			CatelogItem.CURLINE+" integer," +
			CatelogItem.FILE+" integer," +
			CatelogItem.FONT+" text," +
			CatelogItem.LINEOFFSET+" integer)";
		String createTableHistory = "CREATE TABLE "+TABLE_HISTORY+
		"("+ID+" integer primary key AUTOINCREMENT ," +
		BOOKNAME+" text NOT NULL," +
		CatelogItem.PAGEINDEX+" numeric," +
		CatelogItem.TITLE+" text DEFAULT NULL," +
		CatelogItem.DATE+" integer)";
		db.execSQL(createTableBooks);
		db.execSQL(createTableMark);
		db.execSQL(createTableHistory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists "+TABLE_BOOK);
		db.execSQL("drop table if exists "+TABLE_MARKS);
		db.execSQL("drop table if exists "+TABLE_HISTORY);
		onCreate(db);
	}

}
