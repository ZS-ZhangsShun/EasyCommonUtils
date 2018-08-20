package com.zs.easy.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.star.star_common.R;

public class ListViewAdapter extends BaseAdapter {
	private Context mContext;
	private String[] mMsg;

	public ListViewAdapter(Context pContext, String[] pMsg) {
		mContext = pContext;
		mMsg = pMsg;
	}

	@Override
	public int getCount() {
		return mMsg.length;
	}

	@Override
	public Object getItem(int position) {
		return mMsg[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = (LinearLayout) inflater.inflate(
					R.layout.dialog_listview_item, null);
		}
		TextView text = (TextView) convertView.findViewById(R.id.id_list_item);
		String _Info = (String) getItem(position);
		text.setText(_Info);
		
		if(position == getCount() - 1){
			text.setTextColor(mContext.getResources().getColor(R.color.dialog_list_cancel));
		}else {
			text.setTextColor(mContext.getResources().getColor(R.color.dialog_content_color));
		}
		
		return convertView;
	}

}
