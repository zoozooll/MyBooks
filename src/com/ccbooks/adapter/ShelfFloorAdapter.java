package com.ccbooks.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ccbooks.listener.DelListener;
import com.ccbooks.listener.OpenBookListerer;
import com.ccbooks.listener.bookListView.ShelfItemOnTouch;
import com.ccbooks.util.BookUtil;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.R;
import com.ccbooks.vo.Book;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ShelfFloorAdapter extends BaseAdapter {

	private BookShelfView context;
	public List<Book> books;
	private LayoutInflater inflater;
	public int orientation; // 判断横竖屏 1 竖屏 ;2横屏
//	private static int itemsNumLine = 7;	//横屏显示电子书数目
//	private static int itemsNumVir = 4 ;	//竖屏显示电子书数目
	// public boolean isEdit;
	private int numColumns;		//每行的列数；
	
	private int screenWidth;	//屏幕宽度；
	private int screenHeight;	//屏幕高度；
	private int marginLeftFirst =10;	//第一个距离左边
	private int marginLeftItems;	//每个选项间距离左边的距离；
	private int itemWidth =110;	//每个item的宽度；

	public int count;	//书本总数
	public int selectItem=-1;	//选中的选项

	// int[] arrint=
	// {R.drawable.book1,R.drawable.book2,R.drawable.book3,R.drawable.book4,R.drawable.book5,R.drawable.book6};

	public ShelfFloorAdapter(BookShelfView context, List<Book> books) {
		this.context = context;
		this.books = books;
		inflater = LayoutInflater.from(context);
		Display display = context.getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		getNumColmns();
		getMarginLeftItems();
	}

	@Override
	public int getCount() {
		/*计算书架显示的最少行数，
		 * 本方法的计算方式：
		 * （屏幕高-top部的高度）/每行的高度 的进一法
		 * */
		int minCount = (int)(Math.ceil ((double)(screenHeight-44)/159));
		//每一排书本的数量；
		int tempCount = (BookUtil.bookSize(books) + numColumns-1) / numColumns;

		if (tempCount> minCount) {
			return tempCount;
		} else {
			return minCount;
		} 

	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=showInVertical(position,convertView);
		return view;
	}

	/* 
	* 计算字符串的字节长度(字母数字计1，汉字及标点计2) 
	* 
	*/ 
	public  int byteLength(String string){ 
		int lencount = 0; 
		for(int i=0;i<string.length();i++){ 
		if(Integer.toHexString(string.charAt(i)).length()==4){ 
			lencount += 2; 
		}else{ 
			lencount++; 
		} 
		} 
		return lencount; 
	} 

	/* 
	* 按指定长度，省略字符串部分字符 
	* @para String 字符串 
	* @para length 保留字符串长度 
	* @return 省略后的字符串 
	*/ 
	public  String omitString(String string,int length){ 
		StringBuffer sb = new StringBuffer(); 
		if(byteLength(string)>length){ 
		int lencount = 0; 
		for(int i=0;i<string.length();i++){ 
			char temp = string.charAt(i); 
			if(Integer.toHexString(temp).length()==4){ 
				lencount += 2; 
			}else{ 
				lencount++; 
			} 
			if(lencount<length-3){ 
				sb.append(temp); 
			} 
			if(lencount==length-3){ 
				sb.append(temp);
				sb.append("...");
				break; 
			} 
			if(lencount>length-3){ 
				//sb.append(" "); 
				sb.append("...");
				break; 
			} 
		} 
		if(lencount<length-3)
			sb.append("..."); 
		}else{ 
			sb.append(string); 
		} 
		return sb.toString(); 
	} 

/*	class ViewHolderVertical{
		
		Button btBook1;
		Button btBook2;
		Button btBook3;
		Button btBook4;
		ImageView ivdel1;
		ImageView ivdel2;
		ImageView ivdel3;
		ImageView ivdel4;
		ImageView forceground1;
		ImageView forceground2;
		ImageView forceground3;
		ImageView forceground4;
	}*/
	
	class ViewHolder{
		List<Button> btnBooks;
		List<ImageView> ivdels;
		List<ImageView> forceGroups;
	}
	
	/** 竖屏显示 */
	private View showInVertical(int position,View convertView) {
		ViewHolder holder;
		
		if(convertView == null){
			convertView = (LinearLayout) inflater.inflate(R.layout.shelffloor,
				null);
			holder = new ViewHolder();
		// 赋值;
			/*holderVertical.btBook1 = (Button) convertView.findViewById(R.id.btBook1);
			holderVertical.btBook2 = (Button) convertView.findViewById(R.id.btBook2);
			holderVertical.btBook3 = (Button) convertView.findViewById(R.id.btBook3);
			holderVertical.btBook4 = (Button) convertView.findViewById(R.id.btBook4);
			holderVertical.ivdel1 = (ImageView) convertView.findViewById(R.id.ivdel1);
			holderVertical.ivdel2 = (ImageView) convertView.findViewById(R.id.ivdel2);
			holderVertical.ivdel3 = (ImageView) convertView.findViewById(R.id.ivdel3);
			holderVertical.ivdel4 = (ImageView) convertView.findViewById(R.id.ivdel4);
			holderVertical.forceground1 = (ImageView) convertView.findViewById(R.id.forcebackground1);
			holderVertical.forceground2 = (ImageView) convertView.findViewById(R.id.forcebackground2);
			holderVertical.forceground3 = (ImageView) convertView.findViewById(R.id.forcebackground3);
			holderVertical.forceground4 = (ImageView) convertView.findViewById(R.id.forcebackground4);*/
			
			/*
			 * 在考虑不同的分辨率的前提下，每层的书的数目可能会不一样；
			 * */
			
			LayoutParams lp = new LayoutParams(100,135);
			holder.btnBooks = new ArrayList<Button>();
			holder.ivdels = new ArrayList<ImageView>();
			holder.forceGroups = new ArrayList<ImageView>();
			for(int i = 0; i<numColumns; i++) {
				FrameLayout flBookShelfItem = (FrameLayout) inflater.inflate(R.layout.book_shell_item_layout, null);
				if (i == 0){
					lp.setMargins(marginLeftFirst+marginLeftItems, 25, 0, 10);
					
				} else {
					lp.setMargins(marginLeftItems, 15, 0, 10);
				}
				((LinearLayout)convertView).addView(flBookShelfItem, lp);
				holder.btnBooks.add((Button) flBookShelfItem.findViewById(R.id.btBook));
				holder.ivdels.add((ImageView) flBookShelfItem.findViewById(R.id.ivdel));
				holder.forceGroups.add((ImageView) flBookShelfItem.findViewById(R.id.forcebackground));
			}
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		//Button[] bts = { holderVertical.btBook1, holderVertical.btBook2, holderVertical.btBook3, holderVertical.btBook4 };
		//ImageView[] ivdels = { holderVertical.ivdel1, holderVertical.ivdel2, holderVertical.ivdel3, holderVertical.ivdel4 };
		//ImageView[] fgs = { holderVertical.forceground1, holderVertical.forceground2, holderVertical.forceground3, holderVertical.forceground4 };
		for (int i = 0; i < numColumns; i++) {
			if ((position * numColumns) + i >= BookUtil.bookSize(books)) {
				for (; i < numColumns; i++)
				{
					holder.btnBooks.get(i).setVisibility(View.GONE);
					holder.ivdels.get(i).setVisibility(View.GONE);
					holder.forceGroups.get(i).setVisibility(View.GONE);
					
					holder.btnBooks.get(i).setText(null);
				}
				break;
			}
			int itemIndex = position * numColumns + i;
			holder.btnBooks.get(i).setVisibility(View.VISIBLE);
			Book book = books.get(itemIndex);
			Drawable da = null;
			if (book.imgpath != null && !"".equals(book.imgpath)) {
				Bitmap bitmap = BitmapFactory.decodeFile(book.imgpath);
				da = new BitmapDrawable(bitmap);
				holder.btnBooks.get(i).setText(null);
			} else {
				da = context.getResources().getDrawable(R.drawable.def_cover_shell);
				holder.btnBooks.get(i).setText(omitString(book.bookname,20));
			}
			holder.btnBooks.get(i).setBackgroundDrawable(da);
			if (context.isEdit) {
				holder.ivdels.get(i).setVisibility(View.VISIBLE);
			} else {
				holder.ivdels.get(i).setVisibility(View.GONE);
			}
			DelListener delListener = new DelListener(context, this, book);
			holder.ivdels.get(i).setOnClickListener(delListener);
			//context.ivDels.add(ivdels[i]);
			 
			//OpenBookListerer obListener = new OpenBookListerer(context, book,this);
			//fgs[i].setOnClickListener(obListener);
			if (itemIndex == selectItem ){
				holder.forceGroups.get(i).setBackgroundResource(R.drawable.def_coversel_shell);
			} else {
				holder.forceGroups.get(i).setBackgroundResource(R.drawable.btn_book_style);
			}
			holder.forceGroups.get(i).setOnTouchListener(new ShelfItemOnTouch(context ,book, da ,itemIndex));
			holder.forceGroups.get(i).setVisibility(View.VISIBLE);
		}
		return convertView;

	}
	
	/**
	 * 计算一行有多少本书，本方法能够适应各种分辨率以及横竖屏
	 * 
	 * 应用numColumns属性；
	 * @date 2011-6-11上午10:00:41
	 * @author Lee Chok Kong
	 */
	private void getNumColmns(){
		//算法：（屏幕宽度-左右边距离）/一列的宽度
		numColumns = (screenWidth - marginLeftFirst*2)/itemWidth;
	}
	/**
	 * 获得每一个元素的marginLeft属性
	 * @date 2011-6-11上午10:03:01
	 * @author Lee Chok Kong
	 */
	private void getMarginLeftItems() {
		//基本算法:（（屏幕宽度-左右边距离）取模 每个元素的宽度）每行元素个数+基本距里；
		marginLeftItems = ((screenWidth - marginLeftFirst*2)%itemWidth)/numColumns+10;
	} 
	
/*	class ViewHolderHorizontal{
		Button btBook1;
		Button btBook2;
		Button btBook3;
		Button btBook4;
		Button btBook5;
		Button btBook6;
		Button btBook7;
		ImageView ivdel1;
		ImageView ivdel2;
		ImageView ivdel3;
		ImageView ivdel4;
		ImageView ivdel5;
		ImageView ivdel6;
		ImageView ivdel7;
		ImageView forceground1;
		ImageView forceground2;
		ImageView forceground3;
		ImageView forceground4;
		ImageView forceground5;
		ImageView forceground6;
		ImageView forceground7;
	}
	*/
	/*/** 横屏显示 */
	/*private View showInHorizontal(int position,View convertView) {
		ViewHolderHorizontal holderHorizontal;
		if(convertView == null){
			convertView = (LinearLayout) inflater.inflate(
				R.layout.shelffloorhorizontal, null);
			holderHorizontal = new ViewHolderHorizontal();
		
			holderHorizontal.btBook1 = (Button) convertView.findViewById(R.id.btBook1);
			holderHorizontal.btBook2 = (Button) convertView.findViewById(R.id.btBook2);
			holderHorizontal.btBook3 = (Button) convertView.findViewById(R.id.btBook3);
			holderHorizontal.btBook4 = (Button) convertView.findViewById(R.id.btBook4);
			holderHorizontal.btBook5 = (Button) convertView.findViewById(R.id.btBook5);
			holderHorizontal.btBook6 = (Button) convertView.findViewById(R.id.btBook6);
			holderHorizontal.btBook7 = (Button) convertView.findViewById(R.id.btBook7);
			holderHorizontal.ivdel1 = (ImageView) convertView.findViewById(R.id.ivdel1);
			holderHorizontal.ivdel2 = (ImageView) convertView.findViewById(R.id.ivdel2);
			holderHorizontal.ivdel3 = (ImageView) convertView.findViewById(R.id.ivdel3);
			holderHorizontal.ivdel4 = (ImageView) convertView.findViewById(R.id.ivdel4);
			holderHorizontal.ivdel5 = (ImageView) convertView.findViewById(R.id.ivdel5);
			holderHorizontal.ivdel6 = (ImageView) convertView.findViewById(R.id.ivdel6);
			holderHorizontal.ivdel7 = (ImageView) convertView.findViewById(R.id.ivdel7);
			holderHorizontal.forceground1 = (ImageView) convertView.findViewById(R.id.forcebackground1);
			holderHorizontal.forceground2 = (ImageView) convertView.findViewById(R.id.forcebackground2);
			holderHorizontal.forceground3 = (ImageView) convertView.findViewById(R.id.forcebackground3);
			holderHorizontal.forceground4 = (ImageView) convertView.findViewById(R.id.forcebackground4);
			holderHorizontal.forceground5 = (ImageView) convertView.findViewById(R.id.forcebackground5);
			holderHorizontal.forceground6 = (ImageView) convertView.findViewById(R.id.forcebackground6);
			holderHorizontal.forceground7 = (ImageView) convertView.findViewById(R.id.forcebackground7);
			convertView.setTag(holderHorizontal);
		}else{
			System.out.println("convertView.getTag1");
			holderHorizontal = (ViewHolderHorizontal)convertView.getTag();
		}
		
		Button[] bts = { holderHorizontal.btBook1, holderHorizontal.btBook2, holderHorizontal.btBook3, holderHorizontal.btBook4, 
				holderHorizontal.btBook5, holderHorizontal.btBook6,holderHorizontal.btBook7 };
		ImageView[] ivdels = { holderHorizontal.ivdel1, holderHorizontal.ivdel2, holderHorizontal.ivdel3, holderHorizontal.ivdel4, 
				holderHorizontal.ivdel5, holderHorizontal.ivdel6,holderHorizontal.ivdel7 };
		ImageView[] fgs = {	holderHorizontal.forceground1,holderHorizontal.forceground2,holderHorizontal.forceground3,
				holderHorizontal.forceground4,holderHorizontal.forceground5,holderHorizontal.forceground6,
				holderHorizontal.forceground7
		};
		
		for (int i = 0; i < bts.length; i++) {
			//超过条件的，不用做；
			if ((position * bts.length) + i >= BookUtil.bookSize(books)) {
				for (; i < bts.length; i++)
				{
					bts[i].setVisibility(View.GONE);
					ivdels[i].setVisibility(View.GONE);
					fgs[i].setVisibility(View.GONE);
					bts[i].setText(null);	
				}
					break;
			}
			bts[i].setVisibility(View.VISIBLE);
			int itemIndex = position * bts.length + i;
			Book book = books.get(itemIndex);
			Drawable da = null;
			if (book.imgpath != null && !"".equals(book.imgpath)) {
				Bitmap bitmap = BitmapFactory.decodeFile(book.imgpath);
				da = new BitmapDrawable(bitmap);
				bts[i].setText(null);
			} else {
				da = context.getResources()
					.getDrawable(R.drawable.def_cover_shell);
				bts[i].setText(omitString(book.bookname,20));
			}
			bts[i].setBackgroundDrawable(da);
			if (context.isEdit) {
				ivdels[i].setVisibility(View.VISIBLE);
			} else {
				ivdels[i].setVisibility(View.GONE);
			}
			DelListener delListener = new DelListener(context, this, book);
			ivdels[i].setOnClickListener(delListener);
			//OpenBookListerer obListener = new OpenBookListerer(context, book,this);
			//fgs[i].setOnClickListener(obListener);
			if (itemIndex == selectItem ){
				fgs[i].setBackgroundResource(R.drawable.def_coversel_shell);
			} else {
				fgs[i].setBackgroundResource(R.drawable.btn_book_style);
			}
			fgs[i].setOnTouchListener(new ShelfItemOnTouch(context,book,da,itemIndex));
			fgs[i].setVisibility(View.VISIBLE);
		}
		return convertView;

	}*/
	

}
