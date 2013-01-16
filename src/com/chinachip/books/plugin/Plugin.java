package com.chinachip.books.plugin;

public class Plugin{

	final public static String spliter = ";";

	protected int handle = 0;
	
	private char[] temp = null;
	
	public int getHandle()
	{
		return handle;
	}
	
	public boolean isSupport(String filename)
	{
		filename = filename.toLowerCase();
		String myext = getSupport();
		if(myext.indexOf(spliter)<0)
		{
			if(filename.lastIndexOf(myext)==(filename.length()-myext.length()))
				return true;
			return false;
		}
		
		String[] exts = myext.split(spliter);
		for(String ext: exts)
		{
			if(filename.lastIndexOf(ext)==(filename.length()-ext.length()))
				return true;
			if(ext.lastIndexOf(".") > 0 && filename.contains(ext)){
				return true;
			}
		}
		return false;
	}
	
	public String getSupport()
	{
		return "nothing";
	}

	private String change(char[] temp)
	{	
		
		for(int i=0;i<temp.length;i++)	
		{		
			if(temp[i]>='A'&&temp[i]<='Z'){		
				temp[i] = Character.toLowerCase(temp[i]);
			}
		}
		return String.copyValueOf(temp); 
	}


}
