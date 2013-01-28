package com.aaron.reader.local;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import static  com.aaron.reader.local.DBConfig.*;

class DBHelper extends SQLiteOpenHelper {

	private static DBHelper helper;

	static final String DATABASE_NAME = "books.db";
	 static final int DATEBASE_VERSION = 1;
	
	private DBHelper(Context context, int versionCode ) {
		super(context, DATABASE_NAME, null, versionCode);
	}

	static DBHelper getHelper(Context context) {
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
		
		final String createTableBooks = "CREATE TABLE "+TableBooks.TABLE_NAME 
			+" ( "+TableBooks._ID+" integer primary key AUTOINCREMENT , "
			+TableBooks.COL_BOOKNAME+" text NOT NULL, "
			+TableBooks.COL_AUTHOR+" text DEFAULT NULL, "
			+TableBooks.COL_PUBLISHER+" text DEFAULT NULL,  "
			+TableBooks.COL_SELLER+" text DEFAULT NULL, "
			+TableBooks.COL_CATEGORY+"	text DEFAULT NULL, "
			+TableBooks.COL_BOOKTYPE+" text DEFAULT NULL, "
			+TableBooks.COL_LANGUAGE+" text DEFAULT NULL, "
			+TableBooks.COL_IMGPATH+" text DEFAULT NULL, "
			+TableBooks.COL_BOOKPATH+" text DEFAULT NULL,  "
			+TableBooks.COL_DESCRIPT+" text DEFAULT 	NULL,  "
			+TableBooks.COL_BOOKMARK+" text,"
			+TableBooks.COL_FINALPAGE+" text,"
			+TableBooks.COL_FILE_EX+" text,"
			+TableBooks.COL_CURINDEX+" integer,"
			+TableBooks.COL_FONT+" text,"
			+TableBooks.COL_CHARSET+" text) ";
		final String createTableMark = "CREATE TABLE "+TableBookMarks.TABLE_NAME+"("
			+TableBookMarks._ID+" integer primary key AUTOINCREMENT ,"
			+TableBookMarks.COL_TITLE+" text DEFAULT NULL,"
			+TableBookMarks.COL_PAGEINDEX+" numeric," +
			TableBookMarks.COL_DATE+" integer," +
			TableBookMarks.COL_PERCENT+" integer," +
			TableBookMarks.COL_LINEOFFSET+" integer)";
		
		final String createTableHistory = "CREATE TABLE "+TableHistory.TABLE_NAME+
		"("+TableHistory._ID+" integer primary key AUTOINCREMENT ," +
		TableHistory.COL_PAGEINDEX+" numeric," +
		TableHistory.COL_DATE+" integer)";
		
		db.execSQL(createTableBooks);
		db.execSQL(createTableMark);
		db.execSQL(createTableHistory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists "+TableBooks.TABLE_NAME);
		db.execSQL("drop table if exists "+TableBookMarks.TABLE_NAME);
		db.execSQL("drop table if exists "+TableHistory.TABLE_NAME);
		onCreate(db);
	}

}
