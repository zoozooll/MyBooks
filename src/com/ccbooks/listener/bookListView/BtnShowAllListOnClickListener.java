package com.ccbooks.listener.bookListView;

import java.util.List;
import java.util.zip.Inflater;

import com.ccbooks.bo.BooksBo;
import com.ccbooks.view.BookListView;
import com.ccbooks.view.R;
import com.ccbooks.vo.Book;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class BtnShowAllListOnClickListener implements OnClickListener {
	
	private BookListView blv; 
	
	
	public BtnShowAllListOnClickListener(BookListView blv) {
		super();
		this.blv = blv;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnShowAllList:
			if(blv.showType!=BookListView.SHOW_TYPE_ALL){
				blv.isEdit= false;
				blv.btnEdit.setText(R.string.Edit);
				blv.btnShowHistory.setBackgroundResource(R.drawable.outclickbutton_right);
				v.setBackgroundResource(R.drawable.onclickbutton_left);
				blv.btnShowSearch.setBackgroundResource(R.drawable.outclickbutton_middle);
				blv.showType = BookListView.SHOW_TYPE_ALL;
				blv.bia.setBooks(BooksBo.books);
				blv.bia.notifyDataSetChanged();
			}
			break;
		case R.id.btnShowHistory:
			if(blv.showType!=BookListView.SHOW_TYPE_HISTORY){
				blv.isEdit= false;
				blv.btnEdit.setText(R.string.Edit);
				blv.btnShowAllList.setBackgroundResource(R.drawable.outclickbutton_left);
				v.setBackgroundResource(R.drawable.onclickbutton_right);
				blv.btnShowSearch.setBackgroundResource(R.drawable.outclickbutton_middle);
				blv.showType = BookListView.SHOW_TYPE_HISTORY;
				BooksBo bo = new BooksBo(blv, false);
				List<Book> books = bo.getHistoryBookList();
				blv.bia.setBooks(books);
				blv.bia.notifyDataSetChanged();
			}
			break;
		case R.id.btnShowSearch:
			blv.searchBooksDialog();
			break;
		default:
			break;
		}
		
	}

}
