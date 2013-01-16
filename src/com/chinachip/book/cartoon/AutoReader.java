package com.chinachip.book.cartoon;

import android.os.Message;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.ccbooks.view.CartoonReader;
import com.ccbooks.view.R;

public class AutoReader implements Runnable {

	private CartoonReader cr;
	
	private Object o;

	public int flag = 0;// 1 继续运行,2 暂停 ,3 停止


	
	public AutoReader(CartoonReader cr){
		this.cr = cr;
		o=new Object();
	}
	@Override
	public void run() {
		flag = 1;
		while (flag == 1) {
			 try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
//			if(cr.isMoveing == true){
//				break;
//			}
			
			while (flag == 2) {
				synchronized (o) {
					try {
						o.wait();
					} catch (Exception e) {
					}

				}
			}
			
		
			
//			if(cr.isMoveing == true){
//				break;
//			}
			cr.mHandler.sendEmptyMessage(1);
			
			if(cr.file == cr.endFile&& flag == 1){
				cr.isAutoRunning = false;
				
				Message msg1 = new Message();
				
				cr.menuHandler.sendMessage(msg1);
				break;
			}
			if(cr.isAutoRunning == false){
				break;
			}
			
		}
	}
	public void stop_t() { // 停止
		flag = 3;
	}

	public void pause() {
		Log.i("lishq", "----------暂停----------");
		flag = 2;
	}

	public void jixu() {
		flag = 1;
		Log.i("lishq", "----------继续----------");
		synchronized (o) {
			try {
				o.notify();
			} catch (Exception e) {
			}
		}
	}
}
