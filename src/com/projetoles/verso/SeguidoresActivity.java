package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.projetoles.controller.NotificacaoController;
import com.projetoles.controller.SeguidaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Notificacao;
import com.projetoles.model.Seguida;

public class SeguidoresActivity extends Activity {

	private SeguidaController mSeguidaController;
	private ListSeguidaAdapter mListAdapter;
	private ListView mListView;
	private List<Seguida> mListSeguidas;
	private RelativeLayout mLoading;
	private int mCountCarregados;
	
	private void carregarSeguidores() {
		if (!UsuarioController.usuarioLogado.getSeguidores().isEmpty()) {
			mLoading.setVisibility(View.VISIBLE);
		}
		for (String id : UsuarioController.usuarioLogado.getSeguidores().getList()) {
			mSeguidaController.get(id, new OnRequestListener<Seguida>(this) {

				@Override
				public void onSuccess(Seguida result) {
					mListSeguidas.add(result);
					Collections.sort(mListSeguidas);
					mListAdapter.notifyDataSetChanged();
					mCountCarregados++;
					runOnUiThread(new Runnable() {
						public void run() {
							if (mCountCarregados == UsuarioController.usuarioLogado.getSeguidores().size()) {
								mLoading.setVisibility(View.GONE);
							}
						}
					});
				}

				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
					mCountCarregados++;
				}
			});
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seguidores);
		
		mSeguidaController = new SeguidaController(this);

		// Get widgets from layout
		mListView = (ListView) findViewById(R.id.lvExpSeguidores);
		mLoading = (RelativeLayout) findViewById(R.id.loadSeguidores);
		
		// Preparing list view
		mListSeguidas = new ArrayList<Seguida>();
		mListAdapter = new ListSeguidaAdapter(SeguidoresActivity.this, mListSeguidas);
		mListView.setAdapter(mListAdapter);

		carregarSeguidores();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.seguidores, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
