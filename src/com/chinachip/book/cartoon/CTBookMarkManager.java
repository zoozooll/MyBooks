package com.chinachip.book.cartoon;

import java.sql.Date;

/*
public class BookMarkManager {
	private ArrayList<BookMarkInfo> bkmks;
	
	public BookMarkManager(){
		bkmks = new ArrayList<BookMarkInfo>();
	}
	
	public int addBkmk(BookMarkInfo bki){
		bkmks.add(bki);
		return bkmks.indexOf(bki);
	}
	
	public BookMarkInfo getBkmk(int id){
		return bkmks.get(id);
	}
	
	public boolean delBkmk(int id){
		bkmks.remove(id);
		return true;
	}	
	
	public int getBkmkNum(){
		return bkmks.size();
	}
}
*/
import java.util.List;

import com.ccbooks.bo.BookMarkBo;
import com.ccbooks.view.CartoonReader;

import com.ccbooks.vo.CatelogItem;

public class CTBookMarkManager {
	private List<CatelogItem> bkmks;
	private CartoonReader actParent;
	private BookMarkBo bmo;
	
	public CTBookMarkManager(CartoonReader trObj){
		actParent = trObj;
		bmo = new BookMarkBo(actParent);
		bkmks= getBkmkList();
	}
	
	public int addBkmk(CatelogItem bki){
		bmo.addMarksByDb(bki.bookname, bki.pageIndex,
				bki.title,0, bki.percent, bki.curLine, bki.offset,bki.file);
		bkmks = getBkmkList();
		return 0;
	}
	
	public CatelogItem getBkmk(int id){
		CatelogItem bkmkItem = bkmks.get(id);
		return bkmkItem;
	}
	
	public boolean delBkmk(int id){
		bmo.delMarkByDb(actParent.book.bookname, id);
		bkmks = getBkmkList();
		return true;
	}	
	
	public List<CatelogItem> getBkmkList() {
		List<CatelogItem> bkmks = bmo.readMarksByDb(actParent.book.bookname);
		return bkmks;
	}
	
	public int getBkmkNum(){
		return bkmks.size();
	}
}
