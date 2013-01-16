package com.ccbooks.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccbooks.view.R;

public class CharactorSetAdapter extends BaseAdapter {
	
	public int count;
	public int selected;
	private Activity activity;
	private String[] list;
	//public List<Book> books;
	private LayoutInflater inflater;
	private int mode;
	
	public CharactorSetAdapter(Activity activity,String[] list,int selected){
		this.activity = activity;
		//this.books = books;
		this.list = list;
		this.inflater = LayoutInflater.from(activity);
		this.selected = selected;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		if(list != null){
			return position;
		}else{
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = (FrameLayout) inflater.inflate(R.layout.charactor_layout,
				null);
			holder.tvCharactorSelecte = (TextView) convertView.findViewById(R.id.tvCharactorSelecte);
			holder.ivCharactorSelected = (ImageView) convertView.findViewById(R.id.ivCharactorSelected);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.tvCharactorSelecte.setText(list[position]);
		if (mode == 1) {
			holder.tvCharactorSelecte.setTextColor(R.drawable.dialog_list_textcolor);
			holder.tvCharactorSelecte.setTextSize(22);
		} else if (mode == 2) {
			holder.tvCharactorSelecte.setTextColor(R.drawable.dialog_list_textcolor);
			holder.tvCharactorSelecte.setTextSize(22);
		}
		
		if(selected == position){
			holder.ivCharactorSelected.setImageDrawable(activity.getResources().getDrawable(R.drawable.content_btn_font_cuntermark));
		}else{
			holder.ivCharactorSelected.setImageDrawable(activity.getResources().getDrawable(R.drawable.content_btn_font_transparent));
		}
		return convertView;
	}


	public void setSelected(int selected) {
		this.selected = selected;
	}
	class ViewHolder{
		TextView tvCharactorSelecte;
		ImageView ivCharactorSelected;
	}
}
