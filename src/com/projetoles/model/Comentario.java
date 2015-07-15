package com.projetoles.model;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class Comentario implements Comparable<Comentario> {

	private String mComentario;
	private String mAutor;
	private Calendar mDataCriacao;
	
	public Comentario(String comentario, String autor, Calendar data) {
		setComentario(comentario);
		setAutor(autor);
		setDataCriacao(data);
	}
	
	public void setComentario(String comentario) {
		if (comentario == null || comentario.trim().isEmpty()) {
			throw new IllegalArgumentException("Comentário é obrigatório.");
		}
		this.mComentario = comentario;
	}
	
	public String getComentario() {
		return this.mComentario;
	}
	
	public void setAutor(String autor) {
		if (autor == null || autor.trim().isEmpty()) {
			throw new IllegalArgumentException("Autor é obrigatório.");
		}
		this.mAutor = autor;
	}
	
	public String getAutor() {
		return this.mAutor;
	}
	
	public void setDataCriacao(Calendar data) {
		this.mDataCriacao = data;
	}
	
	public Calendar getDataCriacao() {
		return this.mDataCriacao;
	}

	public static Comentario converteJson(JSONObject json) throws JSONException {
		String comentario = json.getString("comentario");
		Long tempo = Long.valueOf(json.getString("dataCriacao"));
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tempo);
		String postador = json.getString("postador");
		return new Comentario(comentario, postador, c);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mAutor == null) ? 0 : mAutor.hashCode());
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
		if (mAutor == null) {
			if (other.mAutor != null)
				return false;
		} else if (!mAutor.equals(other.mAutor))
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

	@Override
	public int compareTo(Comentario arg0) {
		return arg0.mDataCriacao.compareTo(this.mDataCriacao);
	}

}
