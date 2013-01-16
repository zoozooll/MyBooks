package com.ccbooks.view;

import java.util.ArrayList;
import java.util.List;

import com.ccbooks.adapter.CatelogAdapter;
import com.ccbooks.adapter.DirectoryAdapter;
import com.ccbooks.bo.BookMarkBo;
import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.FileBo;
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
import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginUmd;
import com.chinachip.books.plugin.PluginUtil;
import com.chinachip.ccbooks.core.BookCore;
import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.SQLException;
import android.inputmethodservice.Keyboard.Key;
import android.net.Uri;
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
public class TransitView extends Activity {
	
	// 所有控件；
	

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

	public int id = 0;
	public PluginMgr pm;
	
	public Plugin plugin;

	public BooksBo bo;
	
	public int filehandle;
	
	public static final byte[] CHINACHIP= {
		(byte)0x20,(byte)0x22,(byte)0x16,(byte)0x28,(byte)0x22,(byte)0x30,(byte)0x9b,(byte)0x88,(byte)0x67,(byte)0x25,(byte)0x5b,(byte)0x55,(byte)0x66,(byte)0x32,(byte)0xd3,(byte)0x1d
		};//chinachip
	
	 public static final byte[] CHINACHIP1 = {(byte)0x63,(byte)0xb5,(byte)0x7d,(byte)0x3a,(byte)0x5c,(byte)0x98,(byte)0x92,(byte)0x1e,(byte)0x82,(byte)0x06,(byte)0xaf,(byte)0x62,(byte)0xc3,(byte)0x6d,(byte)0xf3,(byte)0x68};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Uri uri = getIntent().getData();
		System.out.println("asdfasdf");
		String path = null;
        if (uri != null){
        	path = uri.getPath();
        }
        String tempStr = path.toLowerCase();
        if(tempStr.lastIndexOf(".xml") > 0){
        	finish();
        	return;
        }
        String temp = "chinachip";
     
        String temp1 = getRealData(CHINACHIP);
        String temp2 = getRealData(CHINACHIP1);
        if((!temp.equals(temp1))&&(!temp.equals(temp2))){
        	throw new  NullPointerException("找不到文件");
        }
		FileBo fileBo = new FileBo(this);
		book = fileBo.getOneByPath(path);
		if(book == null){
			fileBo.InsertBook(path);
		}
		book = fileBo.getOneByPath(path);
		if(BooksBo.books != null){
			BooksBo.books.add(book);
		}
		
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		int fileID = book.id;
		bundle.putInt("bookId", fileID);
		
		intent.putExtras(bundle);
	
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
			intent.setClass(this, BookContentView.class);
		}else if(getReaderMode() == 2)
		{
			intent.setClass(this, TextReader.class);
		}
		startActivity(intent);
		finish();
	}
	private int getReaderMode(){
		SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
		int readerMode = sp.getInt("readerMode", 1);
		BookShelfView.readerMode = readerMode;
		return readerMode;
	}
	public synchronized static String getRealData(byte[] src ) {
		PluginUmd umdd = new PluginUmd();
		byte[] result=umdd.Decrypt(src);
		
		byte[] lastResult = delZero(result);
		String a=new String(lastResult);
		
		return a;
		//return BASE_MAG_URI;
	}
	 /**
	   * 把多余的0去掉
	   * @param src 要处理的数组
	   * @return
	   */
	  private static byte[] delZero(byte[] src){
		  int lastoffest = 0;
		  for(int i = 0;i<src.length;i++){
				
				if(src[i] != 0 ){
					lastoffest++;
				}else{
					break;
				}
			}
			byte[] lastResult = new byte[lastoffest];
			for(int i=0;i<lastResult.length;i++){
				lastResult[i] = src[i];
			}
			return lastResult;
	  }

}
