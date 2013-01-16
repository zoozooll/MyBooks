package com.ccbooks.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ccbooks.adapter.CharactorSetAdapter;
import com.ccbooks.adapter.FontAdapter;
import com.ccbooks.bo.BookContentThread;
import com.ccbooks.bo.BookMarkBo;
import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.SeekBarStatusThread;
import com.ccbooks.dialog.CharDialog;
import com.ccbooks.flip.BookLayout;
import com.ccbooks.flip.PageContent;
import com.ccbooks.flip.SinglePage;
import com.ccbooks.fullscreen.listener.MenuCharactorSetClick;
import com.ccbooks.fullscreen.listener.MenuFontTTFSetClick;
import com.ccbooks.handles.BookContentShowHandler;
import com.ccbooks.listener.BookpagebgClick;
import com.ccbooks.listener.BtnBookMarkOnClickListener;
import com.ccbooks.listener.BtnBookStoreClick;
import com.ccbooks.listener.BtnCatalogueClick;
import com.ccbooks.listener.BtnCharactorClick;
import com.ccbooks.listener.BtnColorBlackClick;
import com.ccbooks.listener.BtnColorBrownClick;
import com.ccbooks.listener.BtnFontClick;
import com.ccbooks.listener.BtnFontEnterButtonAClick;
import com.ccbooks.listener.BtnFontEnterButtonClick;
import com.ccbooks.listener.BtnFontTTFClick;
import com.ccbooks.listener.BtnFullScreenClick;
import com.ccbooks.listener.BtnLightClick;
import com.ccbooks.listener.ClearListener;
import com.ccbooks.listener.SbrMainContentOnChangeListener;
import com.ccbooks.listener.charactorSet.CharactorSetClick;
import com.ccbooks.listener.charactorSet.FontTTFSetClick;
import com.ccbooks.listener.flip.SinglePageOnclickListener;
import com.ccbooks.myUtil.StringTool;
import com.ccbooks.util.CcbooksAction;
import com.ccbooks.util.CursorController;
import com.ccbooks.util.Font;
import com.ccbooks.util.FontListItem;
import com.ccbooks.util.StringConfig;
import com.ccbooks.view.widget.CartoonWidget;
import com.ccbooks.vo.Book;
import com.ccbooks.vo.CatelogItem;
import com.chinachip.book.chardetextor.CharDetect;
import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginUtil;
import com.chinachip.ccbooks.core.BookCore;
import com.chinachip.ccbooks.core.TextSizeUtil;
import com.chinachip.tree.Node;


public class BookContentView extends Activity {

	// 所有控件；
	private LinearLayout lltContentTop;
	private LinearLayout lltContentFoot;
	private Button btnBookStore;
	private Button btnCatalogue;
	private Button btnLight;
	private Button btnFont;
	public Button btnBookMark;
	public BookLayout bookpagebg; 			// 竖屏书背景；
	
	public Spinner characterSp;
	private ArrayAdapter<String> aspnCountries;
	private List<String> allcountries;
	public Button btncharactor;
	public Button btnFontTTF;

	private TextView tvBookNameContent;
	private TextView tvBookAuthor;
	private static long markShowtime;
	private LinearLayout llyContentMain;
	public SeekBar sbrMainContent;
	public TextView mDialogText;			//百分比的textview
	private LinearLayout llyCatelogTitle;
	private LinearLayout btnLightEnter;		// 亮度调节悬浮框；
	public FrameLayout btnFontEnter; 		// 字体调节悬浮框；
	private Button btnFontEnterButtona1; 	// 字体变小按钮；
	private Button btnFontEnterButtonA2; 	// 字体变大按钮；
	private Button btnFontEnterColorBlack; 	// 白背景按钮；
	private Button btnFontEnterColorBrown; 	// 棕色背景按钮；
	public LinearLayout mDialogLayout;		//百分比的layout
	public Button btnFullScreen; 			//全屏阅读模式切换；
	public LinearLayout spinner_ll;
	public FrameLayout btncharactorL;
	public CharactorSetAdapter charactorSetAdapter;
	public ListView charactorList;
	
	public static SeekBarStatusThread seekBarThread = null;
	

	public SeekBar btnLightBar; 			// 屏幕亮度控制拖动条；
	//private BookLayout blo;
	
	public short portraint; 				// 横竖屏显示 横屏为2，竖屏为1；
	public int backgroundColor = StringConfig.WHITE_BACKGROUP_COLOR;		//背景顏色；
	public static float ness ; 				//屏幕亮度当前值
	
	public static final int BCVFOCUS = 0;//焦点为内容页
	public static final int LIGHTFOCUS = 1;//焦点为亮度设置
	public static final int	FONTFOCUS = 2;//焦点为字体设置
	public int focus = BCVFOCUS;
	public int showSettingView =BCV ;//0为显示主界面， 1为显示亮度设置，2为显示字体设置
	public static final int BCV = 0;//显示内容页
	public static final int LIGHT = 1;//显示亮度设置
	public static final int	FONT = 2;//显示字体设置
	public short showFullScreen;//1为正常显示，2为选中
	
