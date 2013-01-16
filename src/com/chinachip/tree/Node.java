package com.chinachip.tree;


import java.io.UnsupportedEncodingException;

public class Node extends SuperNode {

	public byte[] title;
	public int file; // 指向c端章节节点的指针
	
	public Node()
	{
		title = null;
		file = 0;
	}
	
	public Node(byte[] title, int pointer)
	{
		this.title = title;
		this.file = pointer;
	}
	
	public void setTitle(byte[] title)
	{
		this.title = title;
	}
	
	public void setFile(int pointer)
	{
		this.file = pointer;
	}
	
	public int getFile()
	{
		return file;
	}
	
	public String getTitle(String enc) throws UnsupportedEncodingException
	{
		if(title != null)
		{
			return new String(title,enc);
		}
		return "";
	}
	
}
