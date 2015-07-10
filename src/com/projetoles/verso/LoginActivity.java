package com.projetoles.verso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;

public class LoginActivity extends Activity {

	private UsuarioController mController;
	
	private LoginButton buttonFacebook;  
    //permiss�es que usaremos para recuperar dados do usu�rio
	private List<String> facebookPermitions;  
	//respons�vel por gerenciar as a��es em suas aplica��es ap�s o retorno das chamadas ao FacebookSDK
    private CallbackManager callbackManager;  
    
    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.projetoles.verso", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showHashKey(this);
		FacebookSdk.sdkInitialize(this);   
		setContentView(R.layout.activity_login);  
		callbackManager = CallbackManager.Factory.create(); 
		
		facebookPermitions = Arrays.asList("email", "public_profile", "user_friends"); 
		   
		buttonFacebook = (LoginButton)findViewById(R.id.login_button);
		buttonFacebook.setReadPermissions(facebookPermitions); 
		final RelativeLayout etLoading = (RelativeLayout) findViewById(R.id.loginLoading);
		
		buttonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {            
      
	        public void onSuccess(LoginResult loginResult) {  
	        	// App code
               // Toast.makeText(Ready.this, "Connected!", Toast.LENGTH_SHORT).show();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                            		JSONObject object,
                                    GraphResponse response) {
                                // Application code 
                                try {
                                    final String id=object.getString("id");
                                    final String name=object.getString("name");
                                    final String email=object.getString("email");
                                    //biografia
                                    //final String biografia=object.getString("user_about_me");
                                    etLoading.setVisibility(View.VISIBLE);
                                    mController.login(email, id, new OnRequestListener(LoginActivity.this) {
										
										@Override
										public void onSuccess(Object result) {
											Intent i = new Intent(LoginActivity.this, MainActivity.class);
											startActivity(i);
											finish();	
										}
										
										@Override
										public void onError(String errorMessage) {
											mController.registrar(name, email, id, id, new OnRequestListener(LoginActivity.this) {
												
												@Override
												public void onSuccess(Object result) {
													Intent i = new Intent(LoginActivity.this, MainActivity.class);
													startActivity(i);
													finish();
												}
												
												@Override
												public void onError(String errorMessage) {
													new AlertDialog.Builder(LoginActivity.this)
													.setTitle("Um erro ocorreu")
													.setMessage(errorMessage)
													.setNeutralButton("OK", new DialogInterface.OnClickListener() {
														
														@Override
														public void onClick(DialogInterface dialog, int which) {
															etLoading.setVisibility(View.GONE);
														}
													})
													.create().show();
											
													
												}
											});
											
										}
									});
                                    
                                    
                                    //do something with the data here
                                } catch (JSONException e) {
                                    e.printStackTrace(); //something's seriously wrong here
                                }
                            } 
                        }); 
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
	        }  
	                  
	        public void onError(FacebookException arg0) {  
	             Toast.makeText(getBaseContext(), "ERROR!", Toast.LENGTH_LONG).show();  
	             System.out.println("erro" + arg0.getMessage());
	             arg0.printStackTrace();
	        }  
	                  
	         
	        public void onCancel() {  
	             Toast.makeText(getBaseContext(), "CANCEL!", Toast.LENGTH_LONG).show();  
	             System.out.println("cancel");
	        }  
	    });  
	
		getActionBar().hide();
	
		
		final EditText etEmail = (EditText) findViewById(R.id.editEmail);
		final EditText etSenha = (EditText) findViewById(R.id.editSenha);
		final Button cadastrar = (Button) findViewById(R.id.btnCadastrar);
		cadastrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(LoginActivity.this, CriarContaActivity.class);
				startActivity(i);
			}
		});
		final Button entrar = (Button) findViewById(R.id.btnEntrar);
		entrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etLoading.setVisibility(View.VISIBLE);
				String email = etEmail.getText().toString();
				String senha = etSenha.getText().toString();
				mController.login(email, senha, new OnRequestListener(LoginActivity.this) {
					
					@Override
					public void onSuccess(Object result) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Intent i = new Intent(LoginActivity.this, MainActivity.class);
								startActivity(i);
								finish();
							}
						});
					}
					
					
					@Override
					public void onError(final String errorMessage) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								new AlertDialog.Builder(LoginActivity.this)
									.setTitle("Um erro ocorreu")
									.setMessage(errorMessage)
									.setNeutralButton("OK", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											etLoading.setVisibility(View.GONE);
										}
									})
									.create().show();
							}
						});
					}
				});
			}
		});
		
		
		   

		mController = new UsuarioController(this);
		mController.getLoggedUser(new OnRequestListener(this) {
			
			@Override
			public void onSuccess(Object result) {
				Intent i = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
			
			@Override
			public void onError(String errorMessage) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						etLoading.setVisibility(View.GONE);
					}
				});
			}
		});
		
	}
	
	@Override  
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	      super.onActivityResult(requestCode, resultCode, data);  
	      callbackManager.onActivityResult(requestCode, resultCode, data);  
	   }

}
