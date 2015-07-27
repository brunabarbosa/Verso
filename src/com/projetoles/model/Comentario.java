package com.projetoles.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Comentario extends TemporalModel {

	public static final int TAMANHO_MAXIMO_COMENTARIO = 200;
	
	private String mComentario;
	private Usuario mPostador;
	private Poesia mPoesia;
	
	public static final Parcelable.Creator<Comentario> CREATOR = 
			new Parcelable.Creator<Comentario>() {
        public Comentario createFromParcel(Parcel in) {
            return new Comentario(in); 
        }

        public Comentario[] newArray(int size) {
            return new Comentario[size];
        }
    };

	public Comentario(Parcel in) {
		super(in);
		setComentario(in.readString());
		setPostador((Usuario)in.readParcelable(Usuario.class.getClassLoader()));
		setPoesia((Poesia)in.readParcelable(Poesia.class.getClassLoader()));
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.getComentario());
		dest.writeParcelable(this.getPostador(), flags);
		dest.writeParcelable(this.getPoesia(), flags);
	}

	public Comentario(String id, Calendar dataCriacao, String comentario, Usuario postador, Poesia poesia) {
		super(id, dataCriacao);
		setComentario(comentario);
		setPostador(postador);
		setPoesia(poesia);
	}
	
	public void setComentario(String comentario) {
		if (comentario == null || comentario.trim().isEmpty()) {
			throw new IllegalArgumentException("Comentário é obrigatório.");
		} else if (comentario.length() > TAMANHO_MAXIMO_COMENTARIO) {
			throw new IllegalArgumentException("Comentário excede o limite máximo de " + TAMANHO_MAXIMO_COMENTARIO + " caracteres.");
		}
		this.mComentario = comentario;
	}
	
	public String getComentario() {
		return this.mComentario;
	}
	
	public void setPostador(Usuario postador) {
		if (postador == null) {
			throw new IllegalArgumentException("Postador é obrigatório.");
		}
		this.mPostador = postador;
	}
	
	public Usuario getPostador() {
		return this.mPostador;
	}
	
	public void setPoesia(Poesia poesia) {
		if (poesia == null) {
			throw new IllegalArgumentException("Poesia é obrigatória.");
		}
		this.mPoesia = poesia;
	}
	
	public Poesia getPoesia() {
		return this.mPoesia;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mPostador == null) ? 0 : mPostador.hashCode());
		result = prime * result
				+ ((mComentario == null) ? 0 : mComentario.hashCode());
		result = prime * result + ((mDataCriacao == null) ? 0 : mDataCriacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comentario other = (Comentario) obj;
		if (mPostador == null) {
			if (other.mPostador != null)
				return false;
		} else if (!mPostador.equals(other.mPostador))
			return false;
		if (mComentario == null) {
			if (other.mComentario != null)
				return false;
		} else if (!mComentario.equals(other.mComentario))
			return false;
		if (mDataCriacao == null) {
			if (other.mDataCriacao != null)
				return false;
		} else if (!mDataCriacao.equals(other.mDataCriacao))
			return false;
		return true;
	}

	public String toString(){
		return mComentario;
		
	}

}
