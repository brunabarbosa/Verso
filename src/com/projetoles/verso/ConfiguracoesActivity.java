package com.projetoles.verso;

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.os.Bundle;
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

}
