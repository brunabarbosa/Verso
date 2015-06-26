package com.projetoles.verso;

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

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;

public class CriarContaActivity extends Activity {

	private UsuarioController mController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cria_conta);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	
		mController = new UsuarioController(this);
		final RelativeLayout loading = (RelativeLayout) findViewById(R.id.cadastrarLoading);
		final EditText etNome = (EditText) findViewById(R.id.editNomedoUsuario);
		final EditText etEmail = (EditText) findViewById(R.id.editEmaildoUsuario);
		final EditText etSenha = (EditText) findViewById(R.id.editSenhaUsuario);
		final EditText etConfirmaSenha = (EditText) findViewById(R.id.editConfirmaSenha);
		final Button criar = (Button) findViewById(R.id.btnCriaConta);
		criar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email = etEmail.getText().toString(); 
				String nome = etNome.getText().toString();
				String senha = etSenha.getText().toString(); 
				String repetirSenha = etConfirmaSenha.getText().toString();
				loading.setVisibility(View.VISIBLE);
				mController.registrar(nome, email, senha, repetirSenha, new OnRequestListener(CriarContaActivity.this) {
					
					@Override
					public void onSuccess(Object result) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Intent i = new Intent(CriarContaActivity.this, MainActivity.class);
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
								new AlertDialog.Builder(CriarContaActivity.this)
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
		});
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