	/**获得书ID*/
	public static int id;
	/**获得书类*/
	public static Book book;

	public int lineIndex; // 开头行号
	public int lineCount;	//	总行数；
	public int isMark;	//是否书签,并显示书签的值；
	public boolean isMove = false;
	//public boolean  inFirstPage ;
	//public boolean inLastPage ;
	public CursorController cc;
	public CatelogItem mark;
	public boolean hideViews;//是否隐藏图标
	
	public int screenWidth;
	public int screenHeight;
	
	public boolean turnable;
	/**该文件是否存在临时解析文件*/
	//public boolean isTmpFile;	
	/**对话框提示*/
	public Dialog dg;
	
	public static int[] imgs = new int[12];
	public boolean mShowing;//是否显示百分比
	public Handler mHandler = new Handler();
	public RemoveWindow mRemoveWindow = new RemoveWindow(); 

	ArrayList<List<String>> contentList ;
	public int isZeroPage;	//判断是否第一页；
	
	public static BookCore bc = null;
	public BookContentThread bct ;
	public List<String> content; //显示当前页的内容
	public long markPoint=0; //当前显示页的索引值；
	public long nextPoint=0;//下一页索引值
	public long preventPoint=-1;//上一页索引值；
	public long nextEnd = -1; 
	public boolean isFileEnd;	//是否文件结尾判断；
	public Handler bcHandler;
	public boolean isRunningThread;	//判断是否有线程在运行；
	private boolean firstEnd = false;
	private boolean firstBegin = false;
	public static BooksBo bo = null;
	public static int file = 0;
	public long fileLength = 0;
	public int headFile;
	public int endFile;
	public Font font = null;
	public String ttfFileName = "Clockopia";
	public ArrayList<FontListItem> fontList = null;
	public FontAdapter fontAdapter = null;
	public SeekBarHandler seekBarHandle = null;
	
