package com.chinachip.books.plugin;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import android.util.Log;

public class PluginFb2 extends Plugin implements IPlugin {

	final public static int BOOK_BACKLIST_GETROOT = 0x10000080;  //获取目录根节点信息
	final public static int BOOK_BACKLIST_GETNEXT =  0x10000100;  //获取下一个节点信息
	final public static int BOOK_BACKLIST_FINISH     =  0x10000200;  //获取目录结束	
        final public static int BOOK_BACKLIST_GETCHILD     =  0x10000400;  //获取指定节点的子节点	             
        final public static int BOOK_BACKLIST_GETPARENT     =  0x10000800;  //获取指定节点的父节点
        public int fb2handle = 0;
        public int fb2onehandle = 0;
        public String getSupport()
	{
		return ".fb2;.fb2.zip";
	}
	
	@Override
	public int open(String name) {
		// TODO Auto-generated method stub
		
		fb2handle = book_load(name);
		return fb2handle;
	}
	
//	String str = null;
//	try {
//		str = new String(param.getByteBuffer(),"UTF-8");
//	} catch (UnsupportedEncodingException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	Log.d("vrix",str);
	private int getBackList(Param param)
	{
		int t;
		String str = null;
		book_get(fb2handle,BOOK_BACKLIST_GETROOT,param);
		if (param.i>0)
		{
			t = param.i;
			param.bltree.addNext(param.getByteBuffer(),param.j);
		}
		else
		{
			return 0;
		}
		
		while (param.i>0)
		{
			book_get(fb2handle,BOOK_BACKLIST_GETCHILD,param);
			if (param.i>0)
			{
				t = param.i;
				param.bltree.addChild(param.getByteBuffer(),param.j);
				continue;
			}
			else
			{
				param.i = t;
			}
			
			book_get(fb2handle,BOOK_BACKLIST_GETNEXT,param);
			if (param.i>0)
			{
				t = param.i;
				param.bltree.addNext(param.getByteBuffer(),param.j);
				continue;
			}
			else
			{
				param.i = t;
			}
			
			while (param.i>0)
			{
				book_get(fb2handle,BOOK_BACKLIST_GETPARENT,param);
				if (param.i>0)
				{
					t = param.i;
					param.bltree.toParent();
					book_get(fb2handle,BOOK_BACKLIST_GETNEXT,param);
					if (param.i>0)
					{
						
						t = param.i;
						param.bltree.addNext(param.getByteBuffer(),param.j);
						break;
					}
					else
					{
						param.i = t;
					}
				}
				else
				{
					return 0;
				}
			}
		}
		return 0;
	}

	@Override
	public int get(int prop, Param param) {
		switch(prop)
		{
		case PluginUtil.BOOK_BACKLIST:
			return getBackList(param);
		default:
			return book_get(fb2handle,prop,param);
		}
	}
	
	@Override
	public ByteBuffer map(int filehandle,long start, long len) {
		// TODO Auto-generated method stub
		book_seek(fb2onehandle,start,0);
		byte[] array = new byte[(int) len];
		book_read(array,1,len,fb2onehandle);
		ByteBuffer bb = ByteBuffer.wrap(array);
		return bb;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		if(fb2handle != 0)
		{
			book_unload(fb2handle);
		}
		fb2handle = 0;
	}

	// jni 接口方法

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

	static {
		System.loadLibrary("fb2");
	}

	@Override
	public void close(int filehandle) {
		// TODO Auto-generated method stub
		if(fb2onehandle!=0)
			book_close(fb2onehandle);
		fb2onehandle =0;
	}

	@Override
	public long length(int filehandle) {
		// TODO Auto-generated method stub
		long current = book_tell(fb2onehandle);
		book_seek(fb2onehandle,0,2);
		long ret = book_tell(fb2onehandle);
		book_seek(fb2onehandle,(int)current,0);
		return ret;
	}

	@Override
	public int open(int file) {
		// TODO Auto-generated method stub
		Log.d("vrix",Integer.toString(file));
		fb2onehandle =  book_open(fb2handle,file);
		return fb2onehandle;
	}

	public void closeAll()
	{
		if(fb2onehandle != 0)
		{
			close(fb2onehandle);
			fb2onehandle = 0;
		}
		close();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		closeAll();
	}

	@Override
	public boolean getPicture(String name, int filehandle) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
