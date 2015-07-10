package com.projetoles.model;

import java.util.Calendar;

public class Notificacao {
	
	
	private String mTitulo;
	private String mMensagem;
	private Calendar mDataDeCriacao;
	
	public Notificacao(String titulo, String mensagem, Calendar dataDeCriacao) {
		setTitulo(titulo);
		setMensagem(mensagem);
		setDataDeCriacao(dataDeCriacao);
	}

	public String getTitulo() {
		return mTitulo;
	}

	public void setTitulo(String mTitulo) {
		this.mTitulo = mTitulo;
	}

	public String getMensagem() {
		return mMensagem;
	}

	public void setMensagem(String mMensagem) {
		this.mMensagem = mMensagem;
	}

	public Calendar getDataDeCriacao() {
		return mDataDeCriacao;
	}

	public void setDataDeCriacao(Calendar mDataDeCriacao) {
		this.mDataDeCriacao = mDataDeCriacao;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Notificacao))
			return false;
		Notificacao other = (Notificacao) obj;
		return other.getTitulo().equals(this.getTitulo()) && other.getMensagem().equals(this.getMensagem());
	}

	@Override
	public String toString() {
		if ((getTitulo() != null && !getTitulo().trim().equals("")) && (getMensagem() != null && !getMensagem().trim().equals(""))) {
			return "";
		}
		return getTitulo() + ": " + getMensagem();
	}

	public String getStringDataCriacao(){
		return String.valueOf(mDataDeCriacao.getTimeInMillis());
	}
	

}
