package com.projetoles.verso;

import com.projetoles.controller.NotificacaoController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Notificacao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class CriaNotificacoesActivity extends Activity {

	private UsuarioController mUsuarioController;
	private NotificacaoController mNotificacaoController;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_group);

		mUsuarioController = new UsuarioController(this);
		mNotificacaoController = new NotificacaoController(this);

		final RelativeLayout loading = (RelativeLayout) MainActivity.sInstance
				.findViewById(R.id.mainLoading);

		for (Notificacao n : mUsuarioController.usuarioLogado
				.getNotificacaoesCarregadas()) {
			loading.setVisibility(View.VISIBLE);

			mNotificacaoController.criaNotificacao(n.getEnderecado(),
					n.getTitulo(), n.getMensagem(), n.getDataDeCriacao(),
					new OnRequestListener(CriaNotificacoesActivity.this) {

						@Override
						public void onSuccess(Object result) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Intent i = new Intent(
											CriaNotificacoesActivity.this,
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
											CriaNotificacoesActivity.this)
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

	}
}