package com.projetoles.dao;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

/**
 * Implementação do DAO usando um Webservice e requisições POST e GET
 */
public class PoesiaDAO extends DAO<Poesia> {
	
	private static PoesiaDAO sInstance;
	
	public static PoesiaDAO getInstance() {
		if (sInstance == null) {
			sInstance = new PoesiaDAO();
		}
		return sInstance;
	}
	
	@Override
	public void post(Poesia poesia, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("author", poesia.getAutor())
			.addParam("poster", poesia.getPostador().getId())
			.addParam("poetry", poesia.getPoesia())
			.addParam("title", poesia.getTitulo())
			.addParam("date", poesia.getStringDataCriacao())
			.addParam("tags", poesia.getTags())
			.setDomain(DOMAIN)
			.setPath("poetry/post");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override 
	public void get(String id, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("poetry/get");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}
	
	public void pesquisar(String titulo, String autor, String tag, String trecho, 
			OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("title", titulo)
			.addParam("author", autor)
			.addParam("tag", tag)
			.addParam("part", trecho)
			.setDomain(DOMAIN)
			.setPath("poetry/search");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}

	@Override
	public Poesia getFromJSON(JSONObject json, List<Object> params) throws JSONException {
		String id = json.getString("id");
		Calendar dataCriacao = CalendarUtils.stringToCalendar(json.getString("date"));
		String titulo = json.getString("title");
		String poesia = json.getString("poetry");
		String tags = json.getString("tags");
		String autor = json.getString("author");
		ObjectListID comentarios = new ObjectListID(json.getJSONArray("comments"));
		ObjectListID curtidas = new ObjectListID(json.getJSONArray("likes"));
		return new Poesia(id, dataCriacao, titulo, (Usuario)params.get(0), autor, poesia, tags, comentarios, curtidas);
	}
	 
}
