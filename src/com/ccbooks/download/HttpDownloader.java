package com.ccbooks.download;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.ccbooks.view.BookContentView;
import com.ccbooks.view.BookShelfView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;





public class HttpDownloader {
	private URL url = null;
	Context context = null;
	/**
	 * 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
	 */
	public int downFile(String urlStr, String path, String fileName,Context context) {
		InputStream inputStream = null;
		this.context = context;
		try {
			File resultFile=null;
			FileUtils fileUtils;
			if(context instanceof BookShelfView)
				fileUtils = new FileUtils("/sdcard");
			else
				fileUtils = new FileUtils();
			if (fileUtils.isFileExist(fileName,path)) {
				return 1;
			} else {
				inputStream = getInputStreamFromUrl(urlStr);
				resultFile = fileUtils.write2SDFromInput(path,fileName, inputStream,context);
				if (resultFile == null) {
					return -1;
				}
			}
			//判断是否apk文件，是则执行升级
			if(fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()).equals("apk")){
				installapk(resultFile);
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
		url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		((BookShelfView)context).fileSize = urlConn.getContentLength();
		((BookShelfView)context).dialog.setMax(urlConn.getContentLength());
		return inputStream;
	}
	
	/**
	 * 安装文件
	 * @param file 
	 */
	public void installapk(File file){
		BookShelfView.activity.finish();
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		BookShelfView.activity.startActivity(intent);
	}
	
	
}
