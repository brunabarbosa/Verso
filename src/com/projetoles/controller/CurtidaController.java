package com.projetoles.controller;

import java.util.Calendar;

import android.app.Activity;

import com.projetoles.dao.CurtidaDAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Curtida;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

public class CurtidaController extends Controller<Curtida> {

	private static ObjectLoader<Curtida> sLoader = new ObjectLoader<Curtida>();
	
	public CurtidaController(Activity context) {
		super(context);
		mDAO = new CurtidaDAO();
		mLoader = sLoader;
	}
  
	public void get(String id, OnRequestListener<Curtida> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("poster", new UsuarioController(callback.getContext()));
		dependencies.addDependency("poetry", new PoesiaController(callback.getContext()));
		super.get(id, callback, dependencies);
	}

	public void post(Usuario postador, Poesia poesia, OnRequestListener<Curtida> callback) {
		try {
			Curtida curtida = new Curtida(null, Calendar.getInstance(), postador, poesia);
			super.post(curtida, callback);
		} catch (Exception e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		}
	}

	@Override
	public void update(Curtida object, OnRequestListener<Curtida> callback) {
		callback.onSuccess(object);
	}
 
}
