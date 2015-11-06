package com.projetoles.verso;

import com.projetoles.adapter.ExpandablePoesiaAdapter;
import com.projetoles.controller.SeguidaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.DataComparator;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class PerfilActivity extends Activity {

	private SeguidaController mSeguidaController;
	private ExpandablePoesiaAdapter mAdapter;
	private ExpandableListView mExpListView;
	private View mLoading;
	private ObjectListID<Poesia> mListPoesias;
	private int mCountCarregados;
	private View mEmpty;
	private Button mBtnDescobrir;

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
		
		mAdapter = new ExpandablePoesiaAdapter(MainActivity.sInstance, mExpListView, mListPoesias, null, 
				mLoading, mEmpty, true, new DataComparator<Poesia>());
		
		mExpListView.setAdapter(mAdapter);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		mSeguidaController = new SeguidaController(this);
		mExpListView = (ExpandableListView) findViewById(R.id.lvExp);
		mLoading = findViewById(R.id.loading);
		mEmpty = findViewById(R.id.empty);
		mBtnDescobrir = (Button) findViewById(R.id.btnDescobrir);

		// preparing list data
		mListPoesias = new ObjectListID<Poesia>();
	
		UsuarioController controller = new UsuarioController(this);
		controller.getUsuarioLogado(new OnRequestListener<Usuario>(this) {

			@Override
			public void onSuccess(final Usuario usuarioLogado) {
				for (PreloadedObject<Poesia> id : usuarioLogado.getPoesias().getList()) {
					mListPoesias.add(id);
				}
				
				if (usuarioLogado.getSeguindo().isEmpty()) {
					fillPoesias();
					ActivityUtils.loadSharedPoetries(PerfilActivity.this, usuarioLogado, mListPoesias, mAdapter, true);
				}
				
				for (PreloadedObject<Seguida> id : usuarioLogado.getSeguindo().getList()) {
					mSeguidaController.get(id.getId(), new OnRequestListener<Seguida>(PerfilActivity.this) {
		 
						@Override
						public void onSuccess(Seguida result) {
							for (PreloadedObject<Poesia> id : result.getSeguido().getPoesias().getList()) {
								mListPoesias.add(id);
							}
							mCountCarregados++;
							if (mCountCarregados == usuarioLogado.getSeguindo().getList().size()) {
								fillPoesias();
								ActivityUtils.loadSharedPoetries(PerfilActivity.this, usuarioLogado, mListPoesias, mAdapter, true);
							}
						}

						@Override
						public void onError(String errorMessage) {
							System.err.println(errorMessage);
						}

						@Override
						public void onTimeout() {
							System.out.println("TIMEOUT");
						}
					});
				}
				
				mBtnDescobrir.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PerfilActivity.this, DescobrirActivity.class);
						startActivity(intent);
					}
				});
			}

			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
		}, null);
	}

}
