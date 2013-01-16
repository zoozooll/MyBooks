package com.ccbooks.view.widget;

import com.ccbooks.util.CcbooksAction;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.BookListView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.R;
import com.ccbooks.view.service.CartoonService;

import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class CartoonWidget extends AppWidgetProvider {

	private static RemoteViews remoteViews;
	public CartoonWidget() {
		super();
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.i("zkli", "onEnabled");
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("zkli", "onReceive,Action:" + intent.getAction());
		super.onReceive(context, intent);
		intent = new Intent(context,CartoonService.class);
		intent.setAction(CcbooksAction.CARTOON_CHANGE_IMAGE);
		//PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		//PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		//设定人物图片；
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.cartoon_widget_layout);
		remoteViews.setOnClickPendingIntent(R.id.ivFace, pendingIntent);
		
		//设定进入书库按钮
		intent.setComponent(new ComponentName(context, BookShelfView.class));
		pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.ibEnterBookList, pendingIntent);
		//设定历史浏览列表按钮
		intent.setComponent(new ComponentName(context, BookListView.class));
		intent.putExtra("bookListShowType", (short)2);
		intent.setAction(String.valueOf(System.currentTimeMillis()));
		pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.ibEnterHistroy, pendingIntent);
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(new ComponentName(context, this.getClass()), remoteViews);
		if (intent.getAction().equals(CcbooksAction.CARTOON_CHANGE_IMAGE)) {
		}
	}

	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		/*Log.i("zkli", "onUpdate");
		Intent intent = new Intent(context,CartoonService.class);
		intent.setAction(CcbooksAction.CARTOON_CHANGE_IMAGE);
		//PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		//PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		//设定人物图片；
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.cartoon_widget_layout);
		remoteViews.setOnClickPendingIntent(R.id.ivFace, pendingIntent);
		
		//设定进入书库按钮
		intent.setComponent(new ComponentName(context, BookShelfView.class));
		pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.ibEnterBookList, pendingIntent);
		//设定历史浏览列表按钮
		intent.setComponent(new ComponentName(context, BookListView.class));
		intent.putExtra("bookListShowType", (short)2);
		intent.setAction(String.valueOf(System.currentTimeMillis()));
		pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.ibEnterHistroy, pendingIntent);
		
		appWidgetManager.updateAppWidget(new ComponentName(context, this.getClass()), remoteViews);*/
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	public static class WidgetConfig{
		public void getConfig(){
			
		}
	}
}
