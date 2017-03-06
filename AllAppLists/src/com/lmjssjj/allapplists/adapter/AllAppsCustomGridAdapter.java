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

import java.util.Set;

import com.lmjssjj.allapplists.FitGridLayoutManager;
import com.lmjssjj.allapplists.R;
import com.lmjssjj.allapplists.sortbyindex.AlphabeticalAppsList;
import com.lmjssjj.allapplists.view.LmjssjjLetterIndexer;
import com.lmjssjj.allapplists.view.SideBar;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * The grid view adapter of all the apps.
 */
public class AllAppsCustomGridAdapter extends RecyclerView.Adapter<AllAppsCustomGridAdapter.ViewHolder>
		implements SectionIndexer {

	public static final String TAG = "AppsGridAdapter";

	// A section break in the grid
	public static final int SECTION_BREAK_VIEW_TYPE = 0;
	// A normal icon
	public static final int ICON_VIEW_TYPE = 1;
	// A prediction icon
	public static final int PREDICTION_ICON_VIEW_TYPE = 2;
	// The message shown when there are no filtered results
	public static final int EMPTY_SEARCH_VIEW_TYPE = 3;
	// A divider that separates the apps list and the search market button
	public static final int SEARCH_MARKET_DIVIDER_VIEW_TYPE = 4;
	// The message to continue to a market search when there are no filtered
	// results
	public static final int SEARCH_MARKET_VIEW_TYPE = 5;

	/**
	 * ViewHolder for each icon.
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public View mContent;
		public TextView mTv;
		public ImageView mIv;
		public RecyclerView mRcv;
		public ViewHolder(View v) {
			super(v);
			mContent = v;
			mTv = (TextView) v.findViewById(R.id.tv_index);
			mIv = (ImageView) v.findViewById(R.id.iv_index);
			mRcv = (RecyclerView) v.findViewById(R.id.apps_index_view);
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
	private LmjssjjLetterIndexer mIndexer;

	public AllAppsCustomGridAdapter(Context context, AlphabeticalAppsList apps, View.OnTouchListener touchListener,
			View.OnClickListener iconClickListener, View.OnLongClickListener iconLongClickListener) {
		mContext = context;
		mApps = apps;

		mLayoutInflater = LayoutInflater.from(context);
		mTouchListener = touchListener;
		mIconClickListener = iconClickListener;
		// mIconLongClickListener = iconLongClickListener;

	}

	public void setOnLongClickListener(View.OnLongClickListener iconLongClickListener) {
		mIconLongClickListener = iconLongClickListener;
		notifyDataSetChanged();
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
	public void setLetter(LmjssjjLetterIndexer sideBar) {
		mIndexer = sideBar;
		initBar();
	}

	public void notifySideBarChanges(){
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
		if (mIndexer != null && keySet != null && keySet.size() > 0) {
			
			String[] indexs = new String[keySet.size()];
			int index = 0;
			for (String string : keySet) {
				indexs[index++] = string;
				Log.v("lmjssjj_adapter", "initBar:" + string);
			}
			mIndexer.setConstChar(indexs,0,0);
		}
	}

	public void notifySideBarDataChange() {
		initBar();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = mLayoutInflater.inflate(R.layout.custom_all_apps_item, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		
		if (mApps.getData1().get(position).getIndex().equals("**")) {
			holder.mIv.setVisibility(View.VISIBLE);
			holder.mTv.setVisibility(View.INVISIBLE);
		} else {
			holder.mIv.setVisibility(View.INVISIBLE);
			holder.mTv.setVisibility(View.VISIBLE);
		}
		holder.mTv.setText(mApps.getData1().get(position).getIndex());
		if (mApps.getData1().get(position).getmDatas() != null
				&& mApps.getData1().get(position).getmDatas().size() > 0) {
			AllAppsCustomItemAdapter imageAdapter = new AllAppsCustomItemAdapter(mContext,
					mApps.getData1().get(position).getmDatas(), mIconClickListener, mIconLongClickListener);
			holder.mRcv.setLayoutManager(new FitGridLayoutManager(mContext, 4));
			holder.mRcv.setAdapter(imageAdapter);
			holder.mRcv.setVisibility(View.VISIBLE);
		} else {
			holder.mRcv.setVisibility(View.GONE);
		}

	}

	@Override
	public int getItemCount() {
		
		return mApps.getData1().size();
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
}
