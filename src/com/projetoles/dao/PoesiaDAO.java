package com.projetoles.dao;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Comentario;
import com.projetoles.model.Curtida;
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
	public void put(Poesia poesia, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("id", poesia.getId())
			.addParam("title", poesia.getTitulo())
			.addParam("author", poesia.getAutor())
			.addParam("tags", poesia.getTags())
			.addParam("poetry", poesia.getPoesia())
			.setDomain(DOMAIN)
			.setPath("poetry/put");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}

	@Override
	public void delete(String id, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("poetry/delete");
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
	public void update(Poesia poesia, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", poesia.getId())
			.setDomain(DOMAIN)
			.setPath("poetry/update");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}

	public void getMaisCurtidas(OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
				.setDomain(DOMAIN)
				.setPath("poetry/most_liked");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}
	 
	public void getMaisComentadas(OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
				.setDomain(DOMAIN)
				.setPath("poetry/most_commented");
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
		ObjectListID<Comentario> comentarios = new ObjectListID<Comentario>(json.getJSONArray("comments"));
		ObjectListID<Curtida> curtidas = new ObjectListID<Curtida>(json.getJSONArray("likes"));
		Poesia p = new Poesia(id, dataCriacao, titulo, (Usuario)params.get(0), autor, poesia, tags, comentarios, curtidas);
		long numCurtidas = json.getLong("liked");
		long numComments = json.getLong("commented");
		p.setNumCurtidas(numCurtidas);
		p.setNumComentarios(numComments); 
		return p;
	}
	 
}
