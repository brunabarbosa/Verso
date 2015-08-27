package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.projetoles.adapter.ListCurtidaAdapter;
import com.projetoles.controller.CurtidaController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Curtida;
import com.projetoles.model.Poesia;

public class CurtidaActivity extends Activity {

	private Poesia mPoesia;
	private CurtidaController mCurtidaController;
	private List<Curtida> mListCurtidas;
	private ListView mListView;
	private ListCurtidaAdapter mAdapter;
	
	private void carregaCurtidas() {
		for (String id : mPoesia.getCurtidas().getList()) {
			mCurtidaController.get(id, new OnRequestListener<Curtida>(this) {
				 
				@Override 
				public void onSuccess(Curtida curtida) {
					mListCurtidas.add(curtida);
					Collections.sort(mListCurtidas);
					mAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
				}
			});
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curtida);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle b = getIntent().getExtras();
		mPoesia = (Poesia) b.getParcelable("poesia");
		getActionBar().setTitle(mPoesia.getTitulo() + " - Curtidas");
		
		mCurtidaController = new CurtidaController(this);
		
		mListCurtidas = new ArrayList<Curtida>();
		mListView = (ListView) findViewById(R.id.lvExpPesquisa);
		mAdapter = new ListCurtidaAdapter(this, mListCurtidas);
		mListView.setAdapter(mAdapter);
		
		carregaCurtidas(); 
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
