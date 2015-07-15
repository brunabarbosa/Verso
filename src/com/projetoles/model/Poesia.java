package com.projetoles.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Poesia implements Comparable<Poesia> {
	
	private String mId;
	private String mTitulo;
	private String mPostador;
	private String mAutor;
	private String mPoesia;
	private Calendar mDataDeCriacao;
	private String mTags;
	private Set<String> mComentarios;
	private Set<Comentario> mComentariosCarregados;
	
	public Poesia(String id, String titulo, String postador, String autor, String poesia, Calendar dataDeCriacao, String tags) 
			throws IllegalArgumentException  {
		setId(id);
		setTitulo(titulo);
		setPostador(postador);
		setAutor(autor);
		setPoesia(poesia);
		setDataDeCriacao(dataDeCriacao);
		setTags(tags);
		this.mComentarios = new HashSet<String>();
		this.mComentariosCarregados = new HashSet<Comentario>();
	}

	public Poesia() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return mId;
	}
	
	public void setId(String id) {
		this.mId = id;
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
	
	public String getPostador() {
		return mPostador;
	}
	
	public void setPostador(String postador) throws IllegalArgumentException {
		if (postador == null || postador.trim().equals("")) {
			throw new IllegalArgumentException("Postador não deve ser vazio.");
		}
		this.mPostador = postador;
	}
	
	public Set<String> getComentarios() {
		return Collections.unmodifiableSet(this.mComentarios);
	}
	
	public Set<Comentario> getComentariosCarregados() {
		return Collections.unmodifiableSet(this.mComentariosCarregados);
	}
	
	public void addComentario(String comentario) {
		this.mComentarios.add(comentario);
	}
	
	public void addComentarioCarregado(Comentario comentario) {
		this.mComentariosCarregados.add(comentario);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mAutor == null) ? 0 : mAutor.hashCode());
		result = prime * result + ((mPoesia == null) ? 0 : mPoesia.hashCode());
		result = prime * result + ((mTitulo == null) ? 0 : mTitulo.hashCode());
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
		Poesia other = (Poesia) obj;
		if (mAutor == null) {
			if (other.mAutor != null)
				return false;
		} else if (!mAutor.equals(other.mAutor))
			return false;
		if (mPoesia == null) {
			if (other.mPoesia != null)
				return false;
		} else if (!mPoesia.equals(other.mPoesia))
			return false;
		if (mTitulo == null) {
			if (other.mTitulo != null)
				return false;
		} else if (!mTitulo.equals(other.mTitulo))
			return false;
		return true;
	}

	public String getStringDataCriacao(){
		return String.valueOf(mDataDeCriacao.getTimeInMillis());
	}

	public static Poesia converteJson(JSONObject json) throws JSONException {
		String id = json.getString("id");
		String titulo = json.getString("titulo");
		String poesia = json.getString("poesia");
		String tags = json.getString("tags");
		String autor = json.getString("autor");
		String postador = json.getString("postador");
		Long tempo = Long.valueOf(json.getString("dataCriacao"));
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tempo);
		Poesia p = new Poesia(id, titulo, postador, autor, poesia, c, tags);
		JSONArray comentarios = json.getJSONArray("comentarios");
		for (int i = 0; i < comentarios.length(); i++) {
			p.addComentario(comentarios.getString(i));
		}
		return p;
	}

	@Override
	public int compareTo(Poesia arg0) {
		return arg0.mDataDeCriacao.compareTo(this.mDataDeCriacao);
	}

}
