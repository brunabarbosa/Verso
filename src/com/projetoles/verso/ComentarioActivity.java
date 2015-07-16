package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comentario);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
				
		Bundle b = getIntent().getExtras();
		mPoesia = (Poesia) b.getParcelable("poesia");
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
			listComentarios.add(new Comentario(comentario, mPoesia.getAutor(), mPoesia.getDataDeCriacao()));
		}
		
		Collections.sort(listComentarios);
		listAdapter.notifyDataSetChanged();
		
		
		final Button button = (Button) findViewById(R.id.sendComentario);
		
		final EditText editText = (EditText) findViewById(R.id.novoComentario);
		final String comentarioString = editText.getText().toString();
		final Comentario comentario = new Comentario(comentarioString, mPoesia.getAutor(), mPoesia.getDataDeCriacao());
		
		
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               mPoesiaController.criarComentario(mPoesia, comentario, new OnRequestListener(ComentarioActivity.this) {
				
				@Override
				public void onSuccess(Object result) {
					mPoesia.addComentario(comentario.getComentario());
					listComentarios.add(comentario);
					
					Collections.sort(listComentarios);
					listAdapter.notifyDataSetChanged();
					
				}
				
				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
					
				}
			});
            }
        });
		
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
