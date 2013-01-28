/**
 * 
 */
package com.aaron.reader.shelf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * 
 * @author Aaron Lee writed at 下午8:40:38 2013-1-27
 */
public class ShelfAdapter extends CursorAdapter {
	
	

	/**
	 * @param context
	 * @param c
	 * @param autoRequery
	 * @author Aaron Lee writed at 2013-1-27 下午8:41:17
	 */
	public ShelfAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	/**
	 * @param context
	 * @param c
	 * @param flags
	 * @author Aaron Lee writed at 2013-1-27 下午8:41:17
	 */
	@SuppressLint("NewApi")
	public ShelfAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	/** (non-Javadoc)
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 * @author writed by Aaron Lee
	 * writed at 下午8:40:39 2013-1-27
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return null;
	}

	/** (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 * @author writed by Aaron Lee
	 * writed at 下午8:40:39 2013-1-27
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {

	}

}
