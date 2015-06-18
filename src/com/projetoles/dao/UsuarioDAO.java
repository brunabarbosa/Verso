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

	public void insert(Usuario usuario, OnRequestListener callbackListener) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("username", usuario.getEmail())
			.addParam("name", usuario.getNome() == null ? "" : usuario.getNome())
			.addParam("password", usuario.getSenha())
			.setDomain(DOMAIN)
			.setPath("user");
		POST post = (POST) postRequest.create();
		post.execute(callbackListener);
	}
	
}