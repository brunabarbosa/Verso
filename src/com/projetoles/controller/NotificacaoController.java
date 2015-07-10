package com.projetoles.controller;

import android.app.Activity;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.dao.NotificacaoDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Notificacao;

public class NotificacaoController extends Controller{
	
	private static NotificacaoDAO nDao = NotificacaoDAO.getInstance();

	public NotificacaoController(Activity context) {
		super(context);
	}

	public void criaNotificacao(final String titulo, final String mensagem, final String email,
			final Calendar dataDeCriacao, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				final Notificacao notificacao = new Notificacao(titulo, mensagem, dataDeCriacao);
				nDao.criarNotificacao(notificacao, new OnRequestListener(callback.getContext()) {

					@Override
					public void onSuccess(Object result) {
						try {
							JSONObject json = new JSONObject(result.toString());
							boolean success = json.getBoolean("success");
							if (success) {
								callback.onSuccess(notificacao);
							} else {
								callback.onError(json.getString("message"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
							callback.onError(e.getMessage());
						}
					}

					@Override
					public void onError(String errorMessage) {
						callback.onError(errorMessage);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				callback.onError(e.getMessage());
			}
		}else {
			callback.onError("Usuário não encontrado.");
		}
		
	}

}
