package com.chinachip.books.plugin;

import java.nio.ByteBuffer;

import android.util.Log;

import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;

public class PluginHtm extends PluginChm{
	final static int HTM_FILE_HANDLE = 0x80;
	private String title = null;
	public BackList bltree = new BackList();
	
	@Override
	public String getSupport() {
		return ".htm";
	}

	@Override
	public int open(String bookName) {
		// TODO Auto-generated method stub
		title = bookName.substring(bookName.lastIndexOf("/")+1);
		title = title.substring(0,title.lastIndexOf("."));
		return super.open(bookName);
	}

	@Override
	public int get(int prop, Param param) {
		if(prop == PluginUtil.BOOK_TYPE)
			return PluginUtil.BOOK_TYPE_TEXT;
		else if(prop == PluginUtil.BOOK_BACKLIST) {
			Node root = (Node) bltree.setRoot(new Node());			
			root.setTitle(title.getBytes());
			root.setFile(HTM_FILE_HANDLE);
			param.bltree = bltree;
			return 1;
		}
		else if(prop == PluginUtil.BOOK_BACKLIST_CHARSET) {
			param.setStr("UTF-8");
			return 1;
		}
		else if(prop == PluginUtil.BOOK_CHARSET) {
			param.i = handle;
			int ret = book_get(0, prop, param);
			return ret;
		}
		return 0;
	}
	
	@Override
	public int open(int chapter) {
		//no use here
		return handle;
	}

	@Override
	public void close() {
		//这里的HANDLE实际上是HTML的HANDLE,所以采用BOOK_CLOSE的方法.
		if(handle != 0)
			book_close(handle);
		handle = 0;
	}		
	
	@Override
	public void close(int h_chapter) {
		//no use here
	}

	@Override
	public ByteBuffer map(int h_file,long start, long len) {
		byte[] byBuf = new byte[(int) len];
		if(0 != book_seek(h_file, start, 0))
			return null;
		
		long ret = book_read(byBuf, 1, len, h_file);
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
	
}
