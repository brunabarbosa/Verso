package com.projetoles.verso;

import java.util.Calendar;

import com.projetoles.controller.NotificacaoController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class NotificacaoLikeActivity extends Activity {
	

	private UsuarioController mUsuarioController;
	private NotificacaoController mNotificacaoController;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_group);
		
		mUsuarioController = new UsuarioController(this);
		mNotificacaoController = new NotificacaoController(this);
		
		final RelativeLayout loading = (RelativeLayout) MainActivity.sInstance.findViewById(R.id.mainLoading);
		Button buttonLike = (Button) findViewById(R.id.facebookIcon);

		buttonLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String titulo = UsuarioController.usuarioLogado.getNome(); 
				String mensagem = titulo + "curtiu sua poesia.";
				Calendar dataDeCriacao = Calendar.getInstance();

				// verificar se vai precisar
				loading.setVisibility(View.VISIBLE);
				mNotificacaoController.criaNotificacao(titulo, mensagem,
						mUsuarioController.usuarioLogado.getEmail(),
						dataDeCriacao, new OnRequestListener(
								NotificacaoLikeActivity.this) {

							@Override
							public void onSuccess(Object result) {
								mUsuarioController.usuarioLogado
										.addPoemaCarregado((Poesia) result);
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										Intent i = new Intent(
												NotificacaoLikeActivity.this,
												MainActivity.class);
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
										new AlertDialog.Builder(
												NotificacaoLikeActivity.this)
												.setTitle("Um erro ocorreu")
												.setMessage(errorMessage)
												.setNeutralButton(
														"OK",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																loading.setVisibility(View.GONE);
															}
														}).create().show();
									}
								});
							}
						});
			}
		});
	}
}