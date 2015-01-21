package com.ccbooks.view;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ccbooks.adapter.BookInfosAdapter;
import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.FileBo;
import com.ccbooks.dao.BooksDao;
import com.ccbooks.listener.BtnOnlineBookStoreClick;
import com.ccbooks.listener.bookListView.BtnAboutUsOnclick;
import com.ccbooks.listener.bookListView.BtnShowAllListOnClickListener;
import com.ccbooks.vo.Book;
import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginUtil;

public class BookListView extends ListActivity implements OnScrollListener {

	//当前使用到的常量;
	public static final short SHOW_TYPE_ALL = 1;
	public static final short SHOW_TYPE_HISTORY = 2;
	public static final short SHOW_TYPE_SEARCH = 3;
	
	// 各组控件
	private Button btnBookStore;
	private Button btnBookshelf;
	private Button btnBooklist;
	public Button btnEdit;
	private Button btnReflesh;
	public Button btnShowAllList;	//显示全部按钮；
	public Button btnShowHistory;	//显示历史列表按钮;
	public Button btnShowSearch;		//搜索按钮
	private Button btnAbout;	//关于自己按钮
	private int pos;

	public BookInfosAdapter bia;
	private Dialog pd;
	public BooksBo butil;
	// public List<ImageView> ivs;
	public  boolean isEdit;
	public static updateHandler mHandler;
	
	/**显示的状态，1 表示全部，2代表历史,3代表搜索结果*/
	public short showType = SHOW_TYPE_ALL;
	
	public PluginMgr pm;
	
	public Plugin plugin;
	
	public Book book;
	
	public BooksBo bo;
	
