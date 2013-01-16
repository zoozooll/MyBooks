package com.ccbooks.listener;



import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.BookStoreView;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;


public class BtnBookStoreClick implements OnClickListener{

	
	private Activity context;
	
	

	public BtnBookStoreClick(Activity context) {
		super();
		this.context = context;
	}

	// 返回
	@Override
	public void onClick(View v) {
		if (context instanceof BookContentView){
			Intent intent = new Intent();
			intent.setClass(context, BookShelfView.class);
			context.startActivity(intent);
			context.finish();
		}else if (context instanceof BookCatalogView){
			context.setResult(-1);
			context.finish();
		}else if (context instanceof BookStoreView){
			Intent intent = new Intent();
			intent.setClass(context, BookShelfView.class);
			context.startActivity(intent);
			context.finish();
		}
	}

	
	
	
}
