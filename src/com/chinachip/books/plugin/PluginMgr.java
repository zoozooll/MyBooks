package com.chinachip.books.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.R.string;

import com.chinachip.tree.BackList;

public class PluginMgr{
	public ArrayList<Plugin> plugin = new ArrayList<Plugin>();

	public Plugin default_plugin = null;
	
	public BackList bltree = null;
	
	public int filehandle = 0;
	
	public void setDefaultPlugin(Plugin plugin)
	{
		default_plugin = plugin;
	}
	
	public Plugin getDefaultPlugin()
	{
		return default_plugin;
	}
	
	public int registerPlugin(Plugin plugin)
	{
		Class s = plugin.getClass();
		String name = s.getName();
		
	
		if(this.plugin.size() != 0){
			for(int i = 0; i< this.plugin.size(); i++)
			{
				if(this.plugin.get(i).getClass().getName().equals(name))
				{
					return this.plugin.size();
				}
				
			}
		}
		
		this.plugin.add(plugin);
		
		return this.plugin.size();
	}
	
	public Plugin getPlugin(String filename)
	{
		for(Plugin plugin : this.plugin)   {   
	          if(plugin.isSupport(filename))
	          {
	        	  default_plugin = plugin;
	        	  return plugin;
	          }
		}
		return null;
	}

	public int open(Plugin plugin,String name) {
		// TODO Auto-generated method stub
		
		Class clazz = plugin.getClass();
		try {
			Method m = clazz.getMethod("open", new Class[]{String.class});
			return (int)((Integer)m.invoke(plugin, new Object[]{name}));
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int open(Plugin plugin,int file) {
		// TODO Auto-generated method stub
		
		Class clazz = plugin.getClass();
		try {
			Method m = clazz.getMethod("open", new Class[]{int.class});
			filehandle = (int)((Integer)m.invoke(plugin, new Object[]{file}));
			return filehandle;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}	
	
	public long length(Plugin plugin,int filehandle) {
		// TODO Auto-generated method stub
		Class clazz = plugin.getClass();
		try {
			Method m = clazz.getMethod("length", new Class[]{int.class});
			return (long)(Long)m.invoke(plugin, new Object[]{filehandle});
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int get(Plugin plugin,int prop, Param param) {
		// TODO Auto-generated method stub
		Class clazz = plugin.getClass();
		try {
			Method m = clazz.getMethod("get", new Class[]{int.class,Param.class});
			return (int)((Integer)m.invoke(plugin, new Object[]{prop,param}));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return 0;
	}

	public ByteBuffer map(Plugin plugin,int filehandle, long start, long len) {
		// TODO Auto-generated method stub
		Class clazz = plugin.getClass();
		try {
			Method m = clazz.getMethod("map", new Class[]{int.class,long.class,long.class});
			return (ByteBuffer)m.invoke(plugin, new Object[]{filehandle,start,len});
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			Throwable t = e.getTargetException();
			t.getMessage();
			e.printStackTrace();
		}	
		return null;
	}

	
	
	
	public void close(Plugin plugin,int filehandle) {
		// TODO Auto-generated method stub
		Class clazz = plugin.getClass();
		try {
			Method m = clazz.getMethod("close", new Class[]{int.class});
			m.invoke(plugin, new Object[]{filehandle});
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close(Plugin plugin) {
		// TODO Auto-generated method stub
		
		if(filehandle != 0)
		{
			close(default_plugin,filehandle);
		    filehandle = 0;
		}	
		
		Class clazz = plugin.getClass();
		try {
			Method m = clazz.getMethod("close", null);
			m.invoke(plugin, null);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeAll()
	{
		if(default_plugin != null)
		{
			close(default_plugin,filehandle);
			filehandle = 0;
		}
		if(default_plugin.getHandle() != 0)
		{
			close(default_plugin);
			default_plugin.handle = 0;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		closeAll();
	}
	

	
	
	
	public boolean getPicture(Plugin plugin,String name,int filehandle) {
		// TODO Auto-generated method stub
		Class clazz = plugin.getClass();
		try {
			Method m = clazz.getMethod("getPicture", new Class[]{String.class,int.class});
			return (boolean)((Boolean) m.invoke(plugin, new Object[]{name,filehandle}));
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			Throwable t = e.getTargetException();
			t.getMessage();
			e.printStackTrace();
		}	
		return false;
	}

}
