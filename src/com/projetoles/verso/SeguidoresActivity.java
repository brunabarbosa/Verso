package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.projetoles.adapter.ListSeguidoresAdapter;
import com.projetoles.controller.SeguidaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SeguidoresActivity extends Activity {

	private SeguidaController mSeguidaController;
	private ListSeguidoresAdapter mListAdapter;
	private ListView mListView;
	private List<Seguida> mListSeguidas;
	private RelativeLayout mLoading;
	private int mCountCarregadosSeguindo;
	private int mCountCarregadosSeguidores;
	private Class mCallback;
	private Usuario mUsuario;
	protected static boolean mSeguindo;

	private void carregaSeguidas(List<String> seguidas) {
		if (!mUsuario.getSeguidores().isEmpty()) {
			mLoading.setVisibility(View.VISIBLE);
		}
		for (String id : seguidas) {
			mSeguidaController.get(id, new OnRequestListener<Seguida>(this) {

				@Override
				public void onSuccess(Seguida result) {
					mListSeguidas.add(result);
					Collections.sort(mListSeguidas);
					mListAdapter.notifyDataSetChanged();
					mCountCarregadosSeguidores++;
					runOnUiThread(new Runnable() {
						public void run() {
							if (mCountCarregadosSeguidores == mUsuario
									.getSeguidores().size()) {
								mLoading.setVisibility(View.GONE);
							}
						}
					});
				}

				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
					mCountCarregadosSeguidores++;
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seguidores);

		Bundle b = getIntent().getExtras();
		mCallback = (Class) b.get("callback");
		mUsuario = (Usuario) b.getParcelable("usuario");
		mSeguindo = b.getBoolean("seguindo");
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mSeguidaController = new SeguidaController(this);

		// Get widgets from layout
		mListView = (ListView) findViewById(R.id.lvExpSeguidores);
		mLoading = (RelativeLayout) findViewById(R.id.loadSeguidores);

		// Preparing list view
		mListSeguidas = new ArrayList<Seguida>();
		mListAdapter = new ListSeguidoresAdapter(this, mListSeguidas, mSeguindo);
		mListView.setAdapter(mListAdapter);

		if (mUsuario.equals(UsuarioController.usuarioLogado)) { 
			if (mSeguindo) {
				getActionBar().setTitle("Pessoas que eu sigo");
				carregaSeguidas(mUsuario.getSeguindo().getList());
			} else {
				getActionBar().setTitle("Meus seguidores");
				carregaSeguidas(mUsuario.getSeguidores().getList());
			}
		} else {
			if (mSeguindo) {
				getActionBar().setTitle("Pessoas que " + mUsuario.getNome() + " segue");
				carregaSeguidas(mUsuario.getSeguindo().getList());
			} else {
				getActionBar().setTitle("Seguidores de " + mUsuario.getNome());
				carregaSeguidas(mUsuario.getSeguidores().getList());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.seguidores, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(SeguidoresActivity.this, mCallback);
		i.putExtra("usuario", mUsuario);
		startActivity(i);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}