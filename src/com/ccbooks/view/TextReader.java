package com.ccbooks.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.FSSeekBarStatusThread;
import com.ccbooks.fullscreen.bookcore.BookCore;
import com.ccbooks.fullscreen.content.CommentView;
import com.ccbooks.fullscreen.content.TextFlipper;
import com.ccbooks.fullscreen.content.TextScroller;
import com.ccbooks.fullscreen.content.TextThreeDim;

import com.ccbooks.util.Font;
import com.ccbooks.view.R;
import com.ccbooks.view.widget.CartoonWidget;
import com.ccbooks.vo.Book;
import com.ccbooks.vo.CatelogItem;
import com.chinachip.TextReader.BookMarkManager;
import com.chinachip.TextReader.Line;
import com.chinachip.TextReader.TextMenu;
import com.chinachip.book.chardetextor.CharDetect;
import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginUtil;
import com.chinachip.ccbooks.engine.LineIndex;
import com.chinachip.tree.Node;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.database.SQLException;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

public class TextReader extends Activity implements ColorPickerDialog.OnColorChangedListener{
	
	static final String DEBUG_TAG = "jf";
	static final int FLING_MIN_DISTANCE = 120;
	public final static short DAY_LIGHT = 1;
	public final static short DARK_LIGHT = 2;
	
	public short dayOrNight;
	private int screenWidth;
	private int screenHeight;
	private FrameLayout layoutMain;	
	public static BookCore bc;
	public int fontColor = 0xff565656;
	public int bkColor = 0xff444444;
	public TextMenu menuMain;
	private BookMarkManager bkmkManager;
	private int readMode;	//1为scrollerMain
	private int readModeRequest;
	private TextFlipper flipperMain;
	private TextScroller scrollerMain;
	public TextThreeDim threeDimMain;
	private long markPoint = 0;
	public static Book book;
	public static BooksBo bo = null;
	public static int file = 0;
	public long fileLength = 0;
	public String enc = null;
	public int headFile;
	public int endFile;
	private boolean isComment; //判断是否在批注
	private CountDownTimer brewCountDownTimer;
	private CommentView commentView;
	private boolean isBrew;
	public int [] transpixel ;
	public String ttfFileName = "Clockopia";
	
	public Font font;
	
	public SeekBarHandler seekBarHandle = null;
	
	public static FSSeekBarStatusThread seekBarThread = null;
	
	private Handler handler = new Handler();

