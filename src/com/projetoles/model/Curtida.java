package com.projetoles.model;

import java.util.Calendar;

public class Curtida implements Comparable<Curtida> {

	private String mPostador;
	private Calendar mDataCriacao;
	
	public Curtida(String postador, Calendar dataCriacao) {
		setPostador(postador);
		setDataCriacao(dataCriacao);
	}
	
	public void setPostador(String postador) {
		if (postador == null || postador.trim().isEmpty()) {
			throw new IllegalArgumentException("Autor é obrigatório.");
		}
		this.mPostador = postador;
	}
	
	public String getPostador() {
		return mPostador;
	}
	
	public void setDataCriacao(Calendar dataCriacao) {
		this.mDataCriacao = dataCriacao;
	}
	
	public Calendar getDataCriacao() {
		return this.mDataCriacao;
	}

	public String getStringDataCriacao(){
		return String.valueOf(mDataCriacao.getTimeInMillis());
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
