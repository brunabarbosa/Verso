package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.SeguidaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;
import com.projetoles.model.Seguida;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class PerfilActivity extends Activity {

	private PoesiaController mPoesiaController;
	private UsuarioController mUsuarioController;
	private SeguidaController mSeguidaController;
	private ExpandablePoesiaAdapter mAdapter;
	private ExpandableListView mExpListView;
	private List<Poesia> mListPoesias;

	private void addPoesia(Poesia p) {
		mListPoesias.add(p);
		Collections.sort(mListPoesias);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		mPoesiaController = new PoesiaController(this);
		mUsuarioController = new UsuarioController(this);
		mSeguidaController = new SeguidaController(this);
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
		for (String id : UsuarioController.usuarioLogado.getSeguindo().getList()) {
			mSeguidaController.get(id, new OnRequestListener<Seguida>(this) {

				@Override
				public void onSuccess(Seguida result) {
					for (String id : result.getSeguido().getPoesias().getList()) {
						mPoesiaController.get(id, new OnRequestListener<Poesia>(PerfilActivity.this) {

							@Override
							public void onSuccess(Poesia result) {
								addPoesia(result);
							}

							@Override
							public void onError(String errorMessage) {
								System.err.println(errorMessage);
							}
						});
					}
				}

				@Override
				public void onError(String errorMessage) {
					System.err.println(errorMessage);
				}
			});
		}
		for (String id : UsuarioController.usuarioLogado.getPoesias().getList()) {
			mPoesiaController.get(id, new OnRequestListener<Poesia>(this) {

				@Override
				public void onSuccess(Poesia p) {
					addPoesia(p);
				}

				@Override
				public void onError(String errorMessage) {
					System.err.println(errorMessage);
				} 
			});
		}
	}

}
