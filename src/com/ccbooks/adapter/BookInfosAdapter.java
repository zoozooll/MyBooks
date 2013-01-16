package com.ccbooks.adapter;

import java.util.List;

import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.FileBo;
import com.ccbooks.listener.DelListener;
import com.ccbooks.util.BookUtil;
import com.ccbooks.view.BookListView;
import com.ccbooks.view.R;
import com.ccbooks.vo.Book;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.TextSize;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookInfosAdapter extends BaseAdapter {

	public int count;
	private BookListView activity;
	public List<Book> books;
	private LayoutInflater inflater;

	public BookInfosAdapter(BookListView activity, List<Book> books) {
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity);
		this.books = books;
	}
	
	public void setBooks(List<Book> books) {
		this.books = books;
	}

	@Override
	public int getCount() {
		if (books==null){
			return 0;
		}
		return books.size();

	}

	@Override
	public Book getItem(int position) {
		return books.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (books==null){
			return 0;
		}
		return books.get(position).id;
	}

	class ViewHolder{
		ImageView ivDelIcon;
		ImageView ivBookIcon;
		TextView tvBookName;
		TextView tvCategory;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = (LinearLayout) inflater.inflate(R.layout.bookinfos,
				null);
			holder.ivDelIcon= (ImageView) convertView.findViewById(R.id.ivDelIcon);
			holder.ivBookIcon = (ImageView) convertView.findViewById(R.id.ivBookIcon);
			holder.tvBookName = (TextView) convertView.findViewById(R.id.tvBookName);
//		TextView tvAuthor = (TextView) ly.findViewById(R.id.tvAuthor);
			holder.tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		if (books != null) {
			if(books.get(position).imgpath!=null){
				Bitmap bitmap = BitmapFactory
					.decodeFile(books.get(position).imgpath);
				holder.ivBookIcon.setImageBitmap(bitmap);
			}else{
				//判断是否为TXT后缀
				if(FileBo.BOOKTYPE_TXT.equals(FileBo.getFileEx(books.get(position).bookpath))){	
					holder.ivBookIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.fileicon_txt));
				}else{
					holder.ivBookIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.def_cover_list));
				}	
			}
			DelListener delListener=new DelListener(activity,this,books.get(position));
		//	holder.ivDelIcon.setOnClickListener(delListener);
			holder.tvBookName.setText(books.get(position).bookname);
			holder.tvBookName.setEllipsize(TextUtils.TruncateAt.END);
/*			V1.0屏蔽作者//2010.12.16
			if(BooksBo.books.get(position).author !=null && !"".equals( BooksBo.books.get(position).author)){
				tvAuthor.setText(BooksBo.books.get(position).author);
			}else {
				tvAuthor.setText("作者：Unkown");
			}
			if(BooksBo.books.get(position).category !=null && !"".equals( BooksBo.books.get(position).category)){
				tvCategory.setText(BooksBo.books.get(position).category);
			}else {
				tvAuthor.setText("CCBOOKS");
			}
*/			
			if(activity.isEdit){  //设置是否显示删除图标
				holder.ivDelIcon.setVisibility(View.VISIBLE);
			}else{
				holder.ivDelIcon.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

}
