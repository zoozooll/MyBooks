package com.ccbooks.fullscreen.listener;


import java.util.ArrayList;

import com.ccbooks.adapter.DirectoryAdapter;
import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.TextReader;
import com.chinachip.TextReader.MenuCatalog;
import com.chinachip.TextReader.TextMenu;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.tree.Node;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Toast;

import android.widget.AdapterView.OnItemClickListener;

public class MenuDirectoryClick implements OnItemClickListener{
	
	private MenuCatalog bcv;
	private DirectoryAdapter directoryAdapter; //亮度调节悬浮框；
	private TextMenu menuParent;
	private TextReader tr;

	public MenuDirectoryClick(TextMenu menuParent,MenuCatalog mCatalog,DirectoryAdapter directoryAdapter) {
		super();
		this.bcv = mCatalog;
		this.directoryAdapter = directoryAdapter;
		this.menuParent = menuParent;
		this.tr = this.menuParent.getActParent();
	}

	
	
	// 点击背景按钮�?
	
	

		
	        
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			directoryAdapter.selected = (int) arg3;
			if(bcv.nodeList.size() == 1)
			{	
				Node tempNode = bcv.nodeList.get(directoryAdapter.selected);
				ArrayList<Node> nodeList = bcv.myTree.getChildNodeList(tempNode);
				if(nodeList.size() != 0){
					bcv.nodeList = nodeList;
					directoryAdapter.setNodeList(nodeList);
				}else{
					
					int file = bcv.nodeList.get(0).file;
					
					TextReader.bc.pm.close(TextReader.bc.pm.getDefaultPlugin(),TextReader.bc.pm.filehandle);
					Plugin plugin =TextReader.bc.pm.getDefaultPlugin();
					int filehandle = tr.bc.pm.open(plugin, file);
					long fileLength = TextReader.bc.pm.length(plugin, filehandle);
					if(fileLength == 0){
						
						Toast.makeText(tr, "此章节内容为空!", Toast.LENGTH_SHORT).show();
						return;
					}
					tr.bc.setFilehandle(filehandle);
					tr.bc.resetFile();
					tr.file = file;
					tr.setCurPostion(0);
					tr.reParseText();
					bcv.adCatalog.dismiss();
					
					bcv.menuParent.turntoBookRequest();
				}
				
			}else{
				Node tempNode = bcv.nodeList.get(directoryAdapter.selected);
				
				
				ArrayList<Node> nodeList = bcv.myTree.getChildNodeList(tempNode);
				if(nodeList.size() != 0){
					bcv.nodeList = nodeList;
					directoryAdapter.setNodeList(nodeList);
				}else{
					
					int file = bcv.nodeList.get((int)arg3).file;
					
					TextReader.bc.pm.close(TextReader.bc.pm.getDefaultPlugin(),TextReader.bc.pm.filehandle);
					Plugin plugin =TextReader.bc.pm.getDefaultPlugin();
					int filehandle = tr.bc.pm.open(plugin, file);
					long fileLength = TextReader.bc.pm.length(plugin, filehandle);
					if(fileLength == 0){
						
						Toast.makeText(tr, "此章节内容为空!", Toast.LENGTH_SHORT).show();
						return;
					}
					tr.bc.setFilehandle(filehandle);
					tr.bc.resetFile();
					tr.file = file;
					tr.setCurPostion(0);
					tr.reParseText();
					bcv.adCatalog.dismiss();
					
					bcv.menuParent.turntoBookRequest();
				}
			}
			if(bcv.myTree.isRoot(bcv.nodeList.get(0)))
			{
				bcv.fs_btBack.setVisibility(View.GONE);
			}else{
				bcv.fs_btBack.setVisibility(View.VISIBLE);
			}
			directoryAdapter.notifyDataSetChanged();
			
			//list.setSelectionAfterHeaderView();
			
		}



	
	
	

	
}
