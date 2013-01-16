package com.ccbooks.fullscreen.content;

import java.util.ArrayList;

import com.chinachip.ccbooks.engine.LineIndex;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class BitmapMgr {
	int ws = 800;
	int hs = 480;
	public  Bitmap mbmpCurr = null;
	public boolean isDraw = false;
	public ArrayList<LineIndex> lineIndexs = null;
	public ArrayList<String> stringArray = null;
	public BitmapMgr(int ws,int hs){
		mbmpCurr = Bitmap.createBitmap(ws,hs, Config.ARGB_8888);
	}
}
