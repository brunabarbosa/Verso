package com.projetoles.controller;

import com.projetoles.model.User;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class Controller {

	public static User loggedUser;
	
	protected Activity mContext;
	
	public Controller(Activity context) {
		this.mContext = context;
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
