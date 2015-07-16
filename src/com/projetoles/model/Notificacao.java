package com.projetoles.model;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class Notificacao {
	
	private String mEnderecado;
	private String mTitulo;
	private String mMensagem;
	private Calendar mDataDeCriacao;
	
	public Notificacao(String enderecado, String titulo, String mensagem, Calendar dataDeCriacao) {
		setTitulo(titulo);
		setMensagem(mensagem);
		setDataDeCriacao(dataDeCriacao);
		setEnderecado(enderecado);
	}

	public Notificacao() {
		// TODO Auto-generated constructor stub
	}

	private void setEnderecado(String enderecado) {
		if (enderecado == null || enderecado.trim().isEmpty()) {
			throw new IllegalArgumentException("Enderecado é obrigatório.");
		}
		this.mEnderecado = enderecado;
		
	}

	public String getTitulo() {
		return mTitulo;
	}

	public void setTitulo(String mTitulo) {
		if (mTitulo == null || mTitulo.trim().isEmpty()) {
			throw new IllegalArgumentException("Título é obrigatório.");
		}
		this.mTitulo = mTitulo;
	}

	public String getMensagem() {
		return mMensagem;
	}

	public void setMensagem(String mMensagem) {
		if (mMensagem == null || mMensagem.trim().isEmpty()) {
			throw new IllegalArgumentException("Mensagem é obrigatória.");
		}
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mEnderecado == null) ? 0 : mEnderecado.hashCode());
		return result;
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

	public String getEnderecado() {
		return mEnderecado;
	}
	
	public static Notificacao converteJson(JSONObject json) throws JSONException {
		Long tempo = Long.valueOf(json.getString("dataCriacao"));
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tempo);
		String enderecado = json.getString("enderecado");
		String titulo = json.getString("titulo");
		String mensagem = json.getString("mensagem");
		return new Notificacao(enderecado, titulo, mensagem, c);
	}

}
