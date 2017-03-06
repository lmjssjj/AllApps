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
package com.lmjssjj.allapplists.sortbyindex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.lmjssjj.allapplists.AppInfo;
import com.lmjssjj.allapplists.adapter.AllAppsCustomGridAdapter;
import com.lmjssjj.allapplists.compat.AlphabeticIndexCompat;
import com.lmjssjj.allapplists.domain.AppListBean;
import com.lmjssjj.allapplists.utils.ComponentKey;
import com.lmjssjj.allapplists.utils.ProviderConfig;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

/**
 * The alphabetically sorted list of applications.
 */
public class AlphabeticalAppsList {

	public static final String TAG = "AlphabeticalAppsList";

	private static final int FAST_SCROLL_FRACTION_DISTRIBUTE_BY_NUM_SECTIONS = 1;

	private Context mContext;

	// The set of apps from the system not including predictions
	private final List<AppInfo> mApps = new ArrayList<>();
	private final HashMap<ComponentKey, AppInfo> mComponentToAppMap = new HashMap<>();

	// The set of filtered apps with the current filter
	private List<AppInfo> mFilteredApps = new ArrayList<>();

	// The set of predicted app component names
	private List<ComponentKey> mPredictedAppComponents = new ArrayList<>();
	// The set of predicted apps resolved from the component names and the
	// current set of apps
	private List<AppInfo> mPredictedApps = new ArrayList<>();
	// The of ordered component names as a result of a search query
	private ArrayList<ComponentKey> mSearchResults;
	private HashMap<CharSequence, String> mCachedSectionNames = new HashMap<>();
	private AllAppsCustomGridAdapter mAdapter;
	private AlphabeticIndexCompat mIndexer;
	private AppNameComparator mAppNameComparator;
	private int mNumPredictedAppsPerRow;
	private int mNumAppRowsInAdapter;

	public AlphabeticalAppsList(Context context) {
		mContext = context;
		mIndexer = new AlphabeticIndexCompat(context);
		mAppNameComparator = new AppNameComparator(context);
	}

	/**
	 * Sets the adapter to notify when this dataset changes.
	 */
	public void setAdapter(AllAppsCustomGridAdapter adapter) {
		mAdapter = adapter;
	}

	/**
	 * Returns all the apps.
	 */
	public List<AppInfo> getApps() {
		return mApps;
	}

	/**
	 * Returns the number of rows of applications (not including predictions)
	 */
	public int getNumAppRows() {
		return mNumAppRowsInAdapter;
	}

	/**
	 * Returns the number of applications in this list.
	 */
	public int getNumFilteredApps() {
		return mFilteredApps.size();
	}

	/**
	 * Returns whether there are is a filter set.
	 */
	public boolean hasFilter() {
		return (mSearchResults != null);
	}

	/**
	 * Returns whether there are no filtered results.
	 */
	public boolean hasNoFilteredResults() {
		return (mSearchResults != null) && mFilteredApps.isEmpty();
	}

	/**
	 * Sets the sorted list of filtered components.
	 */
	public boolean setOrderedFilter(ArrayList<ComponentKey> f) {
		if (mSearchResults != f) {
			boolean same = mSearchResults != null && mSearchResults.equals(f);
			mSearchResults = f;
			updateAdapterItems();
			return !same;
		}
		return false;
	}

	/**
	 * Sets the current set of predicted apps. Since this can be called before
	 * we get the full set of applications, we should merge the results only in
	 * onAppsUpdated() which is idempotent.
	 */
	public void setPredictedApps(List<ComponentKey> apps) {
		mPredictedAppComponents.clear();
		mPredictedAppComponents.addAll(apps);
		onAppsUpdated();
	}

	/**
	 * Sets the current set of apps.
	 */
	public void setApps(List<AppInfo> apps) {
		mComponentToAppMap.clear();
		addApps(apps);
	}

	/**
	 * Adds new apps to the list.
	 */
	public void addApps(List<AppInfo> apps) {
		updateApps(apps);
	}

	/**
	 * Updates existing apps in the list
	 */
	public void updateApps(List<AppInfo> apps) {
		for (AppInfo app : apps) {
			mComponentToAppMap.put(app.toComponentKey(), app);
		}
		onAppsUpdated();
	}

	/**
	 * Removes some apps from the list.
	 */
	public void removeApps(List<AppInfo> apps) {
		for (AppInfo app : apps) {
			mComponentToAppMap.remove(app.toComponentKey());
		}
		onAppsUpdated();
	}

