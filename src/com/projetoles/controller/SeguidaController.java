package com.projetoles.controller;

import java.util.Calendar;

import android.app.Activity;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.SeguidaDAO;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

public class SeguidaController extends Controller<Seguida> {

	private static ObjectLoader<Seguida> sLoader = new ObjectLoader<Seguida>();
	
	public SeguidaController(Activity context) {
		super(context);
		mDAO = new SeguidaDAO();
		mLoader = sLoader;
	}
   
	public void get(String id, OnRequestListener<Seguida> callback) {
		Dependencies dependencies = new Dependencies();
		dependencies.addDependency("follower", new UsuarioController(callback.getContext()));
		dependencies.addDependency("followed", new UsuarioController(callback.getContext()));
		super.get(id, callback, dependencies);
	}
 
	public void post(Usuario seguidor, Usuario seguindo, OnRequestListener<Seguida> callback) {
		try {
			Seguida seguida = new Seguida(null, Calendar.getInstance(), seguidor, seguindo);
			super.post(seguida, callback);
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
