package com.lmjssjj.allapplists.load;

import java.util.ArrayList;

import com.lmjssjj.allapplists.AppInfo;

public interface OnLoadingListener {
	
	void onLoadingStart();
	void onLoadingFinish(ArrayList<AppInfo> infos);

}
