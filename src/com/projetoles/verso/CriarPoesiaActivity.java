package com.projetoles.verso;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;

public class CriarPoesiaActivity extends Activity {
	
	private PoesiaController mPoemaController;
	private UsuarioController mUsuarioController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cria_poesia);
		
		mPoemaController = new PoesiaController(this);
		mUsuarioController = new UsuarioController(this);
		//verificar se vai precisar
		final RelativeLayout loading = (RelativeLayout) MainActivity.sInstance.findViewById(R.id.mainLoading);
		final EditText etTitulo = (EditText) findViewById(R.id.poemaTitulo);
		final EditText etPoesia = (EditText) findViewById(R.id.poema);
		final EditText etTags = (EditText) findViewById(R.id.poemaTags);
		final ImageView criar = (ImageView) MainActivity.sInstance.findViewById(R.id.btnCriarPoema);
		criar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String titulo = etTitulo.getText().toString(); 
				String poesia = etPoesia.getText().toString(); 
				String tags = etTags.getText().toString();
				Calendar dataDeCriacao = Calendar.getInstance();
				
				//verificar se vai precisar
				loading.setVisibility(View.VISIBLE);
				mPoemaController.criarPoesia(titulo, mUsuarioController.usuarioLogado.getEmail(), 
						poesia, dataDeCriacao, tags, new OnRequestListener(CriarPoesiaActivity.this) {
					
					@Override
					public void onSuccess(Object result) {
						mUsuarioController.usuarioLogado.addPoemaCarregado((Poesia)result);
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Intent i = new Intent(CriarPoesiaActivity.this, MainActivity.class);
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
								new AlertDialog.Builder(CriarPoesiaActivity.this)
									.setTitle("Um erro ocorreu")
									.setMessage(errorMessage)
									.setNeutralButton("OK", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											loading.setVisibility(View.GONE);
										}
									})
									.create().show();
							}
						});
					}
				});
			}
		});
	}

}
