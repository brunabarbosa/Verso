package com.projetoles.adapter;

import com.projetoles.controller.Controller;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.TemporalModel;

import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class ScrollableList<T extends TemporalModel> extends BaseAdapter {

	private static final int NUMBERS_TO_LOAD = 10;
	private static final int VISIBLE_THRESHOLD = 3;

	private int mPreviousTotalItemCount = 0;
	private boolean mLoadingPoesias = false;
	private int mAlreadyLoaded;
	private int mExpectedLoaded;

	protected Activity mContext;
	protected View mLoading;
	protected ListView mListView;
	protected ObjectListID<T> mList;
	protected Controller<T> mController;
	
	public ScrollableList(Activity context, View loading, ListView listView, ObjectListID<T> list,
			Controller<T> controller) {
		this.mContext = context;
		this.mLoading = loading;
		this.mListView = listView;
		this.mController = controller;
		this.mList = list;
		this.mList.sort();
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (totalItemCount < mPreviousTotalItemCount) {
					mPreviousTotalItemCount = totalItemCount;
					if (totalItemCount == 0) mLoadingPoesias = true;
				}
				if (!mLoadingPoesias) {
					mLoading.setVisibility(View.GONE);
				}
				if (!mLoadingPoesias && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
					loadNextPage(totalItemCount + NUMBERS_TO_LOAD);
				}
			}
		});
        loadNextPage(NUMBERS_TO_LOAD);
	}

	private void loadNextPage(int itemsToLoad) {
		if (mPreviousTotalItemCount >= mList.size()) {
			mLoadingPoesias = false;
		} else {
			mAlreadyLoaded = 0;
			mExpectedLoaded = 0;
			mLoadingPoesias = true;
			for (int i = 0; i < Math.min(mList.size(), itemsToLoad); i++) {
				if (!mList.get(i).isLoaded()) {
					mExpectedLoaded++;
					mList.get(i).load(mController, new OnRequestListener<T>(mContext) {

						@Override
						public void onSuccess(T result) {
							mAlreadyLoaded++;
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingPoesias = false;
							}
							notifyDataSetChanged();
						}

						@Override
						public void onError(String errorMessage) {
							mAlreadyLoaded++;
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingPoesias = false;
							}
						}
					});
				}
				if (mExpectedLoaded > 0) {
					mLoading.setVisibility(View.VISIBLE);
					mLoadingPoesias = true;
				} else {
					mLoading.setVisibility(View.GONE);
					mLoadingPoesias = false;
				}
			}
		}
	}

	@Override
	public int getCount() {
		int count = 0;
		for (PreloadedObject<T> id : mList.getList()) {
			if (id.isLoaded()) ++count;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return this.mList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
}
