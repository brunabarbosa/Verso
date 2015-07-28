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
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Usuario;

public class EditarPerfilActivity extends Activity {
	
	private UsuarioController mController;
	private EditText etNome;
	private EditText etBiografia;
	private EditText etSenha;
	private EditText etEditarSenha;
	private Button btnSalvarPerfil;
	private View mLoading;
	private CameraActivityBundle mCameraBundle;

	private void salvarPerfil() {
		btnSalvarPerfil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mLoading.setVisibility(View.VISIBLE);
				String nome = etNome.getText().toString();
				String biografia = etBiografia.getText().toString();
				String senha = etSenha.getText().toString();
				String repetirSenha = etEditarSenha.getText().toString();
				mController.put(nome, biografia, senha, repetirSenha, new OnRequestListener<Usuario>(EditarPerfilActivity.this) {
					 
					@Override
					public void onSuccess(Usuario usuario) {
						ActivityUtils.showMessageDialog(EditarPerfilActivity.this, "Successo!", "Usuário alterado com sucesso.", mLoading);
					}
					
					@Override
					public void onError(String errorMessage) {
						System.out.println(errorMessage);
						ActivityUtils.showMessageDialog(EditarPerfilActivity.this, "Um erro ocorreu", errorMessage, mLoading);
					}
				});
			
			}
		});
	}
	
	private void preencheCampos() {
		etNome.setText(UsuarioController.usuarioLogado.getNome());
		etBiografia.setText(UsuarioController.usuarioLogado.getBiografia());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editar_perfil);
		FacebookSdk.sdkInitialize(this);   
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mController = new UsuarioController(this);
		
		ImageView foto = (ImageView) findViewById(R.id.showPhoto);
		ImageView fotoFull = (ImageView) findViewById(R.id.editarFotoFull);
		View mProfilePhotoContent = findViewById(R.id.editarFotoContent);
		Button btnEditarFoto = (Button) findViewById(R.id.btnEditPhoto);
		mLoading = findViewById(R.id.editarPerfilLoading);
		mLoading.setVisibility(View.GONE);
		
		etNome = (EditText) findViewById(R.id.etEditName);
		etBiografia = (EditText) findViewById(R.id.etEditBio);
		etSenha = (EditText) findViewById(R.id.etEditPassword);
		etEditarSenha = (EditText) findViewById(R.id.etEditPasswordAgain);
		btnSalvarPerfil = (Button) findViewById(R.id.btnSalvarPerfil);
		// Para evitar usuarios do Facebook alterarem senha
		if (AccessToken.getCurrentAccessToken() != null) {
			etSenha.setVisibility(View.GONE);
			etEditarSenha.setVisibility(View.GONE);
		}
		
		mCameraBundle = new CameraActivityBundle(this, foto, fotoFull, mProfilePhotoContent);
		mCameraBundle.setFoto(UsuarioController.usuarioLogado.getFoto());
		mCameraBundle.editarFoto(btnEditarFoto);
		
		preencheCampos();
		
		salvarPerfil();
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(EditarPerfilActivity.this, BiografiaActivity.class);
		i.putExtra("usuario", UsuarioController.usuarioLogado);
		i.putExtra("callback", MainActivity.class);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
	    mCameraBundle. onActivityResult(requestCode, resultCode, imageReturnedIntent, mLoading);
	}
	
}
