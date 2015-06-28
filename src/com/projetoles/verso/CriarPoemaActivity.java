package com.projetoles.verso;

import java.util.Calendar;

import com.projetoles.controller.PoemaController;
import com.projetoles.dao.OnRequestListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class CriarPoemaActivity extends Activity {
	
	private PoemaController mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cria_poema);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mController = new PoemaController(this);
		//verificar se vai precisar
		final RelativeLayout loading = (RelativeLayout) findViewById(R.id.cadastrarLoading);
		final EditText etTitulo = (EditText) findViewById(R.id.poemaTitulo);
		final EditText etAutor = (EditText) findViewById(R.id.poemaAutor);
		final EditText etPoesia = (EditText) findViewById(R.id.poema);
		final EditText etTags = (EditText) findViewById(R.id.poemaTags);
		/*final Button criar = (Button) findViewById(R.id.btnCriaConta);
		criar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String titulo = etTitulo.getText().toString(); 
				String autor = etAutor.getText().toString();
				String poesia = etPoesia.getText().toString(); 
				String tags = etTags.getText().toString();
				Calendar dataDeCriacao = Calendar.getInstance();
				
				//verificar se vai precisar
				loading.setVisibility(View.VISIBLE);
				mController.CriarPoema(titulo, autor, poesia, dataDeCriacao, tags, new OnRequestListener(CriarPoemaActivity.this) {
					
					@Override
					public void onSuccess(Object result) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Intent i = new Intent(CriarPoemaActivity.this, MainActivity.class);
								startActivity(i);
								finish();
								finish();
							}
						});
					}
					
					@Override
					public void onError(final String errorMessage) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								new AlertDialog.Builder(CriarPoemaActivity.this)
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
		});*/
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
