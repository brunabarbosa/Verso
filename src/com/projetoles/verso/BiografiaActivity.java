package com.projetoles.verso;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class BiografiaActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biografia);
	}
	
	public void fSalvaBio(View view){
		EditText edit_bio = (EditText) findViewById(R.id.biografia);
		String campoTxt = edit_bio.getText().toString();
		
		if(campoTxt == null ||campoTxt ==""){
			edit_bio.setError("Obrigatorio preenchimento");
		}else{
			Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
		}
	}
}
