package com.chinachip.TextReader;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import com.ccbooks.adapter.CharactorSetAdapter;
import com.ccbooks.fullscreen.listener.MenuClick;
import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;

public class MenuComment  extends MenuFun{
	public TextMenu menuParent;
	private LinearLayout layoutFun;

	private TextReader tr;
	public AlertDialog aDCharset;
	public CharactorSetAdapter charactorSetAdapter;
	private CheckBox commentCheck;
	private Button btnDeleteComment; //删除本页批注
	private Button btnColorPicker;


	public void setCommentCheck(CheckBox commentCheck) {
		this.commentCheck = commentCheck;
	}

	private Button btnDeleteAllComment;//删除全部批注
	
	public MenuComment(TextMenu tmObj){
		menuParent = tmObj;
		layoutFun = (LinearLayout)menuParent.getLayoutMenu().findViewById(R.id.fun_comment);
		commentCheck = (CheckBox)menuParent.getLayoutMenu().findViewById(R.id.comment_check);
		btnDeleteComment = (Button)menuParent.getLayoutMenu().findViewById(R.id.deletebtn);
		btnDeleteAllComment = (Button)menuParent.getLayoutMenu().findViewById(R.id.deleteallbtn);
		
		btnColorPicker =(Button)menuParent.getLayoutMenu().findViewById(R.id.colorpickerbtn);
		
		if(menuParent.getActParent().getIsComment()){
			commentCheck.setChecked(true);
		}else{
			commentCheck.setChecked(false);
		}
		commentCheck.setOnCheckedChangeListener(new commentCheckListener());
		btnDeleteComment.setOnClickListener(new deleteCommentListener());
		btnDeleteAllComment.setOnClickListener(new deleteAllCommentListener());
		btnColorPicker.setOnClickListener(new colorPickerListener());
		
		MenuClick menuclick = new MenuClick();
    	layoutFun.setOnClickListener(menuclick);
	}
	

	
	public boolean isShowed(){
		if(View.VISIBLE == layoutFun.getVisibility())
			return true;
		else 
			return false;
	}
	
	public boolean show(){
		if(View.VISIBLE == layoutFun.getVisibility())return false;

		layoutFun.setVisibility(View.VISIBLE);
		return true;
	}
	
	public boolean hide(){
		if(View.GONE == layoutFun.getVisibility())return false;

		layoutFun.setVisibility(View.GONE);
		if(View.GONE == layoutFun.getVisibility())return false;
		layoutFun.setVisibility(View.GONE);
		return true;
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
	
	class commentCheckListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				menuParent.getActParent().setIsComment(true);
				menuParent.getActParent().getCommentView().scrollPaint();
				
			}else{
				menuParent.getActParent().setIsComment(false);
				menuParent.getActParent().getCommentView().clearView();
				if(menuParent.getActParent().getReadMode() == 0)
					menuParent.getActParent().reParseText();
			}
			Log.i("MenuSetting", menuParent.getActParent().getIsComment()+"");
		}
		
	}
	
	class deleteCommentListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			menuParent.getActParent().getCommentView().deleteComment();

		}
		
	};
	
	class deleteAllCommentListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			menuParent.getActParent().getCommentView().deleteAllComment();
			
		}
		
	}
	
	class colorPickerListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			menuParent.getActParent().creatColorPicker();
		}
		
	}
	
	public CheckBox getCommentCheck(){
		return commentCheck;
	}
}
