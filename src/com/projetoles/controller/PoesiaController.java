package com.projetoles.controller;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.PoesiaDAO;
import com.projetoles.model.Notificacao;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
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
			final String trecho, final OnRequestListener<ArrayList<String>> callback) {
		((PoesiaDAO)mDAO).pesquisar(titulo, autor, tag, trecho, new OnRequestListener<String>(callback.getContext()) {
			
			@Override
			public void onSuccess(String jsonResult) {
				try {
					JSONObject json = new JSONObject(jsonResult.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						ArrayList<String> ids = new ArrayList<String>();
						JSONArray array = json.getJSONArray("poetries");
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
 
	public void post(String titulo, String autor, final Usuario postador,
			String poesia, Calendar dataCriacao, String tags,
			final OnRequestListener<Poesia> callback) {
		try { 
			Poesia p = new Poesia(null, dataCriacao, titulo, postador, autor, poesia, tags, new ObjectListID(), new ObjectListID());
			super.post(p, new OnRequestListener<Poesia>(callback.getContext()) {
				
				@Override
				public void onSuccess(Poesia result) {
					if (!postador.equals(UsuarioController.usuarioLogado)) {
						mNotificacao.post(new Notificacao(null, Calendar.getInstance(), 
						postador, UsuarioController.usuarioLogado, " compartilhou sua poesia.", result, "poesia"), 
							new OnRequestListener<Notificacao>(callback.getContext()) {
 
								@Override
								public void onSuccess(Notificacao result) {
									postador.getNotificacoes().add(result.getId());
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
						ArrayList<String> likes = new ArrayList<String>();
						JSONArray array = json.getJSONArray("likes");
						for (int i = 0; i < array.length(); i++) {
							likes.add(array.get(i).toString());
						}
						ArrayList<String> comments = new ArrayList<String>();
						array = json.getJSONArray("comments");
						for (int i = 0; i < array.length(); i++) {
							comments.add(array.get(i).toString());
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
		});
	}
	
}
