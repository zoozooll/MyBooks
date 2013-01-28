package com.aaron.reader.local;

import android.provider.BaseColumns;

/**
 * 
 * 有关本软件数据库字段的常量库
 * @author Aaron Lee
 *  writed at 下午8:57:15 2013-1-20
 */
public class DBConfig {
	
	/**
	 * the table books's config
	 * @author Aaron Lee
	 *  writed at 下午9:25:18 2013-1-20
	 */
	public static class TableBooks implements BaseColumns {
		/* table name */
		public static final String TABLE_NAME="books";
		
		/* table column names */
		public static final String COL_BOOKNAME = "bookname";
		public static final String COL_AUTHOR = "author";
		public static final String COL_PUBLISHER = "publisher";
		public static final String COL_SELLER = "seller";
		public static final String COL_CATEGORY = "category";
		public static final String COL_BOOKTYPE = "booktype";
		public static final String COL_LANGUAGE = "language";
		public static final String COL_IMGPATH = "imgpath";
		public static final String COL_BOOKPATH = "bookpath";
		public static final String COL_DESCRIPT = "description";
		public static final String COL_BOOKMARK = "bookmark";
		public static final String COL_FINALPAGE = "finalpage";
		public static final String COL_FILE = "file";
		public static final String COL_FONT = "font";
		public static final String COL_CHARSET= "charset";
		public static final String COL_KEYWORD= "keyword";

		public static final String COL_FILE_EX = "ex";

		public static final String COL_CURINDEX = "cur_index";
	}
	
	/**
	 * BookMarks
	 * @author Aaron Lee
	 *  writed at 下午9:27:21 2013-1-20
	 */
	public static class TableBookMarks implements BaseColumns {
		/* table name */
		public static final String TABLE_NAME="marks";
		
		public static final String COL_BOOK_ID="bookid";
		public static final String COL_TITLE="title";
		public static final String COL_PAGEINDEX = "page";
		public static final String COL_DATE="date";
		public static final String COL_PERCENT = "percent";
		public static final String COL_LINEOFFSET = "line_offset";
	} 
	
	public static class TableHistory extends TableBookMarks {
		/* table name */
		public static final String TABLE_NAME="history";
	} 
}
