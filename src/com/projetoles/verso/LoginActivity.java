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
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.ImageUtils;
import com.projetoles.model.Usuario;

public class LoginActivity extends Activity {
	
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
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
	private String mRegId;
	
	// google cloud messaging
	private GoogleCloudMessaging mGcm;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    private String SENDER_ID = "1035005040960";
    private TextView mDisplay;
    
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("com.projetoles.verso", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("com.projetoles.verso", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("com.projetoles.verso", "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("com.projetoles.verso", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (mGcm == null) {
                    	mGcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    }
                    mRegId = mGcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + mRegId;
                    System.out.println(msg);
                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(LoginActivity.this, mRegId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            	try {
            		mDisplay.append(msg + "\n");
            	} catch (Exception e) {
            	}
            }
        }.execute(null, null, null);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(LoginActivity.class.getSimpleName(), MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
      // Your implementation here.
    }
    
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
				mController.login(email, senha, mRegId, new OnRequestListener<Usuario>(LoginActivity.this) {
					 
					@Override
					public void onSuccess(Usuario usuario) {
						System.out.println(mRegId);
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
					
					
					@Override
					public void onError(final String errorMessage) {
						ActivityUtils.showMessageDialog(LoginActivity.this, "Um erro ocorreu", errorMessage, mLoading);
					}


					@Override
					public void onTimeout() {
						ActivityUtils.showMessageDialog(LoginActivity.this, "Ops", "Ocorreu um erro com a sua requisição. Verifique sua conexão com a internet.", mLoading);
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
                                    mController.login(email, id, mRegId, new OnRequestListener<Usuario>(LoginActivity.this) {
										
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

																@Override
																public void onTimeout() {
																	if (AccessToken.getCurrentAccessToken() != null) {
																		LoginManager.getInstance().logOut();
																	}
																	ActivityUtils.showMessageDialog(LoginActivity.this, "Ops", "Ocorreu um erro com a sua requisição. Verifique sua conexão com a internet.", mLoading);
																}
															});
														}
													}).start();
												}
												
												@Override
												public void onError(String errorMessage) {
													if (AccessToken.getCurrentAccessToken() != null) {
														LoginManager.getInstance().logOut();
													}
													ActivityUtils.showMessageDialog(LoginActivity.this, "Um erro ocorreu", errorMessage, mLoading);
												}

												@Override
												public void onTimeout() {
													if (AccessToken.getCurrentAccessToken() != null) {
														LoginManager.getInstance().logOut();
													}
													ActivityUtils.showMessageDialog(LoginActivity.this, "Ops", "Ocorreu um erro com a sua requisição. Verifique sua conexão com a internet.", mLoading);
												}
											});
											
										}

										@Override
										public void onTimeout() {
											if (AccessToken.getCurrentAccessToken() != null) {
												LoginManager.getInstance().logOut();
											}
											ActivityUtils.showMessageDialog(LoginActivity.this, "Ops", "Ocorreu um erro com a sua requisição. Verifique sua conexão com a internet.", mLoading);
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
				mLoading.setVisibility(View.GONE);
			}

			@Override
			public void onTimeout() {
				mLoading.setVisibility(View.GONE);
			}
		}, mRegId);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showHashKey(this);
		FacebookSdk.sdkInitialize(this);   
		setContentView(R.layout.activity_login);  
		
		getActionBar().hide();
		
		callbackManager = CallbackManager.Factory.create(); 
		 
		// Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            mGcm = GoogleCloudMessaging.getInstance(this);
            mRegId = getRegistrationId(this);
            if (mRegId.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i("com.projetoles.verso", "No valid Google Play Services APK found.");
        }
        
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

    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }

}
