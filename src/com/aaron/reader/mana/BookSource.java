/**
 * 
 */
package com.aaron.reader.mana;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.aaron.reader.local.BooksDao;
import com.aaron.reader.util.BookType;
import com.aaron.reader.util.FileUtil;
import com.aaron.reader.vo.Book;

import android.content.ContentValues;
import android.content.Context;

/**
 * 
 * 书本来源管理类
 * @author Aaron Lee
 *  writed at 上午10:57:49 2013-1-21
 */
public class BookSource {
	
	private Context context;
	
	BookSource(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 刷新书本列表的数据，并获得list
	 * @param strPath 扫描的目录路
	 * @return
	 * @author writed by Aaron Lee
	 * writed at 下午12:02:19 2013-1-21
	 */
	public void refreshSdFileList(String strPath) {
		List<Book> books = null;
		Book b = null;
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		BooksDao dao = BooksDao.getDao(context);
		if((files == null))
       {
    	   return ;
       } else {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					refreshSdFileList(files[i].getAbsolutePath());
				} else if(files[i].isFile()) {
					final String strFileName = files[i].getAbsolutePath();
					String ex=FileUtil.getFileEx(strFileName);
					if(BookType.EXT_TXT.equals(ex)){
						if (!dao.isBookExits(strFileName))  {
							if (books == null) {
								books = new ArrayList<Book>();
							} else  {
								books.clear();
							}
							b = new Book();
							final String bookname = FileUtil.getFileNa(strFileName);
							final String imagePath = files[i].getParent() +"/"+bookname+".jpg";
							final long bookLength = files[i].length();
							final String bookExt = FileUtil.getFileEx(strFileName).toLowerCase();
							b.setBookname(bookname);
							b.setFilesize(bookLength);
							b.setExt(bookExt);
							if (new File(imagePath).exists()) {
								b.setImgpath(imagePath);
							}
						}
					} // end if is txt file
				}
			} //end for loop
		} // end file != null	
		
		if (books != null) {
			dao.insertBooks(books);
		}
		
	}
	
	
	
}
