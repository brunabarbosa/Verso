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
 * Classe respons�vel por guardar informa��es sobre o usu�rio
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
	private Set<String> mPoemas;
	private Set<Poesia> mPoemasCarregados;
	
	public Usuario(Parcel in) {
		setEmail(in.readString()); 
		setNome(in.readString()); 
		int fotoLength = in.readInt();
		byte[] foto = new byte[fotoLength];
		setFoto(foto);
		in.readByteArray(foto);
		setBiografia(in.readString());
		this.mPoemas = new HashSet<String>();
		this.mPoemasCarregados = new HashSet<Poesia>();
	}
	
	public Usuario(String email, String nome, String senha) 
			throws IllegalArgumentException {
		setEmail(email);
		setNome(nome);
		setSenha(senha);
		setFoto(new byte[]{});
		setBiografia("");
		this.mPoemas = new HashSet<String>();
		this.mPoemasCarregados = new HashSet<Poesia>();
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) throws IllegalArgumentException {
		if (email == null || email.trim().equals("")) {
			throw new IllegalArgumentException("E-mail � obrigat�rio.");
		} else if (email.length() > MAX_LENGTH_EMAIL || !email.matches(EMAIL_REGEX)) {
			throw new IllegalArgumentException("E-mail inv�lido.");
		}
		this.mEmail = email;
	}

	public String getNome() {
		return mNome;
	}

	public void setNome(String nome) throws IllegalArgumentException {
		if (nome != null && nome.length() > MAX_LENGTH_NAME) {
			throw new IllegalArgumentException("Tamanho do nome � maior que o permitido.");
		}
		this.mNome = nome;
	}

	public String getSenha() {
		return mSenha;
	}

	public void setSenha(String password) throws IllegalArgumentException {
		if (password != null) {
			if (password.trim().equals("")) {
				throw new IllegalArgumentException("Senha � obrigat�ria.");
			} else if (password.length() < MIN_LENGTH_PASSWORD) {
				throw new IllegalArgumentException("Tamanho da senha � menor que o permitido.");
			} else if (password.length() > MAX_LENGTH_PASSWORD) {
				throw new IllegalArgumentException("Tamanho da senha � maior que o permitido.");
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
	
	public void addPoema(String poemaId) {
		this.mPoemas.add(poemaId);
	}
	
	public void addPoemaCarregado(Poesia poema) {
		this.mPoemasCarregados.add(poema);
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
		JSONArray poemas = obj.getJSONArray("poemas");
		for (int i = 0; i < poemas.length(); i++) {
			String id = poemas.get(i).toString();
			u.addPoema(id);
		}
		return u;
	}
	
	public Set<Poesia> getPoemasCarregados() {
		return Collections.unmodifiableSet(mPoemasCarregados);
	}
	
	public Set<String> getPoemas() {
		return Collections.unmodifiableSet(mPoemas);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.mEmail);
		dest.writeString(this.mNome);
		dest.writeInt(this.mFoto.length);
		dest.writeByteArray(this.mFoto);
		dest.writeString(this.mBiografia);
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
