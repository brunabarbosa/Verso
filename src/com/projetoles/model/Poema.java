package com.projetoles.model;

import java.util.Calendar;

public class Poema {
	
	private String titulo, autor, poesia;
	private Calendar dataDeCriacao;
	private String tags;
	
	public Poema(String titulo, String autor, String poesia, Calendar dataDeCriacao, String tags) {
		this.titulo = titulo;
		this.autor = autor;
		this.poesia = poesia;
		this.dataDeCriacao = dataDeCriacao;
		this.tags = tags;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getAutor() {
		return autor;
	}

	public String getPoesia() {
		return poesia;
	}

	public Calendar getDataDeCriacao() {
		return dataDeCriacao;
	}

	public String getTags() {
		return tags;
	}
	
	public String getStringDataCriacao(){
		return dataDeCriacao.toString();
	}

}
