package com.projetoles.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;


public class Curtida extends TemporalModel {

	private Usuario mPostador;
	private Poesia mPoesia;

	public static final Parcelable.Creator<Curtida> CREATOR = 
			new Parcelable.Creator<Curtida>() {
        public Curtida createFromParcel(Parcel in) {
            return new Curtida(in); 
        }
 
        public Curtida[] newArray(int size) {
            return new Curtida[size];
        }
    };
    
	public Curtida(Parcel in) {
		super(in);
		this.setPostador((Usuario)in.readParcelable(Usuario.class.getClassLoader()));
		this.setPoesia((Poesia)in.readParcelable(Poesia.class.getClassLoader()));
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);	
		dest.writeParcelable(this.getPostador(), flags);
		dest.writeParcelable(this.getPoesia(), flags);
	}

	public Curtida(String id, Calendar dataCriacao, Usuario postador, Poesia poesia) {
		super(id, dataCriacao);
		setPostador(postador);
		setPoesia(poesia);
	}
	
	public void setPostador(Usuario postador) {
		if (postador == null) {
			throw new IllegalArgumentException("Postador é obrigatório.");
		}
		this.mPostador = postador;
	}
	
	public Usuario getPostador() {
		return mPostador;
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
		result = prime * result
				+ ((mPostador == null) ? 0 : mPostador.hashCode());
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
		Curtida other = (Curtida) obj;
		if (mPostador == null) {
			if (other.mPostador != null)
				return false;
		} else if (!mPostador.equals(other.mPostador))
			return false;
		return true;
	}

}
