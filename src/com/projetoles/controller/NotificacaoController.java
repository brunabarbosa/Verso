package com.projetoles.controller;

import java.util.Calendar;

import android.app.Activity;

import com.projetoles.dao.NotificacaoDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Notificacao;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

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
	
	public void post(Usuario enderecado, Usuario titulo, String mensagem, Poesia poesia, String tipo,
			OnRequestListener<Notificacao> callback) {
		try {
			Notificacao c = new Notificacao(null, Calendar.getInstance(), enderecado, titulo, mensagem, poesia, tipo);
			super.post(c, callback); 
		} catch (Exception e) {
			callback.onError(e.getMessage());
		}
	}
 
	public void get(String id, OnRequestListener<Notificacao> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("enderecado", new UsuarioController(callback.getContext()));
		dependencies.addDependency("titulo", new UsuarioController(callback.getContext()));
		dependencies.addDependency("poesia", new PoesiaController(callback.getContext()));
		super.get(id, callback, dependencies);
	}

}
