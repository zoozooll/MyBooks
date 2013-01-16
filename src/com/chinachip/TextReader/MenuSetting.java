package com.chinachip.TextReader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ccbooks.adapter.CharactorSetAdapter;
import com.ccbooks.adapter.FontAdapter;

import com.ccbooks.dialog.CharDialog;
import com.ccbooks.dialog.FSCharDialog;
import com.ccbooks.dialog.FSTTFDialog;
import com.ccbooks.fullscreen.listener.MenuBklightSeekBarChange;
import com.ccbooks.fullscreen.listener.MenuCharactorSetClick;
import com.ccbooks.fullscreen.listener.MenuCharsetClick;
import com.ccbooks.fullscreen.listener.MenuClick;
import com.ccbooks.fullscreen.listener.MenuFontSeekBarChange;
import com.ccbooks.fullscreen.listener.MenuFontTTFClick;
import com.ccbooks.fullscreen.listener.MenuFontTTFSetClick;
import com.ccbooks.fullscreen.listener.MenuSettingSlideCheckListener;
import com.ccbooks.listener.charactorSet.CharactorSetClick;
import com.ccbooks.listener.charactorSet.FontTTFSetClick;


import com.ccbooks.util.FontListItem;
import com.ccbooks.util.StringConfig;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;

public class MenuSetting  extends MenuFun{
	public TextMenu menuParent;
	private LinearLayout layoutFun;
	private CheckBox slideCheck;
	private Button transMode;
	private Button btnDayOrDark;	//选择白天或者晚上浏览模式按钮;
	private boolean slideOpen;
	private TextReader tr;
	private Button btnFScharactor;
	public AlertDialog aDCharset;
	public CharactorSetAdapter charactorSetAdapter;
	//亮度
	private SeekBar lightSeekBar;
	private SeekBar fontSeekBar;
	private WindowManager.LayoutParams bl;
	private int blLevel;
	public static float ness ; 	
	//字体大小
	final int FONTZISE_MIN = 18;
	private TextView textFont;
	private int fontSize;
	
	//ttf字体设置
	public String ttfFileName = "Clockopia";
	
	public ArrayList<FontListItem> fontList = null;
	
	public FontAdapter fontAdapter = null;
	
	public Button btnFontTTFSetting = null;
	
	public Button btnCharSetSetting = null;
	public MenuSetting(TextMenu tmObj){
		menuParent = tmObj;
		this.tr = tmObj.getActParent();
		layoutFun = (LinearLayout)menuParent.getLayoutMenu().findViewById(R.id.fun_setting);
		slideCheck = (CheckBox)menuParent.getLayoutMenu().findViewById(R.id.check_slide);
		
		if(menuParent.getActParent().getReadMode() == 1)
			slideCheck.setChecked(true);
		else
			slideCheck.setChecked(false);
		MenuSettingSlideCheckListener slideCheckListener= new MenuSettingSlideCheckListener(this);
		slideCheck.setOnCheckedChangeListener(slideCheckListener);

		//亮度
		initLightSetting();
		initFontSetting();
		initFontTTFSetting();
		initCharset();
		MenuClick menuclick = new MenuClick();
    	layoutFun.setOnClickListener(menuclick);
	}
	//亮度设置初始化
	private void initLightSetting(){
		bl = menuParent.getActParent().getWindow().getAttributes();
		lightSeekBar = (SeekBar)menuParent.getLayoutMenu().findViewById(R.id.seebar_bklight);
		SharedPreferences sp = menuParent.getActParent().getSharedPreferences("config", Activity.MODE_PRIVATE);
		ness = sp.getFloat("ness", 0.7f);
		
		lightSeekBar.setProgress((int)ness);
		brightnessMax(ness);
		MenuBklightSeekBarChange seebarlistener = new MenuBklightSeekBarChange(this);
		lightSeekBar.setOnSeekBarChangeListener(seebarlistener);	
	}
	private void initFontSetting(){
		fontSize = menuParent.getActParent().getEngine().getFontsize();
		textFont = (TextView)menuParent.getLayoutMenu().findViewById(R.id.fun_font_info);
		textFont.setText(String.valueOf(fontSize));
		fontSeekBar = (SeekBar)menuParent.getLayoutMenu().findViewById(R.id.seebar_font);
		fontSeekBar.setProgress((fontSize-FONTZISE_MIN)/2);
		MenuFontSeekBarChange seebarlistener = new MenuFontSeekBarChange(this);
		fontSeekBar.setOnSeekBarChangeListener(seebarlistener);	
	}
	
