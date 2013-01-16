package com.ccbooks.flip;

import java.util.ArrayList;
import java.util.Iterator;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class AndroidUtils {

    public static DisplayMetrics getDisplayMetrics(Context cx) {
        DisplayMetrics dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        return dm;
    }
	
	public static ArrayList<ArrayList<String>> getPageContentStringInfo(Paint m_paint,String content,int pageLines,float pageWidth)
	{
		char ch;
		int w = 0;
		int istart = 0;
		int lineNum = 0;
		ArrayList<ArrayList<String>> contentList = new ArrayList<ArrayList<String>>();
		ArrayList<String> cl = null;
		for (int i = 0; i < content.length(); i++)
		{
			if(cl == null)
				cl = new ArrayList<String>();
			ch = content.charAt(i);
			float[] widths = new float[1];
			String srt = String.valueOf(ch);
			m_paint.getTextWidths(srt, widths);

			if (ch == '\n')
			{
				lineNum++;
				cl.add(content.substring(istart, i));
				istart = i + 1;
				w = 0;
			}
			else
			{
				w += (int) (Math.ceil(widths[0]));
				if (w > pageWidth)
				{
					lineNum++;
					cl.add(content.substring(istart, i));
					istart = i;
					i--;
					w = 0;
				}
				else
				{
					if (i == (content.length() - 1))
					{
						lineNum++;
						cl.add(content.substring(istart, content.length()));
					}
				}
			}
			if(lineNum==pageLines||i==(content.length()-1)){
				contentList.add(cl);
				contentList.add((ArrayList<String>) cl.clone());
				contentList.add((ArrayList<String>) cl.clone());
				contentList.add((ArrayList<String>) cl.clone());
				contentList.add((ArrayList<String>) cl.clone());
				
				
				cl = null;
				lineNum = 0;
			}
		}
		 Iterator it1 = contentList.iterator();
		 int i = 0;
		 int j = 0;
	        while(it1.hasNext()){
	        	
	        	i++;
	        	System.out.println("i"+i);
	        	cl = (ArrayList<String>) it1.next();
	        	Iterator it2= cl.iterator();
	        	while(it2.hasNext()){
	        	
		        	j++;
		        	System.out.println("j"+j);
	        		 System.out.println(it2.next());
	        	}
	           
	        }

		return contentList;
	}
	
	public static ArrayList<String> getPageContentString(Paint m_paint,String content,int pageLines,float pageWidth)
	{
		char ch;
		int w = 0;
		int istart = 0;
		int lineNum = 0;
		ArrayList<String> cl = null;
		for (int i = 0; i < content.length(); i++)
		{
			if(cl == null)
				cl = new ArrayList<String>();
			ch = content.charAt(i);
			float[] widths = new float[1];
			String srt = String.valueOf(ch);
			m_paint.getTextWidths(srt, widths);

			if (ch == '\n')
			{
				lineNum++;
				cl.add(content.substring(istart, i));
				istart = i + 1;
				w = 0;
			}
			else
			{
				w += (int) (Math.ceil(widths[0]));
				if (w > pageWidth)
				{
					lineNum++;
					cl.add(content.substring(istart, i));
					istart = i;
					i--;
					w = 0;
				}
				else
				{
					if (i == (content.length() - 1))
					{
						lineNum++;
						cl.add(content.substring(istart, content.length()));
					}
				}
			}
			
		}
	
		return cl;
	}
	
	public static void setFullNoTitleScreen(Activity context){
		context.setTheme(R.style.Theme_Black_NoTitleBar_Fullscreen);
		context.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
}
