package com.projetoles.verso;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.projetoles.controller.UsuarioController;

public class EditarPerfilActivity extends Activity {
	
	private UsuarioController mController;
	private ImageView mFoto;
	
	private void setPhoto(byte[] photo) {
		if (UsuarioController.usuarioLogado.getFoto().length > 0) {
			Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
			/*DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			if (bmp.getHeight() > dm.heightPixels / 2) {
				int width = (int)((float)bmp.getWidth() / bmp.getHeight() * dm.heightPixels / 2);
				int height = dm.heightPixels / 2;
				bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
			}*/
			mFoto.setImageBitmap(bmp);
		} else {
			mFoto.setImageResource(R.drawable.icone_foto);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editar_perfil);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mController = new UsuarioController(this);
		mFoto = (ImageView) findViewById(R.id.showPhoto);
		Button editarFoto = (Button) findViewById(R.id.btnEditPhoto);
		EditText nome = (EditText) findViewById(R.id.etEditName);
		EditText biografia = (EditText) findViewById(R.id.etEditBio);
		EditText senha = (EditText) findViewById(R.id.etEditPassword);
		EditText editarSenha = (EditText) findViewById(R.id.etEditPasswordAgain);
		Button salvarPerfil = (Button) findViewById(R.id.btnSalvarPerfil);
		setPhoto(UsuarioController.usuarioLogado.getFoto());
		nome.setText(UsuarioController.usuarioLogado.getNome());
		biografia.setText(UsuarioController.usuarioLogado.getBiografia());
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