	//mDialog的左偏移初始值；
	public int mDialogPaddingLeft;
	public int mDialogPaddingBottom;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getInt("bookId");
		int file = bundle.getInt("file");
		boolean downLand = bundle.getBoolean("DownLand");
		isMove = false;
		//inFirstPage = false;
		firstEnd = false; 
		firstBegin = false;
		hideViews = true;	//初始化
		markShowtime = 0;
		bo = new BooksBo(this, false);
		bcHandler = new BookContentShowHandler(this);
		getScreenSize();
		if (this.getResources().getConfiguration().orientation == 
				Configuration.ORIENTATION_PORTRAIT) { 
			// 竖屏
			portraint = 1;
			setContentView(R.layout.book_page);

		} else {
			// 横屏
			portraint = 2;
			setContentView(R.layout.book_page_horizontal);
		}
		
		
		//初始化解析引擎
		TextSizeUtil.fontSize = 22;
		SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);	
		ttfFileName = sp.getString("ttfFileName", ttfFileName);
		font = new Font(TextSizeUtil.fontSize, ttfFileName);
		int drawingWidth=0;
		int drawingHeight=0;
		if(portraint == 1)
		{
			drawingWidth = screenWidth-40*2;
			drawingHeight = screenHeight-screenHeight*3/200-screenHeight*9/400-75-21;
		}else if(portraint == 2){
			drawingWidth = screenWidth/2-40*2;
			drawingHeight = screenHeight-screenHeight/40-screenHeight/30-70-12;
		}
		bc = new BookCore(TextSizeUtil.fontSize, 2, drawingWidth, drawingHeight);
		bc.fu.setPaint(font.getPaint());
		if(file == 0){
			try{
				book = bo.getOne(id);
			}catch ( SQLException e ) {
				e.printStackTrace();
			}
			String url = book.bookpath;
			Plugin plugin = bc.pm.getPlugin(url);
			Param param = new Param();
			int filehandle = bc.pm.open(plugin, url);
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
					Toast.makeText(this, "无法打开的文件!", Toast.LENGTH_SHORT).show();
					book = null;
					bc.pm.closeAll();
					this.finish();
					return;
				
				}else if(prop == PluginUtil.BOOK_TYPE_COMIC){
					Toast.makeText(this, "无法打开的文件!", Toast.LENGTH_SHORT).show();
					book = null;
					bc.pm.closeAll();
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
		
		sendBroadcast();	//发送广播更新widget
		findview();
		doing();
		
		new Thread(new InitThread(file)).start();
		
		switchButton();
		addListener();
		if(downLand){
			isDownLand();
		}
		
	}
	
	private boolean isDownLand(){
		seekBarThread = new SeekBarStatusThread(this);
		seekBarHandle = new SeekBarHandler(this);
		new Thread(seekBarThread).start();
		return false;
	}
	
	/**切换屏幕时候的显示*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			for (int i=0;i<10;i++){
				if(bookpagebg.isAutoFlip() || isRunningThread || dg.isShowing()){
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
		{   
			portraint = 1;
			setContentView(R.layout.book_page);
			
		}

		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
		{
			portraint = 2;
			setContentView(R.layout.book_page_horizontal);
			
		}
		markShowtime = 0; 
		findview();
		addListener();
		getScreenSize();
		int drawingWidth = 0;
		int drawingHeight = 0;
		if(portraint == 1)
		{
			drawingWidth = screenWidth-40*2;
			drawingHeight = screenHeight-screenHeight*3/200-screenHeight*9/400-75-21;
		}else if(portraint == 2){
			drawingWidth = screenWidth/2-40*2;
			drawingHeight = screenHeight-screenHeight/40-screenHeight/30-70-12;
		}
		bc.setPageSize(drawingWidth,drawingHeight);
		getTopPeddingTag();
		setBackground(this.backgroundColor);
		bookMark((int)markPoint,file);
		String charset = bc.getEnc();
		bo.updateChartset(book.id, charset);
		book = bo.getOne(id);
		String[] charList = StringConfig.CHARSETS;
		int selected = 0;
		for(int i = 0; i< charList.length; i++ )
		{
			if(charList[i].equals(charset))
				selected = i;
		}
		charactorSetAdapter = new CharactorSetAdapter(this,StringConfig.CHARSETS,selected);
		charactorSetAdapter.setMode(1);
		charactorList.setAdapter(charactorSetAdapter);
		charactorList.setOnItemClickListener(new CharactorSetClick(this, charactorSetAdapter));
		super.onConfigurationChanged(newConfig);
	}
	
	
	@Override
	public void finish() {
		if (book !=null && fileLength != 0 ) {
			BooksBo bo = new BooksBo(this, false);
				Log.i("zkli", "savePoint"+markPoint);
			bo.addCurIndex(book.id, (int)markPoint, 0,file);
			if(content != null)
			bo.saveOrUpdateHistory(book.bookname, (int)markPoint, content.get(0));
			SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putFloat("ness", ness);
			editor.putInt("font_size", TextSizeUtil.fontSize);
			editor.putInt("colorBackground", backgroundColor);
			editor.commit();
			System.gc();
		}
		if (dg!=null&&dg.isShowing()){
			dg.cancel();
		}
		bc.pm.closeAll();
		super.finish();
	}
	
	/**
	 * 2011-3-25下午04:20:33
	 */
	private void findview() {
		lltContentTop = (LinearLayout) findViewById(R.id.lltContentTop);
		lltContentFoot = (LinearLayout) findViewById(R.id.lltContentFoot);
		btnBookStore = (Button) findViewById(R.id.btnBookStore);
		btnCatalogue = (Button) findViewById(R.id.btnCatalogue);
		btnLight = (Button) findViewById(R.id.btnLight);
		btnFont = (Button) findViewById(R.id.btnFont);
		btnBookMark = (Button) findViewById(R.id.btnBookMark);
		tvBookNameContent = (TextView) findViewById(R.id.tvBookNameContent);
		tvBookAuthor = (TextView) findViewById(R.id.tvBookAuthor);
		llyContentMain = (LinearLayout) findViewById(R.id.llyContentMain);
		//tvMainContent = (TextView) findViewById(R.id.tvMainContent);
		sbrMainContent = (SeekBar) findViewById(R.id.sbrMainContent);
		llyCatelogTitle = (LinearLayout) findViewById(R.id.llyCatelogTitle);
		bookpagebg = (BookLayout) findViewById(R.id.bookpagebg);
		btnLightEnter = (LinearLayout) findViewById(R.id.btnLightEnter);
		btnFontEnter = (FrameLayout) findViewById(R.id.btnFontEnter);
		btnFontEnterButtona1 = (Button) findViewById(R.id.btnFontEnterButtona);
		btnFontEnterButtonA2 = (Button) findViewById(R.id.btnFontEnterButtonA);
		btnFontEnterColorBlack = (Button) findViewById(R.id.btnFontEnterColorBlack);
		btnFontEnterColorBrown = (Button) findViewById(R.id.btnFontEnterColorBrown);
		btnLightBar = (SeekBar) findViewById(R.id.btnLightBar);
		mDialogText = (TextView)findViewById(R.id.mDialog);
		mDialogLayout = (LinearLayout)findViewById(R.id.mDialogLayout);//百分比的linearlayout
		btnFullScreen = (Button)findViewById(R.id.btnFullScreen);
		btncharactor = (Button)findViewById(R.id.btncharactor);
		charactorList = (ListView)findViewById(R.id.charactorList);
		btnFontTTF = (Button)findViewById(R.id.btnfontTTF);
		btncharactorL = (FrameLayout)findViewById(R.id.btncharactorL);
		
		
	}

	private void addListener() {
		BtnBookStoreClick btnBookStoreClick = new BtnBookStoreClick(this);
		btnBookStore.setOnClickListener(btnBookStoreClick);
		BookpagebgClick bookpagebgClick = new BookpagebgClick(this,btnLightEnter,
				btnFontEnter);
		bookpagebg.setOnClickListener(bookpagebgClick);
		
		BtnCatalogueClick btnCatalogueClick = new BtnCatalogueClick(this,id,true);
		btnCatalogue.setOnClickListener(btnCatalogueClick);
		
		BtnLightClick btnLightClick = new BtnLightClick(this, btnLightEnter,
				btnFontEnter);
		btnLight.setOnClickListener(btnLightClick);
		
		BtnFontClick btnFontClick = new BtnFontClick(this,btnLightEnter,
				btnFontEnter);
		btnFont.setOnClickListener(btnFontClick);
		
		BtnFontEnterButtonClick btnFontEnterButtonClick = new BtnFontEnterButtonClick(this,
				null, null, null,
				btnFontEnterButtona1,btnFontEnterButtonA2, 
				btnFontEnterColorBlack,btnFontEnterColorBrown);
		btnFontEnterButtona1.setOnClickListener(btnFontEnterButtonClick);
		
		BtnFontEnterButtonAClick btnFontEnterButtonA2Click = new BtnFontEnterButtonAClick(
				this,null, null, null,
				btnFontEnterButtona1, btnFontEnterButtonA2,
				btnFontEnterColorBlack, btnFontEnterColorBrown);
		btnFontEnterButtonA2.setOnClickListener(btnFontEnterButtonA2Click);
		
		BtnColorBlackClick btnColorBlackClick = new BtnColorBlackClick(this,
				bookpagebg, btnFontEnterButtona1, btnFontEnterButtonA2,
				btnFontEnterColorBrown);
		btnFontEnterColorBlack.setOnClickListener(btnColorBlackClick);
		
		BtnColorBrownClick btnColorBrownClick = new BtnColorBrownClick(this,
				bookpagebg, btnFontEnterButtona1, btnFontEnterButtonA2,
				btnFontEnterColorBlack);
		btnFontEnterColorBrown.setOnClickListener(btnColorBrownClick);
		
		BtnCharactorClick  btnCharactorClick = new BtnCharactorClick(id, true, btncharactor, this);
		btncharactor.setOnClickListener(btnCharactorClick);
		
		BtnFullScreenClick btnFullScreenClick = new BtnFullScreenClick(id, true, btnFullScreen, this);
		
		btnFullScreen.setOnClickListener(btnFullScreenClick);
		
		btnLightBar.setOnSeekBarChangeListener(btnLightBarClick);
		
		btnBookMark.setOnClickListener(new BtnBookMarkOnClickListener(this));
	
		sbrMainContent.setOnSeekBarChangeListener(new SbrMainContentOnChangeListener(this));
		bookpagebg.setLongClickable(true);//手势滑动需要
		bookpagebg.setOnTouchListener(new SinglePageOnclickListener(this));
		
		BtnFontTTFClick fontTTFClick = new BtnFontTTFClick(id, true, btnFontTTF, this);
		btnFontTTF.setOnClickListener(fontTTFClick);
		//btnSearch.setOnClickListener();
		ClearListener cl = new ClearListener();
		btnLightEnter.setOnClickListener(cl);
		btnFontEnter.setOnClickListener(cl);
	}

