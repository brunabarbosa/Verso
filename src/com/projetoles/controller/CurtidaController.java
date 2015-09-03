package com.projetoles.controller;

import java.util.Calendar;

import android.app.Activity;

import com.projetoles.dao.CurtidaDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Curtida;
import com.projetoles.model.Notificacao;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

public class CurtidaController extends Controller<Curtida> {

	private static ObjectLoader<Curtida> sLoader = new ObjectLoader<Curtida>();
	private NotificacaoController mNotificacao;
	
	public CurtidaController(Activity context) {
		super(context);
		mDAO = new CurtidaDAO();
		mLoader = sLoader;
		mNotificacao = new NotificacaoController(context);
	}
  
	public void get(String id, OnRequestListener<Curtida> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("poster", new UsuarioController(callback.getContext()));
		dependencies.addDependency("poetry", new PoesiaController(callback.getContext()));
		super.get(id, callback, dependencies);
	}

	public void post(final Usuario postador, final Poesia poesia, final OnRequestListener<Curtida> callback) {
		try {
			Curtida curtida = new Curtida(null, Calendar.getInstance(), postador, poesia);
			super.post(curtida, new OnRequestListener<Curtida>(callback.getContext()) {
				
				@Override
				public void onSuccess(Curtida result) {
					if (!postador.equals(poesia.getPostador())) {
						mNotificacao.post(new Notificacao(null, Calendar.getInstance(), 
						poesia.getPostador(), postador, " curtiu a poesia " + poesia.getTitulo()), 
							new OnRequestListener<Notificacao>(callback.getContext()) {
 
								@Override
								public void onSuccess(Notificacao result) {
									poesia.getPostador().getNotificacoes().add(result.getId());
								}

								@Override
								public void onError(String errorMessage) {
									System.out.println(errorMessage);
								}
							});
					}
					callback.onSuccess(result);
					
				}
				
				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		}
	}

	@Override
	public void update(Curtida object, OnRequestListener<Curtida> callback) {
		callback.onSuccess(object);
	}
 
}
