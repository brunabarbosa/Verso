package com.projetoles.verso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Usuario;

public class CriarContaActivity extends Activity {

	private UsuarioController mController;
	private EditText etNome;
	private EditText etEmail;
	private EditText etSenha;
	private EditText etConfirmaSenha;
	private Button mBtnRegistrar;
	private View mLoading;
	
	private void registrarUsuario() {
		mBtnRegistrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email = etEmail.getText().toString(); 
				String nome = etNome.getText().toString();
				String senha = etSenha.getText().toString(); 
				String repetirSenha = etConfirmaSenha.getText().toString();
				mLoading.setVisibility(View.VISIBLE);
				mController.post(nome, email, senha, repetirSenha, new OnRequestListener<Usuario>(CriarContaActivity.this) {
					 
					@Override
					public void onSuccess(Usuario result) {
						Intent intent = new Intent(CriarContaActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
						finish();
					}
					
					@Override
					public void onError(final String errorMessage) {
						System.out.println(errorMessage);
						ActivityUtils.showMessageDialog(CriarContaActivity.this, "Um erro ocorreu", errorMessage, mLoading);
					}
				});
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cria_conta);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	
		mController = new UsuarioController(this);
		mLoading = (View) findViewById(R.id.cadastrarLoading);
		etNome = (EditText) findViewById(R.id.editNomedoUsuario);
		etEmail = (EditText) findViewById(R.id.editEmaildoUsuario);
		etSenha = (EditText) findViewById(R.id.editSenhaUsuario);
		etConfirmaSenha = (EditText) findViewById(R.id.editConfirmaSenha);
		mBtnRegistrar = (Button) findViewById(R.id.btnCriaConta);
		
		registrarUsuario();
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
