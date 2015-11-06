package com.projetoles.verso;

import com.projetoles.adapter.ExpandablePoesiaAdapter;
import com.projetoles.controller.PoesiaController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.DataComparator;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class MaisRecentesActivity extends Activity {
	
	private ExpandableListView mExpListView;
	private ExpandablePoesiaAdapter mAdapter;
	private View mLoading;
	private View mEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_poesias);
		
		// set userPicture, precisa consertar nessa classe
		mLoading = findViewById(R.id.loading);
		mEmpty = findViewById(R.id.sem_poesia);

		mExpListView = (ExpandableListView) findViewById(R.id.lvPoesiasDoUserExp);

		// preparing list data
		PoesiaController controller = new PoesiaController(this);
		controller.getMaisRecentes(new OnRequestListener<ObjectListID<Poesia>>(this) {

			@Override
			public void onSuccess(ObjectListID<Poesia> result) {
				mExpListView.setOnGroupExpandListener(new OnGroupExpandListener() {
					int previousGroup = -1;

					@Override
					public void onGroupExpand(int groupPosition) {
						if (groupPosition != previousGroup)
							mExpListView.collapseGroup(previousGroup);
						previousGroup = groupPosition;
					}
				});
				
				mAdapter = new ExpandablePoesiaAdapter(MaisRecentesActivity.this, mExpListView, result, null, mLoading, mEmpty, 
						false, new DataComparator<Poesia>());

				mExpListView.setAdapter(mAdapter);
			}

			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
