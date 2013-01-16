package com.chinachip.books.plugin;
import java.nio.ByteBuffer;
import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;



import android.util.Log;


public class PluginPdb extends Plugin implements IPlugin {
   public int pdbhandl= 0;
   private String title = null;
   public BackList bltree = new BackList();
	public String getSupport()
	{
		return ".pdb";
	}
	
	public int open(String name) {
		System.out.println("name"+name);
		pdbhandl = book_load(name);
		title = name.substring(name.lastIndexOf("/"));
		title = title.substring(1,title.lastIndexOf("."));
		System.out.println("pdbhandl"+pdbhandl);
	//	Log.d("zzq", "loadeend");
		return pdbhandl;
	};
	
	@Override
	public int get(int prop, Param param) {
		// TODO Auto-generated method stub
	
		int id =0;
		System.out.println("prop"+prop);
		switch(prop)
		{
		case PluginUtil.BOOK_TITLE:
			param.setStr(title);
			id = 1;
			break;
		case PluginUtil.BOOK_TYPE:
		{
			if(pdbhandl == 0)
			{
				param.i = PluginUtil.BOOK_TYPE_INVALID;
			}
			else
			{
				param.i = PluginUtil.BOOK_TYPE_TEXT;
			}
			break;
		}
			//return 1;
		case PluginUtil.BOOK_BACKLIST:
			
			Node root = (Node) bltree.setRoot(new Node());			
	        root.setTitle(title.getBytes());
	        param.bltree = bltree;
	        if(pdbhandl!=0)
	        {
		        root.setFile(pdbhandl);
			//	param.bltree = bltree;
				id = pdbhandl;
	        }
			break;
		case PluginUtil.BOOK_CHARSET:
			param.setStr("GBK");
			id = 1;
			break;
		case PluginUtil.BOOK_BACKLIST_CHARSET:
			param.setStr("UTF-8");
			id = 1;
			break;
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
		if(pdbhandl != 0)
		{
			book_unload(pdbhandl);
		}
		pdbhandl = 0;
	}

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
	static {
		System.loadLibrary("pdb");
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
		long ret;
		if(filehandle!=0)
		{
			long current = book_tell(filehandle);
			System.out.println("current"+current);
			book_seek(filehandle,0,2);
			ret = book_tell(filehandle);
			System.out.println("ret"+ret);
			book_seek(filehandle,(int)current,0);
		}
		else
			ret = 0;
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
		if(pdbhandl!= 0)
		{
			handle = book_open(pdbhandl,file);
		// TODO Auto-generated method stub
		}
		else
			handle =0;
			System.out.println("pdbhandl"+pdbhandl+"handle"+handle);
		return handle;
	}

	@Override
	public boolean getPicture(String name, int filehandle) {
		// TODO Auto-generated method stub
		return false;
	}

};
