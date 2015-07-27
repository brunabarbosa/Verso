package com.projetoles.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
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
	
	@Override
	protected String getContent() throws Exception {
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
	 
	public static class Builder extends HTTPRequest.Builder {

		@Override
		public HTTPRequest create() {
			String url = getFormattedUrl();
			return new GET(url);
		}
		
	}
	
}