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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projetoles.controller.UsuarioController;
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
			editBio.setText("Este usuário não tem biografia.");
		} else {
			editBio.setText(mUsuario.getBiografia());
		}
		ImageView fotoFull = (ImageView) findViewById(R.id.biografiaFullPhoto);
 		ImageView fotoPreview = (ImageView) findViewById(R.id.biografiaFoto);
 		View loading = (View) findViewById(R.id.biografiaPhotoContent);
 		
 		CameraActivityBundle cameraBundle = new CameraActivityBundle(this, fotoPreview, fotoFull, loading);
 		cameraBundle.setFoto(mUsuario.getFoto());
 		
		btnEditarPerfil = (Button) findViewById(R.id.btnEditarPerfil);
		if (!mUsuario.equals(UsuarioController.usuarioLogado)) {
			btnEditarPerfil.setVisibility(View.GONE);
		}
		
		editarPerfil();
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(BiografiaActivity.this, mCallback);
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
