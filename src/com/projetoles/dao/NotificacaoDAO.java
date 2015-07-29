package com.projetoles.dao;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Notificacao;
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
			.addParam("chave", "APA91bHdoJVPghKuJKKc1kgrV9eCX3YGTT1YiC_hX-8VKRdkRfj3KfS-_2duFzWrIdbOyLHwdPqyJD8GOWeQFWsJW612NtaCVgTxV7Nvq31U-Ee-3Aj54Zwh0GrhFgoS8uYDMgLt4wME")
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
	public Notificacao getFromJSON(JSONObject json, List<Object> params) throws JSONException {
		String id = json.getString("id");
		Calendar dataCriacao = CalendarUtils.stringToCalendar(json.getString("dataCriacao"));
		String mensagem = json.getString("mensagem");
		return new Notificacao(id, dataCriacao, (Usuario)params.get(0), (Usuario)params.get(1), 
				mensagem);
	}

}