	/**
	 * Updates internals when the set of apps are updated.
	 */
	private void onAppsUpdated() {
		// Sort the list of apps
		mApps.clear();
		mApps.addAll(mComponentToAppMap.values());
		Collections.sort(mApps, mAppNameComparator.getAppInfoComparator());

		// As a special case for some languages (currently only Simplified
		// Chinese), we may need to
		// coalesce sections
		Locale curLocale = mContext.getResources().getConfiguration().locale;
		TreeMap<String, ArrayList<AppInfo>> sectionMap = null;
		boolean localeRequiresSectionSorting = curLocale.equals(Locale.SIMPLIFIED_CHINESE);
		if (localeRequiresSectionSorting) {
			// Compute the section headers. We use a TreeMap with the section
			// name comparator to
			// ensure that the sections are ordered when we iterate over it
			// later
			sectionMap = new TreeMap<>(mAppNameComparator.getSectionNameComparator());
			for (AppInfo info : mApps) {
				// Add the section to the cache
				String sectionName = getAndUpdateCachedSectionName(info.title);

				// Add it to the mapping
				ArrayList<AppInfo> sectionApps = sectionMap.get(sectionName);
				if (sectionApps == null) {
					sectionApps = new ArrayList<>();
					sectionMap.put(sectionName, sectionApps);
				}
				sectionApps.add(info);
			}

			// Add each of the section apps to the list in order
			List<AppInfo> allApps = new ArrayList<>(mApps.size());
			for (Map.Entry<String, ArrayList<AppInfo>> entry : sectionMap.entrySet()) {
				allApps.addAll(entry.getValue());
			}

			mApps.clear();
			mApps.addAll(allApps);
		} else {
			// Just compute the section headers for use below
			for (AppInfo info : mApps) {
				// Add the section to the cache
				getAndUpdateCachedSectionName(info.title);
			}
		}

		// Recompose the set of adapter items from the current set of apps
		updateAdapterItems();
	}

	/**
	 * Updates the set of filtered apps with the current filter. At this point,
	 * we expect mCachedSectionNames to have been calculated for the set of all
	 * apps in mApps.
	 */
	private void updateAdapterItems() {

		// Prepare to update the list of sections, filtered apps, etc.
		mFilteredApps.clear();

		// Process the predicted app components
		mPredictedApps.clear();
		if (mPredictedAppComponents != null && !mPredictedAppComponents.isEmpty() && !hasFilter()) {
			for (ComponentKey ck : mPredictedAppComponents) {
				AppInfo info = mComponentToAppMap.get(ck);
				if (info != null) {
					mPredictedApps.add(info);
				} else {
					if (ProviderConfig.IS_DOGFOOD_BUILD) {
						Log.e(TAG, "Predicted app not found: " + ck);
					}
				}
				// Stop at the number of predicted apps
				if (mPredictedApps.size() == mNumPredictedAppsPerRow) {
					break;
				}
			}

		}

		// Recreate the filtered and sectioned apps (for convenience for the
		// grid layout) from the
		// ordered set of sections
		for (AppInfo info : getFiltersAppInfos()) {
			String sectionName = getAndUpdateCachedSectionName(info.title);
			{
				// Add the section to the cache
				// String sectionName =
				// getAndUpdateCachedSectionName(info.title);
				// Add it to the mapping
				ArrayList<AppInfo> sectionApps = sectionMaps.get(sectionName);
				if (sectionApps == null) {
					sectionApps = new ArrayList<>();
					sectionMaps.put(sectionName, sectionApps);
				}
				sectionApps.add(info);
			}
			mFilteredApps.add(info);
		}

		// Refresh the recycler view
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
			mAdapter.notifySideBarChanges();
		}
	}

	private List<AppInfo> getFiltersAppInfos() {
		if (mSearchResults == null) {
			return mApps;
		}

		ArrayList<AppInfo> result = new ArrayList<>();
		for (ComponentKey key : mSearchResults) {
			AppInfo match = mComponentToAppMap.get(key);
			if (match != null) {
				result.add(match);
			}
		}
		return result;
	}

	/**
	 * Returns the cached section name for the given title, recomputing and
	 * updating the cache if the title has no cached section name.
	 */
	private String getAndUpdateCachedSectionName(CharSequence title) {
		String sectionName = mCachedSectionNames.get(title);
		if (sectionName == null) {
			sectionName = mIndexer.computeSectionName(title);
			mCachedSectionNames.put(title, sectionName);
		}
		return sectionName;
	}
	TreeMap<String, ArrayList<AppInfo>> sectionMaps = new TreeMap<>();
	public TreeMap<String, ArrayList<AppInfo>> getData() {
		
		return sectionMaps;
	}
	public ArrayList<AppListBean> getData1() {
		
		Set<String> keySet = sectionMaps.keySet();
		ArrayList<AppListBean> datas = new ArrayList<>();
		for (String string : keySet) {
			AppListBean bean = new AppListBean();
			bean.setIndex(string);
			bean.setmDatas(sectionMaps.get(string));
			datas.add(bean);
		}
		
		return datas;
	}
}
