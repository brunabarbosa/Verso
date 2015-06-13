package com.example.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.enums.EnumCategoria;

public class Poema {
	private String titulo, autor, poesia;
	private Calendar dataDeCriacao;
	private EnumCategoria categoria;
	private List<String> tags;
	
	public Poema(String titulo, String autor, String poesia, Calendar dataDeCriacao, List<String> tags) {
		this.titulo = titulo;
		this.autor = autor;
		this.poesia = poesia;
		this.dataDeCriacao = dataDeCriacao;
		
		tags = new ArrayList<String>();
		this.tags = tags;
		this.categoria = EnumCategoria.INFANTIL;
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

	public EnumCategoria getCategoria() {
		return categoria;
	}

	public List<String> getTags() {
		return tags;
	}

}
