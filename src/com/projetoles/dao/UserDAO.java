package com.projetoles.dao;

import java.io.File;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import com.projetoles.model.User;
import com.projetoles.model.UserMapper;
import com.projetoles.dao.DAO;

/**
 * Implementação do DAO usando um Webservice e requisições POST e GET
 */
public class UserDAO extends DAO {

	private static final String ENCRYPTION_KEY = "5957defce1cfebf1473ab22940871b4b";
	private static UserDAO sInstance;

	public static UserDAO getInstance() {
		if (sInstance == null) {
			sInstance = new UserDAO();
		}
		return sInstance;
	}

	private void insert(User user, File photo, OnRequestListener callbackListener, boolean edit) {
		POST.Builder postRequest = UserMapper.mapUserToRequest(user);
		((POST.Builder) postRequest.setDomain(DOMAIN)).addPhoto(photo);
		if (edit) {
			postRequest.setPath("put_user.php");
		} else {
			postRequest.setPath("auth");
		}
		POST post = (POST) postRequest.create();
		post.execute(callbackListener);
	}

	public void insert(User user, File photo, OnRequestListener callbackListener) {
		insert(user, photo, callbackListener, false);
	}
	
	public void edit(User user, File photo, OnRequestListener callbackListener) {
		insert(user, photo, callbackListener, true);
	}

	@SuppressLint("DefaultLocale") 
	public void find(String login, final OnRequestListener callbackListener) {
		GET getRequest = (GET) new GET.Builder()
				.setDomain(UserDAO.DOMAIN)
				.setPath("get_user.php")
				.addParam("encryption_key", ENCRYPTION_KEY)
				.addParam("login", login.toLowerCase())
				.create();
		getRequest.execute(new OnRequestListener(callbackListener.getContext()) {
			
			@Override
			public void onSuccess(Object result) {
				try {
					String s = getNormalizedString(result);
					JSONObject obj = new JSONObject(s);
					if (!obj.has("login")) {
						callbackListener.onError("Usuário não encontrado.");
					} else { 
						User userFound = UserMapper.mapJsonToUser(obj);
						callbackListener.onSuccess(userFound);
					}
				} catch (Exception e) {
					e.printStackTrace();
					callbackListener.onError(e.getMessage());
				} 
			}
			
			@Override
			public void onError(String errorMessage) {
				callbackListener.onError("Um erro ocorreu. Tente novamente.");
			}
		});
	}
    
	public void delete(User user, OnRequestListener callbackListener) {
		POST postRequest = (POST) new POST.Builder()
				.setDomain(DOMAIN)
				.setPath("delete_user.php")
				.addParam("login", user.getLogin())
				.create();
		postRequest.execute(callbackListener);
	}
	
	
	
}