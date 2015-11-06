package com.projetoles.verso;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PesquisarActivity extends Activity {

	private UsuarioController mUsuarioController;
	private PoesiaController mPoesiaController;
	private int mMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pesquisa);
		
		mUsuarioController = new UsuarioController(this);
		mPoesiaController = new PoesiaController(this);
		
		ImageView pesquisar = (ImageView) MainActivity.sInstance.findViewById(R.id.btnPesquisar);
		final EditText etTitulo = (EditText) findViewById(R.id.buscaPoemaTitulo);
		final EditText etAutor = (EditText) findViewById(R.id.buscaAutor);
		final EditText etTag = (EditText) findViewById(R.id.buscaTag);
		final EditText etTrecho = (EditText) findViewById(R.id.buscaTrecho);
		final EditText etNome = (EditText) findViewById(R.id.buscaUsuarioNome);
		
		final RadioGroup btSelecionar = (RadioGroup) findViewById(R.id.searchButtons);
		final View poesiaView = findViewById(R.id.poetryView);
		final View usuarioView = findViewById(R.id.userView);
		btSelecionar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.searchByPoetry) {
					usuarioView.setVisibility(View.GONE);
					poesiaView.setVisibility(View.VISIBLE);
					mMode = 0;
				} else if (checkedId == R.id.searchByUser) {
					usuarioView.setVisibility(View.VISIBLE);
					poesiaView.setVisibility(View.GONE);
					mMode = 1;
				}
			}
		});
		
		pesquisar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (mMode == 0) {
					String titulo = etTitulo.getText().toString();
					String autor = etAutor.getText().toString();
					String tag = etTag.getText().toString();
					String trecho = etTrecho.getText().toString();
					((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.VISIBLE);
					if (titulo.trim().isEmpty() && autor.trim().isEmpty() && tag.trim().isEmpty() && trecho.trim().isEmpty()) {
						ActivityUtils.showMessageDialog(PesquisarActivity.this, "Um erro ocorreu", "É preciso informar pelo menos um campo.", null);
						((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
					} else {
						mPoesiaController.pesquisar(titulo, autor, tag, trecho, new OnRequestListener<ObjectListID<Poesia>>(PesquisarActivity.this) {
							
							@Override
							public void onSuccess(ObjectListID<Poesia> resultados) {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								Intent i = new Intent(PesquisarActivity.this, ResultadoPesquisaActivity.class);
								i.putExtra("resultados", resultados);
								startActivity(i);
							}
							
							@Override
							public void onError(String errorMessage) {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								ActivityUtils.showMessageDialog(PesquisarActivity.this, "Um erro ocorreu", errorMessage, null);
							}

							@Override
							public void onTimeout() {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								ActivityUtils.showMessageDialog(PesquisarActivity.this, "Ops", "Ocorreu um erro com a sua requisição. Verifique sua conexão com a internet.", null);
							}
						});
					}
				} else {
					String nome = etNome.getText().toString();
					((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.VISIBLE);
					if (nome.trim().isEmpty()) {
						ActivityUtils.showMessageDialog(PesquisarActivity.this, "Um erro ocorreu", "É preciso informar o nome do usuário.", null);
						((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
					} else {
						mUsuarioController.pesquisar(nome, new OnRequestListener<ObjectListID<Usuario>>(PesquisarActivity.this) {
							
							@Override
							public void onSuccess(ObjectListID<Usuario> resultados) {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								Intent i = new Intent(PesquisarActivity.this, ResultadoPesquisaUsuarioActivity.class);
								i.putExtra("resultados", resultados);
								startActivity(i);
							}
							
							@Override
							public void onError(String errorMessage) {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								ActivityUtils.showMessageDialog(PesquisarActivity.this, "Um erro ocorreu", errorMessage, null);
							}

							@Override
							public void onTimeout() {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								ActivityUtils.showMessageDialog(PesquisarActivity.this, "Ops", "Ocorreu um erro com a sua requisição. Verifique sua conexão com a internet.", null);
							}
						});
					}
				}
			}
		});
	}
}
