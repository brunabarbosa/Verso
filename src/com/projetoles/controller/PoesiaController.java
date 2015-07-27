package com.projetoles.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.PoesiaDAO;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

public class PoesiaController extends Controller<Poesia> {
	
	private static ObjectLoader<Poesia> sLoader = new ObjectLoader<Poesia>();
	
	public PoesiaController(Activity context) {
		super(context);
		mDAO = new PoesiaDAO();
		mLoader = sLoader;
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
 
	public void post(String titulo, String autor, Usuario postador,
			String poesia, Calendar dataCriacao, String tags,
			OnRequestListener<Poesia> callback) {
		try { 
			Poesia p = new Poesia(null, dataCriacao, titulo, postador, autor, poesia, tags, new ObjectListID(), new ObjectListID());
			super.post(p, callback);
		} catch (Exception e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		}
	}

	public void get(String id, OnRequestListener<Poesia> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("postador", new UsuarioController(callback.getContext()));
		super.get(id, callback, dependencies);
	}
	
}
