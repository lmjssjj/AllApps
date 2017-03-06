package com.lmjssjj.allapplists;

import java.util.ArrayList;

import com.lmjssjj.allapplists.adapter.AllAppsCustomGridAdapter;
import com.lmjssjj.allapplists.load.LoadTask;
import com.lmjssjj.allapplists.load.OnLoadingListener;
import com.lmjssjj.allapplists.sortbyindex.AlphabeticalAppsList;
import com.lmjssjj.allapplists.view.LmjssjjLetterIndexer;
import com.lmjssjj.allapplists.view.SideBar;
import com.lmjssjj.allapplists.view.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class MainActivity extends Activity
		implements OnLoadingListener, OnClickListener, OnLongClickListener, OnTouchListener {

	private RecyclerView mRV;
	private AlphabeticalAppsList mApps;
	private AllAppsCustomGridAdapter mAdapter;
	private SideBar mSideBar;
	private LmjssjjLetterIndexer mFi;
	private TextView mTv;
	private LoadTask mLoadTask;
	private ArrayList<AppInfo> mDatas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLoadTask = new LoadTask(this, this);
		initView();
		initData();
		initEvent();

	}

	private void initView() {
		mRV = (RecyclerView) findViewById(R.id.rv);
		mSideBar = (SideBar) findViewById(R.id.sidrbar);
		mFi = (LmjssjjLetterIndexer) findViewById(R.id.bar);
		mTv = (TextView) findViewById(R.id.tv);

		mSideBar.setTextView(mTv);

	}

	private void initData() {
		mLoadTask.execute();
		mApps = new AlphabeticalAppsList(this);
		mAdapter = new AllAppsCustomGridAdapter(this, mApps, this, this, this);
		mAdapter.setSideBar(mSideBar);
		mAdapter.setLetter(mFi);
		mRV.setAdapter(mAdapter);
		mApps.setAdapter(mAdapter);
		mRV.setLayoutManager(new LinearLayoutManager(this));
	}

	private void initEvent() {
		mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(int s) {
				// 该字母首次出现的位置
				mRV.getLayoutManager().scrollToPosition(s);

			}
		});
		
		mFi.setOnTouchLetterChangedListener(new LmjssjjLetterIndexer.OnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String letter,int index) {

            	mRV.getLayoutManager().scrollToPosition(index);
            }

            @Override
            public void onTouchActionUp(String letter) {
//                showLetter(letter);
            }
        });
	}

	@Override
	public void onLoadingStart() {

	}

	@Override
	public void onLoadingFinish(ArrayList<AppInfo> infos) {
		mDatas = infos;
		
		mApps.setApps(mDatas);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
