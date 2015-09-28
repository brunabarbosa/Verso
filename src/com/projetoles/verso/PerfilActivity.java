package com.projetoles.verso;

import com.projetoles.adapter.ExpandablePoesiaAdapter;
import com.projetoles.controller.SeguidaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Seguida;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class PerfilActivity extends Activity {

	private SeguidaController mSeguidaController;
	private ExpandablePoesiaAdapter mAdapter;
	private ExpandableListView mExpListView;
	private View mLoading;
	private ObjectListID<Poesia> mListPoesias;
	private int mCountCarregados;

	private void fillPoesias() {
		mExpListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					mExpListView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});
		
		mAdapter = new ExpandablePoesiaAdapter(MainActivity.sInstance, mExpListView, mListPoesias, null, mLoading);
		
		mExpListView.setAdapter(mAdapter);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		mSeguidaController = new SeguidaController(this);
		mExpListView = (ExpandableListView) findViewById(R.id.lvExp);
		mLoading = findViewById(R.id.loading);

		// preparing list data
		mListPoesias = new ObjectListID<Poesia>();
	
		for (PreloadedObject<Poesia> id : UsuarioController.usuarioLogado.getPoesias().getList()) {
			mListPoesias.add(id);
		}
		
		for (PreloadedObject<Seguida> id : UsuarioController.usuarioLogado.getSeguindo().getList()) {
			mSeguidaController.get(id.getId(), new OnRequestListener<Seguida>(this) {
 
				@Override
				public void onSuccess(Seguida result) {
					for (PreloadedObject<Poesia> id : result.getSeguido().getPoesias().getList()) {
						mListPoesias.add(id);
					}
					mCountCarregados++;
					if (mCountCarregados == UsuarioController.usuarioLogado.getSeguindo().getList().size()) {
						fillPoesias();
					}
				}

				@Override
				public void onError(String errorMessage) {
					System.err.println(errorMessage);
				}
			});
		}
	}

}
