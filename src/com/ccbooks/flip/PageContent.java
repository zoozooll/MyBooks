package com.ccbooks.flip;

import java.util.List;

import com.ccbooks.util.StringConfig;
import com.ccbooks.view.BookContentView;
import com.chinachip.ccbooks.core.TextSizeUtil;
import com.chinachip.ccbooks.engine.FontUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PageContent extends View{
	
	private Context context;
	
	private Bitmap img;
	
	private Paint paint;
	
	private int imgResId;
	
	private BookLayout blo;
	private boolean isLeftPage = false;
	
	//字体大小;
	private float textSize;
	
	//内容ID
	private int contentId = -1;
	
	//是否是封面
	private boolean isCover = false;;
	
	//是否是内容页
	private boolean isContentPage = false;
	

	private int x;
	
	private int y;
	
	/**paddingLeft*/
	private static final int PADDING_X = 30;
	private static final int PADDING_Y = 70;
	private static final int PADDING_X_VIRI = 35;
	private static final int PADDING_Y_VIRI = 75;

	private int width;
	
	private int height;
	
	
	//drawbitmap用到的内容；
	public static final int TRANS_ROT180 = 3;
	public static final int TRANS_MIRROR = 1;
	public static final int TRANS_MIRROR_ROT180 = 2;
 	public static final int TRANS_NONE = 0;


	public boolean isCover() {
		return isCover;
	}

	public void setCover(boolean isCover) {
		this.isCover = isCover;
	}

	public boolean isContentPage() {
		return isContentPage;
	}

	public void setContentPage(boolean isContentPage) {
		this.isContentPage = isContentPage;
	}

	public PageContent(Context context,int imgResId,BookLayout blo,boolean isLeftPage) {
		super(context);
		this.context = context;
		this.imgResId = imgResId;
		paint = FontUtil.mPaint;
		
		paint.setAntiAlias(true);
		this.blo = blo;
		this.isLeftPage = isLeftPage;
	}
	
	public void setPosition(float x,float y){
		this.x = (int)x;
		this.y = (int)y;
	}
	
	public void setSize(int w,int h){
		width = w;
		height = h;
	}
	
	public void create(){
		if(imgResId>-1&&img==null){
			img = BitmapFactory.decodeResource(getResources(), imgResId);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		BookContentView bcv = (BookContentView) context;
		if(isContentPage){
			int sx;
			int sy;
			if(bcv.portraint == 1){
				if(TextSizeUtil.fontSize==14)
					sy = y+PADDING_Y_VIRI-6;
				else if(TextSizeUtil.fontSize==26)
					sy = y+PADDING_Y_VIRI-6;
				else if(TextSizeUtil.fontSize==34)
					sy = y+PADDING_Y_VIRI+8;
				else	
					sy = y+PADDING_Y_VIRI;
				sx = x+PADDING_X_VIRI;
			}else {
				if(isLeftPage==false){
					sx = x+PADDING_X+8;
					sy = y+PADDING_Y;
				}else{
					sx = x+PADDING_X;
					sy = y+PADDING_Y;
				}
				
			}
			/*if(img!=null){
				//canvas.drawBitmap(img, new Rect(0,0,width-2*padding,(height-2*padding)/2), new Rect(x+padding,y+padding,x+width-padding,y+height/2), paint);
				//sy = y+height/2;
			}*/
			
			//sy = sy+TextSizeUtil.fontSize+2;
			paint = FontUtil.mPaint;
			paint.setARGB(255, 0, 0, 0);
			paint.setStrokeWidth(0);
			paint.setTextSize(com.chinachip.ccbooks.core.TextSizeUtil.fontSize);
			int i=0;
			
		   
			List<String> cl = null;
			if(blo.getContentList()!=null&&contentId!=-1&&contentId<blo.getContentList().size())
				cl = blo.getContentList().get(contentId);
			String c = null;
			while(sy<y+height){  
				if(cl!=null&&i<cl.size()){
					c = cl.get(i);           
					i++;
					if(TextSizeUtil.fontSize<26)
						canvas.drawText(c, sx, sy-2, paint);
					else
						canvas.drawText(c, sx, sy+10, paint);
					/*if ((context instanceof BookContentView)
							&& ((BookContentView) context).portraint == 1) {
						if (!isLeftPage) {
							canvas.drawText(c, sx, sy-2, paint);
							//canvasTemp.drawText(c, sx, sy - 2, paint);
						} else {
							//canvas.drawText(c, sx, sy - 2, paint);
							 canvasTemp.drawText(c,sx, sy-2, paint);
						}
					} 
					if ((context instanceof BookContentView)
							&& ((BookContentView) context).portraint == 2){
						canvas.drawText(c, sx, sy-2, paint);
					}*/
				}
				//canvas.drawLine(sx, sy, x+width-padding, sy, paint);
				sy = sy+TextSizeUtil.fontSize+2;

			}
			/*if (context instanceof BookContentView
					&& ((BookContentView) context).portraint == 1) {
				if (isLeftPage) {
					drawImage(canvas, paint, mbmpTest, yw, yh - 2, 1);
				} else {
					
				}

			}*/
		}
		
		
	}
	
	
/*	*//**
	 * 计算目前有多少内容行
	 * @return
	 */
	public static int getPageContentLine(int height){
		int sy = height/2;
		int lines = 0;
		sy = sy+TextSizeUtil.fontSize+2;
		while(sy<height){
			sy = sy+TextSizeUtil.fontSize+2;
			lines++;
		}
		return lines;
	}
	
	/**
	 * 一行的宽度
	 * @param width
	 * @return
	 */
	public static float getPageContentWidth(float width){
		return width-PADDING_X;
	}
	
	
	public void destory(){
		if(img!=null){
			img.recycle();
			img = null;
		}
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	
	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	
	public void drawImage(Canvas canvas,Paint aPaint,Bitmap aBitmap,int x,int y,int aFlip)
     {
             int saveCount = 0;
             int fx = 1, fy = 1;
             saveCount=canvas.save(Canvas.MATRIX_SAVE_FLAG);
             if((aFlip & TRANS_MIRROR) == TRANS_MIRROR)
             {
                     fx = -1;
                     x = -x - aBitmap.getWidth();
             }
             if((aFlip & TRANS_MIRROR_ROT180) == TRANS_MIRROR_ROT180)
             {
                     fy = -1;
                     y = -y - aBitmap.getHeight();
             }
             canvas.scale(fx, fy);
             canvas.drawBitmap(aBitmap,x,y,aPaint);
             canvas.restoreToCount(saveCount);
     }
	
}
