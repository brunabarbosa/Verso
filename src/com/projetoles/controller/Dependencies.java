package com.projetoles.controller;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

@SuppressWarnings("rawtypes")
public class Dependencies {

	private List<Pair<String, Object>> mDependencies = new ArrayList<Pair<String, Object>>();
	
	public Dependencies() {
		
	}
	
	public Dependencies(List<Pair<String, Object>> dependencies) {
		this.mDependencies = dependencies;
	}
	
	public void addDependency(String key, Object controller) {
		this.mDependencies.add(new Pair<String, Object>(key, controller));
	}
	
	public boolean isEmpty() {
		return this.mDependencies.isEmpty();
	}
	
	public Pair<String, Object> getFirst() {
		return this.mDependencies.get(0);
	}
	
	public Dependencies getTail() {
		return new Dependencies(mDependencies.subList(1, mDependencies.size()));
	}
	
}
