package com.ccbooks.web;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ToastMessage {
	public static final int DOWNLOADING = 0;
	public static final int HADDOWNLOAD = 1;
	public static final int FINISH = 2;
	public static final int DOWNLOADSTAR = 3;
	public static final int DOWNLOADFAILED = 4;
	public static final int TOOMANYDOWNLOAD = 5;
	Context context;
	public myHandler handler;
	
	public ToastMessage(Context context){
		this.context = context;
		handler = new myHandler();
	}
	
	public class myHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String name = "";
			if(msg.obj instanceof String){
				name = (String)msg.obj;
			}
			switch(msg.what){
			case DOWNLOADING:
				Toast.makeText(context, name+" 正在下载", Toast.LENGTH_SHORT).show();
				break;
			case HADDOWNLOAD:
				Toast.makeText(context, name+" 已经存在,不重新下载", Toast.LENGTH_SHORT).show();
				break;
			case FINISH:
				Toast.makeText(context, name+" 下载成功", Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOADSTAR:
				Toast.makeText(context, name+" 开始下载", Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOADFAILED:
				Toast.makeText(context, name+" 下载失败", Toast.LENGTH_SHORT).show();
				break;
			case TOOMANYDOWNLOAD:
				Toast.makeText(context, "只能同时下载5本书", Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
		
	}
}
