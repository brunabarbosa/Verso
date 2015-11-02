package com.projetoles.verso;

import com.projetoles.adapter.ExpandablePoesiaAdapter;
import com.projetoles.model.DateComparator;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

public class ResultadoPesquisaActivity extends Activity {

	private ExpandablePoesiaAdapter mAdapter;
	private ExpandableListView mExpListView;
	private View mLoading;
	private TextView tvNenhumResultado;
	private ObjectListID<Poesia> mListPoesias;
	private Bundle mBundle;

	private void fillPoesias() {

		mExpListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					mExpListView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});
		mAdapter = new ExpandablePoesiaAdapter(ResultadoPesquisaActivity.this, mExpListView, mListPoesias, 
				mBundle, mLoading, tvNenhumResultado, false, new DateComparator<Poesia>());

		mExpListView.setAdapter(mAdapter);
	}
	
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

		mExpListView = (ExpandableListView) findViewById(R.id.lvExpPesquisa);
		mLoading = findViewById(R.id.load);
		tvNenhumResultado = (TextView) findViewById(R.id.nenhum_resultado_encontrado);
		
		mLoading.setVisibility(View.GONE);
		
		mListPoesias = (ObjectListID)b.getParcelable("resultados");
		
		fillPoesias();
		
		if (mListPoesias.isEmpty()) {
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
