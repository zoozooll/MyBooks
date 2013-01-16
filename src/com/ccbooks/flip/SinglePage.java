package com.ccbooks.flip;

import com.ccbooks.myUtil.StringTool;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class SinglePage extends View {

	private Context context;

	private boolean isLeftPage = false;

	private boolean isLeft = false;

	/**draw范围内开始位置的x坐标；*/
	private int x;
	/**开始位置的y坐标；*/
	private int y;
	/**draw范围内的宽*/
	private int width;
	/**draw范围内的高*/
	private int height;

	private Paint paint;

	private int dis = 150;

	// 当前选中的角
	private int chooseCorner = -1;

	public static final int LEFT_UP_CORNER = 0;

	public static final int LEFT_BOTTOM_CORNER = 1;

	public static final int RIGHT_UP_CORNER = 2;

	public static final int RIGHT_BOTTOM_CORNER = 3;

	private static final int RIGHT_OFFSET = 4;

	// A点的坐标是手指目前的坐标
	private float aX = -1;

	private float aY = -1;

	// B点的坐标是A点翻起后跟下一页的交叉点
	private float bX = -1;

	private float bY = -1;

	// C点的坐标是D点翻起后跟下一页的交叉点，可能与D点重复
	private float cX = -1;

	private float cY = -1;

	// D点的坐标是本页的另一个角的点的坐标
	private float dX = -1;

	private float dY = -1;

	// 旋转的角度
	private float angle = 0;

	// 翻动页背面的阴影角度
	private float shadowAngle = 0;

	// 阴影长度
	private float shadowLength = 0;

	// 阴影的坐标x
	private float sdx = 0;

	// 阴影的坐标y
	private float sdy = 0;

	// 是否隐藏层
	//private boolean isMask;

	// 是否是当前显示页
	private int isLookPage;

	// 隐藏层的显示区域
	private Path maskPath;

	// 翻动页的fx坐标
	private float fx;

	// 翻动页的fy坐标
	private float fy;

	// 背景色
	private int bc;

	// 显示的角
	private int cr;

	private BookLayout blo;

	private PageContent content;

	private int i = 7;
	
	//bookName
	private String bookNameContent;
	
	//bookAuthor
	private String bookAuthor;
	
	//mySeekBar 's progress point
	private float myProgress;
	
	//Whether the bookmark is on;
	private boolean onMark;
	
	//What side the seek show
	private boolean isShowAtLeft = true;
	private boolean isShowAtRight = false;
	
	//Whether show the custom view
	private boolean hideViews;
	
	//各类图片属性;
	Bitmap content_btn_bg = BitmapFactory.decodeResource(getResources(),R.drawable.content_btn_bg_bak);
	Bitmap content_btn_catelog = BitmapFactory.decodeResource(getResources(),R.drawable.content_btn_catelog_bak);
	Bitmap content_btn_fullscreen_bak = BitmapFactory.decodeResource(getResources(),R.drawable.content_btn_fullscreen_bak);
	Bitmap btnLight = BitmapFactory.decodeResource(getResources(),R.drawable.content_btn_light_bak);
	Bitmap content_btn_font = BitmapFactory.decodeResource(getResources(),R.drawable.content_btn_font_bak);
	Bitmap content_btn_outmark = BitmapFactory.decodeResource(getResources(),R.drawable.content_btn_outmark_bak);
	Bitmap content_seek = BitmapFactory.decodeResource(getResources(), R.drawable.content_seek_bak);
	Bitmap content_thumb = BitmapFactory.decodeResource(getResources(), R.drawable.content_thumb_bak);
	Bitmap content_btn_onmark = BitmapFactory.decodeResource(getResources(), R.drawable.content_btn_onmark_bak);
    
	
	
	
    public SinglePage(Context context, boolean isLeftPage, int pageNo,
			BookLayout blo, PageContent content,int bc) {
		super(context);
		this.context = context;

		this.isLeftPage = isLeftPage;
		/*
		 * if (((BookContentView)context).portraint==1){ this.isLeftPage =
		 * !isLeftPage; }
		 */
		this.blo = blo;
		this.content = content;
		this.bc = bc;
		init();
	}
	
	//初始化
	private void init(){
		paint = new Paint();
		//bc = Color.WHITE;
		paint.setARGB(255, Color.red(bc), Color.green(bc), Color.blue(bc));
		paint.setAntiAlias(true);
		//isMask = true;
		isLookPage = 1;
		this.setLongClickable(true);
		//this.bookNameContent = 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		BookContentView bcv = (BookContentView) context;
		// 抗锯齿
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		
		if (isLookPage==1) {
			// 如果是遮罩层
			// if(blo.getCurrentFlipPage()!=null&&this==blo.getMaskPage(blo.getCurrentFlipPage())){
			// Log.e("SinglePage", "------->pageNo:"+pageNo);
			// }
			if (maskPath == null)
				return;
			float tx = 0;
			float ty = 0;
			switch (cr) {
			case LEFT_UP_CORNER: {
				tx = fx;
				ty = fy;
				break;
			}
			case RIGHT_UP_CORNER: {
				tx = fx - width;
				ty = fy;
				break;
			}
			case LEFT_BOTTOM_CORNER: {
				tx = fx;
				ty = fy - height;
				break;
			}
			case RIGHT_BOTTOM_CORNER: {
				tx = fx - width;
				ty = fy - height;
				break;
			}
			}
			canvas.clipPath(maskPath);
			canvas.save();
			canvas.rotate(angle, fx, fy);
			paint.setStyle(Style.FILL);
			paint.setARGB(255, Color.red(bc), Color.green(bc), Color.blue(bc));
			canvas.drawRect(tx, ty, tx + width, ty + height, paint);
			
			//显示模拟自定义控件
			float seekDis = 0;
			paint.setTextAlign(Align.CENTER);
			paint.setColor(0xFF623E00);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setTextSize(17f);
			if (context instanceof BookContentView
					&& ((BookContentView) context).portraint == 2) {
				if (isLeftPage){
					
					if (!hideViews){
						canvas.drawBitmap(content_btn_bg, tx+1, ty-3, paint);
						canvas.drawBitmap(content_btn_catelog, tx+85, ty-3, paint);
						canvas.drawBitmap(content_btn_fullscreen_bak, tx+139, ty-3, paint);
						
						while(true){
							if (tx+85+seekDis> tx+width){
								break;
							}
							canvas.drawBitmap(content_seek, tx+85+seekDis, ty+height-31, paint);
							seekDis+=15.0f;
						}
						if (isLeftPage && isShowAtLeft){
							canvas.drawBitmap(content_thumb, tx+70+myProgress, ty+height-37, paint);
							//canvas.drawRect(left, top, right, bottom, paint)
						}
						canvas.drawText("书库", tx+content_btn_bg.getWidth()/2, ty+3+content_btn_bg.getHeight()/2, paint);
					}else{
						canvas.drawText("作者：unknow", tx+width/2, ty+32, paint);
					}
					
				}else {
					if (!hideViews){
						canvas.drawBitmap(btnLight, tx+220, ty-3, paint);
						canvas.drawBitmap(content_btn_font, tx+280, ty-3, paint);
						if (!onMark) {
							canvas.drawBitmap(content_btn_outmark, tx+337, ty-5, paint);
						} else {
							canvas.drawBitmap(content_btn_onmark, tx+337, ty-5, paint);
						}
						
						while(true){
							if (tx-3+seekDis>= tx+width-100){
								break;
							}
							canvas.drawBitmap(content_seek, tx-3+seekDis, ty+height-31, paint);
							seekDis+=15.0f;
							
						}
						if (!isLeftPage && isShowAtRight){
							canvas.drawBitmap(content_thumb, tx-content_thumb.getWidth()+myProgress+RIGHT_OFFSET, ty+height-37, paint);
						}
					} else {
						canvas.drawText(StringTool.omitString(bcv.book.bookname, 30) , tx+width/2, ty+32, paint);
					}
					
				}
				content.setPosition(tx, ty);
				
				content.draw(canvas);
			}
			/*
			 * paint.setStyle(Style.STROKE); paint.setARGB(255,0,0,0);
			 * canvas.drawRect(tx,ty,tx+width,ty+height, paint);
			 */
			
			canvas.restore();
			canvas.save();
			canvas.rotate(shadowAngle, sdx, sdy);
			paint.setStyle(Style.FILL);
			shadowLength = (float) Math.sqrt(width * width + height * height);
			
			// 竖屏，仅仅显示右边部分；
			if (((BookContentView) context).portraint == 1) {
				if (sdx <= width / 3) {
					isLeft = true;
				} else {
					isLeft = false;
				}
				Shader linearShader = new LinearGradient(sdx - 30, sdy, sdx,
						sdy, Color.argb(0, 0, 0, 0), Color.argb(33, 0, 0, 0),
						TileMode.CLAMP);
				paint.setShader(linearShader);
				canvas.drawRect(sdx - 30, sdy, sdx, sdy + shadowLength, paint);
				Shader linearShader1 = new LinearGradient(sdx, sdy, sdx + 30,
						sdy, Color.argb(33, 0, 0, 0), Color.argb(0, 0, 0, 0),
						TileMode.CLAMP);
				paint.setShader(linearShader1);
				canvas.drawRect(sdx, sdy, sdx + 30, sdy + shadowLength, paint);

			}

			// 横屏，左右两边分别显示；
			if (((BookContentView) context).portraint == 2) {
				if (isLeftPage){
					
					//canvas.drawBitmap(book_page_bg_h_l, x + width - 15, y, paint);
					Shader linearShader = new LinearGradient(sdx-7,sdy,sdx,sdy,Color.argb(10, 0, 0, 0),Color.argb(44, 0, 0, 0),TileMode.CLAMP);
					paint.setShader(linearShader);
					canvas.drawRect(sdx-7, sdy, sdx, sdy+shadowLength, paint);
					Shader linearShader1 = new LinearGradient(sdx-15,sdy,sdx-7,sdy,Color.argb(0, 0, 0, 0),Color.argb(10, 0, 0, 0),TileMode.CLAMP);
					paint.setShader(linearShader1);
					canvas.drawRect(sdx-15, sdy, sdx-7, sdy+shadowLength, paint);
				}else {
					Shader linearShader = new LinearGradient(sdx,sdy,sdx+7,sdy,Color.argb(44, 0, 0, 0),Color.argb(10, 0, 0, 0),TileMode.CLAMP);
					paint.setShader(linearShader);
					canvas.drawRect(sdx, sdy, sdx+7, sdy+shadowLength, paint);
					Shader linearShader1 = new LinearGradient(sdx+7,sdy,sdx+15,sdy,Color.argb(10, 0, 0, 0),Color.argb(0, 0, 0, 0),TileMode.CLAMP);
					paint.setShader(linearShader1);
					canvas.drawRect(sdx+7, sdy, sdx+15, sdy+shadowLength, paint);
					//canvas.drawBitmap(book_page_bg_h_r, x, y, paint);
				}
			}

			paint.setShader(null);
			canvas.restore();
		} else {// 显示层
			Path backPath = null;
			if (aX != -1 && aY != -1) {
				backPath = new Path();
				backPath.moveTo(aX, aY);
				backPath.lineTo(bX, bY);
				backPath.lineTo(cX, cY);
				if (cX != dX || cY != dY) {
					backPath.lineTo(dX, dY);
				}
				backPath.lineTo(aX, aY);
				float px = -1;
				float py = -1;
				float ox = -1;
				float oy = -1;
				float ex = -1;
				float ey = -1;
				switch (chooseCorner) {
				case RIGHT_UP_CORNER: {
					px = x;
					py = y;
					ox = x;
					oy = y + height;
					ex = x + width;
					ey = y + height;
					break;
				}
				case RIGHT_BOTTOM_CORNER: {
					px = x;
					py = y + height;
					ox = x;
					oy = y;
					ex = x + width;
					ey = y;
					break;
				}
				case LEFT_UP_CORNER: {
					px = width + x;
					py = y;
					ox = width + x;
					oy = height + y;
					ex = x;
					ey = y + height;
					break;
				}
				case LEFT_BOTTOM_CORNER: {
					px = width + x;
					py = height + y;
					ox = width + x;
					oy = y;
					ex = x;
					ey = y;
					break;
				}
				}
				maskPath = new Path();
				maskPath.moveTo(bX, bY);
				maskPath.lineTo(px, py);
				maskPath.lineTo(ox, oy);
				if (cX == dX && cY == dY) {
					maskPath.lineTo(ex, ey);
				}
				maskPath.lineTo(cX, cY);
				maskPath.lineTo(bX, bY);
			}
			if (maskPath != null) {
				canvas.clipPath(maskPath);
				canvas.save();
			}
			paint.setStyle(Style.FILL);
			paint.setARGB(255, Color.red(bc), Color.green(bc), Color.blue(bc));
			
			//显示边框；
			/*canvas.drawRect(x,y,x+width,y+height, paint);
			paint.setARGB(255,0,0,0); paint.setStyle(Style.STROKE);*/
			
			content.setPosition(x, y);
			canvas.drawRect(x, y, x + width, y + height, paint);
			
			//以图片形式显示按钮；
			
			float seekDis = 0;
			paint.setTextSize(17f);
			paint.setTextAlign(Align.CENTER);
			paint.setColor(0xFF623E00);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			if (((BookContentView) context).portraint == 1){	//竖屏，做一页显示
				
				if (!hideViews){	//如果是显示控件的状态；
					//配置系数，draw图片时候的距离
					int btnMarkLeft = width+x-content_btn_outmark.getWidth()-10;
					int btnFontLeft = btnMarkLeft-content_btn_font.getWidth()-18;
					int btnLightLeft = btnFontLeft - btnLight.getWidth() - 18;
					
					canvas.drawBitmap(content_btn_bg , 5, y+3, paint);
					canvas.drawText("书库", x+5+content_btn_bg.getWidth()/2, y+35, paint);
					canvas.drawBitmap(content_btn_catelog, 93, y+3, paint);
					canvas.drawBitmap(content_btn_fullscreen_bak, 150, y+3, paint);
					//TODO 亮度控件图标的数值，在480*800的分辨率下为300,y（竖屏），；
					canvas.drawBitmap(btnLight, btnLightLeft, y+3, paint);
					//TODO 字体控件图标的数值，在480*800的分辨率下为358,y（竖屏），；
					canvas.drawBitmap(content_btn_font, btnFontLeft, y+3, paint);
					if (!onMark){
						//TODO 一下为draw书签图标的函数,在480*800的分辨率下为418,y（竖屏），下边同上调法
						canvas.drawBitmap(content_btn_outmark, btnMarkLeft, y, paint);
					} else {
						canvas.drawBitmap(content_btn_onmark, btnMarkLeft, y, paint);
					}
					while(true){
						seekDis+=15.0f;
						if (x+65+seekDis>= width-70){
							break;
						}
						canvas.drawBitmap(content_seek, x+65+seekDis, y+height-33, paint);
					}
					canvas.drawBitmap(content_thumb, x+60+myProgress, y+height-39, paint);
				}else {	//如果是显示文字的状态；
					canvas.drawText(StringTool.omitString(bcv.book.bookname, 30), width/2, y+35, paint);
				}
			} else if (((BookContentView) context).portraint == 2) {
				
				if (isLeftPage){	//左边页面
					if (!hideViews){
						canvas.drawBitmap(content_btn_bg, x+1, y-3, paint);
						canvas.drawBitmap(content_btn_catelog, x+85, y-3, paint);
						canvas.drawBitmap(content_btn_fullscreen_bak, x+139, y-3, paint);
						
						paint.setTextSize(17f);
						canvas.drawText("书库", x+content_btn_bg.getWidth()/2, y+3+content_btn_bg.getHeight()/2, paint);
						while(true){
							if (x+85+seekDis> x+width-10){
								break;
							}
							canvas.drawBitmap(content_seek, x+85+seekDis, y+height-31, paint);
							seekDis+=15.0f;
						}
						if (isLeftPage && isShowAtLeft){
							//canvas.drawBitmap(content_thumb, x+70+myProgress, y+height-37, paint);
							int w = 0;
							if (width-70-myProgress>content_thumb.getWidth()){
								w=content_thumb.getWidth();
							}else{
								w=(int)(width-70-myProgress);
							}
							drawImage(canvas, content_thumb, (int)(x+70+myProgress), y+height-37, w, content_thumb.getHeight(), 0, 0);
						}
					}else{
						canvas.drawText("作者：unknow", x+width/2, y+32, paint);
					}
					
				}else {
					//右边页面
					if (!hideViews){
						int btnMarkMarginRight = blo.screenWidth-x+width-5-content_btn_outmark.getWidth();
						int btnFontRight = btnMarkMarginRight-content_btn_font.getWidth()-18;
						int btnLightRight = btnFontRight-btnLight.getWidth()-18;
						//TODO 原数值为x+220
						canvas.drawBitmap(btnLight, btnLightRight, y-3, paint);
						//TODO 原数值为x+280
						canvas.drawBitmap(content_btn_font, btnFontRight, y-3, paint);
						if (!onMark) {
							//TODO 原数值为x+337
							canvas.drawBitmap(content_btn_outmark, btnMarkMarginRight, y-5, paint);
						} else {
							canvas.drawBitmap(content_btn_onmark, btnMarkMarginRight, y-5, paint);
						}
						
						while(true){
							if (x+seekDis-3>= x+width-100){
								break;
							}
							canvas.drawBitmap(content_seek, x+seekDis-3, y+height-31, paint);
							seekDis+=15.0f;
						}
						if (!isLeftPage && isShowAtRight){
							//canvas.drawBitmap(content_thumb, x-35+myProgress, y+height-37, paint);
							int w = 0;
							if(myProgress>content_thumb.getWidth()){
								w=content_thumb.getWidth();
								drawImage(canvas, content_thumb, (int)(x-content_thumb.getWidth()+RIGHT_OFFSET+myProgress), (int)y+height-37, w, content_thumb.getHeight(), 0, 0);
							}else{
								w=(int)myProgress+RIGHT_OFFSET;
								drawImage(canvas, content_thumb, x, (int)y+height-37, w, content_thumb.getHeight(), content_thumb.getWidth()-w, 0);
							}
							
							//paint.setARGB(255, 255, 255, 255);
							//canvas.drawRect(x-40, y+height-40, x, y+height, paint);
						}
					}else {
						canvas.drawText(StringTool.omitString(bcv.book.bookname, 30), x+width/2, y+32, paint);
					}
				}
			}
			content.draw(canvas);

			// 显示书轴阴影
			paint.reset();
			paint.setStyle(Style.FILL);
			if (!blo.isFirstOrLastPage(this)
					&& ((BookContentView) context).portraint == 2) {
				if (isLeftPage) {			 
					Shader linearShader = new LinearGradient(x + width - 7, y,
							x + width, y, Color.argb(10, 0, 0, 0), Color.argb(
									44, 0, 0, 0), TileMode.CLAMP);
					paint.setShader(linearShader);
					canvas.drawRect(x + width - 7, y, x + width, y + height,
							paint);
					Shader linearShader1 = new LinearGradient(x + width - 15, y,
							x + width-7, y, Color.argb(0, 0, 0, 0), Color.argb(
									10, 0, 0, 0), TileMode.CLAMP);
					paint.setShader(linearShader1);
					canvas.drawRect(x + width - 15, y, x + width-7, y + height,
							paint);
			       //canvas.drawBitmap(book_page_bg_h_l, x + width - 15, y, paint);
				} else {
					Shader linearShader = new LinearGradient(x, y, x + 7, y,
							Color.argb(44, 0, 0, 0), Color.argb(10, 0, 0, 0),
							TileMode.CLAMP);
					paint.setShader(linearShader);
					canvas.drawRect(x, y, x + 7, y + height, paint);
					Shader linearShader1 = new LinearGradient(x+7, y, x + 15, y,
							Color.argb(10, 0, 0, 0), Color.argb(0, 0, 0, 0),
							TileMode.CLAMP);
					paint.setShader(linearShader1);
					canvas.drawRect(x+7, y, x + 15, y + height, paint);
				//	canvas.drawBitmap(book_page_bg_h_r, x,y, paint);
				}
				paint.setShader(null);
			}

			if (maskPath != null) {
				canvas.restore();
			}
			if (backPath != null) {
				float sdx = -1;
				float sdy = -1;
				if (bY > cY) {
					sdy = cY;
					sdx = cX;
				} else {
					sdy = bY;
					sdx = bX;
				}
				blo.flipPage(this, backPath, angle, shadowAngle, aX, aY, sdx,
						sdy, chooseCorner);
			}

		}
	}
	

	/**
	 * 显示反页遮罩层的区域
	 * 
	 * @param path
	 * @param angle
	 * @param x
	 * @param y
	 */
	public void onMaskPathDraw(Path path, float angle, float shadowAngle,
			float x, float y, float sdx, float sdy, int chooseCorner) {
		this.maskPath = path;
		this.angle = angle;
		this.shadowAngle = shadowAngle;
		this.fx = x;
		this.fy = y;
		this.sdx = sdx;
		this.sdy = sdy;
		switch (chooseCorner) {
		case LEFT_UP_CORNER: {
			cr = RIGHT_UP_CORNER;
			break;
		}
		case RIGHT_UP_CORNER: {
			cr = LEFT_UP_CORNER;
			break;
		}
		case LEFT_BOTTOM_CORNER: {
			cr = RIGHT_BOTTOM_CORNER;
			break;
		}
		case RIGHT_BOTTOM_CORNER: {
			cr = LEFT_BOTTOM_CORNER;
			break;
		}
		}
		postInvalidate();
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
		content.setSize(width, height);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		fx = x;
		fy = y;
	}

	public void setBackgroundColor(int color) {
		this.bc = 0x0000ff;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		BookContentView bcv = (BookContentView)context;
		if(-1 ==bcv.judgeFocus(bcv.BCVFOCUS,0)){
			return false;
		}
		
		if (isLookPage!=2 || blo.isAutoFlip()) {
			return false;
		}
		int act = event.getAction();
		float hx = event.getX();
		float hy = event.getY();
		switch (act) {
		case MotionEvent.ACTION_DOWN: {
			if (isNearCorner(hx, hy) && isEnableChoose(hx, hy)) {
				// 判断条件：竖屏，且从左边往右翻页,把底层内容切换
				if (((BookContentView) context).portraint == 1) {
					if (chooseCorner == LEFT_UP_CORNER
							|| chooseCorner == LEFT_BOTTOM_CORNER) {
						blo.leftDownPage.setVisibility(View.VISIBLE);
						blo.rightDownPage.setVisibility(View.GONE);
					} else {
						
						blo.leftDownPage.setVisibility(View.GONE);
						blo.rightDownPage.setVisibility(View.VISIBLE);
					}
				}

				// 初始化翻页的角度
				aX = hx;
				aY = hy;
				calculate();
				postInvalidate();

				blo.setCurrentFlipPage(this);
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (chooseCorner != -1 && isEnableChoose(hx, hy)) {
				aX = hx;
				aY = hy;
				calculate();
				postInvalidate();
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			if (chooseCorner != -1 && aX!=-1 && aY!=-1) {
				successOrResetPage();
			}
			break;
		}
		}
		if (chooseCorner == -1) {
			return false;
		}
		return true;
	}

	// 判断是否在页角附近
	private boolean isNearCorner(float hx, float hy) {

		BookContentView bcv = (BookContentView) context;
		// 超出边界不能响应;
		if (bcv.portraint == 2){
			if (hx > x+width-30 || hy > y+height-20) {
				return false;
			}
		} else {
			if (hx > x+width-15 || hy > y+height-10) {
				return false;
			}
		}
		
		
		// 左上角
		int lux = x;
		int luy = y;

		// 左下角
		int lbx = x;
		int lby = y + height;

		// 右上角
		int rux = x + width;
		int ruy = y;

		// 右下角
		int rbx = x + width;
		int rby = y + height;
		int[][] pd = { { lux, luy }, { lbx, lby }, { rux, ruy }, { rbx, rby } };
		chooseCorner = -1;
		if (((BookContentView) context).portraint == 1) {
			if (hx <= width / 3) {
				isLeft = true;
			} else {
				isLeft = false;
				isLeftPage = true;
			}
			for (int i = 0; i < pd.length; i++) {
				// 计算是否在页角附近，并返回是哪个页角
				if (isLeft) {
					if (i >= 2)
						continue;
				} else {
					if (i < 2)
						continue;
				}

				if ((pd[i][0] - hx) * (pd[i][0] - hx) + (pd[i][1] - hy)
						* (pd[i][1] - hy) <= dis * dis) {
					chooseCorner = i;
				}
			}

		}

		if (((BookContentView) context).portraint == 2) {
			for (int i = 0; i < pd.length; i++) {
				// 计算是否在页角附近，并返回是哪个页角
				if (isLeftPage) {
					if (i >= 2)
						continue;
				} else {
					if (i < 2)
						continue;
				}
				if ((pd[i][0] - hx) * (pd[i][0] - hx) + (pd[i][1] - hy)
						* (pd[i][1] - hy) <= dis * dis) {
					chooseCorner = i;

				}

			}
		}

		// 需要判断是否开头页，然后翻页
		if (chooseCorner != -1) {
			if (bcv.markPoint == 0
					&& (chooseCorner == LEFT_UP_CORNER || chooseCorner == LEFT_BOTTOM_CORNER)) {
				bcv.lastPageShowing();
				chooseCorner = -1;
				return false;
			} else if (bcv.nextPoint == -1
					&& (chooseCorner == RIGHT_UP_CORNER || chooseCorner == RIGHT_BOTTOM_CORNER)) {
				bcv.lastPageShowing();
				chooseCorner = -1;
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	// 判断是否是有效的翻页坐标
	private boolean isEnableChoose(float hx, float hy) {
		boolean flag = false;
		switch (chooseCorner) {
		case LEFT_UP_CORNER: {
			if ((hx - (width + x)) * (hx - (width + x)) + (hy - y) * (hy - y) <= width
					* width
					&& (hx - (width + x)) * (hx - (width + x))
							+ (hy - (height + y)) * (hy - (height + y)) <= width
							* width + height * height) {
				flag = true;
			}
			break;
		}
		case LEFT_BOTTOM_CORNER: {
			if ((hx - (width + x)) * (hx - (width + x)) + (hy - (height + y))
					* (hy - (height + y)) <= width * width
					&& (hx - (width + x)) * (hx - (width + x)) + (hy - y)
							* (hy - y) <= width * width + height * height) {
				flag = true;
			}
			break;
		}
		case RIGHT_UP_CORNER: {
			if ((hx - x) * (hx - x) + (hy - y) * (hy - y) <= width * width
					&& (hx - x) * (hx - x) + (hy - (height + y))
							* (hy - (height + y)) <= width * width + height
							* height) {
				flag = true;
			}
			break;
		}
		case RIGHT_BOTTOM_CORNER: {
			if ((hx - x) * (hx - x) + (hy - (height + y)) * (hy - (height + y)) <= width
					* width
					&& (hx - x) * (hx - x) + (hy - y) * (hy - y) <= width
							* width + height * height) {
				flag = true;
			}
			break;
		}
		}
		// Log.e("Page","isEnableChoose "+flag);
		return flag;
	}

	// 判断是否成功翻页还是取消翻页
	private void successOrResetPage() {
		float sx = -1;
		float sy = -1;
		float fx = -1;
		float fy = -1;

		// 横屏手松开的时候，如果另一侧的角翻动幅度更大的话，以那个角作为A点，启动自动翻书动作
		if (((BookContentView) context).portraint == 2) {
			if (isLeftPage) {
				if (dX > aX) {
					aX = dX;
					aY = dY;
					if (chooseCorner == LEFT_UP_CORNER) {
						chooseCorner = LEFT_BOTTOM_CORNER;
					} else {
						chooseCorner = LEFT_UP_CORNER;
					}
				}
			} else {
				if (dX < aX) {
					aX = dX;
					aY = dY;
					if (chooseCorner == RIGHT_UP_CORNER) {
						chooseCorner = RIGHT_BOTTOM_CORNER;
					} else {
						chooseCorner = RIGHT_UP_CORNER;
					}
				}
			}
		}
		// 竖屏手松开的时候，如果另一侧的角翻动幅度更大的话，以那个角作为A点，启动自动翻书动作
		if (((BookContentView) context).portraint == 1) {
			if (isLeft) {
				if (dX > aX) {
					aX = dX;
					aY = dY;
					if (chooseCorner == LEFT_UP_CORNER) {
						chooseCorner = LEFT_BOTTOM_CORNER;
					} else {
						chooseCorner = LEFT_UP_CORNER;
					}
				}
			} else {
				if (dX < aX) {
					aX = dX;
					aY = dY;
					if (chooseCorner == RIGHT_UP_CORNER) {
						chooseCorner = RIGHT_BOTTOM_CORNER;
					} else {
						chooseCorner = RIGHT_UP_CORNER;
					}
				}
			}
		}
		calculate();

		// 计算成功或失败翻页后，被触碰的页角需要达到的坐标
		switch (chooseCorner) {
		case LEFT_UP_CORNER: {
			sx = x + 2 * width;
			sy = y;
			fx = x;
			fy = y;
			break;
		}
		case LEFT_BOTTOM_CORNER: {
			sx = x + 2 * width;
			sy = y + height;
			fx = x;
			fy = y + height;
			break;
		}
		case RIGHT_UP_CORNER: {
			sx = x - width;
			sy = y;
			fx = x + width;
			fy = y;
			break;
		}
		case RIGHT_BOTTOM_CORNER: {
			sx = x - width;
			sy = y + height;
			fx = x + width;
			fy = y + height;
			break;
		}
		}
		// 横屏往左翻
		if (((BookContentView) context).portraint == 2) {

			if (isLeftPage && (aX >= x + width / 3 )) {
				autoFlipPageToLeft(sx, sy, true);
			} else if (!isLeftPage
					&& (aX <= x + width/ 3 )) {
				autoFlipPageToRight(sx, sy, true);
			} else {
				autoFlipPage(fx, fy, false);
			}
		}

		// 竖屏往左翻
		if (((BookContentView) context).portraint == 1) {
			if (isLeft && (aX >= x + width / 3 )) {
				autoFlipPageToLeft(sx, sy, true);

			} else if (!isLeft && (aX <= x + width / 3 )) {

				autoFlipPageToRight(sx, sy, true);
			} else {
				autoFlipPage(fx, fy, false);
			}
		}
	}

	/**
	 * 强制自动翻页
	 * 
	 * @param flag
	 *            : 转屏：-1 不翻页；1 向左；2 向右
	 * */
	public void successOrResetPage(int flag) {
		if(isLookPage==1||blo.isAutoFlip()){
			return ;
		}
		if (flag != -1) {
			float sx = -1;
			float sy = -1;
			float fx = -1;
			float fy = -1;
			switch (flag) {
			case 1:
				if(((BookContentView) context).portraint == 2){
					aX = 750;
					aY = 385;
				}else{
					aX = 390;
					aY = 700;
				}
				chooseCorner = RIGHT_BOTTOM_CORNER;
				break;
			case 2:
				if(((BookContentView) context).portraint == 2){
					aX = 30;
					aY = 385;
				}else{
					aX = 120;
					aY = 700;
				}
				chooseCorner = LEFT_BOTTOM_CORNER;
				break;
			default:
				break;
			}
			
			if (((BookContentView) context).portraint == 1) {
				if (chooseCorner == LEFT_UP_CORNER
						|| chooseCorner == LEFT_BOTTOM_CORNER) {
					blo.leftDownPage.setVisibility(View.VISIBLE);
					blo.rightDownPage.setVisibility(View.GONE);
				} else {
					blo.leftDownPage.setVisibility(View.GONE);
					blo.rightDownPage.setVisibility(View.VISIBLE);
				}
			}
			switch (chooseCorner) {
			case LEFT_UP_CORNER: {
				sx = x + 2 * width;
				sy = y;
				fx = x;
				fy = y;
				break;
			}
			case LEFT_BOTTOM_CORNER: {
				sx = x + 2 * width;
				sy = y + height;
				fx = x;
				fy = y + height;
				break;
			}
			case RIGHT_UP_CORNER: {
				sx = x - width;
				sy = y;
				fx = x + width;
				fy = y;
				break;
			}
			case RIGHT_BOTTOM_CORNER: {
				sx = x - width;
				sy = y + height;
				fx = x + width;
				fy = y + height;
				break;
			}
			}

			if (flag == 1) {
				autoFlipPageToRight(sx, sy, true);
			} else {
				autoFlipPageToLeft(sx, sy, true);
			}
		}
//		clearPosition();
	}

	// 当在翻页过程中，手松开页角的时候，自动执行翻页动作，让翻页更真实
	private void autoFlipPage(final float tx, final float ty,
			final boolean isSuccessFlip) {
		blo.setAutoFlip(true);
		new Thread() {
			public void run() {
				int count = 1;
				boolean flag = false;
				while (true) {
					try {
						flag = autoMovePageCorner(tx, ty, count++);
						Thread.sleep(80);
						if (flag)
							break;
					} catch (InterruptedException e) {
						e.printStackTrace();

					}
				}
				// 清除翻页参数
				chooseCorner = -1;
				clearPosition();
				if (isSuccessFlip) {
					handler.post(new Runnable() {
						public void run() {
							blo.setCurrentFlipPage(null);
							blo.successFlipPage(SinglePage.this, true);
						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
							blo.setCurrentFlipPage(null);
							blo.successFlipPage(SinglePage.this, false);
						}
					});
				}
				blo.setAutoFlip(false);
			}
		}.start();
	}

	private void autoFlipPageToLeft(final float tx, final float ty,
			final boolean isSuccessFlip) {
		blo.setAutoFlip(true);
		new Thread() {
			public void run() {
				int count = 1;
				boolean flag = false;
				while (true) {
					try {
						flag = autoMovePageCorner(tx, ty, count++);
						Thread.sleep(50);
						if (flag)
							break;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// 清除翻页参数
				chooseCorner = -1;
				clearPosition();
				if (isSuccessFlip) {
					handler.post(new Runnable() {
						public void run() {
							blo.setCurrentFlipPage(null);
							blo.successFlipPageLeft(SinglePage.this, true);

						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
							blo.setCurrentFlipPage(null);
							blo.successFlipPage(SinglePage.this, false);
						}
					});
				}
				blo.setAutoFlip(false);
			}
		}.start();
	}

	private void autoFlipPageToRight(final float tx, final float ty,
			final boolean isSuccessFlip) {
		blo.setAutoFlip(true);
		new Thread() {
			public void run() {
				int count = 1;
				boolean flag = false;
				while (true) {
					try {
						flag = autoMovePageCorner(tx, ty, count++);
						Thread.sleep(50);
						if (flag)
							break;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// 清除翻页参数
				chooseCorner = -1;
				clearPosition();
				if (isSuccessFlip) {
					handler.post(new Runnable() {
						public void run() {
							blo.setCurrentFlipPage(null);
							blo.successFlipPageRight(SinglePage.this, true);
						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
							blo.setCurrentFlipPage(null);
							blo.successFlipPage(SinglePage.this, false);
						}
					});
				}
				blo.setAutoFlip(false);
			}
		}.start();
	}

	private Handler handler = new Handler();

	// 线程内部，自动移动页角
	private boolean autoMovePageCorner(float tx, float ty, int num) {
		boolean isFinish = false;
		if ((aX - tx) * (aX - tx) + (aY - ty) * (aY - ty) == 0) {
			isFinish = true;
		} else {
			handler.post(new Runnable() {
				public void run() {
					postInvalidate();
				}
			});
			aX = aX - num * (aX - tx) / 10;
			aY = aY - num * (aY - ty) / 10;
			calculate();
		}
		return isFinish;
	}

	// 根据A点计算其余点
	private void calculate() {
		float px = -1;
		float py = -1;
		float ox = -1;
		float oy = -1;
		switch (chooseCorner) {
		case LEFT_UP_CORNER: {
			px = x;
			py = y;
			ox = x;
			oy = y + height;
			bY = y;
			cX = x;
			cY = y + height;
			break;
		}
		case LEFT_BOTTOM_CORNER: {
			px = x;
			py = y + height;
			ox = x;
			oy = y;
			bY = y + height;
			cX = x;
			cY = y;
			break;
		}
		case RIGHT_UP_CORNER: {
			px = width + x;
			py = y;
			ox = width + x;
			oy = height + y;
			bY = y;
			cX = x + width;
			cY = y + height;
			break;
		}
		case RIGHT_BOTTOM_CORNER: {
			px = width + x;
			py = height + y;
			ox = width + x;
			oy = y;
			bY = y + height;
			cX = x + width;
			cY = y;
			break;
		}
		}
		if (aY == py) {
			bY = aY;
			bX = (aX + px) / 2;
			dY = oy;
			dX = aX;
			cX = (aX + ox) / 2;
			cY = oy;
			angle = 0;
			return;
		}
		MNLine lineOne = MNLine.initLine(aX, aY, px, py);
		MNLine lineTwo = lineOne.getPBLine((aX + px) / 2, (aY + py) / 2);
		bX = lineTwo.getXbyY(bY);
		float tempCY = lineTwo.getYbyX(cX);
		if (tempCY >= y && tempCY <= y + height) {// 在左或右边交叉，是三角型，
			cY = tempCY;
			dX = cX;
			dY = cY;
		} else {
			cX = lineTwo.getXbyY(cY);
			// 将直线平移，经过页的另一个角
			lineOne.change(ox, oy);
			float[] xAndY = lineOne.getCross(lineTwo);
			dX = 2 * xAndY[0] - ox;
			dY = 2 * xAndY[1] - oy;
		}
		if (((Float) bX).isNaN() || ((Float) bY).isNaN()
				|| ((Float) cX).isNaN() || ((Float) cY).isNaN()
				|| ((Float) dX).isNaN() || ((Float) dY).isNaN()) {
			clearPosition();
		} else {
			MNLine lineThree = MNLine.initLine(aX, aY, dX, dY);
			angle = (float) (Math.atan(lineThree.getA()) * 180 / Math.PI);
			if (angle > 0) {
				if (aX <= dX && aY <= dY && chooseCorner == RIGHT_BOTTOM_CORNER) {
					angle = 90 + angle;
				} else if (aX >= dX && aY >= dY
						&& chooseCorner == LEFT_UP_CORNER) {
					angle = 90 + angle;
				} else {
					angle = angle - 90;
				}
			} else {
				if (aX <= dX && aY >= dY && chooseCorner == RIGHT_UP_CORNER) {
					angle = -90 + angle;
				} else if (aX >= dX && aY <= dY
						&& chooseCorner == LEFT_BOTTOM_CORNER) {
					angle = -90 + angle;
				} else {
					angle = angle + 90;
				}
			}
			shadowAngle = (float) (Math.atan(lineTwo.getA()) * 180 / Math.PI);
			if (shadowAngle > 0) {
				shadowAngle = shadowAngle - 90;
			} else {
				shadowAngle = shadowAngle + 90;
			}
		}
	}
	/**模拟进度条拖动状态*/
	public void mySeekBarProgress(int percent){
		
		if (((BookContentView)context).portraint == 2){
			/*转动的系数，
			 * 本公式的计算方法是
			 * 屏幕宽度-seekbar的外距离左边值 -外距离右边值-内padding值×2
			 * */
			int lenght= blo.screenWidth-(blo.screenWidth/2/16+75)*2-15*2;
			if (percent>=0 && percent<5400 ){
				isShowAtLeft = true;
				if (isLeftPage)
					myProgress = lenght*percent/10000;	
			}else {
				isShowAtLeft = false;
			}
			
			if (percent>=4800  && percent<=10000) {
				isShowAtRight = true;
				if (!isLeftPage)
				myProgress = lenght* (percent-4800)/10000;
			} else {
				isShowAtRight = false;
			}
		}else if (((BookContentView)context).portraint == 1){
			//TODO 当中的函数从屏幕的宽来入手，其中的65是seekbar距离左右的边界，而15则是内部padding的距离。
			//希望下面的方法可以从这里开始入手
			//在480*800情况下是320
			myProgress  = (blo.screenWidth-65*2-15*2)*percent/10000;
		}
		postInvalidate(0,height-100,width*2,y+height);
		
		
	}
	/**模拟按钮变化*/
	public void myBookMarkOnClick(boolean onMark){
		this.onMark = onMark;
		postInvalidate();
	}
	
	//设置隐藏按钮；
	public void setHideViews(boolean hideViews) {
		this.hideViews = hideViews;
		postInvalidate();
	}
	
	//设置屏幕颜色
	public void setBcColor(int color){
		this.bc= color;
		postInvalidate();
	}

/*	public void setMaskPage(boolean flag) {
		isMask = flag;
	}*/

	public void setLookPage(int flag) {
		isLookPage = flag;
		//isMask = !flag;
	}
	
    /** 
     *  图片剪裁区域；
     * @param canvas 
     * @param mBitmap 
     * @param x 屏幕上的x坐标
     * @param y 屏幕上的y坐标
     * @param w 要绘制的图片的宽度
     * @param h 要绘制的图片的高度
     * @param bx 图片上的x坐标
     * @param by 图片上的y坐标
     */  
    public void drawImage(Canvas canvas, Bitmap mBitmap, int x, int y, int w,  
            int h, int bx, int by) {  
        Rect src = new Rect();// 图片裁剪区域  
        Rect dst = new Rect();// 屏幕裁剪区域  
        src.left = bx;  
        src.top = by;  
        src.right = bx + w;  
        src.bottom = by + h;  
  
        dst.left = x;  
        dst.top = y;  
        dst.right = x + w;  
        dst.bottom = y + h;  
  
        canvas.drawBitmap(mBitmap, src, dst, paint);  
        src = null;  
        dst = null;  
    }  
  

	public void reset() {
		clearPosition();
		angle = 0;
		shadowAngle = 0;
		shadowLength = 0;
		fx = x;
		fy = y;
		sdx = -1;
		sdy = -1;
		cr = -1;
		maskPath = null;
		//isMask = true;
		isLookPage = 1;
	}

	public PageContent getPageContent() {
		return content;
	}

	public void setContent(PageContent content) {
		this.content = content;
	}
	
	public String getBookNameContent() {
		return bookNameContent;
	}

	public void setBookNameContent(String bookNameContent) {
			this.bookNameContent = bookNameContent;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	
	public int getBc() {
		return bc;
	}

	public void setBc(int bc) {
		this.bc = bc;
	}

	private void clearPosition() {
		aX = -1;
		aY = -1;
		bX = -1;
		bY = -1;
		cX = -1;
		cY = -1;
		dX = -1;
		dY = -1;
	}

}
