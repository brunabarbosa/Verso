package com.projetoles.dao;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Notificacao;


public class NotificacaoDAO extends DAO<Notificacao> {
	
	private static NotificacaoDAO sInstance;
	
	public static NotificacaoDAO getInstance() {
		if (sInstance == null) {
			sInstance = new NotificacaoDAO();
		}
		return sInstance;
	}
	
	@Override
	public void post(Notificacao notificacao, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("enderecado", notificacao.getEnderecado())
			.addParam("titulo", notificacao.getTitulo())
			.addParam("mensagem", notificacao.getMensagem())
			.addParam("dataCriacao", notificacao.getStringDataCriacao())
			.setDomain(DOMAIN)
			.setPath("notif");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void get(String id, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("notif");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}

	@Override
	public Notificacao getFromJSON(JSONObject json, List<Object> params) throws JSONException {
		String id = json.getString("id");
		Calendar dataCriacao = CalendarUtils.stringToCalendar(json.getString("dataCriacao"));
		String enderecado = json.getString("enderecado");
		String titulo = json.getString("titulo");
		String mensagem = json.getString("mensagem");
		return new Notificacao(id, dataCriacao, enderecado, titulo, mensagem);
	}

}
