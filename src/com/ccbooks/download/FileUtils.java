package com.ccbooks.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ccbooks.view.BookShelfView;

import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

public class FileUtils {
	private String SDCardRoot;
	public int fileSize=0;
	public static boolean canceldown=false;
	public FileUtils() {
		// 得到当前外部存储设备的目录

		this(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
				
	}

	public FileUtils(String RootPath){
		this.SDCardRoot = RootPath + File.separator;
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
	public File write2SDFromInput(String path, String fileName,
			InputStream input,Context context) {

		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = createFileInSDCard(fileName, path);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			canceldown = false;
			while ((temp = input.read(buffer)) != -1 && canceldown == false) {
				output.write(buffer, 0, temp);
				//如果是bookshelfview表示正在升级
				if(context instanceof BookShelfView){
					((BookShelfView)context).donwloadFileSize += temp;
					((BookShelfView)context).dialog.setDownloadFileSize(((BookShelfView)context).donwloadFileSize);
					sendMsg(1,context);
				}
			}
			if(canceldown == true){
				return null;
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
	
	public void sendMsg(int message,Context context){
		Message msg = new Message();
		msg.what = message;
		((BookShelfView)context).dialog.handler.sendMessage(msg);
	}
}
