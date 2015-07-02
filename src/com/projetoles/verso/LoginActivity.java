package com.projetoles.verso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;

public class LoginActivity extends Activity {

	private UsuarioController mController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	
		getActionBar().hide();
	
		final RelativeLayout etLoading = (RelativeLayout) findViewById(R.id.loginLoading);
		final EditText etEmail = (EditText) findViewById(R.id.editEmail);
		final EditText etSenha = (EditText) findViewById(R.id.editSenha);
		final Button cadastrar = (Button) findViewById(R.id.btnCadastrar);
		cadastrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(LoginActivity.this, CriarContaActivity.class);
				startActivity(i);
			}
		});
		final Button entrar = (Button) findViewById(R.id.btnEntrar);
		entrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etLoading.setVisibility(View.VISIBLE);
				String email = etEmail.getText().toString();
				String senha = etSenha.getText().toString();
				mController.login(email, senha, new OnRequestListener(LoginActivity.this) {
					
					@Override
					public void onSuccess(Object result) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Intent i = new Intent(LoginActivity.this, MainActivity.class);
								startActivity(i);
								finish();
							}
						});
					}
					
					@Override
					public void onError(final String errorMessage) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								new AlertDialog.Builder(LoginActivity.this)
									.setTitle("Um erro ocorreu")
									.setMessage(errorMessage)
									.setNeutralButton("OK", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											etLoading.setVisibility(View.GONE);
										}
									})
									.create().show();
							}
						});
					}
				});
			}
		});

		mController = new UsuarioController(this);
		mController.getLoggedUser(new OnRequestListener(this) {
			
			@Override
			public void onSuccess(Object result) {
				Intent i = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
			
			@Override
			public void onError(String errorMessage) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						etLoading.setVisibility(View.GONE);
					}
				});
			}
		});
		
	}

}
