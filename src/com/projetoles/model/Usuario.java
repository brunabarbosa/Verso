package com.projetoles.model;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Classe responsável por guardar informações sobre o usuário
 */
public class Usuario {

	private static final int MAX_LENGTH_NAME = 30;
	private static final int MIN_LENGTH_PASSWORD = 6;
	private static final int MAX_LENGTH_PASSWORD = 20;
	private static final int MAX_LENGTH_EMAIL = 30;
	private static final String EMAIL_REGEX = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$";

	private String mEmail;
	private String mNome;
	private String mSenha;
	private byte[] mFoto;
	private String mBiografia;
	
	public Usuario(String email, String nome, String senha) 
			throws IllegalArgumentException {
		setEmail(email);
		setNome(nome);
		setSenha(senha);
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) throws IllegalArgumentException {
		if (email == null || email.trim().equals("")) {
			throw new IllegalArgumentException("E-mail é obrigatório.");
		} else if (email.length() > MAX_LENGTH_EMAIL || !email.matches(EMAIL_REGEX)) {
			throw new IllegalArgumentException("E-mail inválido.");
		}
		this.mEmail = email;
	}

	public String getNome() {
		return mNome;
	}

	public void setNome(String nome) throws IllegalArgumentException {
		if (nome != null && nome.length() > MAX_LENGTH_NAME) {
			throw new IllegalArgumentException("Tamanho do nome é maior que o permitido.");
		}
		this.mNome = nome;
	}

	public String getSenha() {
		return mSenha;
	}

	public void setSenha(String password) throws IllegalArgumentException {
		if (password != null) {
			if (password.trim().equals("")) {
				throw new IllegalArgumentException("Senha é obrigatória.");
			} else if (password.length() < MIN_LENGTH_PASSWORD) {
				throw new IllegalArgumentException("Tamanho da senha é menor que o permitido.");
			} else if (password.length() > MAX_LENGTH_PASSWORD) {
				throw new IllegalArgumentException("Tamanho da senha é maior que o permitido.");
			}
			this.mSenha = PasswordEncrypter.getEncryptedPassword(password);
		}
	}

	public void setFoto(byte[] foto) {
		this.mFoto = foto;
	}
	
	public byte[] getFoto() {
		return this.mFoto;
	}
	
	public void setBiografia(String biografia) {
		this.mBiografia = biografia;
	}
	
	public String getBiografia() {
		return this.mBiografia;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		return other.getEmail().equals(this.getEmail());
	}

	@Override
	public String toString() {
		if (mNome != null && !mNome.trim().equals("")) {
			return mNome.split(" ")[0];
		}
		return mEmail;
	}
	
	public static Usuario converteJSON(JSONObject obj) throws JSONException {
		String email = obj.getString("email");
		String nome = obj.getString("name");
		String bio = obj.getString("bio");
		String fotoEncoded = obj.getString("foto");
		byte[] foto = {};
		if (!fotoEncoded.equals("undefined"))
			foto = ImageEncoder.decode(obj.getString("foto"));
		Usuario u = new Usuario(email, nome, null);
		if (!bio.equals("undefined"))
			u.setBiografia(bio);
		if (foto.length > 0)
			u.setFoto(foto);
		return u;
	}
	
}
