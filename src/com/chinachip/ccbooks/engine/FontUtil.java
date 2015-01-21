 package com.chinachip.ccbooks.engine;
 
 import android.graphics.Paint;
 import android.graphics.Paint.Cap;
 
 public class FontUtil
 {
   public static Paint mPaint = null;
 
   public final int DEFAULT_FONTSIZE = 20;
   public final int DEFAULT_FONTCOLOR = -1;
 
   public FontUtil()
   {
     initFont(20, -1);
   }
 
   public FontUtil(int fontSize)
   {
     initFont(fontSize, -1);
   }
 
   public FontUtil(int fontSize, int fontColor)
   {
     initFont(fontSize, fontColor);
   }
 
   private void initFont(int fontSize, int fontColor)
   {
     mPaint = new Paint();
     mPaint.setColor(fontColor);
     mPaint.setAntiAlias(true);
     mPaint.setStrokeWidth(5.0F);
     mPaint.setStrokeCap(Paint.Cap.ROUND);
     mPaint.setTextSize(fontSize);
   }
 
   public int getFontSize()
   {
     return (int)mPaint.getTextSize();
   }
 
   public int setFontSize(int size)
   {
     int ret = getFontSize();
     mPaint.setTextSize(size);
     return ret;
   }
 
  /* public float getWidth(String text)
   {
     float w = mPaint.measureText(text, 0, text.length());
 
     return w;
   }*/
 
   /*public float[] getWidths(String text)
   {
     float[] widths = new float[text.length()];
     int count = mPaint.getTextWidths(text, 0, text.length(), widths);
 
     return widths;
   }*/
 
   public Paint getPaint()
   {
     return mPaint;
   }
 
   public Paint setPaint(Paint paint)
   {
     Paint ret = mPaint;
     mPaint = paint;
     return ret;
   }
 }
