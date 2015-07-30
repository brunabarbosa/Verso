package com.projetoles.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
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
			usuarioLogado = usuario;
			mEditor.putString("id", usuario.getId());
			mEditor.commit();
			mEditor.putString("senha", usuario.getSenha());
			mEditor.commit();
		}
	}
	 
	public void logout() {
		if (AccessToken.getCurrentAccessToken() != null) {
			LoginManager.getInstance().logOut();
		}
		usuarioLogado = null;
		mEditor.clear().commit();		
	}

	public void getUsuarioLogado(final OnRequestListener<Usuario> callback, final String regId) {
		if (usuarioLogado != null) {
			callback.onSuccess(usuarioLogado);
		} else if (mSession.contains("id") && mSession.contains("senha")) {
			Usuario usuario = new Usuario(mSession.getString("id", null), null,
					null, null, null, null, null, null, null, null);
			usuario.setSenha(mSession.getString("senha", null), false);
			this.login(usuario, regId, callback);
		} else {
			callback.onError("Usuário não encontrado.");
		} 
	}

	public void login(String email, String senha, String regId, OnRequestListener<Usuario> callback) {
		try {
			Usuario usuario = new Usuario(email, senha, null, null, new byte[]{}, 
					new ObjectListID(), new ObjectListID(), new ObjectListID(), new ObjectListID(), new ObjectListID());
			this.login(usuario, regId, callback);
		} catch (Exception e) {
			e.printStackTrace(); 
			callback.onError(e.getMessage());
		}
	}
	
	private void login(final Usuario usuario, final String regId, final OnRequestListener<Usuario> callback) {
		((UsuarioDAO)mDAO).login(usuario, regId, new OnRequestListener<String>(callback.getContext()) {
			
			@Override
			public void onSuccess(String jsonResult) {
				try {
					JSONObject jsonObject = new JSONObject(jsonResult.toString());
					boolean success = jsonObject.getBoolean("success");
					if (success) {
						Usuario encontrado = mDAO.getFromJSON(jsonObject, new ArrayList<Object>());
						encontrado.setSenha(usuario.getSenha(), false);
						login(encontrado);
						mLoader.save(encontrado);
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
				Usuario usuario = new Usuario(id, senha, nome, "", new byte[]{}, 
						new ObjectListID(), new ObjectListID(), new ObjectListID(), new ObjectListID(), new ObjectListID());
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

	public void put(final String nome, final String biografia, final String senha, String repetirSenha, 
			final OnRequestListener<Usuario> callback) {
		try {
			if (!senha.equals(repetirSenha)) {
				callback.onError("Senhas não coincidem.");
			} else {
				Usuario usuario = new Usuario(usuarioLogado.getId(), senha.trim().isEmpty() ? null : senha, nome, biografia, usuarioLogado.getFoto(), 
						usuarioLogado.getPoesias(), usuarioLogado.getNotificacoes(), usuarioLogado.getCurtidas(), 
						usuarioLogado.getSeguindo(), usuarioLogado.getSeguidores());
				super.put(usuario, new OnRequestListener<Usuario>(callback.getContext()) {

					@Override
					public void onSuccess(Usuario usuario) {
						usuarioLogado.setNome(nome);
						usuarioLogado.setBiografia(biografia);
						if (senha != null && !senha.trim().isEmpty()) {
							usuarioLogado.setSenha(senha);
						}
						login(usuarioLogado);
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

	@Override
	public void update(final Usuario object, final OnRequestListener<Usuario> callback) {
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
						ArrayList<String> poesias = new ArrayList<String>();
						array = json.getJSONArray("poetries");
						for (int i = 0; i < array.length(); i++) {
							poesias.add(array.get(i).toString());
						}
						ArrayList<String> seguindo = new ArrayList<String>();
						array = json.getJSONArray("followeds");
						for (int i = 0; i < array.length(); i++) {
							seguindo.add(array.get(i).toString());
						}
						ArrayList<String> seguidores = new ArrayList<String>();
						array = json.getJSONArray("followers");
						for (int i = 0; i < array.length(); i++) {
							seguidores.add(array.get(i).toString());
						}
						object.getCurtidas().setList(likes);
						object.getPoesias().setList(poesias);
						object.getSeguindo().setList(seguindo);
						object.getSeguidores().setList(seguidores);
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

	public void save(Usuario result) {
		mLoader.save(result);
	}

}