	private int delayMillis = 35;
	private Runnable runnable = new Runnable() {
		public void run() {
			if (threeDimMain.getPageViews().autoRun()) {
				handler.postDelayed(this, delayMillis);
			}
			else
			{
				turnPageOff();
			}
		}
	};
	 
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutMain = (FrameLayout)this.getLayoutInflater().inflate(R.layout.fs_main, null);
        setContentView(layoutMain);
    	layoutMain.setBackgroundColor(bkColor);
        Bundle bundle = this.getIntent().getExtras();
		int id = bundle.getInt("bookId");
		int file = bundle.getInt("file");
		markPoint = bundle.getLong("markPoint");
		boolean downLand = bundle.getBoolean("DownLand");
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
    	creatEngine();
    	String url = book.bookpath;
		Plugin plugin = bc.pm.getPlugin(url);
		Param param = new Param();
		int filehandle = bc.pm.open(plugin, url);
    	if(file == 0){
			try{
				book = bo.getOne(id);
			}catch ( SQLException e ) {
				e.printStackTrace();
			}
			
			if(filehandle == 0){
				Toast.makeText(this, "该电子书不支持!", Toast.LENGTH_SHORT).show();
				book = null;
				this.finish();
				return;
			}
			int rtn = bc.pm.get(plugin, PluginUtil.BOOK_TYPE, param);
			int prop = param.i;
			if(rtn > 0){
				if(prop == PluginUtil.BOOK_TYPE_INVALID){
					Toast.makeText(this, "该电子书不支持!", Toast.LENGTH_SHORT).show();
					book = null;
					
					this.finish();
					return;
				
				}else if(prop == PluginUtil.BOOK_TYPE_COMIC){
					Toast.makeText(this, "该电子书不支持!", Toast.LENGTH_SHORT).show();
					book = null;
					
					this.finish();
					return;
				}
			}
			try{
				if (!(new File(book.bookpath).exists())){
					Toast.makeText(this, R.string.fileNotFound, Toast.LENGTH_SHORT).show();
					book = null;
					this.finish();
					return;
				}
			}catch ( SQLException e ) {
				e.printStackTrace();
			}
			FileReader fr;
			try {
				fr = new FileReader(book.bookpath);
				if(fr.read() ==-1)
				{
					Toast.makeText(this, "该电子书为空!", Toast.LENGTH_SHORT).show();
					book = null;
					this.finish();
					return;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
    	SharedPreferences  sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
        readMode = sp.getInt("readMode", 0);
        readModeRequest = readMode;
     
    	dayOrNight = (short)sp.getInt("dayOrNight", DAY_LIGHT);
    	setDayOrDark(dayOrNight);
    	//float blValue = (short)sp.getFloat("ness", 0.5f);
    	//setDayOrDark(dayOrNight);
    	//WindowManager.LayoutParams bl = getWindow().getAttributes();
    	//bl.screenBrightness = blValue/255;
    	//getWindow().setAttributes(bl);
    	
    	bc.setFontsize((int)sp.getInt("font_size", 22));
    	
        initBkmk();
        openBook();
        if(fileLength == 0){
        	return;
        }
        initMenu();
       
        setCurPostion(markPoint);
        
        initCommentView(); //初始化批注        
        
        if(readMode == 0) {
        	initThreaDim(true,markPoint);
        	initModeStart(false);
        }
        else {
        	initThreaDim(false,markPoint);
        	initModeStart(true);        	
        }
        sendBroadcast();	//开始加入广播；

        setCurPostion(markPoint);
        if(downLand){
			isDownLand();
		}
    }
	private boolean isDownLand(){
		seekBarThread = new FSSeekBarStatusThread(this);
		seekBarHandle = new SeekBarHandler(this);
		new Thread(seekBarThread).start();
		return false;
	}
    public void hideStatusAndTitle() {
    }
    
    /**设置内容页面颜色 ，例如白天模式以及夜晚模式*/
    public void setDayOrDark(short s) {
    	//TODO setDayOrDark
    	dayOrNight = s;
    	switch (s) {
    	case DAY_LIGHT:
    		fontColor = 0xff565656;
    		bkColor = 0xfff4f4f4;
    		
    		break;
    	case DARK_LIGHT:
    		fontColor = 0xffcccccc;
    		bkColor = 0xff444444;
    		
    		break;
    	}
    	bc.setTextColor(fontColor);
    	layoutMain.setBackgroundColor(bkColor);
    }
    
    public void setBGColor(int color) {
    	//TODO setDayOrDark
		bkColor = color;
    	layoutMain.setBackgroundColor(bkColor);
    }
   
    /**
     * 
     * 
     * Created on 2011-4-7  下午03:11:10
     */
    public void initCommentView(){
    	commentView = (CommentView)((ViewStub)findViewById(R.id.viewstub_touchview_text)).inflate();
    	commentView.setVisibility(View.VISIBLE);
    	readComment(this.getResources().getConfiguration().orientation);
        //CommentPaint();
    }

    /**
     * 主动请求画批注
     * 
     * Created on 2011-5-11  上午11:33:48
     */
	public void CommentPaint() {
		if(readMode == 0){
			reParseText();
        }else{
        	scrollerMain.setIsScroll(true);
        }
	}


    
    public void initModeStart(boolean fgStart){
        scrollerMain = (TextScroller)(((ViewStub)findViewById(R.id.viewstub_scroll_text)).inflate());
        scrollerMain.setTextColor(fontColor);
        if(fgStart) {
        	scrollerMain.turntoCurPage();
        }
        else {
        	scrollerMain.setVisibility(View.GONE);
//        	commentView.setVisibility(View.GONE);
        }
    }
    
    public void initThreaDim(boolean fgStart,long markPoint){
    	LinearLayout threaDimLayout = (LinearLayout)(((ViewStub)findViewById(R.id.viewstub_flipper_text)).inflate());
    	threeDimMain = new TextThreeDim(this, bc, threaDimLayout, fontColor,markPoint);
    	threeDimMain.setTextColor(fontColor);
        if(fgStart) {
  		    	 
//    		initEvent();
        }
        else {
        	threeDimMain.getPageViews().setVisibility(View.GONE);
//        	commentView.setVisibility(View.GONE);
        }
    }
    
    public void initFlipperMode(boolean fgStart){
    	LinearLayout flipperLayout = (LinearLayout)(((ViewStub)findViewById(R.id.viewstub_flipper_text)).inflate());
        flipperMain = new TextFlipper(this, bc, flipperLayout, fontColor);
        flipperMain.setTextColor(fontColor);
        if(fgStart) {
        	flipperMain.turnToCurPage();
        }
        else {
        	flipperMain.getFillperView().setVisibility(View.GONE);
//        	commentView.setVisibility(View.VISIBLE);
        }
    }
    
    public boolean creatEngine() {
    	Log.d(DEBUG_TAG, "---------------------TextFlip creatEngine-------------------------");
    	WindowManager windowManager = getWindowManager();    
    	Display display = windowManager.getDefaultDisplay();    	
    	screenWidth = display.getWidth();    
    	screenHeight = display.getHeight();
    	int fontSize = 22;
    	int lineSpace = 6;	
    	SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);	
		ttfFileName = sp.getString("ttfFileName", ttfFileName);
    	font = new Font(fontSize, ttfFileName);
    	bc = new BookCore();
    	bc.setFontsize(fontSize);
    	bc.setPaint(font.getPaint());
    	bc.setLineSpace(lineSpace);
    	bc.setPageWidth(screenWidth - 20);
    	bc.setPageHeight(screenHeight);
    	if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {    
    		//engine.setPortraint(1);
    	} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {    
    		//engine.setPortraint(2);
    	}
    	
    	return true;
    }
    
    public boolean openBook() {
    	Log.d(DEBUG_TAG, "---------------------TextFlip openBook-------------------------");
		String url = book.bookpath;
		SharedPreferences sp = this.getSharedPreferences("config", Activity.MODE_PRIVATE);
		Plugin plugin = bc.pm.getPlugin(url);
	//	bc.pm.open(plugin, url);
		Param param = new Param();
		bc.pm.get(plugin, PluginUtil.BOOK_BACKLIST, param);
		bc.pm.bltree = param.bltree;
		Node root = (Node)bc.pm.bltree.getRoot();
		headFile = root.file;
		endFile = bc.pm.bltree.getEndFile();
		if(book.file > 0){
			this.file = book.file;
			markPoint = book.curIndex;
		}else{
			this.file = root.file;
		}
		plugin = bc.pm.getDefaultPlugin();
		bc.filehandle = bc.pm.open(plugin, this.file);
		enc = book.charset;
		if(enc == null){
			enc = getCharSet(bc.pm, plugin, bc.filehandle);
			bo.updateChartset(book.id, enc);
		}
		
		if(enc == null){
			param = new Param();
			int end = bc.pm.get(plugin, PluginUtil.BOOK_CHARSET, param);
			
			if(end > 0)
			{
				enc = param.getStr();
				if(!PluginUtil.isUnicode(enc)){
					enc = "GBK";
				}
			}else{
				enc = "GBK";
			}
			bo.updateChartset(book.id, enc);
		}
		bo.updateChartset(book.id, enc);
			
		try {
			plugin = bc.pm.getDefaultPlugin();
			
			fileLength = bc.pm.length(plugin, bc.filehandle);
		    if(fileLength == 0){
		    	this.finish();
				Toast.makeText(this, "无法打开的文件!", Toast.LENGTH_SHORT).show();
				return false;
		    }
			bc.setDataSource(enc,book.bookpath);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
    }
    
    public boolean initBkmk() {
    	bkmkManager = new BookMarkManager(this);
    	return true;
    }
    
    public boolean initMenu() {
    	menuMain = new TextMenu(this);
    	//layoutMain.addView(menuMain.getLayoutMenu());
    	return true;
    }
    public boolean reParseText() {
    	if(readMode == 0) {
    		threeDimMain.reparseText();
    	}
    	else
    	{
    		scrollerMain.reparseText();
    	}
    	return true;
    }
    
    public boolean reloadText() {
    	if(readMode == 0) {
    		threeDimMain.reloadText();
    	} else {
			scrollerMain.reloadText();
    	}
    	return true;
    }
    
    public boolean onConfigChange() {
    	if(readMode == 0) {
    		threeDimMain.onConfigChange();
    	} else {
			scrollerMain.reloadText();
    	}
    	return true;
    }
 
    public boolean TurntoPageOfBkmk(CatelogItem bki) {
		long pos = bki.pageIndex;
		int line = bki.curLine;
	    this.file = bki.file;
	    
	    bc.pm.close(TextReader.bc.pm.getDefaultPlugin(),TextReader.bc.pm.filehandle);
		Plugin plugin =TextReader.bc.pm.getDefaultPlugin();
		int filehandle = bc.pm.open(plugin, file);
		bc.setFilehandle(filehandle);
		bc.resetFile();
	    //TODO open file
		float offset = bki.offset;
		setCurPostion(pos);
		reParseText();
		setPageLineOffset(line);
		setLinePrintOffset(offset);
    	return true;
    }
    
    public boolean addBkmk() {
    	
		long len = getFileLength();
		if(len <= 0) 
			return false;
		long curPos = getCurPostion();
		int line = getPageLineOffset();
		float offset = getLinePrintOffset();
		
		CatelogItem bki = new CatelogItem();
		bki.bookname = book.bookname;
		bki.pageIndex = (int)curPos;
		bki.title = getCurFirstText();
		bki.lastEnter = 0;
		bki.percent = (int)(curPos*100/len);
		bki.curLine = line;
		bki.offset = (int)offset;
		bki.file = (int)file;
		
		bkmkManager.addBkmk(bki);
		return true;
    }
    
    public boolean delBkmk(CatelogItem bki) {
    	//for(int i=0;i<bkmkManager.getBkmkNum();i++) {
    	//	if(bkmkManager.getBkmk(i) == bki)
    	//		bkmkManager.
    	//}		
    	return true;
    }
    

    public Book getBook() {
    	return book;
    }
    
    public int getReadModeRequest() {
    	return readModeRequest;
    }    
    public void setReadModeRequest(int mode) {
    	readModeRequest = mode;
    }
    
	public int getReadMode() {
		return readMode;
	}	
	public void setReadMode(int readMode) {
		this.readMode = readMode;
	}
	
	public void readModeCheck() {
		if(readMode != readModeRequest) {
			long pos = getCurPostion();
			readMode  = readModeRequest;
			if(readMode == 1) {
	        	scrollerMain.reparseText();
	        	scrollerMain.setVisibility(View.VISIBLE);
	        	threeDimMain.getPageViews().setVisibility(View.GONE);
			}
			else {
				setCurPostion(pos);
				scrollerMain.setVisibility(View.GONE);
			 	onConfigChange();
				threeDimMain.getPageViews().setVisibility(View.VISIBLE);				
			}
			SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt("readMode", readMode);
			editor.commit();
		}
	}
	
	
	public BookCore getEngine() {
		return bc;
	}
	
	public TextMenu getMenu() {
		return menuMain;
	}
	
	public TextFlipper getFlipperMain() {
		return flipperMain;
	}

    public TextScroller getScrollerMain() {
		return scrollerMain;
	}

	public BookMarkManager getBkmkManager() {
		return bkmkManager;
	}

	public long getFileLength() {
		long fileLength = 0;
		Plugin plugin = bc.pm.getDefaultPlugin();
		fileLength = bc.pm.length(plugin, bc.pm.filehandle);
		
		if(fileLength > 0)return fileLength;
		else return 0;
	}
	
    public String getCurFirstText(){
    	ArrayList<String> ArrStr = null;
    	if(readMode == 0)
    		ArrStr = threeDimMain.getCurrStringArray();
    	else
    		ArrStr = scrollerMain.getCurTextArray(); 	
		String title = null;
		if (ArrStr !=null){
			for (int i = 0;i<ArrStr.size();i++){
				title = ArrStr.get(i).replace(" ", "");
				if (title != null) {
					if (!title.equals("")) {
						if (title == null) {
							break;
						} else {
							if (title.indexOf("\r\n") >= 0) {
								break;
							}
							if (title.length() > 1) {
								break;
							}
						}
							
					}
				}
			}
		}
    	return title;
    }
    
   
    public int calculateLinesOffsetByIndex(LineIndex lineIndex_old) {
    	ArrayList<LineIndex> ArrLong = getCurLinesIndex();
    	if(readMode == 1){
	    	if(ArrLong.size()<bc.getLineCount()+1){
	    		for(int i=ArrLong.size();i<bc.getLineCount()+1;i++){
	    			LineIndex tempLine = new LineIndex();
	    			tempLine.setStart(Long.MIN_VALUE);
	    			tempLine.setTopIndex(Integer.MIN_VALUE);
	    			ArrLong.add(tempLine);
	    		}
	    	}
    	}	
   	
//		printArrLong(ArrLong);
		
    	for(int i = 0;i<ArrLong.size();i++) {
    		if(ArrLong.get(i).compareTo(lineIndex_old))
    			return i;
    	}
    	return -1;
    }

    /**
     * 打印行偏移来调试
     * @param ArrLong
     * Created on 2011-4-25  上午11:25:22
     */
	private void printArrLong(ArrayList<LineIndex> ArrLong) {
		String tempString = "[ ";
    	for(LineIndex myIndex:ArrLong){
    		tempString = tempString+myIndex.getTopIndex()+" ";
    	}
    	tempString = tempString +"]";
    	Log.i("garmen", tempString+"size:"+ArrLong.size());
	}

    public ArrayList<LineIndex> getCurLinesIndex() {
    	if(readMode == 0)
    		return threeDimMain.lineIndexs;
    	else
    		return scrollerMain.getCurLinesIndex(); 	    	
    }
    
    
	public long getCurPostion() {
		if(readMode == 0) {
			long pos = 0;
			try {
				pos = bc.getCurrIndexAt();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pos;
		}
		else
			return scrollerMain.getControler().getCurPos();
	}	
	

	public long getNextPostion() {
		if(readMode == 0) {
			long pos = 0;
			try {
				pos = bc.getNextIndexAt();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pos;
		}
		else
			return scrollerMain.getControler().getCurPos();
	}	
	
	public void setCurPostion(long pos) {
		try {
			bc.getCurrPage(pos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getPageLineOffset() {
		if(readMode == 0)
			return 0;
		else
			return scrollerMain.getControler().getCurLine();
	}	
	public void setPageLineOffset(int line) {
		if(readMode == 1)
			scrollerMain.getControler().setCurLine(line);
	}	
	
	public float getLinePrintOffset() {
		if(readMode == 0)
			return 0;
		else
			return scrollerMain.getyPrintStart();
	}
	public void setLinePrintOffset(float offset) {
		if(readMode == 1)
			scrollerMain.setyPrintStart(offset);
	}
	
	public short getDayOrNight() {
		return dayOrNight;
	}

	public void setDayOrNight(short dayOrNight) {
		this.dayOrNight = dayOrNight;
	}
	
	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public int getBkColor() {
		return bkColor;
	}

	public void setBkColor(int bkColor) {
		this.bkColor = bkColor;
	}
	
	public CommentView getCommentView(){
		return commentView;
	}
	
	public boolean isViewTouchEnable() {
		if(readMode == 0) {
			return threeDimMain.getFoucs();
		} else {
			return scrollerMain.getFoucs();
		}
	}
		
	public void disableTouch() {
		if(readMode == 0) {
			threeDimMain.setFoucs(false);
		} else {
			scrollerMain.setFoucs(false);
		}
	}
	
	public void enableTouch() {
		if(readMode == 0) {
			threeDimMain.setFoucs(true);
		} else {
			scrollerMain.setFoucs(true);
		}		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(DEBUG_TAG, "---------------------TextFlip onTouchEvent-------------------------");
		if(menuMain.isMenuShowed()){
			if(event.getAction() == MotionEvent.ACTION_UP) {
				if(isViewTouchEnable()) 
					disableTouch();
				else
					menuMain.hide();
			}
			return true;
		}
		else if(readMode == 0)
			return threeDimMain.pageViews.onTouchEvent(event);
		else
			return false;
	}
	

	

	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK){
			Log.d(DEBUG_TAG, "---------------------TextFlip onKeyUp-------------------------");
			if(menuMain.isMenuShowed()){
				menuMain.hide();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		//return super.onKeyUp(keyCode, event);
		Log.d(DEBUG_TAG, "---------------------TextFlip onKeyUp-------------------------");
		if (keyCode == KeyEvent.KEYCODE_MENU){
			if(menuMain.isMenuShowed()){
				
				menuMain.hide();
				return true;
			}else{
				
				menuMain.show();
				
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "---------------------TextFlip onConfigurationChanged-------------------------");
		WindowManager windowManager = getWindowManager();    
    	Display display = windowManager.getDefaultDisplay();    	
    	screenWidth = display.getWidth();    
    	screenHeight = display.getHeight();
    	bc.setPageWidth(screenWidth - 20);
    	bc.setPageHeight(screenHeight);
    	onConfigChange();
    	commentView.clearView();//转屏清除批注
    	
    	if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
    		writeComment(Configuration.ORIENTATION_PORTRAIT);
    	else
    		writeComment(Configuration.ORIENTATION_LANDSCAPE);
    	readComment(newConfig.orientation);
        CommentPaint();
		super.onConfigurationChanged(newConfig);
	}
	
	public boolean turnToPageMode() {
		int id = book.id;
		if(menuMain.isMenuShowed()) {
			menuMain.hide();
		}
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("bookId", id);
		intent.putExtras(bundle);
		intent.setClass(this, BookContentView.class);
		startActivityForResult(intent, id);
		SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("readerMode", 1);
		BookShelfView.readerMode = 1;
		editor.putInt("dayOrNight", getDayOrNight());
		editor.commit();
		
		if(isBrew){
			stopCount();
		}
		finish();
		return true;
	}	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
		if(book!=null&& fileLength != 0){
			BooksBo bo = new BooksBo(this, false);
			Log.i("zkli", "savePoint"+markPoint);
			int curIndex = (int)getCurPostion();
			bo.addCurIndex(book.id, curIndex, 0,file);
			String str = getCurFirstText();
			if(str != null)
				bo.saveOrUpdateHistory(book.bookname, (int)markPoint, str);
			SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putFloat("ness", menuMain.menuSettingFun.ness);
			editor.putInt("font_size", bc.getFontsize());
			editor.putString("fontTTF", ttfFileName);
			editor.putInt("dayOrNight", dayOrNight);
			editor.putInt("bg_color", bkColor);
			editor.commit();
			bc.pm.closeAll();
		}
		
		
		
	
		super.finish();
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
	
	public String getCharSet(PluginMgr pm,Plugin plugin,int handle){
		ByteBuffer mbb = null;
		CharDetect charDetect = new CharDetect();
		int start = 0;
		int length = 0;
		int len = 5120;
		int detectLimit = 5*len;
		
		long fileLength = getFileLength();
		if(fileLength == 0)
			return null;
		charDetect.start();
		byte[] buf = null;
		int nread = 0;
		String encoding = null;
		while(start < fileLength){
			if (mbb != null) {
				mbb.flip();
				mbb.clear();
			}
			if(start+len < fileLength){
				length = len;
				buf  = new byte[len];
				
			}else{
				length = (int)fileLength - start;
				buf  = new byte[(int)fileLength - start];
			}
			
			
			mbb = pm.map(plugin,handle,start, length);
			nread = mbb.capacity();
			mbb.get(buf);
			
			start = start + length;
	    	try {
				if(charDetect.detect(buf,0,nread)){
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    		
	     
	        
	      
	        if(start >= detectLimit){
	        	break;
	        }
		}
		  encoding = charDetect.stop();
	        
        if (encoding != null) {
            System.out.println("Detected encoding = " + encoding);     
           
        } 
        else{
        	System.out.println("No encoding detected.");

        }
		 return encoding;
	}
	
	public boolean getIsComment(){
		return isComment;
	}
	
	public void setIsComment(boolean isComment){
		this.isComment = isComment;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
			if(book!=null&& fileLength != 0){
			writeComment(this.getResources().getConfiguration().orientation);
			if(commentView.getmBitmap() != null && commentView.getmBitmap().isRecycled() == false){
				commentView.getmBitmap().recycle();
				Log.e("System","commentView Bitmap 回收完毕");
			}
			if(threeDimMain.bitmap1.mbmpCurr != null){
				threeDimMain.bitmap1.mbmpCurr.recycle();
//				Log.e("System","bitmap1 回收完毕");
			}
			if(threeDimMain.bitmap2.mbmpCurr != null){
				threeDimMain.bitmap2.mbmpCurr.recycle();
//				Log.e("System","bitmap2 回收完毕");
			}	
				
			System.gc();
			
		}
		transpixel = null;
		if(isBrew == true){
			stopCount();
		}
		super.onDestroy();
		System.out.println("on Destroy 完成了！！！！！！！！！！！！！");
	}

	/**
	 * 写批注到xml文件
	 * 
	 * Created on 2011-4-16  上午09:54:07
	 */
	private void writeComment(int orientation) {
		File commentFile = new File(CommentView.COMMENTPATH + book.bookname+ orientation + ".xml");
//		if(commentView.getLineList() != null){
			try {
				FileOutputStream fos = new FileOutputStream(commentFile);
				OutputStreamWriter osw = new OutputStreamWriter(fos);

				commentView.writeXml(osw,orientation);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
	}
	
    /**
     * 读取批注
     * 
     * Created on 2011-4-16  上午09:55:31
     */
	private void readComment(int orientation) {
		File commentFile = new File(CommentView.COMMENTPATH+book.bookname+ orientation +".xml");
    	if(commentFile.exists()){
			try {
				FileInputStream fis = new FileInputStream(commentFile);

				commentView.setVisibility(View.VISIBLE);
				List<Line> templist = commentView.readXml(fis);
				commentView.setLineList(templist);
//				Collections.sort(templist, commentView);
//				for(Line line:templist){
//					Log.d("garmen",line.getStartLineIndex().getStart()+"   "+line.getStartLineIndex().getTopIndex());
//				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(NumberFormatException e){
				commentFile.delete();
			}

    	}else{
    		commentView.clearLineList();
    	}
	}
	
	public int getTextSize(){
		if(readMode == 0){
			return threeDimMain.getTextSize();
		}else{
			return scrollerMain.getTextSize();
		}
	}
	
	/**
	 * 定时关闭批注
	 * 
	 * Created on 2011-4-25  上午09:16:09
	 */
	public void startCount(){
		brewCountDownTimer = new CountDownTimer(5000,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				System.out.println("结束");
				if(isComment == true){
					isComment = false;
					menuMain.getMenuCommentFun().getCommentCheck().setChecked(false);
					closeCommentToast();
				}
			}
		};
		brewCountDownTimer.start();
		isBrew = true;
	}
	
	/**
	 * 取消定时器
	 * 
	 * Created on 2011-4-25  上午09:16:27
	 */
	public void stopCount(){
		if(brewCountDownTimer != null){
			brewCountDownTimer.cancel();
		}
		isBrew = false;
	}
	
	public boolean getIsBrew(){
		return isBrew;
	}
	
	private void closeCommentToast(){
		Toast.makeText(this, R.string.closeComment, Toast.LENGTH_LONG).show();
	}
	public class SeekBarHandler extends Handler {
    	
		TextReader bcv;
	    	
	    	public SeekBarHandler(TextReader bcv) {
	    		super();
	    		this.bcv = bcv;
	    		
	    	}


	    	@Override
	    	public void handleMessage(Message msg) {
	    		// TODO Auto-generated method stub
	    		String myMsg = (String) msg.obj;
	    		if(myMsg.equals("true")){
	    			bcv.menuMain.menuTurntoFun.seeBar.setEnabled(true);
	    		}else if(myMsg.equals("false")){
	    			bcv.menuMain.menuTurntoFun.seeBar.setEnabled(false);
	    		}
	    		
	    		
	    		//对应百分比
	    	
	    		super.handleMessage(msg);
	    	}
	    	
	    };
	    
	    private void turnPageOn(int delayMillis) {
			this.delayMillis = delayMillis;
			handler.postDelayed(runnable, delayMillis);
		}

		private void turnPageOff() {
			handler.removeCallbacks(runnable);
			
		}

		private void initEvent() {
			threeDimMain.getPageViews().setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (arg0 == threeDimMain.getPageViews()) {
						
						turnPageOff();
						boolean ret = threeDimMain.getPageViews().doTouchEvent(arg1);

						if (arg1.getAction() == MotionEvent.ACTION_UP) {
							
							turnPageOn(5);
							threeDimMain.getPageViews().invalidate();
						}

						return ret;
					}
					return false;
				}

			});
		}
		public int getLineSpace() {
			// TODO Auto-generated method stub
			return bc.getLineSpace();
		}
		@Override
		public void colorChanged(int color) {
			// TODO Auto-generated method stub
			Paint mPaint = commentView.getTouchPaint();
			mPaint.setColor(color);
			commentView.setTouchPaint(mPaint);
		}
		public void creatColorPicker() {
			// TODO Auto-generated method stub
			Paint mPaint = commentView.getTouchPaint();
			ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, this, mPaint.getColor());
			colorPickerDialog.setCanceledOnTouchOutside(true);
			colorPickerDialog.show();
		}
		

}