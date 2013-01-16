/**
 * 
 */
package com.kang.test;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * test class
 * @date 2011-4-7  02:48:17
 * @author Lee Chok Kong
 */
public class TestClass {
	
	public static final String TAG= "zkli";

	public static void println(String... args){
		String str = "=====";
		for (int i = 0; i<args.length; i++){
			str+=args[i]+"====";
		}
		Log.i(TAG, str);
	}
	
    public static void getMemoryInfo(Context activity) {  
        final ActivityManager activityManager = 
        (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);  
        ActivityManager.MemoryInfo outInfo = 
        new ActivityManager.MemoryInfo();  
        activityManager.getMemoryInfo(outInfo);  
        println("tf",String.valueOf(outInfo.availMem));

        }  
}
