package com.projetoles.controller;

import java.util.Calendar;

import com.projetoles.dao.CompartilhamentoDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Compartilhamento;
import com.projetoles.model.Notificacao;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

import android.content.Context;

public class CompartilhamentoController extends Controller<Compartilhamento> {

	private static ObjectLoader<Compartilhamento> sLoader = new ObjectLoader<Compartilhamento>();
	private NotificacaoController mNotificacao;
	
	public CompartilhamentoController(Context context) {
		super(context);
		mDAO = new CompartilhamentoDAO();
		mLoader = sLoader;
		mNotificacao = new NotificacaoController(context);
	}
   
	public void get(String id, OnRequestListener<Compartilhamento> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("poster", new UsuarioController(callback.getContext()));
		dependencies.addDependency("poetry", new PoesiaController(callback.getContext()));
		super.get(id, callback, dependencies);
	}
  
	public void post(final Usuario postador, final Poesia poesia, final OnRequestListener<Compartilhamento> callback) {
		try {
			Compartilhamento compartilhamento = new Compartilhamento(null, Calendar.getInstance(), postador, poesia);
			super.post(compartilhamento, new OnRequestListener<Compartilhamento>(callback.getContext()) {

				@Override
				public void onSuccess(final Compartilhamento compartilhamentoResult) {
					mNotificacao.post(new Notificacao(null, Calendar.getInstance(), poesia.getPostador(), postador, " compartilhou sua poesia.", poesia, "poesia"), 
							new OnRequestListener<Notificacao>(callback.getContext()) {

						@Override
						public void onSuccess(Notificacao result) {
							poesia.getPostador().getNotificacoes().add(result.getId(), result.getDataCriacao().getTimeInMillis());
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
					callback.onSuccess(compartilhamentoResult);
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
			e.printStackTrace();
			callback.onError(e.getMessage());
		}
	}

	@Override
	public void update(Compartilhamento object, OnRequestListener<Compartilhamento> callback) {
		callback.onSuccess(object);
	} 

}
