package com.chinachip.books.plugin;

import java.nio.ByteBuffer;

import com.ccbooks.bo.FileBo;
import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;

public class PluginCtxt extends Plugin implements IPlugin{
	  public int txthandl= 0;
	   private String title = null;
	   String Filename = null;
	   public BackList bltree = new BackList();
	   public static final byte[] kdata = {
				(byte)0xE9, (byte)0x2A, (byte)0x8E, (byte)0xDC, (byte)0xDF, (byte)0x2C, (byte)0x6B, (byte)0x54, (byte)0x17, (byte)0xE1, (byte)0x15, (byte)0x24, (byte)0x5E, (byte)0xC7, (byte)0x0C, (byte)0x08, 
				(byte)0xAD, (byte)0x0A, (byte)0x4B, (byte)0xA6, (byte)0x55, (byte)0x7E, (byte)0x16, (byte)0xF7, (byte)0x9E, (byte)0x97, (byte)0xCA, (byte)0xDB, (byte)0x6B, (byte)0xE7, (byte)0xCC, (byte)0x36
	};
	   public static final byte[] kdata1 = {
		   (byte)0x72,(byte)0xbc,(byte)0xe5,(byte)0x62,(byte)0x6e,(byte)0xec,(byte)0xf9,(byte)0x41,(byte)0xb6,(byte)0xa0,(byte)0x96,(byte)0x68,(byte)0xcc,(byte)0x84,(byte)0x35,(byte)0x47,
		   (byte)0x01,(byte)0x61,(byte)0x61,(byte)0xd2,(byte)0x5f,(byte)0x0e,(byte)0xab,(byte)0x79,(byte)0xdb,(byte)0xea,(byte)0xb1,(byte)0x0c,(byte)0x11,(byte)0xa6,(byte)0xed,(byte)0x84,
	};
	   
       public static final byte[] CHINACHIP = {(byte)0x63,(byte)0xb5,(byte)0x7d,(byte)0x3a,(byte)0x5c,(byte)0x98,(byte)0x92,(byte)0x1e,(byte)0x82,(byte)0x06,(byte)0xaf,(byte)0x62,(byte)0xc3,(byte)0x6d,(byte)0xf3,(byte)0x68};
	   
		public String getSupport()
		{
			return ".cct";
		}
		
		public int open(String name) {
			//System.out.println("name"+name);
			String chinachip = FileBo.getRealData(CHINACHIP);
			if(chinachip.equals("chinachip")){
				txthandl = book_load(name,FileBo.getRealData(kdata1));
			}else{
				txthandl = book_load(name,FileBo.getRealData(kdata));
			}
			
			title = name.substring(name.lastIndexOf("/"));
			title = title.substring(1,title.lastIndexOf("."));
			System.out.println("pdbhandl"+txthandl);
		//	Log.d("zzq", "loadeend");
			return txthandl;
		};
		
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
				if(txthandl == 0)
				{
					param.i = PluginUtil.BOOK_TYPE_INVALID;
				}
				else
				{
					param.i = PluginUtil.BOOK_TYPE_TEXT;
				}
				break;
			}
			case PluginUtil.BOOK_CCT_BLOCKINDEX:
			{
				id = book_get(txthandl,prop,param);
			}
			break;
				//return 1;
			case PluginUtil.BOOK_BACKLIST:
				
				Node root = (Node) bltree.setRoot(new Node());			
		        root.setTitle(title.getBytes());
		        param.bltree = bltree;
		        if(txthandl!=0)
		        {
			        root.setFile(txthandl);
				//	param.bltree = bltree;
					id = txthandl;
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

		public void close() {
			// TODO Auto-generated method stub
			if(txthandl != 0)
			{
				book_unload(txthandl);
			}
			txthandl = 0;
		}
		
		
		public int getcover(String destname,String srcname) {
			System.out.println("name"+srcname);
			return book_getcover(destname,srcname);
		//	Log.d("zzq", "loadeend");
		};

		// jni ӿڷ
		public native int book_open(int handle,int id);
		
		protected native long book_read(byte[] data, long bsize, long bnum,
				int filehandle);

		public native int book_seek(int handle,long offset,int mod);
		public native int book_tell(int handle);
		public native int book_eof(int handle);
		public native void book_close(int f);
		
		
		public native int book_get(int handle, int prop, Param param);
		public native int book_load(String name,String key);
		public native int book_unload(int handle);
		
		
		public native int book_getcover(String destname,String srcname);
		static {
			System.loadLibrary("ctxt");
		}

		public void close(int filehandle) {
			// TODO Auto-generated method stub
			book_close(filehandle);
		}

		public long length(int filehandle) {
			// TODO Auto-generated method stub
			System.out.println("current"+filehandle);
			long ret;
			if(filehandle!=0)
			{
				long current;;
				current = book_tell(filehandle);
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

		public int open(int file) {
			if(txthandl!= 0)
			{
				handle = book_open(txthandl,file);
			// TODO Auto-generated method stub
			}
			else
				handle =0;
				System.out.println("pdbhandl"+txthandl+"handle"+handle);
			return handle;
		}

		@Override
		public boolean getPicture(String name, int filehandle) {
			// TODO Auto-generated method stub
			return false;
		}

}
