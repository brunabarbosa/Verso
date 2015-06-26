package com.projetoles.dao;

import com.projetoles.model.Poema;

/**
 * Implementação do DAO usando um Webservice e requisições POST e GET
 */
public class PoemaDAO extends DAO{
	private static PoemaDAO sInstance;
	
	public static PoemaDAO getInstance() {
		if (sInstance == null) {
			sInstance = new PoemaDAO();
		}
		return sInstance;
	}
	
	public void criarPoema(Poema poema, OnRequestListener callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("autor", poema.getAutor())
			.addParam("poesia", poema.getPoesia())
			.addParam("titulo", poema.getTitulo())
			.addParam("dataCriacao", poema.getStringDataCriacao())
			.addParam("tags", poema.getTags())
			.setDomain(DOMAIN)
			.setPath("poema");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
}
