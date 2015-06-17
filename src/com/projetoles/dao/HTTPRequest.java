package com.projetoles.dao;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

public abstract class HTTPRequest {

	protected String mUrl;

	public HTTPRequest(String url) {
		mUrl = url;
	}

	public String getUrl() {
		return mUrl;
	}
	
	public static abstract class Builder {
		
		protected String mDomain;
		protected String mPath;
		protected List<NameValuePair> mParams;
		
		public Builder() {
			mParams = new LinkedList<NameValuePair>();
		}
		
		/**
		 * Define o dominio (url base) da requisi��o
		 * @param domain
		 * 		Url base 
		 * @return
		 * 		O pr�prio objeto
		 */
		public Builder setDomain(String domain) {
			this.mDomain = domain;
			return this;
		}
		
		/**
		 * Define o subdominio (ex: /) da requisi��o
		 * @param path
		 * 		Subdominio 
		 * @return
		 * 		O pr�prio objeto
		 */
		public Builder setPath(String path) {
			this.mPath = path;
			return this;
		}
		
		/**
		 * Adiciona um novo par�metro na requisi��o
		 * @param name
		 * 		Nome do par�metro
		 * @param value
		 * 		Valor do par�metro
		 * @return
		 * 		O pr�prio objeto
		 */
		public Builder addParam(String name, String value) {
			mParams.add(new BasicNameValuePair(name, value));
			return this;
		}
		
		/**
		 * @return
		 * 		Retorna a string contendo a url base, o subdominio e os par�metros
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
