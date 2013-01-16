package com.ccbooks.fullscreen.content;

import java.util.ArrayList;

import com.ccbooks.view.TextReader;

import android.content.Context;  
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;  
import android.graphics.Paint;  
import android.util.AttributeSet;  
import android.view.View;  

public class TextRender extends CommentView {  
    private Paint mPaint = null;  
	private Context mContext;  
    private int textSize;
    private int lineSpace;
	private int textColor;
	private ArrayList<String> stringArray;
  
    public TextRender(Context context) {  
        super(context);
        mContext = context;
        intiPaint();
    }
    
    public TextRender(Context context,AttributeSet attr)  
    {  
        super(context,attr);
        mContext = context;
        intiPaint();
    }
    
    public void intiPaint(){
		mPaint = TextReader.bc.getPaint();
		textColor = 0xff000000;
		mPaint.setColor(textColor);
	    mPaint.setAntiAlias(true);
	    mPaint.setStrokeWidth(5);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    textSize = 18;
	    mPaint.setTextSize(textSize);
    }
    
    public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
		mPaint.setColor(textColor);
	}
	
    public int getLineSpace() {
		return lineSpace;
	}

	public void setLineSpace(int lineSpace) {
		this.lineSpace = lineSpace;
	}   
	
    public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
		mPaint.setTextSize(textSize);
	}     
	
    // ��ȡҪ��ʾ���ַ�
    public ArrayList<String> getTextArray(){
    	return stringArray;
    }
    public void setTextArray(ArrayList<String> strArr){
    	stringArray = strArr;
    }    	
    
    @Override  
    protected void onDraw(Canvas canvas) {  
        int w = this.getWidth();
       	int h = this.getHeight();
        //ǰ����
        renderBefore(canvas,w,h);
        //��������  
        renderExec(canvas,w,h);  
        //����
        renderAfter(canvas,w,h);
        super.onDraw(canvas); 
    }  

    protected void renderExec(Canvas canvas,int w,int h)
    {
    	ArrayList<String> texts = getTextArray();
    	if(texts == null)
    		return;
		int top = this.getTop()+this.getPaddingTop();
		int left = this.getLeft()+this.getPaddingLeft();
    	for(int i = 0;i<texts.size();i++)
    	{
    		canvas.drawText(texts.get(i),left, top+textSize*(i+1)+lineSpace*i, mPaint);
    	}
    	canvas.drawLine(getLeft()+getWidth(), 0, getLeft()+getWidth(), getTop()+getHeight(), mPaint);
    }
    
    // ��Ⱦ����ǰ����
    public void renderBefore(Canvas canvas,int w,int h){
    	//δʵ�ֹ���
    }
    
    // ��Ⱦ���ֺ���
    public void renderAfter(Canvas canvas,int w,int h){
    	//δʵ�ֹ���
    	TextReader actParent = (TextReader)mContext;
    	if(!actParent.getIsComment()){
//    		Thread t = new Thread(this);
//    		t.start();
//    		scrollPaint();
    	}
    }

	public Paint getPaint() {
		return mPaint;
	}

	public void setPaint(Paint mPaint) {
		this.mPaint = mPaint;
	}
  

} 
