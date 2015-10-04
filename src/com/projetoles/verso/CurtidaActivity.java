package com.projetoles.verso;

import com.projetoles.adapter.ListCurtidaAdapter;
import com.projetoles.model.Curtida;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class CurtidaActivity extends Activity {

	private Poesia mPoesia;
	private ObjectListID<Curtida> mListCurtidas;
	private ListView mListView;
	private ListCurtidaAdapter mAdapter;
	private View mLoading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curtida);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle b = getIntent().getExtras();
		mPoesia = (Poesia) b.getParcelable("poesia");
		getActionBar().setTitle(mPoesia.getTitulo() + " - Curtidas");
		
		mListCurtidas = new ObjectListID<Curtida>();
		mListView = (ListView) findViewById(R.id.lvExpPesquisa);
		mLoading = findViewById(R.id.loadCurtidas);
		
		for (PreloadedObject<Curtida> id : mPoesia.getCurtidas().getList()) {
			mListCurtidas.add(id);
		}
	
		mAdapter = new ListCurtidaAdapter(this, mLoading, mListView, mListCurtidas);
		mListView.setAdapter(mAdapter);	
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
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
