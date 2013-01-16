package com.ccbooks.fullscreen.listener;


import java.util.ArrayList;

import com.ccbooks.adapter.DirectoryAdapter;
import com.ccbooks.view.BookCatalogView;
import com.chinachip.TextReader.MenuCatalog;
import com.chinachip.tree.Node;



import android.view.View;
import android.view.View.OnClickListener;


public class MenuBTBackClick implements OnClickListener{
	
	private MenuCatalog bcv;
	private DirectoryAdapter directoryAdapter; //亮度调节悬浮框；
	

	public MenuBTBackClick(MenuCatalog bookCatalogView,DirectoryAdapter directoryAdapter) {
		super();
		this.bcv = bookCatalogView;
		this.directoryAdapter = directoryAdapter;
		
	}




		@Override
		public void onClick(View v) {
			
			Node tempNode = bcv.nodeList.get(0);
			
			ArrayList<Node> nodeList = bcv.myTree.getParentNodeList(tempNode);
			if(nodeList.size() != 0){
				bcv.nodeList = nodeList;
				directoryAdapter.setNodeList(nodeList);
			}
			if(bcv.myTree.isRoot(bcv.nodeList.get(0)))
			{
				bcv.fs_btBack.setVisibility(View.GONE);
			}else{
				bcv.fs_btBack.setVisibility(View.VISIBLE);
			}
			directoryAdapter.notifyDataSetChanged();
			
		}



	
	

	
}
