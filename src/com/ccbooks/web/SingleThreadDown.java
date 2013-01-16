package com.ccbooks.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;

import com.ccbooks.bo.BooksBo;
import com.ccbooks.download.HttpDownloader;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.BookStoreView;
import com.ccbooks.view.CartoonReader;
import com.ccbooks.view.TextReader;
import com.ccbooks.vo.Book;
import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginUtil;

public class SingleThreadDown extends Thread{
	private Book book;
	private String SDCardRoot;
	public boolean stopDownload = false;
	public int fileSize=0;
	public static boolean downfinish=false;
	private Context mContext;
	private boolean insertDatabase;
	
	private PluginMgr pm;
	private Plugin plugin;
	
	public SingleThreadDown(Book book,String RootPath,Context context){
		this.book = book;
		this.SDCardRoot = RootPath + File.separator;
		mContext = context;
	}

	public SingleThreadDown(Book book,Context context){
		this(book,Environment.getExternalStorageDirectory()
				.getAbsolutePath(),context);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		downfinish = false;

		System.out.println(book.downloadPath);
		int result = downFile(book.downloadPath, "ccbook/", book);
		
		downfinish = true;
		super.run();
	}
	
	/**
	 * 下载文件
	 * @param urlStr
	 * @param path
	 * @param book
	 * @return
	 */
	public int downFile(String urlStr, String path, Book book) {
		InputStream inputStream = null;

		try {
			File resultFile=null;
			if (isFileExist(book.bookname+book.booktype.toLowerCase(),path)) {
				return 1;
			} else {
				inputStream = getInputStreamFromUrl(urlStr);
				resultFile = write2SDFromInput(path,book, inputStream);
				if (resultFile == null) {
					return -1;
				}
			}
					
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		URL url;
		url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		
		return inputStream;
	}
	


	
	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File createFileInSDCard(String fileName, String dir)
			throws IOException {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		System.out.println("file---->" + file);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dir) {
		File dirFile = new File(SDCardRoot + dir + File.separator);
		System.out.println(dirFile.mkdirs());
		return dirFile;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName, String path) {
		File file = new File(SDCardRoot + path + File.separator + fileName);
		return file.exists();
	}
	
	/**
	 * 删除文件
	 * @param path
	 * @param fileName
	 */
	public void delectFile(String fileName, String path){
		File file = new File(SDCardRoot + path + File.separator + fileName);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, Book book,
			InputStream input) {

		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = createFileInSDCard(book.bookname+book.booktype.toLowerCase(), path);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			int downloadsize = 0;
			stopDownload = false;
			while ((temp = input.read(buffer)) != -1 && stopDownload == false) {
				output.write(buffer, 0, temp);
				downloadsize += temp;
				book.downloadPrecent = downloadsize*100/book.fileLength;
				
				output.close();//读完4k关闭输出流
				if(((BookStoreView)mContext).getOnlineReading()){
					((BookStoreView)mContext).setOnlineReading(false);
					insertBookToDB();
					insertDatabase = true;
					openBook();
				}
				output = new FileOutputStream(file,true);//以追加文件内容的方式打开输出流
				
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
/*	public void sendMsg(int message,Context context){
		Message msg = new Message();
		msg.what = message;
		((BookShelfView)context).mHandler.sendMessage(msg);
	}*/
	
	public void setStopDownload(boolean stopDownload){
		this.stopDownload = stopDownload;
	}

	public boolean isInsertDatabase() {
		return insertDatabase;
	}

	public void setInsertDatabase(boolean insertDatabase) {
		this.insertDatabase = insertDatabase;
	}
	
	public void insertBookToDB() {
//		File file = new File(book.bookpath);
//		book.bookpath = book.bookpath+(book.booktype).toLowerCase();
//		File dest = new File(book.bookpath);
//		file.renameTo(dest);	//重命名为有后缀的文件
		BooksBo bo = new BooksBo(mContext, true);
		bo.insertDB(book);
	}

	private void openBook(){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("bookId", book.id);
		bundle.putBoolean("DownLand", true);
		
		
		intent.putExtras(bundle);
		
		if(getReaderMode() == 1)
		{
			intent.setClass(mContext, BookContentView.class);
		}else if(getReaderMode() == 2)
		{
			intent.setClass(mContext, TextReader.class);
		}
		
		mContext.startActivity(intent);
	}
	
	private int getReaderMode(){
		int readerMode = 0;
		SharedPreferences sp = ((BookStoreView)mContext).getSharedPreferences("config", Activity.MODE_PRIVATE);
		readerMode = sp.getInt("readerMode", 1);
		return readerMode;
	}
}