	public int filehandle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//从Intent中获得showType；
		showType=  getIntent().getShortExtra("bookListShowType", SHOW_TYPE_ALL);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { // ����
			setContentView(R.layout.bookslist);
		} else {
			setContentView(R.layout.bookslist_h);
		}
		mHandler = new updateHandler();
		getListView().setScrollbarFadingEnabled(true);
		SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
		BookShelfView.readerMode = sp.getInt("readerMode", 1);
		findview();
	}
	@Override
	protected void onResume() {
		super.onResume();
		doing();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (pd!=null && pd.isShowing()){
			pd.cancel();
		}
		BooksDao.getDao(this).close();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			searchBooksDialog();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void doing() {
		butil = new BooksBo(this, false);
		butil.firstBookList();
		showview();
		btnBooklist.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.listonclick));
		btnBookshelf.setOnClickListener(btnBookshelfOnclick);
		btnReflesh.setOnClickListener(btnRefleshOnclick);
		btnEdit.setOnClickListener(btnEditOnclick);
		BtnShowAllListOnClickListener btnShowAllListOnClickListener = new BtnShowAllListOnClickListener(this);
		btnShowAllList .setOnClickListener(btnShowAllListOnClickListener);
		btnShowHistory.setOnClickListener(btnShowAllListOnClickListener);
		btnBookStore.setOnClickListener(new BtnOnlineBookStoreClick(this));
		btnShowSearch.setOnClickListener(btnShowAllListOnClickListener);
		btnAbout.setOnClickListener(new BtnAboutUsOnclick());
	}

	private void showview() {
		if (showType==SHOW_TYPE_ALL){
			this.bia = new BookInfosAdapter(this, BooksBo.books);
			btnShowHistory.setBackgroundResource(R.drawable.outclickbutton_right);
			btnShowAllList.setBackgroundResource(R.drawable.onclickbutton_left);
		} else if (showType==SHOW_TYPE_HISTORY) {
			this.bia = new BookInfosAdapter(this, butil.getHistoryBookList());
			btnShowHistory.setBackgroundResource(R.drawable.onclickbutton_right);
			btnShowAllList.setBackgroundResource(R.drawable.outclickbutton_left);
		}
		
		setListAdapter(bia);
		getListView().setSelection(pos);
		getListView().setOnScrollListener(this);
	}

	private void findview() {
		btnBookStore = (Button) findViewById(R.id.btnBookStore);
		btnBookshelf = (Button) findViewById(R.id.btnBookshelf);
		btnBooklist = (Button) findViewById(R.id.btnBooklist);
		btnEdit = (Button) findViewById(R.id.btnEdit);
		btnReflesh = (Button) findViewById(R.id.btnReflesh);
		btnShowAllList = (Button) findViewById(R.id.btnShowAllList);
		btnShowHistory = (Button) findViewById(R.id.btnShowHistory);
		btnShowSearch = (Button) findViewById(R.id.btnShowSearch);
		btnAbout = (Button) findViewById(R.id.btnAbout);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		pos = position;
		if(isEdit)
		{	
			//编辑的时候根据编辑状态；
			//Toast.makeText(this, "请先退出编辑模式", Toast.LENGTH_SHORT).show();
			if (showType == SHOW_TYPE_ALL){
				android.content.DialogInterface.OnClickListener diol =
					new android.content.DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch (which) {
							case AlertDialog.BUTTON_POSITIVE:
								butil.deletFile(pos);
								bia.notifyDataSetChanged();
								dialog.cancel();
								break;
							case AlertDialog.BUTTON_NEGATIVE:
								dialog.cancel();
								break;
							default:
								break;
							}
						}
					
				};
				
				AlertDialog adg = new AlertDialog.Builder(this).
					setTitle(com.ccbooks.view.R.string.deletetitle)
					.setPositiveButton("删除", diol)
					.setNegativeButton("取消", diol)
					.create();
				adg.show();
			} else if (showType == SHOW_TYPE_HISTORY) {
				android.content.DialogInterface.OnClickListener diol =
					new android.content.DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case AlertDialog.BUTTON_POSITIVE:
								List<Book> historyBooks = butil.getHistoryBookList();
								butil.deleteHistoryBookItem(historyBooks.get(pos).bookname);
								historyBooks.remove(pos);
								bia.books=historyBooks;
								bia.notifyDataSetChanged();
								dialog.cancel();
								break;
							case AlertDialog.BUTTON_NEGATIVE:
								dialog.cancel();
								break;
							default:
								break;
							}
						}
					
				};
				
				AlertDialog adg = new AlertDialog.Builder(this).
					setTitle("是否删除本条历史记录")
					.setPositiveButton("删除", diol)
					.setNegativeButton("取消", diol)
					.create();
				adg.show();
			}
			
		}	
		else
		{		
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			int fileID = bia.books.get(position).id;
			bundle.putInt("bookId", fileID);
			
			intent.putExtras(bundle);
			bo = new BooksBo(this, false);
			try{
				book = bo.getOne(fileID);
			}catch ( SQLException e ) {
				e.printStackTrace();
			}
			pm = new PluginMgr();
	        PluginUtil.PluginInit(pm);
			plugin = pm.getPlugin(book.bookpath);
			Param param = new Param();
			filehandle = pm.open(plugin, book.bookpath);
			pm.get(plugin, PluginUtil.BOOK_TYPE, param);
			pm.closeAll();
			if(param.i == PluginUtil.BOOK_TYPE_COMIC){
				intent.setClass(this, CartoonReader.class);
			}else if(getReaderMode() == 1)
			{
				intent.setClass(BookListView.this, BookContentView.class);
			}else if(getReaderMode() == 2)
			{
				intent.setClass(BookListView.this, TextReader.class);
			}
			startActivity(intent);
			super.onListItemClick(l, v, position, id);
		}
		
	}
	
	private OnClickListener btnBookshelfOnclick = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			//跳转翻页样式效果；
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Intent  mainIntent  =  new  Intent(BookListView.this,BookShelfView.class);        
						BookListView.this.startActivity(mainIntent);        
						BookListView.this.finish();        				     
						overridePendingTransition(R.anim.zoomin,        
				                  R.anim.zoomout);           
				}
			}, 0);
		}
	};

	private OnClickListener btnRefleshOnclick = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			pd = new Dialog(BookListView.this);
			pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
			pd.setContentView(R.layout.dia_loading);
			pd.setCancelable(false);
			FileBo fileUtil = new FileBo(BookListView.this, "/sdcard/bluereader");
			new Thread(fileUtil).start();
			pd.show();
		}

	};

	private OnClickListener btnEditOnclick = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (showType!= SHOW_TYPE_ALL && showType!=SHOW_TYPE_HISTORY) {
				Toast toast = Toast.makeText(BookListView.this, "当前状态不支持删除模式", Toast.LENGTH_SHORT);
				toast.setDuration(1000);
				toast.show();
				return;
			}
			isEdit = !isEdit;
			bia.notifyDataSetChanged();
			if (isEdit) {
				btnEdit.setText(R.string.Complete);
				btnEdit.setBackgroundDrawable(BookListView.this.getResources()
						.getDrawable(R.drawable.onclickbutton));
			} else {
				btnEdit.setText(R.string.Edit);
				btnEdit.setBackgroundDrawable(BookListView.this.getResources()
						.getDrawable(R.drawable.outclickbutton));
			}
		}

	};
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			setContentView(R.layout.bookslist);
		} else {
			setContentView(R.layout.bookslist_h);
		}
		findview();
		doing();
	}
	
	//刷新Handler
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			doing();
			if(pd != null){
				pd.cancel();
			}
			butil.reflesh = true;
			butil.firstShellBooks();
			showview();
			super.handleMessage(msg);
		}
		
	};

	private int getReaderMode(){
		SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
		int readerMode = sp.getInt("readerMode", 1);
		BookShelfView.readerMode = readerMode;
		return readerMode;
	}
	
	/**搜索书本，加载列表；
	 * 
	 * @date 2011-4-26上午10:25:51
	 * @author Lee Chok Kong
	 */
	public void searchBooksDialog(){
		final LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.search_dialog_layout, null);
		DialogInterface.OnClickListener positiveOnclick = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					isEdit= false;
					btnEdit.setText(R.string.Edit);
					EditText etSearchBookName =(EditText)layout.findViewById(R.id.etSearchBookName);
					EditText etSearchAuthor =(EditText)layout.findViewById(R.id.etSearchAuthor);
					EditText etSearchCatalog =(EditText)layout.findViewById(R.id.etSearchCatalog);
					String keyBookname = etSearchBookName.getText().toString();
					String keyAuthor = etSearchAuthor.getText().toString();
					String keyCatalog = etSearchCatalog.getText().toString();
					if ((keyBookname != null && !keyBookname.equals(""))
							|| (keyAuthor != null && !keyAuthor.endsWith(""))
							|| (keyCatalog != null && !keyCatalog.equals(""))) {
						List<Book> result = butil.searchBooks(keyBookname,
								keyAuthor, keyCatalog);
						bia.setBooks(result);
						bia.notifyDataSetChanged();
						btnShowAllList
								.setBackgroundResource(R.drawable.outclickbutton_left);
						btnShowSearch
								.setBackgroundResource(R.drawable.onclickbutton_middle);
						btnShowHistory
								.setBackgroundResource(R.drawable.outclickbutton_right);
						showType = SHOW_TYPE_SEARCH;
					}
					dialog.cancel();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();
					break;
				}
				
			}
		};
		AlertDialog adSearch = new AlertDialog.Builder(this)
			.setTitle("搜索")
			.setIcon(android.R.drawable.ic_menu_search)
			.setView(layout)
			.setPositiveButton("搜索", positiveOnclick)
			.setNegativeButton("取消", positiveOnclick)
			.create();
		adSearch.show();
	}
	
	public class updateHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 3){
				synchronized (bia) {
					bia.notifyDataSetChanged();
				}
				doing();
				butil.reflesh = true;
				butil.firstShellBooks();
				showview();
			}
			super.handleMessage(msg);
		}
		
	}
}
