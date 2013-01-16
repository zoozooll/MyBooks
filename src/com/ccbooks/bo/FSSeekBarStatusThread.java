package com.ccbooks.bo;



import com.ccbooks.view.TextReader;
import com.ccbooks.web.SingleThreadDown;


import android.os.Message;



public class FSSeekBarStatusThread implements Runnable {

	public TextReader bcv;

	
	
	public FSSeekBarStatusThread(TextReader bcv) {
		super();
		this.bcv = bcv;
	}

	@Override
	public void run() {
	
		while(!SingleThreadDown.downfinish){
			
			if(this.bcv.menuMain.menuTurntoFun.seeBar.isEnabled()){
				String msg = "false";				 			//读取服务端发来的消息
    			//获得EditText对象
	    		Message msg1 = new Message();
	    		msg1.obj = msg;
	    		this.bcv.seekBarHandle.sendMessage(msg1);
    	
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		if(!this.bcv.menuMain.menuTurntoFun.seeBar.isEnabled()){
			String msg = "true";				 			//读取服务端发来的消息
			//获得EditText对象
			Message msg1 = new Message();
			msg1.obj = msg;
			this.bcv.seekBarHandle.sendMessage(msg1);
			
		}
		
		
		
	}
	

}


