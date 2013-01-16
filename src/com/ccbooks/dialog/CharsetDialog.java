package com.ccbooks.dialog;

import com.ccbooks.bo.ContentBo;
import com.ccbooks.util.StringConfig;
import com.ccbooks.view.BookContentView1;
import com.ccbooks.view.R;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CharsetDialog extends Dialog {

	private BookContentView1 context;
	private TextView tvMainContent;
	private int charsetsIndex;
	
	public CharsetDialog(BookContentView1 context,TextView tvMainContent, int charsetsIndex) {
		super(context);
		this.context = context;
		this.tvMainContent = tvMainContent;
		this.charsetsIndex = charsetsIndex;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.book_content1_chartset);
		ListView lv = (ListView) findViewById(R.id.lvChart);
		lv.setAdapter(new ArrayAdapter<String>(context,
				  android.R.layout.simple_list_item_single_choice, StringConfig.CHARSETS));
		lv.setItemChecked(charsetsIndex, true);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				context.count = 0 ;
				tvMainContent.setText(context.getStringFromFile(StringConfig.CHARSETS[arg2]));
				ContentBo cbo = new ContentBo(context,context.book.bookpath,StringConfig.CHARSETS[arg2]);
				ContentBo.flag = true;
				new Thread(context.cbo).start();
				context.charsetsIndex = arg2;
				cancel();
			}
			
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		LinearLayout bookContentCharsetDia = (LinearLayout) findViewById(R.id.bookContentCharsetDia);
		if (event.getX() < 0 || event.getY() < 0
				|| event.getX() > bookContentCharsetDia.getWidth()
				|| event.getY() > bookContentCharsetDia.getHeight()) {
			this.cancel();
		}
		return super.onTouchEvent(event);
	}

}
