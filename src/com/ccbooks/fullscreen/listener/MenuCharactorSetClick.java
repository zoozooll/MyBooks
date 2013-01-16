package com.ccbooks.fullscreen.listener;

import java.io.IOException;

import com.ccbooks.adapter.CharactorSetAdapter;
import com.ccbooks.util.StringConfig;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;
import com.chinachip.TextReader.MenuBkmk;
import com.chinachip.TextReader.MenuCharset;
import com.chinachip.TextReader.MenuSetting;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuCharactorSetClick implements OnItemClickListener {
	static final String DEBUG_TAG = "jf";
	private MenuSetting funObj;

	public MenuCharactorSetClick(MenuSetting mc) {
		funObj = mc;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		funObj.changeCharsetByID(arg2);
	}
}
