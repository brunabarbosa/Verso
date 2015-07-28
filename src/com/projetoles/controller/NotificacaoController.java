package com.projetoles.controller;

import android.app.Activity;

import com.projetoles.dao.NotificacaoDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Notificacao;

public class NotificacaoController extends Controller<Notificacao> {
	
	private static ObjectLoader<Notificacao> sLoader = new ObjectLoader<Notificacao>();
	
	public NotificacaoController(Activity context) {
		super(context);
		mDAO = new NotificacaoDAO();
		mLoader = sLoader;
	}

	@Override
	public void update(Notificacao object,
			OnRequestListener<Notificacao> callback) {
		callback.onSuccess(object);
	} 

}
