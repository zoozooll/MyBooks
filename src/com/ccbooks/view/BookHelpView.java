package com.ccbooks.view;

import com.ccbooks.download.CheckUpdate;
import com.ccbooks.view.R.drawable;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BookHelpView extends Activity{
	private Button helpNext;
	private CheckBox doNotTipsCheck;
	private ImageView helpImage;
	private Button helpCancel;
	private TextView tvPage;
	private boolean doNotTips = true;
	private boolean showTipsCheckBox;
	
	int[] imageResoure = {R.drawable.help_picture1,
						  R.drawable.help_picture2,
						  R.drawable.help_picture3,
						  R.drawable.help_picture4,
						  R.drawable.help_picture5,
						  R.drawable.help_picture6,
						  R.drawable.help_picture7};
	private int imageNum = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
		
		Intent intent = getIntent();
		showTipsCheckBox = intent.getBooleanExtra(BookShelfView.SHOWTIPSCHECKBOX, false);
		
		helpNext = (Button)findViewById(R.id.help_nextpage);
		helpImage = (ImageView)findViewById(R.id.helpimage);
		helpCancel = (Button)findViewById(R.id.help_cancel);
		doNotTipsCheck = (CheckBox)findViewById(R.id.help_checkbox);
		tvPage = (TextView)findViewById(R.id.help_page);
		if(showTipsCheckBox){
			doNotTipsCheck.setVisibility(View.VISIBLE);
		}else{
			doNotTipsCheck.setVisibility(View.GONE);
		}
		
		helpImage.setImageResource(imageResoure[imageNum]);
		helpNext.setOnClickListener(new helpNextListener());
		helpCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		doNotTipsCheck.setOnCheckedChangeListener(new checkTipsListener());
		setNumText();
	}




	public void setNumText() {
		int totalNum = imageResoure.length;
		int nowNum = imageNum+1;
		tvPage.setText(nowNum+"/"+totalNum);
	}
	
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(showTipsCheckBox){
			if(doNotTips){
				SharedPreferences sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putInt("saveVersion", CheckUpdate.getCurrentVersion(this));
				editor.commit();
			}
		}	
		super.onDestroy();
	}




	class helpNextListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(imageNum == (imageResoure.length-1)){
				imageNum = 0;
			}else{
				imageNum++;
			}
			helpImage.setImageResource(imageResoure[imageNum]);
			setNumText();
			Log.i("garmen", "图片实际大小为:"+helpImage.getWidth()+"  "+helpImage.getHeight());
		}
	}
	
	class checkTipsListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				doNotTips = true;
			}else{
				doNotTips = false;
			}
		}
		
	}
	
}
