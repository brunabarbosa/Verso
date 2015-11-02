package com.projetoles.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Notificacao extends TemporalModel {
	
	private Usuario mEnderecado;
	private Usuario mTitulo;
	private String mMensagem;
	private Poesia mPoesia;
	private String mTipo;

	public static final Parcelable.Creator<Notificacao> CREATOR = 
			new Parcelable.Creator<Notificacao>() {
        public Notificacao createFromParcel(Parcel in) {
            return new Notificacao(in); 
        }

        public Notificacao[] newArray(int size) {
            return new Notificacao[size];
        }
    };
    
	public Notificacao(Parcel in) {
		super(in);
		setEnderecado((Usuario)in.readParcelable(Usuario.class.getClassLoader()));
		setTitulo((Usuario)in.readParcelable(Usuario.class.getClassLoader()));
		setMensagem(in.readString());
		setTipo(in.readString());
		setPoesia((Poesia)in.readParcelable(Usuario.class.getClassLoader()));
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);		
		dest.writeParcelable(this.getEnderecado(), flags);
		dest.writeParcelable(this.getTitulo(), flags);
		dest.writeString(this.getMensagem());
		dest.writeParcelable(this.getPoesia(), flags);
		dest.writeString(this.getTipo());
	}

	public Notificacao(String id, Calendar dataCriacao, Usuario enderecado, Usuario titulo, 
			String mensagem, Poesia poesia, String tipo) {
		super(id, dataCriacao);
		setTitulo(titulo);
		setMensagem(mensagem);
		setEnderecado(enderecado);
		setTipo(tipo);
		setPoesia(poesia);
	}

	private void setEnderecado(Usuario enderecado) {
		if (enderecado == null) {
			throw new IllegalArgumentException("Enderecado é obrigatório.");
		}
		this.mEnderecado = enderecado;
		
	}

	public Usuario getEnderecado() {
		return mEnderecado;
	}
	
	public void setTitulo(Usuario mTitulo) {
		if (mTitulo == null) {
			throw new IllegalArgumentException("Título é obrigatório.");
		}
		this.mTitulo = mTitulo;
	}

	public Usuario getTitulo() {
		return mTitulo;
	}

	public void setMensagem(String mMensagem) {
		if (mMensagem == null || mMensagem.trim().isEmpty()) {
			throw new IllegalArgumentException("Mensagem é obrigatória.");
		}
		this.mMensagem = mMensagem;
	}

	public String getMensagem() {
		return mMensagem;
	}

	public Poesia getPoesia() {
		return mPoesia;
	}

	public void setPoesia(Poesia mPoesia) {
		if (mPoesia == null && mTipo.equals("poesia")) {
			throw new IllegalArgumentException("Poesia é obrigatória.");
		}
		this.mPoesia = mPoesia;
	}
	
	public void setTipo(String tipo) {
		if (tipo == null || tipo.trim().isEmpty()) {
			throw new IllegalArgumentException("Tipo é obrigatório.");
		}
		this.mTipo = tipo;
	}

	public String getTipo() {
		return mTipo;
	}

}
