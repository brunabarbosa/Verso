package com.projetoles.controller;

import java.util.Calendar;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.SeguidaDAO;
import com.projetoles.model.Notificacao;
import com.projetoles.model.Poesia;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

import android.content.Context;

public class SeguidaController extends Controller<Seguida> {

	private static ObjectLoader<Seguida> sLoader = new ObjectLoader<Seguida>();
	private NotificacaoController mNotificacao;
	
	public SeguidaController(Context context) {
		super(context);
		mDAO = new SeguidaDAO();
		mLoader = sLoader;
		mNotificacao = new NotificacaoController(context);
	}
   
	public void get(String id, OnRequestListener<Seguida> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("follower", new UsuarioController(callback.getContext()));
		dependencies.addDependency("followed", new UsuarioController(callback.getContext()));
		super.get(id, callback, dependencies);
	}
 
	public void post(final Usuario seguidor, final Usuario seguindo, final OnRequestListener<Seguida> callback) {
		UsuarioController controller = new UsuarioController(callback.getContext());
		controller.getUsuarioLogado(new OnRequestListener<Usuario>(callback.getContext()) {

			@Override
			public void onSuccess(final Usuario usuarioLogado) {
				try {
					Seguida seguida = new Seguida(null, Calendar.getInstance(), seguidor, seguindo);
					SeguidaController.super.post(seguida, new OnRequestListener<Seguida>(callback.getContext()) {

						@Override
						public void onSuccess(final Seguida seguidaResult) {
							if (!seguindo.equals(usuarioLogado)) {
								mNotificacao.post(new Notificacao(null, Calendar.getInstance(), seguindo, seguidor, " seguiu seu perfil.", new Poesia(), "usuario"), 
									new OnRequestListener<Notificacao>(callback.getContext()) {
		 
										@Override
										public void onSuccess(Notificacao result) {
											seguindo.getNotificacoes().add(result.getId(), result.getDataCriacao().getTimeInMillis());
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
							callback.onSuccess(seguidaResult);
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
			public void onError(String errorMessage) {
				callback.onError(errorMessage);
			}

			@Override
			public void onTimeout() {
				callback.onTimeout();
			}
		}, null);
	}

	@Override
	public void update(Seguida object, OnRequestListener<Seguida> callback) {
		callback.onSuccess(object);
	} 

}
