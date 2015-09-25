package com.projetoles.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

public abstract class HTTPRequest {

	protected static final int MAX_NUM_THREADS = 6;
	protected static int COUNT_NUM_THREADS;
	protected static List<Thread> SLEEPING_THREADS = Collections.synchronizedList(new ArrayList<Thread>());
	 
	protected String mUrl;

	public HTTPRequest(String url) {
		mUrl = url;
	}

	public String getUrl() {
		return mUrl;
	}
	
	protected abstract String getContent() throws Exception;
	
	/**
	 * Executa a requisição 
	 * @param listener
	 * 		Evento que será chamado ao termino da conexão
	 */
	public void execute(final OnRequestListener<String> listener) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean isError = false;
				String content;
				try {
					content = getContent();
				} catch(Exception e) {
					content = e.getMessage();
					isError = true;
				}
				//necessário para acessar dentro da thread
				final boolean finalIsError = isError;
				final String finalContent = content;
				COUNT_NUM_THREADS--;
				if (!SLEEPING_THREADS.isEmpty()) {
					Thread t = SLEEPING_THREADS.get(0);
					SLEEPING_THREADS.remove(t);
					t.start(); 
				}
				listener.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if (finalIsError) {
							listener.onError(finalContent);
						} else {
							listener.onSuccess(finalContent);
						}
					}
				});
			}
		});
		if (COUNT_NUM_THREADS < MAX_NUM_THREADS) {
			COUNT_NUM_THREADS++;
			t.start(); 
		} else {
			SLEEPING_THREADS.add(t);
		}
	}
	
	
	public static abstract class Builder {
		
		protected String mDomain;
		protected String mPath;
		protected List<NameValuePair> mParams;
		
		public Builder() {
			mParams = new LinkedList<NameValuePair>();
		}
		
		/**
		 * Define o dominio (url base) da requisição
		 * @param domain
		 * 		Url base 
		 * @return
		 * 		O próprio objeto
		 */
		public Builder setDomain(String domain) {
			this.mDomain = domain;
			return this;
		}
		
		/**
		 * Define o subdominio (ex: /) da requisição
		 * @param path
		 * 		Subdominio 
		 * @return
		 * 		O próprio objeto
		 */
		public Builder setPath(String path) {
			this.mPath = path;
			return this;
		}
		
		/**
		 * Adiciona um novo parâmetro na requisição
		 * @param name
		 * 		Nome do parâmetro
		 * @param value
		 * 		Valor do parâmetro
		 * @return
		 * 		O próprio objeto
		 */
		public Builder addParam(String name, String value) {
			mParams.add(new BasicNameValuePair(name, value));
			return this;
		}
		
		/**
		 * @return
		 * 		Retorna a string contendo a url base, o subdominio e os parâmetros
		 */
		protected String getFormattedUrl() {
			Uri.Builder builder = new Uri.Builder()
				.scheme("http")
				.authority(mDomain)
				.path(mPath);
			for (NameValuePair pair : mParams) {
				builder.appendQueryParameter(pair.getName(), pair.getValue());
			}
			return builder.build().toString();
		}
			
		public JSONObject getJSON() throws JSONException {
			String json = "{";
			for (NameValuePair pair : mParams) {
				if (pair.getValue().length() > 1 && pair.getValue().substring(0, 1).equals("[")) {
					json += "\"" + pair.getName() + "\":" + pair.getValue();
				} else {
					json += "\"" + pair.getName() + "\":\"" + pair.getValue() + "\"";
				}
				if (!pair.equals(mParams.get(mParams.size() - 1))) {
					json += ",";
				}
			}
			json += "}";
			return new JSONObject(json);
		}

		public abstract HTTPRequest create();

	}
	
}
