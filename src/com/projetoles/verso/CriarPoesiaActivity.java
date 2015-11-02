package com.projetoles.verso;

import java.util.Calendar;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

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
				mLoading.setVisibility(View.VISIBLE);
				UsuarioController controller = new UsuarioController(CriarPoesiaActivity.this);
				controller.getUsuarioLogado(new OnRequestListener<Usuario>(CriarPoesiaActivity.this) {
 
					@Override
					public void onSuccess(final Usuario usuarioLogado) {
						String titulo = etTitulo.getText().toString(); 
						String autor = etAutor.getText().toString();
						String poesia = etPoesia.getText().toString(); 
						String tags = etTags.getText().toString();
						Calendar dataCriacao = Calendar.getInstance();

						mController.post(titulo, autor, usuarioLogado, poesia, dataCriacao, 
								tags, new OnRequestListener<Poesia>(CriarPoesiaActivity.this) {
							
							@Override
							public void onSuccess(Poesia poesia) {
								usuarioLogado.getPoesias().add(poesia.getId(), poesia.getDataCriacao().getTimeInMillis());
								Intent i = new Intent(CriarPoesiaActivity.this, MainActivity.class);
								startActivity(i);
								finish();
							}
							
							@Override
							public void onError(final String errorMessage) {
								ActivityUtils.showMessageDialog(CriarPoesiaActivity.this, "Um erro ocorreu", errorMessage, mLoading);
							}

							@Override
							public void onTimeout() {
								ActivityUtils.showMessageDialog(CriarPoesiaActivity.this, "Ops", "Ocorreu um erro com a sua requisição. Verifique sua conexão com a internet.", mLoading);
							}
						});
					}

					@Override
					public void onError(String errorMessage) {
						ActivityUtils.showMessageDialog(CriarPoesiaActivity.this, "Um erro ocorreu", errorMessage, mLoading);
					}

					@Override
					public void onTimeout() {
						ActivityUtils.showMessageDialog(CriarPoesiaActivity.this, "Ops", "Ocorreu um erro com a sua requisição. Verifique sua conexão com a internet.", mLoading);
					}
				}, null);
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
