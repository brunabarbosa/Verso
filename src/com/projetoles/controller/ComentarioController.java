package com.projetoles.controller;

import java.util.Calendar;

import com.projetoles.dao.ComentarioDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Comentario;
import com.projetoles.model.Notificacao;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

import android.content.Context;

public class ComentarioController extends Controller<Comentario> {

	private static ObjectLoader<Comentario> sLoader = new ObjectLoader<Comentario>();
	private NotificacaoController mNotificacao;
	
	public ComentarioController(Context context) {
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
						mNotificacao.post(new Notificacao(null, Calendar.getInstance(), poesia.getPostador(), postador, " comentou a poesia " + poesia.getTitulo(), poesia, "poesia"), 
							new OnRequestListener<Notificacao>(callback.getContext()) {
  
							@Override
							public void onSuccess(Notificacao result) {
								poesia.getPostador().getNotificacoes().add(result.getId(), result.getDataCriacao().getTimeInMillis());
								callback.onSuccess(comentarioResult);
							}

							@Override
							public void onError(String errorMessage) {
								System.out.println(errorMessage);
							}

							@Override
							public void onTimeout() {
								System.out.println("TIMEOUT");
							}
						});
					}
					callback.onSuccess(comentarioResult);
				}

				@Override
				public void onError(String errorMessage) {
					callback.onError(errorMessage);
				}

				@Override
				public void onTimeout() {
					callback.onTimeout();
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
