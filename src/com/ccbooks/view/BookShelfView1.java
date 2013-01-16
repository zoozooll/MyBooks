/**
 * 
 */
package com.ccbooks.view;

import com.ccbooks.adapter.ShelfFloorAdapter;
import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.FileBo;
import com.ccbooks.dao.BooksDao;
import com.ccbooks.download.CheckUpdate;
import com.ccbooks.download.DownloadDialog;
import com.ccbooks.listener.BtnOnlineBookStoreClick;
import com.ccbooks.listener.bookListView.BtnAboutUsOnclick;
import com.ccbooks.view.BookShelfView.updateHandler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;

/**
 * 
 * @date 2011-5-31 上午09:38:06
 * @author Lee Chok Kong
 */
public class BookShelfView1 extends Activity {
	
	
	/*-
	 *all views 
	 * */
	private Button btnBookStore;
	private Button btnBookshelf;
	private Button btnBooklist;
	private Button btnEdit;
	private Button btnReflesh;
	private Button btnAbout;
	private GridView gvwBookItems;
	public com.ccbooks.subview.OpenBookFrame flOpenBookAnim;
	//private ImageView ivChangeImg;
	private FrameLayout flChange;
	
	
	
	public static boolean hadCheckUpdate=false;
	//adapter
	private BooksBo bunit;
	public boolean isEdit;
	public int orientation;
	public DownloadDialog dialog;

	public static Activity activity =null;
	public int fileSize=0;			//下载文件的总大小
	public int donwloadFileSize=0;		//实时下载文件大小
	public int downPrecent = 0;
	public static updateHandler mHandler;
	public int selectItem= -1;			//选中的选项，以显示图标用；
	
	/**是否启用listener,如果为true，则所有事件不能相应*/
	private boolean listenerEnable=true;	
		
	public static int readerMode = 1; 	//1、代表翻页模式。2、代表划屏模式。
	/**对话框*/
	private Dialog pd;

	public static String SHOWTIPSCHECKBOX = "showTipsCheckBox";

