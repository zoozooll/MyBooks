package com.ccbooks.view;

import java.util.ArrayList;
import java.util.List;

import com.ccbooks.adapter.CatelogAdapter;
import com.ccbooks.adapter.DirectoryAdapter;
import com.ccbooks.bo.BookMarkBo;
import com.ccbooks.bo.BooksBo;
import com.ccbooks.bookCatalog.listener.BTBackClick;
import com.ccbooks.bookCatalog.listener.DirectoryClick;
import com.ccbooks.listener.BtnBookStoreClick;
import com.ccbooks.listener.BtnCatalogueClick;
import com.ccbooks.listener.BtnContentCatelogClick;
import com.ccbooks.listener.BtnContentMarkClick;
import com.ccbooks.listener.bookCatalog.LvCatalogOnItemClickListener;
import com.ccbooks.util.StringConfig;
import com.ccbooks.vo.Book;
import com.ccbooks.vo.CatelogItem;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.ccbooks.core.BookCore;
import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
/**
 * @author Lee Chok Hong
 * 2011-3-31 下午03:07:16
 */
public class BookCatalogView extends Activity {
	
	// 所有控件；
	private Button btnBookStore;
	private Button btnCatalogue;
	public Button btnBookMark;
	private LinearLayout bookpagebg; // 竖屏书背景；

	private TextView tvBookNameContent;
	// private LinearLayout llyContentMain;
	private LinearLayout llyContentMain;
	private LinearLayout llyCatelogTitle;
	private Button btnContentCatelog;
	public Button btnContentMark;
	public ListView lvCatalog;
	
	public short showCatelogType; // 目录显示规则 1表示目录，2表示书签；
	public short portraint; // 横竖屏显示 横屏为2，竖屏为1；	

	public Book book;
	public CatelogItem mark;
	private TextView tvEmpty; 
	public int backgroundColor = StringConfig.WHITE_BACKGROUP_COLOR;
	//private static float ness ; 
	/**items*/
	public List<CatelogItem> items;
	
	public static ArrayList<Node> nodeList = null;
	public BackList myTree;
	public Button btBack;
	private DirectoryAdapter mAdapter;
	public int id = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		if(BookContentView.ness>0.02f)
		{
		    if(BookContentView.ness>1)
				lp.screenBrightness = BookContentView. ness/255;
			else
				lp.screenBrightness = BookContentView. ness/255;
		}
		else
		{
			
			lp.screenBrightness = 0.02f;
		}
		getWindow().setAttributes(lp);
		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getInt("bookId");
		BooksBo bo = new BooksBo(this, false);
		book = bo.getOne(id);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { // ����
			portraint = 1;
			setContentView(R.layout.book_catalog);
		} else {
			portraint = 2;
			setContentView(R.layout.book_catalog_horizontal);
		}
		findview();
		addListener();
		doing();
	}
	
	private void findview() {
		bookpagebg = (LinearLayout) findViewById(R.id.bookpagebg);
		btnBookStore = (Button) findViewById(R.id.btnBookStore);
		btnCatalogue = (Button) findViewById(R.id.btnLastread);
		if (portraint == 2)
		{
			tvBookNameContent = (TextView) findViewById(R.id.tvBookNameContent); 
		}
		llyContentMain = (LinearLayout) findViewById(R.id.llyContentMain);
		llyCatelogTitle = (LinearLayout) findViewById(R.id.llyCatelogTitle);
		btnContentCatelog = (Button) findViewById(R.id.btnContentCatelog);
		btnContentMark = (Button) findViewById(R.id.btnContentMark);
		lvCatalog = (ListView) findViewById(R.id.lvCatalog);
		tvEmpty  = (TextView) findViewById(android.R.id.empty);
		if (portraint == 1) {
			
		} else if (portraint == 2) {
			
		}
		initUI();
	}
	
	/**
	 * void
	 * 2011-3-31下午03:04:50
	 */
	private void initUI() {
		lvCatalog = (ListView) findViewById(R.id.lvCatalog);
		PluginMgr pm = BookContentView.bc.pm;
		myTree = pm.bltree;
		Node root = (Node)myTree.getRoot();
		nodeList = myTree.getNextNodeList(root);
		mAdapter = new DirectoryAdapter(this, nodeList,1);
		lvCatalog.setOnItemClickListener(new DirectoryClick(this,mAdapter));
		btBack = (Button)findViewById(R.id.btBack);
		BTBackClick btBackClick = new BTBackClick(this, mAdapter);
		btBack.setOnClickListener(btBackClick);
	}



	private void addListener() {
		
		BtnCatalogueClick btnCatalogueClick = new BtnCatalogueClick(this,book.id,false);
		btnCatalogue.setOnClickListener(btnCatalogueClick);
		BtnContentCatelogClick btnContentCatelogClick = new BtnContentCatelogClick(
				this,btnContentCatelog,btnContentMark,lvCatalog );
		btnContentCatelog.setOnClickListener(btnContentCatelogClick);
		BtnContentMarkClick btnContentMarkClick = new BtnContentMarkClick(
				this,btnContentCatelog, btnContentMark);
		btnContentMark.setOnClickListener(btnContentMarkClick);
		BtnBookStoreClick btnBookStoreClick = new BtnBookStoreClick(this);
		btnBookStore.setOnClickListener(btnBookStoreClick);
	//	System.out.print("onCreate: 10\n");
		
	}

	private void doing() {
		SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
		backgroundColor =  sp.getInt("colorBackground", StringConfig.WHITE_BACKGROUP_COLOR);
		System.out.println("catlogview backgroundColor"+backgroundColor);
		if (portraint == 1) {
			if(backgroundColor==StringConfig.BROWN_BACKGROUP_COLOR ){
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
		showCatelogType = 2;
		showing();

	}

	public void showing() {
		//btnCatalogue.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_transparent));
		//btnCatalogue.setText("续读");
		
		
		if (portraint == 1) {
			
			
		} else if (portraint == 2) {
			tvBookNameContent.setText(book.bookname);   //处理?	
		}
		if(showCatelogType == 1){
			showListCatelog();	
		}else if (showCatelogType ==2){
			showListMark();	
		}
		 
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
		{   
			portraint = 1;
			setContentView(R.layout.book_catalog);
		}

		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
		{
			portraint = 2;
			setContentView(R.layout.book_catalog_horizontal);
		}
		findview();
		addListener();
		doing();
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			setResult(-2);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**显示目录列表项*/
	private void showListCatelog() {
		lvCatalog.setOnItemClickListener(null);
		

		lvCatalog.setOnItemClickListener(new DirectoryClick(this,mAdapter));
		lvCatalog.setAdapter(mAdapter);
	
		tvEmpty.setText("本书没有目录章节");
		lvCatalog.setEmptyView(tvEmpty);
	}
	
	/**显示书签列表项*/
	private void showListMark() {
		lvCatalog.setOnItemClickListener(null);
		lvCatalog .setOnItemClickListener( new LvCatalogOnItemClickListener(this));
		BookMarkBo bo = new BookMarkBo(this);
		items= bo.readMarksByDb(book.bookname);
		CatelogAdapter adapter = new CatelogAdapter(this, items);
		lvCatalog.setAdapter(adapter);
		tvEmpty.setText("本书未有保存的书签");
		lvCatalog.setEmptyView(tvEmpty);
	}
	
}
