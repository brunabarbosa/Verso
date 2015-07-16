package com.projetoles.verso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.projetoles.controller.PoesiaController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Curtida;
import com.projetoles.model.Poesia;

public class CurtidaActivity extends Activity {

	private Poesia mPoesia;
	private PoesiaController mController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pessoas_que_curtiram);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle b = getIntent().getExtras();
		mPoesia = (Poesia) b.getParcelable("poesia");
		getActionBar().setTitle(mPoesia.getTitulo() + " - Curtidas");
		mController = new PoesiaController(this);
		
		final List<String> curtidas = new ArrayList<String>();
		ListView lv = (ListView) findViewById(R.id.lvExpPesquisa);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, curtidas);
		lv.setAdapter(adapter);
		for (String id : mPoesia.getCurtidas()) {
			mController.getCurtida(id, new OnRequestListener(this) {
				
				@Override
				public void onSuccess(Object result) {
					Curtida c = (Curtida) result;
					String data = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getDataCriacao().getTime());
					curtidas.add(c.getPostador() + " curtiu isso em " + data + ".");
					adapter.notifyDataSetChanged();
				}
				
				@Override
				public void onError(String errorMessage) {
					// TODO Auto-generated method stub
					
				}
			});
		}
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
