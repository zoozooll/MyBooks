package com.ccbooks.listener;


import com.ccbooks.bo.BookMarkBo;
import com.ccbooks.bo.FileBo;
import com.ccbooks.myUtil.StringTool;
import com.chinachip.ccbooks.core.TextSizeUtil;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 书本竖屏页面当用户触摸屏幕时候的动作
 * 自右向左划为下一页
 * 自左向右划为上一页
 * 书本横屏页面当用户触摸屏幕时候的动作
 * 触摸tvMainContentLeft 为上一页
 * 触摸tvMainContentRight 为下一页
 * */
public class MainContentOnTouchListener implements OnTouchListener {

	private BookContentView bcv;
	//float statrX, startY;
	float endX, endY;
	long downTime,upTime;
	static final private int LEFTEDGE = 290;
	static final private int RIGHTEDGE = 128;
	public MainContentOnTouchListener(BookContentView bcv) {
		this.bcv = bcv;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		/*// TODO Auto-generated method stub
		bcv.hiddenOpenWins();
		if (!bcv.turnable){
			//Toast.makeText(bcv, "请稍等", Toast.LENGTH_SHORT);
			Log.i("zkli", "====turnable=========");
			if(bcv.dg!=null){
				bcv.dg.show();
			}
			return false;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 按下的处理
			//statrX = event.getX();
			//startY = event.getY();
			endX = event.getX();
			endY = event.getY();
			downTime = event.getDownTime();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// 放开时的的处理
			endX = event.getX();
			endY = event.getY();
			upTime=event.getEventTime();
			if (upTime - downTime > 100) {
				
				if (v.getId()== R.id.tvMainContent && bcv.portraint == 1) {
					if (endX >= v.getWidth()/2+40 && endX <=v.getWidth()) {
						String content = null;
						System.out.println("------listener----tvMainContent---");
						try {
							content = bcv.cc.nextPage();
						} catch (IndexOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						((TextView) v).setText(content);
					} else if (endX >= 0 && endX <=v.getWidth()/2-40 ) {
						String content = null;
						try {
							content = bcv.cc.previousPage();
						} catch (IndexOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						((TextView) v).setText(content);
					}
					else if(endX>v.getWidth()/2-40 && endX<v.getWidth()/2+40)
					{
						bcv.switchButton();
					}
					
				} else if (v.getId()== R.id.tvMainContentLeft && bcv.portraint == 2) {
					String content = null;
					System.out.println("------listener----tvMainContentLeft---");
					if(endX<LEFTEDGE){		//左翻页如果是翻页区域就翻页	
						try {
							content = bcv.cc.previousPage();
							int midNo = StringTool.getContentMiddleIndex(content, '\n', TextSizeUtil.getLineCount(TextSizeUtil.fontSize, bcv.portraint)/2);
							String str1 = null;
							String str2 = null;
							if (midNo>0){
								str1 = content.substring(0,midNo);
								str2 = content.substring(midNo+1);
							}else{
								str1 = content;
							}
							((TextView) v).setText(str1);
							bcv.tvMainContentRight.setText(str2);
						} catch (NullPointerException e) {
							e.printStackTrace();
						} catch (IndexOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else{ //否则隐藏或显示图标
						bcv.switchButton();
					}
				} else if (v.getId()== R.id.tvMainContentRight && bcv.portraint == 2 ){
					String content = null;
					String str1 = null;
					String str2 = null;
					System.out.println("------listener----tvMainContentRight---");
					System.out.println("v.getWidth------>"+v.getWidth());
					System.out.println("endX------>"+endX);
					if(endX>RIGHTEDGE){		//右翻页如果是翻页区域就翻页
						try {
							content = bcv.cc.nextPage();
							int midNo = StringTool.getContentMiddleIndex(content, '\n', TextSizeUtil.getLineCount(TextSizeUtil.fontSize, bcv.portraint)/2);
							if (midNo>0){
								str1 = content.substring(0,midNo);
								str2 = content.substring(midNo+1);
							}else{
								str1 = content;
							}
						} catch (IndexOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NullPointerException e){
							e.printStackTrace();
							
						}					
						bcv.tvMainContentLeft.setText(str1);
						((TextView) v).setText(str2);

					}else{//否则隐藏或显示图标
						bcv.switchButton();
					}
				}
			}
			if(FileBo.BOOKTYPE_TXT.equals(bcv.book.booktype)){	
				bcv.sbrMainContent.setProgress(BooksEngine.markPoint*100/(int)(BooksEngine.fileLength));
			}else if(FileBo.BOOKTYPE_PDB.equals(bcv.book.booktype)){
				bcv.sbrMainContent.setProgress(BooksEnginePDB.markPoint*100/(int)(BooksEnginePDB.str.length()));
			}
			
			BookMarkBo.isMark(bcv);
			if (bcv.mark != null) {
				bcv.btnBookMark
					.setBackgroundDrawable(bcv.getResources().getDrawable(R.drawable.content_btn_onmark));
			} else {
				bcv.btnBookMark
					.setBackgroundDrawable(bcv.getResources().getDrawable(R.drawable.content_btn_outmark));
			}
			bcv.lastPageShowing();
			
		}*/        
		return false;
	}



}
