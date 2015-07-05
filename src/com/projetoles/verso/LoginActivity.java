package com.projetoles.verso;

import java.util.Arrays;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.*;
import com.facebook.login.widget.LoginButton;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;

public class LoginActivity extends Activity {

	private UsuarioController mController;
	
	private LoginButton buttonFacebook;  
    //permissões que usaremos para recuperar dados do usuário
	private List<String> facebookPermitions;  
	//responsável por gerenciar as ações em suas aplicações após o retorno das chamadas ao FacebookSDK
    private CallbackManager callbackManager;  
    

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		FacebookSdk.sdkInitialize(this);   
		setContentView(R.layout.activity_login);
	
		setContentView(R.layout.activity_login);  
		callbackManager = CallbackManager.Factory.create(); 
		
		facebookPermitions = Arrays.asList("email", "public_profile", "user_friends"); 
		   
		buttonFacebook = (LoginButton)findViewById(R.id.login_button);
		buttonFacebook.setReadPermissions(facebookPermitions); 
		
		buttonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {            
	          
	        public void onSuccess(LoginResult arg0) {  
	             Toast.makeText(getBaseContext(), "SUCESSO!", Toast.LENGTH_LONG).show();  
	        }  
	                  
	        public void onError(FacebookException arg0) {  
	             Toast.makeText(getBaseContext(), "ERROR!", Toast.LENGTH_LONG).show();  
	        }  
	                  
	         
	        public void onCancel() {  
	             Toast.makeText(getBaseContext(), "CANCEL!", Toast.LENGTH_LONG).show();  
	        }  
	    });  
		
		
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
