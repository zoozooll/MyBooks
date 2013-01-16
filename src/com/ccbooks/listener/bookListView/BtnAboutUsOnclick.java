/**
 * 
 */
package com.ccbooks.listener.bookListView;

import com.ccbooks.dialog.AboutDialog;
import com.ccbooks.view.BookShelfView;
import com.ccbooks.view.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 相应about us按钮事件
 * @author Administrator
 * 
 * 2011-3-28 下午04:12:36
 */
public class BtnAboutUsOnclick implements OnClickListener {

/*	private String content = "<h1>CCBook</h1>"
		+"版本：V1.30<br/><br/>"
		+"深圳市华芯飞科技有限公司<br/><br/>"
		+"网址：http://www.chinachip.cn<br/><br/><br/>"
		+"Copyright(C) 2009-2011 Chinachip.All Right Reserved.";*/
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		AboutDialog aboutUs= new AboutDialog(v.getContext(),R.style.dialog);
//		aboutUs.setTitle("关于CCBook");
//		aboutUs.setIcon(R.drawable.smallicon);
//		aboutUs.setMessage(Html.fromHtml(content));
/*			aboutUs.setPositiveButton("帮助", new  DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			});*/
			//.create();
		aboutUs.setCanceledOnTouchOutside(true);
		aboutUs.show();
	}

}
