package com.projetoles.dao;

import java.io.IOException;

import com.projetoles.dao.POST.Builder;
import com.projetoles.model.Usuario;

/**
 * Implementa��o do DAO usando um Webservice e requisi��es POST e GET
 */
public class UsuarioDAO extends DAO {

	private static UsuarioDAO sInstance;

	public static UsuarioDAO getInstance() {
		if (sInstance == null) {
			sInstance = new UsuarioDAO();
		}
		return sInstance;
	}

	public void auth(Usuario usuario, OnRequestListener callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("email", usuario.getEmail())
			.addParam("password", usuario.getSenha())
			.setDomain(DOMAIN)
			.setPath("auth");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	 
	public void registrar(Usuario usuario, OnRequestListener callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("email", usuario.getEmail())
			.addParam("password", usuario.getSenha())
			.addParam("name", usuario.getNome())
			.setDomain(DOMAIN)
			.setPath("user");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	public void addFoto(Usuario usuario, byte[] photo, OnRequestListener callback) {
		try {
			POST.Builder postRequest = (Builder) ((POST.Builder) (new POST.Builder()
				.addParam("email", usuario.getEmail())))
				.addPhoto(photo)
				.setDomain(DOMAIN)
				.setPath("photo_upload");
			POST post = (POST) postRequest.create();
			post.execute(callback);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callback.onError(e.getMessage());
		}
	}
	
	public void editUser(Usuario usuario, String biografia, String nome, String senha, OnRequestListener callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("email", usuario.getEmail())
			.addParam("nome", nome )
			.addParam("bio", biografia)
			.addParam("senha", senha)
			.setDomain(DOMAIN)
			.setPath("edit_user");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
}