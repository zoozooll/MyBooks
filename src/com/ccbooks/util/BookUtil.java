package com.ccbooks.util;

import java.util.List;

import android.content.Context;

import com.ccbooks.dao.BooksDao;
import com.ccbooks.dao.DBHelper;
import com.ccbooks.vo.Book;

public class BookUtil {

	public static int countBooks;
	
	public static int getCount(Context context){
		if(countBooks==0){
			BooksDao dao= BooksDao.getDao(context);
			countBooks=dao.count(DBHelper.TABLE_BOOK);
		}
		return countBooks;
	}
	
	public static int bookSize(List<Book> books) {
		if (books != null) {
			return books.size();
		} else {
			return 0;
		}
	}
	public static String tansDecimal(int number){
		String decimal = null;
		if(number == 0){
			decimal = "0.00";
		}else if(number<100 && number>0){
			if(number < 10){
				decimal = "0.0"+number;
			}else{
				decimal = "0."+number;
			}
		}else if(number == 100){
			decimal = "1.00";
		}else if(number == 10000){
			decimal = "100.00";
		}else{
			int temp = number/100;
			int temp2 = number%100;
			if(temp2 == 0){
				decimal = temp+"."+"00";
			}else if(temp2 < 10){
				decimal = temp+".0"+temp2;
			}else{
				decimal = temp+"."+temp2;
			}
			
		}
		return decimal;
	}
}
