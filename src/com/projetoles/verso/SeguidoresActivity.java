package com.projetoles.verso;

import com.projetoles.adapter.ListSeguidoresAdapter;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.DataComparator;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SeguidoresActivity extends Activity {

	private ListSeguidoresAdapter mListAdapter;
	private ListView mListView;
	private ObjectListID<Seguida> mListSeguidas;
	private RelativeLayout mLoading;
	private Class mCallback;
	private Usuario mUsuario;
	protected static boolean mSeguindo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seguidores);

		Bundle b = getIntent().getExtras();
		mCallback = (Class) b.get("callback");
		mUsuario = (Usuario) b.getParcelable("usuario");
		mSeguindo = b.getBoolean("seguindo");
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Get widgets from layout
		mListView = (ListView) findViewById(R.id.lvExpSeguidores);
		mLoading = (RelativeLayout) findViewById(R.id.loadSeguidores);

		mListSeguidas = new ObjectListID<Seguida>();
		
		UsuarioController controller = new UsuarioController(this);
		controller.getUsuarioLogado(new OnRequestListener<Usuario>(this) {

			@Override
			public void onSuccess(final Usuario usuarioLogado) {
				if (mUsuario.equals(usuarioLogado)) { 
					if (mSeguindo) {
						getActionBar().setTitle("Pessoas que eu sigo");
						for (PreloadedObject<Seguida> id : mUsuario.getSeguindo().getList()) {
							mListSeguidas.add(id);
						}
					} else {
						getActionBar().setTitle("Meus seguidores");
						for (PreloadedObject<Seguida> id : mUsuario.getSeguidores().getList()) {
							mListSeguidas.add(id);
						}
					}
				} else {
					if (mSeguindo) {
						getActionBar().setTitle("Pessoas que " + mUsuario.getNome() + " segue");
						for (PreloadedObject<Seguida> id : mUsuario.getSeguindo().getList()) {
							mListSeguidas.add(id);
						}
					} else {
						getActionBar().setTitle("Seguidores de " + mUsuario.getNome());
						for (PreloadedObject<Seguida> id : mUsuario.getSeguidores().getList()) {
							mListSeguidas.add(id);
						}
					}
				}
				
				// Preparing list view
				mListAdapter = new ListSeguidoresAdapter(SeguidoresActivity.this, mLoading, mListView, 
						mListSeguidas, new DataComparator<Seguida>(), mSeguindo);
				mListView.setAdapter(mListAdapter);
			}
			
			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		}, null);
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