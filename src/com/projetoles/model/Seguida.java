package com.projetoles.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;


public class Seguida extends TemporalModel {

	private Usuario mSeguidor;
	private Usuario mSeguido;

	public static final Parcelable.Creator<Seguida> CREATOR = 
			new Parcelable.Creator<Seguida>() {
        public Seguida createFromParcel(Parcel in) {
            return new Seguida(in); 
        }
 
        public Seguida[] newArray(int size) {
            return new Seguida[size];
        }
    };
    
	public Seguida(Parcel in) {
		super(in);
		this.setSeguidor((Usuario)in.readParcelable(Usuario.class.getClassLoader()));
		this.setSeguido((Usuario)in.readParcelable(Usuario.class.getClassLoader()));
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);	
		dest.writeParcelable(this.getSeguidor(), flags);
		dest.writeParcelable(this.getSeguido(), flags);
	}

	public Seguida(String id, Calendar dataCriacao, Usuario seguidor, Usuario seguido) {
		super(id, dataCriacao);
		setSeguidor(seguidor);
		setSeguido(seguido);
	}
	
	public void setSeguidor(Usuario seguidor) {
		if (seguidor == null) {
			throw new IllegalArgumentException("Seguidor é obrigatório.");
		}
		this.mSeguidor = seguidor;
	}
	
	public Usuario getSeguidor() {
		return this.mSeguidor;
	}
	
	public void setSeguido(Usuario seguido) {
		if (seguido == null) {
			throw new IllegalArgumentException("Seguido é obrigatório.");
		}
		this.mSeguido = seguido;
	}
	
	public Usuario getSeguido() {
		return this.mSeguido;
	}

}
