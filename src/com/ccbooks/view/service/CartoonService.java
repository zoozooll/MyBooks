package com.ccbooks.view.service;

import com.ccbooks.util.CcbooksAction;
import com.ccbooks.view.R;
import com.ccbooks.view.widget.CartoonWidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class CartoonService extends Service {
	//卡通人物头像
	int[] faceAsks = { R.drawable.widget_face0, R.drawable.widget_face1,
			R.drawable.widget_face2, R.drawable.widget_face3, R.drawable.widget_face4, R.drawable.widget_face5};	//小丑脸图片rid索引;
	//对话图
	int[] popImgs = {R.drawable.pop0, R.drawable.pop1, R.drawable.pop2, R.drawable.pop3, R.drawable.pop4, R.drawable.pop5 };
	//按钮
	int[] widgetBtnImgs = {R.drawable.widget_btn0, R.drawable.widget_btn1, R.drawable.widget_btn2, R.drawable.widget_btn3, R.drawable.widget_btn4, R.drawable.widget_btn5};
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i("zkli", "start:"+startId);
		Log.i("zkli", "action:"+intent.getAction());
		if (intent.getAction().equals(CcbooksAction.CARTOON_CHANGE_IMAGE)) {
			int faceIndex = startId % faceAsks.length;
			RemoteViews  remoteViews = new RemoteViews(getPackageName(),R.layout.cartoon_widget_layout);
			remoteViews.setImageViewResource(R.id.ivFace, faceAsks[faceIndex]);
			remoteViews.setImageViewResource(R.id.ibWelcomeWord, popImgs[faceIndex]);
			remoteViews.setImageViewResource(R.id.ibEnterBookList, widgetBtnImgs[faceIndex]);
			remoteViews.setImageViewResource(R.id.ibEnterLastRead, widgetBtnImgs[faceIndex]);
			remoteViews.setImageViewResource(R.id.ibEnterHistroy, widgetBtnImgs[faceIndex]);
			AppWidgetManager.getInstance(this).updateAppWidget(
					new ComponentName(this, CartoonWidget.class),
					remoteViews);
		}
	}
	
/*	@Override
	public void onCreate() {
		Log.i("zkli", "Create");
		super.onCreate();
	}*/


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