/*	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "字符集设置");
		menu.findItem(0);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()){
		case 0:
			actionClickMenuItem();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void actionClickMenuItem(){
		spinner.setVisibility(View.VISIBLE);
	}
	*/
	private void doing() {
		
		
		SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
		ness = sp.getFloat("ness", 100);
		TextSizeUtil.fontSize = sp.getInt("font_size", 22);
		backgroundColor =  sp.getInt("colorBackground", StringConfig.WHITE_BACKGROUP_COLOR);
		if(TextSizeUtil.fontSize >=38){
			btnFontEnterButtonA2.setEnabled(false);
		}
		if(TextSizeUtil.fontSize <=18){
			btnFontEnterButtona1.setEnabled(false);
		}
		setBtnBackgroundColor();
		setBackground(backgroundColor);
		btnLightBar.setProgress((int)ness);
		brightnessMax(ness);
		//百分比的run
		mShowing = false;
		showDialog();
		mDialogText.setVisibility(View.INVISIBLE);
		getTopPeddingTag();
	}

	/**
	 * 设置颜色按钮
	 * 
	 * @author lamkaman
	 * Created on 2011-5-20  上午11:20:01
	 */
	public void setBtnBackgroundColor() {
		if(backgroundColor == StringConfig.WHITE_BACKGROUP_COLOR){
			btnFontEnterColorBrown.setEnabled(true);
			btnFontEnterColorBlack.setEnabled(false);
		}else{
			btnFontEnterColorBrown.setEnabled(false);
			btnFontEnterColorBlack.setEnabled(true);
		}
	}

	//设置背景色
	private void setBackground(int backgroundColor) {
		System.out.println("setBackground backgroundColor"+backgroundColor);
		if (portraint == 1) {
			if(backgroundColor==StringConfig.BROWN_BACKGROUP_COLOR){
				bookpagebg.setBackgroundResource(R.drawable.bookpagebg_brown);
			}else{
				bookpagebg.setBackgroundResource(R.drawable.bookpagebg);
			}
		} else if (portraint == 2) {
			if(backgroundColor==StringConfig.BROWN_BACKGROUP_COLOR){
				bookpagebg.setBackgroundResource(R.drawable.book_page_bg_horizontal_brown);

			}else{
				bookpagebg.setBackgroundResource(R.drawable.book_page_bg_horizontal);
			}
		}
		
	}

	/**从书签中返回结果调用*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("zkli", "========ActivityResult========");
		if(resultCode>=0 && data !=null){
			Bundle bundle = data.getExtras();
			
			file = bundle.getInt("file");
			this.file = file;
			
			bookMark(resultCode,file);
		} else if (resultCode == -1){
			this.finish();
		} else if (resultCode == -2){
			BookMarkBo.isMark(this);
		}

	}
	
	
	private void showing(int file) {
		String enc = null;
		//tvBookNameContent.setText(book.bookname);
		if(file == 0){
		
			try {
				//open file
				String url = book.bookpath;
				Plugin plugin = bc.pm.getPlugin(url);
				Param param = new Param();
				
//				bc.pm.open(plugin, url);
				bc.pm.get(plugin, PluginUtil.BOOK_BACKLIST, param);
				bc.pm.bltree = param.bltree;
				Node root = (Node)bc.pm.bltree.getRoot();
				headFile = root.file;
				endFile = bc.pm.bltree.getEndFile();
				if(book.file > 0){
					this.file = book.file;
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
				
				fileLength = bc.pm.length(plugin, bc.filehandle);
			    if(fileLength == 0){
			    	this.finish();
					Toast.makeText(this, "无法打开的文件!", Toast.LENGTH_SHORT).show();
					return;
			    }
				bc.setDataSource(enc,book.bookpath);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			Plugin plugin = bc.pm.getDefaultPlugin();
			this.file = file;
			int filehandle = bc.pm.open(plugin, file);
			bc.setFilehandle(filehandle);
			bc.resetFile();
			
		}	
		bc.setFontSize(TextSizeUtil.fontSize);
		bc.setPortraint(portraint);
		book = bo.getOne(id);
		bct = new BookContentThread(this);
		bc.getCacheManager().resetCharset();
//		if(FileBo.BOOKTYPE_TXT.equals(book.booktype)){
			if (portraint == 1) {
				//int fontSize = (int) tvMainContent.getTextSize();
				cc = new CursorController(this,bc);
				if (book.curIndex > 0) {
					bookMark(book.curIndex,file);
				}else {
					bct.what =BookContentThread.FIRST_VIRI;
					new Thread(bct).start();
				}
				
			} else if (portraint == 2) {
				cc = new CursorController(this,bc);
				
				
				if (book.curIndex > 0) {
					
					bookMark(book.curIndex,file);
				}else {
					bct.what = BookContentThread.FIRST_HORI;
					new Thread(bct).start();
				}
			}
		
			String charset = bc.getEnc();
			bo.updateChartset(book.id, charset);
			book = bo.getOne(id);
			String[] charList = StringConfig.CHARSETS;
			int selected = 0;
			for(int i = 0; i< charList.length; i++ )
			{
				if(charList[i].equals(charset))
					selected = i;
			}
			charactorSetAdapter = new CharactorSetAdapter(this,StringConfig.CHARSETS,selected);
			charactorSetAdapter.setMode(1);
			charactorList.setAdapter(charactorSetAdapter);
			charactorList.setOnItemClickListener(new CharactorSetClick(this, charactorSetAdapter));
		    fileLength = getFileLength();
	}
	
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		Log.i("zkli","ball");
		return false;
	}
	
	/**亮度调节的端口*/
	private OnSeekBarChangeListener btnLightBarClick = new SeekBar.OnSeekBarChangeListener() {

		
		public void onStopTrackingTouch(SeekBar seekBar) {
			ness=seekBar.getProgress();
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			seekBar.setProgress((int)ness);
		}

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if(progress >2)
			{
				ness = (float) progress;
			}
			else
			{
				
			}
			brightnessMax(ness);

		}
	};

	//显示亮度
	private void brightnessMax(float ness) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		ness = ness/255.0f;
		if(ness>0.02)
		{
			lp.screenBrightness = ness;
		}
		else
		{
			lp.screenBrightness = 0.02f;
		}
		getWindow().setAttributes(lp);
	}
	
	/**从字符索引取得内容,书签，开头时候调用多*/
	public void bookMark(int markPoint,int file){
		Log.i("zkli", "point"+markPoint);
		showDialog();
		//bc.reset();
		bc.setPortraint(portraint);
//		if(FileBo.BOOKTYPE_TXT.equals(book.booktype)){
		
		bct.skipNumber = markPoint;
		
			if (portraint==1){
				
				bct.what = 7;
				
			}else if (portraint==2) {

				bct.what = 8;
			}
			new Thread(bct).start();
		
	}
	
	public void hiddenOpenWins(){
		System.out.println("hiddenopen wins");
		if(btnLightEnter.getVisibility()!=View.GONE){
			btnLightEnter.setVisibility(View.GONE);
		}
		if(btnFontEnter.getVisibility()!=View.GONE){
			btnFontEnter.setVisibility(View.GONE);
		}
		showSettingView = BCV ;
	}
	
	
	/**开头和结尾进度条显示*/
	public void lastPageShowing(){
		if(System.currentTimeMillis()>(2000+markShowtime))
		{
			if (preventPoint == -1){
					if(firstBegin == true)
					{
						firstBegin = false;
						markShowtime = System.currentTimeMillis();
						Toast.makeText(this, R.string.isBeginning, Toast.LENGTH_SHORT).show();
					}
					else
						firstBegin = true;
				}
				else
					firstBegin = false;
			
			if (nextPoint == -1){
				if(firstEnd == true)
				{
				//	firstEnd = false;
					markShowtime = System.currentTimeMillis();
					Toast.makeText(this, R.string.isEndding, Toast.LENGTH_SHORT).show();
			
				}
				else
					firstEnd = true;
					
			}
			else
				firstEnd = false;
	
		}
	}
	/**显示对话框*/
	public void showDialog(){
		if (dg==null || !dg.isShowing()){
			dg = new Dialog(this);
			dg.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dg.setContentView(R.layout.dia_loading);
			dg.show();
		}
	}
	/*隐藏百分比窗口*/
	public final class RemoveWindow implements Runnable{

		@Override
		public void run() {
			removeWindow();
		}		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		dg.dismiss();
	}

	public void removeWindow(){
		if(mShowing){
			mShowing = false;
			mDialogText.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**显示&隐藏view
	 * 以属性hideViews为ture时隐藏控件，否则显示控件；
	 * */
	public void switchButton() {
		hideViews = !hideViews;
		if(hideViews)//hideViews为true时隐藏图标
		{	
			hiddenOpenWins();
			btnBookMark.setVisibility(View.INVISIBLE);
			btnFont.setVisibility(View.INVISIBLE);
			btnLight.setVisibility(View.INVISIBLE);
			btnBookStore.setVisibility(View.INVISIBLE);
			btnCatalogue.setVisibility(View.INVISIBLE);
			btnFullScreen.setVisibility(View.INVISIBLE);
			sbrMainContent.setVisibility(View.INVISIBLE);
			tvBookNameContent.setVisibility(View.VISIBLE);

		
			if (portraint == 2){
				tvBookAuthor.setVisibility(View.VISIBLE);
			}
		}
		else//hideViews为false时显示图标
		{
			btnBookMark.setVisibility(View.VISIBLE);
			btnFont.setVisibility(View.VISIBLE);
			btnLight.setVisibility(View.VISIBLE);
			btnBookStore.setVisibility(View.VISIBLE);
			btnCatalogue.setVisibility(View.VISIBLE);
			btnFullScreen.setVisibility(View.VISIBLE);
			sbrMainContent.setVisibility(View.VISIBLE);
			tvBookNameContent.setVisibility(View.INVISIBLE);
			if (portraint == 2){
				tvBookAuthor.setVisibility(View.INVISIBLE);
			}
			if (mark!=null){
				btnBookMark.setBackgroundResource(R.drawable.content_btn_onmark);
			}else {
				btnBookMark.setBackgroundResource(R.drawable.content_btn_outmark);
			}
			//显示进度条
			try{
				sbrMainContent.setProgress((int)(markPoint*10000)/(int)fileLength);
			}catch(ArithmeticException e){
				e.printStackTrace();
			}
		}
		//虚拟按钮的显示隐藏功能
		if (bookpagebg.pageList!=null){
			for (SinglePage sp:bookpagebg.pageList){
				sp.setHideViews(hideViews);
			}
		}
		
	}
	
	/**
	 * 翻页效果转页
	 * */
	public void turnPage (List<String> str1,List<String> str2,List<String> str3,List<String> str4,List<String> str5,List<String> str6) {
		List<SinglePage> singlePageList = new ArrayList<SinglePage>();
		boolean isLeft = false;
		int num = 0;
		for(int i=0;i<imgs.length;i++){
			PageContent content = new PageContent(this,imgs[i],bookpagebg,isLeft);
			content.setTextSize(com.chinachip.ccbooks.core.TextSizeUtil.fontSize);
			if(i==0||i==imgs.length-2||i==imgs.length-1){
				content.setCover(true);
			}else if(i!=1){
				content.setContentPage(true);
				content.setContentId(num++);
			}
			SinglePage page = new SinglePage(this,isLeft,i,bookpagebg,content,backgroundColor);
			singlePageList.add(page);
			isLeft = !isLeft;
		}
		
		contentList = new ArrayList<List<String>>();
		
		contentList.add(0,null);
		contentList.add(1, str1);   
		contentList.add(2, str2);
		contentList.add(3, str3);
		contentList.add(4, str4);
		contentList.add(5, str5);
		contentList.add(6, str6);
		/*contentList.add(7, al7);
		contentList.add(8, al8);*/
		
		bookpagebg.setContentList(contentList);
		
		//blo.setContentList(AndroidUtils.getPageContentStringInfo(m_paint, contenta, PageContent.getPageContentLine(BookLayout.PAGE_HEIGHT), PageContent.getPageContentWidth(BookLayout.PAGE_WIDTH)));
		bookpagebg.setPageList(singlePageList, 5);
		for (SinglePage sp:bookpagebg.pageList){
			sp.setHideViews(hideViews);
		}
	}
	
	public void turnPage (List<String>  str3,List<String>  str4,List<String> str5,List<String> str6){
		turnPage(null, null, str3, str4, str5, str6);
	}
	
	/**判断悬浮框的焦点
	 * @param nowfocus 传入现在的焦点
	 * @param flag 为1时隐藏悬浮框，为0时不改变
	 * */
	public int judgeFocus(int nowfocus,int flag){
		if(focus == nowfocus){//如果点击的是自己
			//do something
		}else{
			if(flag == 1){
				focus = BCVFOCUS;
				btnLightEnter.setVisibility(View.GONE);
				btncharactorL.setVisibility(View.VISIBLE);
				btncharactor.setVisibility(View.VISIBLE);
				charactorList.setVisibility(View.GONE);
				
				btnFontEnter.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.content_btn_font_enter));
				LayoutParams laParames = (LayoutParams)btnFontEnter.getLayoutParams();
				laParames.height = 250;
				btnFontEnter.setLayoutParams(laParames);
				btnFontEnter.setVisibility(View.GONE);
				showSettingView = BCV;
			}	
			return -1;
		}
		return 1;
	}
	
	
	private static final String[] mCountries = { "China" ,"Russia", "Germany",
		"Ukraine", "Belarus", "USA" };

	private void find_and_modify_view1() {
		characterSp = (Spinner) findViewById(R.id.spinner_1);
		allcountries = new ArrayList<String>();
		for (int i = 0; i < mCountries.length; i++) {
			allcountries.add(mCountries[i]);
		}
		aspnCountries = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allcountries);
		aspnCountries
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		characterSp.setAdapter(aspnCountries);
		
		
		
	}
	private void find_and_modify_view() {
		/*
		 * characterSp = (Spinner) findViewById(R.id.spinner_1);
		allcountries = new ArrayList<String>();
		for (int i = 0; i < mCountries.length; i++) {
			allcountries.add(mCountries[i]);
		}
		aspnCountries = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allcountries);
		aspnCountries
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		characterSp.setAdapter(aspnCountries);
		
		*/
		
	}
	public long getFileLength(){
		long fileLength = 0;
		Plugin plugin = bc.pm.getDefaultPlugin();
		fileLength = bc.pm.length(plugin, bc.pm.filehandle);
		
		if(fileLength > 0) return fileLength;
		else return 0;
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
				BookContentView.class.getName()));
		intent.setAction(String.valueOf(System.currentTimeMillis()));
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.ibEnterLastRead,
				pendingIntent);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		appWidgetManager.updateAppWidget(new ComponentName(this, CartoonWidget.class), remoteViews);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode){
		case KeyEvent.KEYCODE_BACK:
			bc.pm.closeAll();
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
		
	public String getCharSet(PluginMgr pm,Plugin plugin,int handle){
		ByteBuffer mbb = null;
		CharDetect charDetect = new CharDetect();
		int start = 0;
		int length = 0;
		int len = 5120;
		int detectLimit = 5*len;
		
		long fileLength = getFileLength();
		
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
	
	public boolean turnToFontTTFList(){
		LinearLayout bkmkListLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.ttf_dialog, null);
		String fontName = ttfFileName;
		bo.updateFont(book.id, fontName);
		fontList = font.getFontListItem();
		int selected = 0;
		for(int i = 0; i< fontList.size(); i++ )
		{
			if(fontList.get(i).getTtfFileName().equals(fontName))
				selected = i;
		}
		fontAdapter = new FontAdapter(this,fontList,selected);
		fontAdapter.setMode(1);
		ListView list = (ListView)bkmkListLayout.findViewById(R.id.fontff_list);
		list.setAdapter(fontAdapter);
		list.setOnItemClickListener(new FontTTFSetClick(this, fontAdapter));
		new CharDialog(this,bkmkListLayout).show();
//		AlertDialog aDCharset = new AlertDialog.Builder(this)
//				.setTitle(R.string.fontTTF_list)
//				.setView(bkmkListLayout)
//				.create();
//		aDCharset.setCancelable(true);
//		aDCharset.setCanceledOnTouchOutside(true);
//		aDCharset.show();
		return true;
	}
	
	public boolean turnToFontList(){
		LinearLayout bkmkListLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.char_dialog, null);
		String charset = bc.getEnc();
		bo.updateChartset(book.id, charset);
		String[] charList = StringConfig.CHARSETS;
		int selected = 0;
		for(int i = 0; i< charList.length; i++ )
		{
			if(charList[i].equals(charset))
				selected = i;
		}
		charactorSetAdapter = new CharactorSetAdapter(this,StringConfig.CHARSETS,selected);
		charactorSetAdapter.setMode(1);
		ListView list = (ListView)bkmkListLayout.findViewById(R.id.charactor_list);
		list.setAdapter(charactorSetAdapter);
		list.setOnItemClickListener(new CharactorSetClick(this, charactorSetAdapter));
		new CharDialog(this,bkmkListLayout).show();
		
//		charDailog.show();
//		AlertDialog aDCharset = new AlertDialog.Builder(this)
//				.setIcon(R.drawable.icon)
//				.setTitle(R.string.charactor_list)
//				.setView(bkmkListLayout)
//				.create();
//		aDCharset.setCancelable(true);
//		aDCharset.setCanceledOnTouchOutside(true);
//		aDCharset.show();
		return true;
	}
	
	/**
	 * 获得屏幕宽高
	 * 
	 * @date 2011-6-2下午04:28:03
	 * @author Lee Chok Kong
	 */
	public void getScreenSize(){
		WindowManager windowManager = getWindowManager();  
		Display display = windowManager.getDefaultDisplay();   
		screenWidth = display.getWidth();  
		screenHeight = display.getHeight(); 
	}
	
	//设置头部第一个按钮的偏移量；
	public void getTopPeddingTag(){
		int left = 0;
		int top  = 0;
		int right = 0;
		int bottom=0;
		//顶部padding参数；
		if (portraint==2){
			left=screenWidth/32+1;
			top = screenHeight/40-6;
			right = screenWidth/32+5;
		}else {
			left=5;
			top = screenHeight*3/200-1;
			right =screenWidth/24+10;
		}
		
		lltContentTop.setPadding(left, top, right, bottom);
		
		//底部padding参数；
		if (portraint==2) {
			left = screenWidth/2/16+75;
			right =screenWidth/2/16+75;
			top = 0;
			bottom = screenHeight/30+5;
		}else {
			left = 65;
			right = 65;
			top = 0;
			bottom = screenHeight/32;
		}
		lltContentFoot.setPadding(left, top, right, bottom);
		
		//mDialogPaddingLeft的计算；
		if(portraint ==1){
			//在480×800时候偏移数值为48;
			//基础算法 :x左偏移量+本应该的偏移量；
			mDialogPaddingLeft=50;
			//基础算法：BookLayout的偏移距离下方偏移量+上方偏移量；
			mDialogPaddingBottom= screenHeight*9/400+40;
		}else {
			mDialogPaddingLeft = screenWidth/2/16+60;
			mDialogPaddingBottom = screenHeight*9/400+40;
		}
	}
	
	public class SeekBarHandler extends Handler {
	    	
		  BookContentView bcv;
	    	
	    	public SeekBarHandler(BookContentView bcv) {
	    		super();
	    		this.bcv = bcv;
	    		
	    	}


	    	@Override
	    	public void handleMessage(Message msg) {
	    		// TODO Auto-generated method stub
	    		String myMsg = (String) msg.obj;
	    		if(myMsg.equals("true")){
	    			bcv.sbrMainContent.setEnabled(true);
	    		}else if(myMsg.equals("false")){
	    			bcv.sbrMainContent.setEnabled(false);
	    		}
	    		
	    		
	    		//对应百分比
	    	
	    		super.handleMessage(msg);
	    	}
	    	
	    };
	    
	public class InitThread implements Runnable{

		private int file;
		
		public InitThread(int file) {
			this.file = file;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			showing(file);
		}
		
	};

}
