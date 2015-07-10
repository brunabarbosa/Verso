package com.projetoles.dao;

import com.projetoles.dao.GET.Builder;
import com.projetoles.model.Poesia;

/**
 * Implementação do DAO usando um Webservice e requisições POST e GET
 */
public class PoesiaDAO extends DAO{
	private static PoesiaDAO sInstance;
	
	public static PoesiaDAO getInstance() {
		if (sInstance == null) {
			sInstance = new PoesiaDAO();
		}
		return sInstance;
	}
	
	public void criarPoesia(Poesia poema, String postador, OnRequestListener callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("autor", poema.getAutor())
			.addParam("postador", postador)
			.addParam("poesia", poema.getPoesia())
			.addParam("titulo", poema.getTitulo())
			.addParam("dataCriacao", poema.getStringDataCriacao())
			.addParam("tags", poema.getTags())
			.setDomain(DOMAIN)
			.setPath("poetry");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	public void getPoesia(String id, OnRequestListener callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("poetry");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}
	
	public void pesquisar(String titulo, String autor, String tag, String trecho,
			OnRequestListener callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("titulo", titulo)
			.addParam("autor", autor)
			.addParam("tag", tag)
			.addParam("trecho", trecho)
			.setDomain(DOMAIN)
			.setPath("search");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}
	
}
