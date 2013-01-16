package com.ccbooks.listener;

import java.io.IOException;

import com.ccbooks.bo.FileBo;
import com.chinachip.ccbooks.core.BookCore;
import com.chinachip.ccbooks.core.TextSizeUtil;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BtnFontEnterButtonAClick implements OnClickListener {

	private BookContentView bcv;
	private Button btnFontEnterButtona1;
	private Button btnFontEnterButtonA2; // 字体变大按钮；
	private Button btnFontEnterColorBlack; // 白背景按钮；
	private Button btnFontEnterColorBrown; // 棕色背景按钮；

	// 点击字体变小按钮；
	@Override
	public void onClick(View v) {
		//bcv.restartSet();
		bcv.showDialog();
		
		int textSize =  bcv.bc.getFontSize()+ 2;
		if (textSize >= 38) {
			textSize = 38;
		}
		bcv.bc.setFontSize(textSize);
		TextSizeUtil.fontSize = (int) textSize;
//		if(FileBo.BOOKTYPE_TXT.equals(bcv.book.booktype)){
			//bcv.bc.reset();
			bcv.bc.setPortraint(bcv.portraint);
				
			if (bcv.portraint==1){
				
				bcv.bct.skipNumber = bcv.markPoint;
				bcv.bct.what = 7;
				
			}else if (bcv.portraint==2) {

				bcv.bct.skipNumber =bcv.markPoint;
				bcv.bct.what = 8;
			}
			new Thread(bcv.bct).start();
		/*}else if(FileBo.BOOKTYPE_PDB.equals(bcv.book.booktype)){
			
		}else if(FileBo.BOOKTYPE_CHM.equals(bcv.book.booktype)){
			
		}else if(FileBo.BOOKTYPE_HTML.equals(bcv.book.booktype)){
			
		}else if(FileBo.BOOKTYPE_PDF.equals(bcv.book.booktype)){
			
		}*/
		
		if(textSize >= 38){
			btnFontEnterButtonA2.setEnabled(false);
		}else{
			btnFontEnterButtonA2.setEnabled(true);
			btnFontEnterButtona1.setEnabled(true);
		}
	}

	public BtnFontEnterButtonAClick(BookContentView bcv,TextView tvMainContent,
			TextView tvMainContentLeft, TextView tvMainContentRight,
			Button btnFontEnterButtona1, Button btnFontEnterButtonA2,
			Button btnFontEnterColorBlack, Button btnFontEnterColorBrown
			) {
		super();
		this.bcv = bcv;
		this.btnFontEnterButtona1 = btnFontEnterButtona1;
		this.btnFontEnterButtonA2 = btnFontEnterButtonA2;
		this.btnFontEnterColorBlack = btnFontEnterColorBlack;
		this.btnFontEnterColorBrown = btnFontEnterColorBrown;
	}


}
