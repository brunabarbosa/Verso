package com.projetoles.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.projetoles.dao.ComentarioDAO;
import com.projetoles.dao.CurtidaDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.PoesiaDAO;
import com.projetoles.model.Comentario;
import com.projetoles.model.Curtida;
import com.projetoles.model.Poesia;

public class PoesiaController extends Controller {
	
	private static PoesiaDAO pDao = PoesiaDAO.getInstance();
	private static CurtidaDAO cDao = CurtidaDAO.getInstance();
	private static ComentarioDAO commentDao = ComentarioDAO.getInstance();

	public PoesiaController(Activity context) {
		super(context);
	}

	public void getPoesia(final String id, final OnRequestListener callback) {
		pDao.getPoesia(id, new OnRequestListener(callback.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				try {
					JSONObject json = new JSONObject(result.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						Poesia poema = Poesia.converteJson(json);
						callback.onSuccess(poema);
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

	public void pesquisar(final String titulo, final String autor, final String tag,
			final String trecho, final OnRequestListener callback) {
		pDao.pesquisar(titulo, autor, tag, trecho, new OnRequestListener(callback.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				try {
					JSONObject json = new JSONObject(result.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						List<String> ids = new ArrayList<String>();
						JSONArray array = json.getJSONArray("poesias");
						for (int i = 0; i < array.length(); i++) {
							ids.add(array.get(i).toString());
						}
						callback.onSuccess(ids);
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
	
	public void criarPoesia(final String titulo, final String autor, final String postador,
			final String poesia, final Calendar dataDeCriacao,
			final String tags, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				final Poesia poema = new Poesia(null, titulo, postador, autor, poesia, dataDeCriacao, tags);
				pDao.criarPoesia(poema, postador, new OnRequestListener(callback.getContext()) {

					@Override
					public void onSuccess(Object result) {
						try {
							JSONObject json = new JSONObject(result.toString());
							boolean success = json.getBoolean("success");
							if (success) {
								poema.setId(json.getString("id"));
								callback.onSuccess(poema);
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
	
	public void curtir(final Poesia poesia, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				final Curtida curtida = new Curtida(UsuarioController.usuarioLogado.getEmail(), Calendar.getInstance());
				cDao.curtir(poesia, curtida, new OnRequestListener(callback.getContext()) {
					
					@Override
					public void onSuccess(Object result) {
						try {
							JSONObject json = new JSONObject(result.toString());
							boolean success = json.getBoolean("success");
							if (success) {
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
	
	public void getCurtida(final String id, final OnRequestListener callback) {
		cDao.getCurtida(id, new OnRequestListener(callback.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				try {
					JSONObject json = new JSONObject(result.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						Curtida curtida = Curtida.converteJson(json);
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
	}
	
	public void criarComentarioPoesia(final String comentario, final Poesia poesia, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				final Comentario comment = new Comentario(comentario, UsuarioController.usuarioLogado.getEmail(), Calendar.getInstance());
				commentDao.comentar(poesia, comment, new OnRequestListener(callback.getContext()) {
		            @Override
		            public void onSuccess(Object result) {
		                try {
		                    JSONObject json = new JSONObject(result.toString());
		                    boolean success = json.getBoolean("success");
		                    if (success) {
		                            poesia.addComentario(comentario.toString());
		                            callback.onSuccess(poesia);
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
	
}
