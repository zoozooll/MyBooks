package com.ccbooks.view;



import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;


import com.ccbooks.bo.BooksBo;
import com.ccbooks.util.BookUtil;
import com.ccbooks.view.R;
import com.ccbooks.view.widget.CartoonWidget;
import com.ccbooks.vo.Book;
import com.ccbooks.vo.CatelogItem;

import com.chinachip.book.cartoon.AutoReader;
import com.chinachip.book.cartoon.BitmapUtil;
import com.chinachip.book.cartoon.CTBookMarkManager;
import com.chinachip.book.cartoon.CTMenuBklight;
import com.chinachip.book.cartoon.CTMenuBkmk;
import com.chinachip.book.cartoon.CTMenuTurnto;
import com.chinachip.book.cartoon.Util;
import com.chinachip.book.cartoon.ZoomImageView;

import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginUtil;
import com.chinachip.tree.Node;








import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class CartoonReader extends Activity implements OnGestureListener {

	public ViewFlipper flipper;

	public GestureDetector detector;
	
	public LinearLayout layoutFun;
	public LinearLayout layoutLight;
	public LinearLayout layoutTurnto;

	
	
	public PluginMgr pm = null;
	public Plugin plugin = null;
	public int filehandle = 0;
	public int fileLength = 0;
	public int headFile = 0;
	public int endFile = 0;
	public int file = 1;
	
	private Button big;
	private Button small;
	public ZoomImageView zoomview1;
	public ZoomImageView zoomview2;

	public Bitmap bitmap1;
	public Bitmap bitmap2;
	private float ratiof = 0.0f;
	private float zoommax = 0.4f;
	private float zoommin = -0.4f;
	
	public Handler mHandler = null;
	
	public Handler menuHandler = null;
	
	private Menu mainMenu;
	
	private BooksBo bo = null;
	
	public Book book = null;
	
	public CTBookMarkManager bkmkManager;
	
	public CTMenuBkmk menuBkmk;
	
	public  CTMenuBklight menuBlight;

	public CTMenuTurnto menuTurnto;
	
	public FrameLayout carton_main;
	
	public boolean mode = false;
	

	
	public String imagePath = "/sdcard/cctemp/";
	
	public String imagePath11 = "/sdcard/cctemp/image1.jpg";
	
	public String imagePath21 = "/sdcard/cctemp/image2.jpg";
	
	public String ex = ".jpg";

	// 屏幕宽高
	public int display_w;
	public int display_h;
	
	public AutoReader ar = null;
	
	public boolean isMoveing = false;

	public boolean isAutoRunning = false;
	
	public boolean isWait = false;
	
	public boolean isNotify = false;
	
	private TranslateAnimation move = null; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 Util.setFullNoTitleScreen(this);
	
		 setContentView(R.layout.cartoon_main);
			SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
			mode = sp.getBoolean("mode", false);
		 Bundle bundle = this.getIntent().getExtras();
		 int id = bundle.getInt("bookId");
		 file = bundle.getInt("file");
		 bo = new BooksBo(this, false);
			try{
				book = bo.getOne(id);
				if (!(new File(book.bookpath).exists())){
					Toast.makeText(this, R.string.fileNotFound, Toast.LENGTH_SHORT).show();
					book = null;
					this.finish();
					return;
				}
			}catch ( SQLException e ) {
				e.printStackTrace();
			}
	     file = book.file;
	     if(file == 0){
	    	 file = 1;
	     }
		 initUI();
		 openFile(file);
		 initMenu();
		 sendBroadcast();
		 
	}
	
	/**切换屏幕时候的显示*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		 initUI(file) ;
		super.onConfigurationChanged(newConfig);
	}
	private void initUI(){
		carton_main = (FrameLayout)findViewById(R.id.carton_main);

		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper01);
		
	
	}
	private void openFile(int initFile) {
		detector = new GestureDetector(this);
		
		flipper.removeAllViews();
		// 获取屏幕宽高
		Display display = getWindowManager().getDefaultDisplay();
		display_w = display.getWidth();
		display_h = display.getHeight();
		
		 	pm = new PluginMgr();
	        PluginUtil.PluginInit(pm);
			plugin = pm.getPlugin(book.bookpath);
			Param param = new Param();
			filehandle = pm.open(plugin, book.bookpath);
			
			//	bc.pm.open(plugin, url);
				
				pm.get(plugin, PluginUtil.BOOK_BACKLIST, param);
				pm.bltree = param.bltree;
				Node root = (Node)pm.bltree.getRoot();
				headFile = root.file;
				endFile = pm.bltree.getEndFile();
				if(initFile <= 1){
					this.file = headFile;
					initFile = this.file;
				}else{
					this.file = initFile;
				}
				
				
				
		

		// 初始化功能按钮
		big = (Button) findViewById(R.id.big);
		big.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if (ratiof < zoommax) {
					
					small.setEnabled(true);
					
					ratiof = getRoundFloat(ratiof + 0.2f);
				
					
					
					
					if(zoomview1.isShowing){
						
						
						String filePath = zoomview1.filePath;
				
				
						
						bitmap1 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
						
							//bitmap1 = BitmapUtil.initZoomBitmap(bitmap1, 1.0f + ratiof);
							zoomview1.init(display_w, display_h, bitmap1);
							zoomview1.invalidate();
							

							filePath = zoomview2.filePath;
					
							bitmap2 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
						
						//	bitmap2 = BitmapUtil.initZoomBitmap(bitmap2, 1.0f + ratiof);
							
							zoomview2.init(display_w, display_h, bitmap2);
							zoomview2.invalidate();
					}else{
						
						
						String filePath = zoomview2.filePath;
						
						bitmap2 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
						
						//	bitmap1 = BitmapUtil.initZoomBitmap(bitmap1, 1.0f + ratiof);
							zoomview2.init(display_w, display_h, bitmap2);
							zoomview2.invalidate();
							
						
							filePath = zoomview1.filePath;
						
							
							
							bitmap1 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
						
						//	bitmap2 = BitmapUtil.initZoomBitmap(bitmap2, 1.0f + ratiof);
							
							zoomview1.init(display_w, display_h, bitmap1);
							zoomview1.invalidate();
					}
					
					
				}else{
					big.setEnabled(false);
					
				}
			}
		});

		small = (Button) findViewById(R.id.small);
		small.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				big.setEnabled(true);
				Log.e("0.8====", "" + ratiof);
				if (ratiof > zoommin) {
					
					ratiof = getRoundFloat(ratiof - 0.2f);
					
					
					
					if(zoomview1.isShowing){
						String filePath = zoomview1.filePath;
						bitmap1 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
						//bitmap1 = BitmapUtil.initZoomBitmap(bitmap1, 1.0f + ratiof);
						zoomview1.init(display_w, display_h, bitmap1);
						zoomview1.invalidate();
					
					
						
						filePath = zoomview2.filePath;
						bitmap2 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
						
					//	bitmap2 = BitmapUtil.initZoomBitmap(bitmap2, 1.0f + ratiof );
						
						zoomview2.init(display_w, display_h, bitmap2);
						zoomview2.invalidate();
					}else{
						String filePath = zoomview2.filePath;
						bitmap2 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
						//bitmap1 = BitmapUtil.initZoomBitmap(bitmap1, 1.0f + ratiof);
						
						zoomview2.init(display_w, display_h, bitmap2);
						zoomview2.invalidate();
					
						filePath = zoomview1.filePath;
						
						
						bitmap1 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
						
					//	bitmap2 = BitmapUtil.initZoomBitmap(bitmap2, 1.0f + ratiof );
						zoomview1.init(display_w, display_h, bitmap1);
						zoomview1.invalidate();
						
					}					
				}else{
					small.setEnabled(false);
					
				}
			}
		});

		// 设置图片
		zoomview1 = new ZoomImageView(this);
		filehandle = pm.open(plugin, file);
		String filePath = getfileName(file);
		File imageFile = new File(filePath);
		if(!imageFile.exists()||imageFile.length() == 0){
			File cctemp = new File(imagePath);
			if(!cctemp.exists()){
				cctemp.mkdir();
			}
			File fileDir = new File(imagePath + book.bookname+"/");
			fileDir.mkdir();
			pm.getPicture(plugin, filePath, filehandle);
		}
		bitmap1 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);

//		float multiple = 0;
//		if(display_w*display_h < bitmap1.getWidth()*bitmap1.getHeight()){
//			multiple =  (float)bitmap1.getWidth()/(float)display_w/2;
//			ratiof = -(0.0f - multiple);
//			bitmap1 = BitmapUtil.initZoomBitmap(bitmap1, multiple);
//		}else{
//			bitmap1 = BitmapUtil.initZoomBitmap(bitmap1, 1.0f);
//		}
		
		zoomview1.filePath = filePath;
		zoomview1.init(display_w, display_h, bitmap1);
		zoomview1.isShowing = true;
		zoomview2 = new ZoomImageView(this);
		
		
		pm.close(plugin, filehandle);
	
		if(initFile != endFile){
			filePath = getfileName(initFile+1);
			filehandle = pm.open(plugin, initFile+1);
		}else{
			filePath = getfileName(file);
			filehandle = pm.open(plugin, endFile);
		}
		
		
		imageFile = new File(filePath);
		if(!imageFile.exists()||imageFile.length() == 0){
			File fileDir = new File(imagePath + book.bookname+"/");
			fileDir.mkdir();
			pm.getPicture(plugin, filePath, filehandle);
		}
	
	
		
		bitmap2 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
		
//		if(display_w*display_h < bitmap1.getWidth()*bitmap1.getHeight()){
//			multiple =  (float)bitmap1.getWidth()/(float)display_w/10;
//			ratiof = 1.0f - multiple;
//			bitmap2 = BitmapUtil.initZoomBitmap(bitmap2, multiple);
//		}else{
//			bitmap2 = BitmapUtil.initZoomBitmap(bitmap2, 1.0f);
//		}
		zoomview2.filePath = filePath;
		zoomview2.init(display_w, display_h, bitmap2);
		zoomview2.isShowing = false;
		flipper.addView(zoomview1,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		flipper.addView(zoomview2,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
	}
	
	private void initUI(int initFile) {
	
		detector = new GestureDetector(this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper01);
		flipper.removeAllViews();
		// 获取屏幕宽高
		Display display = getWindowManager().getDefaultDisplay();
		display_w = display.getWidth();
		display_h = display.getHeight();
		
		
		// 设置图片
		zoomview1 = new ZoomImageView(this);
		pm.close(plugin, filehandle);
		filehandle = pm.open(plugin, initFile);
		
		String filePath = getfileName(initFile);
		File imageFile = new File(filePath);
		if(!imageFile.exists()||imageFile.length() == 0){
			File cctemp = new File(imagePath);
			if(!cctemp.exists()){
				cctemp.mkdir();
			}
			File fileDir = new File(imagePath + book.bookname+"/");
			fileDir.mkdir();
			pm.getPicture(plugin, filePath, filehandle);
		}
		
		
		bitmap1 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
		zoomview1.filePath = filePath;
	//	bitmap1 = BitmapUtil.initZoomBitmap(bitmap1, 1.0f + ratiof);
		zoomview1.init(display_w, display_h, bitmap1);
		zoomview1.isShowing = true;
		flipper.addView(zoomview1,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		zoomview2 = new ZoomImageView(this);
		
		pm.close(plugin, filehandle);
		
	
		
		if(initFile != endFile){

			filehandle = pm.open(plugin, initFile+1);
			filePath = getfileName(initFile+1);
		}else{
			filehandle = pm.open(plugin, endFile);
			filePath = getfileName(endFile);
		}
		
		
		imageFile = new File(filePath);
		if(!imageFile.exists()||imageFile.length() == 0){
			File cctemp = new File(imagePath);
			if(!cctemp.exists()){
				cctemp.mkdir();
			}
			File fileDir = new File(imagePath + book.bookname+"/");
			fileDir.mkdir();
			pm.getPicture(plugin, filePath, filehandle);
		}
		
		
		
		bitmap2 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
		
	//	bitmap2 = BitmapUtil.initZoomBitmap(bitmap2, 1.0f + ratiof);
		zoomview2.filePath = filePath;
		zoomview2.init(display_w, display_h, bitmap2);
		
		zoomview2.isShowing = false;
		flipper.addView(zoomview2,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		
		
	}
	
	/**
	 * 给桌面widget发送广播
	 * void
	 * 2011-3-31下午02:47:53
	 */
	public void sendBroadcast() {
		//给桌面widget发送信息更新操作；
		
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.cartoon_widget_layout);
		// 使用改变按钮属性
		remoteViews.setTextViewText(R.id.tvEnterLastRead, "上次读过："
				+ book.bookname);
		// 改变上次阅读按钮的事件
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("bookId", book.id);
		intent.setComponent(new ComponentName(this.getPackageName(),
				this.getClass().getName()));
		intent.setAction(String.valueOf(System.currentTimeMillis()));
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.ibEnterLastRead,
				pendingIntent);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		appWidgetManager.updateAppWidget(new ComponentName(this, CartoonWidget.class), remoteViews);
	}

	private float getRoundFloat(float ft) {
		BigDecimal bd = new BigDecimal((double) ft);
		bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	
	public void getNextPage(){
		pm.close(plugin, filehandle);
		file += 1;
		if(file > endFile){
			file = endFile;
		}
		

		
		filehandle = pm.open(plugin, file);
		String filePath = getfileName(file);
		
		File imageFile = new File(filePath);
		if(!imageFile.exists()||imageFile.length() == 0){
			File cctemp = new File(imagePath);
			if(!cctemp.exists()){
				cctemp.mkdir();
			}
			File fileDir = new File(imagePath + book.bookname+"/");
			fileDir.mkdir();
			pm.getPicture(plugin, filePath, filehandle);
		}
		
		
		
	
		if(zoomview1.isShowing){
			
			bitmap2 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
			zoomview2.filePath = filePath;
			//bitmap2 = BitmapUtil.initZoomBitmap(bitmap2,  1.0f+ratiof);
			zoomview2.top = 0;
			zoomview2.left = 0;
			zoomview2.init(display_w, display_h, bitmap2);
			zoomview1.isShowing = false;
			zoomview2.isShowing = true;
		}else{
			bitmap1 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
			zoomview1.filePath = filePath;
			
		//	bitmap1 = BitmapUtil.initZoomBitmap(bitmap1, 1.0f+ratiof);
			zoomview1.top = 0;
			zoomview1.left = 0;
			zoomview1.init(display_w, display_h, bitmap1);
			zoomview1.isShowing = true;
			zoomview2.isShowing = false;
		}
		
	}
	
	public void getPrevPage(){
		pm.close(plugin, filehandle);
		file -= 1;
		if(file < headFile){
			file = headFile;
		}

		filehandle = pm.open(plugin, file);
		String filePath = getfileName(file);
		
		File imageFile = new File(filePath);
		if(!imageFile.exists()||imageFile.length() == 0){
			File cctemp = new File(imagePath);
			if(!cctemp.exists()){
				cctemp.mkdir();
			}
			File fileDir = new File(imagePath + book.bookname+"/");
			fileDir.mkdir();
			pm.getPicture(plugin, filePath, filehandle);
		}
		if(zoomview1.isShowing){
			
			bitmap2 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
			
			//bitmap2 = BitmapUtil.initZoomBitmap(bitmap2, 1.0f+ratiof);
			zoomview2.top = 0;
			zoomview2.left = 0;
			zoomview2.filePath = filePath;
			zoomview2.init(display_w, display_h, bitmap2);
			zoomview1.isShowing = false;
			zoomview2.isShowing = true;
		}else{
			bitmap1 = BitmapUtil.zoomBitmap(filePath, 1.0f + ratiof);
			zoomview1.filePath = filePath;
			//bitmap1 = BitmapUtil.initZoomBitmap(bitmap1,  1.0f+ratiof);
			zoomview1.top = 0;
			zoomview1.left = 0;
			zoomview1.init(display_w, display_h , bitmap1);
			zoomview1.isShowing = true;
			zoomview2.isShowing = false;
		}
		
	}
	
	 public boolean TurntoPageOfBkmk(CatelogItem bki) {
		
		    this.file = bki.file;
		    
		    pm.close(pm.getDefaultPlugin(),pm.filehandle);
			Plugin plugin =pm.getDefaultPlugin();
			filehandle =pm.open(plugin, file);
			initUI(file);
	    	return true;
	    }
	    
	    public boolean addBkmk() {
	    	
			
			CatelogItem bki = new CatelogItem();
			bki.bookname = book.bookname;
			bki.title = String.valueOf(file);
			bki.file = (int)file;
			
			bkmkManager.addBkmk(bki);
			return true;
	    }
	    
	public void initMenu(){
		layoutFun = (LinearLayout)findViewById(R.id.ct_bkmk);
		layoutLight = (LinearLayout)findViewById(R.id.ct_bklight);
		layoutTurnto = (LinearLayout)findViewById(R.id.ct_turnto);
		bkmkManager = new CTBookMarkManager(this);
		menuBkmk = new CTMenuBkmk(this);
		menuBlight = new CTMenuBklight(this);
		menuTurnto = new CTMenuTurnto(this);
		mHandler = new BookContentShowHandler(this);
		menuHandler = new MenuHandler(this);
	}    
 
	@Override
	public void closeOptionsMenu() {
		// TODO Auto-generated method stub
		super.closeOptionsMenu();
	}
	@Override
	public void openOptionsMenu() {
		// TODO Auto-generated method stub
		layoutFun.setVisibility(View.GONE);
		layoutLight.setVisibility(View.GONE);
		layoutTurnto.setVisibility(View.GONE);
		if(menuBkmk.isShowed()){
			menuBkmk.hide();
		}
		if(menuBlight.isShowed()){
			menuBlight.hide();
		}
		if(menuTurnto.isShowed()){
			menuTurnto.hide();
		}
		
		super.openOptionsMenu();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);  
		MenuInflater inflater = getMenuInflater();  
		inflater.inflate(R.menu.menu, menu);  
		mainMenu = menu;
		if(mode ){
			mainMenu.getItem(3).setTitle(R.string.mode_label1);
			mainMenu.getItem(3).setIcon(R.drawable.ct_mode1);
		
			
		}else{
			
			mainMenu.getItem(3).setTitle(R.string.mode_label2);
			mainMenu.getItem(3).setIcon(R.drawable.ct_mode2);
			
		}
		
		
		
		return true;  
	} 
	
	@Override  
	public boolean onOptionsItemSelected(MenuItem item) {  
		switch (item.getItemId()) {  
		case R.id.mark:  
	
			move = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			move.setDuration(1000);
			layoutFun.startAnimation(move);
			layoutFun.setVisibility(View.VISIBLE);
			layoutLight.setVisibility(View.GONE);
			layoutTurnto.setVisibility(View.GONE);
			return true; 
		case R.id.light:
			move = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			
			move.setDuration(1000);
			layoutLight.startAnimation(move);
			layoutLight.setVisibility(View.VISIBLE);
			layoutFun.setVisibility(View.GONE);
			layoutTurnto.setVisibility(View.GONE);
			int temp = (int)(menuBlight.ness/255*100);
			if(temp == 0){
				temp = 50;
				menuBlight.seeBar.setProgress(128);
			}
			menuBlight.textInfo.setText(String.valueOf(temp)+"%");
			
			return true; 
		case R.id.turnto:
			move = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			double tempProgress = (double)file/(double)endFile*10000;
			
			menuTurnto.seeBar.setProgress((int)tempProgress);
			String temps = BookUtil.tansDecimal((int)tempProgress);
			menuTurnto.textInfo.setText(temps+"%");
			move.setDuration(1000);
			layoutTurnto.startAnimation(move);
			layoutTurnto.setVisibility(View.VISIBLE);
			layoutFun.setVisibility(View.GONE);
			layoutLight.setVisibility(View.GONE);
			
			return true; 
		case R.id.mode:
			if(mode){
				mode = false;
				mainMenu.getItem(3).setTitle(R.string.mode_label2);
				mainMenu.getItem(3).setIcon(R.drawable.ct_mode2);
				layoutFun.setVisibility(View.GONE);
				layoutLight.setVisibility(View.GONE);
				layoutTurnto.setVisibility(View.GONE);
			}else{
				mode = true;
				mainMenu.getItem(3).setTitle(R.string.mode_label1);
				mainMenu.getItem(3).setIcon(R.drawable.ct_mode1);
				layoutFun.setVisibility(View.GONE);
				layoutLight.setVisibility(View.GONE);
				layoutTurnto.setVisibility(View.GONE);
			}
			
			return true; 
			// More items go here (if any) ...  }  return false;  } 
		case R.id.auto:
			layoutFun.setVisibility(View.GONE);
			layoutLight.setVisibility(View.GONE);
			layoutTurnto.setVisibility(View.GONE);
			
			if(this.isAutoRunning == false){
				ar = new AutoReader(this);
				this.isAutoRunning = true;
				mainMenu.getItem(4).setTitle(R.string.auto_label2);
				mainMenu.getItem(4).setIcon(R.drawable.ct_pause);
				new Thread(ar).start();
			}else{
				ar.stop_t();
				this.isAutoRunning = false;
				
				mainMenu.getItem(4).setTitle(R.string.auto_label);
				mainMenu.getItem(4).setIcon(R.drawable.ct_auto);
			}
			
			
			return true;
		case R.id.back:
			layoutFun.setVisibility(View.GONE);
			layoutLight.setVisibility(View.GONE);
			layoutTurnto.setVisibility(View.GONE);
			finish();
			return true; 
			// More items go here (if any) ...  }  return false;  } 
		}
	
		
		return false;
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		openOptionsMenu();
		System.out.println("-------LongPress---------");
	}
	


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub

		return false;
	}
	public void setCurPostion(int pos) {
		if(pos == 0){
			file = 1;
			pos = 1;
		}else{
			file = pos;
		}
		
		initUI(pos);
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(this.isAutoRunning == true){
			ar.stop_t();
			this.isAutoRunning = false;
			
			mainMenu.getItem(4).setTitle(R.string.auto_label);
			mainMenu.getItem(4).setIcon(R.drawable.ct_auto);
		}
		if(book!=null){
			BooksBo bo = new BooksBo(this, false);
			
			bo.addCurIndex(book.id, 0, 0,file);
			String str = String.valueOf(book.file);
			if(str != null)
				bo.saveOrUpdateHistory(book.bookname, 0, str);
			SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putFloat("ness", menuBlight.ness);
			editor.putBoolean("mode", mode);
			editor.commit();
			
			pm.closeAll();
		}
		
		System.gc();
		super.finish();
	}	
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
	     if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	 layoutFun.setVisibility(View.GONE);
	    	 layoutLight.setVisibility(View.GONE);
	    	 layoutTurnto.setVisibility(View.GONE);
	    	 openOptionsMenu();
	         return true;
	     }
      
       return super.onKeyDown(keyCode, msg);
   }
	
	public static int computeSampleSize(BitmapFactory.Options options,  

			          int minSideLength, int maxNumOfPixels) {  

			      int initialSize = computeInitialSampleSize(options, minSideLength,  

			              maxNumOfPixels);  

			   

			      int roundedSize;  

			      if (initialSize <= 8 ) {  

			          roundedSize = 1 ;  

			          while (roundedSize < initialSize) {  

			              roundedSize <<= 1 ;  

			          }  

			      } else {  

			          roundedSize = (initialSize + 7 ) / 8 * 8 ;  

			      }  

			   

			      return roundedSize;  

			 }  

	 private static int computeInitialSampleSize(BitmapFactory.Options options,  

			           int minSideLength, int maxNumOfPixels) {  

			       double w = options.outWidth;  

			       double h = options.outHeight;  

			    

			       int lowerBound = (maxNumOfPixels == - 1 ) ? 1 :  

			               ( int ) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  

			       int upperBound = (minSideLength == - 1 ) ? 128 :  

			               ( int ) Math.min(Math.floor(w / minSideLength),  

			               Math.floor(h / minSideLength));  

			    

			       if (upperBound < lowerBound) {  

			           // return the larger one when there is no overlapping zone.  

			           return lowerBound;  

			       }  

			    

			       if ((maxNumOfPixels == - 1 ) &&  

			               (minSideLength == - 1 )) {  

			           return 1 ;  

			       } else if (minSideLength == - 1 ) {  

			           return lowerBound;  

			       } else {  

			           return upperBound;  

			       }  

			  }    

	 private String getfileName(int file){
		 String filePath =(imagePath + book.bookname+"/"+file + ex);
		 return filePath;
	 }
	 
	 public  void flipNextPage(){
			 
		 if(mode){
			 if(file < endFile)
				{
					Log.i("onScroll", "向下");
					flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
					getNextPage();
					flipper.showNext();
					if(file == endFile){
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast successToast;
						successToast = Toast.makeText(this, "此书完!", Toast.LENGTH_SHORT);
						successToast.show();
					}
				}else{
					Toast successToast;
					successToast = Toast.makeText(this, "此书完!", Toast.LENGTH_SHORT);
					successToast.show();
				}
		 }else{
			 if(file < endFile)
				{
					Log.i("onScroll", "向右");
					flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
					getNextPage();
					flipper.showNext();
					if(file == endFile){
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast successToast;
						successToast = Toast.makeText(this, "此书完!", Toast.LENGTH_SHORT);
						successToast.show();
					}
				}else{
					Toast successToast;
					successToast = Toast.makeText(this, "此书完!", Toast.LENGTH_SHORT);
					successToast.show();
				}
		 }
		
		
	 }
	 
	  public class BookContentShowHandler extends Handler {
	    	
	    	CartoonReader ct;
	    	
	    	public BookContentShowHandler(CartoonReader ct) {
	    		super();
	    		this.ct = ct;
	    	}


	    	@Override
	    	public void handleMessage(Message msg) {
	    		// TODO Auto-generated method stub
	    		if(isAutoRunning == true){
	    			flipNextPage();
	    		}
	    		
	    		
	    		//对应百分比
	    	
	    		super.handleMessage(msg);
	    	}
	    	
	    }
	  
	  public class MenuHandler extends Handler {
	    	
	    	CartoonReader ct;
	    	
	    	public MenuHandler(CartoonReader ct) {
	    		super();
	    		this.ct = ct;
	    	}


	    	@Override
	    	public void handleMessage(Message msg) {
	    		// TODO Auto-generated method stub
	    	
	    		mainMenu.getItem(4).setTitle(R.string.auto_label);
				mainMenu.getItem(4).setIcon(R.drawable.ct_auto);
	    		
	    		//对应百分比
	    	
	    		super.handleMessage(msg);
	    	}
	    	
	    }
	  @Override
		protected void onDestroy() {
		
		  if(bitmap1 != null){
				bitmap1.recycle();
				System.out.println("回收bitmap");
			}
			if(bitmap2 != null){
				bitmap2.recycle();
			}
			System.gc();
			super.onDestroy();
			
		}
}