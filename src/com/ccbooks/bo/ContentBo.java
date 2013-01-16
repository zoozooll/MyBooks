package com.ccbooks.bo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.os.Handler;
import android.os.Message;
import android.view.ViewDebug.FlagToString;
import android.widget.Toast;

import com.ccbooks.listener.TvMainContentTouch;
import com.ccbooks.view.BookContentView1;

public class ContentBo implements Runnable {

	private BookContentView1 bcv;
	private String bookpath;
	public String defaultCode;
	
	public static boolean flag;
	public String content;
	
	
	public ContentBo(BookContentView1 bcv, String bookpath, String defaultCode
			) {
		super();
		this.bcv = bcv;
		this.bookpath = bookpath;
		this.defaultCode = defaultCode;
	
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = bcv.count;
		if(flag){
			//getStringSecondFile(defaultCode,count);
			handler.sendEmptyMessage(1);
		}
		flag=false;
		
	}
	/**鑾峰緱鏁版嵁*/
	public String getStringSecondFile(String code,int count)
	{
		try {
			
			StringBuffer sBuffer = new StringBuffer(1024);
			BufferedReader localBufferedReader;
			DataInputStream localDataInputStream;
			FileInputStream fInputStream = new FileInputStream(bookpath);
		      localDataInputStream = new DataInputStream((InputStream)fInputStream);
			InputStreamReader inputStreamReader = new InputStreamReader(localDataInputStream, code);
			   localBufferedReader = new BufferedReader((Reader)inputStreamReader);
			BufferedReader in = new BufferedReader(inputStreamReader);
			in.skip(count);
			int i = 8192;
			  char[] arrayOfChar = new char[i];
			  while (true)
		        {
		          int j = localBufferedReader.read(arrayOfChar);
		          i = -1;
		          int s = 0;
		          
		          count += j;
		          
		          if (j == i){
		            break;
		          }
		          
		          //String str = String.valueOf(arrayOfChar, 0, j);
		          sBuffer.append(arrayOfChar,0,j);
		          if(count<=8192){
		        	  return null;
		          } else if (count >8192){
		        	  if(sBuffer.length()>=1024){
			        	  content = new String(sBuffer);
			        	  sBuffer.delete(0,sBuffer.length());
			        	  bcv.tvMainContent.append(content);
			        	  //bcv.handler.sendEmptyMessage(1);
			          }
		          }
		        }
			  bcv.count=count;
			return null;
		}catch(FileNotFoundException e){
			e.printStackTrace();
			content = "\n\n\n电子书文件有误";
			Toast.makeText(bcv, content, Toast.LENGTH_SHORT);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			getStringSecondFile(defaultCode,bcv.count);
		
			super.handleMessage(msg);
		}
		
	};
}
