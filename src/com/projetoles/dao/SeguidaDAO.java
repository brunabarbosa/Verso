package com.projetoles.dao;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;


public class SeguidaDAO extends DAO<Seguida> {

	private static SeguidaDAO sInstance;
	
	public static SeguidaDAO getInstance() {
		if (sInstance == null) {
			sInstance = new SeguidaDAO();
		}
		return sInstance;
	}

	@Override
	public void post(Seguida seguida, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("follower", seguida.getSeguidor().getId())
			.addParam("followed", seguida.getSeguido().getId())
			.addParam("date", seguida.getStringDataCriacao())
			.setDomain(DOMAIN)
			.setPath("follow/post");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void delete(String id, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("follow/delete");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void get(String id, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("follow/get");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	} 
 
	@Override
	public Seguida getFromJSON(JSONObject json, List<Object> params) throws JSONException {
		String id = json.getString("id");
		Calendar dataCriacao = CalendarUtils.stringToCalendar(json.getString("date"));
		return new Seguida(id, dataCriacao, (Usuario)params.get(0), (Usuario)params.get(1));
	}

}
