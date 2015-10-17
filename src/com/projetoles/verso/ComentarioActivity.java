package com.projetoles.verso;

import com.projetoles.adapter.ExpandablePoesiaAdapter;
import com.projetoles.adapter.ListComentarioAdapter;
import com.projetoles.controller.ComentarioController;
import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Comentario;
import com.projetoles.model.ObjectListID;
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
	private Button btnCriaComentario;
	private EditText etComentario;
	private Class mCallback;
	private Bundle mBundle;
	private RelativeLayout mLoading;
	private boolean mPosting;
	private ObjectListID<Comentario> mList;
	
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
							mLoading.setVisibility(View.GONE);
							mPosting = false;
							if (!mList.contains(comentario.getId())) {
								PreloadedObject<Comentario> obj = new PreloadedObject<Comentario>(comentario.getDataCriacao(), comentario.getId());
								obj.setLoadedObj(comentario);
								mList.add(obj);
								mList.sort();
								mPoesia.getComentarios().add(comentario.getId(), comentario.getDataCriacao().getTimeInMillis());
								mPoesiaController.update(mPoesia, new OnRequestListener<Poesia>(ComentarioActivity.this) {

									@Override
									public void onSuccess(Poesia result) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onError(String errorMessage) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onTimeout() {
										// TODO Auto-generated method stub
										
									}
								});
								mListAdapter.notifyDataSetChanged();
								etComentario.setText("");
							}
						}

						@Override
						public void onError(String errorMessage) {
							mLoading.setVisibility(View.GONE);
							mPosting = false;
							ActivityUtils.showMessageDialog(ComentarioActivity.this, "Um erro ocorreu", errorMessage, mLoading);
						}

						@Override
						public void onTimeout() {
							mLoading.setVisibility(View.GONE);
							mPosting = false;
							ActivityUtils.showMessageDialog(ComentarioActivity.this, "Ops", "Ocorreu um erro com a sua requisição. Verifique sua conexão com a internet.", mLoading);
						}
					});
				}
				
			}
		});
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
		mList = new ObjectListID<Comentario>();
		for (PreloadedObject<Comentario> id : mPoesia.getComentarios().getList()) {
			mList.add(id);
		}
		mListAdapter = new ListComentarioAdapter(ComentarioActivity.this, mLoading, mListView, mList);
		mListView.setAdapter(mListAdapter);

		TextView poesiaTitulo = (TextView) findViewById(R.id.poesiaTitulo);
		poesiaTitulo.setText(mPoesia.getTitulo());
		TextView poesiaConteudo = (TextView) findViewById(R.id.poesia);
		poesiaConteudo.setText(mPoesia.getPoesia());
		TextView poesiaTags = (TextView) findViewById(R.id.poesiaTags); 
		ExpandablePoesiaAdapter.setPoesiaTags(poesiaTags, mPoesia, mPoesiaController, this);
		TextView poesiaData = (TextView) findViewById(R.id.poesiaData);
		ExpandablePoesiaAdapter.setPoesiaData(poesiaData, mPoesia, this);
		
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
