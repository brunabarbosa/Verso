package com.projetoles.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;

/**
 * Classe responsável por realizar uma requisição HTTP do tipo GET
 */
public class GET extends HTTPRequest {

	private GET(String url) {
		super(url);
	}
	
	private String getContent() throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		HttpGet getRequest = new HttpGet(mUrl);
		HttpResponse response = client.execute(getRequest);
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
		return Jsoup.parse(output).text();
	}
	
	/**
	 * Executa a requisição GET
	 * @param listener
	 * 		Evento que será chamado ao termino da conexão
	 */
	public void execute(final OnRequestListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean isError = false;
				String content;
				try {
					do {
						content = getContent();
					//propaganda
					} while (content.substring(content.length() >= 30 ? 30 : content.length()).equals("Web hosting, domain names, VPS"));
				} catch(Exception e) {
					content = e.getMessage();
					isError = true;
				}
				//necessário para acessar dentro da thread
				final boolean finalIsError = isError;
				final String finalContent = content;
				listener.getContext().runOnUiThread(new Runnable() {
					
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
		}).start();
	}
	
	public static class Builder extends HTTPRequest.Builder {

		@Override
		public HTTPRequest create() {
			String url = getFormattedUrl();
			return new GET(url);
		}
		
	}
	
}