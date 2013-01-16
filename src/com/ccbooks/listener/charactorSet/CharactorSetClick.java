package com.ccbooks.listener.charactorSet;

import java.io.IOException;

import com.ccbooks.adapter.CharactorSetAdapter;
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

public class CharactorSetClick implements OnItemClickListener{
	
	private BookContentView bcv;
	private CharactorSetAdapter charactorSetAdapter; //亮度调节悬浮框；
	

	public CharactorSetClick(BookContentView bcv,CharactorSetAdapter charactorSetAdapter) {
		super();
		this.bcv=bcv;
		this.charactorSetAdapter=charactorSetAdapter;
		
	}

	
	
	// 点击背景按钮；
	  
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			charactorSetAdapter.selected = (int) arg3;
			String charSet = StringConfig.CHARSETS[(int) arg3];
			bcv.bo.updateChartset(bcv.book.id, charSet);
			
			bcv.bc.setCharset(charSet);
			new Thread(bcv.bct).start();
			charactorSetAdapter.notifyDataSetChanged();
			
		}



	
	

	
}
