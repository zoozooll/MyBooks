package com.chinachip.book.cartoon;



import com.ccbooks.view.CartoonReader;
import com.ccbooks.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

/**
 * ZoomImageView
 */
public class ZoomImageView extends View implements OnTouchListener {

	
	

	// 屏幕宽高
	private int display_w;
	private int display_h;
	private Bitmap bitmap;

	private int image_w;
	private int image_h;

	public int left = 0;
	public boolean isShowing = false;
	public int top = 0;
	
	private CartoonReader tf = null;
	
	public String filePath = null;
	
	public ZoomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
		setFocusable(true);
		setLongClickable(true);
	}
	
	public ZoomImageView(Context context) {
		super(context);
		setOnTouchListener(this);
		setFocusable(true);
		setLongClickable(true);
		this.tf = (CartoonReader)context;
	}

	/**
	 * 初始�?
	 */
	public void init(int display_w, int display_h, Bitmap bitmap) {
		this.display_w = display_w;
		this.display_h = display_h;
		this.bitmap = bitmap;
		
		// 防止放大缩小时不按比例显�?
		if(bitmap.getWidth() >= display_w && left >= bitmap.getWidth() - display_w){
			left = bitmap.getWidth() - display_w;
		}
		if(bitmap.getHeight() >= display_h && top >= bitmap.getHeight() - display_h){
			top = bitmap.getHeight() - display_h;
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		canvas.drawRect(new Rect(0, 0, display_w, display_h), paint);
		
		image_w = bitmap.getWidth() - left > display_w ? display_w : bitmap.getWidth() - left;
		image_h = bitmap.getHeight() - top > display_h ? display_h : bitmap.getHeight() - top;
		if(bitmap.getWidth() <= display_w){
			left = 0;
			image_w = bitmap.getWidth();
		}
		if(bitmap.getHeight() <= display_h){
			top = 0;
			image_h = bitmap.getHeight();
		}
		int tempLeft = (display_w - image_w)/2;
		int tempRight = (display_h - image_h)/2;
		Bitmap temp = Bitmap.createBitmap(bitmap, left, top, image_w, image_h,
				null, true);
		canvas.drawBitmap(temp, tempLeft, tempRight, null);
		
	}

	int lastx = 0;
	int lasty = 0;
	int dx = 0;
	int dy = 0;
	int movex = 0;
	int movey = 0;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(tf.menuBkmk.isShowed()){
			TranslateAnimation move = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
			move.setDuration(500);
			tf.layoutFun.startAnimation(move);
			tf.menuBkmk.hide();
		} 
		if(tf.menuBlight.isShowed()){
			TranslateAnimation move = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
			move.setDuration(500);
			tf.layoutLight.startAnimation(move);
			tf.menuBlight.hide();
		} 
		if(tf.menuTurnto.isShowed()){
			TranslateAnimation move = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
			move.setDuration(500);
			tf.layoutTurnto.startAnimation(move);
			tf.menuTurnto.hide();
		} 
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			movex = (int) event.getRawX();
			movey = (int) event.getRawY();
			lastx = (int) event.getRawX();
			lasty = (int) event.getRawY();
			if(tf.isAutoRunning == true){
				//tf.isMoveing = true;
				tf.ar.pause();
			
			}
		
			break;
		case MotionEvent.ACTION_MOVE:
			if(tf.isAutoRunning == true && tf.ar.flag == 1){
//				new Thread(tf.ar).start();
//				tf.isMoveing = false;

				tf.ar.jixu();
			}
			if (bitmap.getWidth() >= display_w || bitmap.getHeight() >= display_h) {
				dx = (int) event.getRawX() - lastx;
				dy = (int) event.getRawY() - lasty;
			/*	Log.i("onScroll", "onScrollonScroll---X" + dx);
				Log.i("onScroll", "onScrollonScroll---Y" + dy);
*/
				left = left - dx;
				top = top - dy;
			
				
				if (left < 0) {
					left = 0;
				}
				if (bitmap.getWidth() > display_w && left >= bitmap.getWidth() - display_w) {
					left = bitmap.getWidth() - display_w;
				}
				if (top < 0) {
					top = 0;
				}
				if (bitmap.getHeight() > display_h && top >= bitmap.getHeight() - display_h) {
					top = bitmap.getHeight() - display_h;
				}
				invalidate();
				
				
				Log.i("onScroll", "onScrollonScroll---dx" + dx);
				Log.i("onScroll", "onScrollonScroll---dy" + dy);
				Log.i("onScroll", "onScrollonScroll---left" + left);
				Log.i("onScroll", "onScrollonScroll---top" + top);
				Log.i("onScroll", "onScrollonScroll---bitmap.getHeight()" +  (bitmap.getHeight()-display_h));
				Log.i("onScroll", "onScrollonScroll---bitmap.getWidth()" +  (bitmap.getWidth()-display_w));
				lastx = (int) event.getRawX();
				lasty = (int) event.getRawY();
			}
			break;
		case MotionEvent.ACTION_UP:
			movex = (int) event.getRawX() - movex;
			movey = (int) event.getRawY() - movey;
			if(tf.isAutoRunning == true){
//				new Thread(tf.ar).start();
//				tf.isMoveing = false;

				tf.ar.jixu();
			}
			Log.i("onScroll", "onScrollonScroll---Math.abs(dx)"+dx );
			Log.i("onScroll", "onScrollonScroll---Math.abs(dy)"+dy );
			if(left == 0 && movex >80 && !tf.mode && !tf.isAutoRunning){
				if(tf.file > tf.headFile )
				{
					tf.flipper.setInAnimation(AnimationUtils.loadAnimation(tf, R.anim.push_right_in));
					tf.flipper.setOutAnimation(AnimationUtils.loadAnimation(tf, R.anim.push_right_out));
					
					tf.getPrevPage();
					tf.flipper.showPrevious();
					Log.i("onScroll", "向左");
				}else{
					Toast successToast;
					successToast = Toast.makeText(tf, "已到最前页!", Toast.LENGTH_SHORT);
					successToast.show();
				}
				
			
				
				break;
			}if( top == 0 && movey > 80 && tf.mode && !tf.isAutoRunning){
				if(tf.file > tf.headFile)
				{
					tf.flipper.setInAnimation(AnimationUtils.loadAnimation(tf, R.anim.push_down_in));
					tf.flipper.setOutAnimation(AnimationUtils.loadAnimation(tf, R.anim.push_down_out));
					tf.getPrevPage();
					tf.flipper.showPrevious();
					Log.i("onScroll", "向上");
				}else{
					Toast successToast;
					successToast = Toast.makeText(tf, "已到最前页!", Toast.LENGTH_SHORT);
					successToast.show();
				}
				
				break;
			}if(top >= (bitmap.getHeight()-display_h) && movey  < -80  && tf.mode && !tf.isAutoRunning){
				if(tf.file < tf.endFile)
				{
					Log.i("onScroll", "向下");
					tf.flipper.setInAnimation(AnimationUtils.loadAnimation(tf, R.anim.push_up_in));
					tf.flipper.setOutAnimation(AnimationUtils.loadAnimation(tf, R.anim.push_up_out));
					tf.getNextPage();
					tf.flipper.showNext();
				}else{
					Toast successToast;
					successToast = Toast.makeText(tf, "此书完!", Toast.LENGTH_SHORT);
					successToast.show();
				}
				
				break;
			}if(left >= (bitmap.getWidth()-display_w) && movex < -80  && !tf.mode && !tf.isAutoRunning){
				if(tf.file < tf.endFile)
				{
					Log.i("onScroll", "向右");
					tf.flipper.setInAnimation(AnimationUtils.loadAnimation(tf, R.anim.push_left_in));
					tf.flipper.setOutAnimation(AnimationUtils.loadAnimation(tf, R.anim.push_left_out));
					tf.getNextPage();
					tf.flipper.showNext();
				}else{
					Toast successToast;
					successToast = Toast.makeText(tf, "此书完!", Toast.LENGTH_SHORT);
					successToast.show();
				}
				
				break;
			}
			break;
		default:
			break;
		}

		return tf.detector.onTouchEvent(event);
	}

	


	
}