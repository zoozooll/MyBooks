package com.ccbooks.flip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.ccbooks.bo.BookContentThread;
import com.ccbooks.bo.BookMarkBo;
import com.ccbooks.myUtil.StringTool;
import com.ccbooks.util.ContentManager;
import com.ccbooks.view.BookContentView;
import com.chinachip.books.plugin.Plugin;



import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridView;

/**
 * 目前仍然会出现翻动成功后在最后一刻下面页偶尔会闪动的情况，出现几率太小，无法调试排错
 * 翻页成功后，在最后一刻，翻动页的页边有时会脱离书轴，出现几率太小，无法调试排错
 * @author xiexj
 *
 */

public class BookLayout extends FrameLayout {

	//所有的页信息
	public List<SinglePage> pageList;
	public List<SinglePage> pageList2;
	
	public int i = 7;
	public int temp = 1;
	//左侧目前显示的书页,如果没有是-1,说明这本书是合着的
	private int currentPage = -1;
	
	//页面宽
	public int pageWidth;
	
	//页面高
	public int pageHeight;
	
	//底层
	private FrameLayout buttomLayout;
	
	//顶层（反页显示层）
	private FrameLayout topLayout;
	
	//第二层（当前页显示层）
	private FrameLayout secondLayout;
	
	//第三层（下一页显示层）
	private FrameLayout thirdLayout;
	
	//是否处于自动翻页
	private boolean isAutoFlip = false;
	
	//当前翻动的页
	private SinglePage currentFlipPage = null;
	
	//书本内容
	private ArrayList<List<String>> contentList;

	//是否第一次往前面翻
	public boolean isToBack = true;
	
	//左边开始第一点
	public int firstX;
	public int firstY;
	//屏幕宽高
	public int screenWidth;
	public int screenHeight;
	
	//左边显示页
	public SinglePage leftShowPage;
	//左边的翻转页
	public SinglePage leftMaskPage;
	//左边的底层页
	public SinglePage leftDownPage;
	//右边显示页
	public SinglePage rightShowPage;
	//右边的翻转页
	public SinglePage rightMaskPage;
	//右边的底层页
	public SinglePage rightDownPage;
	
	private BookContentView bcv = null;
	
	private int fileLength = 0;
	
	public BookLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.currentPage = -1;
		this.bcv = (BookContentView) context;
		buttomLayout = new FrameLayout(context);
		buttomLayout.setLayoutParams(LayoutBean.FF);
		//buttomLayout.setPadding(20, 10, 20, 10);
		//buttomLayout.setLayoutParams(params)
		this.addView(buttomLayout);
		
		thirdLayout = new FrameLayout(context);
		thirdLayout.setLayoutParams(LayoutBean.FF);
		//thirdLayout.setPadding(20, 10, 20, 10);
		this.addView(thirdLayout);
		
		secondLayout = new FrameLayout(context);
		secondLayout.setLayoutParams(LayoutBean.FF);
		//secondLayout.setPadding(20, 10, 20, 10);
		this.addView(secondLayout);
		
		topLayout = new FrameLayout(context);
		topLayout.setLayoutParams(LayoutBean.FF);
		//topLayout.setPadding(20, 10, 20, 10);
		this.addView(topLayout);
		
