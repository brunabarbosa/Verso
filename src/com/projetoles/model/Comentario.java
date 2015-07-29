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
	public String toString(){
		return mComentario;
		
	}

}
