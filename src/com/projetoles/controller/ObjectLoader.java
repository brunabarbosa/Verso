package com.projetoles.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.projetoles.model.Model;

public class ObjectLoader<T extends Model> {

	private static final int MAX_NUM_ITEMS = 20;
	
	private Map<String, T> mLoaded = new HashMap<String, T>();
	private List<String> mPriorityQueue = new ArrayList<String>();
	
	public void save(T object) {
		if (mLoaded.size() > MAX_NUM_ITEMS) {
			mLoaded.remove(mPriorityQueue.get(MAX_NUM_ITEMS - 1));
			mPriorityQueue.remove(MAX_NUM_ITEMS - 1);
		}
		mLoaded.put(object.getId(), object);
		mPriorityQueue.add(0, object.getId());
	}
	 
	public boolean contains(String id) {
		return mLoaded.containsKey(id);
	}
	
	public T get(String id) {
		mPriorityQueue.remove(id);
		mPriorityQueue.add(0, id);
		return mLoaded.get(id);
	}
	
	public void remove(String id) {
		this.mLoaded.remove(id);
	}
	
}
