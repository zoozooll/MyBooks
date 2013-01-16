package com.ccbooks.listener.bookCatalog;

import com.ccbooks.bo.BooksBo;
import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.chinachip.books.plugin.Plugin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class LvCatalogOnItemClickListener implements OnItemClickListener {

	private BookCatalogView bcv;
	
	public LvCatalogOnItemClickListener(BookCatalogView bcv){
		this.bcv = bcv;
	}
	
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
		// TODO Auto-generated method stub
		System.out.println("setResult");
		int file = bcv.items.get(position).file;
		Plugin plugin = BookContentView.bc.pm.getDefaultPlugin();
		int filehandle = BookContentView.bc.pm.filehandle;
		BookContentView.bc.pm.close(plugin, filehandle);
		filehandle = BookContentView.bc.pm.open(plugin, file);
		BookContentView.bc.setFilehandle(filehandle);
		BookContentView.bc.resetFile();
		
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		
		bundle.putInt("file", file);
		intent.putExtras(bundle);
		bcv.setResult(bcv.items.get(position).pageIndex,intent);
		bcv.finish();
	}

}
