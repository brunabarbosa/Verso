package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.SeguidaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;
import com.projetoles.model.Seguida;

public class PerfilActivity extends Activity {

	private PoesiaController mPoesiaController;
	private SeguidaController mSeguidaController;
	private ExpandablePoesiaAdapter mAdapter;
	private ExpandableListView mExpListView;
	private List<Poesia> mListPoesias;

	private void getId(String id) {
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);
		
		TextView seguidores = (TextView) findViewById(R.id.txtNumSeguidores);
		seguidores.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(PerfilActivity.this, SeguidoresActivity.class);
			}
		});

		mPoesiaController = new PoesiaController(this);
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
		for (String id : UsuarioController.usuarioLogado.getPoesias().getList()) {
			getId(id);
		}
		for (String seguidaId : UsuarioController.usuarioLogado.getSeguindo().getList()) {
			mSeguidaController.get(seguidaId, new OnRequestListener<Seguida>(this) {

				@Override
				public void onSuccess(Seguida seguida) {
					for (String poesiaId : seguida.getSeguido().getPoesias().getList()) {
						getId(poesiaId);
					}
				}

				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
				}
			});
		}
	}

}
