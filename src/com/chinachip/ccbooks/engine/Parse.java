/*     */ package com.chinachip.ccbooks.engine;
/*     */ 
/*     */ import android.graphics.Paint;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class Parse
/*     */ {
/*  13 */   public final short DT_SINGLEBYTE = 1;
/*  14 */   public final short DT_MULTIBYTES = 2;
/*  15 */   public final short DT_UNICODEBE = 3;
/*  16 */   public final short DT_UNICODELE = 4;
/*  17 */   public final short DT_UTF8 = 5;
/*     */ 
/*  19 */   protected short dataType = 5;
/*     */ 
/*  21 */   private int curLine = 0;
/*  22 */   private String codePage = "GBK,GB2312,CP936,CP949,CP932,CP950,BIG5,EUC-CN,EUC-JP,EUC-KR,SHIFT-JIS";
/*  23 */   protected String enc = "GBK";
/*     */ 
/*     */   public abstract Paint getPaint();
/*     */ 
/*     */   public abstract int getLineCount();
/*     */ 
/*     */   public abstract int getPageWidth();
/*     */ 
/*     */   private boolean isUnicodeLE()
/*     */   {
/* 115 */     return this.dataType == 4;
/*     */   }
/*     */ 
/*     */   protected boolean isMultiByte()
/*     */   {
/* 288 */     return this.codePage.contains(this.enc);
/*     */   }
/*     */ 
/*     */   public int GetUtf8ByteNumForWord(byte firstCh)
/*     */   {
/* 293 */     byte temp = -128;
/* 294 */     int num = 0;
/*     */ 
/* 296 */     while ((temp & firstCh) == temp) {
/* 297 */       num++;
/* 298 */       temp = (byte)(temp >> 1);
/*     */     }
/* 300 */     if (num == 0) {
/* 301 */       return 1;
/*     */     }
/* 303 */     return num;
/*     */   }
/*     */ 
/*     */   public int run(Cache cache, int beg, int end, ArrayList<String> al, int lineCount)
/*     */   {
/* 308 */     String text = cache.getData();
/* 309 */     CharSequence toMeasure = text;
/*     */ 
/* 311 */     int length = toMeasure.length();
/*     */ 
/* 313 */     int next = beg;
/*     */ 
/* 315 */     int width = getPageWidth();
/*     */ 
/* 317 */     float[] measuredWidth = { 0.0F };
/*     */ 
/* 319 */     while (next < length)
/*     */     {
/* 322 */       int bPoint = getPaint().breakText(toMeasure, next, length, true, width, measuredWidth);
/*     */ 
/* 326 */       int enterPosition = 0;
/*     */ 
/* 328 */       enterPosition = text.substring(next, next + bPoint).indexOf('\n');
/*     */ 
/* 333 */       if (enterPosition < 0)
/*     */       {
/* 335 */         al.add(text.substring(next, next + bPoint));
/* 336 */         next += bPoint;
/*     */       }
/*     */       else
/*     */       {
/* 340 */         al.add(text.substring(next, next + enterPosition + 1));
/*     */ 
/* 342 */         next += enterPosition + 1;
/*     */       }
/*     */ 
/* 347 */       if (end > 0) {
/* 348 */         if (next >= end) {
/* 349 */           if (next == Math.abs(end))
/*     */           {
/* 352 */             return next;
/*     */           }
/* 354 */           if (next > Math.abs(end)) {
/* 355 */             next -= ((String)al.get(al.size() - 1)).length();
/*     */ 
/* 358 */             return next;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/* 363 */       else if (end == 0) {
/* 364 */         if (al.size() == lineCount) {
/* 365 */           return next;
/*     */         }
/*     */ 
/*     */       }
/* 369 */       else if (next >= Math.abs(end))
/*     */       {
/* 371 */         return next;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 378 */     return next;
/*     */   }
/*     */ 
/*     */   public String getEnc()
/*     */   {
/* 384 */     return this.enc;
/*     */   }
/*     */ 
/*     */   public void setEnc(String enc) {
/* 388 */     this.enc = enc;
/*     */   }
/*     */ }

/* Location:           I:\Workspaces\eclipse3.4\CCBookPro\lib\bookcore.jar
 * Qualified Name:     com.chinachip.ccbooks.engine.Parse
 * JD-Core Version:    0.6.0
 */