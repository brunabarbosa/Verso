package com.projetoles.verso;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.projetoles.controller.PoesiaController;
import com.projetoles.dao.OnRequestListener;

public class PesquisarActivity extends Activity {

	private PoesiaController mController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pesquisa_poesia);
		
		mController = new PoesiaController(this);
		
		ImageView pesquisar = (ImageView) MainActivity.sInstance.findViewById(R.id.btnPesquisar);
		final EditText etTitulo = (EditText) findViewById(R.id.buscaPoemaTitulo);
		final EditText etAutor = (EditText) findViewById(R.id.buscaAutor);
		final EditText etTag = (EditText) findViewById(R.id.buscaTag);
		final EditText etTrecho = (EditText) findViewById(R.id.buscaTrecho);
		pesquisar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String titulo = etTitulo.getText().toString();
				String autor = etAutor.getText().toString();
				String tag = etTag.getText().toString();
				String trecho = etTrecho.getText().toString();
				if (titulo.trim().isEmpty() && autor.trim().isEmpty() && tag.trim().isEmpty() && trecho.trim().isEmpty()) {
					ActivityUtils.showMessageDialog(PesquisarActivity.this, "Um erro ocorreu", "É preciso informar pelo menos um campo.", null);
				} else {
					mController.pesquisar(titulo, autor, tag, trecho, new OnRequestListener<ArrayList<String>>(PesquisarActivity.this) {
						
						@Override
						public void onSuccess(ArrayList<String> resultados) {
							Intent i = new Intent(PesquisarActivity.this, ResultadoPesquisaActivity.class);
							i.putStringArrayListExtra("resultados", resultados);
							startActivity(i);
						}
						
						@Override
						public void onError(String errorMessage) {
							ActivityUtils.showMessageDialog(PesquisarActivity.this, "Um erro ocorreu", errorMessage, null);
						}
					});
				}
			}
		});
	}
}
