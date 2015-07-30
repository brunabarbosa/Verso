package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.projetoles.controller.SeguidaController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

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

	private void carregarSeguidores() {
		if (!mUsuario.getSeguidores().isEmpty()) {
			mLoading.setVisibility(View.VISIBLE);
		}
		for (String id : mUsuario.getSeguidores().getList()) {
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

	private void carregarSeguindo() {
		if (!mUsuario.getSeguindo().isEmpty()) {
			mLoading.setVisibility(View.VISIBLE);
		}
		for (String id : mUsuario.getSeguindo().getList()) {
			mSeguidaController.get(id, new OnRequestListener<Seguida>(this) {

				@Override
				public void onSuccess(Seguida result) {
					mListSeguidas.add(result);
					Collections.sort(mListSeguidas);
					mListAdapter.notifyDataSetChanged();
					mCountCarregadosSeguindo++;
					runOnUiThread(new Runnable() {
						public void run() {
							if (mCountCarregadosSeguindo == mUsuario
									.getSeguindo().size()) {
								mLoading.setVisibility(View.GONE);
							}
						}
					});
				}

				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
					mCountCarregadosSeguindo++;
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
		mListAdapter = new ListSeguidoresAdapter(SeguidoresActivity.this,
				mListSeguidas);
		mListView.setAdapter(mListAdapter);

		if (mSeguindo) {
			getActionBar().setTitle(mUsuario.getNome() + " está Seguindo:");
			carregarSeguindo();
		} else {
			getActionBar()
					.setTitle("Seguidores de " + mUsuario.getNome() + ":");
			carregarSeguidores();
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
		Intent i = new Intent(SeguidoresActivity.this, MainActivity.class);
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