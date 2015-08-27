package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import com.projetoles.adapter.ExpandablePoesiaAdapter;
import com.projetoles.controller.PoesiaController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;

public class ResultadoPesquisaActivity extends Activity {

	private PoesiaController mPoemaController;
	private ExpandablePoesiaAdapter mAdapter;
	private ExpandableListView mExpListView;
	private View mLoading;
	private TextView tvNenhumResultado;
	private List<Poesia> mListPoesias;
	private Bundle mBundle;
	private int countCarregados;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_pesquisa);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = getIntent().getExtras();
		if (b.getBundle("bundle") != null) {
			b = b.getBundle("bundle");
		}
		mBundle = b;

		mPoemaController = new PoesiaController(this);
		mExpListView = (ExpandableListView) findViewById(R.id.lvExpPesquisa);

		mListPoesias = new ArrayList<Poesia>();
		mAdapter = new ExpandablePoesiaAdapter(ResultadoPesquisaActivity.this, mListPoesias, mBundle);

		mExpListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					mExpListView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});

		mExpListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		mLoading = findViewById(R.id.load);
		tvNenhumResultado = (TextView) findViewById(R.id.nenhum_resultado_encontrado);
		
		final List<String> resultados = b.getStringArrayList("resultados");
		if (resultados.isEmpty()) {
			mLoading.setVisibility(View.GONE);
		} else {
			tvNenhumResultado.setVisibility(View.GONE);
			for (String id : resultados) {
				mPoemaController.get(id, new OnRequestListener<Poesia>(this) {

					@Override
					public void onSuccess(Poesia p) {
						mListPoesias.add(p);
						Collections.sort(mListPoesias);
						mAdapter.notifyDataSetChanged();
						countCarregados++;
						if (countCarregados == resultados.size()) {
							mLoading.setVisibility(View.GONE);
						}
					}

					@Override
					public void onError(String errorMessage) {
						System.out.println(errorMessage);
					}
				});
			}
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
