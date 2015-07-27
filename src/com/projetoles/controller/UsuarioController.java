package com.projetoles.controller;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.UsuarioDAO;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Usuario;

public class UsuarioController extends Controller<Usuario> {
 
	private static ObjectLoader<Usuario> sLoader = new ObjectLoader<Usuario>();
	public static Usuario usuarioLogado;
	
	public UsuarioController(Activity context) {
		super(context);
		mDAO = new UsuarioDAO();
		mLoader = sLoader;
	}

	private void login(Usuario usuario) {
		if (usuario != null) {
			if (usuarioLogado == null) {
				usuarioLogado = usuario;
			} else {
				usuarioLogado.setBiografia(usuario.getBiografia());
				usuarioLogado.setCurtidas(usuario.getCurtidas());
				usuarioLogado.setFoto(usuario.getFoto());
				usuarioLogado.setId(usuario.getId());
				usuarioLogado.setNotificacoes(usuario.getNotificacoes());
				usuarioLogado.setPoesias(usuario.getPoesias());
				if (usuario.getSenha() != null && !usuario.getSenha().isEmpty())
					usuarioLogado.setSenha(usuario.getSenha());
			} 
			mEditor.putString("id", usuario.getId()).apply();
			mEditor.putString("senha", usuario.getSenha()).apply();
		}
	}
	 
	public void logout() {
		usuarioLogado = null;
		mEditor.clear().apply();
	}

	public void getUsuarioLogado(final OnRequestListener<Usuario> callback) {
		if (usuarioLogado != null) {
			callback.onSuccess(usuarioLogado);
		} else if (mSession.contains("id")) {
			Usuario usuario = new Usuario(mSession.getString("id", null), mSession.getString("senha", null),
					null, null, null, null, null, null);
			this.login(usuario, callback);
		} else {
			callback.onError("Usuário não encontrado.");
		} 
	}

	public void login(String email, String senha, OnRequestListener<Usuario> callback) {
		try {
			Usuario usuario = new Usuario(email, senha, null, null, new byte[]{}, new ObjectListID(), new ObjectListID(), new ObjectListID());
			this.login(usuario, callback);
		} catch (Exception e) {
			e.printStackTrace(); 
			callback.onError(e.getMessage());
		}
	}
	
	private void login(final Usuario usuario, final OnRequestListener<Usuario> callback) {
		((UsuarioDAO)mDAO).auth(usuario, new OnRequestListener<String>(callback.getContext()) {
			
			@Override
			public void onSuccess(String jsonResult) {
				try {
					JSONObject jsonObject = new JSONObject(jsonResult.toString());
					boolean success = jsonObject.getBoolean("success");
					if (success) {
						Usuario encontrado = mDAO.getFromJSON(jsonObject, new ArrayList<Object>());
						mLoader.save(encontrado);
						login(encontrado);
						callback.onSuccess(encontrado);
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
		});
	}
	
	public void setFoto(final Usuario usuario, final byte[] foto, final OnRequestListener<Usuario> callback) {
		((UsuarioDAO)mDAO).setFoto(usuario, foto, new OnRequestListener<String>(callback.getContext()) {
			
			@Override
			public void onSuccess(String jsonResult) {
				JSONObject json;
				try {
					json = new JSONObject(jsonResult.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						usuario.setFoto(foto);
						callback.onSuccess(usuario);
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
 
	public void post(String nome, String id, String senha, String repetirSenha,
			final OnRequestListener<Usuario> callback) {
		try {
			if (!senha.equals(repetirSenha)) {
				callback.onError("Senhas não coincidem.");
			} else {
				Usuario usuario = new Usuario(id, nome, "", new byte[]{}, new ObjectListID(), new ObjectListID(), new ObjectListID());
				super.post(usuario, new OnRequestListener<Usuario>(callback.getContext()) {

					@Override
					public void onSuccess(Usuario result) {
						login(result);
						callback.onSuccess(result);
					}

					@Override
					public void onError(String errorMessage) {
						callback.onError(errorMessage);
					}
				}); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		}
	}

	public void put(final String nome, final String biografia, String senha, String repetirSenha, 
			final OnRequestListener<Usuario> callback) {
		try {
			if (!senha.equals(repetirSenha)) {
				callback.onError("Senhas não coincidem.");
			} else {
				if (senha.trim().isEmpty()) {
					senha = null;
				} 
				final String ssenha = senha;
				Usuario usuario = new Usuario(usuarioLogado.getId(), senha, nome, biografia, usuarioLogado.getFoto(), 
						usuarioLogado.getPoesias(), usuarioLogado.getNotificacoes(), usuarioLogado.getCurtidas());
				super.put(usuario, new OnRequestListener<Usuario>(callback.getContext()) {

					@Override
					public void onSuccess(Usuario usuario) {
						usuario.setNome(nome); 
						usuario.setBiografia(biografia);
						if (ssenha != null) {
							usuario.setSenha(ssenha);
						}
						login(usuario);
						callback.onSuccess(usuario);
					}

					@Override
					public void onError(String errorMessage) {
						callback.onError(errorMessage);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			callback.onError(e.getMessage());
		}
	}

}