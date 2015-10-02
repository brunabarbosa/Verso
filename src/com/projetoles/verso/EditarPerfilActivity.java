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
	
	private Class mCallback;
	private UsuarioController mController;
	private EditText etNome;
	private EditText etBiografia;
	private EditText etSenha;
	private EditText etEditarSenha;
	private Button btnSalvarPerfil;
	private View mLoading;
	private CameraActivityBundle mCameraBundle;

	private void salvarPerfil(Usuario usuario) {
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
						ActivityUtils.showMessageDialog(EditarPerfilActivity.this, "Sucesso!", "Usuário alterado com sucesso.", mLoading);
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
	
	private void preencheCampos(Usuario usuario) {
		etNome.setText(usuario.getNome());
		etBiografia.setText(usuario.getBiografia());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editar_perfil);
		FacebookSdk.sdkInitialize(this);   
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = getIntent().getExtras();
		mCallback = (Class) b.get("callback");
		
		mController = new UsuarioController(this);
		
		final ImageView foto = (ImageView) findViewById(R.id.showPhoto);
		final ImageView fotoFull = (ImageView) findViewById(R.id.editarFotoFull);
		final View mProfilePhotoContent = findViewById(R.id.editarFotoContent);
		final Button btnEditarFoto = (Button) findViewById(R.id.btnEditPhoto);
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
		
		mController.getUsuarioLogado(new OnRequestListener<Usuario>(this) {

			@Override
			public void onSuccess(Usuario result) {
				mCameraBundle = new CameraActivityBundle(EditarPerfilActivity.this, result, foto, fotoFull, mProfilePhotoContent);
				mCameraBundle.setFoto(result.getFoto());
				mCameraBundle.editarFoto(btnEditarFoto);
				preencheCampos(result);
				salvarPerfil(result);
			}

			@Override
			public void onError(String errorMessage) {
				System.out.println(errorMessage);
			}
		}, "");
	
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(EditarPerfilActivity.this, BiografiaActivity.class);
		i.putExtra("usuario", UsuarioController.usuarioLogado);
		i.putExtra("callback", mCallback);
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
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
	    new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (mCameraBundle == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mCameraBundle.onActivityResult(requestCode, resultCode, imageReturnedIntent, mLoading);
					}
				});
			}
		}).start();
	}
	
}
