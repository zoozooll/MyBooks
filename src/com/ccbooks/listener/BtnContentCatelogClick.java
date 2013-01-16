package com.ccbooks.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ccbooks.adapter.CatelogAdapter;
import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.vo.CatelogItem;
import com.chinachip.tree.Node;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class BtnContentCatelogClick implements OnClickListener {

	private BookCatalogView bcv;
	// 所有控件；
	// private LinearLayout llyContentMain;

	private Button btnContentCatelog;
	private Button btnContentMark;
	private ListView lvCatalog;

	// 点击目录按钮

	public BtnContentCatelogClick(BookCatalogView bcv,
			Button btnContentCatelog, Button btnContentMark,
			ListView lvCatalog) {
		super();
		this.bcv = bcv;
		this.btnContentCatelog = btnContentCatelog;
		this.btnContentMark = btnContentMark;
		this.lvCatalog = lvCatalog;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (bcv.showCatelogType != 1) {
			bcv.showCatelogType = 1;
			bcv.showing();
			btnContentMark.setBackgroundDrawable(bcv.getResources()
					.getDrawable(R.drawable.bookcatalog_right_unselected));
			btnContentCatelog.setBackgroundDrawable(bcv.getResources()
					.getDrawable(R.drawable.bookcatalog_left_selected));
			int selected = BookContentView.file;
			selected = sreachNode(bcv.nodeList, selected);

			
			bcv.lvCatalog.setSelectionFromTop(selected , 20);

			if(bcv.myTree.isRoot(bcv.nodeList.get(0)))
			{
				bcv.btBack.setVisibility(View.INVISIBLE);
			}else{
				bcv.btBack.setVisibility(View.VISIBLE);
			}
			
		}
	}
	public static Node sreachNode1(ArrayList<Node> nodeList,int file)
	{
		Iterator<Node> it = nodeList.iterator();
		while(it.hasNext()){
			Node node = it.next();
			if(file == node.file)
			{
				return node;
			}
		}
		return nodeList.get(0);
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
