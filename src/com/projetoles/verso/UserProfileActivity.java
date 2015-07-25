package com.projetoles.verso;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;

public class UserProfileActivity extends Activity {

	
	private UsuarioController mController;
	private ImageView mUserPicturePreview;
	private ImageView mUserPicture;
	private RelativeLayout mProfilePhotoContent;
	private Usuario mUsuario; 
	private PoesiaController mPoesiaController;
	private Poesia mPoesia;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		getActionBar().hide();
		
		Intent intent = getIntent();
		
		String poema = intent.getStringExtra("poema");
		String titulo = intent.getStringExtra("poemaTitulo");
		String autor = intent.getStringExtra("poemaAutor");
		String tag = intent.getStringExtra("poemaTag");
		
		mPoesiaController.pesquisar(titulo, autor, tag, poema, new OnRequestListener(this) {
			
			@Override
			public void onSuccess(Object result) {
				mPoesia = (Poesia) result;
				
			}
			
			@Override
			public void onError(String errorMessage) {
				finish();
				
			}
		});

		mController = new UsuarioController(this);
		mController.getUsuario(mPoesia.getPostador(), new OnRequestListener(this) {
			
			@Override
			public void onSuccess(Object result) {
				mUsuario = (Usuario) result;
				setUp();
				
			}
			
			@Override
			public void onError(String errorMessage) {
				finish();
				
			}
		});
						
	}
	
	public Bitmap getCroppedBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	            bitmap.getWidth() / 2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
	    //return _bmp;
	    return output;
	}
	
	
	
	private void setUp() {

		// set userName
		Usuario usuario = mUsuario;
		TextView usuarioName = (TextView) findViewById(R.id.otherUserName);
		usuarioName.setText(usuario.getNome());

		// submenu
		TextView biografia = (TextView) findViewById(R.id.otherTextBiografia);
		biografia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserProfileActivity.this,
						BiografiaActivity.class);
				intent.putExtra("usuario", mUsuario);
				intent.putExtra("callback", MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// set userPicture, precisa consertar nessa classe
		mUserPicturePreview = (ImageView) findViewById(R.id.otherProfilePhoto);
		mUserPicture = (ImageView) findViewById(R.id.otherUserPicture);
		mProfilePhotoContent = (RelativeLayout) findViewById(R.id.otherProfilePhoto);
		
		mUserPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mProfilePhotoContent.setVisibility(View.VISIBLE);
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