	private void initFontTTFSetting(){
		btnFontTTFSetting = (Button)menuParent.getLayoutMenu().findViewById(R.id.font_setting);
		MenuFontTTFClick fontTTFClick = new MenuFontTTFClick(menuParent,this);
		btnFontTTFSetting.setOnClickListener(fontTTFClick);
	}
	public void slideModeChangeEvent() {
		slideOpen = !slideOpen;
	}
	
	public boolean isShowed(){
		if(View.VISIBLE == layoutFun.getVisibility())
			return true;
		else 
			return false;
	}
	
	public boolean show(){
		if(View.VISIBLE == layoutFun.getVisibility())return false;
		if(menuParent.getActParent().getReadMode() == 1)
			slideOpen = true;
		else
			slideOpen = false;
		layoutFun.setVisibility(View.VISIBLE);
		lightShow();
		fontShow();
		
		return true;
	}
	
	private void lightShow(){
		bl = menuParent.getActParent().getWindow().getAttributes();
		SharedPreferences sp = menuParent.getActParent().getSharedPreferences("config", Activity.MODE_PRIVATE);
		ness = sp.getFloat("ness", 0.5f);
		lightSeekBar.setProgress((int)(ness));
	}
	
	public boolean hide(){
		lightHide();
		if(View.GONE == layoutFun.getVisibility())return false;
		if(slideOpen)
			menuParent.getActParent().setReadModeRequest(1);
		else
			menuParent.getActParent().setReadModeRequest(0);
		layoutFun.setVisibility(View.GONE);
		if(View.GONE == layoutFun.getVisibility())return false;
		layoutFun.setVisibility(View.GONE);
		return true;
	}

