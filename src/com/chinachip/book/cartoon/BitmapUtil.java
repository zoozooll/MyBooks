package com.chinachip.book.cartoon;



import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtil {

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static byte[] Bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	
	public static final float LEVEL2 = 0.80f;
	public static final float LEVEL4 = 0.60f;
	public static final float LEVEL8 = 0.30f;
	
	/*
	 * 等比缩小
	 */
	public static Bitmap getSmallBitmap(String imgpath, float ratio) {
		Bitmap big = BitmapFactory.decodeFile(imgpath);
		Drawable drawable = resizeImage(big, ratio);
		Bitmap small = BitmapUtil.drawableToBitmap(drawable);
		big.recycle();
		return small;
	}
	
	/*
	 * 等比缩小
	 */
	public static Drawable getDrawable(String imgpath, float ratio) {
		Bitmap big = BitmapFactory.decodeFile(imgpath);
		Drawable drawable = resizeImage(big, ratio);
		return drawable;
	}

	public static Drawable resizeImage(Bitmap bitmap, float ratio) {
        Bitmap origin = bitmap;
        int width = origin.getWidth();
        int height = origin.getHeight();
        float scaleWidth = ratio;
        float scaleHeight = ratio;
      
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(origin, 0, 0, width,
                        height, matrix, true);
        return new BitmapDrawable(resizedBitmap);

	}
	
	// 放大缩小图片
	public static Bitmap zoomBitmap(String imgpath, float ratio) {
		
		 BitmapFactory.Options opts = new BitmapFactory.Options();  

		  opts.inJustDecodeBounds = true ;  

		  Bitmap origin = null;

		BitmapFactory.decodeFile(imgpath,opts);
		
	        

		  opts.inSampleSize = computeSampleSize(opts, - 1 , 800  * 480);        

		  opts.inJustDecodeBounds = false ;  

		  try {  

		       origin = BitmapFactory.decodeFile(imgpath, opts);  

		     

		       } catch (OutOfMemoryError err) {  
		    	   err.printStackTrace();
		       }  

		
		
		int width = origin.getWidth();
		int height = origin.getHeight();
		float scaleWidth = ratio;
		float scaleHeight = ratio;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(origin, 0, 0, width, height,
				matrix, true);
		return resizedBitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,  

	          int minSideLength, int maxNumOfPixels) {  

	      int initialSize = computeInitialSampleSize(options, minSideLength,  

	              maxNumOfPixels);  

	   

	      int roundedSize;  

	      if (initialSize <= 8 ) {  

	          roundedSize = 1 ;  

	          while (roundedSize < initialSize) {  

	              roundedSize <<= 1 ;  

	          }  

	      } else {  

	          roundedSize = (initialSize + 7 ) / 8 * 8 ;  

	      }  

	   

	      return roundedSize;  

	 }  

	private static int computeInitialSampleSize(BitmapFactory.Options options,  
	
		           int minSideLength, int maxNumOfPixels) {  
	
		       double w = options.outWidth;  
	
		       double h = options.outHeight;  
	
		    
	
		       int lowerBound = (maxNumOfPixels == - 1 ) ? 1 :  
	
		               ( int ) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
	
		       int upperBound = (minSideLength == - 1 ) ? 128 :  
	
		               ( int ) Math.min(Math.floor(w / minSideLength),  
	
		               Math.floor(h / minSideLength));  
	
		    
	
		       if (upperBound < lowerBound) {  
	
		           // return the larger one when there is no overlapping zone.  
	
		           return lowerBound;  
	
		       }  
	
		    
	
		       if ((maxNumOfPixels == - 1 ) &&  
	
		               (minSideLength == - 1 )) {  
	
		           return 1 ;  
	
		       } else if (minSideLength == - 1 ) {  
	
		           return lowerBound;  
	
		       } else {  
	
		           return upperBound;  
	
		       }  
	
		  }    


	public static Bitmap initZoomBitmap1(Bitmap origin, float ratio) {
		int width = origin.getWidth();
		int height = origin.getHeight();
		float scaleWidth = ratio;
		float scaleHeight = ratio;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(origin, 0, 0, width, height,
				matrix, true);
		return resizedBitmap;
	}
}
