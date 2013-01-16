package com.ccbooks.bo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ccbooks.myUtil.StringTool;
import com.ccbooks.util.ContentManager;
import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.tree.Node;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class BookContentThread implements Runnable {

	public BookContentView bcv;
	public int what;
	public List<String> str;
	public List<String> strPrev;
	public List<String> strNext;
	public long skipNumber;
	
	/**第一次运行，竖屏*/
	public static final int FIRST_VIRI = 1;
	/**第一次运行，横屏*/
	public static final int FIRST_HORI = 2;
	/**中间运行，竖屏*/
	public static final int THREAD_VIRI = 7;
	/**中间运行，横屏*/
	public static final int THREAD_HORI = 8;
	/**结尾运行，竖屏*/
	public static final int END_VIRI = 9;
	/**结尾运行，横屏*/
	public static final int END_HORI = 10;
	
	public BookContentThread(BookContentView bcv) {
		super();
		this.bcv = bcv;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		bcv.isRunningThread = true;
		if (what == FIRST_VIRI || what == FIRST_HORI){
			runningFirstThread();
			
		} else if (what == THREAD_VIRI || what == THREAD_HORI) {
			runningCommentThread();
		} else if (what == END_VIRI || what == END_HORI){
			runningEndThread();
			
		}
		Message msg = bcv.bcHandler.obtainMessage();
		Bundle b = new Bundle();
		b.putInt("what", what);
		msg.setData(b);
		msg.sendToTarget();	
		bcv.isRunningThread = false;
	}
	
	/**开头运行
	 * 
	 * void
	 * 2011-3-26上午10:36:17
	 */
	public void runningFirstThread(){
		strPrev = null;
		bcv.bookpagebg.isToBack = true;
		try {
			str = StringTool.getCleanList(bcv.bc.getCurrPage(0));
			bcv.preventPoint = -1;
			bcv.markPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			strNext = StringTool.getCleanList(bcv.cc.nextPage());
			if (strNext!=null && strNext.size()>0){
				bcv.nextPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
			}else {
				strNext = new ArrayList<String>();
				if(bcv.file >= bcv.endFile)
				{
					strNext.add("全书完");
				}else{
					strNext.add("本节完");
				}
				bcv.nextPoint = bcv.getFileLength();
			}
			bcv.nextEnd = bcv.bc.getNextIndexAt();
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (NullPointerException e){
			e.printStackTrace();
			strNext = null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**正常运行的方法
	 * 
	 * void
	 * 2011-3-26上午10:36:25
	 */
	private void runningCommentThread(){
		Log.i("zkli", "skipNumber"+skipNumber); 
		if (skipNumber==0){
			if(bcv.portraint == 1){
				what = FIRST_VIRI;
			}else if(bcv.portraint == 2){
				what = FIRST_HORI;
			}
			runningFirstThread();
		}else if(skipNumber >= getFileLength()){
			if(bcv.portraint == 1){
				what = END_VIRI;
			}else if(bcv.portraint == 2){
				what = END_HORI;
			}
			runningEndThread();
		}else{
			bcv.bookpagebg.isToBack = true;
			if(bcv.portraint == 1){
				what = THREAD_VIRI;
			}else if(bcv.portraint == 2){
				what = THREAD_HORI;
			}
			try {
				//让拖动子转到正确的位置
				//SbrMainContentOnChangeListener.possion=((int)(skipNumber*100/bcv.book.fileLength));
				//bcv.bookpagebg.leftShowPage.mySeekBarProgress((int)(skipNumber*100/bcv.book.fileLength));
				str =StringTool.getCleanList( bcv.bc.getCurrPage(skipNumber));
				bcv.markPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
				strPrev = StringTool.getCleanList(bcv.cc.previousPage());
				bcv.preventPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
				if (bcv.preventPoint<=0){
					strPrev = StringTool.getCleanList( bcv.bc.getCurrPage(0));
					bcv.preventPoint = 0;
					str = StringTool.getCleanList(bcv.cc.nextPage());
					//bcv.markPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
					strNext = StringTool.getCleanList(bcv.cc.nextPage());
					bcv.nextPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
				}else{
					bcv.cc.nextPage();
					Log.i("zkli", "++++++++++"+ContentManager.getCurrIndexAtOutOfException(bcv.bc)+"++++++++++++");
					strNext = StringTool.getCleanList(bcv.cc.nextPage());
					if (strNext!=null && strNext.size()>0){
						bcv.nextPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
					}else {
						strNext = new ArrayList<String>();
						if(bcv.file >= bcv.endFile)
						{
							strNext.add("全书完");
						}else{
							strNext.add("本节完");
						}
						bcv.nextPoint = getFileLength();
					}
					
				}
				bcv.nextEnd = bcv.bc.getNextIndexAt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**结尾运行的方法
	 * 
	 * void
	 * 2011-3-26上午10:36:34
	 */
	private void runningEndThread(){
		//bcv.bc.setFileend(true);
		try {
			Log.i("zkli", "lastNum"+skipNumber);
			bcv.bc.getCurrPage(skipNumber);
			str =new ArrayList<String>();
			if(bcv.file >= bcv.endFile)
			{
				str.add("全书完");
			}else{
				str.add("本节完");
			}
			bcv.markPoint = getFileLength();
			strPrev = StringTool.getCleanList(bcv.cc.previousPage());
			bcv.preventPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
			bcv.nextEnd = -1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bcv.bookpagebg.isToBack = false;
		bcv.nextPoint = -1;
		strNext = null;
	}

	public long getFileLength(){
		long fileLength = 0;
		Plugin plugin = bcv.bc.pm.getDefaultPlugin();
		fileLength = bcv.bc.pm.length(plugin, bcv.bc.pm.filehandle);
		
		if(fileLength > 0) return fileLength;
		else return 0;
	}
	
	public Node findNode(int file) {
		Node root = (Node)bcv.bc.pm.bltree.getRoot();
		Node curr = root;
		do {
			Node next = (Node) bcv.bc.pm.bltree.getNext(curr);
			Node currChild = (Node)curr.child;
			if (currChild != null)
				return findNode(currChild.file);
			curr = next;

		} while (curr != root && curr.file != file);
		return curr;
	}
}


