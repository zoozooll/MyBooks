package com.chinachip.TextReader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ccbooks.fullscreen.listener.MenuClick;
import com.ccbooks.fullscreen.listener.colorsetting.BesconClick;
import com.ccbooks.fullscreen.listener.colorsetting.BlacknightClick;
import com.ccbooks.fullscreen.listener.colorsetting.ElegantblueClick;
import com.ccbooks.fullscreen.listener.colorsetting.ElegantpowderClick;
import com.ccbooks.fullscreen.listener.colorsetting.ElegantpurpleClick;
import com.ccbooks.fullscreen.listener.colorsetting.ElegantwhiteClick;
import com.ccbooks.fullscreen.listener.colorsetting.ElegantyellowClick;
import com.ccbooks.fullscreen.listener.colorsetting.FreshblueClick;
import com.ccbooks.fullscreen.listener.colorsetting.MattpurpleClick;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;

public class MenuTheme extends MenuFun{
	private TextMenu menuParent;
	private FrameLayout layoutFun;
	private TextReader tr = null;
	//梦幻绿
	public Button btn_bescon = null;
	final public static int COLOR_BESCON = 0xffbcc61f;
	//哑光紫
	public Button btn_mattpurple = null;
	final public static int COLOR_MATTPURPLE = 0xffb57cee;
	//清新蓝
	public Button btn_freshblue = null;
	final public static int COLOR_FRESHBLUE = 0xff77caf7;
	//淡雅粉
	public Button btn_elegantpowder = null;
	final public static int COLOR_ELEGANTPOWDER = 0xfffaeeff;
	//淡雅紫
	public Button btn_elegantpurple = null;
	final public static int COLOR_ELEGANTPURPLE = 0xffefe9fc;
	//淡雅黄
	public Button btn_elegantyellow = null;
	final public static int COLOR_ELEGANTYELLOW = 0xfff7f2ee;
	//淡雅蓝
	public Button btn_elegantblue = null;
	final public static int COLOR_ELEGANTBLUE = 0xffd8e6ed;
	//淡雅白
	public Button btn_elegantwhite = null;
	final public static int COLOR_ELEGANTWHITE = 0xffffffff;
	//夜间黑
	public Button btn_blacknight = null;
	final public static int COLOR_BLACKNIGHT = 0xff000000;
	private int bgColor;
	
	
	public MenuTheme(TextMenu tmObj) {
		menuParent = tmObj;
		this.tr = tmObj.getActParent();
		initView();
		SharedPreferences sp = tr.getSharedPreferences("config", Activity.MODE_PRIVATE);
		bgColor = sp.getInt("bg_color", COLOR_ELEGANTYELLOW);
		initSelectedBGColor(bgColor);
	}
	
	private void initView(){
		layoutFun = (FrameLayout)menuParent.getLayoutMenu().findViewById(R.id.fun_contextual_model);
		//梦幻绿
		btn_bescon = (Button)menuParent.getLayoutMenu().findViewById(R.id.btn_bescon);
		BesconClick besconClick = new BesconClick(menuParent, this);
		btn_bescon.setOnClickListener(besconClick);
		//哑光紫
		btn_mattpurple = (Button)menuParent.getLayoutMenu().findViewById(R.id.btn_mattpurple);
		MattpurpleClick mattpurpleClick = new MattpurpleClick(menuParent, this);
		btn_mattpurple.setOnClickListener(mattpurpleClick);
		//清新蓝
		btn_freshblue = (Button)menuParent.getLayoutMenu().findViewById(R.id.btn_freshblue);
		FreshblueClick freshblueClick = new FreshblueClick(menuParent, this);
		btn_freshblue.setOnClickListener(freshblueClick);
		//淡雅粉
		btn_elegantpowder = (Button)menuParent.getLayoutMenu().findViewById(R.id.btn_elegantpowder);
		ElegantpowderClick elegantpowderClick = new ElegantpowderClick(menuParent, this);
		btn_elegantpowder.setOnClickListener(elegantpowderClick);
		//淡雅紫
		btn_elegantpurple = (Button)menuParent.getLayoutMenu().findViewById(R.id.btn_elegantpurple);
		ElegantpurpleClick elegantpurpleClick = new ElegantpurpleClick(menuParent, this);
		btn_elegantpurple.setOnClickListener(elegantpurpleClick);
		//淡雅黄
		btn_elegantyellow = (Button)menuParent.getLayoutMenu().findViewById(R.id.btn_elegantyellow);
		ElegantyellowClick elegantyellowClick = new ElegantyellowClick(menuParent, this);
		btn_elegantyellow.setOnClickListener(elegantyellowClick);
		//淡雅蓝
		btn_elegantblue = (Button)menuParent.getLayoutMenu().findViewById(R.id.btn_elegantblue);
		ElegantblueClick elegantblueClick = new ElegantblueClick(menuParent, this);
		btn_elegantblue.setOnClickListener(elegantblueClick);
		//淡雅白
		btn_elegantwhite = (Button)menuParent.getLayoutMenu().findViewById(R.id.btn_elegantwhite);
		ElegantwhiteClick elegantwhiteClick = new ElegantwhiteClick(menuParent, this);
		btn_elegantwhite.setOnClickListener(elegantwhiteClick);
		//夜间黑
		btn_blacknight = (Button)menuParent.getLayoutMenu().findViewById(R.id.btn_blacknight);
		BlacknightClick blacknightClick = new BlacknightClick(menuParent, this);
		btn_blacknight.setOnClickListener(blacknightClick);
		
		MenuClick menuclick = new MenuClick();
    	layoutFun.setOnClickListener(menuclick);
		
	}
	
