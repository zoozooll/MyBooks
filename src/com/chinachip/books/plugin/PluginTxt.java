package com.chinachip.books.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;


public class PluginTxt extends Plugin implements IPlugin {
	private File fileIn = null;
	private FileChannel fc = null;
	private RandomAccessFile raf = null;

	private boolean isOpen = false;
	private String title = null;
	
	private int DEFAULT_HANDLE = 0x0001;
	
	private MappedByteBuffer mbb = null;
	
	public BackList bltree = new BackList();
	
	public String getSupport()
	{
		return ".txt";
	}
	@Override
	public int open(String name) {
		// TODO Auto-generated method stub
		fileIn = new File(name);
	
		try {
			raf = new RandomAccessFile(fileIn, "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		fc = raf.getChannel();
		isOpen = true;
		
		title = name.substring(name.lastIndexOf("/"));
		title = title.substring(1,title.lastIndexOf("."));
		handle = DEFAULT_HANDLE; // ¾ä±ú±ØÐë±»±£´æ
		return handle;
	}

	
	@Override
	public int get(int prop, Param param) {
		// TODO Auto-generated method stub
		switch(prop)
		{
		case PluginUtil.BOOK_TITLE:
			param.setStr(title);
			return 1;
		case PluginUtil.BOOK_BACKLIST:
			Node root = (Node) bltree.setRoot(new Node());			
	        root.setTitle(title.getBytes());
	        root.setFile(DEFAULT_HANDLE);
			param.bltree = bltree;
			return DEFAULT_HANDLE;
		case PluginUtil.BOOK_BACKLIST_CHARSET:
			param.setStr("UTF-8");
			return 1;
		case PluginUtil.BOOK_TYPE:
			param.i=1;
			return 1;
		}
		
			
		return 0;
	}

	@Override
	public ByteBuffer map(int filehandle, long start, long len) {
		
		// TODO Auto-generated method stub
		if (mbb != null) {
			mbb.flip();
			mbb.clear();
		}
		try {
			mbb = fc.map(FileChannel.MapMode.READ_ONLY, start, len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mbb.slice();
	}

	@Override
	public void close(int filehandle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		try {
			fc.close();
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isOpen = false;
		handle = 0;
	}
	@Override
	public long length(int filehandle) {
		// TODO Auto-generated method stub
		long fileLength = 0;
		try {
			fileLength = fc.size();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileLength;
	}
	@Override
	public int open(int file) {
		// TODO Auto-generated method stub
		return DEFAULT_HANDLE;
	}
	@Override
	public boolean getPicture(String name, int filehandle) {
		// TODO Auto-generated method stub
		return false;
	}
}
