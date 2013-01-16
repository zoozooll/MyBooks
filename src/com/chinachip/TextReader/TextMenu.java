package com.chinachip.TextReader;

import com.ccbooks.fullscreen.listener.MenuBklightClick;
import com.ccbooks.fullscreen.listener.MenuBkmkClick;
import com.ccbooks.fullscreen.listener.MenuBookListClick;
import com.ccbooks.fullscreen.listener.MenuCommentClick;
import com.ccbooks.fullscreen.listener.MenuFontTTFClick;
import com.ccbooks.fullscreen.listener.MenuPageModeClick;
import com.ccbooks.fullscreen.listener.MenuCatalogClick;
import com.ccbooks.fullscreen.listener.MenuCharsetClick;
import com.ccbooks.fullscreen.listener.MenuClick;
import com.ccbooks.fullscreen.listener.MenuFontClick;
import com.ccbooks.fullscreen.listener.MenuSettingClick;
import com.ccbooks.fullscreen.listener.MenuThemeClick;
import com.ccbooks.fullscreen.listener.MenuTurntoClick;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TextMenu {
	private TextReader actParent;
	private FrameLayout layoutMenu;	
	private LinearLayout layoutLevel_1;
	private FrameLayout layoutLevel_2;
	private MenuFont menuFontFun;
	public MenuBklight menuBklightFun;
	private MenuBkmk menuBkmkFun;
	public MenuTurnto menuTurntoFun;
	public MenuSetting menuSettingFun;
	private MenuCatalog menuCatalogFun;
	private MenuCharset menuCharsetFun;
	private MenuPageMode menuPageModeFun;
	private MenuFontTTF menuFontTTFFun;
	private MenuTheme menuThemeFun;
	private MenuBookList menuBookListFun;
	private MenuComment menuCommentFun;
	private ImageButton btnMenuFont;
	private ImageButton btnMenuBklight;
	private ImageButton btnMenuBkmk;
	private ImageButton btnMenuTurnto;
	private ImageButton btnMenuSetting;
	private ImageButton btnCatalog;
	private ImageButton btnCharset;
	private ImageButton btnPageMode;
	private ImageButton btnTheme;
	private ImageButton btnFontTTF;
	private ImageButton btnBookList;
	private ImageButton btnMenuComment;
	
	public TextMenu(TextReader trObj){
		actParent = trObj;
		//layoutMenu = (LinearLayout)actParent.getLayoutInflater().inflate(R.layout.menu, null);
		layoutMenu = (FrameLayout)(((ViewStub)actParent.findViewById(R.id.viewstub_menu)).inflate());
		initLevel2();
		initLevel1();
	}
	
	public void initLevel1(){
		layoutLevel_1 = (LinearLayout)layoutMenu.findViewById(R.id.menu_level_1);
		
		btnMenuSetting = (ImageButton)layoutLevel_1.findViewById(R.id.menu1);
    	MenuSettingClick settingclick = new MenuSettingClick(this,menuSettingFun);
    	btnMenuSetting.setOnClickListener(settingclick);
    	
    	btnMenuComment = (ImageButton)layoutLevel_1.findViewById(R.id.menu2);
    	MenuCommentClick commentClick = new MenuCommentClick(this, menuCommentFun);
    	btnMenuComment.setOnClickListener(commentClick);
		
    	btnMenuBkmk = (ImageButton)layoutLevel_1.findViewById(R.id.menu3);
    	MenuBkmkClick bkmkclick = new MenuBkmkClick(this,menuBkmkFun);
    	btnMenuBkmk.setOnClickListener(bkmkclick);
    	
    	btnMenuTurnto = (ImageButton)layoutLevel_1.findViewById(R.id.menu4);
    	MenuTurntoClick turntoclick = new MenuTurntoClick(this,menuTurntoFun);
    	btnMenuTurnto.setOnClickListener(turntoclick);
    	
    	btnBookList = (ImageButton)layoutLevel_1.findViewById(R.id.menu5);
    	MenuBookListClick bookListClick = new MenuBookListClick(this, menuBookListFun);
    	btnBookList.setOnClickListener(bookListClick);   	
    	
    	btnCatalog = (ImageButton)layoutLevel_1.findViewById(R.id.menu6);
    	MenuCatalogClick menuCatalog = new MenuCatalogClick(this, menuCatalogFun);
    	btnCatalog.setOnClickListener(menuCatalog);
    	
    	btnTheme = (ImageButton)layoutLevel_1.findViewById(R.id.menu7);
    	MenuThemeClick themeClick = new MenuThemeClick(this, menuThemeFun);
    	btnTheme.setOnClickListener(themeClick);
    	
    	btnPageMode = (ImageButton)layoutLevel_1.findViewById(R.id.menu8);
    	MenuPageModeClick pageModeClick = new MenuPageModeClick(this, menuPageModeFun);
    	btnPageMode.setOnClickListener(pageModeClick);  	
    	
//    	btnCharset = (ImageButton)layoutLevel_1.findViewById(R.id.menu9);
//    	MenuCharsetClick charsetClick = new MenuCharsetClick(this, menuCharsetFun);
//    	btnCharset.setOnClickListener(charsetClick);
//
//    	
//    	
//    	btnFontTTF = (ImageButton)layoutLevel_1.findViewById(R.id.menu11);
//    	MenuFontTTFClick fontTTFClick = new MenuFontTTFClick(this, menuFontTTFFun);
//    	btnFontTTF.setOnClickListener(fontTTFClick);
//
//    	btnMenuFont = (ImageButton)layoutLevel_1.findViewById(R.id.menu1);
//    	MenuFontClick fontclick = new MenuFontClick(this,menuFontFun);
//    	btnMenuFont.setOnClickListener(fontclick);
//    	
//    	btnMenuBklight = (ImageButton)layoutLevel_1.findViewById(R.id.menu2);
//    	MenuBklightClick bklightclick = new MenuBklightClick(this,menuBklightFun);
//    	btnMenuBklight.setOnClickListener(bklightclick);
//    	
    	MenuClick menuclick = new MenuClick();
    	layoutLevel_1.setOnClickListener(menuclick);
	}
	
	public void initLevel2(){
		layoutLevel_2 = (FrameLayout)layoutMenu.findViewById(R.id.menu_level_2);
	//	menuFontFun = new MenuFont(this);
	//	menuBklightFun = new MenuBklight(this);
		menuBkmkFun = new MenuBkmk(this);
		menuTurntoFun = new MenuTurnto(this);
		menuSettingFun = new MenuSetting(this);
		menuCatalogFun = new MenuCatalog(this);
	//	menuCharsetFun = new MenuCharset(this);
		menuPageModeFun = new MenuPageMode(this);
		menuThemeFun = new MenuTheme(this);
		menuBookListFun = new MenuBookList(this);
	//	menuFontTTFFun = new MenuFontTTF(this);
		menuCommentFun = new MenuComment(this);
	}	
	
	public void initFont(){
		
	}
	
	public void setAllLevel2Gone() {
	//	menuFontFun.hide();
	//	menuBklightFun.hide();
		menuBkmkFun.hide();
		menuTurntoFun.hide();
		menuSettingFun.hide();
		menuCommentFun.hide();
		menuThemeFun.hide();
		//menuCatalogFun.hide();
	}
	
	public TextReader getActParent() {
		return actParent;
	}

	public void setActParent(TextReader actParent) {
		this.actParent = actParent;
	}
	
	public FrameLayout getLayoutMenu() {
		return layoutMenu;
	}
	
	public void setLayoutMenu(FrameLayout layoutMenu) {
		this.layoutMenu = layoutMenu;
	}
	
	public boolean isMenuShowed() {
		if(View.GONE == layoutMenu.getVisibility())
			return false;
		else
			return true;
	}
	
	public boolean isShowed(){
		if(View.GONE != layoutMenu.getVisibility()) return false;
		else return true;
	}
	
	public void turntoBookRequest(){
		hide();
	}
	
	public boolean show() {
		if(View.GONE != layoutMenu.getVisibility()) return false;
		TranslateAnimation move = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		move.setDuration(100);
		layoutMenu.startAnimation(move);
		layoutMenu.setVisibility(View.VISIBLE);
		return true;
	}
	
	public boolean hide() {
		if(View.VISIBLE != layoutMenu.getVisibility()) return false;
		setAllLevel2Gone();
		actParent.readModeCheck();
		actParent.enableTouch();
		TranslateAnimation move = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
		move.setDuration(100);
		layoutMenu.startAnimation(move);
		layoutMenu.setVisibility(View.GONE);
		return true;		
	}
	public MenuSetting getMenuSettingFun() {
		return menuSettingFun;
	}

	public void setMenuSettingFun(MenuSetting menuSettingFun) {
		this.menuSettingFun = menuSettingFun;
	}
	
	public MenuComment getMenuCommentFun(){
		return menuCommentFun;
	}
}
