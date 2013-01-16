package com.chinachip.books.plugin;

import java.nio.ByteBuffer;


public interface IPlugin {
	public int open(String name);
	public int open(int file);
	public int get(int prop,Param param);
	public long length(int filehandle);
	public ByteBuffer map(int filehandle,long start,long len);
	public boolean getPicture(String name,int filehandle);
	public void close(int filehandle);
	public void close();
}
