package com.projetoles.verso;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;

public class CriarPoesiaActivity extends Activity {
	
	private PoesiaController mController;
	private EditText etTitulo;
	private EditText etAutor;
	private EditText etPoesia;
	private EditText etTags;
	private ImageView btnCriarPoesia;
	private View mLoading;

	private void criarPoesia() {
		btnCriarPoesia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String titulo = etTitulo.getText().toString(); 
				String autor = etAutor.getText().toString();
				if (autor.trim().isEmpty()) {
					autor = UsuarioController.usuarioLogado.getNome();
				}
				String poesia = etPoesia.getText().toString(); 
				String tags = etTags.getText().toString();
				Calendar dataCriacao = Calendar.getInstance();
				
				mLoading.setVisibility(View.VISIBLE);
				mController.post(titulo, autor, UsuarioController.usuarioLogado, poesia, dataCriacao, 
						tags, new OnRequestListener<Poesia>(CriarPoesiaActivity.this) {
					
					@Override
					public void onSuccess(Poesia poesia) {
						UsuarioController.usuarioLogado.getPoesias().add(poesia.getId(), poesia.getDataCriacao().getTimeInMillis());
						Intent i = new Intent(CriarPoesiaActivity.this, MainActivity.class);
						startActivity(i);
						finish();
					}
					
					@Override
					public void onError(final String errorMessage) {
						ActivityUtils.showMessageDialog(CriarPoesiaActivity.this, "Um erro ocorreu", errorMessage, mLoading);
					}
				});
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cria_poesia);
		
		mController = new PoesiaController(this);
	
		mLoading = MainActivity.sInstance.findViewById(R.id.mainLoading);
		etTitulo = (EditText) findViewById(R.id.poemaTitulo);
		etAutor = (EditText) findViewById(R.id.poemaAutor);
		etPoesia = (EditText) findViewById(R.id.poema);
		etTags = (EditText) findViewById(R.id.poemaTags);
		btnCriarPoesia = (ImageView) MainActivity.sInstance.findViewById(R.id.btnCriarPoema);
		
		criarPoesia();
	}

}
