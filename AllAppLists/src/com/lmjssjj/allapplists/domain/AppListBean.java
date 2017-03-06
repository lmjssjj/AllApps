package com.lmjssjj.allapplists.domain;

import java.util.List;

import com.lmjssjj.allapplists.AppInfo;


public class AppListBean {
	
	private String index;
	private List<AppInfo> mDatas;
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public List<AppInfo> getmDatas() {
		return mDatas;
	}
	public void setmDatas(List<AppInfo> mDatas) {
		this.mDatas = mDatas;
	}

}
