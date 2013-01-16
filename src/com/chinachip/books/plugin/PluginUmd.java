package com.chinachip.books.plugin;
import java.nio.ByteBuffer;
import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;



import android.util.Log;


public class PluginUmd extends Plugin implements IPlugin {
	final public static int BOOK_BACKLIST_GETROOT = 0x10000080;  //获取目录根节点信息
	final public static int BOOK_BACKLIST_GETNEXT = 0x10000100;  //获取下一个节点信息
	final public static int BOOK_BACKLIST_FINISH  = 0x10000200;  //获取目录结束	
   public int umdhandl= 0;

	public String getSupport()
	{
		return ".umd";
	}
	
	public int open(String name) {
		System.out.println("name"+name);
		umdhandl = book_load(name);
		System.out.println("umdhandl"+umdhandl);
	//	Log.d("zzq", "loadeend");
		return umdhandl;
	};
	
	
	public int load(int bookhandle,int id) {
		System.out.println("zzq"+"load");
		handle = book_open(bookhandle,id);
		System.out.println("zzq"+"load end");
		return handle;
	};
	private int getBackList(Param param)
	{
		System.out.println("zzq"+"getBackList end");
		book_get(umdhandl,BOOK_BACKLIST_GETROOT,param);
		System.out.println("zzq"+"BOOK_BACKLIST_GETROOT end"+param.j);
		param.bltree.addNext(param.getByteBuffer(),param.j);
		while(param.i>0)
		{
			System.out.println("param.j"+param.j +"param.i"+param.i);
			book_get(umdhandl,BOOK_BACKLIST_GETNEXT,param);
			if(param.i>0)
				param.bltree.addNext(param.getByteBuffer(),param.j);
		}
		
		System.out.println("zzq"+"BOOK_BACKLIST_FINISH end");
	//	book_get(umdhandl,BOOK_BACKLIST_FINISH,param);
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
			case PluginUtil.BOOK_CHARSET:
			{
				id = 1;
				param.setStr("UTF-16LE");
			}
			break;
			case PluginUtil.BOOK_BACKLIST_CHARSET:
			{
				id = 1;
				param.setStr("UTF-16LE");
			}
				break;
		case PluginUtil.BOOK_BACKLIST:
			{
				id = getBackList(param);
				break;	
			}
		default:
		   {
			   System.out.println("get prop book_get"+prop);
			   id = book_get(umdhandl,prop,param);
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
			byte[] array = new byte[(int) len];
			book_read(array,1,(long)len,filehandle);
			ByteBuffer bb = ByteBuffer.wrap(array);
			return bb;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if(umdhandl != 0)
		{
			book_unload(umdhandl);
		}
		umdhandl = 0;
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
	public native int book_rel(byte[] destdata ,byte[] srcdata, int len);
	public native int book_getpicture(String name,int handle);
	
	static {
		System.loadLibrary("umd");
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
		if(filehandle!=0)
		{
			long current = book_tell(filehandle);
			System.out.println("current"+current);
			book_seek(filehandle,0,2);
			long ret = book_tell(filehandle);
			System.out.println("ret"+ret);
			book_seek(filehandle,(int)current,0);
			return ret;
		}
		else
			return 0;
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
		handle = book_open(umdhandl,file);
		// TODO Auto-generated method stub
		System.out.println("umdhandl"+umdhandl+"handle"+handle);
		return handle;
	}
	
	
	
	public byte[] Decrypt(byte[] src) {
		byte[] dst = new byte[src.length];
		book_rel(dst, src, src.length);
		return dst;
	}

	public boolean getPicture(String name, int filehandle) {
		// TODO Auto-generated method stub
		book_getpicture(name,filehandle);
		return false;
	}


};
