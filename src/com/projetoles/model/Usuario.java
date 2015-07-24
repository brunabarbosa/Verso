package com.projetoles.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Classe responsável por guardar informações sobre o usuário
 */
public class Usuario implements Parcelable {

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
	private Set<String> mPoesias;
	private Set<Poesia> mPoesiasCarregadas;
	private Set<String> mNoticacoes;
	private Set<Notificacao> mNoticacoesCarregadas;
	private Set<String> mCurtidas;
	
	public Usuario(Parcel in) {
		setEmail(in.readString()); 
		setNome(in.readString()); 
		int fotoLength = in.readInt();
		byte[] foto = new byte[fotoLength];
		setFoto(foto);
		in.readByteArray(foto);
		setBiografia(in.readString());
		this.mPoesias = new HashSet<String>();
		this.mPoesiasCarregadas = new HashSet<Poesia>();
		this.mNoticacoes = new HashSet<String>();
		this.mNoticacoesCarregadas = new HashSet<Notificacao>();
		this.mCurtidas = new HashSet<String>();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.mEmail);
		dest.writeString(this.mNome);
		dest.writeInt(this.mFoto.length);
		dest.writeByteArray(this.mFoto);
		dest.writeString(this.mBiografia);
	}
	
	public Usuario(String email, String nome, String senha) 
			throws IllegalArgumentException {
		setEmail(email);
		setNome(nome);
		setSenha(senha);
		setFoto(new byte[]{});
		setBiografia("");
		this.mPoesias = new HashSet<String>();
		this.mPoesiasCarregadas = new HashSet<Poesia>();
		this.mNoticacoes = new HashSet<String>();
		this.mNoticacoesCarregadas = new HashSet<Notificacao>();
		this.mCurtidas = new HashSet<String>();
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
	
	public void addPoesia(String poesia) {
		this.mPoesias.add(poesia);
	}
	
	public void addPoesiaCarregada(Poesia poesia) {
		this.mPoesiasCarregadas.add(poesia);
	}
	
	public void addNotifiacaoCarregada(Notificacao notificacao) {
		this.mNoticacoesCarregadas.add(notificacao);
	}
	
	public void addCurtida(String id) {
		this.mCurtidas.add(id);
	}
	
	public void removeCurtida(String id) {
		this.mCurtidas.remove(id);
	}
	
	public Set<String> getCurtidas() {
		return Collections.unmodifiableSet(this.mCurtidas);
	}
	
	public static Usuario converteJSON(JSONObject obj) throws JSONException {
		String email = obj.getString("email");
		String nome = obj.getString("name");
		String bio = obj.getString("bio");
		String fotoEncoded = obj.getString("foto");
		byte[] foto = {};
		if (!fotoEncoded.equals("undefined"))
			foto = ImageUtils.decode(obj.getString("foto"));
		Usuario u = new Usuario(email, nome, null);
		if (!bio.equals("undefined"))
			u.setBiografia(bio);
		if (foto.length > 0)
			u.setFoto(foto);
		JSONArray poemas = obj.getJSONArray("poesias");
		for (int i = 0; i < poemas.length(); i++) {
			String id = poemas.getString(i);
			u.addPoesia(id);
		}
		JSONArray notificacoes = obj.getJSONArray("notificacoes");
		for (int i = 0; i < notificacoes.length(); i++) {
			u.addNotifiacao(notificacoes.getString(i));
		}
		JSONArray curtidas = obj.getJSONArray("curtidas");
		for (int i = 0; i < curtidas.length(); i++) {
			String id = curtidas.getString(i);
			u.addCurtida(id);
		}
		return u;
	}
	
	private void addNotifiacao(String string) {
		this.mNoticacoes.add(string);
		
	}

	public Set<Poesia> getPoesiasCarregadas() {
		return Collections.unmodifiableSet(mPoesiasCarregadas);
	}
	
	public Set<String> getPoesias() {
		return Collections.unmodifiableSet(mPoesias);
	}
	
	public Set<String> getNotificacaoes() {
		return Collections.unmodifiableSet(mNoticacoes);
	}
	
	public Set<Notificacao> getNotificacaoesCarregadas() {
		return Collections.unmodifiableSet(mNoticacoesCarregadas);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Usuario> CREATOR = 
			new Parcelable.Creator<Usuario>() {
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in); 
        }

        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

}
