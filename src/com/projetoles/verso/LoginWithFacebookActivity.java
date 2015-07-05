package com.projetoles.verso;

import java.util.Arrays;
import java.util.List;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginWithFacebookActivity extends Activity{
	private LoginButton buttonFacebook;  
    private List<String> facebookPermitions;  
    private CallbackManager callbackManager;  
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this);   
		setContentView(R.layout.activity_login);	
		
		callbackManager = CallbackManager.Factory.create(); 
		facebookPermitions = Arrays.asList("email", "public_profile", "user_friends"); 
		   
		buttonFacebook = (LoginButton)findViewById(R.id.login_button);
		buttonFacebook.setReadPermissions(facebookPermitions); 
		
		buttonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {            
	          
	        public void onSuccess(LoginResult arg0) {  
	             Toast.makeText(getBaseContext(), "SUCESSO!", Toast.LENGTH_LONG).show();  
	        }  
	                  
	        public void onError(FacebookException arg0) {  
	             Toast.makeText(getBaseContext(), "ERROR!", Toast.LENGTH_LONG).show();  
	        }  
	                  
	         
	        public void onCancel() {  
	             Toast.makeText(getBaseContext(), "CANCEL!", Toast.LENGTH_LONG).show();  
	        }  
	    });  
		
		
		
		

		

}


}
