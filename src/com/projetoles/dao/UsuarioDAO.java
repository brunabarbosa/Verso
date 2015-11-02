package com.projetoles.dao;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.dao.POST.Builder;
import com.projetoles.model.Compartilhamento;
import com.projetoles.model.Curtida;
import com.projetoles.model.ImageUtils;
import com.projetoles.model.Notificacao;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

/**
 * Implementação do DAO usando um Webservice e requisições POST e GET
 */
public class UsuarioDAO extends DAO<Usuario> {

	private static UsuarioDAO sInstance;

	public static UsuarioDAO getInstance() {
		if (sInstance == null) {
			sInstance = new UsuarioDAO();
		}
		return sInstance;
	}

	public void login(Usuario usuario, String regId, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("email", usuario.getId())
			.addParam("password", usuario.getSenha())
			.addParam("regid", regId)
			.setDomain(DOMAIN)
			.setPath("user/get");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}

	@Override
	public void get(String email, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("email", email)
			.addParam("password", "")
			.setDomain(DOMAIN)
			.setPath("user/get");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}

	public void setFoto(Usuario usuario, byte[] photo, OnRequestListener<String> callback) {
		try {
			POST.Builder postRequest = (Builder) ((POST.Builder) (new POST.Builder()
				.addParam("email", usuario.getId())))
				.addPhoto(photo)
				.setDomain(DOMAIN)
				.setPath("user/photo_upload");
			POST post = (POST) postRequest.create();
			post.execute(callback);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callback.onError(e.getMessage());
		}
	}
	
	@Override
	public void post(Usuario usuario, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("email", usuario.getId())
			.addParam("date", usuario.getStringDataCriacao())
			.addParam("password", usuario.getSenha())
			.addParam("name", usuario.getNome())
			.setDomain(DOMAIN)
			.setPath("user/post");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void put(Usuario usuario, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("email", usuario.getId())
			.addParam("name", usuario.getNome())
			.addParam("bio", usuario.getBiografia())
			.addParam("password", usuario.getSenha() == null ? "" : usuario.getSenha())
			.setDomain(DOMAIN)
			.setPath("user/put");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}

	@Override
	public void update(Usuario usuario, OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
			.addParam("email", usuario.getId())
			.setDomain(DOMAIN)
			.setPath("user/update");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	} 
	
	public void setNotificacoes(Usuario usuario, boolean notificacoes, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("email", usuario.getId())
			.addParam("notifications", notificacoes ? "1" : "0")
			.setDomain(DOMAIN)
			.setPath("user/notifications");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	public void getMaisSeguidos(OnRequestListener<String> callback) {
		GET.Builder getRequest = (GET.Builder) new GET.Builder()
				.setDomain(DOMAIN)
				.setPath("user/most_followed");
		GET get = (GET) getRequest.create();
		get.execute(callback);
	}
	 
	@Override
	public Usuario getFromJSON(JSONObject obj, List<Object> params) throws JSONException {
		String email = obj.getString("email");
		long data = obj.getLong("date");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(data);
		String nome = obj.getString("name");
		byte[] foto = {};
		if (obj.has("photo")) {
			foto = ImageUtils.decode(obj.getString("photo"));
		}
		String biografia = "";
		if (obj.has("bio")) {
			biografia = obj.getString("bio");
		}
		ObjectListID<Poesia> poesias = new ObjectListID<Poesia>(obj.getJSONArray("poetries"));
		ObjectListID<Notificacao> notificacoes = new ObjectListID<Notificacao>(obj.getJSONArray("notifications"));
		ObjectListID<Curtida> curtidas = new ObjectListID<Curtida>(obj.getJSONArray("likes"));
		ObjectListID<Seguida> seguindo = new ObjectListID<Seguida>(obj.getJSONArray("followeds"));
		ObjectListID<Seguida> seguidores = new ObjectListID<Seguida>(obj.getJSONArray("followers"));
		ObjectListID<Compartilhamento> compartilhamentos = new ObjectListID<Compartilhamento>(obj.getJSONArray("shares"));
		boolean notificacoesHabilitadas = obj.getBoolean("enable_notifications");
		Usuario usuario = new Usuario(email, c, nome, biografia, foto, poesias, notificacoes, curtidas, seguindo, seguidores, compartilhamentos, notificacoesHabilitadas);
		long numSeguidores = obj.getLong("followed");
		usuario.setNumSeguidores(numSeguidores);
		return usuario;
	}

}