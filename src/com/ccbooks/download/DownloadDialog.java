package com.ccbooks.download;


import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.R;

import android.R.style;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadDialog extends AlertDialog{
	
	 public ProgressBar mProgress;
	 private TextView mProgressNumber;
	 private String mProgressNumberFormat;
	 private TextView mProgressPercent;
	 public Handler handler;
	 private int fileSize;
	 private int downloadFileSize;
	 private Button btnCancel;
	 public Context context;

	 
	public DownloadDialog(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

    public DownloadDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("下载");
		setContentView(R.layout.alert_dialog_progress);
		mProgress = (ProgressBar)findViewById(R.id.progress);
        mProgressNumber = (TextView) findViewById(R.id.progress_number);
        mProgressPercent = (TextView) findViewById(R.id.progress_percent);
        btnCancel = (Button)findViewById(R.id.cancel);
        
        btnCancel.setText(android.R.string.cancel);
        btnCancel.setOnClickListener(new cancelListener());
        
        handler = new DownloadHandler();
        
        
		
		
	}
	
	class cancelListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FileUtils.canceldown = true;
			Message msg = new Message();
			msg.what = 2;
			((BookShelfView)context).mHandler.sendMessage(msg);
			

		}
		
	}
	
	public int getDownloadFileSize() {
		return downloadFileSize;
	}

	public void setDownloadFileSize(int downloadFileSize) {
		this.downloadFileSize = downloadFileSize;
	}

	public void setMax(int max){
		fileSize = max;
		mProgress.setMax(max);
	}
	
	class DownloadHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			mProgress.setProgress(downloadFileSize);
			mProgressNumber.setText(downloadFileSize+"/"+fileSize+" bytes");
			mProgressPercent .setText((int)(downloadFileSize*100/fileSize) +"%");
		}
		
	}
	
}


