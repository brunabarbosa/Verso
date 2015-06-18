package com.projetoles.controller;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.UsuarioDAO;
import com.projetoles.model.Usuario;

public class UsuarioController extends Controller {

	private static UsuarioDAO sDao = UsuarioDAO.getInstance();
	public static Usuario usuarioLogado;
	
	public UsuarioController(Activity context) {
		super(context);
	}

	public void login(final String email, final String senha, 
			final OnRequestListener callbackListener) {
		Usuario usuario = null;
		try {
			usuario = new Usuario(email, null, senha);
		} catch (Exception e) {
			callbackListener.onError(e.getMessage());
		} 
		sDao.auth(usuario, new OnRequestListener(callbackListener.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				try {
					JSONObject json = new JSONObject(result.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						Usuario encontrado = Usuario.converteJSON(json);
						salvaUsuario(encontrado);
						callbackListener.onSuccess(encontrado);
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
		if (usuarioLogado != null) {
			callbackListener.onSuccess(usuarioLogado);
		} else if (mSession.contains("email")) {
			login(mSession.getString("email", ""), mSession.getString("senha", ""), 
				new OnRequestListener(callbackListener.getContext()) {
					
					@Override
					public void onSuccess(Object result) {
						callbackListener.onSuccess(result);
					}
					
					@Override
					public void onError(String errorMessage) {
						logout();
						callbackListener.onError(errorMessage);
					}
				});
		} else {
			callbackListener.onError("Usuário não encontrado.");
		}
	}

	private void salvaUsuario(Usuario usuario) {
		usuarioLogado = usuario;
		mEditor.putString("email", usuario.getEmail());
		mEditor.putString("senha", usuario.getSenha());
		mEditor.commit();
	}
	
	public void logout() {
		usuarioLogado = null;
		mEditor.clear();
		mEditor.commit();
	}

}