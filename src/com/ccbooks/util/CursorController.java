package com.ccbooks.util;

import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

import com.ccbooks.bo.FileBo;
import com.ccbooks.view.BookContentView;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.ccbooks.core.BookCore;

public class CursorController {
	public int cursor = 0;
	public int lineCount;	//	总行数；
	public BookContentView bcv;
	
	private BookCore bc = null;

	/**是否文件靠头*/
	public static  boolean isFirstPage;
	
	public CursorController(BookContentView bcv,BookCore bc){
		this.bcv = bcv;
		this.bc = bc;
	}
	
	
	/**取得下一页内容
	 * @throws IOException */
	public ArrayList<String> nextPage() {
		ArrayList<String> content = null;
//		if(FileBo.BOOKTYPE_TXT.equals(bcv.book.booktype)){
			try{
				if (bcv.markPoint<getFileLength() && bcv.bc.getNextIndexAt()<getFileLength()){
					content = bc.getNextPage();
				}
				
			}catch (NullPointerException e) {
				e.printStackTrace();
				content = null;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	/*	}else if(FileBo.BOOKTYPE_PDB.equals(bcv.book.booktype)){
			
		}else if(FileBo.BOOKTYPE_CHM.equals(bcv.book.booktype)){
			
		}else if(FileBo.BOOKTYPE_HTML.equals(bcv.book.booktype)){
			
		}else if(FileBo.BOOKTYPE_PDF.equals(bcv.book.booktype)){
			
		}*/
		return content;
	}
	
	/**取得上一页内容
	 * @throws IOException */
	public ArrayList<String> previousPage() {
		ArrayList<String> content = null;
		bcv.isFileEnd = false;
//		if(FileBo.BOOKTYPE_TXT.equals(bcv.book.booktype)){	
			try {
				if (bcv.markPoint>0){
					content = bc.getPrevPage();
				}
				
			} catch (NullPointerException e){
				e.printStackTrace();
				content = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	/*	}else if(FileBo.BOOKTYPE_PDB.equals(bcv.book.booktype)){
		
		}else if(FileBo.BOOKTYPE_CHM.equals(bcv.book.booktype)){
			
		}else if(FileBo.BOOKTYPE_HTML.equals(bcv.book.booktype)){
			
		}else if(FileBo.BOOKTYPE_PDF.equals(bcv.book.booktype)){
			
		}*/
		
			return content;
		} 
	public long getFileLength(){
		long fileLength = 0;
		Plugin plugin = bcv.bc.pm.getDefaultPlugin();
		fileLength = bcv.bc.pm.length(plugin, bcv.bc.pm.filehandle);
		
		if(fileLength > 0) return fileLength;
		else return 0;
	}
}
	
		
	

