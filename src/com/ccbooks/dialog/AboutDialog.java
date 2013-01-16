package com.ccbooks.dialog;


import com.ccbooks.view.BookHelpView;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 关于界面的显示dialog,布局为about_dialog
 * btnhelp是帮助按钮
 * @author lamkaman
 *
 */
public class AboutDialog extends AlertDialog{
	Context context;
	Button btnhelp;
	
	public AboutDialog(Context context){
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public AboutDialog(Context context,int theme){
		super(context,theme);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_dialog);
		btnhelp = (Button)findViewById(R.id.btnhelp);
		btnhelp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, BookHelpView.class);
				context.startActivity(intent);
				cancel();
			}
		});
	}
	

	
}
