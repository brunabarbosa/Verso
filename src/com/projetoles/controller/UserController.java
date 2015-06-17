package com.projetoles.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.projetoles.verso.MainActivity;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.POST;
import com.projetoles.dao.UserDAO;
import com.projetoles.model.PasswordEncrypter;
import com.projetoles.model.User;
import com.projetoles.model.UserMapper;

import org.json.JSONException;
import org.json.JSONObject;

public class UserController extends Controller {
	
	private static UserDAO sDao = UserDAO.getInstance();
	private SharedPreferences mSession;
	private Editor mEditor;

	public UserController(Activity context) {
		super(context);
		this.mSession = context.getSharedPreferences("User", 0); 
		this.mEditor = mSession.edit();
	}

	private void login(User user) {
		loggedUser = user;
		mEditor.putString("login", user.getLogin());
		mEditor.commit();
	}
	
	public void logout() {
		loggedUser = null;
		mEditor.clear();
		mEditor.commit();
	}

	public void login(final String login, final String password, 
			final OnRequestListener callbackListener) {
		if (login.trim().equals("")) {
			callbackListener.onError("Login é obrigatório.");
		} else if (password.trim().equals("")) {
			callbackListener.onError("Senha é obrigatória.");
		} else {
			sDao.find(login, new OnRequestListener(callbackListener.getContext()) {
				
				@Override
				public void onSuccess(Object result) {
					User userFound = (User) result;
					String encryptedPassword;
					encryptedPassword = PasswordEncrypter.getEncryptedPassword(password);
					if (userFound.getPassword().equals(encryptedPassword)) {
						//Guarda dados do usuário
						login(userFound);
						callbackListener.onSuccess(userFound);
					} else {
						//Senhas não coincidem
						callbackListener.onError("Usuário ou senha inválidos.");
					}
				}
				
				//Usuário não encontrado
				@Override
				public void onError(String errorMessage) {
					callbackListener.onError("Usuário ou senha inválidos.");
				}
			});
		}
	}

	public void registerUser(final String login, final String email, final String password , final OnRequestListener callbackListener) {
		if (login.trim().equals("")) {
			callbackListener.onError("Login é obrigatório.");
		} else if (email.trim().equals("")) {
			callbackListener.onError("E-mail é obrigatório.");
		} else if (password.trim().equals("")) {
			callbackListener.onError("Senha é obrigatória.");
		} else {
			//Procura por usuário que tenha mesmo e-mail
			sDao.find(login, new OnRequestListener(callbackListener.getContext()) {
				
				//Usuário encontrado
				@Override
				public void onSuccess(Object result) {
					callbackListener.onError("Usuário já cadastrado.");
				}
				
				//Usuário não encontrado
				@Override
				public void onError(String errorMessage) {
					try {
						//Registra usuário
						final User newUser = new User(login, email, null, password);
						sDao.insert(newUser, null, new OnRequestListener(callbackListener.getContext()) {
							
							@Override
							public void onSuccess(Object result) {
								//Guarda dados do usuário
								login(newUser);
								callbackListener.onSuccess(newUser);
								JSONObject newJson = new JSONObject();
								POST.Builder postRequest = UserMapper.mapUserToRequest((User) result);
								try {
									if(newJson.getString(result.toString()) == "success"){
										
									}else{
										callbackListener.onError("Usuário não econtrado.");
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							@Override
							public void onError(String errorMessage) {
								callbackListener.onError("Login ou e-mail já utilizado.");
							}
						});
					} catch(IllegalArgumentException e) {
						//Erro ao criar usuário
						callbackListener.onError(e.getMessage());
					}
				}
			});
		}
	}
	
	public void getLoggedUser(final OnRequestListener callbackListener) {
		if (loggedUser != null) {
			callbackListener.onSuccess(loggedUser);
		} else {
			//Se existe usuário na sessão
			if (mSession.contains("login")) {
				String login = mSession.getString("login", "");
				sDao.find(login, new OnRequestListener(mContext) {
					
					@Override
					public void onSuccess(Object result) {
						loggedUser = (User)result;
						callbackListener.onSuccess(result);
						
					}
					
					@Override
					public void onError(String errorMessage) {
						//Dados corrompidos
						mEditor.clear();
						mEditor.commit();
						callbackListener.onError("Session expired.");
					}
				});
			} else {
				callbackListener.onError("Session expired.");
			}
		}
	}

	

	
	
}