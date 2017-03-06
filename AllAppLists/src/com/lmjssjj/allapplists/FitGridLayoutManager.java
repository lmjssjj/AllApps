package com.lmjssjj.allapplists;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class FitGridLayoutManager extends GridLayoutManager {
	
	// RecyclerView高度随Item自适应
	public FitGridLayoutManager(Context context, int spanCount) {
		super(context, spanCount);
	}
	
	@Override
	public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, final int widthSpec,
			final int heightSpec) {
			// 不能使用 View view = recycler.getViewForPosition(0);
			// measureChild(view, widthSpec, heightSpec);
			// int measuredHeight view.getMeasuredHeight(); 这个高度不准确
			
			  int height = 0;
		        int childCount = getItemCount();
		        for (int i = 0; i < childCount; i++) {
		            View child = recycler.getViewForPosition(i);
		            measureChild(child, widthSpec, heightSpec);
		            if (i % getSpanCount() == 0) {
		                int measuredHeight = child.getMeasuredHeight() + getDecoratedBottom(child);
		                height += measuredHeight;
		            }
		        }
		        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);
	}
}
