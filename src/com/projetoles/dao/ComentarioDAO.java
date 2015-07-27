package com.projetoles.dao;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Comentario;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

public class ComentarioDAO extends DAO<Comentario> {

	private static ComentarioDAO sInstance;
	
	public static ComentarioDAO getInstance() {
		if (sInstance == null) {
			sInstance = new ComentarioDAO();
		}
		return sInstance;
	}

	@Override
	public void post(Comentario comentario, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("poesia", comentario.getPoesia().getId())
			.addParam("comentario", comentario.getComentario())
			.addParam("dataCriacao", comentario.getStringDataCriacao())
			.addParam("postador", comentario.getPostador().getId())
			.setDomain(DOMAIN)
			.setPath("comment");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void get(String id, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("comment");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}

	@Override
	public Comentario getFromJSON(JSONObject json, List<Object> params) throws JSONException {
		String id = json.getString("id");
		Calendar dataCriacao = CalendarUtils.stringToCalendar(json.getString("dataCriacao"));
		String comentario = json.getString("comentario");
		return new Comentario(id, dataCriacao, comentario, (Usuario)params.get(0), (Poesia)params.get(1));
	}

}
