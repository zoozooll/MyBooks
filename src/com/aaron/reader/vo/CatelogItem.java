package com.aaron.reader.vo;

public class CatelogItem {

	private int id;//书签ID
	private String bookname;//书名
	private int bookid;//书ID
	private String title;//书签内容
	private int pageIndex;//页码索引
	private long date;//日期
	private int lastEnter;//
	private int percent;//百分比,全屏浏览用
	
	public CatelogItem() {
		super();
	}


	public CatelogItem(String title, int pageIndex) {
		super();
		this.title = title;
		this.pageIndex = pageIndex;
	}


	/**
	 * @return the id
	 * @author writed by  Aaron Lee
	 * writed at 上午11:34:51 2013-1-21
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:34:51
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the bookname
	 * @author writed by  Aaron Lee
	 * writed at 上午11:34:51 2013-1-21
	 */
	public String getBookname() {
		return bookname;
	}


	/**
	 * @param bookname the bookname to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:34:51
	 */
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}


	/**
	 * @return the bookid
	 * @author writed by  Aaron Lee
	 * writed at 上午11:34:51 2013-1-21
	 */
	public int getBookid() {
		return bookid;
	}


	/**
	 * @param bookid the bookid to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:34:51
	 */
	public void setBookid(int bookid) {
		this.bookid = bookid;
	}


	/**
	 * @return the title
	 * @author writed by  Aaron Lee
	 * writed at 上午11:34:51 2013-1-21
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:34:51
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the pageIndex
	 * @author writed by  Aaron Lee
	 * writed at 上午11:34:51 2013-1-21
	 */
	public int getPageIndex() {
		return pageIndex;
	}


	/**
	 * @param pageIndex the pageIndex to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:34:51
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}


	/**
	 * @return the date
	 * @author writed by  Aaron Lee
	 * writed at 上午11:34:51 2013-1-21
	 */
	public long getDate() {
		return date;
	}


	/**
	 * @param date the date to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:34:51
	 */
	public void setDate(long date) {
		this.date = date;
	}


	/**
	 * @return the lastEnter
	 * @author writed by  Aaron Lee
	 * writed at 上午11:34:51 2013-1-21
	 */
	public int getLastEnter() {
		return lastEnter;
	}


	/**
	 * @param lastEnter the lastEnter to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:34:51
	 */
	public void setLastEnter(int lastEnter) {
		this.lastEnter = lastEnter;
	}


	/**
	 * @return the percent
	 * @author writed by  Aaron Lee
	 * writed at 上午11:34:51 2013-1-21
	 */
	public int getPercent() {
		return percent;
	}


	/**
	 * @param percent the percent to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:34:51
	 */
	public void setPercent(int percent) {
		this.percent = percent;
	}

}
