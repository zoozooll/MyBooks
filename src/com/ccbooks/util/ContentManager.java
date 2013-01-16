package com.ccbooks.util;

import java.io.UnsupportedEncodingException;

import com.chinachip.ccbooks.core.BookCore;

public class ContentManager {

	/**
	 * 获得当前索引，出去异常;
	 * */
	public static long getCurrIndexAtOutOfException(BookCore bc){
		long num =0;
		try {
			num = bc.getCurrIndexAt();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			num = -1;
			e.printStackTrace();
		} catch (Exception e){
			num = -1;
			e.printStackTrace();
		}
		return num;
	}
}
