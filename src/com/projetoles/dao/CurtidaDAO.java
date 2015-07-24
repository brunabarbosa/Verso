package com.projetoles.dao;

import com.projetoles.model.Curtida;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;


public class CurtidaDAO extends DAO {

	private static CurtidaDAO sInstance;
	
	public static CurtidaDAO getInstance() {
		if (sInstance == null) {
			sInstance = new CurtidaDAO();
		}
		return sInstance;
	}

	public void curtir(Poesia poesia, Curtida curtida, OnRequestListener callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("poesia", poesia.getId())
			.addParam("dataCriacao", curtida.getStringDataCriacao())
			.addParam("postador", curtida.getPostador().getEmail())
			.setDomain(DOMAIN)
			.setPath("like");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	public void descurtir(Usuario usuario, Poesia poesia, OnRequestListener callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("poesia", poesia.getId())
			.addParam("postador", usuario.getEmail())
			.setDomain(DOMAIN)
			.setPath("unlike");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	public void getCurtida(String id, OnRequestListener callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("like");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	} 
	
}
