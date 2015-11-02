package com.projetoles.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Poesia extends TemporalModel {
	
	private static final int TAMANHO_MAXIMO_TITULO = 30;
	private static final int TAMANHO_MAXIMO_AUTOR = 30;
	private static final int TAMANHO_MAXIMO_TAGS = 150;
	private static final int TAMANHO_MAXIMO_POESIA = 5000;
	
	private String mTitulo;
	private Usuario mPostador;
	private String mAutor;
	private String mPoesia;
	private String mTags;
	private ObjectListID<Comentario> mComentarios;
	private ObjectListID<Curtida> mCurtidas;
	private List<Compartilhamento> mCompartilhadoPor = new ArrayList<Compartilhamento>();
	private long mNumCurtidas;
	private long mNumComentarios;
	
	public static final Parcelable.Creator<Poesia> CREATOR = 
			new Parcelable.Creator<Poesia>() {
        public Poesia createFromParcel(Parcel in) {
            return new Poesia(in); 
        }

        public Poesia[] newArray(int size) {
            return new Poesia[size];
        }
    };
    
	public Poesia(Parcel in) {
		super(in);
		setTitulo(in.readString());
		setPostador((Usuario)in.readParcelable(Usuario.class.getClassLoader()));
		setAutor(in.readString());
		setPoesia(in.readString());
		setTags(in.readString());
		setComentarios((ObjectListID)in.readParcelable(ObjectListID.class.getClassLoader()));
		setCurtidas((ObjectListID)in.readParcelable(ObjectListID.class.getClassLoader()));
		setNumCurtidas(in.readLong());
		setNumComentarios(in.readLong());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);		
		dest.writeString(this.getTitulo());
		dest.writeParcelable(this.getPostador(), flags);
		dest.writeString(this.getAutor());
		dest.writeString(this.getPoesia());
		dest.writeString(this.getTags());
		dest.writeParcelable(this.getComentarios(), flags);
		dest.writeParcelable(this.getCurtidas(), flags);
		dest.writeLong(this.getNumCurtidas());
		dest.writeLong(this.getNumComentarios());
	}

	public Poesia(String id, Calendar dataCriacao, String titulo, Usuario postador, String autor, String poesia,
			String tags, ObjectListID<Comentario> comentarios, ObjectListID<Curtida> curtidas) {
		super(id, dataCriacao);
		setTitulo(titulo);
		setPostador(postador);
		setAutor(autor);
		setPoesia(poesia);
		setDataCriacao(dataCriacao);
		setTags(tags);
		setComentarios(comentarios);
		setCurtidas(curtidas);
	}

	public Poesia() {
		super();
	}

	public void setTitulo(String titulo) {
		if (titulo == null || titulo.trim().isEmpty()) {
			throw new IllegalArgumentException("Título é obrigatório.");
		} else if (titulo.length() > TAMANHO_MAXIMO_TITULO) {
			throw new IllegalArgumentException("Título excede o tamanho máximo.");
		}
		this.mTitulo = titulo;
	}

	public String getTitulo() {
		return mTitulo;
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
	
	public void setAutor(String autor) {
		if (autor.length() > TAMANHO_MAXIMO_AUTOR) {
			throw new IllegalArgumentException("Autor excede o tamanho máximo.");
		}
		this.mAutor = autor;
	}

	public String getAutor() {
		return mAutor;
	}
	
	public void setPoesia(String poesia) {
		if (poesia == null || poesia.trim().isEmpty()) {
			throw new IllegalArgumentException("É preciso escrever algo antes de publicar.");
		} else if (poesia.length() > TAMANHO_MAXIMO_POESIA) {
			throw new IllegalArgumentException("Poesia excede o limite máximo de " + TAMANHO_MAXIMO_POESIA + " caracteres.");
		}
		this.mPoesia = poesia;
	}

	public String getPoesia() {
		return mPoesia;
	}
	
	public void setTags(String tags) {
		if (tags != null && tags.length() > TAMANHO_MAXIMO_TAGS) {
			throw new IllegalArgumentException("Tags excede o tamanho máximo.");
		}
		this.mTags = tags;
	}

	public String getTags() {
		return mTags;
	}
	
	public void setComentarios(ObjectListID<Comentario> comentarios) {
		this.mComentarios = comentarios;
	}
	
	public ObjectListID<Comentario> getComentarios() {
		return this.mComentarios;
	}
	 
	public void setCurtidas(ObjectListID<Curtida> curtidas) {
		this.mCurtidas = curtidas;
	}
	
	public ObjectListID<Curtida> getCurtidas() {
		return this.mCurtidas;
	}
 
	public List<Compartilhamento> getCompartilhadoPor() {
		return this.mCompartilhadoPor;
	}
	
	public boolean isCompartilhado(Usuario usuario) {
		for (Compartilhamento compartilhamento : mCompartilhadoPor) {
			if (compartilhamento.getPostador().equals(usuario)) return true;
		}
		return false;
	}
	
	public long getNumCurtidas() {
		return mNumCurtidas;
	}
	
	public void setNumCurtidas(long numCurtidas) {
		this.mNumCurtidas = numCurtidas;
	}
	
	public long getNumComentarios() {
		return mNumComentarios;
	}
	
	public void setNumComentarios(long numComentarios) {
		this.mNumComentarios = numComentarios;
	}
	
}
