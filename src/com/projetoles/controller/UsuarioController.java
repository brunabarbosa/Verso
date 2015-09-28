package com.projetoles.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.UsuarioDAO;
import com.projetoles.model.Curtida;
import com.projetoles.model.Notificacao;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

import android.content.Context;

public class UsuarioController extends Controller<Usuario> {
 
	private static ObjectLoader<Usuario> sLoader = new ObjectLoader<Usuario>();
	public static Usuario usuarioLogado;
	
	public UsuarioController(Context context) {
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
					null, null, null, null, null, null, null, null, false);
			usuario.setSenha(mSession.getString("senha", null), false);
			this.login(usuario, regId, callback);
		} else {
			callback.onError("Usu�rio n�o encontrado.");
		} 
	}

	public void login(String email, String senha, String regId, OnRequestListener<Usuario> callback) {
		try {
			Usuario usuario = new Usuario(email, senha, null, null, new byte[]{}, 
					new ObjectListID<Poesia>(), new ObjectListID<Notificacao>(), new ObjectListID<Curtida>(), new ObjectListID<Seguida>(), new ObjectListID<Seguida>(), false);
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
				callback.onError("Senhas n�o coincidem.");
			} else {
				Usuario usuario = new Usuario(id, senha, nome, "", new byte[]{}, 
						new ObjectListID<Poesia>(), new ObjectListID<Notificacao>(), new ObjectListID<Curtida>(), new ObjectListID<Seguida>(), new ObjectListID<Seguida>(), false);
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
				callback.onError("Senhas n�o coincidem.");
			} else {
				Usuario usuario = new Usuario(usuarioLogado.getId(), senha.trim().isEmpty() ? null : senha, nome, biografia, usuarioLogado.getFoto(), 
						usuarioLogado.getPoesias(), usuarioLogado.getNotificacoes(), usuarioLogado.getCurtidas(), 
						usuarioLogado.getSeguindo(), usuarioLogado.getSeguidores(), usuarioLogado.getNotificacoesHabilitadas());
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
						ArrayList<PreloadedObject<Curtida>> likes = new ArrayList<PreloadedObject<Curtida>>();
						JSONArray array = json.getJSONArray("likes");
						for (int i = 0; i < array.length(); i++) {
							String id = array.getJSONObject(i).getString("id");
							long time = array.getJSONObject(i).getLong("date");
							likes.add(new PreloadedObject<Curtida>(time, id));
						}
						ArrayList<PreloadedObject<Poesia>> poesias = new ArrayList<PreloadedObject<Poesia>>();
						array = json.getJSONArray("poetries");
						for (int i = 0; i < array.length(); i++) {
							String id = array.getJSONObject(i).getString("id");
							long time = array.getJSONObject(i).getLong("date");
							poesias.add(new PreloadedObject<Poesia>(time, id));
						}
						ArrayList<PreloadedObject<Seguida>> seguindo = new ArrayList<PreloadedObject<Seguida>>();
						array = json.getJSONArray("followeds");
						for (int i = 0; i < array.length(); i++) {
							String id = array.getJSONObject(i).getString("id");
							long time = array.getJSONObject(i).getLong("date");
							seguindo.add(new PreloadedObject<Seguida>(time, id));
						}
						ArrayList<PreloadedObject<Seguida>> seguidores = new ArrayList<PreloadedObject<Seguida>>();
						array = json.getJSONArray("followers");
						for (int i = 0; i < array.length(); i++) {
							String id = array.getJSONObject(i).getString("id");
							long time = array.getJSONObject(i).getLong("date");
							seguidores.add(new PreloadedObject<Seguida>(time, id));
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

	public void setNotificacoesHabilitadas(boolean notificacoes, final OnRequestListener<Void> callback) {
		((UsuarioDAO)mDAO).setNotificacoes(usuarioLogado, notificacoes, new OnRequestListener<String>(callback.getContext()) {

			@Override
			public void onSuccess(String result) {
				callback.onSuccess(null);
			}

			@Override
			public void onError(String errorMessage) {
				callback.onError(errorMessage);
			}
		});
	}

	@Override
	public void get(String id, OnRequestListener<Usuario> callback) {
		throw new UnsupportedOperationException();
	}
	
}