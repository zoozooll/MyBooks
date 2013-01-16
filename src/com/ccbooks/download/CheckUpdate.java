package com.ccbooks.download;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.ccbooks.util.XmlReader;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CheckUpdate {
	
	public static final int VERSIONCODE = 0;
	public static final int DATE = 1;
	public static final int URI = 3;
	public static final int VERSIONNAME = 4;
	public static final int CONTENT = 5;
	
	Context context;
	String url;
	String fileName;
	CheckBox mCheckBox;
	
	public CheckUpdate(Context context){
		this.context = context;
		mCheckBox = new CheckBox(context);
		mCheckBox.setText(R.string.doNotTips);
		mCheckBox.setOnCheckedChangeListener(new doNotTipsListener());
	}
	
	class doNotTipsListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				((BookShelfView)context).hadCheckUpdate = true;
			}	
		}
		
	}

	/**
	 * 检测更新
	 */
	public void start(){

			if(isNetworkAvailable(context) == true){
				int curVersion = getCurrentVersion(context);
				int newVersion =0;
				String softSize;
				List<String> val;
				XmlReader xmlReader = new XmlReader("http://www.ccdang.com/uploads/110215/update.xml");
				try {
					val=xmlReader.getAttribute("update");
					for(int i=0;i<val.size();i++)
						Log.i("garmen", val.get(i));
					if(val.isEmpty())
						return;
					else if(val.get(VERSIONCODE).equals("")){
						return;
					}else
						newVersion=Integer.parseInt(val.get(VERSIONCODE));
					url = val.get(URI);
					fileName = url.substring(url.lastIndexOf("/")+1, url.length());
					softSize = b2kb(val.get(DATE));
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				
				if(newVersion>curVersion){
					
			      	Dialog updateDialog = new AlertDialog.Builder(BookShelfView.activity).setTitle(R.string.findupdate)
			      	.setIcon(R.drawable.smallicon)
	        		.setMessage("版本:"+val.get(VERSIONNAME)+",大小:"+softSize+"\n\n" 
	        				+ "升级版本将下载至目录:内部存储/apk/CCBook.apk,若安装失败，请先卸载再至该目录手动安装升级。" + "\n" 
	        				+"更新内容:"+"\n"+val.get(CONTENT))
	        		.setView(mCheckBox)
	        		.setPositiveButton(R.string.updatenow, new DialogInterface.OnClickListener() {
	        			
						@Override
						public void onClick(DialogInterface dialog, int which) {

							Runnable r = new Runnable() {
								@Override
								public void run() {
									downFile(url,fileName);
								}
							};
							new Thread(r).start();

							((BookShelfView)context).showWaitDialog();
				        	
						}
					}).setNegativeButton(R.string.donotupdate, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}).create();
			      	try{
			      		updateDialog.show();
			      		
			      	}catch(Exception e){
			      		e.printStackTrace();
			      	}
				}
				
			}
		}
	
    /**
     * 检测网络是否连接
     * @param context
     * @return true false
     */
    public static boolean isNetworkAvailable(Context context){
    	try{
    		ConnectivityManager cm = (ConnectivityManager) context
    			.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo info = cm.getActiveNetworkInfo();
    		return (info !=null&& info.isConnected());
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    }
    
	/**
	 * 获得当前版本号
	 * @return
	 */
	public static int getCurrentVersion(Context context){
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return info.versionCode;
	}
	
	private String b2kb(String input){
		String result;
		if(input.equals(""))
			return null;
		int size=Integer.parseInt(input);
		if(size>1000){
			size = size/1000;
			result = size+"KB";
		}else{
			result = size+"B";
		}
		return result;
	}
	
	private void downFile(String url,String filename){
		String path="apk/";
		HttpDownloader httpDownloader = new HttpDownloader();
		System.out.println("url:"+url);
		System.out.println("filename:"+filename);
		int Result = httpDownloader.downFile(url, path, filename,context);
		if(Result == 1){
			FileUtils fileUtils = new FileUtils("/local");
			fileUtils.delectFile(filename, path);
			httpDownloader.downFile(url, path, filename,context);
		}
		if(((BookShelfView)context).dialog.isShowing()){
			((BookShelfView)context).dialog.cancel();
			((BookShelfView)context).dialog.dismiss();
		}	
	}
	
}
