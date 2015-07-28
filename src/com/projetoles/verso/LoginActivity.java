package com.projetoles.verso;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.projetoles.model.ImageUtils;
import com.projetoles.model.Usuario;

public class LoginActivity extends Activity {

	private UsuarioController mController;
	
	private LoginButton buttonFacebook;  
    //permissões que usaremos para recuperar dados do usuário
	private List<String> facebookPermitions;  
	//responsável por gerenciar as ações em suas aplicações após o retorno das chamadas ao FacebookSDK
    private CallbackManager callbackManager;

	private Button mBtnEntrar;
	private EditText etEmail;
	private EditText etSenha;
	private Button mBtnCadastrar; 
	private View mLoading;
    
    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.projetoles.verso", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (Exception e) {
        }
    }
    
    private void logar() {
    	mBtnEntrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mLoading.setVisibility(View.VISIBLE);
				String email = etEmail.getText().toString();
				String senha = etSenha.getText().toString();
				mController.login(email, senha, new OnRequestListener<Usuario>(LoginActivity.this) {
					 
					@Override
					public void onSuccess(Usuario usuario) {
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
					
					
					@Override
					public void onError(final String errorMessage) {
						ActivityUtils.showMessageDialog(LoginActivity.this, "Um erro ocorreu", errorMessage, mLoading);
					}
				});
			}
		});
    }
    
    private void logarComFacebook() {
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
                                    final String id = object.getString("id");
                                    final String name = object.getString("name");
                                    final String email = object.getString("email");
                                    //biografia
                                    //final String biografia=object.getString("user_about_me");
                                    mLoading.setVisibility(View.VISIBLE);
                                    mController.login(email, id, new OnRequestListener<Usuario>(LoginActivity.this) {
										
										@Override
										public void onSuccess(final Usuario result) {
											Intent intent = new Intent(LoginActivity.this, MainActivity.class);
											startActivity(intent);
											finish();
										}
										
										@Override
										public void onError(String errorMessage) {
											mController.post(name, email, id, id, new OnRequestListener<Usuario>(LoginActivity.this) {
												
												@Override
												public void onSuccess(final Usuario result) {
													new Thread(new Runnable() {
														
														@Override
														public void run() {
															
															byte[] foto = {};
															try {
																foto = ImageUtils.getPhotoFromURL("https://graph.facebook.com/" + id + "/picture‌​");
															} catch (IOException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
																Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
															}
															mController.setFoto(result, foto, new OnRequestListener<Usuario>(LoginActivity.this) {

																@Override
																public void onSuccess(Usuario result) {
																	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
																	startActivity(intent);
																	finish();
																}

																@Override
																public void onError(String errorMessage) {
																	Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show(); 
																	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
																	startActivity(intent);
																	finish();
																	System.out.println(errorMessage);
																}
															});
														}
													}).start();
												}
												
												@Override
												public void onError(String errorMessage) {
													ActivityUtils.showMessageDialog(LoginActivity.this, "Um erro ocorreu", errorMessage, mLoading);
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
    }
    
    private void cadastrar() {
    	mBtnCadastrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(LoginActivity.this, CriarContaActivity.class);
				startActivity(intent);
			}
		});
    }
    
    private void checaSeEstaLogado() {
    	mController = new UsuarioController(this);
		mController.getUsuarioLogado(new OnRequestListener<Usuario>(this) {
			
			@Override
			public void onSuccess(Usuario usuario) {
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
			
			@Override
			public void onError(String errorMessage) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mLoading.setVisibility(View.GONE);
					}
				});
			}
		});
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showHashKey(this);
		FacebookSdk.sdkInitialize(this);   
		setContentView(R.layout.activity_login);  
		
		getActionBar().hide();
		
		callbackManager = CallbackManager.Factory.create(); 
		
		etEmail = (EditText) findViewById(R.id.editEmail);
		etSenha = (EditText) findViewById(R.id.editSenha);
		mBtnEntrar = (Button) findViewById(R.id.btnEntrar);
		mBtnCadastrar = (Button) findViewById(R.id.btnCadastrar);
		mLoading = (RelativeLayout) findViewById(R.id.loginLoading);

		facebookPermitions = Arrays.asList("email", "public_profile", "user_friends"); 
		   
		buttonFacebook = (LoginButton)findViewById(R.id.login_button);
		buttonFacebook.setReadPermissions(facebookPermitions); 
		
		cadastrar();
		
		logar();
		
		logarComFacebook();
		
		checaSeEstaLogado();
	}
	
	@Override  
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	      super.onActivityResult(requestCode, resultCode, data);  
	      callbackManager.onActivityResult(requestCode, resultCode, data);  
	   }

}
