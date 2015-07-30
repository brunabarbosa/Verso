package com.projetoles.controller;

import java.util.Calendar;

import android.app.Activity;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.SeguidaDAO;
import com.projetoles.model.Notificacao;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

public class SeguidaController extends Controller<Seguida> {

	private static ObjectLoader<Seguida> sLoader = new ObjectLoader<Seguida>();
	private NotificacaoController mNotificacao;
	
	public SeguidaController(Activity context) {
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
		try {
			Seguida seguida = new Seguida(null, Calendar.getInstance(), seguidor, seguindo);
			super.post(seguida, new OnRequestListener<Seguida>(callback.getContext()) {

				@Override
				public void onSuccess(Seguida result) {
					if (!seguidor.equals(UsuarioController.usuarioLogado)) {
						mNotificacao.post(new Notificacao(null, Calendar.getInstance(), 
						seguidor, seguindo, " seguiu seu perfil."), 
							new OnRequestListener<Notificacao>(callback.getContext()) {
 
								@Override
								public void onSuccess(Notificacao result) {
									seguindo.getNotificacoes().add(result.getId());
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
	public void update(Seguida object, OnRequestListener<Seguida> callback) {
		callback.onSuccess(object);
	} 

}