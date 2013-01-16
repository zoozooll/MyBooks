package com.chinachip.TextReader;

import java.util.ArrayList;

import com.ccbooks.adapter.DirectoryAdapter;

import com.ccbooks.dialog.FSCharDialog;
import com.ccbooks.fullscreen.listener.MenuBTBackClick;


import com.ccbooks.fullscreen.listener.MenuDirectoryClick;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.tree.BackList;
import com.chinachip.tree.Node;
import com.chinachip.tree.SuperNode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MenuCatalog  extends MenuFun{
	public TextMenu menuParent;
	private LinearLayout layoutFun;

	private Button fun_catalog_list;
	public FSCharDialog adCatalog;

	public BackList myTree;
	public ListView fs_lvCatalog;
	public Button fs_btBack;
	public ArrayList<Node> nodeList;
    public DirectoryAdapter directoryAdapter;
	public MenuCatalog(TextMenu tmObj){
		menuParent = tmObj;
	}
	

	public void bkmkListEvent(){
		turnToBkmkList();
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
		LinearLayout bkmkListLayout = (LinearLayout)menuParent.getActParent().getLayoutInflater().inflate(R.layout.fs_catalog_list, null);
		fs_lvCatalog = (ListView)bkmkListLayout.findViewById(R.id.fs_lvCatalog);
		fs_btBack = (Button)bkmkListLayout.findViewById(R.id.fs_btBack);

		PluginMgr pm = TextReader.bc.pm;
		int file = TextReader.file;
		Node root = (Node)pm.bltree.getRoot();
		Node temp = null;
		if(root.file == file){
			temp = root;
		}else{
			temp = findNode(file, root);
		}
			
		
		myTree = pm.bltree;
		nodeList = myTree.getCurrNodeList(temp);
		int selected = TextReader.file;
		selected = sreachNode(nodeList, selected);

		
		

		if(myTree.isRoot(nodeList.get(0)))
		{
			fs_btBack.setVisibility(View.INVISIBLE);
		}else{
			fs_btBack.setVisibility(View.VISIBLE);
		}
	
		directoryAdapter = new DirectoryAdapter(menuParent.getActParent(), nodeList,2);
		MenuDirectoryClick menuDirectory = new MenuDirectoryClick(menuParent,this,directoryAdapter);
		
		ListView listview = (ListView) bkmkListLayout.findViewById(R.id.fs_lvCatalog);
		listview.setAdapter(directoryAdapter);
		listview.setOnItemClickListener(menuDirectory);
		fs_btBack = (Button)bkmkListLayout.findViewById(R.id.fs_btBack);
		MenuBTBackClick btBackClick = new MenuBTBackClick(this,directoryAdapter);
		fs_btBack.setOnClickListener(btBackClick);
		
		
	
		adCatalog = new FSCharDialog(menuParent.getActParent(),bkmkListLayout);
		adCatalog.show();
		
		fs_lvCatalog.setSelectionFromTop(selected , 20);
		
		return true;
	}


	@Override
	public LinearLayout getLayoutFont() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean isEquals(int file,int file1){
		if(file == file1){
			System.out.println("----------equals-----------------");
			return true;
		}
		return false;
	}
	public Node findNode(int file,SuperNode node) {
		Node root = (Node)node;
		Node curr = root;
		
		do {
			Node next = (Node) curr.next;
			System.out.println("--------next----------"+next.file);
			int temp = next.file;
			if(isEquals(file, temp)){
				System.out.println("--------next--equals--------"+next.file);
				return next;
			}
			Node currChild = (Node)curr.child;
			
			if (currChild != null){
				System.out.println("---------currChild---------"+currChild.file);
				int temp2 = currChild.file;
				if(isEquals(file, temp2)){
					System.out.println("--------currChild--equals--------"+next.file);
					return currChild;
				}
				Node tt = findNode(file,currChild);
				if(isEquals(file,tt.file)){
					System.out.println("----------------return----tt.file------------"+tt.file);
					return tt;
				}
					
				
			}
				
			curr = next;
			

		} while (curr != root);
		return curr;
	}
	public static int sreachNode(ArrayList<Node> nodeList,int file)
	{
		for(int i = 0; i< nodeList.size(); i++){
			if(nodeList.get(i).file == file){
				return i;
			}
		}
		return 0;
	}
}
