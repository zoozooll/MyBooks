package com.ccbooks.listener.flip;

import com.ccbooks.view.BookContentView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class SinglePageOnclickListener implements OnTouchListener ,OnGestureListener {

	private BookContentView bcv;
	private GestureDetector detector = new GestureDetector(this);
	float x,y,width,height;
	private static final int FLING_MIN_DISTANCE = 100;//滑动最小距离：像素
	private static final int FLING_MIN_VELOCITY = 100;//滑动最小速度
	public SinglePageOnclickListener(BookContentView bcv) {
		super();
		this.bcv = bcv;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		width = v.getWidth();
		height = v.getHeight();
		return detector.onTouchEvent(event);//把event交给GestureDetector来判断调用的callback
/*		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 按下的处理
			//statrX = event.getX();
			//startY = event.getY();

		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			x = event.getX();
			y = event.getY();
			if (bcv.portraint == 1){
				if (y >= v.getHeight()/2-80 && y <=v.getHeight()/2+80){
					bcv.switchButton();
				}
			}else if(bcv.portraint == 2){
				if (y >= v.getHeight()/2-50 && y <=v.getHeight()/2+50){
					bcv.switchButton();
				}	
			}
		}
		
		return false;*/
	}
		
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		//Log.i("garmen", "onDown");
		return false;
	}
	
	/**onFling就是滑动动作
	 * @param e1:第一个action_down的MotionEvent
	 * @param e2:最后一个action_move的MotionEvent
	 * @param velocityX:x轴上的移动速度
	 * @param velocityY:y轴上的移动速度
	 * 
	 * */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		//Log.i("garmen", "onFling");
		if(-1 == bcv.judgeFocus(bcv.BCVFOCUS,1)){
			return false;
		}
		//判断是否向右滑动
		if(e2.getX()-e1.getX()>FLING_MIN_DISTANCE 
				&& Math.abs(velocityX)>FLING_MIN_VELOCITY){
			turnToRight();
		}
		//判断是否向左滑动
		if(e1.getX()-e2.getX()>FLING_MIN_DISTANCE 
				&& Math.abs(velocityX)>FLING_MIN_VELOCITY){
			turnToLeft();
		}
		
		return false;
	}
	
	/**从左向右翻的方法*/
	public void turnToRight(){
		if(bcv.preventPoint!= -1){
			if(bcv.portraint == 1){
				bcv.bookpagebg.rightShowPage.successOrResetPage(2);
			}else{
				bcv.bookpagebg.leftShowPage.successOrResetPage(2);
			}
		}else{
			bcv.lastPageShowing();
		}
	}
	
	/**从右向左翻的方法*/
	public void turnToLeft(){
		if(bcv.nextPoint != -1){
			bcv.bookpagebg.rightShowPage.successOrResetPage(1);
		}else{
			bcv.lastPageShowing();
		}
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		//Log.i("garmen", "onLongPress");
		//Toast.makeText(bcv, "longPress", Toast.LENGTH_SHORT).show();
		if(-1 == bcv.judgeFocus(bcv.BCVFOCUS,1)){
			return ;
		}
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		//Log.i("garmen", "onScroll");
		if(-1 == bcv.judgeFocus(bcv.BCVFOCUS,0)){
			return false;
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		//Log.i("garmen", "onShowPress");
		if(-1 == bcv.judgeFocus(bcv.BCVFOCUS,0)){
			return ;
		}
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		//Log.i("garmen", "onSingleTapUp");
		if(-1 == bcv.judgeFocus(bcv.BCVFOCUS,1)){
			return false;
		}
		x=e.getX();
		if(bcv.portraint == 2){
			if(x>0 && x <70){
				turnToRight();
			}else if(x>700 && x < width){
				turnToLeft();
			}else if(x>70 && x< 80 ){
				
			}else if(x>690&&x<700){
				
			}else{
				bcv.switchButton();
			}
		} else{
			if(x>0 && x <60){
				turnToRight();
			}else if(x>420 && x < height){
				turnToLeft();
			}else if(x>60 && x< 70 ){
				
			}else if(x>410&&x<420){
				
			}else{
				bcv.switchButton();
			}
		}
		return false;
	}

/*
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("zkli", "onclick");
		bcv.switchButton();
	}
*/
}
