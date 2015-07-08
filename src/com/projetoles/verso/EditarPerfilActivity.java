package com.projetoles.verso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;

public class EditarPerfilActivity extends Activity {
	
	private UsuarioController mController;
	private RelativeLayout mProfilePhotoContent;
	private ImageView mFoto;
	private ImageView mFotoFull;

	private static final int MAX_PHOTO_SIZE = 600;
	private static final int SELECT_PHOTO = 100;
	private static final int CAMERA_REQUEST = 1888; 
	
	
	private void setPhoto(byte[] photo) {
		if (UsuarioController.usuarioLogado.getFoto().length > 0) {
			Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editar_perfil);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mController = new UsuarioController(this);
		mFoto = (ImageView) findViewById(R.id.showPhoto);
		mFotoFull = (ImageView) findViewById(R.id.editarFotoFull);
		Button editarFoto = (Button) findViewById(R.id.btnEditPhoto);
		editarFoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(EditarPerfilActivity.this)
					.setTitle("Editar foto")
					.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
							photoPickerIntent.setType("image/*");
							startActivityForResult(photoPickerIntent, SELECT_PHOTO);    
						}
					})
					.setNeutralButton("Câmera", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
			                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
						}
					})
					.create().show();
			}
		
		});
		EditText nome = (EditText) findViewById(R.id.etEditName);
		EditText biografia = (EditText) findViewById(R.id.etEditBio);
		EditText senha = (EditText) findViewById(R.id.etEditPassword);
		EditText editarSenha = (EditText) findViewById(R.id.etEditPasswordAgain);
		Button salvarPerfil = (Button) findViewById(R.id.btnSalvarPerfil);
		setPhoto(UsuarioController.usuarioLogado.getFoto());
		mProfilePhotoContent = (RelativeLayout) findViewById(R.id.editarFotoContent);
		mFoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mProfilePhotoContent.setVisibility(View.VISIBLE);
			}
		});
		mProfilePhotoContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mProfilePhotoContent.setVisibility(View.GONE);;
			}
		});
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
        if (resultCode == RESULT_OK) { 
        	Bitmap bitmap = null;
        	//camera
        	if (requestCode == CAMERA_REQUEST) {
        		Bundle extras = imageReturnedIntent.getExtras();
    	        bitmap = (Bitmap) extras.get("data");
        	//galeria
        	} else {
        		Uri selectedImage = imageReturnedIntent.getData();
	            InputStream imageStream = null;
				try {
					imageStream = getContentResolver().openInputStream(selectedImage);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bitmap = BitmapFactory.decodeStream(imageStream);
        	}
        	if (bitmap.getWidth() > MAX_PHOTO_SIZE) {
        		int width = MAX_PHOTO_SIZE;
        		int height = (int)((float)bitmap.getHeight() / bitmap.getWidth() * MAX_PHOTO_SIZE);
        		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        	}
        	if (bitmap.getHeight() > MAX_PHOTO_SIZE) {
        		int width = (int)((float)bitmap.getWidth() / bitmap.getHeight() * MAX_PHOTO_SIZE);
        		int height = MAX_PHOTO_SIZE;
        		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        	}
        	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        	byte[] b = stream.toByteArray();
        	mController.addFoto(UsuarioController.usuarioLogado, b, new OnRequestListener(this) {
				
				@Override
				public void onSuccess(Object result) {
					setPhoto(UsuarioController.usuarioLogado.getFoto());
					mProfilePhotoContent.setVisibility(View.GONE);
				}
				
				@Override
				public void onError(String errorMessage) {
					new AlertDialog.Builder(EditarPerfilActivity.this)
						.setTitle("Um erro ocorreu")
						.setMessage(errorMessage)
						.setNeutralButton("OK", null)
						.create().show();
				}
			});
        }
	}
	
}
