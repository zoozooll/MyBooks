package com.ccbooks.listener;

import com.ccbooks.bo.BookMarkBo;
import com.ccbooks.bo.FileBo;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.chinachip.books.plugin.Plugin;

import android.R.bool;
import android.view.View;
import android.view.View.OnClickListener;

public class BtnBookMarkOnClickListener implements OnClickListener {

	private BookContentView bcv;
	private BookMarkBo bmo;
	
	public BtnBookMarkOnClickListener(BookContentView bcv) {
		super();
		this.bcv = bcv;
	}

	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(-1 == bcv.judgeFocus(bcv.BCVFOCUS,1)){
			return;
		}
		if(bcv.markPoint == getFileLength())
		{
			return;
		}
		if(bmo == null)	{
			bmo = new BookMarkBo(bcv);
		}
		//bcv.restartSet();
		if (bcv.mark == null) {
			//isMark = b;
			bcv.btnBookMark.setBackgroundDrawable(bcv
					.getResources().getDrawable(
							R.drawable.content_btn_onmark));

//				if(FileBo.BOOKTYPE_TXT.equals(bcv.book.booktype)){
					try {
						String title = null;
						for (int i = 0;i<bcv.content.size();i++){
							title = bcv.content.get(i).replace(" ", "");
							if (title != null) {
								if (!title.equals("")) {
									if (title == null) {
										break;
									} else {
										if (title.indexOf("\r\n") >= 0) {
											break;
										}
										if (title.length() > 1) {
											break;
										}
									}
										
								}
							}
						}
						bmo.addMarksByDb(bcv.book.bookname, (int)bcv.markPoint,
								title,0, 0, 0, 0,bcv.file);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			/*	}else if(FileBo.BOOKTYPE_PDB.equals(bcv.book.booktype)){	
					
				}else if(FileBo.BOOKTYPE_CHM.equals(bcv.book.booktype)){
					
				}else if(FileBo.BOOKTYPE_HTML.equals(bcv.book.booktype)){
					
				}else if(FileBo.BOOKTYPE_PDF.equals(bcv.book.booktype)){
				
				}
			*/
			BookMarkBo.isMark(bcv);

		}else {
			bcv.btnBookMark.setBackgroundDrawable(bcv
					.getResources().getDrawable(
							R.drawable.content_btn_outmark));
			bmo.delMarkByDb(bcv.book.bookname, bcv.mark.id);
			bcv.mark = null;
			BookMarkBo.isMark(bcv);
		}
		
	}
	public long getFileLength(){
		long fileLength = 0;
		Plugin plugin = bcv.bc.pm.getDefaultPlugin();
		fileLength = bcv.bc.pm.length(plugin, bcv.bc.pm.filehandle);
		
		if(fileLength > 0) return fileLength;
		else return 0;
	}
}
