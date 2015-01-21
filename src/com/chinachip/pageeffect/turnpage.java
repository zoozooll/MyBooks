 package com.chinachip.pageeffect;
 
 import android.graphics.Bitmap;
 import android.graphics.Canvas;
 import android.graphics.ColorMatrix;
 import android.graphics.ColorMatrixColorFilter;
 import android.graphics.Matrix;
 import android.graphics.Paint;
 import android.graphics.Path;
 import android.graphics.PointF;
 import android.graphics.Region;
 import android.graphics.Region.Op;
 import android.graphics.drawable.GradientDrawable;
 import android.graphics.drawable.GradientDrawable.Orientation;
 
 public class turnpage
 {
   public boolean isLeftUp = false;
   public boolean isLeftDown = false;
   public boolean isRightUp = false;
   public boolean isRightDown = false;
 
   public static double count = 0.0D;
 
   public static PointF pta = null;
   public static PointF ptf = null;
   public static PointF ptg = null;
   public static PointF pth = null;
   public static PointF pte = null;
   public static PointF ptc = null;
   public static PointF ptj = null;
   public static PointF ptb = null;
   public static PointF ptk = null;
   public static PointF ptd = null;
   public static PointF pti = null;
   public static PointF ptl = null;
   public static PointF ptm = null;
 
   public static PointF pta2 = null;
   public static PointF ptd1 = null;
   public static PointF pti1 = null;
 
   public static float mTouchToCornerDis = 0.0F;
 
   public static Path mPath0 = null;
   public static Path mPath1 = null;
 
   public static Path mPath2 = null;
   public static Path mPath3 = null;
   public static Path mPath4 = null;
 
   public static Path mPath5 = null;
   public static Path mPath6 = null;
   public static Path mPath7 = null;
 
   public static Path mPath8 = null;
 
   public static Path mPath9 = null;
 
   public static PointF dragCorner = new PointF();
   public static PointF lockCorner = new PointF();
   public static PointF targetCorner = new PointF();
 
   public static double MaxLength = 0.0D;
   public static float W = 0.0F;
   public static float H = 0.0F;
 
   public static int[] mBackShadowColors = null;
   public static int[] mFrontShadowColors = null;
   public static GradientDrawable mBackShadowDrawableLR;
   public static GradientDrawable mBackShadowDrawableRL;
   public static GradientDrawable mFolderShadowDrawableLR;
   public static GradientDrawable mFolderShadowDrawableRL;
   public static GradientDrawable mFrontShadowDrawableHBT;
   public static GradientDrawable mFrontShadowDrawableHTB;
   public static GradientDrawable mFrontShadowDrawableVLR;
   public static GradientDrawable mFrontShadowDrawableVRL;
   public static Paint mPaint = new Paint();
   public static ColorMatrix cm = new ColorMatrix();
   public static float[] array = { 0.55F, 0.0F, 0.0F, 0.0F, 80.0F, 0.0F, 0.55F, 0.0F, 0.0F, 80.0F, 0.0F, 0.0F, 0.55F, 0.0F, 80.0F, 0.0F, 0.0F, 0.0F, 0.2F, 0.0F };
 
   public static PointF g(PointF a, PointF f)
   {
     PointF g = new PointF();
     g.x = ((a.x + f.x) / 2.0F);
     g.y = ((a.y + f.y) / 2.0F);
     return g;
   }
 
   public static PointF e(PointF g, PointF f)
   {
     PointF e = new PointF();
 
     g.x -= (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
     e.y = f.y;
     return e;
   }
 
   public static PointF h(PointF g, PointF f)
   {
     PointF h = new PointF();
     h.x = f.x;
     float fg = f.y - g.y;
     if (fg < 0.0F)
       fg = -fg;
     g.y -= (f.x - g.x) * (f.x - g.x) / (f.y - g.y);
     return h;
   }
 
   public static double hypot(PointF a, PointF b)
   {
     return Math.hypot(a.x - b.x, a.y - b.y);
   }
 
   public static PointF a1(PointF a, PointF f, PointF c)
   {
     PointF a1 = new PointF();
     PointF g1 = new PointF();
     PointF o = new PointF(a.x, f.y);
     float cn = 0.0F;
     float nf = 0.0F;
 
     cn = (int)((float)Math.pow(hypot(a, o), 2.0D) * (float)hypot(c, f) / Math.pow(hypot(a, f), 2.0D));
 
     if (c.x <= a.x)
       nf = f.x - cn;
     else {
       nf = c.x - cn;
     }
     float g1n = (float)Math.sqrt(cn * nf);
 
     if (c.x < a.x)
     {
       g1.x = cn;
     }
     else if (c.x == a.x)
     {
       if (a.x < f.x)
       {
         g1.x = cn;
       }
       else
       {
         g1.x = nf;
       }
     }
     else {
       g1.x = nf;
     }
     if (a.y < f.y)
       f.y -= g1n;
     else {
       g1.y = g1n;
     }
     if (c.x < o.x)
       g1.x -= nf / 3.0F;
     else if (c.x == o.x)
     {
       if (a.x < f.x)
       {
         g1.x -= nf / 3.0F;
       }
       else
       {
         g1.x += nf / 3.0F;
       }
     }
     else {
       g1.x += nf / 3.0F;
     }
     if (a.y < f.y)
       g1.y -= g1n / 3.0F;
     else {
       g1.y += g1n / 3.0F;
     }
 
     return a1;
   }
 
   public static PointF a2(PointF a, PointF e, PointF f)
   {
     mTouchToCornerDis = (float)Math.hypot(a.x - f.x, a.y - f.y);
     count = Math.min(mTouchToCornerDis / 4.0F, 12.0F);
 
     PointF a2 = new PointF();
     double degree = 0.0D;
 
     if (dragCorner.x == 0.0F)
     {
       if (dragCorner.y == 0.0F)
       {
         degree = 0.7853981633974483D - Math.atan2(a.y - e.y, a.x - e.x);
         double distance2 = count * 1.414D * Math.sin(degree);
         double distance1 = count * 1.414D * Math.cos(degree);
 
         a2.x = (float)(a.x + distance1);
         a2.y = (float)(a.y - distance2);
       }
       else
       {
         degree = 0.7853981633974483D - Math.atan2(e.y - a.y, a.x - e.x);
 
         double distance2 = count * 1.414D * Math.sin(degree);
         double distance1 = count * 1.414D * Math.cos(degree);
 
         a2.x = (float)(a.x + distance1);
         a2.y = (float)(a.y + distance2);
       }
 
     }
     else if (dragCorner.y == 0.0F)
     {
       degree = 0.7853981633974483D - Math.atan2(e.y - a.y, a.x - e.x);
 
       double distance2 = count * 1.414D * Math.sin(degree);
       double distance1 = count * 1.414D * Math.cos(degree);
 
       a2.x = (float)(a.x + distance1);
       a2.y = (float)(a.y + distance2);
     }
     else
     {
       degree = 0.7853981633974483D - Math.atan2(a.y - e.y, a.x - e.x);
       double distance2 = count * 1.414D * Math.sin(degree);
       double distance1 = count * 1.414D * Math.cos(degree);
 
       a2.x = (float)(a.x + distance1);
       a2.y = (float)(a.y - distance2);
     }
 
     return a2;
   }
 
   public static PointF a2(PointF a, PointF b, PointF k, PointF i, PointF d)
   {
     PointF a2 = new PointF();
     double distanceBK = Math.sqrt(Math.pow(b.x - k.x, 2.0D) + Math.pow(b.y - k.y, 2.0D));
     double distanceID = Math.sqrt(Math.pow(i.x - d.x, 2.0D) + Math.pow(i.y - d.y, 2.0D));
     a2.x = (float)(d.x - (b.x - a.x) * distanceID / distanceBK);
     a2.y = (float)(d.y - (b.y - a.y) * distanceID / distanceBK);
 
     return a2;
   }
 
   public static PointF d1(PointF l, PointF d, PointF f)
   {
     PointF d1 = new PointF();
     float a = (d.y - l.y) / (d.x - l.x);
     float c = (d.x * l.y - d.y * l.x) / (d.x - l.x);
 
     d1.y = f.y;
     d1.x = ((d1.y - c) / a);
     return d1;
   }
 
   public static PointF i1(PointF m, PointF i, PointF f)
   {
     PointF i1 = new PointF();
     float a = (i.y - m.y) / (i.x - m.x);
     float c = (i.x * m.y - i.y * m.x) / (i.x - m.x);
 
     i1.x = f.x;
     i1.y = (a * i1.x + c);
     return i1;
   }
 
   public static PointF targetTrack(PointF p1, PointF p2, float x)
   {
     PointF point = new PointF();
 
     float a = 0.0F; float b = 0.0F;
 
     if (p1.x != p2.x)
     {
       a = (p1.y - p2.y) / (p1.x - p2.x);
       b = (p1.x * p2.y - p2.x * p1.y) / (p1.x - p2.x);
     }
     point.x = x;
     point.y = (a * x + b);
 
     return point;
   }
 
   private static PointF j(PointF h, PointF f)
   {
     PointF j = new PointF();
     j.x = f.x;
     h.y -= (f.y - h.y) / 2.0F;
     return j;
   }
 
   public static Boolean isOver(PointF d, PointF i, float w, float h)
   {
     PointF a = new PointF();
     PointF b = new PointF();
 
     PointF approach = new PointF();
     if (dragCorner.x == 0.0F)
     {
       if (dragCorner.y == 0.0F)
       {
         a.x = 10.0F;
         a.y = h;
         b.x = 20.0F;
         b.y = h;
         approach = lineCross(d, i, a, b);
         if (approach.x >= w - 3.0F)
         {
           return Boolean.valueOf(true);
         }
 
         return Boolean.valueOf(false);
       }
 
       a.x = 10.0F;
       a.y = 0.0F;
       b.x = 20.0F;
       b.y = 0.0F;
       approach = lineCross(d, i, a, b);
       if (approach.x >= w - 1.0F)
       {
         return Boolean.valueOf(true);
       }
 
       return Boolean.valueOf(false);
     }
 
     if (dragCorner.y == 0.0F)
     {
       a.x = 10.0F;
       a.y = h;
       b.x = 20.0F;
       b.y = h;
       approach = lineCross(d, i, a, b);
       if (approach.x <= 3.0F)
       {
         return Boolean.valueOf(true);
       }
 
       return Boolean.valueOf(false);
     }
 
     a.x = 10.0F;
     a.y = 0.0F;
     b.x = 20.0F;
     b.y = 0.0F;
     approach = lineCross(d, i, a, b);
     if (approach.x <= 3.0F)
     {
       return Boolean.valueOf(true);
     }
 
     return Boolean.valueOf(false);
   }
 
   private static PointF lineCross(PointF pa, PointF pb, PointF pc, PointF pd)
   {
     PointF p = new PointF();
 
     float x1 = pa.x; float x2 = pb.x; float x3 = pc.x; float x4 = pd.x;
     float y1 = pa.y; float y2 = pb.y; float y3 = pc.y; float y4 = pd.y;
 
     float a = 0.0F; float b = 0.0F;
 
     if (x1 != x2)
     {
       a = (y1 - y2) / (x1 - x2);
       b = (x1 * y2 - x2 * y1) / (x1 - x2);
     }
 
     float c = 0.0F; float d = 0.0F;
     if (x3 != x4)
     {
       c = (y3 - y4) / (x3 - x4);
       d = (x3 * y4 - x4 * y3) / (x3 - x4);
     }
 
     if ((x1 == x2) && (x3 != x4))
     {
       p.x = x1;
       p.y = (c * x1 + d);
     }
     else if ((x1 != x2) && (x3 == x4))
     {
       p.x = x3;
       p.y = (a * x3 + b);
     }
     else
     {
       p.x = 
         (((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1)) / (
         (x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4)));
 
       p.y = 
         (((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4)) / (
         (y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4)));
     }
     return p;
   }
 
   private static PointF triangleCenter(PointF c, PointF b, PointF e)
   {
     PointF d = new PointF();
     d.x = (((c.x + b.x) / 2.0F + e.x) / 2.0F);
     d.y = (((c.y + b.y) / 2.0F + e.y) / 2.0F);
     return d;
   }
 
   public static PointF c(PointF a, PointF f)
   {
     PointF c = new PointF();
     PointF g = g(a, f);
     PointF e = e(g, f);
     c.y = f.y;
     f.x -= (f.y - ((a.y + f.y) / 2.0F + a.y) / 2.0F) * (f.x - e.x) / (f.y - (a.y + f.y) / 2.0F);
     return c;
   }
 
   public static void calcPoints(PointF a, PointF f)
   {
     pta = a;
     ptf = f;
 
     ptg = g(pta, ptf);
 
     pth = h(ptg, ptf);
     pte = e(ptg, ptf);
     ptc = c(pta, ptf);
 
     ptj = j(pth, ptf);
 
     ptb = lineCross(pta, pte, ptc, ptj);
     ptk = lineCross(pta, pth, ptc, ptj);
 
     ptd = triangleCenter(ptc, ptb, pte);
     pti = triangleCenter(ptj, ptk, pth);
 
     ptl = lineCross(ptd, pti, pta, pte);
     ptm = lineCross(ptd, pti, pta, pth);
 
     pta2 = a2(pta, pte, ptf);
 
     ptd1 = d1(ptl, ptd, ptf);
     pti1 = i1(ptm, pti, ptf);
   }
 
   public static Path getPath0()
   {
     Path mPath0 = new Path();
 
     mPath0.moveTo(ptj.x, ptj.y);
     mPath0.quadTo(pth.x, pth.y, ptk.x, ptk.y);
     mPath0.lineTo(pta.x, pta.y);
     mPath0.lineTo(ptb.x, ptb.y);
     mPath0.quadTo(pte.x, pte.y, ptc.x, ptc.y);
     mPath0.lineTo(ptf.x, ptf.y);
     mPath0.close();
 
     return mPath0;
   }
 
   public static Path getPath1()
   {
     Path mPath1 = new Path();
 
     mPath1.moveTo(pti.x, pti.y);
     mPath1.quadTo(ptm.x, ptm.y, ptk.x, ptk.y);
     mPath1.lineTo(pta.x, pta.y);
     mPath1.lineTo(ptb.x, ptb.y);
     mPath1.quadTo(ptl.x, ptl.y, ptd.x, ptd.y);
     mPath1.lineTo(pti.x, pti.y);
     mPath1.close();
 
     return mPath1;
   }
 
   public static Path getPath2()
   {
     Path mPath2 = new Path();
 
     mPath2.moveTo(pta2.x, pta2.y);
     mPath2.lineTo(pta.x, pta.y);
 
     mPath2.lineTo(ptb.x, ptb.y);
 
     mPath2.quadTo(ptl.x, ptl.y, ptd.x, ptd.y);
 
     mPath2.close();
     return mPath2;
   }
 
   public static Path getPath3()
   {
     Path mPath3 = new Path();
 
     mPath3.moveTo(pta2.x, pta2.y);
     mPath3.lineTo(pta.x, pta.y);
 
     mPath3.lineTo(ptk.x, ptk.y);
 
     mPath3.quadTo(ptm.x, ptm.y, pti.x, pti.y);
     mPath3.close();
     return mPath3;
   }
 
   public static Path getPath4()
   {
     Path mPath4 = new Path();
 
     mPath4.moveTo(pte.x, pte.y);
     mPath4.lineTo(ptc.x, ptc.y);
     mPath4.quadTo(ptd1.x, ptd1.y, ptd.x, ptd.y);
 
     mPath4.lineTo(pti.x, pti.y);
     mPath4.quadTo(pti1.x, pti1.y, ptj.x, ptj.y);
     mPath4.lineTo(pth.x, pth.y);
 
     mPath4.close();
     return mPath4;
   }
 
   public static Path getPath5()
   {
     Path mPath5 = new Path();
 
     mPath5.moveTo(ptc.x, ptc.y);
     mPath5.quadTo(pte.x, pte.y, ptb.x, ptb.y);
     mPath5.lineTo(pta.x, pta.y);
     mPath5.lineTo(ptk.x, ptk.y);
     mPath5.quadTo(pth.x, pth.y, ptj.x, ptj.y);
     mPath5.lineTo(ptf.x, ptf.y);
     mPath5.close();
     return mPath5;
   }
 
   public static Path getPath6()
   {
     Path mPath6 = new Path();
 
     mPath6.moveTo(pta2.x, pta2.y);
     mPath6.lineTo(pta.x, pta.y);
     mPath6.lineTo(pte.x, pte.y);
     mPath6.lineTo(ptc.x, ptc.y);
     mPath6.close();
     return mPath6;
   }
 
   public static Path getPath7() {
     Path mPath7 = new Path();
 
     mPath7.moveTo(pta2.x, pta2.y);
     mPath7.lineTo(pta.x, pta.y);
     mPath7.lineTo(pth.x, pth.y);
     mPath7.lineTo(ptj.x, ptj.y);
     mPath7.close();
     return mPath7;
   }
 
   public static Path getPath8()
   {
     Path mPath8 = new Path();
 
     mPath8.moveTo(ptc.x, ptc.y);
     mPath8.lineTo(ptd.x, ptd.y);
     mPath8.lineTo(pti.x, pti.y);
     mPath8.lineTo(ptj.x, ptj.y);
     mPath8.lineTo(ptf.x, ptf.y);
     mPath8.close();
 
     return mPath8;
   }
 
   public static Path getPath9()
   {
     Path mPath9 = new Path();
 
     mPath9.moveTo(pti.x, pti.y);
     mPath9.lineTo(ptd.x, ptd.y);
     mPath9.lineTo(ptb.x, ptb.y);
     mPath9.lineTo(pta.x, pta.y);
     mPath9.lineTo(ptk.x, ptk.y);
     mPath9.close();
 
     return mPath9;
   }
 
   public static void workOut(PointF a, PointF f)
   {
     calcPoints(a, f);
     mPath0 = getPath0();
     mPath1 = getPath1();
 
     mPath2 = getPath2();
     mPath3 = getPath3();
     mPath4 = getPath4();
 
     mPath5 = getPath5();
     mPath6 = getPath6();
     mPath7 = getPath7();
 
     mPath8 = getPath8();
 
     mPath9 = getPath9();
   }
 
   private static void findCorner1(float x, float y, float w, float h)
   {
     float cx = w / 2.0F;
     float cy = h / 2.0F;
 
     if (x < cx) {
       if (y < cy) {
         dragCorner.x = 0.0F;
         dragCorner.y = 0.0F;
       }
       else
       {
         dragCorner.x = 0.0F;
         dragCorner.y = h;
       }
 
     }
     else if (y < cy) {
       dragCorner.x = w;
       dragCorner.y = 0.0F;
     }
     else
     {
       dragCorner.x = w;
       dragCorner.y = h;
     }
   }
 
   private static void findCorner2(float x, float y, float w, float h)
   {
     findCorner1(x, y, w, h);
 
     if (dragCorner.x == 0.0F)
     {
       if (dragCorner.y == 0.0F)
       {
         lockCorner.x = w;
         lockCorner.y = 0.0F;
       }
       else
       {
         lockCorner.x = w;
         lockCorner.y = h;
       }
 
     }
     else if (dragCorner.y == 0.0F)
     {
       lockCorner.x = 0.0F;
       lockCorner.y = 0.0F;
     }
     else
     {
       lockCorner.x = 0.0F;
       lockCorner.y = h;
     }
   }
 
   public static void findCorner(PointF a, float w, float h)
   {
     W = w;
     H = h;
 
     MaxLength = (float)Math.hypot(w, h);
 
     findCorner2(a.x, a.y, w, h);
 
     if (lockCorner.x > dragCorner.x)
       lockCorner.x *= 2.0F;
     else {
       targetCorner.x = (-dragCorner.x);
     }
     targetCorner.y = dragCorner.y;
   }
 
   public static boolean isCornerLock(PointF a, PointF f, float w, float h)
   {
     PointF c = c(a, f);
     if (lockCorner.x == 0.0F)
     {
       if (lockCorner.y == 0.0F)
       {
         if (c.x < 0.0F) {
           return true;
         }
 
       }
       else if (c.x < 0.0F) {
         return true;
       }
 
     }
     else if (lockCorner.y == 0.0F)
     {
       if (c.x > w) {
         return true;
       }
 
     }
     else if (c.x > w) {
       return true;
     }
 
     return false;
   }
 
   public static void createDrawable()
   {
     int[] color = { 3355443, -1338821837 };
     mFolderShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color);
     mFolderShadowDrawableRL.setGradientType(0);
 
     mFolderShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
     mFolderShadowDrawableLR.setGradientType(0);
 
     mBackShadowColors = new int[] { -15658735, 1118481 };
     mBackShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
     mBackShadowDrawableRL.setGradientType(0);
 
     mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
     mBackShadowDrawableLR.setGradientType(0);
 
     mFrontShadowColors = new int[] { -2146365167, 1118481 };
     mFrontShadowDrawableVLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
     mFrontShadowDrawableVLR.setGradientType(0);
     mFrontShadowDrawableVRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
     mFrontShadowDrawableVRL.setGradientType(0);
 
     mFrontShadowDrawableHTB = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
     mFrontShadowDrawableHTB.setGradientType(0);
 
     mFrontShadowDrawableHBT = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
     mFrontShadowDrawableHBT.setGradientType(0);
   }
 
   public static void drawCurrentPageShadow(Canvas canvas)
   {
     int leftx = 0;
     int rightx = 0;
     GradientDrawable mCurrentPageShadow = null;
     float rotateDegrees = 0.0F;
 
     if (dragCorner.x == 0.0F)
     {
       if (dragCorner.y == 0.0F)
       {
         leftx = (int)(pte.x - count);
         rightx = (int)pte.x + 1;
         mCurrentPageShadow = mFrontShadowDrawableVRL;
       }
       else
       {
         leftx = (int)pte.x;
         rightx = (int)((int)pte.x + count);
         mCurrentPageShadow = mFrontShadowDrawableVLR;
       }
 
     }
     else if (dragCorner.y == 0.0F)
     {
       leftx = (int)pte.x;
       rightx = (int)((int)pte.x + count);
       mCurrentPageShadow = mFrontShadowDrawableVLR;
     }
     else
     {
       leftx = (int)(pte.x - count);
       rightx = (int)pte.x + 1;
       mCurrentPageShadow = mFrontShadowDrawableVRL;
     }
 
     canvas.save();
 
     canvas.clipPath(mPath5, Region.Op.XOR);
     canvas.clipPath(mPath6, Region.Op.INTERSECT);
     rotateDegrees = (float)Math.toDegrees(Math.atan2(pta.x - pte.x, pte.y - pta.y));
     canvas.rotate(rotateDegrees, pte.x, pte.y);
 
     mCurrentPageShadow.setBounds(leftx, (int)(pte.y - MaxLength), rightx, (int)pte.y);
     mCurrentPageShadow.draw(canvas);
     canvas.restore();
 
     if (dragCorner.x == 0.0F)
     {
       if (dragCorner.y == 0.0F)
       {
         leftx = (int)(pth.y - count);
         rightx = (int)(pth.y + 1.0F);
         mCurrentPageShadow = mFrontShadowDrawableHBT;
       }
       else
       {
         leftx = (int)pth.y;
         rightx = (int)(pth.y + count);
         mCurrentPageShadow = mFrontShadowDrawableHTB;
       }
 
     }
     else if (dragCorner.y == 0.0F)
     {
       leftx = (int)pth.y;
       rightx = (int)(pth.y + count);
       mCurrentPageShadow = mFrontShadowDrawableHTB;
     }
     else
     {
       leftx = (int)(pth.y - count);
       rightx = (int)(pth.y + 1.0F);
       mCurrentPageShadow = mFrontShadowDrawableHBT;
     }
 
     canvas.save();
     canvas.clipPath(mPath5, Region.Op.XOR);
     canvas.clipPath(mPath7, Region.Op.INTERSECT);
 
     rotateDegrees = (float)Math.toDegrees(Math.atan2(pth.y - pta.y, pth.x - pta.x));
 
     canvas.rotate(rotateDegrees, pth.x, pth.y);
 
     float temp = 0.0F;
     if (pth.y < 0.0F)
       temp = pth.y - H;
     else {
       temp = pth.y;
     }
     int hmg = (int)Math.hypot(pth.x, temp);
     if (hmg > MaxLength)
     {
       mCurrentPageShadow.setBounds((int)(pth.x - count) - hmg, leftx, (int)(pth.x + MaxLength) - hmg, rightx);
     }
     else
     {
       mCurrentPageShadow.setBounds((int)(pth.x - MaxLength), leftx, (int)pth.x, rightx);
     }
 
     mCurrentPageShadow.draw(canvas);
     canvas.restore();
   }
 
   public static void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap)
   {
     int leftx = 0;
     int rightx = 0;
     GradientDrawable mBackShadowDrawable = null;
     float mDegrees = (float)Math.toDegrees(Math.atan2(pte.x - ptf.x, pth.y - ptf.y));
 
     if (dragCorner.x == 0.0F)
     {
       if (dragCorner.y == 0.0F)
       {
         leftx = (int)(ptc.x - mTouchToCornerDis / 4.0F);
         rightx = (int)ptc.x;
         mBackShadowDrawable = mBackShadowDrawableRL;
       }
       else
       {
         leftx = (int)ptc.x;
         rightx = (int)(ptc.x + mTouchToCornerDis / 4.0F);
         mBackShadowDrawable = mBackShadowDrawableLR;
       }
 
     }
     else if (dragCorner.y == 0.0F)
     {
       leftx = (int)ptc.x;
       rightx = (int)(ptc.x + mTouchToCornerDis / 4.0F);
       mBackShadowDrawable = mBackShadowDrawableLR;
     }
     else
     {
       leftx = (int)(ptc.x - mTouchToCornerDis / 4.0F);
       rightx = (int)ptc.x;
       mBackShadowDrawable = mBackShadowDrawableRL;
     }
 
     canvas.save();
     canvas.clipPath(mPath0);
     canvas.clipPath(mPath8, Region.Op.INTERSECT);
     canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
     canvas.rotate(mDegrees, ptc.x, ptc.y);
     mBackShadowDrawable.setBounds(leftx, (int)ptc.y, rightx, (int)(MaxLength + ptc.y));
     mBackShadowDrawable.draw(canvas);
     canvas.restore();
   }
 
   public static void drawCurrentPageArea(Canvas canvas, Bitmap bitmap)
   {
     canvas.save();
     canvas.clipPath(mPath5, Region.Op.XOR);
     canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
     canvas.restore();
   }
 
   public static void drawCurrentBackArea(Canvas canvas, Bitmap bitmap)
   {
     int i = (int)(ptc.x + pte.x) / 2;
     float f1 = Math.abs(i - pte.x);
     int i1 = (int)(ptj.y + pth.y) / 2;
     float f2 = Math.abs(i1 - pth.y);
     float f3 = Math.min(f1, f2);
     GradientDrawable mFolderShadowDrawable = null;
     int left = 0;
     int right = 0;
 
     cm.set(array);
     ColorMatrixColorFilter mColorMatrixFilter = new ColorMatrixColorFilter(cm);
 
     Matrix mMatrix = new Matrix();
     float[] mMatrixArray = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F };
 
     float mDegrees = (float)Math.toDegrees(Math.atan2(pte.x - ptf.x, pth.y - ptf.y));
 
     if (dragCorner.x == 0.0F)
     {
       if (dragCorner.y == 0.0F)
       {
         left = (int)(ptc.x - f3 - 1.0F);
         right = (int)(ptc.x + 1.0F);
         mFolderShadowDrawable = mFolderShadowDrawableRL;
       }
       else
       {
         left = (int)(ptc.x - 3.0F);
         right = (int)(ptc.x + f3 + 4.0F);
         mFolderShadowDrawable = mFolderShadowDrawableLR;
       }
 
     }
     else if (dragCorner.y == 0.0F)
     {
       left = (int)(ptc.x - 1.0F);
       right = (int)(ptc.x + f3 + 1.0F);
       mFolderShadowDrawable = mFolderShadowDrawableLR;
     }
     else
     {
       left = (int)(ptc.x - f3 - 1.0F);
       right = (int)(ptc.x + 1.0F);
       mFolderShadowDrawable = mFolderShadowDrawableRL;
     }
 
     canvas.save();
     canvas.clipPath(mPath0);
     canvas.clipPath(mPath9, Region.Op.INTERSECT);
 
     mPaint.setColorFilter(mColorMatrixFilter);
 
     mPaint.setFlags(1);
 
     float dis = (float)Math.hypot(ptf.x - pte.x, pth.y - ptf.y);
     float f8 = (ptf.x - pte.x) / dis;
     float f9 = (pth.y - ptf.y) / dis;
     mMatrixArray[0] = (1.0F - 2.0F * f9 * f9);
     mMatrixArray[1] = (2.0F * f8 * f9);
     mMatrixArray[3] = mMatrixArray[1];
     mMatrixArray[4] = (1.0F - 2.0F * f8 * f8);
 
     mMatrix.setValues(mMatrixArray);
     mMatrix.preTranslate(-pte.x, -pte.y);
     mMatrix.postTranslate(pte.x, pte.y);
     canvas.drawBitmap(bitmap, mMatrix, mPaint);
 
     mPaint.setColorFilter(null);
     canvas.rotate(mDegrees, ptc.x, ptc.y);
     mFolderShadowDrawable.setBounds(left, (int)ptc.y, right, (int)(ptc.y + MaxLength));
     mFolderShadowDrawable.draw(canvas);
     canvas.restore();
   }
 }