	// public List<Book> books;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			orientation=1;
			setContentView(R.layout.shelf_v1);
		} else {
			orientation=2;
			setContentView(R.layout.shelf_h1);
		}
		mHandler = new updateHandler();
		activity = BookShelfView1.this;
		
		checkFirstTimeRunning();
		
		if(hadCheckUpdate == false){
			Runnable runUpdate = new Runnable() {
				
				@Override
				public void run() {
					Looper.prepare();
					CheckUpdate checkUpdate = new CheckUpdate(BookShelfView1.this);
					checkUpdate.start();
					Looper.loop();
				}
			};
			new Thread(runUpdate).start();
			//((CcbookApplication)getApplication()).setGlobalUpdate(true);
		}
		SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
		readerMode = sp.getInt("readerMode", 1);
		findview();
		addListener();
		//doing();
	}
	
	private void checkFirstTimeRunning() {
		int packageVersion;
		int saveVersion;
		SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
		saveVersion = sp.getInt("saveVersion", 0);
		packageVersion = CheckUpdate.getCurrentVersion(this);
		Log.i("garmen", "saveVersion is"+saveVersion+" packageVersion is"+packageVersion);
		if(saveVersion < packageVersion){
			Intent intent = new Intent();
			intent.setClass(this, BookHelpView.class);
			intent.putExtra(SHOWTIPSCHECKBOX, true);
			this.startActivity(intent);
		}	
	}

	@Override
	protected void onResume() {
		//flOpenBookAnim.showItemViewClose ();
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
/*		if (pd!=null && pd.isShowing()){
			pd.cancel();
		}
		BooksDao.getDao(this).close();*/
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
/*		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { 
			orientation=1;
			setContentView(R.layout.shelf_v);
		} else {
			orientation=2;
			setContentView(R.layout.shelf_h);
		}
		setListenerEnableTrue();	//复位
		findview();
		addListener();
		doing();*/
	}
	private void findview() {
		LayoutInflater inflater = LayoutInflater.from(this);
		btnBookStore = (Button) findViewById(R.id.btnBookStore);
		btnBookshelf = (Button) findViewById(R.id.btnBookshelf);
		btnBooklist = (Button) findViewById(R.id.btnBooklist);
		btnEdit = (Button) findViewById(R.id.btnEdit);
		btnReflesh = (Button) findViewById(R.id.btnReflesh);
		btnAbout = (Button) findViewById(R.id.btnAbout);
		gvwBookItems = (GridView) findViewById(R.id.gvwBookItems);
		flOpenBookAnim = (com.ccbooks.subview.OpenBookFrame) findViewById(R.id.flOpenBookAnim);
		//ivChangeImg = (ImageView) findViewById(R.id.ivChangeImg);
		
	}
	
	private void addListener(){
		btnBookshelf.setBackgroundDrawable(getResources().getDrawable(R.drawable.shelfonclick));
		btnBooklist.setOnClickListener(btnBookshelfOnclick);
		btnReflesh.setOnClickListener(btnRefleshOnclick);
		btnEdit.setOnClickListener(btnEditOnclick);
		btnBookStore.setOnClickListener(new BtnOnlineBookStoreClick(this));
		btnAbout.setOnClickListener(new BtnAboutUsOnclick());
		
	}

	private void doing() {
		
		bunit = new BooksBo(this, false);
		bunit.firstShellBooks();
		
		showview(orientation);
	}

	/**
	 * @param orientation 
	 * */
	private void showview(int orientation) {
		
	}


	//各种事件监听
	//按跳转书库按钮
	private OnClickListener btnBookshelfOnclick = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			//跳转翻页样式效果；
			Intent  mainIntent  =  new  Intent(BookShelfView1.this,BookListView.class); 
			transItemsWithAnimation(mainIntent, R.anim.zoomin, R.anim.zoomout,
					0, true);
		}

	};

	private OnClickListener btnRefleshOnclick = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			pd = new Dialog(BookShelfView1.this);
			pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
			pd.setContentView(R.layout.dia_loading);
			FileBo fileUtil = new FileBo(BookShelfView1.this, "/sdcard");
			new Thread(fileUtil).start();
			pd.setCancelable(false);
			pd.show();
		}
		
	};
	
	
	private OnClickListener btnEditOnclick = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			isEdit = !isEdit;
			if(isEdit){
				btnEdit.setText(R.string.Complete);
				btnEdit.setBackgroundDrawable(BookShelfView1.this.getResources()
						.getDrawable(R.drawable.onclickbutton));
			}else{
				btnEdit.setText(R.string.Edit);
				btnEdit.setBackgroundDrawable(BookShelfView1.this.getResources()
						.getDrawable(R.drawable.outclickbutton));
			}
		}

	};
	
	
	//刷新Handler
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			doing();
			if(pd != null && pd.isShowing()){
				pd.cancel();
			}
			bunit.reflesh = true;
			bunit.firstShellBooks();
			showview(orientation);
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 下载的时候显示
	 */
	public void showWaitDialog(){
		
    	//dialog=new AlertDialog(BookShelfView.this);
		dialog = new DownloadDialog(BookShelfView1.this,android.R.style.Theme_Dialog);
		dialog.setMessage("正在下载,请稍候...");
		dialog.setCancelable(false);
    	dialog.show();
    }
	
	
	
	/**
	 * 
	 * @param intent 传入intent
	 * @param delayMillis 延时启动
	 * @param isFinish 是否关闭当前activity
	 * @date 2011-5-19下午05:27:08
	 * @author Lee Chok Kong
	 */
	public void transItemsWithAnimation(final Intent intent, final int inAnim,
			final int outAnim, long delayMillis, final boolean isFinish) {
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				BookShelfView1.this.startActivity(intent); 
				if (isFinish){
					BookShelfView1.this.finish();        				     
				}
				//overridePendingTransition(inAnim,outAnim);           
			}
		}, delayMillis);
	}
	
	public void transItemsWithAnimation(final Intent intent, final int inAnim,
			final int outAnim, long delayMillis, final boolean isFinish ,boolean forResult) {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				BookShelfView1.this.startActivityForResult(intent, 2);
				if (isFinish) {
					BookShelfView1.this.finish();
				}
				overridePendingTransition(inAnim, outAnim);
			}
		}, delayMillis);
	}
	
	/**获得状态，是否有停止状态
	 * 
	 * @return 如果为false 表示锁住，为true表示没锁
	 * @date 2011-5-26下午02:11:00
	 * @author Lee Chok Kong
	 */
	public boolean isListenerEnable() {
		return listenerEnable;
	}

	/**
	 * 设置可以解锁所有监听事件
	 * @date 2011-5-26下午02:11:25
	 * @author Lee Chok Kong
	 */
	public void setListenerEnableTrue() {
		this.listenerEnable = true;
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	}
	
	/**
	 * 设置可以锁住所有监听事件
	 * @date 2011-5-26下午02:11:25
	 * @author Lee Chok Kong
	 */
	public void setListenerEnableFalse() {
		this.listenerEnable = false;
	}
	
/*	public void newShowWaitDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(BookShelfView.this);
    	    	try {
    				Method setProgressNumberFormat = ProgressDialog.class.getMethod("setProgressNumberFormat",
    						String.class);
//    				setProgressNumberFormat.setAccessible(true);
    				try {
    					setProgressNumberFormat.invoke(dialog, "");
    				} catch (IllegalArgumentException e) {
    					e.printStackTrace();
    				} catch (IllegalAccessException e) {
    					e.printStackTrace();
    				} catch (InvocationTargetException e) {
    					e.printStackTrace();
    				}
    			} catch (SecurityException e) {
    				e.printStackTrace();
    			} catch (NoSuchMethodException e) {
    				e.printStackTrace();
    			}
    	    	LayoutInflater inflater = LayoutInflater.from(BookShelfView.this);
    	    	View view = inflater.inflate(R.layout.alert_dialog_progress, null);
    	    	builder.setView(view);
    	    	builder.setMessage("正在下载,请稍候...");
    	        
    	    	//builder.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	    	builder.setCancelable(false);
    	    	dialog = builder.create();
    	    	dialog.setButton("取消", new DialogInterface.OnClickListener() {
    				
    				@Override
    				public void onClick(DialogInterface dialog, int which) {
    					FileUtils.canceldown = true;
    					dialog.cancel();
    				}
    			});
	}*/
	
	public class updateHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				//dialog.setProgress(donwloadFileSize);

			}else if(msg.what ==2){
				dialog.cancel();
				dialog.dismiss();
			}else if(msg.what == 3){
/*				synchronized (sfa) {
					sfa.notifyAll();
				}*/
				doing();
				bunit.reflesh = true;
				bunit.firstShellBooks();
				showview(orientation);
			}
			super.handleMessage(msg);
		}
		
	};

	
}
