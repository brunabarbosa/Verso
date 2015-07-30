package com.projetoles.controller;

import java.util.Calendar;

import android.app.Activity;

import com.projetoles.dao.ComentarioDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Comentario;
import com.projetoles.model.Notificacao;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

public class ComentarioController extends Controller<Comentario> {

	private static ObjectLoader<Comentario> sLoader = new ObjectLoader<Comentario>();
	private NotificacaoController mNotificacao;
	
	public ComentarioController(Activity context) {
		super(context);
		mDAO = new ComentarioDAO();
		mLoader = sLoader;
		mNotificacao = new NotificacaoController(context);
	}

	public void post(final Usuario postador, final Poesia poesia, String comentario,
			final OnRequestListener<Comentario> callback) {
		try {
			Comentario c = new Comentario(null, Calendar.getInstance(), comentario, postador, poesia);
			super.post(c, new OnRequestListener<Comentario>(callback.getContext()) {

				@Override
				public void onSuccess(final Comentario comentarioResult) {
					if (!postador.equals(poesia.getPostador())) {
						mNotificacao.post(new Notificacao(null, Calendar.getInstance(), poesia.getPostador(), postador, " comentou sua poesia."), 
							new OnRequestListener<Notificacao>(callback.getContext()) {
 
							@Override
							public void onSuccess(Notificacao result) {
								poesia.getPostador().getNotificacoes().add(result.getId());
								callback.onSuccess(comentarioResult);
							}

							@Override
							public void onError(String errorMessage) {
								System.out.println(errorMessage);
							}
						});
					}
					callback.onSuccess(comentarioResult);
				}

				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
				}
			}); 
		} catch (Exception e) {
			callback.onError(e.getMessage());
		}
	}
 
	public void get(String id, OnRequestListener<Comentario> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("poster", new UsuarioController(callback.getContext()));
		dependencies.addDependency("poetry", new PoesiaController(callback.getContext()));
		super.get(id, callback, dependencies);
	}

	@Override
	public void update(Comentario object, OnRequestListener<Comentario> callback) {
		callback.onSuccess(object);
	}
	
}
