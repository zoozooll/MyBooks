package com.ccbooks.handles;

import java.util.List;

import com.ccbooks.bo.BookContentThread;
import com.ccbooks.bo.BookMarkBo;
import com.ccbooks.view.BookContentView;
import com.chinachip.books.plugin.Plugin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BookContentShowHandler extends Handler {
	
	private BookContentView bcv;
	
	public BookContentShowHandler(BookContentView bcv) {
		super();
		this.bcv = bcv;
	}


	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		Bundle b = msg.getData();
		switch (b.getInt("what")) {
		case BookContentThread.FIRST_VIRI:
			if( bcv.portraint==1 ){
				List<String> str = bcv.bct.str;
				
				//写入content;
				bcv.content = str;
				
				List<String> strNext = bcv.bct.strNext;
				Log.i("zkli", "markPoint"+bcv.markPoint);
				Log.i("zkli", "preventPoint"+bcv.preventPoint);
				Log.i("zkli", "nextPoint"+bcv.nextPoint);
				bcv.turnPage(str, str,strNext,strNext);
				bcv.isMove = false;
				//判断该页是否有书签
				BookMarkBo.isMark(bcv);
				//判断最后一页
				//bcv.lastPageShowing();
			}
			break;
		case BookContentThread.FIRST_HORI:
			if(bcv.portraint == 2) {
				List<String> str = bcv.bct.str;
				List<String> strNext = bcv.bct.strNext;
				List<String>  str1 =null;
				List<String>  str2 =null;
				List<String>  str3 =null;
				List<String>  str4 =null ;
				
				//写入content;
				bcv.content = str;
				if (str!=null){
					if (str.size()>=bcv.bc.getLineCount()/2){
						str1 = str.subList(0, bcv.bc.getLineCount()/2);
						str2 = str.subList(bcv. bc.getLineCount()/2,str.size());	
					} else {
						str1 = str;
						str2 = null;
					}
					
				}
				if (strNext!=null){
					if (strNext.size()>=bcv.bc.getLineCount()/2){
						str3 = strNext.subList(0, strNext.size()/2);
						str4 = strNext.subList( bcv.bc.getLineCount()/2,strNext.size());
					} else {
						str3 = strNext;
						str4 = null;
					}
					
				}
				Log.i("zkli", "markPoint"+bcv.markPoint);
				Log.i("zkli", "preventPoint"+bcv.preventPoint);
				Log.i("zkli", "nextPoint"+bcv.nextPoint);
				bcv.turnPage(str1, str2,str3,str4);
	
				//判断该页是否有书签
				BookMarkBo.isMark(bcv);
				//判断最后一页
				//bcv.lastPageShowing();
			}
			
			break;
		case 3 :
			break;
		case 4 :
			break;
		case 5 :
			if(bcv.dg!=null){
				bcv.dg.dismiss();
			}
			break;
		case 6 :
			bcv.showDialog();
			break;
		case BookContentThread.THREAD_VIRI:
			if( bcv.portraint==1 ){
				List<String> str = bcv.bct.str;
				List<String> strPrev = bcv.bct.strPrev;
				List<String> strNext = bcv.bct.strNext;
				
				Log.i("zkli", "markPoint"+bcv.markPoint);
				Log.i("zkli", "preventPoint"+bcv.preventPoint);
				Log.i("zkli", "nextPoint"+bcv.nextPoint);
				bcv.turnPage(strPrev, strPrev, str, str, strNext, strNext);
				if(bcv.dg!=null&& bcv.dg.isShowing()){
					bcv.dg.dismiss();
				}
				bcv.content=str;
				bcv.isMove = false;
				//判断该页是否有书签
				BookMarkBo.isMark(bcv);
				//判断最后一页
				//bcv.lastPageShowing();
			}
			break;
		case BookContentThread.THREAD_HORI:
			/***
			 * 取得百分比及数据的初始数据。
			 * 先调当前页，然后调上一页。再掉两次下一页。
			 * 当前的位置保存在了下一页的位置，需调整当前位置。
			 * */
			
			if(bcv.portraint == 2) {
				List<String> str =bcv. bct.str;
				List<String> strPrev = bcv.bct.strPrev;
				List<String> strNext = bcv.bct.strNext;
				
				Log.i("zkli", "markPoint"+bcv.markPoint);
				Log.i("zkli", "preventPoint"+bcv.preventPoint);
				Log.i("zkli", "nextPoint"+bcv.nextPoint);
				List<String>  str1 = null ;
				List<String>  str2 = null ;
				List<String>  str3 = null ;
				List<String>  str4 = null ;
				List<String>  str5 = null ;
				List<String>  str6 = null ;
				
				if (strPrev!=null && strPrev.size()>=bcv.bc.getLineCount()/2){
					str1 = strPrev.subList(0, bcv.bc.getLineCount()/2);
					str2 = strPrev.subList( bcv.bc.getLineCount()/2,strPrev.size());
				}else {
					str1 = strPrev;
					str2 = null;
				}
				if (str!=null && str.size() >= bcv.bc.getLineCount()/2){
					str3 = str.subList(0,bcv.bc.getLineCount()/2);
					str4 = str.subList( bcv.bc.getLineCount()/2,str.size());
				}else {
					str3 = str;
					str4 = null;
				}
				if (strNext!=null && strNext.size()>bcv.bc.getLineCount()/2){
					str5 = strNext.subList(0,bcv.bc.getLineCount()/2);
					str6 = strNext.subList( bcv.bc.getLineCount()/2,strNext.size());
				}else {
					str5 = strNext;
					str6= null;
				}
				//写入content;
				bcv.content = str3;
				bcv.turnPage(str1, str2,str3,str4,str5,str6);
				//判断该页是否有书签
				BookMarkBo.isMark(bcv);
				
				//bcv.lastPageShowing();
			}
			
			break;
		case BookContentThread.END_VIRI :
			if (bcv.portraint == 1) {
				List<String> str = null;
				List<String> strPrev = null;

				try {

					str = bcv.bct.str;
					strPrev = bcv.bct.strPrev;
					Log.i("zkli", "markPoint" + bcv.markPoint);
					Log.i("zkli", "preventPoint" + bcv.preventPoint);
					Log.i("zkli", "nextPoint" + bcv.nextPoint);
					bcv.turnPage(strPrev, strPrev, str, str, null, null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// 写入content;

				bcv.content = str;

				if (bcv.dg != null && bcv.dg.isShowing()) {
					bcv.dg.dismiss();
				}

				bcv.isMove = false;
				// lastPageShowing();
				// 提示最后一页;
				//bcv.lastPageShowing();
				//判断该页是否有书签
				BookMarkBo.isMark(bcv);
			}
			
			break;
		
		case BookContentThread.END_HORI:
			if (bcv.portraint== 2){
				List<String> str = null;
				List<String> strPrev = null;
				List<String>  str1 = null ;
				List<String>  str2 = null ;
				List<String>  str3 = null ;
				List<String>  str4 = null ;
				
				try {
					
					str = bcv.bct.str;
					strPrev = bcv.bct.strPrev;
					Log.i("zkli", "markPoint"+bcv.markPoint);
					Log.i("zkli", "preventPoint"+bcv.preventPoint);
					Log.i("zkli", "nextPoint"+bcv.nextPoint);
					bcv.turnPage(strPrev, strPrev, str, str, null, null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//写入content;
				bcv.content = str;
				
				if (strPrev!=null && strPrev.size()>=bcv.bc.getLineCount()/2){
					str1 = strPrev.subList(0, bcv.bc.getLineCount()/2);
					str2 = strPrev.subList( bcv.bc.getLineCount()/2,strPrev.size());
				} else {
					str1 = strPrev;
					str2 = null;
				}
				if (str!=null && str.size()>=bcv.bc.getLineCount()/2){
					str3 = str.subList(0, str.size()/2);
					str4 = str.subList( str.size()/2,str.size());
				} else {
					str3 = str;
					str4 = null;
				}
				bcv.turnPage(str1, str2, str3, str4, null, null);	
				bcv.bookpagebg.isToBack = false;
				//提示最后一页;
				//bcv.lastPageShowing();
				//判断该页是否有书签
				BookMarkBo.isMark(bcv);
			}
			break;
		default:
			break;
		}
		//对应百分比
		Log.i("zkli", "progress__bcv.markPoint"+bcv.markPoint);
		bcv.sbrMainContent.setProgress((int)(10000*bcv.markPoint/getFileLength()));
		if(bcv.dg!=null&& bcv.dg.isShowing()){
			bcv.dg.dismiss();
		}
		super.handleMessage(msg);
	}
	public long getFileLength(){
		long fileLength = 0;
		Plugin plugin = bcv.bc.pm.getDefaultPlugin();
		fileLength = bcv.bc.pm.length(plugin, bcv.bc.pm.filehandle);
		
		if(fileLength > 0) return fileLength;
		else return 0;
	}
}
