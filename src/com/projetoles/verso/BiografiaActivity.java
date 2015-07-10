package com.projetoles.verso;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projetoles.controller.UsuarioController;
import com.projetoles.model.Usuario;

public class BiografiaActivity extends Activity {
	
	private ImageView mFoto;
	private ImageView mFotoFull;
	private Class mCallback;
	
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
	
	private void setPhoto(byte[] photo) {
		if (UsuarioController.usuarioLogado.getFoto().length > 0) {
			Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
			bmp = getCroppedBitmap(bmp);
			mFoto.setImageBitmap(bmp);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			if (bmp.getHeight() > dm.heightPixels / 2) {
				int width = (int)((float)bmp.getWidth() / bmp.getHeight() * dm.heightPixels / 2);
				int height = dm.heightPixels / 2;
				bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
			}
			mFotoFull.setImageBitmap(bmp);
		} else {
			mFoto.setImageResource(R.drawable.icone_foto);
		}
	}
	
	private Usuario mUsuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biografia);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle b = getIntent().getExtras();
		mCallback = (Class) b.get("callback");
		mUsuario = (Usuario) b.getParcelable("usuario");
		TextView editBio = (TextView) findViewById(R.id.biografia);
		if (mUsuario.getBiografia() == null || mUsuario.getBiografia().trim().equals("")) {
			editBio.setText("Este usu�rio n�o tem biografia.");
		} else {
			editBio.setText(mUsuario.getBiografia());
		}
		mFotoFull = (ImageView) findViewById(R.id.biografiaFullPhoto);
 		mFoto = (ImageView) findViewById(R.id.biografiaFoto);
		setPhoto(mUsuario.getFoto());
		final RelativeLayout biografiaPhotoContent = (RelativeLayout) findViewById(R.id.biografiaPhotoContent);
		mFoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				biografiaPhotoContent.setVisibility(View.VISIBLE);
			}
		});
		biografiaPhotoContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				biografiaPhotoContent.setVisibility(View.GONE);
			}
		});
		Button editarPerfil = (Button) findViewById(R.id.btnEditarPerfil);
		if (!mUsuario.equals(UsuarioController.usuarioLogado)) {
			editarPerfil.setVisibility(View.GONE);
		}
		editarPerfil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(BiografiaActivity.this, EditarPerfilActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(BiografiaActivity.this, mCallback);
		startActivity(i);
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