		WindowManager windowManager = bcv.getWindowManager();  
		Display display = windowManager.getDefaultDisplay();   
		screenWidth = display.getWidth();  
		screenHeight = display.getHeight(); 
		if (((BookContentView)context).portraint == 2){
			firstX = screenWidth/2/16;
			firstY = screenHeight/40;
			int pagePendingButtom = screenHeight/30;
			pageWidth = screenWidth/2-firstX;
			pageHeight = screenHeight-firstY-pagePendingButtom ;
		} else {
			//在480×800情况下是
			int pagePendingRight = screenWidth/24;
			int pagePendingButtom = screenHeight*9/400;
			firstX = 0;
			firstY = screenHeight*3/200;
			pageWidth = screenWidth-firstX-pagePendingRight;
			pageHeight = screenHeight-firstY-pagePendingButtom;
			
		}
	}

	public BookLayout(Context context) {
		super(context);
		this.currentPage = -1;
		buttomLayout = new FrameLayout(context);
		buttomLayout.setLayoutParams(LayoutBean.FF);
		this.addView(buttomLayout);
		thirdLayout = new FrameLayout(context);
		thirdLayout.setLayoutParams(LayoutBean.FF);
		this.addView(thirdLayout);
		secondLayout = new FrameLayout(context);
		secondLayout.setLayoutParams(LayoutBean.FF);
		this.addView(secondLayout);
		topLayout = new FrameLayout(context);
		topLayout.setLayoutParams(LayoutBean.FF);
		this.addView(topLayout);
	}
	
	public void setPageList(List<SinglePage> pageList,int currentPage){
		this.pageList = pageList;
		this.currentPage = currentPage;
		if(currentPage<-1||currentPage==pageList.size()){
			currentPage = -1;
		}
		refresh();
	}
	
	public void setPageList2(List<SinglePage> pageList){
		this.pageList2 = pageList;
	}
	
	
	public void setPageLists(List<SinglePage> pageList){
		this.pageList = pageList;
	}
	public void setPageList(int currentPage){
		this.currentPage = currentPage;
		refresh();
	}
	/**
	 * 翻动书页，显示其他两页的内容
	 * @param page
	 * @param maskPagePath
	 * @param resultPagePath
	 */
	void flipPage(SinglePage page,Path maskPagePath,float angle,float shadowAngle,float fx,float fy,float sdx,float sdy,int chooseCorner){
		SinglePage maskPage = getMaskPage(page);
		maskPage.onMaskPathDraw(maskPagePath,angle,shadowAngle,fx,fy,sdx,sdy,chooseCorner);
	}
	/**
	 * 未翻页刷新
	 * */
	void refresh(){
		thirdLayout.removeAllViews();
		secondLayout.removeAllViews();
		topLayout.removeAllViews();
		List<SinglePage> showList = new ArrayList<SinglePage>();
		if(currentPage!=-1){
			leftShowPage = pageList.get(currentPage);
			leftShowPage.reset();
			leftShowPage.getPageContent().create();
			leftShowPage.setLookPage(2);
			showList.add(leftShowPage);
			leftShowPage.setPosition(firstX, firstY);
			leftShowPage.setSize(pageWidth,pageHeight);
			
			leftMaskPage = getMaskPage(leftShowPage);
			leftMaskPage.getPageContent().create();
			leftMaskPage.reset();
			showList.add(leftMaskPage);
			leftMaskPage.setPosition(firstX, firstY);
			leftMaskPage.setSize(pageWidth, pageHeight);
			
			if(currentPage-2>=0){
				leftDownPage = pageList.get(currentPage-2);
				leftDownPage.reset();
				leftDownPage.getPageContent().create();
				leftDownPage.setLookPage(0);
				showList.add(leftDownPage);
				leftDownPage.setPosition(firstX, firstY);
				leftDownPage.setSize(pageWidth,pageHeight);
			}
		}
		
		int rightPageIndex = currentPage+1;
		if(rightPageIndex<pageList.size()){
			rightShowPage = pageList.get(rightPageIndex);
			rightShowPage.reset();
			rightShowPage.getPageContent().create();
			rightShowPage.setLookPage(2);
			showList.add(rightShowPage);
			if (bcv.portraint == 2){
				rightShowPage.setPosition(firstX+pageWidth, firstY);
			} else {
				rightShowPage.setPosition(firstX, firstY);
			}
			rightShowPage.setSize(pageWidth, pageHeight);
			
			rightMaskPage = getMaskPage(rightShowPage);
			rightMaskPage.reset();
			rightMaskPage.getPageContent().create();
			showList.add(rightMaskPage);
			if (bcv.portraint == 2){
				rightMaskPage.setPosition(firstX+pageWidth, firstY);
			} else {
				rightMaskPage.setPosition(firstX, firstY);
			}
			rightMaskPage.setSize(pageWidth, pageHeight);
			if(rightPageIndex+2<pageList.size()){
				rightDownPage = pageList.get(rightPageIndex+2);
				rightDownPage.reset();
				rightDownPage.getPageContent().create();
				rightDownPage.setLookPage(0);
				showList.add(rightDownPage);
				if (bcv.portraint == 2){
					rightDownPage.setPosition(firstX+pageWidth, firstY);
				} else {
					rightDownPage.setPosition(firstX, firstY);
				}
				rightDownPage.setSize(pageWidth,pageHeight);
			}
		}
		if(leftShowPage!=null){
			secondLayout.addView(leftShowPage);
			if (bcv.portraint == 1){
				leftShowPage.setVisibility(View.GONE);
			}
		}
		if(rightShowPage!=null){
			secondLayout.addView(rightShowPage);
		}
		if(leftMaskPage!=null){
			topLayout.addView(leftMaskPage);
		}
		if(rightMaskPage!=null){
			topLayout.addView(rightMaskPage);
		}
		if(leftDownPage!=null ){
			thirdLayout.addView(leftDownPage);
			if (bcv.portraint == 1){
				leftDownPage.setVisibility(View.GONE);
			}
		}
		if(rightDownPage!=null){
			thirdLayout.addView(rightDownPage);
		}
		fileLength = (int)getFileLength();
		try{
			leftDownPage.mySeekBarProgress((int)(10000*bcv.preventPoint/fileLength));
			leftMaskPage.mySeekBarProgress((int)(10000*bcv.preventPoint/fileLength));
			leftShowPage.mySeekBarProgress((int)(10000*bcv.markPoint/fileLength));
			rightDownPage.mySeekBarProgress((int)(10000*bcv.nextPoint/fileLength));
			rightMaskPage.mySeekBarProgress((int)(10000*bcv.nextPoint/fileLength));
			rightShowPage.mySeekBarProgress((int)(10000*bcv.markPoint/fileLength));
		}catch(ArithmeticException e)
		{
			e.printStackTrace();
		}
		for(SinglePage p : pageList){
			if(showList.contains(p)) {
				//p.mySeekBarProgress((int)(100*bcv.markPoint/bcv.book.fileLength));
				continue;
			}
			p.getPageContent().destory();
		}
	}
	
	/**
	 * 向右翻自动刷新
	 * */
	void refresh2(){
		
		int length = (int)getFileLength();
		thirdLayout.removeAllViews();
		secondLayout.removeAllViews();
		topLayout.removeAllViews();
		
		List<SinglePage> showList = new ArrayList<SinglePage>();
		if(currentPage!=-1){
			leftShowPage = pageList.get(currentPage);
			leftShowPage.reset();
			leftShowPage.getPageContent().create();
			leftShowPage.setLookPage(2);
			showList.add(leftShowPage);
			leftShowPage.setPosition(firstX, firstY);
			leftShowPage.setSize(pageWidth,pageHeight);
			
			leftMaskPage = getMaskPage(leftShowPage);
			leftMaskPage.getPageContent().create();
			leftMaskPage.reset();
			showList.add(leftMaskPage);
			leftMaskPage.setPosition(firstX, firstY);
			leftMaskPage.setSize(pageWidth, pageHeight);
			if(currentPage-2>=0){
				leftDownPage = pageList.get(currentPage-2);
				leftDownPage.reset();
				leftDownPage.getPageContent().create();
				leftDownPage.setLookPage(0);
				showList.add(leftDownPage);
				leftDownPage.setPosition(firstX, firstY);
				leftDownPage.setSize(pageWidth,pageHeight);
			}
		}
		int rightPageIndex = currentPage+1;
		if(rightPageIndex<pageList.size()){
			rightShowPage = pageList.get(rightPageIndex);
			rightShowPage.reset();
			rightShowPage.getPageContent().create();
			rightShowPage.setLookPage(2);
			showList.add(rightShowPage);
			if (bcv.portraint == 2){
				rightShowPage.setPosition(firstX+pageWidth, firstY);
			} else {
				rightShowPage.setPosition(firstX, firstY);
			}
			rightShowPage.setSize(pageWidth, pageHeight);
			
			rightMaskPage = getMaskPage(rightShowPage);
			rightMaskPage.reset();
			rightMaskPage.getPageContent().create();
			showList.add(rightMaskPage);
			if (bcv.portraint == 2){
				rightMaskPage.setPosition(firstX+pageWidth, firstY);
			} else {
				rightMaskPage.setPosition(firstX, firstY);
			}
			rightMaskPage.setSize(pageWidth, pageHeight);
			if(rightPageIndex+2<pageList.size()){
				rightDownPage = pageList.get(rightPageIndex+2);
				rightDownPage.reset();
				rightDownPage.getPageContent().create();
				rightDownPage.setLookPage(0);
				showList.add(rightDownPage);
				if (bcv.portraint == 2){
					rightDownPage.setPosition(firstX+pageWidth, firstY);
				} else {
					rightDownPage.setPosition(firstX, firstY);
				}
				rightDownPage.setSize(pageWidth,pageHeight);
			}
			
			//翻页的方向是否改变，如果改变翻页方向，索引需要立即向后翻两页；
			if (!isToBack) {
				bcv.cc.nextPage();
				if(bcv.markPoint>0){
					bcv.cc.nextPage();
				}
			}
			isToBack = true;
			//转换当前页索引，上一页索引，下一页索引；
			bcv.preventPoint = bcv.markPoint;
			bcv.markPoint = bcv.nextPoint;
			List<String> content = null;
			List<String> contentg = null;		//第一页数据
			List<String> contenth = null;		//第二页数据；
			//分横竖屏获得cc的数据
			try {
				content = StringTool.getCleanList(bcv.cc.nextPage());
			} catch (IndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (content != null && content.size()>0){
				bcv.nextPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
				try {
					bcv.nextEnd = bcv.bc.getNextIndexAt();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				if (bcv.markPoint<length){
					content =new ArrayList<String>();
					content.add("全书完");
					bcv.nextPoint = length;
				} else {
					isToBack= false;
					bcv.nextPoint =-1;
				}
				bcv.nextEnd = -1;
			}
			if  (bcv.portraint ==1) {
					
				contentg = content;
				contenth = content;
		
			}
			if (bcv.portraint ==2){

				if (content!=null && content.size()>= bcv.bc.getLineCount()/2){
					contentg = content.subList(0, bcv.bc.getLineCount()/2);
					contenth = content.subList(bcv.bc.getLineCount()/2, content.size());
				}else {
					contentg = content ;
					contenth = null;
				}			
					
			}
			Log.i("zkli", "markPoint"+bcv.markPoint);
			Log.i("zkli", "preventPoint"+bcv.preventPoint);
			Log.i("zkli", "nextPoint"+bcv.nextPoint);	
			//ArrayList<ArrayList<String>> contentList = blo.getContentList();
			
			List<String> al3 = new ArrayList<String>();
			List<String> al4 = new ArrayList<String>();
			List<String> al5 = new ArrayList<String>();
			List<String> al6 = new ArrayList<String>();
			List<String> al7 = new ArrayList<String>();
			List<String> al8 = new ArrayList<String>();
//			ArrayList al9 = new ArrayList<String>();
//			ArrayList al10 = new ArrayList<String>();
				al3 = contentList.get(3);
				al4 = contentList.get(4);
				al5 = contentList.get(5);
				al6 = contentList.get(6);
//				
				
			al7 = contentg;
			al8 = contenth;
		
			//修改BookContentView里面的content
			bcv.content = al5;
			
			contentList.clear();
			contentList.add(0, al3);
			contentList.add(1, al3);
			contentList.add(2, al4);
			contentList.add(3, al5);
			contentList.add(4, al6);
			contentList.add(5, al7);
			contentList.add(6, al8);
	
			ArrayList<String> cl = null;
			setContentList(contentList);
		
		}
		if(leftShowPage!=null){
			secondLayout.addView(leftShowPage);
			if (bcv.portraint == 1){
				leftShowPage.setVisibility(View.GONE);
			}
		}
		if(rightShowPage!=null){
			secondLayout.addView(rightShowPage);
		}
		if(leftMaskPage!=null){
			topLayout.addView(leftMaskPage);
		}
		if(rightMaskPage!=null){
			topLayout.addView(rightMaskPage);
		}
		if(leftDownPage!=null){
			thirdLayout.addView(leftDownPage);
			if (bcv.portraint == 1){
				leftDownPage.setVisibility(View.GONE);
			}
		}
		if(rightDownPage!=null){
			thirdLayout.addView(rightDownPage);
		}
		fileLength = (int)getFileLength();
		try{
			leftDownPage.mySeekBarProgress((int)(10000*bcv.preventPoint/fileLength));
			leftMaskPage.mySeekBarProgress((int)(10000*bcv.preventPoint/fileLength));
			leftShowPage.mySeekBarProgress((int)(10000*bcv.markPoint/fileLength));
			rightDownPage.mySeekBarProgress((int)(10000*bcv.nextPoint/fileLength));
			rightMaskPage.mySeekBarProgress((int)(10000*bcv.nextPoint/fileLength));
			rightShowPage.mySeekBarProgress((int)(10000*bcv.markPoint/fileLength));
		}catch(ArithmeticException e)
		{
			e.printStackTrace();
		}
		

		for(SinglePage p : pageList){
			if(showList.contains(p)) {
				//p.mySeekBarProgress((int)(100*bcv.markPoint/bcv.book.fileLength));
				continue;
			}
			p.getPageContent().destory();
		}
		
		
		//判断该页是否有书签
		BookMarkBo.isMark(bcv);
		//对应百分比
		//bcv.sbrMainContent.setProgress((int)(100*bcv.markPoint/bcv.book.fileLength));
	}
	
	
	/**
	 * 向左翻自动刷新
	 * 
	 * */
	void refresh3() {
		
		List<String> content = null;
		List<String> contentg = new ArrayList<String>();
		List<String> contenth = new ArrayList<String>();
		
		//翻页的方向是否改变，如果改变翻页方向，索引需要立即向前翻两页；
		if (isToBack) {
			bcv.cc.previousPage();
			bcv.cc.previousPage();
		}
		if (bcv.markPoint != -1 && bcv.preventPoint == -1){
			if (bcv.portraint == 1){
				bcv.bct.what = BookContentThread.FIRST_VIRI;
			}else {
				bcv.bct.what = BookContentThread.FIRST_HORI;
			}
			bcv.bct.run();
			return;
		}
		//转换当前页索引，上一页索引，下一页索引；
		bcv.nextEnd = bcv.nextPoint;
		bcv.nextPoint = bcv.markPoint;
		bcv.markPoint = bcv.preventPoint;
		try {
			content = StringTool.getCleanList(bcv.cc.previousPage());
			if (content!=null && content.size()>0){
				bcv.preventPoint = ContentManager.getCurrIndexAtOutOfException(bcv.bc);
			}else{
				if (bcv.markPoint>0){
					bcv.preventPoint = 0;
					content = StringTool.getCleanList(bcv.bc.getCurrPage(0));
				}else {
					bcv.preventPoint = -1;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			content = null;
			e.printStackTrace();
		} 
		if  (bcv.portraint ==1) {
			
				contentg = content;
				contenth = content;
		
		}
		if (bcv.portraint ==2){
			try {
				if (content!=null && content.size()>= bcv.bc.getLineCount()/2){
					contentg = content.subList(0, bcv.bc.getLineCount()/2);
					contenth = content.subList(bcv.bc.getLineCount()/2, content.size());
				}else {
					contentg = content ;
					contenth = null;
				}
			} catch (IndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				content = null;
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		
	
		Log.i("zkli", "markPoint"+bcv.markPoint);
		Log.i("zkli", "preventPoint"+bcv.preventPoint);
		Log.i("zkli", "nextPoint"+bcv.nextPoint);
		List<String> al_1 = new ArrayList<String>();
		List<String> al0 = new ArrayList<String>();
		List<String> al1 = new ArrayList<String>();
		List<String> al2 = new ArrayList<String>();
		List<String> al3 = new ArrayList<String>();
		List<String> al4 = new ArrayList<String>();

		al1 = contentList.get(1);
		al2 = contentList.get(2);
		al3 = contentList.get(3);
		al4 = contentList.get(4);

		al_1 = contentg;
		al0 = contenth;

		contentList.clear();
		contentList.add(0, al_1);
		contentList.add(1, al_1);
		contentList.add(2, al0);
		contentList.add(3, al1);
		contentList.add(4, al2);
		contentList.add(5, al3);
		contentList.add(6, al4);
		
		//修改BookContentView的参量;
		bcv.content = al1;
		
		// ((MyBook)context).resetSingleList();
		thirdLayout.removeAllViews();
		secondLayout.removeAllViews();
		topLayout.removeAllViews();

		List<SinglePage> showList = new ArrayList<SinglePage>();
		if (currentPage != -1) {
			leftShowPage = pageList.get(currentPage);
			leftShowPage.reset();
			leftShowPage.getPageContent().create();
			leftShowPage.setLookPage(2);
			showList.add(leftShowPage);
			leftShowPage.setPosition(firstX, firstY);
			leftShowPage.setSize(pageWidth, pageHeight);
			
			leftMaskPage = getMaskPage(leftShowPage);
			leftMaskPage.getPageContent().create();
			leftMaskPage.reset();
			showList.add(leftMaskPage);
			leftMaskPage.setPosition(firstX, firstY);
			leftMaskPage.setSize(pageWidth, pageHeight);
			if (currentPage - 2 >= 0) {
				leftDownPage = pageList.get(currentPage - 2);
				leftDownPage.reset();
				leftDownPage.getPageContent().create();
				leftDownPage.setLookPage(0);
				showList.add(leftDownPage);
				leftDownPage.setPosition(firstX, firstY);
				leftDownPage.setSize(pageWidth, pageHeight);
			}
		}
		int rightPageIndex = currentPage + 1;
		if (rightPageIndex < pageList.size()) {
			rightShowPage = pageList.get(rightPageIndex);
			rightShowPage.reset();
			rightShowPage.getPageContent().create();
			rightShowPage.setLookPage(2);
			showList.add(rightShowPage);
			if (bcv.portraint == 2){
				rightShowPage.setPosition(firstX+pageWidth, firstY);
			} else {
				rightShowPage.setPosition(firstX, firstY);
			}
			rightShowPage.setSize(pageWidth, pageHeight);
			
			rightMaskPage = getMaskPage(rightShowPage);
			rightMaskPage.reset();
			rightMaskPage.getPageContent().create();
			showList.add(rightMaskPage);
			if (bcv.portraint == 2){
				rightMaskPage.setPosition(firstX+pageWidth, firstY);
			} else {
				rightMaskPage.setPosition(firstX, firstY);
			}
			rightMaskPage.setSize(pageWidth, pageHeight);
			if (rightPageIndex + 2 < pageList.size()) {
				rightDownPage = pageList.get(rightPageIndex + 2);
				rightDownPage.reset();
				rightDownPage.getPageContent().create();
				rightDownPage.setLookPage(0);
				showList.add(rightDownPage);
				if (bcv.portraint == 2){
					rightDownPage.setPosition(firstX+pageWidth, firstY);
				} else {
					rightDownPage.setPosition(firstX, firstY);
				}
				rightDownPage.setSize(pageWidth, pageHeight);
			}

			//			
			ArrayList<String> cl = null;
			setContentList(contentList);

			if (leftShowPage != null) {
				secondLayout.addView(leftShowPage);
				if (bcv.portraint == 1){
					leftShowPage.setVisibility(View.GONE);
				}
			}
			if (rightShowPage != null) {
				secondLayout.addView(rightShowPage);
			}
			if (leftMaskPage != null) {
				topLayout.addView(leftMaskPage);
			}
			if (rightMaskPage != null) {
				topLayout.addView(rightMaskPage);
			}
			if (leftDownPage != null) {
				thirdLayout.addView(leftDownPage);
				if (bcv.portraint == 1){
					leftDownPage.setVisibility(View.GONE);
				}
			}
			if (rightDownPage != null) {
				thirdLayout.addView(rightDownPage);
			}
			fileLength = (int)getFileLength();
			try{
				leftDownPage.mySeekBarProgress((int)(10000*bcv.preventPoint/fileLength));
				leftMaskPage.mySeekBarProgress((int)(10000*bcv.preventPoint/fileLength));
				leftShowPage.mySeekBarProgress((int)(10000*bcv.markPoint/fileLength));
				rightDownPage.mySeekBarProgress((int)(10000*bcv.nextPoint/fileLength));
				rightMaskPage.mySeekBarProgress((int)(10000*bcv.nextPoint/fileLength));
				rightShowPage.mySeekBarProgress((int)(10000*bcv.markPoint/fileLength));
			}catch(ArithmeticException e)
			{
				e.printStackTrace();
			}
			for (SinglePage p : pageList) {
				if(showList.contains(p)) {
					//p.mySeekBarProgress((int)(100*bcv.markPoint/bcv.book.fileLength));
					continue;
				}
				p.getPageContent().destory();
			}
		}
		isToBack = false;
		//判断该页是否有书签
		BookMarkBo.isMark(bcv);
		//对应百分比
		//bcv.sbrMainContent.setProgress((int)(100*bcv.markPoint/bcv.book.fileLength));
	}
	/**
	 * 设置当前显示页
	 * @param page
	 */
	public void setCurrentPage(int page){
		if(page%2==0){
			page -=1;
		}
		currentPage = page;
		refresh();
	}
	
	/**
	 * 是否是书本的封面
	 * @param p
	 * @return
	 */
	public boolean isFirstOrLastPage(SinglePage p){
		boolean flag = false;
		if(pageList!=null){
			SinglePage fp = pageList.get(0);
			if(fp==p){
				flag = true;
			}else{
				SinglePage lp = pageList.get(pageList.size()-1);
				if(lp==p) flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取当前翻动页的反页
	 * @param page
	 * @return
	 */
	SinglePage getMaskPage(SinglePage page){
		int c = pageList.indexOf(page);
		if(c%2!=0){
			return pageList.get(c-1);
		}else{
			return pageList.get(c+1);
		}
	}
	

	
	/**
	 * 成功翻动当前页
	 * @param page
	 */
	
	void successFlipPage(SinglePage page,boolean flag){
		if(flag){
			int c = pageList.indexOf(page);
//			if(c%2!=0){
//				currentPage-=2;
//			}else{
//				currentPage+=2;
//			}
			refresh();
		}else{
			refresh();
		}
	}
	
	
	void successFlipPageLeft(SinglePage page,boolean flag){
		if(flag){
			int c = pageList.indexOf(page);
//			if(c%2!=0){
//				currentPage-=2;
//			}else{
//				currentPage+=2;
//			}
			refresh3();
		}else{
			refresh3();
		}
	}
	
	void successFlipPageRight(SinglePage page,boolean flag){
		if(flag){
			int c = pageList.indexOf(page);
//			if(c%2!=0){
//				currentPage-=2;
//			}else{
//				currentPage+=2;
//			}
			refresh2();
		}else{
			refresh2();
		}
	}

	public boolean isAutoFlip() {
		return isAutoFlip;
	}

	public void setAutoFlip(boolean isAutoFlip) {
		this.isAutoFlip = isAutoFlip;
	}

	public SinglePage getCurrentFlipPage() {
		return currentFlipPage;
	}

	public void setCurrentFlipPage(SinglePage currentFlipPage) {
		this.currentFlipPage = currentFlipPage;
	}

	public ArrayList<List<String>> getContentList() {
		return contentList;
	}

	public void setContentList(ArrayList<List<String>> contentList) {
		this.contentList = contentList;
	}
	public List<SinglePage> getPageList() {
		return pageList;
	}

	public void setPageList(List<SinglePage> pageList) {
		this.pageList = pageList;
	}
	public long getFileLength(){
		long fileLength = 0;
		Plugin plugin = bcv.bc.pm.getDefaultPlugin();
		fileLength = bcv.bc.pm.length(plugin, bcv.bc.pm.filehandle);
		
		if(fileLength > 0) return fileLength;
		else return 0;
	}
}
