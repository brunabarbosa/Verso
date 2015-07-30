package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;

public class PerfilActivity extends Activity {

	private PoesiaController mPoesiaController;
	private ExpandablePoesiaAdapter mAdapter;
	private ExpandableListView mExpListView;
	private List<Poesia> mListPoesias;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		mPoesiaController = new PoesiaController(this);
		mExpListView = (ExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		mListPoesias = new ArrayList<Poesia>();
		mAdapter = new ExpandablePoesiaAdapter(MainActivity.sInstance, mListPoesias, null);

		mExpListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					mExpListView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});

		mExpListView.setAdapter(mAdapter);
		for (String id : UsuarioController.usuarioLogado.getPoesias().getList()) {
			mPoesiaController.get(id, new OnRequestListener<Poesia>(this) {

				@Override
				public void onSuccess(Poesia p) {
					mListPoesias.add(p);
					Collections.sort(mListPoesias);
					mAdapter.notifyDataSetChanged();

				}

				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
				}
			});
		}
	}

}
