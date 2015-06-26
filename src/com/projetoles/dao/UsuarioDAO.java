package com.projetoles.dao;

import com.projetoles.model.Usuario;

/**
 * Implementação do DAO usando um Webservice e requisições POST e GET
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
	
}