package com.projetoles.controller;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.UsuarioDAO;
import com.projetoles.model.PasswordEncrypter;
import com.projetoles.model.Usuario;

public class UsuarioController extends Controller {

	private static UsuarioDAO sDao = UsuarioDAO.getInstance();
	public static Usuario usuarioLogado;
	
	public UsuarioController(Activity context) {
		super(context);
	}

	public void registrar(final String nome, final String email, final String senha, 
			final String repetirSenha, final OnRequestListener callback) {
		if (!senha.equals(repetirSenha)) {
			callback.onError("Senhas n�o coincidem.");
		} else {
			try {
				final Usuario usuario = new Usuario(email, nome, senha);
				sDao.registrar(usuario, new OnRequestListener(callback.getContext()) {
					
					@Override
					public void onSuccess(Object result) {
						try {
							JSONObject json = new JSONObject(result.toString());
							boolean success = json.getBoolean("success");
							if (success) {
								salvaUsuario(usuario);
								callback.onSuccess(result);
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
			} catch (Exception e) {
				e.printStackTrace();
				callback.onError(e.getMessage());
			}
		}
	}
	
	public void login(final String email, final String senha, 
			final OnRequestListener callback) {
		try {
			final Usuario usuario = new Usuario(email, null, senha);
			sDao.auth(usuario, new OnRequestListener(callback.getContext()) {
				
				@Override
				public void onSuccess(Object result) {
					try {
						JSONObject json = new JSONObject(result.toString());
						boolean success = json.getBoolean("success");
						if (success) {
							Usuario encontrado = Usuario.converteJSON(json);
							encontrado.setSenha(senha);
							salvaUsuario(encontrado);
							callback.onSuccess(encontrado);
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
		} catch (Exception e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		} 
	}

	public void addFoto(final Usuario usuario, final byte[] foto, final OnRequestListener callback) {
		sDao.addFoto(usuario, foto, new OnRequestListener(callback.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				JSONObject json;
				try {
					json = new JSONObject(result.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						usuario.setFoto(foto);
						callback.onSuccess(null);
					} else {
						callback.onError(json.getString("message"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
			callbackListener.onError("Usu�rio n�o encontrado.");
		}
	}

	private void salvaUsuario(Usuario usuario) {
		usuarioLogado = usuario;
		mEditor.putString("email", usuario.getEmail()).apply();
		if (!usuario.getSenha().trim().equals(""))
			mEditor.putString("senha", usuario.getSenha()).apply();
	}
	 
	public void logout() {
		usuarioLogado = null;
		mEditor.clear().apply();
	}
	
	public void editUser(final String nome, final String biografia, final String senha, final String confirmaSenha, final OnRequestListener callback){
		if(!senha.equals(confirmaSenha)){
			callback.onError("Senhas n�o coincidem.");
		}else{
			if(!senha.isEmpty() && senha.length() < 6){
				callback.onError("Senha deve ter pelo menos 6 caracteres.");	
			}else{
				sDao.editUser(usuarioLogado, biografia, nome, PasswordEncrypter.getEncryptedPassword(senha), new OnRequestListener(callback.getContext()) {
					
					@Override
					public void onSuccess(Object result) {
						try {
							JSONObject json = new JSONObject(result.toString());
							boolean success = json.getBoolean("success");
							if (success) {
								usuarioLogado.setBiografia(biografia);
								usuarioLogado.setNome(nome);
								if(!senha.isEmpty()){
									usuarioLogado.setSenha(senha);
									salvaUsuario(usuarioLogado);
								}
								callback.onSuccess(result);
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
	
	}

}