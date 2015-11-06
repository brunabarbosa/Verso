package com.projetoles.controller;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.PoesiaDAO;
import com.projetoles.model.Comentario;
import com.projetoles.model.Curtida;
import com.projetoles.model.Notificacao;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Usuario;

import android.content.Context;

public class PoesiaController extends Controller<Poesia> {
	
	private static ObjectLoader<Poesia> sLoader = new ObjectLoader<Poesia>();
	private NotificacaoController mNotificacao;
	
	public PoesiaController(Context context) {
		super(context);
		mDAO = new PoesiaDAO();
		mLoader = sLoader;
		mNotificacao = new NotificacaoController(context);
	}

	public void pesquisar(final String titulo, final String autor, final String tag,
			final String trecho, final OnRequestListener<ObjectListID<Poesia>> callback) {
		((PoesiaDAO)mDAO).pesquisar(titulo, autor, tag, trecho, new OnRequestListener<String>(callback.getContext()) {
			
			@Override
			public void onSuccess(String jsonResult) {
				try {
					JSONObject json = new JSONObject(jsonResult.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						ObjectListID<Poesia> ids = new ObjectListID<Poesia>(json.getJSONArray("poetries"));
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

			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
		});
	}
 
	public void post(final String titulo, final String autor, final Usuario postador,
			final String poesia, final Calendar dataCriacao, final String tags,
			final OnRequestListener<Poesia> callback) {
		UsuarioController controller = new UsuarioController(callback.getContext());
		controller.getUsuarioLogado(new OnRequestListener<Usuario>(callback.getContext()) {

			@Override
			public void onSuccess(final Usuario usuarioLogado) {
				try { 
					Poesia p = new Poesia(null, dataCriacao, titulo, usuarioLogado, autor, poesia, tags, new ObjectListID<Comentario>(), new ObjectListID<Curtida>());
					PoesiaController.super.post(p, new OnRequestListener<Poesia>(callback.getContext()) {
						
						@Override
						public void onSuccess(Poesia result) {
							if (!postador.equals(usuarioLogado)) {
								mNotificacao.post(new Notificacao(null, Calendar.getInstance(), 
										postador, usuarioLogado, " compartilhou sua poesia.", result, "poesia"), 
									new OnRequestListener<Notificacao>(callback.getContext()) {
		 
										@Override
										public void onSuccess(Notificacao result) {
											postador.getNotificacoes().add(result.getId(), result.getDataCriacao().getTimeInMillis());
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
							callback.onSuccess(result);
							
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
	public void get(String id, OnRequestListener<Poesia> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("poster", new UsuarioController(callback.getContext()));
		super.get(id, callback, dependencies);
	}

	@Override
	public void update(final Poesia object, final OnRequestListener<Poesia> callback) {
		mDAO.update(object, new OnRequestListener<String>(callback.getContext()) {

			@Override
			public void onSuccess(String jsonResult) {
				try {
					JSONObject json = new JSONObject(jsonResult);
					boolean success = json.getBoolean("success");
					if (success) {
						long numLikes = json.getLong("liked");
						long numComments = json.getLong("commented");
						object.setNumCurtidas(numLikes);
						object.setNumComentarios(numComments);
						ArrayList<PreloadedObject<Curtida>> likes = new ArrayList<PreloadedObject<Curtida>>();
						JSONArray array = json.getJSONArray("likes");
						for (int i = 0; i < array.length(); i++) {
							String id = array.getJSONObject(i).getString("id");
							long time = array.getJSONObject(i).getLong("date");
							likes.add(new PreloadedObject<Curtida>(time, id));
						}
						ArrayList<PreloadedObject<Comentario>> comments = new ArrayList<PreloadedObject<Comentario>>();
						array = json.getJSONArray("comments");
						for (int i = 0; i < array.length(); i++) {
							String id = array.getJSONObject(i).getString("id");
							long time = array.getJSONObject(i).getLong("date");
							comments.add(new PreloadedObject<Comentario>(time, id));
						}
						object.getCurtidas().setList(likes);
						object.getComentarios().setList(comments);
						callback.onSuccess(object);
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

			@Override
			public void onTimeout() {
				callback.onTimeout();
			}
		});
	}

	public void getMaisCurtidas(final OnRequestListener<ObjectListID<Poesia>> callback) {
		((PoesiaDAO)mDAO).getMaisCurtidas(new OnRequestListener<String>(callback.getContext()) {

			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					if (success) { 
						ObjectListID<Poesia> poesias = new ObjectListID<Poesia>();
						JSONArray array = jsonObject.getJSONArray("poetries");
						for (int i = 0; i < array.length(); i++) {
							String id = array.getJSONObject(i).getString("id");
							long time = array.getJSONObject(i).getLong("date");
							poesias.add(new PreloadedObject<Poesia>(time, id));
						}
						callback.onSuccess(poesias);
					} else {
						callback.onError(jsonObject.getString("message"));
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

			@Override
			public void onTimeout() {
				callback.onTimeout();
			}
		});
	}
	
	public void getMaisComentadas(final OnRequestListener<ObjectListID<Poesia>> callback) {
		((PoesiaDAO)mDAO).getMaisComentadas(new OnRequestListener<String>(callback.getContext()) {

			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					if (success) { 
						ObjectListID<Poesia> poesias = new ObjectListID<Poesia>();
						JSONArray array = jsonObject.getJSONArray("poetries");
						for (int i = 0; i < array.length(); i++) {
							String id = array.getJSONObject(i).getString("id");
							long time = array.getJSONObject(i).getLong("date");
							poesias.add(new PreloadedObject<Poesia>(time, id));
						}
						callback.onSuccess(poesias);
					} else {
						callback.onError(jsonObject.getString("message"));
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

			@Override
			public void onTimeout() {
				callback.onTimeout();
			}
		});
	}

	public void getMaisRecentes(final OnRequestListener<ObjectListID<Poesia>> callback) {
		((PoesiaDAO)mDAO).getMaisRecentes(new OnRequestListener<String>(callback.getContext()) {

			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					if (success) { 
						ObjectListID<Poesia> poesias = new ObjectListID<Poesia>();
						JSONArray array = jsonObject.getJSONArray("poetries");
						for (int i = 0; i < array.length(); i++) {
							String id = array.getJSONObject(i).getString("id");
							long time = array.getJSONObject(i).getLong("date");
							poesias.add(new PreloadedObject<Poesia>(time, id));
						}
						callback.onSuccess(poesias);
					} else {
						callback.onError(jsonObject.getString("message"));
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

			@Override
			public void onTimeout() {
				callback.onTimeout();
			}
		});
	}
	
}
