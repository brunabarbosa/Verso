package com.projetoles.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Pair;

import com.projetoles.dao.DAO;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Model;

public abstract class Controller<T extends Model> {

	protected Activity mContext;
	protected SharedPreferences mSession;
	protected Editor mEditor;
	protected Map<String, List<OnRequestListener<T>>> mRequisitions;
	protected ObjectLoader<T> mLoader;
	protected DAO<T> mDAO;

	public Controller(Activity context) {
		this.mContext = context;
		this.mSession = context.getSharedPreferences("com.example.verso", Context.MODE_PRIVATE); 
		this.mEditor = mSession.edit();
		this.mRequisitions = new HashMap<String, List<OnRequestListener<T>>>();
	}

	public Activity getContext() {
		return mContext;
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void load(final Dependencies dependencies, final List<Object> loaded, final JSONObject json,
    		final OnRequestListener<List<Object>> callback) {
    	if (dependencies.isEmpty()) {
    		callback.onSuccess(loaded);
    	} else { 
    		Pair<String, Controller> first = dependencies.getFirst();
    		try {
	    		String key = json.getString(first.first);
	    		first.second.get(key, new OnRequestListener<Object>(callback.getContext()) {
	
					@Override
					public void onSuccess(Object result) {
						loaded.add(result);
						Controller.this.load(dependencies.getTail(), loaded, json, callback);
					}
	
					@Override
					public void onError(String errorMessage) {
						callback.onError(errorMessage);
					}
				}, new Dependencies());
			} catch (JSONException e) {
				e.printStackTrace();
				callback.onError(e.getMessage());
			}
    	}
    }
    
    public abstract void update(final T object, final OnRequestListener<T> callback);
    
	public void get(final String id, final OnRequestListener<T> callback, final Dependencies dependencies) {
		if (mLoader.contains(id)) {
			this.update(mLoader.get(id), new OnRequestListener<T>(callback.getContext()) {

				@Override
				public void onSuccess(T result) {
					callback.onSuccess(result);
				}

				@Override
				public void onError(String errorMessage) {
					callback.onError(errorMessage);
				}
			});
		} else { 
			if (!mRequisitions.containsKey(id)) {
				mRequisitions.put(id, new ArrayList<OnRequestListener<T>>());
				mRequisitions.get(id).add(callback);
				mDAO.get(id, new OnRequestListener<String>(callback.getContext()) {
					 
					@Override
					public void onSuccess(final String jsonResult) {
						try {
							final JSONObject jsonObject = new JSONObject(jsonResult);
							boolean success = jsonObject.getBoolean("success");
							if (success) {
								List<Object> params = new ArrayList<Object>();
								Controller.this.load(dependencies, params, jsonObject, new OnRequestListener<List<Object>>(callback.getContext()) {

									@Override
									public void onSuccess(List<Object> result) {
										try { 
											final T object = mDAO.getFromJSON(jsonObject, result);
											mLoader.save(object);
											for (final OnRequestListener<T> listener : mRequisitions.get(id)) {
												listener.getContext().runOnUiThread(new Runnable() {
													
													@Override
													public void run() {
														listener.onSuccess(object);
													}
												});
											}
											mRequisitions.remove(id);
										} catch (JSONException e) {
											e.printStackTrace();
											callback.onError(e.getMessage());
										}
									}

									@Override
									public void onError(String errorMessage) {
										callback.onError(errorMessage);
									}
								});
							} else {
								callback.onError(jsonObject.getString("message"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
							callback.onError(e.getMessage());
						}
					}
					
					@Override
					public void onError(String errorMessage) {
						callback.onError(errorMessage);
					}
				});
			} else {
				mRequisitions.get(id).add(callback);
			}
		}
	}
	
	public void post(final T object, final OnRequestListener<T> callback) {
		mDAO.post(object, new OnRequestListener<String>(callback.getContext()) {
			@Override
			public void onSuccess(String jsonResult) {
				try {
					JSONObject json = new JSONObject(jsonResult.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						String id = json.getString("id");
						object.setId(id);
						mLoader.save(object);
						callback.getContext().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								callback.onSuccess(object);
							}
						});
					} else {
						callback.onError(json.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					callback.onError(e.getMessage());
				}
			}

			@Override
			public void onError(String errorMessage) {
				callback.onError(errorMessage);
			}
		});
	}
	
	public void delete(final String id, final OnRequestListener<String> callback) {
		mDAO.delete(id, new OnRequestListener<String>(callback.getContext()) {
            @Override
            public void onSuccess(String jsonResult) {
                try {
                    JSONObject json = new JSONObject(jsonResult.toString());
                    boolean success = json.getBoolean("success");
                    if (success) {
                    	mLoader.remove(id);
                    	callback.getContext().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
		                    	callback.onSuccess(id);
							}
						});
                    } else {
                    	callback.onError(json.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
	} 
	
	public void put(final T object, final OnRequestListener<T> callback) {
		mDAO.put(object, new OnRequestListener<String>(callback.getContext()) {
			
			@Override
			public void onSuccess(String jsonResult) {
				try {
					JSONObject json = new JSONObject(jsonResult.toString());
					boolean success = json.getBoolean("success");
					if (success) {
						callback.getContext().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								callback.onSuccess(object);
							}
						});
					} else {
						callback.onError(json.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					callback.onError(e.getMessage());
				}
			}
			
			@Override
			public void onError(String errorMessage) {
				callback.onError(errorMessage);
			}
		});
	}
	
}
