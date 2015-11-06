package com.projetoles.verso;

import com.projetoles.adapter.ListUsuarioAdapter;
import com.projetoles.model.NumSeguidoresComparator;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ResultadoPesquisaUsuarioActivity extends Activity {

	private ListUsuarioAdapter mAdapter;
	private ListView mListView;
	private View mLoading;
	private TextView tvNenhumResultado;
	private ObjectListID<Usuario> mListUsuarios;
	
	private void fillUsuarios() {
		mAdapter = new ListUsuarioAdapter(this, mLoading, mListView, mListUsuarios, new NumSeguidoresComparator());
		mListView.setAdapter(mAdapter);	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_pesquisa_usuario);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = getIntent().getExtras();
		if (b.getBundle("bundle") != null) {
			b = b.getBundle("bundle");
		}

		mListView = (ListView) findViewById(R.id.lvExpPesquisa);
		mLoading = findViewById(R.id.load);
		tvNenhumResultado = (TextView) findViewById(R.id.nenhum_resultado_encontrado);
		
		mLoading.setVisibility(View.GONE);
		
		mListUsuarios = (ObjectListID)b.getParcelable("resultados");
		
		fillUsuarios();
		
		if (mListUsuarios.isEmpty()) {
			tvNenhumResultado.setVisibility(View.VISIBLE);
		} else {
			tvNenhumResultado.setVisibility(View.GONE);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
