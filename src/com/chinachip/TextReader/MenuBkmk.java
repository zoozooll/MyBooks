package com.chinachip.TextReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccbooks.adapter.CatelogAdapter;
import com.ccbooks.adapter.CharactorSetAdapter;
import com.ccbooks.adapter.DirectoryAdapter;
import com.ccbooks.bookCatalog.listener.BTBackClick;
import com.ccbooks.bookCatalog.listener.DirectoryClick;
import com.ccbooks.dialog.FSCharDialog;
import com.ccbooks.fullscreen.listener.MenuBkmkBottunAddClick;
import com.ccbooks.fullscreen.listener.MenuBkmkBottunListClick;
import com.ccbooks.fullscreen.listener.MenuBkmkListItemClick;

import com.ccbooks.fullscreen.listener.MenuClick;

import com.ccbooks.util.StringConfig;
import com.ccbooks.view.R;
import com.ccbooks.vo.CatelogItem;
import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MenuBkmk  extends MenuFun{
	private TextMenu menuParent;
	private LinearLayout layoutFun;
	private Button btnAdd;
	private Button btnList;

	FSCharDialog adBkmk;

	private List<CatelogItem> bkmkItems;
	private BookMarkManager bkmkManager;

	private final String[] listKeys = 
		{"bkmk_list_front_icon", "bkmk_list_tips", "bkmk_list_percent", "bkmk_list_date", "bkmk_list_del_icon"};
	private final int[] listValues = 
		{R.id.bkmk_list_front_icon, R.id.bkmk_list_tips, R.id.bkmk_list_percent, R.id.bkmk_list_date, R.id.bkmk_list_del_icon};
	
	public MenuBkmk(TextMenu tmObj){
		menuParent = tmObj;
		layoutFun = (LinearLayout)menuParent.getLayoutMenu().findViewById(R.id.fun_bkmk);
		btnAdd = (Button)menuParent.getLayoutMenu().findViewById(R.id.fun_bkmk_add);
		MenuBkmkBottunAddClick btnAddListener = new MenuBkmkBottunAddClick(this);
		btnAdd.setOnClickListener(btnAddListener);
	
		btnList = (Button)menuParent.getLayoutMenu().findViewById(R.id.fun_bkmk_list);
		MenuBkmkBottunListClick btnListListener = new MenuBkmkBottunListClick(this);
		btnList.setOnClickListener(btnListListener);	
		bkmkManager = menuParent.getActParent().getBkmkManager();
    	MenuClick menuclick = new MenuClick();
    	layoutFun.setOnClickListener(menuclick);
	}
	
	public void bkmkAddEvent(){
		Toast successToast;
		if(menuParent.getActParent().addBkmk() == true)
			successToast = Toast.makeText(menuParent.getActParent(), "添加成功!", Toast.LENGTH_SHORT);
		else
			successToast = Toast.makeText(menuParent.getActParent(), "添加失败!", Toast.LENGTH_SHORT);			
		successToast.show();
	}

	public void bkmkListEvent(){
		turnToBkmkList();
	}

	public void bkmkListItemClickEvent(long id){
		menuParent.getActParent().TurntoPageOfBkmk(bkmkManager.getBkmk((int)id));
		adBkmk.dismiss();
		hide();
		menuParent.turntoBookRequest();
	}
	
	public LinearLayout getLayoutFont() {
		return layoutFun;
	}
	
	public void setlayoutFun(LinearLayout layoutFun) {
		this.layoutFun = layoutFun;
	}
	
	public boolean isShowed(){
		if(View.VISIBLE == layoutFun.getVisibility())
			return true;
		else 
			return false;
	}
	
	public boolean show(){
		if(View.VISIBLE == layoutFun.getVisibility())return false;
		layoutFun.setVisibility(View.VISIBLE);
		return true;
	}
	
	public boolean hide(){
		if(View.GONE == layoutFun.getVisibility())return false;
		layoutFun.setVisibility(View.GONE);
		return true;
	}	
	
	public boolean turnToBkmkList(){
		if(bkmkManager.getBkmkNum() <= 0)
		{
			Toast successToast = Toast.makeText(menuParent.getActParent(), "未添加书签!", Toast.LENGTH_SHORT);
			successToast.show();
			return false;
		}	
		
		LinearLayout bkmkListLayout = (LinearLayout)menuParent.getActParent().getLayoutInflater().inflate(R.layout.fs_bkmk_list, null);
		ListView list = (ListView)bkmkListLayout.findViewById(R.id.bkmk_list);
		
		
		//create a list data
		/*
		List<Map<String,Object>> bkmkLists = new ArrayList<Map<String,Object>>();
		for(int i=0;i<bkmkManager.getBkmkNum();i++){
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("bkmk_list_front_icon", R.drawable.icon);
			item.put("bkmk_list_tips", bkmkManager.getBkmk(i).title);
			item.put("bkmk_list_percent", 
								"[" + String.valueOf(bkmkManager.getBkmk(i).percent) + "]");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			item.put("bkmk_list_date", sdf.format(bkmkManager.getBkmk(i).date*1000));
			item.put("bkmk_list_del_icon", R.drawable.icon);
			bkmkLists.add(item);
		}
		
		SimpleAdapter sa = new SimpleAdapter(menuParent.getActParent(), bkmkLists, R.layout.fs_bkmk_list_item, 
				listKeys, listValues);
		list.setAdapter(sa);
		MenuBkmkListItemClick listItemClickListener = new MenuBkmkListItemClick(this);
		list.setOnItemClickListener(listItemClickListener);
		*/
		CatelogAdapter ca = new CatelogAdapter(menuParent.getActParent(), bkmkManager.getBkmkList());
		list.setAdapter(ca);
		
		MenuBkmkListItemClick listItemClickListener = new MenuBkmkListItemClick(this);
		list.setOnItemClickListener(listItemClickListener);
		TextView emptyView = (TextView) bkmkListLayout.findViewById(android.R.id.empty);
		emptyView.setText("当前书没有书签");
		list.setEmptyView(emptyView);
		//show the list
		adBkmk = new FSCharDialog(menuParent.getActParent(),bkmkListLayout);
		adBkmk.show();
		
	
		return true;
	}
}
