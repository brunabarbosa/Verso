package com.projetoles.verso;

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class ConfiguracoesActivity extends Activity {

	private UsuarioController mController;
	private CheckBox mHabilitado;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracoes);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mController = new UsuarioController(this);
		
		mHabilitado = (CheckBox) findViewById(R.id.notificacoes_habilitado);
		mHabilitado.setChecked(UsuarioController.usuarioLogado.getNotificacoesHabilitadas());
		mHabilitado.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
				mController.setNotificacoesHabilitadas(isChecked, new OnRequestListener<Void>(ConfiguracoesActivity.this) {
					
					@Override
					public void onSuccess(Void result) {
						UsuarioController.usuarioLogado.setNotificacoesHabilitadas(isChecked);
					}
					
					@Override
					public void onError(String errorMessage) {
						Toast.makeText(ConfiguracoesActivity.this, errorMessage, Toast.LENGTH_LONG).show();
					}
				});
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
		// getMenuInflater().inflate(R.menu.main, menu);
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
