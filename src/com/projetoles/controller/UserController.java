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
			callbackListener.onError("Login � obrigat�rio.");
		} else if (password.trim().equals("")) {
			callbackListener.onError("Senha � obrigat�ria.");
		} else {
			sDao.find(login, new OnRequestListener(callbackListener.getContext()) {
				
				@Override
				public void onSuccess(Object result) {
					User userFound = (User) result;
					String encryptedPassword;
					encryptedPassword = PasswordEncrypter.getEncryptedPassword(password);
					if (userFound.getPassword().equals(encryptedPassword)) {
						//Guarda dados do usu�rio
						login(userFound);
						callbackListener.onSuccess(userFound);
					} else {
						//Senhas n�o coincidem
						callbackListener.onError("Usu�rio ou senha inv�lidos.");
					}
				}
				
				//Usu�rio n�o encontrado
				@Override
				public void onError(String errorMessage) {
					callbackListener.onError("Usu�rio ou senha inv�lidos.");
				}
			});
		}
	}

	public void registerUser(final String login, final String email, final String password , final OnRequestListener callbackListener) {
		if (login.trim().equals("")) {
			callbackListener.onError("Login � obrigat�rio.");
		} else if (email.trim().equals("")) {
			callbackListener.onError("E-mail � obrigat�rio.");
		} else if (password.trim().equals("")) {
			callbackListener.onError("Senha � obrigat�ria.");
		} else {
			//Procura por usu�rio que tenha mesmo e-mail
			sDao.find(login, new OnRequestListener(callbackListener.getContext()) {
				
				//Usu�rio encontrado
				@Override
				public void onSuccess(Object result) {
					callbackListener.onError("Usu�rio j� cadastrado.");
				}
				
				//Usu�rio n�o encontrado
				@Override
				public void onError(String errorMessage) {
					try {
						//Registra usu�rio
						final User newUser = new User(login, email, null, password);
						sDao.insert(newUser, null, new OnRequestListener(callbackListener.getContext()) {
							
							@Override
							public void onSuccess(Object result) {
								//Guarda dados do usu�rio
								login(newUser);
								callbackListener.onSuccess(newUser);
								JSONObject newJson = new JSONObject();
								POST.Builder postRequest = UserMapper.mapUserToRequest((User) result);
								try {
									if(newJson.getString(result.toString()) == "success"){
										
									}else{
										callbackListener.onError("Usu�rio n�o econtrado.");
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							@Override
							public void onError(String errorMessage) {
								callbackListener.onError("Login ou e-mail j� utilizado.");
							}
						});
					} catch(IllegalArgumentException e) {
						//Erro ao criar usu�rio
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
			//Se existe usu�rio na sess�o
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