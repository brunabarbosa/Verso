package com.projetoles.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Notificacao extends TemporalModel {
	
	private Usuario mEnderecado;
	private Usuario mTitulo;
	private String mMensagem;

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
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);		
		dest.writeParcelable(this.getEnderecado(), flags);
		dest.writeParcelable(this.getTitulo(), flags);
		dest.writeString(this.getMensagem());
	}

	public Notificacao(String id, Calendar dataCriacao, Usuario enderecado, Usuario titulo, 
			String mensagem) {
		super(id, dataCriacao);
		setTitulo(titulo);
		setMensagem(mensagem);
		setEnderecado(enderecado);
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

}
