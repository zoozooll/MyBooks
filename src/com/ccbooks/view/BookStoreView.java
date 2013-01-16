package com.ccbooks.view;

import java.io.File;
import java.io.IOException;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.ccbooks.bo.FileBo;
import com.ccbooks.listener.BtnBookStoreClick;
import com.ccbooks.vo.Book;
import com.ccbooks.web.DownloadManager;
import com.ccbooks.web.DownloadThread;
import com.ccbooks.web.SingleThreadDown;
import com.ccbooks.web.ToastMessage;
import com.ccbooks.web.XmlPull;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class BookStoreView extends Activity{
	private final static String TAG = "BookStoreView";
	private WebView bookWebView;
	private Button btnBookBack;
	private Button btnDownManager;
	private Button btnRefresh;
	private Dialog loadingDialog;
	AlertDialog.Builder builder;
	private Book book;
	private ToastMessage toastMessage;
	private static HashMap<String, SingleThreadDown> threadList=new HashMap<String, SingleThreadDown>();
	public static List<Book> books = new ArrayList<Book>();
	public static boolean downloadMangerStart = false;
	private String strUrl = null;
	private String refreshUrl = null;
	private boolean onlineReading = false;
	
	 public static final byte[] URL = {
			(byte)0x5E, (byte)0x75, (byte)0x54, (byte)0xE6, (byte)0x71, (byte)0x2B, (byte)0x71, (byte)0x6F, (byte)0x2D, (byte)0x86, (byte)0xBF, (byte)0x7A, (byte)0xCB, (byte)0x2E, (byte)0x6C, (byte)0xB3, 
			(byte)0x5A, (byte)0x03, (byte)0x9E, (byte)0xEF, (byte)0x1C, (byte)0x91, (byte)0xA9, (byte)0x35, (byte)0x7D, (byte)0x04, (byte)0xB2, (byte)0x2D, (byte)0x82, (byte)0xDE, (byte)0xB5, (byte)0xA7, 
			(byte)0xC1, (byte)0x5A, (byte)0x68, (byte)0xCC, (byte)0x47, (byte)0xCB, (byte)0xED, (byte)0xA5, (byte)0x26, (byte)0xE7, (byte)0xD1, (byte)0x44, (byte)0xBE, (byte)0xDB, (byte)0x40, (byte)0xB5, 
			(byte)0x3E, (byte)0x10, (byte)0xB6, (byte)0x29, (byte)0xC4, (byte)0xD0, (byte)0x5B, (byte)0x63, (byte)0xF5, (byte)0x24, (byte)0x3C, (byte)0xAB, (byte)0xC2, (byte)0xE2, (byte)0x23, (byte)0x97
		};

	 public static final byte[] URL1 = {
		 (byte)0xf0,(byte)0x12,(byte)0x8d,(byte)0x99,(byte)0x04,(byte)0x16,(byte)0x86,(byte)0x7c,(byte)0x60,(byte)0xcd,(byte)0x4b,(byte)0x26,(byte)0xdb,(byte)0xda,(byte)0xb3,(byte)0xf6,
		 (byte)0xa1,(byte)0x3d,(byte)0x9a,(byte)0x4f,(byte)0x29,(byte)0xe8,(byte)0x35,(byte)0x72,(byte)0x45,(byte)0x52,(byte)0x51,(byte)0xb0,(byte)0x7c,(byte)0x34,(byte)0x33,(byte)0x76,
		 (byte)0x47,(byte)0xa0,(byte)0x1f,(byte)0x52,(byte)0xdc,(byte)0xef,(byte)0x48,(byte)0xda,(byte)0xa2,(byte)0xd3,(byte)0xd0,(byte)0x8e,(byte)0x17,(byte)0x61,(byte)0xec,(byte)0xd4,
		 (byte)0x33,(byte)0xa3,(byte)0x23,(byte)0x7d,(byte)0x23,(byte)0x28,(byte)0x27,(byte)0x55,(byte)0x72,(byte)0x41,(byte)0x89,(byte)0x12,(byte)0x51,(byte)0xbf,(byte)0x3e,(byte)0x9c,
		};
	 public static final byte[] CHINACHIP = {(byte)0x63,(byte)0xb5,(byte)0x7d,(byte)0x3a,(byte)0x5c,(byte)0x98,(byte)0x92,(byte)0x1e,(byte)0x82,(byte)0x06,(byte)0xaf,(byte)0x62,(byte)0xc3,(byte)0x6d,(byte)0xf3,(byte)0x68};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setOrientationContentView();
		String chinachip = FileBo.getRealData(CHINACHIP);
		if(chinachip.equals("chinachip")){
			strUrl = FileBo.getRealData(URL1);
		}else{
			strUrl = FileBo.getRealData(URL);
		}
		
		refreshUrl = strUrl;
		
		bookWebView = (WebView)findViewById(R.id.webView1);
		btnBookBack = (Button) findViewById(R.id.btnBookBack);		
		btnDownManager =(Button)findViewById(R.id.downmanager);
		btnRefresh = (Button)findViewById(R.id.refresh);
		
		btnBookBack.setOnClickListener(new BtnBookStoreClick(BookStoreView.this));
		btnRefresh.setOnClickListener(new refreshClickListener());
		
		
		btnDownManager.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DownloadManager(BookStoreView.this).start();
			}
		});
		
		//设置浏览器的各个属性
		WebSettings bookWebViewSetting = bookWebView.getSettings();
		bookWebViewSetting.setJavaScriptEnabled(true);

		bookWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		bookWebView.setWebViewClient(new bookWebViewClient());
		
		setDialog();
		try{
			loadingDialog.show();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL newUrl = new URL(strUrl);
					HttpURLConnection urlc = (HttpURLConnection) newUrl.openConnection();
					urlc.setRequestProperty("User-Agent", "Android Application:");
					urlc.setRequestProperty("Connection", "close");
					urlc.setConnectTimeout(1000 * 10); // mTimeout is in seconds
					urlc.connect();
					if (urlc.getResponseCode() == 200) {

						bookWebView.loadUrl(strUrl);
					}else{

						bookWebView.loadUrl("file:///android_asset/err.html");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block

					bookWebView.loadUrl("file:///android_asset/err.html");
					e.printStackTrace();
				}

			}
		};
		new Thread(r).start();
		//bookWebView.setWebChromeClient(new bookWebChromeClient());
	}

	private void setOrientationContentView() {
		// TODO Auto-generated method stub
/*		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setContentView(R.layout.book_store);
		}else{
			setContentView(R.layout.book_store_horizontal);
		}*/
		setContentView(R.layout.book_store_horizontal);
	}

	//重写按返回键变为返回到上一页
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && bookWebView.canGoBack()){
			bookWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

	class bookWebViewClient extends WebViewClient{
		//点击链接在本页面显示
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, final String url) {
			// TODO Auto-generated method stub
			refreshUrl = url;
			Log.i("BookStoreView", url);
			if(url.indexOf("xml") != -1){
				builder = new AlertDialog.Builder(BookStoreView.this);
				builder.setTitle(R.string.downloadConfirm);
				//下载按钮
				builder.setPositiveButton(R.string.downloadNow,
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								onlineReading = false;
								startDownload(url);
							}
						});
				//取消按钮
				builder.setNegativeButton(android.R.string.cancel, 
						new AlertDialog.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								
							}
					
				});
				//中间按钮为在线阅读
				
				builder.setNeutralButton(R.string.readNow, 
					new AlertDialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							onlineReading = true;
							startDownload(url);
						}
					
				});
				builder.show();
				loadingDialog.dismiss();
			}else{
				bookWebView.loadUrl(url);
			}
			
			return true;
			//return super.shouldOverrideUrlLoading(view, url);
		}
		
		//装载页面结束
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			if(loadingDialog.isShowing()){
				loadingDialog.dismiss();
			}
			super.onPageFinished(view, url);
		}
		
		//开始装载页面
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			
			if(url.indexOf("downloadXml") !=-1){
				
			}else{
				if(!loadingDialog.isShowing()){
					setDialog();
					try{
						loadingDialog.show();
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			}
			
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			bookWebView.loadUrl("file:///android_asset/err.html");
			
/*			if(loadingDialog.isShowing()){
				loadingDialog.cancel();
				loadingDialog.dismiss();
			}
			AlertDialog.Builder builder = new Builder(BookStoreView.this);
			builder.setTitle("Loading Error");
			builder.setMessage(R.string.loadingErr);
			builder.setPositiveButton(R.string.reload,new AlertDialog.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					bookWebView.loadUrl(strUrl);
				}				
			});
			builder.setNegativeButton(R.string.back, new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
				
			});
			builder.show();*/
			//super.onReceivedError(view, errorCode, description, failingUrl);
		}

		private void startDownload(final String url) {
			bookWebView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean isSuccess = prepareToLoad(url);
					if(isSuccess){
						doLoad(book);
					}else{
						toastMessage = new ToastMessage(BookStoreView.this);//下载准备错误，提示用户下载出错
						toastMessage.handler.sendEmptyMessage(ToastMessage.DOWNLOADFAILED);
						book = null;
					}
				}
			}, 100);
		}
		
		
	}
	/**
	 * 读取网络的xml文件，解析出bookname，downpath，suffix等属性，为下载做准备
	 * @param url
	 * @return false 解析错误 true 解析正确
	 * Created on 2011-3-24  上午10:10:33
	 */
	private boolean prepareToLoad(String url) {
		// TODO Auto-generated method stub
		XmlPull xmlPull = new XmlPull(url);
		try {
			book = xmlPull.parse();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//book.bookname=book.bookname+book.booktype;
		if(Environment.getExternalStorageDirectory()
				.canWrite()){
			Log.i("BookStoreView", "BookStoreView sdcard can write");
			book.bookpath="/sdcard/ccbook/" +book.bookname+book.booktype.toLowerCase();
		}else{
			book.bookpath="/local/ccbook/" +book.bookname+book.booktype.toLowerCase();
			Log.i("BookStoreView", "BookStoreView sdcard can not write");
		}
		
		book.downloadPrecent = 0;
		Log.i(TAG, book.toString());
		return true;
	}
	
	/**
	 * 暂时没有使用到
	 * @author Administrator
	 *
	 */
	class bookWebChromeClient extends WebChromeClient{

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			BookStoreView.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress*100);
			super.onProgressChanged(view, newProgress);
		}
		
	}
	/**
	 * 获得网络book的属性后开线程下载
	 * @param book
	 * Created on 2011-3-24  上午10:09:13
	 */
	private void doLoad(Book book){
		new Thread(new DownloadThread(book, BookStoreView.this)).start();
	}
	
	/**
	 * 设置loading窗口的属性
	 * 
	 * Created on 2011-3-24  上午10:08:46
	 */
	private void setDialog(){
		loadingDialog = new Dialog(BookStoreView.this,R.style.dialog);
		loadingDialog.setCancelable(true);
		loadingDialog.setContentView(R.layout.dia_loading);
		loadingDialog.setCanceledOnTouchOutside(true);
	}

	protected static void postDelayed(Runnable runnable, int i) {
		// TODO Auto-generated method stub
		
	}
	
	public static HashMap<String, SingleThreadDown> getDownloadThread(){
		return threadList;
	}
	
	class refreshClickListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			bookWebView.loadUrl(refreshUrl);
		}
		
	}
	
	public boolean getOnlineReading(){
		return onlineReading;
	}
	
	public void setOnlineReading(boolean onlineReading){
		this.onlineReading = onlineReading;
	}
}
