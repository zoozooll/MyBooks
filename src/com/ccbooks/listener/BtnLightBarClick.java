package com.ccbooks.listener;



import com.ccbooks.view.BookContentView;



import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class BtnLightBarClick implements OnClickListener{

	
	private BookContentView bookContentView;
	
	

	public BtnLightBarClick(BookContentView bookContentView) {
		super();
		this.bookContentView = bookContentView;
	}



	// 返回
	

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}


		//屏幕亮度控制拖动条；
	//	private OnSeekBarChangeListener btnLightBarClick = new SeekBar.OnSeekBarChangeListener(){


	
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			
			}
			
		
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
				float a = (float)progress/(float)255;
				if(a<=0.2f){
					a = 0.2f;
				}
				System.out.println("1212"+a);
				brightnessMax(a);
				
			}
			
	
		
		private void brightnessMax(float ness) {    
		     WindowManager.LayoutParams lp = bookContentView.getWindow().getAttributes();    
		     lp.screenBrightness = ness;    
		     bookContentView.getWindow().setAttributes(lp);   
		     
		}   
	
}
