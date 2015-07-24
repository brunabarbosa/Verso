package com.projetoles.controller;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.projetoles.dao.CurtidaDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Curtida;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

public class CurtidaController extends Controller {

	private static CurtidaDAO sDao = CurtidaDAO.getInstance();
	
	public CurtidaController(Activity context) {
		super(context);
	}
	
	public void curtir(final Poesia poesia, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				final Curtida curtida = new Curtida(null, UsuarioController.usuarioLogado, Calendar.getInstance());
				sDao.curtir(poesia, curtida, new OnRequestListener(callback.getContext()) {
					
					@Override 
					public void onSuccess(Object result) {
						try {
							JSONObject json = new JSONObject(result.toString());
							boolean success = json.getBoolean("success");
							if (success) {
								curtida.setId(json.getString("id"));
								callback.onSuccess(curtida);
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

	public void descurtir(Poesia poesia, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				sDao.descurtir(UsuarioController.usuarioLogado, poesia, new OnRequestListener(callback.getContext()) {
		            @Override
		            public void onSuccess(Object result) {
		                try {
		                    JSONObject json = new JSONObject(result.toString());
		                    boolean success = json.getBoolean("success");
		                    if (success) {
		                    	callback.onSuccess(json.getString("id"));
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
	
	public void getCurtida(final String id, final OnRequestListener callback) {
		sDao.getCurtida(id, new OnRequestListener(callback.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				try {
					final JSONObject json = new JSONObject(result.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						final String postador = json.getString("postador");
						if (UsuariosCarregados.temUsuario(postador)) {
							Curtida curtida = Curtida.converteJson(UsuariosCarregados.getUsuario(postador), json);
							callback.onSuccess(curtida);
						} else {
							UsuarioController controller = new UsuarioController(callback.getContext());
							controller.getUsuario(postador, new OnRequestListener(callback.getContext()) {
								
								@Override
								public void onSuccess(Object result) {
									Usuario usuario = (Usuario) result;
									UsuariosCarregados.salvaUsuario(usuario);
									try {
										Curtida curtida = Curtida.converteJson(usuario, json);
										callback.onSuccess(curtida);
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
