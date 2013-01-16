package com.chinachip.books.plugin;

import java.nio.ByteBuffer;

import android.util.Log;

public class PluginJar extends Plugin implements IPlugin {

	final public static int BOOK_BACKLIST_GETROOT = 0x10000080;  //获取目录根节点信息
	final public static int BOOK_BACKLIST_GETNEXT = 0x10000100;  //获取下一个节点信息
	final public static int BOOK_BACKLIST_FINISH  = 0x10000200;  //获取目录结束	
	
	
	public String getSupport() {
		return ".jar";
	}

	@Override
	public int open(String name) {
		// TODO Auto-generated method stub
		if(handle != 0) 
			closeAll();
		
		handle = book_load(name);
		return handle;
	}
	
	private int getBackList(Param param)
	{
		book_get(handle,BOOK_BACKLIST_GETROOT,param);
		param.bltree.addNext(param.getByteBuffer(),param.j);
		while(param.i>0)
		{
			book_get(handle,BOOK_BACKLIST_GETNEXT,param);
			if(param.i>0)
				param.bltree.addNext(param.getByteBuffer(),param.j);
		}
		book_get(handle,BOOK_BACKLIST_FINISH,param);
		return 0;
	}
	
	@Override
	public int get(int prop, Param param) {
		switch(prop)
		{
		case PluginUtil.BOOK_BACKLIST:
			return getBackList(param);
		default:
			return book_get(handle,prop,param);
		}
	}

	@Override
	public ByteBuffer map(int filehandle,long start, long len) {
		// TODO Auto-generated method stub
		Log.i("vrix","----------------map start-----------------------");
		book_seek(filehandle,start,0);
		byte[] array = new byte[(int) len];
		book_read(array,1,len,filehandle);
		
		ByteBuffer bb = ByteBuffer.wrap(array);
		Log.i("vrix","----------------map end-----------------------");	
		return bb;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if(handle != 0)
		{
			book_unload(handle);
		}
		handle = 0;
	}

	// jni
	
	protected native int book_load(String name);

	protected native int book_get(int bookhandle, int prop, Param param);

	protected native void book_unload(int bookhandle);

	protected native int book_open(int bookhandle,int file);

	protected native long book_seek(int filehandle, long offset, int mode);

	protected native long book_read(byte[] data, long bsize, long bnum,
			int filehandle);

	protected native long book_tell(int filehandle);

	protected native long book_eof(int filehandle);

	protected native long book_close(int filehandle);
	
	public native int book_getpicture(String name,int handle);

	static {
		System.loadLibrary("jar");
	}

	@Override
	public void close(int filehandle) {
		// TODO Auto-generated method stub
		if(filehandle != 0)
		  book_close(filehandle);
		filehandle = 0;
	}

	@Override
	public long length(int filehandle) {
		// TODO Auto-generated method stub
		Log.i("vrix","----------------length start-----------------------");
		Log.i("vrix","----------------length 1------------"+filehandle+"-----------");
		long current = book_tell(filehandle);
		
		book_seek(filehandle,0,2);
		Log.i("vrix","----------------length 2-----------------------");
		long ret = book_tell(filehandle);
		Log.i("vrix","----------------length 3-----------------------");
		book_seek(filehandle,(int)current,0);
		Log.i("vrix","----------------length end-----------------------");
		return ret;
	}

	@Override
	public int open(int file) {
		// TODO Auto-generated method stub
		Log.d("vrix",Integer.toString(file));
		return book_open(handle,file);
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
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		closeAll();
	}

	public boolean getPicture(String name, int filehandle) {
		// TODO Auto-generated method stub
		book_getpicture(name,filehandle);
		return false;
	}
	

}
