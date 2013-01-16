package com.ccbooks.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;
import com.ccbooks.vo.Book;
import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import com.chinachip.books.plugin.PluginUtil;
import com.chinachip.tree.Node;


public class DirectoryAdapter extends BaseAdapter {
	public int count;
	public int selected;
	private Activity activity;
	private ArrayList<Node> nodeList;
	private Book book;
	int readerMode;
	public void setNodeList(ArrayList<Node> nodeList) {
		this.nodeList = nodeList;
	}
	//public List<Book> books;
	private LayoutInflater inflater;
	
	public DirectoryAdapter(Activity activity ,ArrayList<Node> nodeList ,int readerMode){
		this.activity = activity;
		//this.books = books;
		this.nodeList = nodeList;
		this.inflater = LayoutInflater.from(activity);
		this.readerMode = readerMode;
	}

	@Override
	public int getCount() {
		return nodeList.size();
	}

	@Override
	public Object getItem(int position) {
		return nodeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(nodeList.size() != 0){
			return position;
		}else{
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = (LinearLayout) inflater.inflate(R.layout.root,
				null);
			holder.TVTitle = (TextView) convertView.findViewById(R.id.TVTitle);
			
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		Param param = new Param();
		PluginMgr pm = null;
		if(readerMode == 1){
			pm = BookContentView.bc.pm;
			book = BookContentView.book;
			holder.TVTitle.setTextColor(Color.BLACK);
		}else if(readerMode == 2){
			pm = TextReader.bc.pm;
			book = TextReader.book;
			holder.TVTitle.setTextColor(R.drawable.dialog_list_textcolor);
		}
		String enc = null;
		

		Plugin plugin = pm.getDefaultPlugin();
		int end = pm.get(plugin, PluginUtil.BOOK_BACKLIST_CHARSET, param);
		
		if(end > 0)
		{
			enc= param.getStr();
			if(!PluginUtil.isUnicode(enc)){
				enc = "GBK";
				
			}
		}else{
			enc = "GBK";
		}
		if(enc == null){
			enc = "GBK";
		}
	
		try {
				holder.TVTitle.setText(nodeList.get(position).getTitle(enc));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(selected == position){}
		return convertView;
	}


	public void setSelected(int selected) {
		this.selected = selected;
	}
	class ViewHolder{
		TextView TVTitle;
		
	}
	private int getReaderMode(){
		int readerMode = 0;
		SharedPreferences sp = activity.getSharedPreferences("config", Activity.MODE_PRIVATE);
		readerMode = sp.getInt("readerMode", 1);
		return readerMode;
	}
}
