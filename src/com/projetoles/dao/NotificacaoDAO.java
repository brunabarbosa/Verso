package com.projetoles.dao;

import com.projetoles.model.Notificacao;


public class NotificacaoDAO extends DAO{
	private static NotificacaoDAO sInstance;
	
	public static NotificacaoDAO getInstance() {
		if (sInstance == null) {
			sInstance = new NotificacaoDAO();
		}
		return sInstance;
	}
	
	public void criarNotificacao(Notificacao notificacao, OnRequestListener callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("titulo", notificacao.getEnderecado())
			.addParam("titulo", notificacao.getTitulo())
			.addParam("dataCriacao", notificacao.getStringDataCriacao())
			.addParam("mensagem", notificacao.getMensagem())
			.setDomain(DOMAIN)
			.setPath("notif");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	public void getNotificacao(String id, OnRequestListener callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("id", id)
			.setDomain(DOMAIN)
			.setPath("notif");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}
	
}
