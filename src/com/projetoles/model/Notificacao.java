package com.projetoles.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Notificacao extends TemporalModel {
	
	private String mEnderecado;
	private String mTitulo;
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
		setEnderecado(in.readString());
		setTitulo(in.readString());
		setMensagem(in.readString());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);		
		dest.writeString(this.getEnderecado());
		dest.writeString(this.getTitulo());
		dest.writeString(this.getMensagem());
	}

	public Notificacao(String id, Calendar dataCriacao, String enderecado, String titulo, String mensagem) {
		super(id, dataCriacao);
		setTitulo(titulo);
		setMensagem(mensagem);
		setEnderecado(enderecado);
	}

	private void setEnderecado(String enderecado) {
		if (enderecado == null || enderecado.trim().isEmpty()) {
			throw new IllegalArgumentException("Enderecado é obrigatório.");
		}
		this.mEnderecado = enderecado;
		
	}

	public String getEnderecado() {
		return mEnderecado;
	}
	
	public void setTitulo(String mTitulo) {
		if (mTitulo == null || mTitulo.trim().isEmpty()) {
			throw new IllegalArgumentException("Título é obrigatório.");
		}
		this.mTitulo = mTitulo;
	}

	public String getTitulo() {
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

	@Override
	public String toString() {
		if ((getTitulo() != null && !getTitulo().trim().equals("")) && (getMensagem() != null && !getMensagem().trim().equals(""))) {
			return "";
		}
		return getTitulo() + ": " + getMensagem();
	}

}
