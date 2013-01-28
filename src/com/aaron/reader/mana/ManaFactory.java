/**
 * 
 */
package com.aaron.reader.mana;

import java.lang.ref.SoftReference;

import android.content.Context;

/**
 * 
 * @author Aaron Lee writed at 下午5:48:34 2013-1-21
 */
public class ManaFactory {
	
	private static SoftReference<BookSource> sBookSource ;

	public static BookSource initSource(Context context) {
		if (sBookSource.get() == null) {
			sBookSource = new SoftReference<BookSource>(new BookSource(context));
		}
		return sBookSource.get();
	}
}
