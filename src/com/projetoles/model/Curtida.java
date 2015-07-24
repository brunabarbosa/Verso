package com.projetoles.model;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class Curtida implements Comparable<Curtida> {

	private String mId;
	private Usuario mPostador;
	private Calendar mDataCriacao;
	
	public Curtida(String id, Usuario postador, Calendar dataCriacao) {
		setId(id);
		setPostador(postador);
		setDataCriacao(dataCriacao);
	}
	
	public void setPostador(Usuario postador) {
		if (postador == null) {
			throw new IllegalArgumentException("Autor é obrigatório.");
		}
		this.mPostador = postador;
	}
	
	public Usuario getPostador() {
		return mPostador;
	}
	
	public void setDataCriacao(Calendar dataCriacao) {
		this.mDataCriacao = dataCriacao;
	}
	
	public Calendar getDataCriacao() {
		return this.mDataCriacao;
	}

	public String getStringDataCriacao() {
		return String.valueOf(mDataCriacao.getTimeInMillis());
	}

	public void setId(String id) {
		this.mId = id;
	}
	
	public String getId() {
		return this.mId;
	}
	
	public static Curtida converteJson(Usuario postador, JSONObject json) throws JSONException {
		Long tempo = Long.valueOf(json.getString("dataCriacao"));
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tempo);
		String id = json.getString("id");
		return new Curtida(id, postador, c);
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

	@Override
	public int compareTo(Curtida arg0) {
		return arg0.mDataCriacao.compareTo(this.mDataCriacao);
	}
	
}
