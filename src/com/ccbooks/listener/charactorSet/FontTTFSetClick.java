package com.ccbooks.listener.charactorSet;

import java.io.IOException;

import com.ccbooks.adapter.CharactorSetAdapter;
import com.ccbooks.adapter.FontAdapter;
import com.ccbooks.util.StringConfig;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FontTTFSetClick implements OnItemClickListener{
	
	private BookContentView bcv;
	private FontAdapter fontAdapter; //亮度调节悬浮框；
	

	public FontTTFSetClick(BookContentView bcv,FontAdapter fontAdapter) {
		super();
		this.bcv=bcv;
		this.fontAdapter=fontAdapter;
		
	}

	
	
	// 点击背景按钮；
	  
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			fontAdapter.selected = (int) arg3;
			String fontName = bcv.fontList.get((int)arg3).getTtfFileName();
			bcv.bo.updateFont(bcv.book.id, fontName);
			bcv.font.setFont(fontName);
			bcv.bc.fu.setPaint(bcv.font.getPaint());
		
			bcv.ttfFileName = fontName;
			SharedPreferences sp =bcv.getSharedPreferences("config", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("ttfFileName", fontName);
			editor.commit();
			new Thread(bcv.bct).start();
			fontAdapter.notifyDataSetChanged();
			
		}



	
	

	
}
