package com.projetoles.controller;

import java.security.acl.NotOwnerException;
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
	
	private static PoesiaDAO poesiaDao = PoesiaDAO.getInstance();
	private static CurtidaDAO curtidaDao = CurtidaDAO.getInstance();
	private static ComentarioDAO comentarioDao = ComentarioDAO.getInstance();
	private NotificacaoController mNotificacaoController;
	
	public PoesiaController(Activity context) {
		super(context);
		mNotificacaoController = new NotificacaoController(context);
	}

	public void getPoesia(final String id, final OnRequestListener callback) {
		poesiaDao.getPoesia(id, new OnRequestListener(callback.getContext()) {
			
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
		poesiaDao.pesquisar(titulo, autor, tag, trecho, new OnRequestListener(callback.getContext()) {
			
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
				poesiaDao.criarPoesia(poema, postador, new OnRequestListener(callback.getContext()) {

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
				curtidaDao.curtir(poesia, curtida, new OnRequestListener(callback.getContext()) {
					
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

	public void descurtir(Poesia poesia, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				curtidaDao.descurtir(UsuarioController.usuarioLogado, poesia, new OnRequestListener(callback.getContext()) {
		            @Override
		            public void onSuccess(Object result) {
		                try {
		                    JSONObject json = new JSONObject(result.toString());
		                    boolean success = json.getBoolean("success");
		                    if (success) {
		                    	callback.onSuccess(null);
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
		curtidaDao.getCurtida(id, new OnRequestListener(callback.getContext()) {
			
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
	
	public void comentar(final Poesia poesia, final String stringComentario, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				final Comentario comentario = new Comentario(stringComentario, UsuarioController.usuarioLogado.getEmail(), Calendar.getInstance());
				comentarioDao.comentar(poesia, comentario, new OnRequestListener(callback.getContext()) {
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
		comentarioDao.getComentario(id, new OnRequestListener(callback.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				try {
					JSONObject json = new JSONObject(result.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						Comentario comentario = Comentario.converteJson(json);
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
	}
	
}
