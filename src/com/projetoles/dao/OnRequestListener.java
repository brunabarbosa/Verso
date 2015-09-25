package com.projetoles.dao;

import android.content.Context;
import android.os.Handler;

/**
 * Listener genérico que possuí uma chamada para falha e outra para sucesso
 */
public abstract class OnRequestListener<T> {

	private Context mContext;
	
	public OnRequestListener(Context context) {
		this.mContext = context;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public void setContext(Context newContext) {
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
	
	public void runOnUiThread(Runnable runnable) {
		Handler handler = new Handler(mContext.getMainLooper());
		handler.post(runnable);
	}
	
}
