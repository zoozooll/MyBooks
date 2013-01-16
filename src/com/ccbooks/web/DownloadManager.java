package com.ccbooks.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ccbooks.view.BookStoreView;
import com.ccbooks.view.R;
import com.ccbooks.vo.Book;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadManager extends Thread{
	Context context;
	View manageView;
	LinearLayout ll;
	myhandler handler;
	int loadNum;
	List myList;
	private static boolean shouldStop=false;
	ArrayList allTextView;
	ArrayList allProgressbar;
	private static AlertDialog showDownload;
	
	public DownloadManager(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		allTextView=new ArrayList<TextView>();
		allProgressbar=new ArrayList<ProgressBar>();
		handler = new myhandler();
		BookStoreView.downloadMangerStart = true;
		loadNum=BookStoreView.getDownloadThread().size();
		//如果有下载，显示之
		if(loadNum>0){
			//用inflate添加dialog里要显示的内容
			LayoutInflater li = LayoutInflater.from(context);
			ScrollView myScrollView = (ScrollView) li.inflate(R.layout.downloadmanageview2, null,false);
			ll = (LinearLayout) myScrollView.findViewById(R.id.LinearLayout01);
			for(int num = 0;num<loadNum;num++){
				LinearLayout l2=(LinearLayout)li.inflate(R.layout.loadingmsg, null);
		    	
		    	TextView bookname=(TextView)l2.findViewById(R.id.magazinename);
				TextView persent=(TextView)l2.findViewById(R.id.progresstext);
				ProgressBar Mybar=(ProgressBar)l2.findViewById(R.id.myselfProgressBar);
				Button btnCancel=(Button)l2.findViewById(R.id.pauseandbegan);
				
				allTextView.add(persent);
				allProgressbar.add(Mybar);
				
				final Book book = BookStoreView.books.get(num);
				bookname.setText(book.bookname);
				persent.setText(book.downloadPrecent+"%");
				Mybar.setProgress(book.downloadPrecent);
				btnCancel.setText("取消");
				btnCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((LinearLayout)(v.getParent().getParent().getParent())).setVisibility(View.GONE);
						if(BookStoreView.getDownloadThread().get(book.bookname) !=null){
							BookStoreView.getDownloadThread().get(book.bookname).setStopDownload(true);
							BookStoreView.getDownloadThread().remove(book.bookname);
						}
						try{
							File file = new File(book.bookpath);
							file.delete();
						}catch(NullPointerException e){
							e.printStackTrace();
						}
						BookStoreView.books.remove(book);
					}
				});
				ll.addView(l2, num);
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(myScrollView);
			builder.setPositiveButton("隐藏", new AlertDialog.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					shouldStop = true;
					BookStoreView.downloadMangerStart = false;
				}
				
			});
			
			showDownload = builder.create();
			showDownload.show();
		}else{
			BookStoreView.downloadMangerStart = false;
			Toast.makeText(context, "没有书籍在下载", Toast.LENGTH_LONG).show();
			shouldStop = true;
		}
	}
	
	/**
	 * 刷新页面
	 * @return 
	 */
	private void flush(){
		for(int num=0;num < BookStoreView.books.size();num++){
			File file = new File(BookStoreView.books.get(num).bookpath);
			//precent = (int) (file.length()*100/BookStoreView.books.get(num).fileLength);
			Log.i("DownloadManager", "BookStoreView.getDownloadThread().size():"+BookStoreView.getDownloadThread().size());
			Log.i("garmen", "flush num----->"+num+",allTextView.size()"+allTextView.size());
			if(BookStoreView.books.get(num).downloadPrecent == 100){
				((LinearLayout)(((TextView)allTextView.get(num)).getParent().getParent().getParent())).setVisibility(View.GONE);
				allTextView.remove(num);
				allProgressbar.remove(num);
				
				BookStoreView.books.remove(num);//如果打开了下载管理dialog，由dialog来负责remove books的内容
				Log.i("garmen", "finish finish num----->"+num);
				break;
			}else{
				
				((TextView)allTextView.get(num)).setText(BookStoreView.books.get(num).downloadPrecent+"%");
				((ProgressBar)allProgressbar.get(num)).setProgress(BookStoreView.books.get(num).downloadPrecent);
			}
			
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!shouldStop){
			try {
				Thread.sleep(500);
				handler.sendEmptyMessage(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.run();
	}

	class myhandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			flush();
		}
	}
	
	public static void stopDownLoadManager(){
		shouldStop = true;
		showDownload.cancel();
	}
}
