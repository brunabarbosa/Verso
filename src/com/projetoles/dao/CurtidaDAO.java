package com.projetoles.dao;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Curtida;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;


public class CurtidaDAO extends DAO<Curtida> {

	private static CurtidaDAO sInstance;
	
	public static CurtidaDAO getInstance() {
		if (sInstance == null) {
			sInstance = new CurtidaDAO();
		}
		return sInstance;
	}

	@Override
	public void post(Curtida curtida, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("poesia", curtida.getPoesia().getId())
			.addParam("dataCriacao", curtida.getStringDataCriacao())
			.addParam("postador", curtida.getPostador().getId())
			.setDomain(DOMAIN)
			.setPath("like");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void delete(String id, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("unlike");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void get(String id, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("like");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	} 
 
	@Override
	public Curtida getFromJSON(JSONObject json, List<Object> params) throws JSONException {
		String id = json.getString("id");
		Calendar dataCriacao = CalendarUtils.stringToCalendar(json.getString("dataCriacao"));
		return new Curtida(id, dataCriacao, (Usuario)params.get(0), (Poesia)params.get(1));
	}

}
