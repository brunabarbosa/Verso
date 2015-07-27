package com.projetoles.dao;

import android.app.Activity;

/**
 * Listener genérico que possuí uma chamada para falha e outra para sucesso
 */
public abstract class OnRequestListener<T> {

	private Activity mContext;
	
	public OnRequestListener(Activity context) {
		this.mContext = context;
	}
	
	public Activity getContext() {
		return mContext;
	}
	
	public void setContext(Activity newContext) {
		mContext = newContext;
	}
	
	/**
	 * Método que será chamado em caso de sucesso do evento
	 * @param result
	 * 		Retorno do evento
	 */
	public abstract void onSuccess(T result);
	
	/**
	 * Método que será chamado em caso de falha do evento
	 * @param errorMessage	
	 * 		Causa da falha
	 */
	public abstract void onError(String errorMessage);
	
}
