/**
 * 
 */
package com.ccbooks.subview;

import com.ccbooks.myUtil.StringTool;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.R;
import com.kang.test.TestClass;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 打开书本动画使用的类
 * 
 * @date 2011-5-17 下午05:53:40
 * @author Lee Chok Kong
 */
public class OpenBookFrame extends FrameLayout {

	private Context context;
	
	// child view
	private FrameLayout flChange;
	private ImageView ivChangeImg;
	
	// touch event tag;
/*	public float touchX;
	public float touchY;*/
	
	//书本初始值以及y值
	public float beginX;
	public float beginY;
	
	//屏幕宽高
	public int screenWidth;
	public int screenHeight;
	
	//移动窗体宽高
	private int itemWidth = 100;
	private int itemHeight = 130;
	
	//涉及的动画
	AnimationSet animation;
	AnimationSet animation2;
	AnimationSet animationClose;
	AnimationSet animationClose2;
	
	private Bitmap bm;
	Matrix mat;

	/**
	 * @param context
	 * @date 2011-5-17 下午05:53:47
	 * @author Lee Chok Hong
	 */

	public OpenBookFrame(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public OpenBookFrame(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public OpenBookFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		flChange = (FrameLayout) LayoutInflater.from(context).inflate(
				R.layout.openbook_layout, null);
		ivChangeImg = (ImageView) flChange.findViewById(R.id.ivChangeImg);
		mat = new Matrix();
        mat.setValues(new float[]{	//进行变换
        	1.0f,0.0f,0.0f,
        	-0.2f,1.2f,0.0f,
        	0.0f,0.0f,1.0f
        });
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
/*		touchX = event.getX();
		touchY = event.getY();*/
		return false;
	}

	/**启动打开的动画；
	 * 
	 * @param beginX
	 * @param beginY
	 * @param da
	 * @date 2011-5-20下午03:03:57
	 * @author Lee Chok Kong
	 */
	public void showItemView(int beginX, int beginY ,Drawable da) {
		//显示封面背景;
		Bitmap source = StringTool.drawableToBitmap(itemWidth, itemHeight,da);
        mat.setValues(new float[]{	//进行变换
        	1.0f,0.0f,0.0f,
        	-0.2f,1.0f,0.0f,
        	0.0f,0.0f,1.0f
        });
	    bm = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), mat, true);
	    da= new BitmapDrawable(bm);
	    source.recycle();
		ivChangeImg.setBackgroundDrawable(da);
		//设置封面开始显示的位置
		this.beginX = beginX;
		this.beginY = beginY;
		createAnimation();	//创建动画；
		
		LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
		lp.setMargins(beginX, beginY, 0, 0);
		lp.gravity = Gravity.AXIS_PULL_BEFORE;
		flChange.setLayoutParams(lp);
		removeAllViews();
		addView(flChange);
		/*Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.fr_open_anim);*/
		
		ivChangeImg.startAnimation(animation2);
		/*Animation anim2 = AnimationUtils.loadAnimation(context,
				R.anim.fr_anim_open);*/
		flChange.startAnimation(animation);
		
	}
	
	public void showItemViewClose (){
		setBackgroundColor(Color.TRANSPARENT);
		createAnimationClose();
		if (animationClose!=null && animationClose2!=null ) {
			ivChangeImg.startAnimation(animationClose2);
			flChange.startAnimation(animationClose);
		}else {
			((BookShelfView)context).setListenerEnableTrue();
		}
		
	}
	
	/** 构建打开动画
	 * 
	 * @date 2011-5-19上午09:18:48
	 * @author Lee Chok Kong
	 */
	private void createAnimation(){
		//窗体要扩大的倍数
		float multiple = 3.8f;
		//窗体需要一定的宽和高
		float tranX = (screenWidth/2 - itemWidth*multiple/2 - beginX)/multiple;
		float tranY = (screenHeight/2 - itemHeight*multiple/2 - beginY)/multiple;
		
		animation = new AnimationSet(false);
		animation2 = new AnimationSet(false);
		
		TranslateAnimation ta = new TranslateAnimation(0, tranX, 0, tranY);
		ta.setDuration(800);	
		animation.addAnimation(ta);	
		
		ScaleAnimation sa = new ScaleAnimation(1.0f, multiple, 1.0f, multiple,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		sa.setDuration(800);
		animation.addAnimation(sa);
		

		ScaleAnimation sa1 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, 0, 0);
		sa1.setDuration(800);
		animation2.addAnimation(sa1);
		//动画监听
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				((BookShelfView)context).setListenerEnableFalse();	//进行锁
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//removeAllViews();
				flChange.setVisibility(View.GONE);
				setBackgroundResource(R.drawable.dim_content);
				//OpenBookFrame.this.animation.reset();
				//animation2.reset();
			}
		});
		
	}
	
	/**
	 * 关闭时候动画
	 * @date 2011-5-20下午03:02:57
	 * @author Lee Chok Kong
	 */
	private void createAnimationClose() {
		//窗体要扩大的倍数
		float multiple = 3.8f;
		//窗体需要一定的宽和高
		float tranX = (screenWidth/2 - itemWidth*multiple/2 - beginX)/multiple;
		float tranY = (screenHeight/2 - itemHeight*multiple/2 - beginY)/multiple;
		
		animationClose = new AnimationSet(false);
		animationClose2 = new AnimationSet(false);
		
		TranslateAnimation ta = new TranslateAnimation(tranX,0, tranY,0);
		ta.setDuration(800);	
		animationClose.addAnimation(ta);	
		
		ScaleAnimation sa = new ScaleAnimation( multiple,1.0f, multiple,1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		sa.setDuration(800);
		animationClose.addAnimation(sa);
		

		ScaleAnimation sa1 = new ScaleAnimation(0.01f, 1.0f, 1.0f, 1.0f, 0, 0);
		sa1.setDuration(800);
		animationClose2.addAnimation(sa1);
		//动画监听
		animationClose.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				setBackgroundColor(Color.TRANSPARENT);
				flChange.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				removeAllViews();
				((BookShelfView)context).sfa.notifyDataSetChanged();
				//setBackgroundColor(Color.WHITE);
				//OpenBookFrame.this.animation.reset();
				//animation2.reset();
				TestClass.println(String.valueOf(((BookShelfView)context).isListenerEnable()));
				((BookShelfView)context).setListenerEnableTrue();	//解锁监听	
			}
		});
	}
}
