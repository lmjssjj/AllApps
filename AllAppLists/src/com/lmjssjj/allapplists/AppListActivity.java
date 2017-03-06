package com.lmjssjj.allapplists;

import com.lmjssjj.allapplists.sortbyindex.AlphabeticalAppsList;
import com.lmjssjj.allapplists.view.SideBar;
import com.lmjssjj.allapplists.view.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


public class AppListActivity extends Activity implements OnItemClickListener{

	private ListView mLv;
	private AlphabeticalAppsList mApps;
	private SideBar mSideBar;
	private TextView mTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_apps_listview);
		init();
		
		initEvent();
		Log.v("lmjssjj", "onCreate");
	}

	private void init() {
		
		 mApps = new AlphabeticalAppsList(this);
		
		
		
		mSideBar = (SideBar) findViewById(R.id.sidrbar);
		mSideBar.setTextView(mTv);
		
		
	}
	
	private void initEvent(){
		mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(int s) {
				// 该字母首次出现的位置
				

			}
		});
		
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

	

}
