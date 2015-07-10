package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.projetoles.controller.PoesiaController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;

public class ResultadoPesquisaActivity extends Activity {

	private PoesiaController poemaController;
	private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<Poesia> listPoesias;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_pesquisa);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		//change fonts
		poemaController = new PoesiaController(this);

		// get the listview 
		expListView = (ExpandableListView) findViewById(R.id.lvExpPesquisa);
		
		//preparing list data
		listPoesias = new ArrayList<Poesia>();
        listAdapter = new ExpandableListAdapter(ResultadoPesquisaActivity.this, listPoesias);
		
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
	        int previousGroup = -1;

	        @Override
	        public void onGroupExpand(int groupPosition) {
	            if(groupPosition != previousGroup)
	            	expListView.collapseGroup(previousGroup);
	            previousGroup = groupPosition;
	        }
	    });
		
		//setting the list adapter
		expListView.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
		
		Bundle b = getIntent().getExtras();
		List<String> resultados = b.getStringArrayList("resultados");
		for (String id : resultados) {
			poemaController.getPoesia(id, new OnRequestListener(this) {
				
				@Override
				public void onSuccess(Object result) {
					Poesia p = (Poesia) result;
					listPoesias.add(p);
					Collections.sort(listPoesias);
			        listAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
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
