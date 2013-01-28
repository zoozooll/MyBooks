package com.aaron.reader.vo;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Book {
	
	private static final long serialVersionUID = 1L;
	private int  id ;  
	/**书名*/
	private String  bookname;	  
	/**作者*/
	private String  author;	  
	/**出版社*/
	private String  publisher;	  
	/**发行书店*/
	private String  seller;	 
	/**类别*/
	private String  category;
	/**分类*/
	private String  booktype;	
	/**语言*/
	private String  language;	 
	/**单价*/
	private double  price;	
	/**优惠价*/
	private double  mprice;	  
	/**封面路径*/
	private String  imgpath	;	
	/**书本路径（本地）*/
	private String  bookpath;	
	/**简介*/
	private String  description;	 
	/**下载时间 以开始下载时间为准*/
	private String  lastdowntime;	  
	/**下载次数，服务器统计*/
	private String  downcount;	
	/**书本字节总数 会根据不同文件而变化*/
	private long  filesize;	 
	/**加入时间 以本地加入数据库时间为准*/
	private String  addtime;	
	/**编码方式 以本地设定的编码方式为准 ，原则自动获取编码格式*/
	private String  charset;
	/**字体大小 以当前设定值*/
	private String font;
	/**当前阅读到的字节索引 关闭书籍时候，或者离开时候保存的值*/
	private int curIndex;
	/**下载路径 在线阅读时候的 服务器的路径*/
	private String downloadPath;
	/**下载的百分比*/
	private int downloadPrecent;
	/**关键字*/
	private String keyword;
	/**文件扩展名*/
	private String ext;
	
	/**
	 * @return the id
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the bookname
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getBookname() {
		return bookname;
	}

	/**
	 * @param bookname the bookname to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	/**
	 * @return the author
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the publisher
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the seller
	 * @author by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getSeller() {
		return seller;
	}

	/**
	 * @param seller the seller to set
	 * @authorwrited by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setSeller(String seller) {
		this.seller = seller;
	}

	/**
	 * @return the category
	 * @authorwrited by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 * @authorwrited by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the booktype
	 * @author writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getBooktype() {
		return booktype;
	}

	/**
	 * @param booktype the booktype to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setBooktype(String booktype) {
		this.booktype = booktype;
	}

	/**
	 * @return the language
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the price
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the mprice
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public double getMprice() {
		return mprice;
	}

	/**
	 * @param mprice the mprice to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setMprice(double mprice) {
		this.mprice = mprice;
	}

	/**
	 * @return the imgpath
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getImgpath() {
		return imgpath;
	}

	/**
	 * @param imgpath the imgpath to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	/**
	 * @return the bookpath
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getBookpath() {
		return bookpath;
	}

	/**
	 * @param bookpath the bookpath to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setBookpath(String bookpath) {
		this.bookpath = bookpath;
	}

	/**
	 * @return the description
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the lastdowntime
	 * @author writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getLastdowntime() {
		return lastdowntime;
	}

	/**
	 * @param lastdowntime the lastdowntime to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setLastdowntime(String lastdowntime) {
		this.lastdowntime = lastdowntime;
	}

	/**
	 * @return the downcount
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getDowncount() {
		return downcount;
	}

	/**
	 * @param downcount the downcount to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setDowncount(String downcount) {
		this.downcount = downcount;
	}

	/**
	 * @return the filesize
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public long getFilesize() {
		return filesize;
	}

	/**
	 * @param filesize the filesize to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	/**
	 * @return the addtime
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getAddtime() {
		return addtime;
	}

	/**
	 * @param addtime the addtime to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	/**
	 * @return the charset
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset the charset to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return the font
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getFont() {
		return font;
	}

	/**
	 * @param font the font to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setFont(String font) {
		this.font = font;
	}

	/**
	 * @return the curIndex
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public int getCurIndex() {
		return curIndex;
	}

	/**
	 * @param curIndex the curIndex to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	/**
	 * @return the downloadPath
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public String getDownloadPath() {
		return downloadPath;
	}

	/**
	 * @param downloadPath the downloadPath to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	/**
	 * @return the downloadPrecent
	 * writed by  Aaron Lee
	 * writed at 上午11:14:16 2013-1-21
	 */
	public int getDownloadPrecent() {
		return downloadPrecent;
	}

	/**
	 * @param downloadPrecent the downloadPrecent to set
	 * writed by Aaron Lee
	 * writed at 2013-1-21 上午11:14:16
	 */
	public void setDownloadPrecent(int downloadPrecent) {
		this.downloadPrecent = downloadPrecent;
	}

	/**
	 * @return the keyword
	 * @author writed by  Aaron Lee
	 * writed at 上午11:19:19 2013-1-21
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @return the ext
	 * @author writed by  Aaron Lee
	 * writed at 下午10:29:01 2013-1-23
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * @param ext the ext to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-23 下午10:29:01
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}

	/**
	 * @param keyword the keyword to set
	 * @author writed by Aaron Lee
	 * writed at 2013-1-21 上午11:19:19
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	/** (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 * @author writed by Aaron Lee
	 * writed at 上午11:36:07 2013-1-21
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bookpath == null) ? 0 : bookpath.hashCode());
		return result;
	}

	/** (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @author writed by Aaron Lee
	 * writed at 上午11:36:07 2013-1-21
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (bookpath == null) {
			if (other.bookpath != null)
				return false;
		} else if (!bookpath.equals(other.bookpath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "bookid:"+id+",bookname:"+bookname+",downloadPath:"+downloadPath;
	}

}
