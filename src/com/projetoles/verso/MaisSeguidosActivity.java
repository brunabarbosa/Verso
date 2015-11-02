package com.projetoles.verso;

import com.projetoles.adapter.ListUsuarioAdapter;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.NumSeguidoresComparator;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MaisSeguidosActivity extends Activity {

	private ListView mListView;
	private ListUsuarioAdapter mAdapter;
	private View mLoading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curtida);

		mListView = (ListView) findViewById(R.id.lvExpPesquisa);
		mLoading = findViewById(R.id.loadCurtidas);
		mLoading.setVisibility(View.VISIBLE);
		
		UsuarioController controller = new UsuarioController(this);
		controller.getMaisSeguidos(new OnRequestListener<ObjectListID<Usuario>>(this) {
			
			@Override
			public void onSuccess(ObjectListID<Usuario> result) {
				mAdapter = new ListUsuarioAdapter(MaisSeguidosActivity.this, mLoading, mListView, result, new NumSeguidoresComparator());
				mListView.setAdapter(mAdapter);	
			}
			 
			@Override
			public void onError(String errorMessage) {
				mLoading.setVisibility(View.GONE);
			}

			@Override
			public void onTimeout() {
				mLoading.setVisibility(View.GONE);
			}
			
		});
	}

}
