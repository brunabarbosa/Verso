package com.projetoles.model;

public interface PreloadedListener<T extends TemporalModel> {

	public void onLoad(T obj);
	
}
