package com.ccbooks.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ccbooks.adapter.BookInfosAdapter.ViewHolder;
import com.ccbooks.bo.BooksBo;
import com.ccbooks.listener.DelListener;
import com.ccbooks.listener.delMark;
import com.ccbooks.view.BookCatalogView;
import com.ccbooks.view.BookContentView;
import com.ccbooks.view.R;
import com.ccbooks.view.R.string;
import com.ccbooks.view.TextReader;
import com.ccbooks.vo.CatelogItem;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera.Size;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CatelogAdapter extends BaseAdapter {

	public Activity activity;
	public List<CatelogItem> items;
	private LayoutInflater inflater;

	public CatelogAdapter(Activity activity, List<CatelogItem> items) {
		super();
		this.activity = activity;
		this.items = items;
		inflater = LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		if (items != null) {
			return items.size();
		}
		return 0;
	}

	@Override
	public CatelogItem getItem(int position) {
		if (items != null) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if(items != null){
			return items.get(position).id;
		}else{
			return 0;
		}
	}

	class markViewHolder{
		ImageView ImageView01;
		TextView tvChaptersTitle;
		TextView tvChapterDate;
		ImageView  btnDelMark;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout ly = null;
		markViewHolder holder = null;
		ly = (LinearLayout) inflater.inflate(R.layout.catelogitem, null);
		if (items != null) {
			holder = new markViewHolder();
			
			holder.tvChaptersTitle = (TextView) ly
					.findViewById(R.id.tvChaptersTitle);
			//TextView tvChaptersPage = (TextView) ly
			//		.findViewById(R.id.tvChaptersPage);
			holder.tvChapterDate = (TextView) ly
					.findViewById(R.id.tvChapterDate);
			holder.btnDelMark = (ImageView) ly
						.findViewById(R.id.btnDelMark);
			if (activity instanceof TextReader){
				holder.tvChaptersTitle .setTextColor(R.drawable.dialog_list_textcolor);
				holder.tvChapterDate.setTextColor(R.drawable.dialog_list_textcolor);
			} else if (activity instanceof BookCatalogView){
				holder.tvChaptersTitle .setTextColor(Color.BLACK);
				holder.tvChapterDate.setTextColor(Color.BLACK);
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(getItem(position).date*1000);
			holder.tvChaptersTitle.setText(getItem(position).title);
			holder.btnDelMark.setOnTouchListener(new delMark(activity,this,getItem(position).bookname,getItem(position).id));
		//	tvChaptersPage.setText(String.valueOf(getItem(position).pageIndex));
			if (getItem(position).date != 0) {
				holder.tvChapterDate.setText(String.valueOf(dateString));
			}
			ly.setTag(holder);
		}
		else
		{
			 holder = (markViewHolder)ly.getTag();
		}
		return ly;
	}

}