	private void lightHide(){
		SharedPreferences sp = menuParent.getActParent().getSharedPreferences("config", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat("ness", ness);
		editor.commit();
	}
	
	@Override
	public LinearLayout getLayoutFont() {
		// TODO Auto-generated method stub
		return layoutFun;
	}
	
	public void bkmkListEvent(){
		turnToBkmkList();
	}
	
	public boolean turnToBkmkList(){
		return true;
	}
	

	private void brightnessMax(float ness) {
		ness = ness/255;
		WindowManager.LayoutParams lp = menuParent.getActParent().getWindow().getAttributes();
		if(ness>0.02)
		{
			lp.screenBrightness = ness;
		}
		else
		{
			lp.screenBrightness = 0.02f;
		}
			
		menuParent.getActParent().getWindow().setAttributes(lp);
	}
	public void lightProgressChangeEvent(int size){
		if(size < 10)size=10;
		if(size > 255)size=255;
		bl.screenBrightness = ((float)size)/(float)255;
		
		ness = size;
		menuParent.getActParent().getWindow().setAttributes(bl);
		
	}
	//字体
	public void fontProgressChangeEvent(int size){
		fontSize = size*2+FONTZISE_MIN;
		textFont.setText(String.valueOf(fontSize));		
		menuParent.getActParent().getEngine().setFontsize(fontSize);
		menuParent.getActParent().reloadText();
	//	menuParent.getActParent().CommentPaint();
		if(menuParent.getActParent().getIsComment()){
			Toast.makeText(menuParent.getActParent(), R.string.changeFontTips, Toast.LENGTH_SHORT).show();
			menuParent.getActParent().getCommentView().clearView();
			menuParent.getActParent().setIsComment(false);
		}
		if(menuParent.getActParent().getCommentView().getLineListFont() != null){
			if(menuParent.getActParent().getCommentView().getLineListFont().get(fontSize+"") != null){
				Toast.makeText(menuParent.getActParent(), "字体"+fontSize+"下有批注", Toast.LENGTH_SHORT).show();
			}
		}	
	}
	
	private void fontShow(){
		textFont.setText(String.valueOf(fontSize));
		fontSeekBar.setProgress((fontSize-FONTZISE_MIN)/2);
	}
	
	public boolean turnToFontTTFList(){
		LinearLayout bkmkListLayout = (LinearLayout)tr.getLayoutInflater().inflate(R.layout.ttf_dialog, null);
		String fontName = ttfFileName;
		TextReader.bo.updateFont(TextReader.book.id, fontName);
		fontList = menuParent.getActParent().font.getFontListItem();
		int selected = 0;
		for(int i = 0; i< fontList.size(); i++ )
		{
			if(fontList.get(i).getTtfFileName().equals(fontName))
				selected = i;
		}
		fontAdapter = new FontAdapter(menuParent.getActParent(),fontList,selected);
		fontAdapter.setMode(2);
		ListView list = (ListView)bkmkListLayout.findViewById(R.id.fontff_list);
		list.setAdapter(fontAdapter);
		list.setOnItemClickListener(new MenuFontTTFSetClick(this));
		new FSTTFDialog(tr,bkmkListLayout).show();
//		AlertDialog aDCharset = new AlertDialog.Builder(this)
//				.setTitle(R.string.fontTTF_list)
//				.setView(bkmkListLayout)
//				.create();
//		aDCharset.setCancelable(true);
//		aDCharset.setCanceledOnTouchOutside(true);
//		aDCharset.show();
		return true;
	}
	public void changeTTFByID(int id) {
		fontAdapter.selected = id;
		String fontName = fontList.get(id).getTtfFileName();
		long tempoffset = menuParent.getActParent().getCurPostion();
		menuParent.getActParent().bo.updateFont(menuParent.getActParent().book.id, fontName);
		menuParent.getActParent().font.setFont(fontName);
		menuParent.getActParent().bc.setPaint(menuParent.getActParent().font.mPaint);
		menuParent.getActParent().ttfFileName = fontName;
		SharedPreferences sp = menuParent.getActParent().getSharedPreferences("config", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("ttfFileName", fontName);
		editor.commit();
//		TextView textFont.setText(String.valueOf(fontSize));		
//		menuParent.getActParent().getEngine().setFontsize(fontSize);
//		menuParent.getActParent().reloadText();
//		
		menuParent.getActParent().setCurPostion(tempoffset);
		menuParent.getActParent().reloadText();
      //  menuParent.turntoBookRequest();
		fontAdapter.notifyDataSetChanged();
	}
	public void ttfShow(){
		turnToFontTTFList();
	}
	
	//charset字符编码设置
	
	private void initCharset(){
		btnCharSetSetting = (Button)menuParent.getLayoutMenu().findViewById(R.id.charset_setting);
		MenuCharsetClick charsetClick = new MenuCharsetClick(menuParent, this);
		btnCharSetSetting.setOnClickListener(charsetClick);
	}
	public boolean turnToFontList(){
		LinearLayout bkmkListLayout = (LinearLayout)tr.getLayoutInflater().inflate(R.layout.char_dialog, null);
		String charset = tr.bc.getEnc();
		tr.bo.updateChartset(tr.book.id, charset);
		String[] charList = StringConfig.CHARSETS;
		int selected = 0;
		for(int i = 0; i< charList.length; i++ )
		{
			if(charList[i].equals(charset))
				selected = i;
		}
		charactorSetAdapter = new CharactorSetAdapter(tr,StringConfig.CHARSETS,selected);
		charactorSetAdapter.setMode(2);
		ListView list = (ListView)bkmkListLayout.findViewById(R.id.charactor_list);
		list.setAdapter(charactorSetAdapter);
		list.setOnItemClickListener(new MenuCharactorSetClick(this));
		new FSCharDialog(tr,bkmkListLayout).show();
		
//		charDailog.show();
//		AlertDialog aDCharset = new AlertDialog.Builder(this)
//				.setIcon(R.drawable.icon)
//				.setTitle(R.string.charactor_list)
//				.setView(bkmkListLayout)
//				.create();
//		aDCharset.setCancelable(true);
//		aDCharset.setCanceledOnTouchOutside(true);
//		aDCharset.show();
		return true;
	}
	public void charSetshow(){
		turnToFontList();
	}
	public void changeCharsetByID(int id) {
		charactorSetAdapter.selected = id;
		String charSet = StringConfig.CHARSETS[id];
		long tempoffset = menuParent.getActParent().getCurPostion();
		menuParent.getActParent().bo.updateChartset(menuParent.getActParent().book.id, charSet);
		menuParent.getActParent().bc.setCharset(charSet);

		menuParent.getActParent().setCurPostion(tempoffset);
		menuParent.getActParent().reloadText();
     	charactorSetAdapter.notifyDataSetChanged();
	}
}
