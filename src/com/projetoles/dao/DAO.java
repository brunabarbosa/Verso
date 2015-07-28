package com.projetoles.dao;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.model.Model;

public abstract class DAO<T extends Model> {

	public static final String DOMAIN  = "verso-projetoles.rhcloud.com";
	
	public abstract void post(T object, OnRequestListener<String> callback);
	
	public abstract void get(String id, OnRequestListener<String> callback);
	
	public void delete(String id, OnRequestListener<String> callback) {
		throw new UnsupportedOperationException();
	}
	
	public void put(T object, OnRequestListener<String> callback) {
		throw new UnsupportedOperationException();
	}

	public void update(T object, OnRequestListener<String> callback) {
		
	}
	
	public abstract T getFromJSON(JSONObject json, List<Object> params) throws JSONException;
	
}
