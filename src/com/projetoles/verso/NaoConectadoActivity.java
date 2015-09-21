package com.projetoles.verso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NaoConectadoActivity extends Activity {

	private Button mBtnTentarNovamente;

	// check network connection
    public boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) 
        		context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
 
    private void irPraTelaDeLogin() {
    	Intent i = new Intent(NaoConectadoActivity.this, LoginActivity.class);
		startActivity(i);
		finish();
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isConnected(this)) {
			irPraTelaDeLogin();
		} else {
			setContentView(R.layout.activity_nao_conectado);
			mBtnTentarNovamente = (Button) findViewById(R.id.notConnected_btTryAgain);
			mBtnTentarNovamente.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if (isConnected(NaoConectadoActivity.this)) {
						irPraTelaDeLogin();
					} else {
						Toast.makeText(NaoConectadoActivity.this, "Sem sucesso", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}
	
}
