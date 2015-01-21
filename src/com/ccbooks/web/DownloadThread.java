package com.ccbooks.web;

import java.io.File;
import com.ccbooks.view.BookListView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.BookStoreView;
import com.ccbooks.vo.Book;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class DownloadThread implements Runnable{
	private Book bookInfo;
//	File bookTemp;
	Context context;
	ToastMessage toastMessage;
	boolean bStop=false;
	
	public DownloadThread(Book book,Context context){
		this.bookInfo = book;
		this.context = context;
		toastMessage = new ToastMessage(context);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//bookInfo = (Book) intent.getSerializableExtra("book");
		if(BookStoreView.getDownloadThread().size()>=5){
			sendMessageToToast(ToastMessage.TOOMANYDOWNLOAD,bookInfo.bookname+bookInfo.booktype.toLowerCase());
			return;
		}
		
		
		File bookTempEx = new File(bookInfo.bookpath);
//		Log.i("garmen", "下载的书为:"+bookTempEx.getAbsolutePath());
		if(bookTempEx.exists()){
			Log.i("garmen", "网络大小"+bookInfo.fileLength+"实际大小"+bookTempEx.length());
			if(bookInfo.fileLength <= bookTempEx.length()){	//大小相等表示已存在
				sendMessageToToast(ToastMessage.HADDOWNLOAD,bookInfo.bookname+bookInfo.booktype.toLowerCase());
				return;
			}else if(BookStoreView.getDownloadThread().get(bookInfo.bookname+bookInfo.booktype.toLowerCase())!=null){//大小不等但下载线程中能找到
				sendMessageToToast(ToastMessage.DOWNLOADING,bookInfo.bookname+bookInfo.booktype.toLowerCase());
				return;
			}else{	//不完整的遗留文件,删除重新下载
				bookTempEx.delete();
				bookTempEx= new File(bookInfo.bookpath);
			}
		}
		
		sendMessageToToast(ToastMessage.DOWNLOADSTAR,bookInfo.bookname+bookInfo.booktype.toLowerCase());
		SingleThreadDown singleThread = null;
		if(bookInfo.bookpath.indexOf("local")!= -1)
			singleThread = new SingleThreadDown(bookInfo,"/sdcard",context);
		else 
			singleThread = new SingleThreadDown(bookInfo,context);
		
//		Log.i("garmen", "创建DownloadThread1"+bookInfo.bookpath);
		BookStoreView.books.add(bookInfo);
		BookStoreView.getDownloadThread().put(bookInfo.bookname+bookInfo.booktype.toLowerCase(), singleThread);
//		Log.i("garmen", bookInfo.bookname);
		singleThread.start();
//		Log.i("garmen", "threadList.size()========="+BookStoreView.getDownloadThread().size());
		while(!bStop){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(singleThread.downfinish==true){
				bStop=true;
			}
		}

		BookStoreView.getDownloadThread().remove(bookInfo.bookname+bookInfo.booktype.toLowerCase());
		//如果没有打开下载管理dialog 就把书本列表删除了，若有就交给dialog删除
		if(BookStoreView.getDownloadThread().size() ==0 && BookStoreView.downloadMangerStart == true){
			DownloadManager.stopDownLoadManager();
		}
		if(BookStoreView.downloadMangerStart == false){
			BookStoreView.books.remove(bookInfo);
		}	
		Log.i("garmen", "threadList.size()========="+BookStoreView.getDownloadThread().size());
		//下载完成判断，执行相关操作
		if(bookInfo.downloadPrecent >= 100){
			sendMessageToToast(ToastMessage.FINISH,bookInfo.bookname+bookInfo.booktype.toLowerCase());
			if(!singleThread.isInsertDatabase()){
				singleThread.insertBookToDB();
			}	
			
			//更新列表
			if(BookShelfView.mHandler != null){
				BookShelfView.mHandler.sendEmptyMessage(3);
			}
			if(BookListView.mHandler != null){
				BookListView.mHandler.sendEmptyMessage(3);
			}
			Log.i("garmen", bookInfo.bookname+"下载完成");
		}else{
			sendMessageToToast(ToastMessage.DOWNLOADFAILED,bookInfo.bookname+bookInfo.booktype.toLowerCase());
		}
	}

	private void sendMessageToToast(int what,String name) {
		Message msg = new Message();
		msg.what = what;//ToastMessage.TOOMANYDOWNLOAD;
		msg.obj = name;//bookInfo.bookname+bookInfo.booktype.toLowerCase();
		toastMessage.handler.sendMessage(msg);
	}
}
