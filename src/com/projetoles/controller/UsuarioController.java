package com.projetoles.controller;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.UsuarioDAO;
import com.projetoles.model.Usuario;

public class UsuarioController extends Controller {
	
	private static UsuarioDAO sDao = UsuarioDAO.getInstance();

	public UsuarioController(Activity context) {
		super(context);
	}

	public void login(final String email, final String senha, 
			final OnRequestListener callbackListener) throws JSONException {
		Usuario usuario = null;
		try {
			usuario = new Usuario(email, null, senha);
		} catch (Exception e) {
			callbackListener.onError(e.getMessage());
		}
		sDao.insert(usuario, new OnRequestListener(callbackListener.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				try {
					JSONObject json = new JSONObject(result.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						callbackListener.onSuccess(result);
					} else {
						callbackListener.onError(json.getString("message"));
					}
				} catch (JSONException e) {
					callbackListener.onError(e.getMessage());
				}
			}
			
			@Override
			public void onError(String errorMessage) {
				callbackListener.onError(errorMessage);
			}
		});
	}

	public void getLoggedUser(final OnRequestListener callbackListener) {
		// ainda a ser implementado
	}

	private void salvaUsuario(Usuario user) {
		loggedUser = user;
		mEditor.putString("email", user.getEmail());
		mEditor.commit();
	}
	
	public void logout() {
		loggedUser = null;
		mEditor.clear();
		mEditor.commit();
	}

}