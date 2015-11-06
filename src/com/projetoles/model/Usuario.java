package com.projetoles.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;


public class Usuario extends TemporalModel {

	private static final int TAMANHO_MAXIMO_NOME = 30;
	private static final int TAMANHO_MINIMO_SENHA = 6;
	private static final int TAMANHO_MAXIMO_SENHA = 20;
	private static final int TAMANHO_MAXIMO_EMAIL = 50;
	private static final int TAMANHO_MAXIMO_BIOGRAFIA = 500;
	private static final String EMAIL_REGEX = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$";

	private String mNome;
	private String mSenha;
	private byte[] mFoto;
	private String mBiografia;
	private ObjectListID<Poesia> mPoesias;
	private ObjectListID<Notificacao> mNotificacoes;
	private ObjectListID<Curtida> mCurtidas;
	private ObjectListID<Seguida> mSeguindo;
	private ObjectListID<Seguida> mSeguidores;
	private ObjectListID<Compartilhamento> mCompartilhamentos;
	private boolean mNotificacoesHabilitadas;
	private long mNumSeguidores;

	public static final Parcelable.Creator<Usuario> CREATOR = 
			new Parcelable.Creator<Usuario>() {
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in); 
        }

        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

	public Usuario(Parcel in) {
		super(in);
		setNome(in.readString()); 
		int fotoLength = in.readInt();
		byte[] foto = new byte[fotoLength];
		in.readByteArray(foto);
		setFoto(foto); 
		setBiografia(in.readString());
		setPoesias((ObjectListID)in.readParcelable(ObjectListID.class.getClassLoader()));
		setNotificacoes((ObjectListID)in.readParcelable(ObjectListID.class.getClassLoader()));
		setCurtidas((ObjectListID)in.readParcelable(ObjectListID.class.getClassLoader()));
		setSeguindo((ObjectListID)in.readParcelable(ObjectListID.class.getClassLoader()));
		setSeguidores((ObjectListID)in.readParcelable(ObjectListID.class.getClassLoader()));
		setCompartilhamentos((ObjectListID)in.readParcelable(ObjectListID.class.getClassLoader()));
		setNumSeguidores(in.readLong());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.getNome());
		dest.writeInt(this.getFoto().length);
		dest.writeByteArray(this.getFoto());
		dest.writeString(this.getBiografia());
		dest.writeParcelable(this.getPoesias(), flags);
		dest.writeParcelable(this.getNotificacoes(), flags);
		dest.writeParcelable(this.getCurtidas(), flags);
		dest.writeParcelable(this.getSeguindo(), flags);
		dest.writeParcelable(this.getSeguidores(), flags);
		dest.writeParcelable(this.getCompartilhamentos(), flags);
		dest.writeLong(this.getNumSeguidores());
	}
	
	public Usuario(String email, Calendar dataCriacao, String senha, String nome, String biografia, byte[] foto, 
			ObjectListID<Poesia> poesias, ObjectListID<Notificacao> notificacoes, ObjectListID<Curtida> curtidas,
			ObjectListID<Seguida> seguindo, ObjectListID<Seguida> seguidores, ObjectListID<Compartilhamento> compartilhamentos,
			boolean notificacoesHabilitadas) {
		super(email, dataCriacao);
		setSenha(senha);
		setNome(nome);
		setFoto(foto);
		setBiografia(biografia);
		setPoesias(poesias);
		setNotificacoes(notificacoes);
		setCurtidas(curtidas);
		setSeguindo(seguindo);
		setSeguidores(seguidores);
		setCompartilhamentos(compartilhamentos);
		setNotificacoesHabilitadas(notificacoesHabilitadas);
	}

	public Usuario(String email, Calendar dataCriacao, String nome, String biografia, byte[] foto,
			ObjectListID<Poesia> poesias, ObjectListID<Notificacao> notificacoes, ObjectListID<Curtida> curtidas,
			ObjectListID<Seguida> seguindo, ObjectListID<Seguida> seguidores, ObjectListID<Compartilhamento> compartilhamentos,
			boolean notificacoesHabilitadas) {
		this(email, dataCriacao, null, nome, biografia, foto, poesias, notificacoes, curtidas, seguindo, seguidores, compartilhamentos,
				notificacoesHabilitadas);
	}

	@Override
	public void setId(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("E-mail é obrigatório.");
		} else if (email.length() > TAMANHO_MAXIMO_EMAIL || !email.matches(EMAIL_REGEX)) {
			throw new IllegalArgumentException("E-mail inválido.");
		}
		super.setId(email);
	}

	public void setNome(String nome) throws IllegalArgumentException {
		if (nome != null) {
			if (nome.trim().isEmpty()) {
				throw new IllegalArgumentException("Nome é obrigatório.");
			} else if (nome.length() > TAMANHO_MAXIMO_NOME) {
				throw new IllegalArgumentException("Tamanho do nome excede o limite de " + TAMANHO_MAXIMO_NOME + " caracteres.");
			}
		}
		this.mNome = nome;
	}

	public String getNome() {
		return mNome;
	}

	public void setSenha(String password, boolean encrypted) {
		if (password != null) {
			if (password.trim().isEmpty()) {
				throw new IllegalArgumentException("Senha é obrigatória.");
			} else if (password.length() < TAMANHO_MINIMO_SENHA) {
				throw new IllegalArgumentException("Deve deve ter pelo menos " + TAMANHO_MINIMO_SENHA + " caracteres.");
			} else if (password.length() > TAMANHO_MAXIMO_SENHA) {
				throw new IllegalArgumentException("Tamanho da senha excede o limite de " + TAMANHO_MAXIMO_SENHA + " caracteres.");
			}
			if (encrypted) {
				this.mSenha = PasswordEncrypter.getEncryptedPassword(password);
			} else {
				this.mSenha = password;
			}
		}
	}

	public void setSenha(String password) {
		setSenha(password, true);
	}
	
	public String getSenha() {
		return mSenha;
	}

	public void setFoto(byte[] foto) {
		this.mFoto = foto;
	}
	
	public byte[] getFoto() {
		return this.mFoto;
	}
	
	public void setBiografia(String biografia) {
		if (biografia != null && biografia.length() > TAMANHO_MAXIMO_BIOGRAFIA) {
			throw new IllegalArgumentException("Biografia excede o tamanho máximo de " + TAMANHO_MAXIMO_BIOGRAFIA + " caracteres.");
		}
		this.mBiografia = biografia;
	}
	
	public String getBiografia() {
		return this.mBiografia;
	}
	
	public void setPoesias(ObjectListID<Poesia> poesias) {
		this.mPoesias = poesias;
	}
	 
	public ObjectListID<Poesia> getPoesias() {
		return this.mPoesias;
	}
	
	public void setNotificacoes(ObjectListID<Notificacao> notificacoes) {
		this.mNotificacoes = notificacoes;
	}
	
	public ObjectListID<Notificacao> getNotificacoes() {
		return this.mNotificacoes;
	}
	
	public void setCurtidas(ObjectListID<Curtida> curtidas) {
		this.mCurtidas = curtidas;
	}
	
	public ObjectListID<Curtida> getCurtidas() {
		return this.mCurtidas;
	}
	
	public void setSeguindo(ObjectListID<Seguida> seguindo) {
		this.mSeguindo = seguindo;
	}
	
	public ObjectListID<Seguida> getSeguindo() {
		return this.mSeguindo;
	}
	
	public void setSeguidores(ObjectListID<Seguida> seguidores) {
		this.mSeguidores = seguidores;
	}
	
	public ObjectListID<Seguida> getSeguidores() {
		return this.mSeguidores;
	}
	
	public void setCompartilhamentos(ObjectListID<Compartilhamento> compartilhamentos) {
		this.mCompartilhamentos = compartilhamentos;
	}
	
	public ObjectListID<Compartilhamento> getCompartilhamentos() {
		return this.mCompartilhamentos;
	}
	
	public boolean getNotificacoesHabilitadas() {
		return this.mNotificacoesHabilitadas;
	}
	
	public void setNotificacoesHabilitadas(boolean notificacoesHabilitadas) {
		this.mNotificacoesHabilitadas = notificacoesHabilitadas;
	}
	
	public long getNumSeguidores() {
		return this.mNumSeguidores;
	}
	
	public void setNumSeguidores(long numSeguidores) {
		this.mNumSeguidores = numSeguidores;
	}
	
	@Override
	public String toString() {
		return this.getNome();
	}
	
}
