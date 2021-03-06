package com.projetoles.verso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Usuario;

public class BiografiaActivity extends Activity {
	 
	private Class mCallback;
	private Usuario mUsuario;
	private Button btnEditarPerfil;
	
	private void editarPerfil() {
		btnEditarPerfil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(BiografiaActivity.this, EditarPerfilActivity.class);
				i.putExtra("callback", mCallback);
				i.putExtra("usuario", mUsuario);
				startActivity(i);
				finish();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biografia);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		 
		Bundle b = getIntent().getExtras();
		mCallback = (Class) b.get("callback");
		mUsuario = (Usuario) b.getParcelable("usuario");
		TextView editBio = (TextView) findViewById(R.id.biografia);
		if (mUsuario.getBiografia() == null || mUsuario.getBiografia().trim().equals("")) {
			editBio.setText("Este usu�rio n�o tem biografia.");
		} else {
			editBio.setText(mUsuario.getBiografia());
		}
		ImageView fotoFull = (ImageView) findViewById(R.id.biografiaFullPhoto);
 		ImageView fotoPreview = (ImageView) findViewById(R.id.biografiaFoto);
 		View loading = (View) findViewById(R.id.biografiaPhotoContent);
 		
 		CameraActivityBundle cameraBundle = new CameraActivityBundle(this, mUsuario, fotoPreview, fotoFull, loading);
 		cameraBundle.setFoto(mUsuario.getFoto());
 		
		btnEditarPerfil = (Button) findViewById(R.id.btnEditarPerfil);
		btnEditarPerfil.setVisibility(View.GONE);
		UsuarioController usuarioController = new UsuarioController(this);
		usuarioController.getUsuarioLogado(new OnRequestListener<Usuario>(this) {
			
			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(Usuario usuarioLogado) {
				if (mUsuario.equals(usuarioLogado)) {
					btnEditarPerfil.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		}, null);
		
		editarPerfil();
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(BiografiaActivity.this, mCallback);
		i.putExtra("usuario", mUsuario);
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
