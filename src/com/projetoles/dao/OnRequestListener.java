package com.projetoles.dao;

import android.app.Activity;

/**
 * Listener gen�rico que possu� uma chamada para falha e outra para sucesso
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
	 * M�todo que ser� chamado em caso de sucesso do evento
	 * @param result
	 * 		Retorno do evento
	 */
	public abstract void onSuccess(T result);
	
	/**
	 * M�todo que ser� chamado em caso de falha do evento
	 * @param errorMessage	
	 * 		Causa da falha
	 */
	public abstract void onError(String errorMessage);
	
}
