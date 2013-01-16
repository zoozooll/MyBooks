package com.chinachip.book.cartoon;

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
import com.ccbooks.fullscreen.listener.MenuClick;

import com.ccbooks.util.StringConfig;
import com.ccbooks.view.CartoonReader;
import com.ccbooks.view.R;
import com.ccbooks.vo.CatelogItem;
import com.chinachip.book.cartoon.listener.CTMenuBkmkBottunAddClick;
import com.chinachip.book.cartoon.listener.CTMenuBkmkBottunListClick;
import com.chinachip.book.cartoon.listener.CTMenuBkmkListItemClick;
import com.chinachip.book.cartoon.listener.CTMenuClick;
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

public class CTMenuBkmk {
	private CartoonReader menuParent;
	public LinearLayout layoutFun;
	private Button btnAdd;
	private Button btnList;

	FSCharDialog adBkmk;

	private List<CatelogItem> bkmkItems;
	private CTBookMarkManager bkmkManager;
	
	private final String[] listKeys = 
		{"bkmk_list_front_icon", "bkmk_list_tips", "bkmk_list_percent", "bkmk_list_date", "bkmk_list_del_icon"};
	private final int[] listValues = 
		{R.id.bkmk_list_front_icon, R.id.bkmk_list_tips, R.id.bkmk_list_percent, R.id.bkmk_list_date, R.id.bkmk_list_del_icon};
	
	public CTMenuBkmk(CartoonReader tmObj){
		menuParent = tmObj;
		layoutFun = menuParent.layoutFun;
		
		btnAdd = (Button)layoutFun.findViewById(R.id.ct_bkmk_add);
		CTMenuBkmkBottunAddClick btnAddListener = new CTMenuBkmkBottunAddClick(this);
		btnAdd.setOnClickListener(btnAddListener);
	
		btnList = (Button)layoutFun.findViewById(R.id.ct_bkmk_list);
		CTMenuBkmkBottunListClick btnListListener = new CTMenuBkmkBottunListClick(this);
		btnList.setOnClickListener(btnListListener);	
		bkmkManager = menuParent.bkmkManager;
		CTMenuClick menuclick = new CTMenuClick();
    	layoutFun.setOnClickListener(menuclick);
	}
	
	public void bkmkAddEvent(){
		Toast successToast;
		if(menuParent.addBkmk() == true)
			successToast = Toast.makeText(menuParent, "添加成功!", Toast.LENGTH_SHORT);
		else
			successToast = Toast.makeText(menuParent, "添加失败!", Toast.LENGTH_SHORT);			
		successToast.show();
	}

	public void bkmkListEvent(){
		turnToBkmkList();
	}

	public void bkmkListItemClickEvent(long id){
		menuParent.TurntoPageOfBkmk(bkmkManager.getBkmk((int)id));
		adBkmk.dismiss();
		menuParent.closeOptionsMenu();
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
			Toast successToast = Toast.makeText(menuParent, "未添加书签!", Toast.LENGTH_SHORT);
			successToast.show();
			return false;
		}	
		
		LinearLayout bkmkListLayout = (LinearLayout)menuParent.getLayoutInflater().inflate(R.layout.fs_bkmk_list, null);
		ListView list = (ListView)bkmkListLayout.findViewById(R.id.bkmk_list);
		
		
	
		CatelogAdapter ca = new CatelogAdapter(menuParent, bkmkManager.getBkmkList());
		list.setAdapter(ca);
		
		CTMenuBkmkListItemClick listItemClickListener = new CTMenuBkmkListItemClick(this);
		list.setOnItemClickListener(listItemClickListener);
		TextView emptyView = (TextView) bkmkListLayout.findViewById(android.R.id.empty);
		emptyView.setText("当前书没有书签");
		list.setEmptyView(emptyView);
		//show the list
		adBkmk = new FSCharDialog(menuParent,bkmkListLayout);
		adBkmk.show();
		return true;
	}
}
