package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.projetoles.adapter.ExpandablePoesiaAdapter;
import com.projetoles.adapter.ListComentarioAdapter;
import com.projetoles.controller.ComentarioController;
import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Comentario;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ComentarioActivity extends Activity {

	private ComentarioController mComentarioController;
	private PoesiaController mPoesiaController;
	private Poesia mPoesia;
	private ListComentarioAdapter mListAdapter;
	private ListView mListView;
	private List<Comentario> mListComentarios;
	private Button btnCriaComentario;
	private EditText etComentario;
	private Class mCallback;
	private Bundle mBundle;
	private RelativeLayout mLoading;
	private int mCountCarregados;
	private boolean mPosting;
	
	private void criarComentario() {
		btnCriaComentario.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mPosting) {
					mPosting = true;
					final String comentario = etComentario.getText().toString();
					mLoading.setVisibility(View.VISIBLE);
					mComentarioController.post(UsuarioController.usuarioLogado, mPoesia, comentario, new OnRequestListener<Comentario>(ComentarioActivity.this) {
	 
						@Override
						public void onSuccess(Comentario comentario) {
							if (!mListComentarios.contains(comentario)) {
								mPoesia.getComentarios().add(comentario.getId(), comentario.getDataCriacao().getTimeInMillis());
								mListComentarios.add(comentario);
								Collections.sort(mListComentarios);
								mListAdapter.notifyDataSetChanged();
								mLoading.setVisibility(View.GONE);
								etComentario.setText("");
							}
							mPosting = false;
						}

						@Override
						public void onError(String errorMessage) {
							mPosting = false;
							System.out.println(errorMessage);
							ActivityUtils.showMessageDialog(ComentarioActivity.this, "Um erro ocorreu", errorMessage, mLoading);
						}
					});
				}
				
			}
		});
	}
	
	private void carregarComentarios() {
		if (!mPoesia.getComentarios().isEmpty()) {
			mLoading.setVisibility(View.VISIBLE);
		}
		for (PreloadedObject<Comentario> comentario : mPoesia.getComentarios().getList()) {
			mComentarioController.get(comentario.getId(), new OnRequestListener<Comentario>(this) {

				@Override
				public void onSuccess(Comentario comentario) {
					mListComentarios.add(comentario);
					Collections.sort(mListComentarios);
					mCountCarregados++;
					runOnUiThread(new Runnable() {
						public void run() {
							if (mCountCarregados == mPoesia.getComentarios().size()) {
								mLoading.setVisibility(View.GONE);
							}
							mListAdapter.notifyDataSetChanged();
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
		setContentView(R.layout.activity_comentario);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	
		// Get intent
		Bundle b = getIntent().getExtras();
		mPoesia = (Poesia) b.getParcelable("poesia");
		mCallback = (Class) b.get("callback");
		mBundle = b.getBundle("bundle");
		getActionBar().setTitle(mPoesia.getTitulo() + " - Comentários");

		mComentarioController = new ComentarioController(this);
		mPoesiaController = new PoesiaController(this);

		// Get widgets from layout
		mListView = (ListView) findViewById(R.id.lvExpComentario);
		btnCriaComentario = (Button) findViewById(R.id.sendComentario);
		etComentario = (EditText) findViewById(R.id.novoComentario);
		mLoading = (RelativeLayout) findViewById(R.id.loadComentario);
		
		// Preparing list view
		mListComentarios = new ArrayList<Comentario>();
		mListAdapter = new ListComentarioAdapter(ComentarioActivity.this, mListComentarios);
		mListView.setAdapter(mListAdapter);

		TextView poesiaTitulo = (TextView) findViewById(R.id.poesiaTitulo);
		poesiaTitulo.setText(mPoesia.getTitulo());
		TextView poesiaConteudo = (TextView) findViewById(R.id.poesia);
		poesiaConteudo.setText(mPoesia.getPoesia());
		TextView poesiaTags = (TextView) findViewById(R.id.poesiaTags); 
		ExpandablePoesiaAdapter.setPoesiaTags(poesiaTags, mPoesia, mPoesiaController, this);
		TextView poesiaData = (TextView) findViewById(R.id.poesiaData);
		ExpandablePoesiaAdapter.setPoesiaData(poesiaData, mPoesia, this);
		
		carregarComentarios();
		criarComentario();
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(ComentarioActivity.this, mCallback);
		i.putExtra("bundle", mBundle);
		startActivity(i);
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
