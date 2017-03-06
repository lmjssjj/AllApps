package com.lmjssjj.allapplists.adapter;

import java.util.HashMap;
import java.util.List;

import com.lmjssjj.allapplists.AppInfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AllAppsGridViewItemAdapter extends BaseAdapter {

	private List<AppInfo> mDatas;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private View.OnClickListener mIconClickListener;

	public AllAppsGridViewItemAdapter(Context context, List<AppInfo> listDatas) {

	}

	public AllAppsGridViewItemAdapter(Context context, List<AppInfo> getmDatas, OnClickListener iconClickListener) {
		this.mDatas = getmDatas;
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		this.mIconClickListener = iconClickListener;
	}

	class ItemViewHolder extends RecyclerView.ViewHolder {
		public View mContent;

		public ItemViewHolder(final View v) {
			super(v);
			mContent = v;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView icon = new TextView(mContext);
		icon.setOnClickListener(mIconClickListener);
		AppInfo info = mDatas.get(position);
		//icon.setCompoundDrawables(null, new Drainfo.iconBitmap, null, null);
		return icon;
	}

}
