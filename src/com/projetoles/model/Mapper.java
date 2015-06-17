package com.projetoles.model;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Mapper {

	public static Set<Long> getSetFromJSON(JSONObject obj, String field) throws JSONException {
		Set<Long> mySet = new HashSet<Long>();
		JSONArray jsonArray = obj.getJSONArray(field);
		for (int i = 0; i < jsonArray.length(); i++) {
			mySet.add(Long.valueOf(jsonArray.getString(i)));
		}
		return mySet;
	}
	
	public static Set<String> getSetStringFromJSON(JSONObject obj, String field) throws JSONException {
		Set<String> mySet = new HashSet<String>();
		JSONArray jsonArray = obj.getJSONArray(field);
		for (int i = 0; i < jsonArray.length(); i++) {
			mySet.add(jsonArray.getString(i));
		}
		return mySet;
	}

	public static <T> String getStringFromSet(Set<T> set) {
		String result = "[";
		int count = 0;
		for (Object obj : set) {
			result += "\"" + obj + "\"";
			if (count != set.size() - 1) {
				result += ",";
			}
			count++;
		}
		result += "]";
		return result;
	}
	
}
