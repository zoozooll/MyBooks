package com.ccbooks.myUtil;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class StringTool {

	public static int getContentMiddleEnterIndex(String content,int lineCount){
		int midNo = 0 ;
		for(int i = 0 ;i< lineCount/2 ;i ++){
			int temp = content.indexOf('\n', midNo);
			if (temp>0){
				midNo = content.indexOf('\n', midNo)+1;
			}else {
				break;
			}
			
		}
		return midNo;
	}
	
	/**改方法显示第num个出现letter字符的索引
	 * @param str 字符串
	 * @param letter 搜索的字符
	 * @param num 第几次出现
	 * @return 第几次出现的位置索引
	 * */
	public static int getContentMiddleIndex (String str,char letter,int num){
		/*
	int i = 0;
	int m = 0;
	char c = new String(letter).charAt(0);
	char [] ch = str.toCharArray();
	 for(int j=0; j<ch.length; j++){
	 if(ch[j] == c){
	 i++;
	 if(i == num){
	 m = j;
	 break;
	 }
	 }
	 }
	 return m;
	 */
	 	int i = 0;
	 	int m = 0 ;
	 	for (int j = 0;j<str.length();j++){
	 		if (str.charAt(j)==letter) {
	 			i++;
	 			if (i==num){
	 				
	 			m = j;
	 			break;
	 			}
	 		}
	 	}
	 	return m;
	 }
	
	/**过滤无关的显示字符*/
	public static String cleanString(String str){
		return str.replace('\r', '\u0000').replace('\t', '\u0000').replace('\n', '\u0000');
	}
	
	/**获得纯净的字符串数组*/
	public static List<String> getCleanList(List<String> list){
		if (list == null){
			return null;
		}
		List<String> arrStr = new ArrayList<String>();
		for (int i = 0;i<list.size();i++){
			String str = cleanString(list.get(i));
			arrStr.add(str);
		}
		return arrStr;
	}
	/**调整字符串长度，超过长度即显示省略号
	 * @param num: 字符宽度，中文显示2，其他为1;
	 * */
	public static String subTooLongString(String str,int num){
		return str;
	}
	
	/**
	* 计算字符串的字节长度(字母数字计1，汉字及标点计2) 
	* 
	*/ 
	public static int byteLength(String string) {
		int count = 0;
		for (int i = 0; i < string.length(); i++) {
			if (Integer.toHexString(string.charAt(i)).length() == 4) {
				count += 2;
			} else {
				count++;
			}
		}
		return count;
	} 

	/**
	* 按指定长度，省略字符串部分字符 
	* @para String 字符串 
	* @para length 保留字符串长度 
	* @return 省略后的字符串 
	*/ 
	public static String omitString(String string, int length) {
		StringBuffer sb = new StringBuffer();
		if (byteLength(string) > length) {
			int count = 0;
			for (int i = 0; i < string.length(); i++) {
				char temp = string.charAt(i);
				if (Integer.toHexString(temp).length() == 4) {
					count += 2;
				} else {
					count++;
				}
				if (count < length - 3) {
					sb.append(temp);
				}
				if (count == length - 3) {
					sb.append(temp);
					sb.append("...");
					break;
				}
				if (count > length - 3) {
					// sb.append(" ");
					sb.append("...");
					break;
				}
			}
			if (count < length - 3)
				sb.append("...");
		} else {
			sb.append(string);
		}
		return sb.toString();
	} 
	
	/**
	 * Drawable转为Bitmap
	 * @param drawable
	 * @return
	 * @date 2011-5-24下午03:08:31
	 * @author Lee Chok Kong
	 */
    public static Bitmap drawableToBitmap(Drawable drawable) {  
        
        Bitmap bitmap = Bitmap  
                        .createBitmap(  
                                        drawable.getIntrinsicWidth(),  
                                        drawable.getIntrinsicHeight(),  
                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                                        : Bitmap.Config.RGB_565);  
        Canvas canvas = new Canvas(bitmap);  
        //canvas.setBitmap(bitmap);  
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
        drawable.draw(canvas);  
        return bitmap;  
    }  
    
    public static Bitmap drawableToBitmap(int width,int height,Drawable drawable) {  
        
        Bitmap bitmap = Bitmap.createBitmap(  
        		width,  
        		height,  
            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                       : Bitmap.Config.RGB_565);  
        Canvas canvas = new Canvas(bitmap);  
        //canvas.setBitmap(bitmap);  
        drawable.setBounds(0, 0, width, height);  
        drawable.draw(canvas); 
        return bitmap;  
    }  
}
