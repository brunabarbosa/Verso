package com.projetoles.dao;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.dao.POST.Builder;
import com.projetoles.model.ImageUtils;
import com.projetoles.model.ObjectListID;
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

	public void auth(Usuario usuario, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("email", usuario.getId())
			.addParam("password", usuario.getSenha())
			.setDomain(DOMAIN)
			.setPath("auth");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}

	public void setFoto(Usuario usuario, byte[] photo, OnRequestListener<String> callback) {
		try {
			POST.Builder postRequest = (Builder) ((POST.Builder) (new POST.Builder()
				.addParam("email", usuario.getId())))
				.addPhoto(photo)
				.setDomain(DOMAIN)
				.setPath("photo_upload");
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
			.addParam("password", usuario.getSenha())
			.addParam("name", usuario.getNome())
			.setDomain(DOMAIN)
			.setPath("user");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}
	
	@Override
	public void get(String email, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("email", email)
			.addParam("password", "-1")
			.setDomain(DOMAIN)
			.setPath("auth");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}

	@Override
	public void put(Usuario usuario, OnRequestListener<String> callback) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
			.addParam("email", usuario.getId())
			.addParam("nome", usuario.getNome())
			.addParam("bio", usuario.getBiografia())
			.addParam("senha", usuario.getSenha() == null ? "" : usuario.getSenha())
			.setDomain(DOMAIN)
			.setPath("edit_user");
		POST post = (POST) postRequest.create();
		post.execute(callback);
	}

	@Override
	public Usuario getFromJSON(JSONObject obj, List<Object> params) throws JSONException {
		String email = obj.getString("email");
		String nome = obj.getString("name");
		String bio = obj.getString("bio");
		String fotoEncoded = obj.getString("foto");
		byte[] foto = {};
		if (!fotoEncoded.equals("undefined")) {
			foto = ImageUtils.decode(obj.getString("foto"));
		}
		String biografia = "";
		if (!bio.equals("undefined")) {
			biografia = bio;
		}
		ObjectListID poesias = new ObjectListID(obj.getJSONArray("poesias"));
		ObjectListID notificacoes = new ObjectListID(obj.getJSONArray("notificacoes"));
		ObjectListID curtidas = new ObjectListID(obj.getJSONArray("curtidas"));
		return new Usuario(email, nome, biografia, foto, poesias, notificacoes, curtidas);
	}

}