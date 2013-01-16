package com.chinachip.books.plugin;

import java.nio.ByteBuffer;

import com.chinachip.tree.BackList;

import android.util.Log;

public class PluginChm extends Plugin implements IPlugin {
	public final static int CHM_PROPERTY_INDEX = 14;
	public final static int  CHM_PROPERTY_TREE = 15;
	public final static int CHM_PROPERTY_DATA = 16;
	public final static int CHM_UNITINFO_TREE_ROOT = 0x81;
	public final static int CHM_UNITINFO_TREE_NEXT = 0x83;
	public final static int CHM_UNITINFO_TREE_CHILD = 0x84;
	public final static int CHM_UNITINFO_TREE_PARENT = 0x85;
	public final static int CHM_UNITINFO_TREE_FINISH = 0x86;
	private int handle_chapter =  0;
	
	public String getIndex(){ 
		Param param = new Param();
		param.setStr("");   	  
		int ret = book_get(handle,CHM_PROPERTY_INDEX, param);
		Log.d("vrix",param.getStr());
		return param.getStr();
	};
  
	public BackList getTree(){ 
		Param param = new Param();
		param.bltree = new BackList();
		book_get(handle, CHM_PROPERTY_TREE, param);
		return param.bltree;
	};
  
	public String getData(String str){ 
		Param param = new Param();
		param.setStr(str);   	  
		int ret = book_get(handle,CHM_PROPERTY_DATA, param);
		Log.d("vrix",param.getStr());
		return param.getStr();
	};
	
	public String getSupport() {
		return ".chm";
	}
	
	public int getBackList(Param param){ 
		int ret = 0;
		if(0 != book_get(handle, CHM_UNITINFO_TREE_ROOT, param))
			param.bltree.addNext(param.getByteBuffer(),param.j);
		else
			return 0;
		boolean needToSearchChild = true;
		while(true) {
			if(needToSearchChild) {
				if(0 != book_get(handle, CHM_UNITINFO_TREE_CHILD, param))
					param.bltree.addChild(param.getByteBuffer(),param.j);
				else
					needToSearchChild = false;
			}
			else if(0 != book_get(handle, CHM_UNITINFO_TREE_NEXT, param)) {
				param.bltree.addNext(param.getByteBuffer(),param.j);
				needToSearchChild = true;
			}
			else if(0 != book_get(handle, CHM_UNITINFO_TREE_PARENT, param)) {
				param.bltree.toParent();
			}
			else
				break;
		}
		return 1;
	};
	
	@Override
	public int open(String bookName) {
		handle = book_load(bookName);
		return handle;
	}
	
	@Override
	public int get(int prop, Param param) {
		if(prop == PluginUtil.BOOK_TYPE)
			return PluginUtil.BOOK_TYPE_TEXT;
		else if(prop == PluginUtil.BOOK_BACKLIST)
			return getBackList(param);
		else if((prop == PluginUtil.BOOK_CHARSET) || (prop == PluginUtil.BOOK_BACKLIST_CHARSET))
			param.i = 0;
		int ret = book_get(handle, prop, param);
		return ret;
	}
	
	@Override
	public void close() {
		book_unload(handle);
	}
		
	@Override
	public int open(int chapter) {
		Log.d("vrix", "");
		handle_chapter = book_open(handle, chapter);
		return handle_chapter;
	}
	
	public int openChapter(int chapter) {
		Log.d("vrix", "open");
		int h_chapter = book_open(handle, chapter);
		return h_chapter;
	}
	
	@Override
	public long length(int h_chapter) {
		Log.d("vrix", "length"+String.valueOf(h_chapter));
		if(0 != book_seek(h_chapter, 0, 2))
			return 0;
		long len = book_tell(h_chapter);
		Log.d("vrix", String.valueOf(len));
		if(len > 0)
			return len;
		else
			return 0;
	}
	
	@Override
	public void close(int h_chapter) {
		if(h_chapter != 0)
			book_close(h_chapter);
		h_chapter = 0;
	}
	
	@Override
	public ByteBuffer map(int h_chapter,long start, long len) {
		Log.d("vrix", "map"+String.valueOf(len));
		byte[] byBuf = new byte[(int) len];
		Log.d("vrix", "seek");
		if(0 != book_seek(h_chapter, start, 0))
			return null;
		
		long ret = book_read(byBuf, 1, len, h_chapter);
		String str = new String(byBuf);
		Log.d("vrix", "start="+String.valueOf(start)+"str===="+str);
		if(ret > 0) {
			ByteBuffer buff = ByteBuffer.wrap(byBuf); 
			return buff;
		}
		else {
			return null;
		}
	}
	
	public void closeAll() {
		if(handle != 0)
		{
			if(handle_chapter != 0)
				close(handle_chapter);
			handle_chapter = 0;
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
	
	// book method
	public native int book_load(String name);
	public native int book_get(int h_book, int prop, Param param);
	public native void book_unload(int h_book);	
	//chapter method
	public native int book_open(int h_book, int charpter);
	public native long book_seek(int h_chapter, long offset, int mode);
	public native long book_read(byte[] data,  long size, long count, int h_chapter);
	public native long book_tell(int h_chapter);
	public native long book_eof(int h_chapter);
	public native long book_close(int h_chapter);
	
	static {
		System.loadLibrary("chm");
	}

	@Override
	public boolean getPicture(String name, int filehandle) {
		// TODO Auto-generated method stub
		return false;
	}
};
