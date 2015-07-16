package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.projetoles.controller.PoesiaController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Comentario;
import com.projetoles.model.Poesia;

public class ComentarioActivity extends Activity {

	private PoesiaController mPoesiaController;
	private Poesia mPoesia;
	private ExpandableComentarioAdapter listAdapter;
	private ExpandableListView expListView;
	private List<Comentario> listComentarios;
	private Class mCallback;
	private Bundle mBundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comentario);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = getIntent().getExtras();
		mPoesia = (Poesia) b.getParcelable("poesia");
		mCallback = (Class) b.get("callback");
		mBundle = b.getBundle("bundle");
		getActionBar().setTitle(mPoesia.getTitulo());

		mPoesiaController = new PoesiaController(this);

		// get the listview 
		expListView = (ExpandableListView) findViewById(R.id.lvExpComentario);

		//preparing list data
		listComentarios = new ArrayList<Comentario>();
		listAdapter = new ExpandableComentarioAdapter(ComentarioActivity.this, listComentarios);

		//setting the list adapter
		expListView.setAdapter(listAdapter);

		for (String comentario : mPoesia.getComentarios()) {
			mPoesiaController.getComentario(comentario, new OnRequestListener(this) {

				@Override
				public void onSuccess(Object result) {
					Comentario comentario = (Comentario) result;
					listComentarios.add(comentario);
					Collections.sort(listComentarios);
					listAdapter.notifyDataSetChanged();
				}

				@Override
				public void onError(String errorMessage) {
					// TODO Auto-generated method stub
					System.out.println(errorMessage);
				}
			});
		}

		final Button button = (Button) findViewById(R.id.sendComentario);
		final EditText editText = (EditText) findViewById(R.id.novoComentario);
		final RelativeLayout loading = (RelativeLayout) findViewById(R.id.loadComentario);
		
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String comentarioString = editText.getText().toString();
				loading.setVisibility(View.VISIBLE);
				mPoesiaController.comentar(mPoesia, comentarioString, new OnRequestListener(ComentarioActivity.this) {

					@Override
					public void onSuccess(Object result) {
						Comentario comentario = (Comentario) result;
						listComentarios.add(comentario);
						Collections.sort(listComentarios);
						listAdapter.notifyDataSetChanged();
						loading.setVisibility(View.GONE);
						editText.setText("");
					}

					@Override
					public void onError(String errorMessage) {
						System.out.println(errorMessage);
						loading.setVisibility(View.GONE);
						new AlertDialog.Builder(ComentarioActivity.this)
							.setTitle("Um erro ocorreu")
							.setMessage(errorMessage)
							.setNeutralButton("OK", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									loading.setVisibility(View.GONE);
								}
							})
							.create().show();

					}
				});
			}
		});

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
