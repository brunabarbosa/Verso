package com.projetoles.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;

import android.net.Uri;

/**
 * Classe responsável por realizar uma requisição HTTP do tipo POST
 */
public class POST extends HTTPRequest {

	private String mUrl;
	private List<NameValuePair> mParams;
	private File mPhoto;
	
	private POST(String url, List<NameValuePair> params, File photo) {
		super(url);
		this.mUrl = url;
		this.mParams = params;
		this.mPhoto = photo;
	}

	private String execute() throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(mUrl);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();        
		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		//Adiciona foto
		if (mPhoto != null) {
		    builder.addPart("photo", new FileBody(mPhoto));
		} 
		//Adiciona outros parâmetros
		for (NameValuePair param : mParams) {
			builder.addTextBody(param.getName(), param.getValue(), contentType);
	    }
	    postRequest.setEntity(builder.build());
		HttpResponse response = client.execute(postRequest);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException(response.getStatusLine().getReasonPhrase());
		}
		String output = "";
		String line;
		BufferedReader br = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		while ((line = br.readLine()) != null) {
			output += line;
		}
		client.getConnectionManager().shutdown();
		System.out.println(output);
		return Jsoup.parse(output).text();
	}
	
	public void execute(final OnRequestListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String errorMessage = null;
				String message = null;
				try {
					message = execute();
				} catch(Exception e) {
					errorMessage = e.getMessage();
					e.printStackTrace();
				}
				//necessário para acessar dentro da thread
				final String finalErrorMessage = errorMessage;
				final String finalMessage = message;
				listener.getContext().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if (finalErrorMessage != null) {
							listener.onError(finalErrorMessage);
						} else {
							System.out.println(finalMessage);
							listener.onSuccess(finalMessage);
						}
					}
				});
			}
		}).start();
	}
	
	public static class Builder extends HTTPRequest.Builder {
		
		private File mPhoto;
		
		/**
		 * Adiciona um arquivo (geralmente uma foto) à requisição
		 * @param photo
		 * 		Foto que se deseja anexar junto à requisição
		 * @return
		 * 		O próprio objeto
		 */
		public Builder addPhoto(File photo) {
			this.mPhoto = photo;
			return this;
		}

		@Override
		protected String getFormattedUrl() {
			Uri.Builder builder = new Uri.Builder()
				.scheme("http")
				.authority(mDomain)
				.path(mPath);
			return builder.build().toString();
		}
		
		@Override
		public HTTPRequest create() {
			String url = getFormattedUrl();
			return new POST(url, mParams, mPhoto);
		}

	}
	
}