	public void changeThemeByID(int id) {
		TextReader tr = menuParent.getActParent();
		if (tr.getDayOrNight() == TextReader.DAY_LIGHT) {
			tr.setDayOrNight(TextReader.DARK_LIGHT);
			tr.setDayOrDark(TextReader.DARK_LIGHT);
		} else if (tr.getDayOrNight() == TextReader.DARK_LIGHT) {
			tr.setDayOrNight(TextReader.DAY_LIGHT);
			tr.setDayOrDark(TextReader.DAY_LIGHT);
		}
    	tr.reloadText();
	}

	@Override
	public LinearLayout getLayoutFont() {
		return null;
	}

	@Override
	public boolean hide() {
		if(View.GONE == layoutFun.getVisibility())return false;
		layoutFun.setVisibility(View.GONE);
		return true;
	}

	@Override
	public boolean isShowed() {
		if(View.VISIBLE == layoutFun.getVisibility())
			return true;
		else 
			return false;
	}

	@Override
	public boolean show() {
	//	changeThemeByID(0);
		if(View.VISIBLE == layoutFun.getVisibility())return false;
		initSelectedButton(bgColor);
		layoutFun.setVisibility(View.VISIBLE);
		
		return true;
	}
	//梦幻绿
	public void changeBescon(){
		tr.setBGColor(COLOR_BESCON);
		tr.reloadText();
		setButtonFocus();
		btn_bescon.setEnabled(false);
	}                                         
	//哑光紫      
	public void changeMattpurple(){
		tr.setBGColor(COLOR_MATTPURPLE);
		tr.reloadText();
		setButtonFocus();
		btn_mattpurple.setEnabled(false);
	}   
	//清新蓝                                 
	public void changeFreshblue(){
		tr.setBGColor(COLOR_FRESHBLUE);
		tr.reloadText();
		setButtonFocus();
		btn_freshblue.setEnabled(false);
	}  
	//淡雅粉       
	public void changeElegantpowder(){
		tr.setBGColor(COLOR_ELEGANTPOWDER);
		tr.reloadText();
		setButtonFocus();
		btn_elegantpowder.setEnabled(false);
	} 
	//淡雅紫       
	public void changeElegantpurple(){
		tr.setBGColor(COLOR_ELEGANTPURPLE);
		tr.reloadText();
		setButtonFocus();
		btn_elegantpurple.setEnabled(false);
	}                                   
	//淡雅黄                                  
	public void changeElegantyellow(){
		tr.setBGColor(COLOR_ELEGANTYELLOW);
		tr.reloadText();
		setButtonFocus();
		btn_elegantyellow.setEnabled(false);
	}             
	//淡雅蓝                       
	public void changeElegantblue(){
		tr.setBGColor(COLOR_ELEGANTBLUE);
		tr.reloadText();
		setButtonFocus();
		btn_elegantblue.setEnabled(false);
	}                                
	//淡雅白      
	public void changeElegantwhite(){
		tr.setBGColor(COLOR_ELEGANTWHITE);
		tr.reloadText();
		setButtonFocus();
		btn_elegantwhite.setEnabled(false);
	}                                
	//夜间黑                          
	public void changeBlacknight(){
		tr.setBGColor(COLOR_BLACKNIGHT);
		tr.reloadText();
		setButtonFocus();
		btn_blacknight.setEnabled(false);
	}   
	
