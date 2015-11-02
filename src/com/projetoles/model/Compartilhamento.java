package com.projetoles.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Compartilhamento extends TemporalModel {

	private Usuario mPostador;
	private Poesia mPoesia;
	
	public static final Parcelable.Creator<Compartilhamento> CREATOR = 
			new Parcelable.Creator<Compartilhamento>() {
        public Compartilhamento createFromParcel(Parcel in) {
            return new Compartilhamento(in); 
        }
 
        public Compartilhamento[] newArray(int size) {
            return new Compartilhamento[size];
        }
    };
    
	public Compartilhamento(Parcel in) {
		super(in);
		this.setPostador((Usuario)in.readParcelable(Usuario.class.getClassLoader()));
		this.setPoesia((Poesia)in.readParcelable(Poesia.class.getClassLoader()));
		if (!mPoesia.getCompartilhadoPor().contains(this)) {
			mPoesia.getCompartilhadoPor().add(this);
		}
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);	
		dest.writeParcelable(this.getPostador(), flags);
		dest.writeParcelable(this.getPoesia(), flags);
	}

	public Compartilhamento(String id, Calendar dataCriacao, Usuario postador, Poesia poesia) {
		super(id, dataCriacao);
		setPostador(postador);
		setPoesia(poesia);
		if (!poesia.getCompartilhadoPor().contains(this)) {
			poesia.getCompartilhadoPor().add(this);
		}
	}
	
	public Usuario getPostador() {
		return mPostador;
	}
	
	public void setPostador(Usuario postador) {
		if (postador == null) {
			throw new IllegalArgumentException("Postador é obrigatório.");
		}
		this.mPostador = postador;
	}
	
	public Poesia getPoesia() {
		return mPoesia;
	}
	
	public void setPoesia(Poesia poesia) {
		if (poesia == null) {
			throw new IllegalArgumentException("Poesia é obrigatória.");
		}
		this.mPoesia = poesia;
	}
	
}