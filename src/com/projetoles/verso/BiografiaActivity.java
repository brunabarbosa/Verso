package com.projetoles.verso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.projetoles.controller.UsuarioController;
import com.projetoles.model.Usuario;

public class BiografiaActivity extends Activity {
	
	private Usuario mUsuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biografia);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle b = getIntent().getExtras();
		mUsuario = (Usuario) b.getParcelable("usuario");
		TextView editBio = (TextView) findViewById(R.id.biografia);
		editBio.setText(mUsuario.getBiografia());
		Button editarPerfil = (Button) findViewById(R.id.btnEditarPerfil);
		if (!mUsuario.equals(UsuarioController.usuarioLogado)) {
			editarPerfil.setVisibility(View.GONE);
		}
		editarPerfil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(BiografiaActivity.this, EditarPerfilActivity.class);
				startActivity(i);
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
