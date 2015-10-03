package com.projetoles.dao;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Notificacao;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;


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
			.addParam("enderecado", notificacao.getEnderecado().getId())
			.addParam("titulo", notificacao.getTitulo().getId())
			.addParam("mensagem", notificacao.getMensagem())
			.addParam("dataCriacao", notificacao.getStringDataCriacao())
			.addParam("chave", "APA91bGMpABcm-4ilpI1NltzH2DNia1qFENGiD04fsjxkd8B2A0C9OaTTUA1OVG6mWDJXd0QeadOQvCiPXWBJWKHoODGqdu5HuWQ1g0BPpP-Cjqc0LETbFgYRhWiCq6Ujz_E1FW5KJxd")
			.addParam("poesia", notificacao.getPoesia().getId())
			.addParam("tipo", notificacao.getTipo())
			.setDomain(DOMAIN)
			.setPath("notif/post");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void get(String id, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("notif/get");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}

	@Override
	public void delete(String id, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("notif/delete");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public Notificacao getFromJSON(JSONObject json, List<Object> params) throws JSONException {
		String id = json.getString("id");
		Calendar dataCriacao = CalendarUtils.stringToCalendar(json.getString("date"));
		String mensagem = json.getString("mensagem");
		String tipo = json.getString("tipo");
		return new Notificacao(id, dataCriacao, (Usuario)params.get(0), (Usuario)params.get(1), mensagem, (Poesia) params.get(2), tipo);
	}

}
