package com.chinachip.books.plugin;

import java.nio.ByteBuffer;

import android.util.Log;

import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;

public class PluginMobi extends Plugin implements IPlugin {
	   final static int HTM_FILE_HANDLE = 0x80;
	   private String title = null;
	   public BackList bltree = new BackList();
	   private int handle_chapter;
	  
	   @Override
	   public String getSupport() {
			return ".mobi";
		}
		
		@Override
		public int open(String name) {
			System.out.println("name"+name);
			handle = book_load(name);
			title = name.substring(name.lastIndexOf("/")+1);
			title = title.substring(0,title.lastIndexOf("."));
			return handle;
		};
		
		@Override
		public int open(int file) {
			handle_chapter = 0;
			if(handle != 0)
				handle_chapter = book_open(handle,file);
			return handle_chapter;
		}
		
		@Override
		public int get(int prop, Param param) {
			switch(prop)
			{
					case PluginUtil.BOOK_TITLE:
						param.setStr(title);
						return 1;
					case PluginUtil.BOOK_BACKLIST:
						Node root = (Node) bltree.setRoot(new Node());			
				        root.setTitle(title.getBytes());
				        root.setFile(HTM_FILE_HANDLE);
						param.bltree = bltree;
						return handle;
					case PluginUtil.BOOK_CHARSET:
					case PluginUtil.BOOK_BACKLIST_CHARSET:
						param.setStr("UTF-8");
						return 1;
			}
			return 0;
		}

		@Override
		public ByteBuffer map(int filehandle,long start, long len) {
			// TODO Auto-generated method stub
			byte[] byBuf = new byte[(int) len];
			if(book_seek(filehandle, start, 0) < 0)
				return null;
			
			long ret = book_read(byBuf, 1, len, filehandle);
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

		@Override
		public void close() {
			// TODO Auto-generated method stub
			if(handle != 0)
			{
				book_unload(handle);
			}
			handle = 0;
		}
		
		@Override
		public void close(int filehandle) {
			// TODO Auto-generated method stub
			if(filehandle != 0)
				book_close(filehandle);
			filehandle = 0;
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
			System.loadLibrary("mobi");
		}

		@Override
		public long length(int filehandle) {
			if(book_seek(filehandle, 0, 2) < 0)
				return 0;
			long len = book_tell(filehandle);
			Log.d("vrix", "length======"+String.valueOf(len));
			if(len > 0)
				return len;
			else
				return 0;
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

		@Override
		public boolean getPicture(String name, int filehandle) {
			// TODO Auto-generated method stub
			return false;
		}	
}
