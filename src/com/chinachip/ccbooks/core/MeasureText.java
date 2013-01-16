package com.chinachip.ccbooks.core;
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.*;

public class MeasureText {

  private Paint   mPaint;
  private float   mOriginX = 10;
  private float   mOriginY = 80;
   
    
    public MeasureText(int fontSize)
  {
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setStrokeWidth(5);
    mPaint.setStrokeCap(Paint.Cap.ROUND);
    mPaint.setTextSize(fontSize);
  
    mPaint.setTypeface(Typeface.create(Typeface.SERIF,
                                       Typeface.ITALIC));
    // TODO Auto-generated constructor stub
  }

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final int STRIDE = 64;   // must be >= WIDTH
    
   
        
        
        private void showText(String text, Paint.Align align) {
        
            float[] widths = new float[text.length()];

            int count = mPaint.getTextWidths(text, 0, text.length(), widths);
          
            float w = mPaint.measureText(text, 0, text.length());
            System.out.println("w------------>"+w);
         
            
            mPaint.setColor(0xFF88FF88);
       
            mPaint.setColor(Color.BLACK);
        
            
            float[] pts = new float[2 + count*2];
            float x = 0;
            float y = 0;
            pts[0] = x;
            pts[1] = y;
            for (int i = 0; i < count; i++) {
                x += widths[i];
                pts[2 + i*2] = x;
                pts[2 + i*2 + 1] = y;
            }
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(0);
        
            mPaint.setStrokeWidth(5);
        
        }
        
        public float getCharWidth(String text) {
            
           
          
            float w = mPaint.measureText(text, 0, text.length());
            //System.out.println("w------------>"+w);
         
           return w;
        }
        
        
        
        public void test() {
       
          
        	getCharWidth("M");
       
         //wText("Text", Paint.Align.RIGHT);
     
  }



  
}

