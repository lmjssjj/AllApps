/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lmjssjj.allapplists.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.lmjssjj.allapplists.AppListGridView;
import com.lmjssjj.allapplists.R;
import com.lmjssjj.allapplists.sortbyindex.AlphabeticalAppsList;
import com.lmjssjj.allapplists.view.SideBar;

/**
 * The grid view adapter of all the apps.
 */
public class AllAppsListViewAdapter extends BaseAdapter implements SectionIndexer {

	/**
	 * ViewHolder for each icon.
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public View mContent;
		public TextView mTv;
		public AppListGridView mRcv;

		public ViewHolder(View v) {
			super(v);
			mContent = v;
			mTv = (TextView) v.findViewById(R.id.tv_index);
			mRcv = (AppListGridView) v.findViewById(R.id.apps_index_view);
		}
	}

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	AlphabeticalAppsList mApps;
	private View.OnTouchListener mTouchListener;
	private View.OnClickListener mIconClickListener;
	private View.OnLongClickListener mIconLongClickListener;
	
	final Rect mBackgroundPadding = new Rect();
	
	int mPredictionBarDividerOffset;
	
	int mAppsPerRow;
	
	boolean mIsRtl;
	private SideBar mSideBar;

	public AllAppsListViewAdapter(Context context, AlphabeticalAppsList apps,
			View.OnTouchListener touchListener, View.OnClickListener iconClickListener,
			View.OnLongClickListener iconLongClickListener) {
		mContext = context;
		mApps = apps;

		mLayoutInflater = LayoutInflater.from(mContext);
		mTouchListener = touchListener;
		mIconClickListener = iconClickListener;
		mIconLongClickListener = iconLongClickListener;

	}

	/**
	 * Sets whether we are in RTL mode.
	 */
	public void setRtl(boolean rtl) {
		mIsRtl = rtl;
	}

	public void setSideBar(SideBar sideBar) {
		mSideBar = sideBar;
		initBar();
	}

	public void initBar() {
		Set<String> keySet = mApps.getData().keySet();
		if (mSideBar != null && keySet != null && keySet.size() > 0) {

			String[] indexs = new String[keySet.size()];
			int index = 0;
			for (String string : keySet) {
				indexs[index++] = string;
				Log.v("lmjssjj_adapter", "initBar:" + string);
			}
			mSideBar.setIndexs(indexs);
		}
	}

	public void notifySideBarDataChange() {
		initBar();
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		return sectionIndex;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mApps.getData1().size();
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

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.all_apps_item_gridview, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mTv.setText(mApps.getData1().get(position).getIndex());

		if (mApps.getData1().get(position).getmDatas() != null
				&& mApps.getData1().get(position).getmDatas().size() > 0) {
			AllAppsGridViewItemAdapter imageAdapter = new AllAppsGridViewItemAdapter(mContext,
					mApps.getData1().get(position).getmDatas(), mIconClickListener);
			
			holder.mRcv.setAdapter(imageAdapter);
			holder.mRcv.setVisibility(View.VISIBLE);
		} else {
			holder.mRcv.setVisibility(View.GONE);
		}

		return convertView;
	}
}
