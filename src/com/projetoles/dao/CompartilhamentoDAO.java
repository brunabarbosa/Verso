package com.projetoles.dao;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Compartilhamento;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;


public class CompartilhamentoDAO extends DAO<Compartilhamento> {

	private static CompartilhamentoDAO sInstance;
	
	public static CompartilhamentoDAO getInstance() {
		if (sInstance == null) {
			sInstance = new CompartilhamentoDAO();
		}
		return sInstance;
	}

	@Override
	public void post(Compartilhamento compartilhamento, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("poster", compartilhamento.getPostador().getId())
			.addParam("poetry", compartilhamento.getPoesia().getId())
			.addParam("date", compartilhamento.getStringDataCriacao())
			.setDomain(DOMAIN)
			.setPath("share/post");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void delete(String id, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("share/delete");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void get(String id, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("share/get");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	} 
 
	@Override
	public Compartilhamento getFromJSON(JSONObject json, List<Object> params) throws JSONException {
		String id = json.getString("id");
		Calendar dataCriacao = CalendarUtils.stringToCalendar(json.getString("date"));
		return new Compartilhamento(id, dataCriacao, (Usuario)params.get(0), (Poesia)params.get(1));
	}

}
