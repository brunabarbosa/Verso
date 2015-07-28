package com.projetoles.controller;

import java.util.Calendar;

import android.app.Activity;

import com.projetoles.dao.ComentarioDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Comentario;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

public class ComentarioController extends Controller<Comentario> {

	private static ObjectLoader<Comentario> sLoader = new ObjectLoader<Comentario>();
	
	public ComentarioController(Activity context) {
		super(context);
		mDAO = new ComentarioDAO();
		mLoader = sLoader;
	}

	public void post(Usuario postador, Poesia poesia, String comentario,
			OnRequestListener<Comentario> callback) {
		try {
			Comentario c = new Comentario(null, Calendar.getInstance(), comentario, postador, poesia);
			super.post(c, callback); 
		} catch (Exception e) {
			callback.onError(e.getMessage());
		}
	}
 
	public void get(String id, OnRequestListener<Comentario> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("poster", new UsuarioController(callback.getContext()));
		dependencies.addDependency("poetry", new PoesiaController(callback.getContext()));
		super.get(id, callback, dependencies);
	}

	@Override
	public void update(Comentario object, OnRequestListener<Comentario> callback) {
		callback.onSuccess(object);
	}
	
}
