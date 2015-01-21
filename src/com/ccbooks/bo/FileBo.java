package com.ccbooks.bo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.ccbooks.dao.BooksDao;
import com.ccbooks.dao.DBHelper;
import com.ccbooks.util.BookUtil;
import com.ccbooks.view.BookListView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.vo.Book;
import com.chinachip.books.plugin.PluginCtxt;
import com.chinachip.books.plugin.PluginEpub;
import com.chinachip.books.plugin.PluginUmd;

import android.R.string;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class FileBo implements Runnable {
	
	//各种格式的电子书
	public static final String BOOKTYPE_TXT="txt";
	public static final String BOOKTYPE_PDF="pdf";
	public static final String BOOKTYPE_HTM="htm";
	public static final String BOOKTYPE_HTML="html";
	public static final String BOOKTYPE_CHM="chm";
	public static final String BOOKTYPE_PDB="pdb";
	public static final String BOOKTYPE_JAR="jar";
	public static final String BOOKTYPE_EPUB="epub";
	public static final String BOOKTYPE_MOBI="mobi";
	public static final String BOOKTYPE_UMD="umd";
	public static final String BOOKTYPE_FB2="fb2";
	public static final String BOOKTYPE_CBZ="cbz";
	public static final String BOOKTYPE_CCT="cct";
	public static final String BOOKTYPE_FB2_ZIP="fb2.zip";
	
	 public static final byte[] SDCARD= {
			 (byte)0xAD, (byte)0x8A, (byte)0x39, (byte)0xE1, (byte)0xC7, (byte)0xE7, (byte)0x06, (byte)0xC7, (byte)0x88, (byte)0x51, (byte)0xCB, (byte)0x72, (byte)0x01, (byte)0xB1, (byte)0xEE, (byte)0x55
			};//sd

	 public static final 	byte[] LOCAL = {
			(byte)0x16, (byte)0x1B, (byte)0x8F, (byte)0xCD, (byte)0x8D, (byte)0x23, (byte)0xA3, (byte)0x05, (byte)0x82, (byte)0xFC, (byte)0xBC, (byte)0xB5, (byte)0x84, (byte)0x97, (byte)0x3A, (byte)0x5A
			}; //local
	
	 public static final byte[] SDCARD1= {
		 (byte)0x6f,(byte)0xef,(byte)0x13,(byte)0x63,(byte)0xcc,(byte)0x28,(byte)0x05,(byte)0xbd,(byte)0xe4,(byte)0xec,(byte)0xe5,(byte)0x0a,(byte)0xeb,(byte)0x3f,(byte)0x14,(byte)0x9a,
			};//sd

	 public static final 	byte[] LOCAL1 = {
		 (byte)0x78,(byte)0x08,(byte)0xb3,(byte)0xf0,(byte)0x6b,(byte)0x2c,(byte)0x38,(byte)0xff,(byte)0xff,(byte)0xcc,(byte)0xe1,(byte)0x13,(byte)0xf8,(byte)0x98,(byte)0x00,(byte)0xd7,
			}; //local
	 
	 public static final byte[] CHINACHIP = {(byte)0x63,(byte)0xb5,(byte)0x7d,(byte)0x3a,(byte)0x5c,(byte)0x98,(byte)0x92,(byte)0x1e,(byte)0x82,(byte)0x06,(byte)0xaf,(byte)0x62,(byte)0xc3,(byte)0x6d,(byte)0xf3,(byte)0x68};
 
	private Context context;
	public String firstPath;
	private BooksDao dao;
	private int count;
	
	public FileBo(Context context,String firstPath){
		this.firstPath=firstPath;
		this.context=context;
	}
	
	public FileBo(Context context){
		this.context=context;
	}
	
	public FileBo(Context context,String firstPath,Dialog dialog){
		this.firstPath=firstPath;
		this.context=context;
	}
	
	
	
	public void WriteFileEx(String name,String destname) {
	       try {
				//获取源图片的大小 
				Bitmap bm;
				BitmapFactory.Options opts = new BitmapFactory.Options();  
				//当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight   
				BitmapFactory.decodeFile(name ,opts);
				int srcWidth = opts.outWidth; 
				int srcHeight = opts.outHeight;  
				int destWidth = 0; 
				int destHeight = 0;  
				//缩放的比例 hiR+cPSF  
				double ratio = 0.0;     
				//按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度 V-i:t,*lk(  
				if(srcWidth >srcHeight) {  
					
					if(srcWidth>75)
					{
						destWidth = 75;   
					}
					else
						destWidth = srcWidth;
					ratio = srcWidth / destWidth;  
						destHeight = (int) (srcHeight / ratio);  
				}   
				else {  
					if(srcHeight>70)
					{
						destHeight = 70;   
					}
					else
						destHeight = srcHeight;
					ratio = srcHeight / destHeight;  
					destWidth = (int) (srcWidth / ratio); 
				} 
				//对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩 <{1 3Nd'o  
				BitmapFactory.Options newOpts = new BitmapFactory.Options();  
				//缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值 Tj=g[)+K  
				newOpts.inSampleSize = (int) ratio + 1;
				//inJustDecodeBounds设为false表示把图片读进内存中  
				newOpts.inJustDecodeBounds = false;   
				//设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放 (( _v>{  
				newOpts.outHeight = destHeight;   
				newOpts.outWidth = destWidth;  
				//添加尺寸信息， /F @a@m|  
				//tvInfo.append("\nWidth:" + newOpts.outWidth + " Height:" + newOpts.outHeight);  
				//获取缩放后图片
				Bitmap destBm = BitmapFactory.decodeFile(name, newOpts);
				
				if(destBm == null) { 
					//showAlert("CreateFile" 0 "Create Failed" "OK" false); 
				} else {   
				//文件命名，通过GUID可避免命名的重复 Tp/+{|~      
					File destFile = new File(destname);   
					//创建文件输出流 m:~s6c6H  
					OutputStream os = new FileOutputStream(destFile);   
					//存储 BY$%gIB6>  
					boolean issuccess = destBm.compress(CompressFormat.JPEG,30,os);  
					
					
					File srcFile = new File(name);
					srcFile.delete();
					//关闭流 l
					os.close();   
				}
			} catch(Exception e) {  
				e.printStackTrace();
			} 
}

	
	
	//�鿴�����ļ�
	public List<String> refreshSdFileList(String strPath) {
		File dir = new File(strPath);
		File[] files = dir.listFiles();

		if((files == null))
       {
    	   return null;
       }
		List<String> filelist=new ArrayList<String>();	
		if (files != null){
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					refreshSdFileList(files[i].getAbsolutePath());
				} else if(files[i].isFile()) {
					if(dao==null){
						dao=BooksDao.getDao(context);
						dao.deleteAll(DBHelper.TABLE_BOOK);
					}
					String strFileName = files[i].getAbsolutePath().toLowerCase();
					//Log.i("file", strFileName);
					String ex=FileBo.getFileEx(strFileName);
					if(BOOKTYPE_TXT.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
			
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
						//filelist.add(strFileName);
					}else if(BOOKTYPE_EPUB.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath= strFileName+"1.jpg";
						String strDestImgPath= strFileName+".jpg";
						PluginEpub epub = new PluginEpub();
						epub.getcover(strImgPath,strFileName);
						WriteFileEx(strImgPath,strDestImgPath);
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strDestImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strDestImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}else if(BOOKTYPE_HTM.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;						
					}else if(BOOKTYPE_HTML.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;						
					}else if(BOOKTYPE_CHM.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;						
					}else if(BOOKTYPE_FB2_ZIP.equals(ex)){
						String bookname=FileBo.getFB2ZIPFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;						
					}else if(BOOKTYPE_JAR.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
					else if(BOOKTYPE_PDB.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strImgPath).exists()){
							cv.put("imgpath", strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
					else if(BOOKTYPE_UMD.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath= strFileName+"1.jpg";
						String strDestImgPath= strFileName+".jpg";
						PluginUmd umd = new PluginUmd();
						umd.getcover(strImgPath,strFileName);
						WriteFileEx(strImgPath,strDestImgPath);
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strDestImgPath).exists()){
							cv.put("imgpath", strDestImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}else if(BOOKTYPE_FB2.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strImgPath).exists()){
							cv.put("imgpath", strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
					else if(BOOKTYPE_CBZ.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strImgPath).exists()){
							cv.put("imgpath", strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}	
					else if(BOOKTYPE_CCT.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath= strFileName+"1.jpg";
						String strDestImgPath= strFileName+".jpg";
						PluginCtxt cct = new PluginCtxt();
						cct.getcover(strImgPath,strFileName);
						WriteFileEx(strImgPath,strDestImgPath);
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strDestImgPath).exists()){
							cv.put("imgpath", strDestImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}		
					
					else if(BOOKTYPE_MOBI.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strImgPath).exists()){
							cv.put("imgpath", strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
				}
			}
			//return null;
		}		
		return filelist;
	}
	
	
	
	
	public List<String> refreshLocalFileList(String strPath) {
		File dirLocal = new File(strPath);	
		File[] filesLocal = dirLocal.listFiles();
	//	int count=0;
		System.out.println("refreshFileList2");
		if(filesLocal == null)
       {
    	   return null;
       }
		List<String> filelist=new ArrayList<String>();	
		if (filesLocal != null){
			for (int i = 0; i < filesLocal.length; i++) {
				if (filesLocal[i].isDirectory()) {
					refreshLocalFileList(filesLocal[i].getAbsolutePath());
				} else if(filesLocal[i].isFile()) {
					if(dao==null){
						dao=BooksDao.getDao(context);
						dao.deleteAll(DBHelper.TABLE_BOOK);
					}
					String strFileName = filesLocal[i].getAbsolutePath().toLowerCase();
					//Log.i("file", strFileName);
					String ex=FileBo.getFileEx(strFileName);
					if(BOOKTYPE_TXT.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
						//filelist.add(strFileName);
					}else if(BOOKTYPE_JAR.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}else if(BOOKTYPE_EPUB.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strFileName+"1.jpg";
						String strDestImgPath=strFileName+".jpg";
						PluginEpub epub = new PluginEpub();
						epub.getcover(strImgPath,strFileName);
						WriteFileEx(strImgPath,strDestImgPath);
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strDestImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strDestImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}else if(BOOKTYPE_HTM.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}else if(BOOKTYPE_HTML.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}else if(BOOKTYPE_CHM.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}else if(BOOKTYPE_FB2_ZIP.equals(ex)){
						String bookname=FileBo.getFB2ZIPFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;	
					}else if(BOOKTYPE_FB2.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
					else if(BOOKTYPE_CBZ.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put(DBHelper.BOOKNAME, bookname);
						cv.put(DBHelper.BOOKPATH,strFileName);
						cv.put(DBHelper.BOOKTYPE, ex);
						if(new File(strImgPath).exists()){
							cv.put(DBHelper.IMGPATH, strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
					else if(BOOKTYPE_CCT.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strFileName+"1.jpg";
						String strDestImgPath=strFileName+".jpg";
						PluginCtxt cct = new PluginCtxt();
						WriteFileEx(strImgPath,strDestImgPath);
						cct.getcover(strImgPath,strFileName);
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strDestImgPath).exists()){
							cv.put("imgpath", strDestImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
					else if(BOOKTYPE_PDB.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strImgPath).exists()){
							cv.put("imgpath", strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
					else if(BOOKTYPE_UMD.equals(ex)){
						
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath= strFileName+"1.jpg";
						String strDestImgPath= strFileName+".jpg";
						PluginUmd umd = new PluginUmd();
						umd.getcover(strImgPath,strFileName);
						WriteFileEx(strImgPath,strDestImgPath);
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strDestImgPath).exists()){
							cv.put("imgpath", strDestImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
					else if(BOOKTYPE_MOBI.equals(ex)){
						String bookname=FileBo.getFileNa(strFileName);
						String strImgPath=strPath+"/"+bookname+".jpg";
						ContentValues cv=new ContentValues();
						cv.put("bookname", bookname);
						cv.put("bookpath",strFileName);
						cv.put("booktype", ex);
						if(new File(strImgPath).exists()){
							cv.put("imgpath", strImgPath);
						}
						dao.insert(DBHelper.TABLE_BOOK,cv);
						count++;
					}
				}
			}
			//return null;
		}		
		return filelist;
	}
	
	public Book getOneByPath(String filePath) throws SQLException,NullPointerException {
		BooksDao dao=null;
		Book book=null;
		dao = BooksDao.getDao(context);
		String where = DBHelper.BOOKPATH + "=?";
		String[] selectionArgs = { filePath };
		Cursor cursor = null;
		try{
			cursor = dao.select(true,DBHelper.TABLE_BOOK, null, where, selectionArgs, null, "0,1");
			
			cursor.moveToFirst();
			if ( !cursor.isAfterLast()) {
				book = new Book();
				book.id = cursor.getInt(0);
				book.bookname = cursor.getString(cursor
						.getColumnIndex(DBHelper.BOOKNAME));
				book.author = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.AUTHOR));
				book.bookpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKPATH));
				book.imgpath = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.IMGPATH));
				book.charset = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.CHARSET));
				book.booktype = cursor.getString(cursor
						.getColumnIndexOrThrow(DBHelper.BOOKTYPE));
				book.curIndex = cursor.getInt(cursor.
						getColumnIndexOrThrow(DBHelper.CURINDEX));
				book.file = cursor.getInt(cursor.
						getColumnIndexOrThrow(DBHelper.FILE));
				book.font = cursor.getString(cursor.
						getColumnIndexOrThrow(DBHelper.FONT));
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			if (cursor!=null){
				cursor.close();
			}
		}
	
		return book;
	}
	
	public void InsertBook(String strPath) {
		File dir = new File(strPath);
	

		if(!dir.exists())
        {
    	   return ;
        }
		
		if(dao==null){
			dao=BooksDao.getDao(context);
		
		}
		String strFileName = strPath;
		//Log.i("file", strFileName);
		String ex=FileBo.getFileEx(strFileName);
		if(BOOKTYPE_TXT.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put(DBHelper.BOOKNAME, bookname);
			cv.put(DBHelper.BOOKPATH,strFileName);
			cv.put(DBHelper.BOOKTYPE, ex);

			if(new File(strImgPath).exists()){
				cv.put(DBHelper.IMGPATH, strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
			
			//filelist.add(strFileName);
		}else if(BOOKTYPE_EPUB.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath= strFileName+"1.jpg";
			String strDestImgPath= strFileName+".jpg";
			PluginEpub epub = new PluginEpub();
			epub.getcover(strImgPath,strFileName);
			WriteFileEx(strImgPath,strDestImgPath);
			ContentValues cv=new ContentValues();
			cv.put(DBHelper.BOOKNAME, bookname);
			cv.put(DBHelper.BOOKPATH,strFileName);
			cv.put(DBHelper.BOOKTYPE, ex);
			if(new File(strDestImgPath).exists()){
				cv.put(DBHelper.IMGPATH, strDestImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
			
		}else if(BOOKTYPE_HTM.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put(DBHelper.BOOKNAME, bookname);
			cv.put(DBHelper.BOOKPATH,strFileName);
			cv.put(DBHelper.BOOKTYPE, ex);
			if(new File(strImgPath).exists()){
				cv.put(DBHelper.IMGPATH, strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
								
		}else if(BOOKTYPE_HTML.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put(DBHelper.BOOKNAME, bookname);
			cv.put(DBHelper.BOOKPATH,strFileName);
			cv.put(DBHelper.BOOKTYPE, ex);
			if(new File(strImgPath).exists()){
				cv.put(DBHelper.IMGPATH, strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
								
		}else if(BOOKTYPE_CHM.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put(DBHelper.BOOKNAME, bookname);
			cv.put(DBHelper.BOOKPATH,strFileName);
			cv.put(DBHelper.BOOKTYPE, ex);
			if(new File(strImgPath).exists()){
				cv.put(DBHelper.IMGPATH, strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
								
		}else if(BOOKTYPE_FB2_ZIP.equals(ex)){
			String bookname=FileBo.getFB2ZIPFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put(DBHelper.BOOKNAME, bookname);
			cv.put(DBHelper.BOOKPATH,strFileName);
			cv.put(DBHelper.BOOKTYPE, ex);
			if(new File(strImgPath).exists()){
				cv.put(DBHelper.IMGPATH, strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
								
		}else if(BOOKTYPE_JAR.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put(DBHelper.BOOKNAME, bookname);
			cv.put(DBHelper.BOOKPATH,strFileName);
			cv.put(DBHelper.BOOKTYPE, ex);
			if(new File(strImgPath).exists()){
				cv.put(DBHelper.IMGPATH, strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
			
		}
		else if(BOOKTYPE_PDB.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put("bookname", bookname);
			cv.put("bookpath",strFileName);
			cv.put("booktype", ex);
			if(new File(strImgPath).exists()){
				cv.put("imgpath", strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
			
		}
		else if(BOOKTYPE_UMD.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath= strFileName+"1.jpg";
			String strDestImgPath= strFileName+".jpg";
			PluginUmd umd = new PluginUmd();
			umd.getcover(strImgPath,strFileName);
			WriteFileEx(strImgPath,strDestImgPath);
			ContentValues cv=new ContentValues();
			cv.put("bookname", bookname);
			cv.put("bookpath",strFileName);
			cv.put("booktype", ex);
			if(new File(strDestImgPath).exists()){
				cv.put("imgpath", strDestImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
			
		}else if(BOOKTYPE_FB2.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put("bookname", bookname);
			cv.put("bookpath",strFileName);
			cv.put("booktype", ex);
			if(new File(strImgPath).exists()){
				cv.put("imgpath", strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
			
		}
		else if(BOOKTYPE_CBZ.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put("bookname", bookname);
			cv.put("bookpath",strFileName);
			cv.put("booktype", ex);
			if(new File(strImgPath).exists()){
				cv.put("imgpath", strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
			
		}	
		else if(BOOKTYPE_CCT.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath= strFileName+"1.jpg";
			String strDestImgPath= strFileName+".jpg";
			PluginCtxt cct = new PluginCtxt();
			cct.getcover(strImgPath,strFileName);
			WriteFileEx(strImgPath,strDestImgPath);
			ContentValues cv=new ContentValues();
			cv.put("bookname", bookname);
			cv.put("bookpath",strFileName);
			cv.put("booktype", ex);
			if(new File(strDestImgPath).exists()){
				cv.put("imgpath", strDestImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
			
		}		
		
		else if(BOOKTYPE_MOBI.equals(ex)){
			String bookname=FileBo.getFileNa(strFileName);
			String strImgPath=strPath+"/"+bookname+".jpg";
			ContentValues cv=new ContentValues();
			cv.put("bookname", bookname);
			cv.put("bookpath",strFileName);
			cv.put("booktype", ex);
			if(new File(strImgPath).exists()){
				cv.put("imgpath", strImgPath);
			}
			dao.insert(DBHelper.TABLE_BOOK,cv);
			
		}
		
	
	}
	
/*	//批量计算文件的大小
	public void getAllFileLength(){
		if(dao==null){
			dao=BooksDao.getDao(context);
		}
		ContentValues cv=null;
		for(Book b:BooksBo.books){
			if(cv==null){
				cv =new ContentValues();
			}
			cv.clear();
			try {
				int totalBytes = 0 ;
				if(BOOKTYPE_TXT.equals(b.booktype)){
					
					totalBytes= BooksEngine.getFileSize(b.bookpath, "gbk");
				}
				cv.put(DBHelper.FILE_LENGTH, totalBytes);
				dao.update(DBHelper.TABLE_BOOK, b.id, cv);
			} catch(FileNotFoundException e){
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}*/
	public static void delFile(String path) throws IOException,FileNotFoundException,Exception{
		File file=new File(path);
		if(file.exists()){
			file.delete();
		}
	}
	
	// 扩展名
	public static String getFileEx(String uri) {
		String ex = uri.substring(uri.lastIndexOf(".") + 1, uri.length())
			.toLowerCase();
		if(ex.equals("zip")){
			if(uri.indexOf("fb2.zip")>0){
				return "fb2.zip";
			}
		} 
		return ex;
	}

	// 不带路径不带扩展名的文件名
	public static String getFileNa(String uri) {
		return uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));
	}
	//fb2.zip后缀
	public static String getFB2ZIPFileNa(String uri) {
		return uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf(".fb2.zip"));
	}
	
	//带路径不带扩展名的文件名
	public static String getFileRealname(String uri){
		return uri.substring(0, uri.lastIndexOf("/"));
	}
	
	  /**
	   * 把多余的0去掉
	   * @param src 要处理的数组
	   * @return
	   */
	  private static byte[] delZero(byte[] src){
		  int lastoffest = 0;
		  for(int i = 0;i<src.length;i++){
				
				if(src[i] != 0 ){
					lastoffest++;
				}else{
					break;
				}
			}
			byte[] lastResult = new byte[lastoffest];
			for(int i=0;i<lastResult.length;i++){
				lastResult[i] = src[i];
			}
			return lastResult;
	  }

	
	public synchronized static String getRealData(byte[] src ) {
		PluginUmd umdd = new PluginUmd();
		byte[] result=umdd.Decrypt(src);
		
		byte[] lastResult = delZero(result);
		String a=new String(lastResult);
		
		return a;
		//return BASE_MAG_URI;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		count = 0;
		refreshSdFileList(firstPath);
		//refreshLocalFileList("/local");
		/*String chinachip = getRealData(CHINACHIP);
		if(chinachip.equals("chinachip")){
			refreshSdFileList(getRealData(SDCARD1));
			refreshLocalFileList(getRealData(LOCAL1));
		}else{
			refreshSdFileList(getRealData(SDCARD));
			refreshLocalFileList(getRealData(LOCAL));
		}*/
		
		BookUtil.countBooks=count;
		if(context instanceof BookShelfView ){
			((BookShelfView)context).handler.sendEmptyMessage(0);
		}else if(context instanceof BookListView){
			((BookListView)context).handler.sendEmptyMessage(0);
		}
	}
}