	//梦幻绿
	public void changeBesconColor(){
		tr.setBGColor(COLOR_BESCON);
	}                                         
	//哑光紫      
	public void changeMattpurpleColor(){
		tr.setBGColor(COLOR_MATTPURPLE);
	}   
	//清新蓝                                 
	public void changeFreshblueColor(){
		tr.setBGColor(COLOR_FRESHBLUE);
	}  
	//淡雅粉       
	public void changeElegantpowderColor(){
		tr.setBGColor(COLOR_ELEGANTPOWDER);
	} 
	//淡雅紫       
	public void changeElegantpurpleColor(){
		tr.setBGColor(COLOR_ELEGANTPURPLE);
	}                                   
	//淡雅黄                                  
	public void changeElegantyellowColor(){
		tr.setBGColor(COLOR_ELEGANTYELLOW);
	}             
	//淡雅蓝                       
	public void changeElegantblueColor(){
		tr.setBGColor(COLOR_ELEGANTBLUE);
	}                                
	//淡雅白      
	public void changeElegantwhiteColor(){
		tr.setBGColor(COLOR_ELEGANTWHITE);
	}                                
	//夜间黑                          
	public void changeBlacknightColor(){
		tr.setBGColor(COLOR_BLACKNIGHT);
	}   
	
	//梦幻绿
	public void changeBesconButton(){
		setButtonFocus();
		btn_bescon.setEnabled(false);
	}                                         
	//哑光紫      
	public void changeMattpurpleButton(){
		setButtonFocus();
		btn_mattpurple.setEnabled(false);
	}   
	//清新蓝                                 
	public void changeFreshblueButton(){
		setButtonFocus();
		btn_freshblue.setEnabled(false);
	}  
	//淡雅粉       
	public void changeElegantpowderButton(){
		setButtonFocus();
		btn_elegantpowder.setEnabled(false);
	} 
	//淡雅紫       
	public void changeElegantpurpleButton(){
		setButtonFocus();
		btn_elegantpurple.setEnabled(false);
	}                                   
	//淡雅黄                                  
	public void changeElegantyellowButton(){
		setButtonFocus();
		btn_elegantyellow.setEnabled(false);
	}             
	//淡雅蓝                       
	public void changeElegantblueButton(){
		setButtonFocus();
		btn_elegantblue.setEnabled(false);
	}                                
	//淡雅白      
	public void changeElegantwhiteButton(){
		setButtonFocus();
		btn_elegantwhite.setEnabled(false);
	}                                
	//夜间黑                          
	public void changeBlacknightButton(){
		setButtonFocus();
		btn_blacknight.setEnabled(false);
	}   
	
	private void setButtonFocus(){
		//梦幻绿               
		btn_bescon.setEnabled(true);
		//哑光紫               
		btn_mattpurple.setEnabled(true); 
		//清新蓝               
		btn_freshblue.setEnabled(true);
		//淡雅粉               
		btn_elegantpowder.setEnabled(true);
		//淡雅紫               
		btn_elegantpurple.setEnabled(true);
		//淡雅黄               
		btn_elegantyellow.setEnabled(true); 
		//淡雅蓝               
		btn_elegantblue.setEnabled(true);
		//淡雅白               
		btn_elegantwhite.setEnabled(true);
		//夜间黑               
		btn_blacknight.setEnabled(true);
	}
	
	private void initSelectedButton(int bgColor){
		setButtonFocus();
		switch (bgColor) {
		case COLOR_BESCON:
			changeBesconButton();
			break;
		case COLOR_MATTPURPLE:
			changeMattpurpleButton();
			break;
		case COLOR_FRESHBLUE:
			changeFreshblueButton();
			break;
		case COLOR_ELEGANTPOWDER:
			changeElegantpowderButton();
			break;
		case COLOR_ELEGANTPURPLE:
			changeElegantpurpleButton();
			break;
		case COLOR_ELEGANTYELLOW:
			changeElegantyellowButton();
			break;
		case COLOR_ELEGANTBLUE:
			changeElegantblueButton();
			break;
		case COLOR_ELEGANTWHITE:
			changeElegantwhiteButton();
			break;
		case COLOR_BLACKNIGHT:
			changeBlacknightButton();
			break;
		default:
			changeElegantyellowButton();
			break;
		}
	}
	
	
	private void initSelectedBGColor(int bgColor){
		setButtonFocus();
		switch (bgColor) {
		case COLOR_BESCON:
			changeBesconColor();
			break;
		case COLOR_MATTPURPLE:
			changeMattpurpleColor();
			break;
		case COLOR_FRESHBLUE:
			changeFreshblueColor();
			break;
		case COLOR_ELEGANTPOWDER:
			changeElegantpowderColor();
			break;
		case COLOR_ELEGANTPURPLE:
			changeElegantpurpleColor();
			break;
		case COLOR_ELEGANTYELLOW:
			changeElegantyellowColor();
			break;
		case COLOR_ELEGANTBLUE:
			changeElegantblueColor();
			break;
		case COLOR_ELEGANTWHITE:
			changeElegantwhiteColor();
			break;
		case COLOR_BLACKNIGHT:
			changeBlacknightColor();
			break;
		default:
			changeElegantyellowColor();
			break;
		}
	}
}                                     
