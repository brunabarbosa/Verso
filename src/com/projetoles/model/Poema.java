package com.projetoles.model;

import java.util.Calendar;

public class Poema {
	
	private String mTitulo;
	private String mAutor;
	private String mPoesia;
	private Calendar mDataDeCriacao;
	private String mTags;
	
	public Poema(String titulo, String autor, String poesia, Calendar dataDeCriacao, String tags) 
			throws IllegalArgumentException  {
		setTitulo(titulo);
		setAutor(autor);
		setPoesia(poesia);
		setDataDeCriacao(dataDeCriacao);
		setTags(tags);
	}

	public Poema() {
		// TODO Auto-generated constructor stub
	}

	public String getTitulo() {
		return mTitulo;
	}

	public void setTitulo(String titulo) throws IllegalArgumentException {
		if (titulo == null || titulo.trim().equals("")) {
			throw new IllegalArgumentException("Título é obrigatório.");
		}
		this.mTitulo = titulo;
	}
	
	public String getAutor() {
		return mAutor;
	}
	
	public void setAutor(String autor) throws IllegalArgumentException {
		if (autor == null || autor.trim().equals("")) {
			throw new IllegalArgumentException("Autor é obrigatório.");
		}
		this.mAutor = autor;
	}

	public String getPoesia() {
		return mPoesia;
	}
	
	public void setPoesia(String poesia) throws IllegalArgumentException {
		if (poesia == null || poesia.trim().equals("")) {
			throw new IllegalArgumentException("É preciso escrever algo antes de publicar.");
		}
		this.mPoesia = poesia;
	}

	public Calendar getDataDeCriacao() {
		return mDataDeCriacao;
	}

	public void setDataDeCriacao(Calendar dataCriacao) {
		this.mDataDeCriacao = dataCriacao;
	}
	
	public String getTags() {
		return mTags;
	}
	
	public void setTags(String tags) {
		this.mTags = tags;
	}
	
	public String getStringDataCriacao(){
		return mDataDeCriacao.toString();
	}

}
