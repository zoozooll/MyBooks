package com.ccbooks.listener;

import com.ccbooks.view.BookListView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.BookStoreView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class BtnOnlineBookStoreClick implements OnClickListener{

	private Activity context;
	private boolean netStatus=false;
	
	public BtnOnlineBookStoreClick(Activity context){
		super();
		this.context=context;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(context instanceof BookShelfView || context instanceof BookListView){
			ConnectivityManager cManagger = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			cManagger.getActiveNetworkInfo();
			if(cManagger.getActiveNetworkInfo()!=null){
				netStatus = cManagger.getActiveNetworkInfo().isAvailable();
			}
			if(netStatus){
				Intent intent = new Intent();
				intent.setClass(context, BookStoreView.class);
				context.startActivity(intent);
				context.finish();
			}
			else{
				android.content.DialogInterface.OnClickListener diol =
					new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch (which) {
							case AlertDialog.BUTTON_POSITIVE:
							{
								Intent mIntent = new Intent("/");
								ComponentName comp = new ComponentName(
										"com.android.settings",
										"com.android.settings.WirelessSettings");
								mIntent.setComponent(comp);
								mIntent.setAction("android.intent.action.VIEW");
								context.startActivity(mIntent);
								dialog.cancel();
							}
								break;
							case AlertDialog.BUTTON_NEGATIVE:
								dialog.cancel();
								break;
							default:
								break;
							}
						}
					
				};
				
				AlertDialog adg = new AlertDialog.Builder(context).
				setTitle("网络没有连接,是否连接网络?")
					.setPositiveButton("确定", diol)
					.setNegativeButton("取消", diol)
					.create();
				adg.show();
				//Toast.makeText(context, "请先打开网络", Toast.LENGTH_SHORT).show();
			}
				
		}	
	}

}
