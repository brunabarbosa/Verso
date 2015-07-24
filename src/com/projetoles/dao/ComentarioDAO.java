package com.projetoles.dao;

import com.projetoles.model.Comentario;
import com.projetoles.model.Poesia;

public class ComentarioDAO extends DAO {

	private static ComentarioDAO sInstance;
	
	public static ComentarioDAO getInstance() {
		if (sInstance == null) {
			sInstance = new ComentarioDAO();
		}
		return sInstance;
	}

	public void comentar(Poesia poesia, Comentario comentario, OnRequestListener callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("poesia", poesia.getId())
			.addParam("comentario", comentario.getComentario())
			.addParam("dataCriacao", comentario.getStringDataCriacao())
			.addParam("postador", comentario.getPostador().getEmail())
			.setDomain(DOMAIN)
			.setPath("comment");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	public void getComentario(String id, OnRequestListener callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("comment");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}
	
}
