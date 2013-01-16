package com.ccbooks.vo;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Comparable<Book> ,Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int  id ;   
	 public String  bookname;	  
	 public String  author;	  
	 public String  published;	  
	 public String  publisher;	  
	 public String  seller;	  
	 public String  category;	  
	 public String  booktype;	  
	 public String  ratings;	  
	 public String  language;	  
	 public double  price;	  
	 public double  mprice;	  
	 public String  imgpath	;	  
	 public String  bookpath;	  
	 public String  description	;	  
	 public String  lastdowntime;	  
	 public String  downcount;	  
	 public String  filesize;	  
	 public String  addtime;	  
	 public String  regrade;
	 public String  charset;
	 public int file;
	 public String font;
	public int  fileLength;
	 public int curIndex;
	 public int nextEnter;
	 public String downloadPath;
	 public int downloadPrecent;

	@Override
	public int compareTo(Book arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "bookid:"+id+",bookname:"+bookname+",downloadPath:"+downloadPath;
	}

}
