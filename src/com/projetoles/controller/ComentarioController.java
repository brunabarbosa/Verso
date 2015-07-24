package com.projetoles.controller;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.projetoles.dao.ComentarioDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Comentario;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

public class ComentarioController extends Controller {

	private static ComentarioDAO sDao = ComentarioDAO.getInstance();
	
	public ComentarioController(Activity context) {
		super(context);
	}
	
	public void comentar(final Poesia poesia, final String stringComentario, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				final Comentario comentario = new Comentario(stringComentario, UsuarioController.usuarioLogado, Calendar.getInstance());
				sDao.comentar(poesia, comentario, new OnRequestListener(callback.getContext()) {
					@Override
					public void onSuccess(Object result) {
						try {
							JSONObject json = new JSONObject(result.toString());
							boolean success = json.getBoolean("success");
							if (success) {
								callback.onSuccess(comentario);
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
		} else {
			callback.onError("Usuário não encontrado.");
		}
	}

	public void getComentario(final String id, final OnRequestListener callback) {
		sDao.getComentario(id, new OnRequestListener(callback.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				try {
					final JSONObject json = new JSONObject(result.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						final String postador = json.getString("postador");
						if (UsuariosCarregados.temUsuario(postador)) {
							Comentario comentario = Comentario.converteJson(UsuariosCarregados.getUsuario(postador), json);
							callback.onSuccess(comentario);
						} else {
							UsuarioController controller = new UsuarioController(callback.getContext());
							controller.getUsuario(postador, new OnRequestListener(callback.getContext()) {
								
								@Override
								public void onSuccess(Object result) {
									Usuario usuario = (Usuario) result;
									UsuariosCarregados.salvaUsuario(usuario);
									Comentario comentario;
									try {
										comentario = Comentario.converteJson(usuario, json);
										callback.onSuccess(comentario);
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
						}
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
	}
	
}
