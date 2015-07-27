package com.projetoles.controller;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

@SuppressWarnings("rawtypes")
public class Dependencies {

	private List<Pair<String, Controller>> mDependencies = new ArrayList<Pair<String, Controller>>();
	
	public Dependencies() {
		
	}
	
	public Dependencies(List<Pair<String, Controller>> dependencies) {
		this.mDependencies = dependencies;
	}
	
	public void addDependency(String key, Controller controller) {
		this.mDependencies.add(new Pair<String, Controller>(key, controller));
	}
	
	public boolean isEmpty() {
		return this.mDependencies.isEmpty();
	}
	
	public Pair<String, Controller> getFirst() {
		return this.mDependencies.get(0);
	}
	
	public Dependencies getTail() {
		return new Dependencies(mDependencies.subList(1, mDependencies.size()));
	}
	
}
