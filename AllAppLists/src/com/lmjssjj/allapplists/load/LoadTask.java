package com.lmjssjj.allapplists.load;

import java.util.ArrayList;
import java.util.List;

import com.lmjssjj.allapplists.AppInfo;
import com.lmjssjj.allapplists.compat.LauncherActivityInfoCompat;
import com.lmjssjj.allapplists.compat.LauncherAppsCompat;
import com.lmjssjj.allapplists.compat.UserHandleCompat;
import com.lmjssjj.allapplists.compat.UserManagerCompat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

public class LoadTask extends AsyncTask<Void, Void, ArrayList<AppInfo>> {

	private LauncherAppsCompat mLauncherApps;
	private UserManagerCompat mUserManager;

	public static final int DEFAULT_APPLICATIONS_NUMBER = 42;

	/** The list off all apps. */
	public ArrayList<AppInfo> mBgAllAppsList = new ArrayList<AppInfo>(DEFAULT_APPLICATIONS_NUMBER);

	private Context mContext;
	private OnLoadingListener mLoadingListener;

	public LoadTask(Context context , OnLoadingListener listener) {
		this.mContext = context;
		this.mLoadingListener = listener;
		mUserManager = UserManagerCompat.getInstance(mContext);
		mLauncherApps = LauncherAppsCompat.getInstance(mContext);
	}

	private ArrayList<AppInfo> loadAllApps() {

		final long loadTime = SystemClock.uptimeMillis();

		final List<UserHandleCompat> profiles = mUserManager.getUserProfiles();

		// Clear the list of apps
		mBgAllAppsList.clear();
		for (UserHandleCompat user : profiles) {
			// Query for the set of apps
			final long qiaTime = SystemClock.uptimeMillis();
			final List<LauncherActivityInfoCompat> apps = mLauncherApps.getActivityList(null, user);

			// Fail if we don't have any apps
			// TODO: Fix this. Only fail for the current user.
			if (apps == null || apps.isEmpty()) {
				return null;
			}
			boolean quietMode = mUserManager.isQuietModeEnabled(user);
			// Create the ApplicationInfos
			for (int i = 0; i < apps.size(); i++) {
				LauncherActivityInfoCompat app = apps.get(i);
				// This builds the icon bitmaps.
				mBgAllAppsList.add(new AppInfo(mContext, app, user, quietMode));
			}

		}
		return mBgAllAppsList;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mLoadingListener!=null){
			mLoadingListener.onLoadingStart();
		}
	}
	
	@Override
	protected ArrayList<AppInfo> doInBackground(Void... params) {
		ArrayList<AppInfo> loadAllApps = loadAllApps();
		return loadAllApps;
	}

	@Override
	protected void onPostExecute(ArrayList<AppInfo> result) {
		super.onPostExecute(result);
		if(mLoadingListener!=null){
			mLoadingListener.onLoadingFinish(result);
		}

	}
}
