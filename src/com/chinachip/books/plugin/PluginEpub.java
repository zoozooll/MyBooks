package com.chinachip.books.plugin;
import java.nio.ByteBuffer;
import com.chinachip.tree.BackList;



import android.util.Log;


public class PluginEpub extends Plugin implements IPlugin {
	final public static int BOOK_BACKLIST_GETROOT = 0x10000080;  //获取目录根节点信息
	final public static int BOOK_BACKLIST_GETNEXT = 0x10000100;  //获取下一个节点信息
	final public static int BOOK_BACKLIST_FINISH  = 0x10000200;  //获取目录结束	
   public int epubhandl= 0;

	public String getSupport()
	{
		return ".epub";
	}
	
	public int open(String name) {
		System.out.println("name"+name);
		epubhandl = book_load(name);
		System.out.println("epubhandl"+epubhandl);
		
		System.out.println("handle"+handle);
	//	Log.d("zzq", "loadeend");
		return epubhandl;
	};
	
	
	public int load(int bookhandle,int id) {
		Log.d("zzq", "openepub");
		handle = book_open(bookhandle,id);
		Log.d("zzq", "openeend");
		return handle;
	};
	
	private int getBackList(Param param)
	{
		System.out.println("zzq"+"getBackList end");
		if(0 !=book_get(epubhandl,BOOK_BACKLIST_GETROOT,param))
		{
			System.out.println("zzq"+"BOOK_BACKLIST_GETROOT end"+param.j);
			param.bltree.addNext(param.getByteBuffer(),param.j);
		}
			System.out.println("param.j"+param.j +"param.i"+param.i);
			while(true) 
			{
				if(0 !=	book_get(epubhandl,BOOK_BACKLIST_GETNEXT,param))
				{
						param.bltree.addNext(param.getByteBuffer(),param.j);
				}
				else
					break;
			}	
		System.out.println("zzq"+"BOOK_BACKLIST_FINISH end");
		book_get(epubhandl,BOOK_BACKLIST_FINISH,param);
		System.out.println("zzq"+"BOOK_BACKLIST_FINISH end");
		return 1;
	}
	
	@Override
	public int get(int prop, Param param) {
		// TODO Auto-generated method stub
		int id;
		System.out.println("get prop"+prop);
		switch(prop)
		{
			case PluginUtil.BOOK_BACKLIST:
			{
				id = getBackList(param);
				break;	
			}
			default:
		   {
			   System.out.println("get prop book_get"+prop);
			   id = book_get(epubhandl,prop,param);
		       break;
		   }
		   
		}
		return id;
	}

	@Override
	public ByteBuffer map(int filehandle,long start, long len) {
		// TODO Auto-generated method stub
		System.out.println("zzq"+len);
		book_seek(filehandle,start,0);
		System.out.println("zzq"+"end");
		Log.d("zzq", "33333333333333333333openeend");
		byte[] array = new byte[(int) len];
		book_read(array,1,(long)len,filehandle);
		ByteBuffer bb = ByteBuffer.wrap(array);
		return bb;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if(epubhandl != 0)
		{
			book_unload(epubhandl);
		}
		epubhandl = 0;
	}

	
	public int getcover(String destname,String srcname) {
		System.out.println("name"+srcname);
		return book_getcover(destname,srcname);
	//	Log.d("zzq", "loadeend");
	};
	
	// jni 接口方法
	public native int book_open(int handle,int id);
	
	protected native long book_read(byte[] data, long bsize, long bnum,
			int filehandle);

	public native int book_seek(int handle,long offset,int mod);
	public native int book_tell(int handle);
	public native int book_eof(int handle);
	public native void book_close(int f);
	
	
	public native int book_get(int handle, int prop, Param param);
	public native int book_load(String name);
	public native int book_unload(int handle);
	public native int book_getcover(String destname,String srcname);
	static {
		System.loadLibrary("epub");
	}

	@Override
	public void close(int filehandle) {
		// TODO Auto-generated method stub
		book_close(filehandle);
	}

	@Override
	public long length(int filehandle) {
		// TODO Auto-generated method stub
		System.out.println("current"+filehandle);
		long current = book_tell(filehandle);
		System.out.println("current"+current);
		book_seek(filehandle,0,2);
		long ret = book_tell(filehandle);
		System.out.println("ret"+ret);
		book_seek(filehandle,(int)current,0);
		return ret;
	}

	public void closeAll()
	{
		if(handle != 0)
		{
			close(handle);
			close();
			handle = 0;
		}
	}

	@Override
	public int open(int file) {
		handle = book_open(epubhandl,file);
		// TODO Auto-generated method stub
		return handle;
	}

	@Override
	public boolean getPicture(String name, int filehandle) {
		// TODO Auto-generated method stub
		return false;
	}

};
