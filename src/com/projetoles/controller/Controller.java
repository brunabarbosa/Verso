package com.projetoles.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class Controller {

	protected Activity mContext;
	protected SharedPreferences mSession;
	protected Editor mEditor;

	public Controller(Activity context) {
		this.mContext = context;
		this.mSession = context.getSharedPreferences("com.projetoles.verso", Context.MODE_PRIVATE); 
		this.mEditor = mSession.edit();
	}

	public Activity getContext() {
		return mContext;
	}
	
	// check network connection
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) 
        		mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}
