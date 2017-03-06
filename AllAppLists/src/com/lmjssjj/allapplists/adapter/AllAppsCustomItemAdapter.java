package com.lmjssjj.allapplists.adapter;

import java.util.HashMap;
import java.util.List;

import com.lmjssjj.allapplists.AppInfo;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AllAppsCustomItemAdapter extends RecyclerView.Adapter<AllAppsCustomItemAdapter.ItemViewHolder> {

	private List<AppInfo> mDatas;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private View.OnClickListener mIconClickListener;
	private View.OnLongClickListener mIconLongClickListener;

	public AllAppsCustomItemAdapter(Context context, List<AppInfo> listDatas) {
		
	}

	public AllAppsCustomItemAdapter(Context context, List<AppInfo> getmDatas, OnClickListener iconClickListener, OnLongClickListener iconLongClickListener) {
		this.mDatas = getmDatas;
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		this.mIconClickListener = iconClickListener;
		this.mIconLongClickListener = iconLongClickListener;
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		
		AppInfo info = mDatas.get(position);
		TextView view = (TextView) holder.mContent;
		view.setText(info.title);
		Drawable drawable = info.iconBitmap;
		drawable.setBounds(0, 0, 102, 102);
		view.setCompoundDrawables(null, drawable, null, null);
		
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		
		TextView view = new TextView(mContext);
		view.setGravity(Gravity.CENTER);
		view.setBackgroundResource(android.R.color.darker_gray);
		return new ItemViewHolder(view);

	}

	class ItemViewHolder extends RecyclerView.ViewHolder {
		public View mContent;

		public ItemViewHolder(final View v) {
			super(v);
			mContent = v;
		}
	}

}
