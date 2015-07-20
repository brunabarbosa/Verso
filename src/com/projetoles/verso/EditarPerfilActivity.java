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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Usuario;

public class EditarPerfilActivity extends Activity {
	
	private UsuarioController mController;
	private RelativeLayout mProfilePhotoContent;
	private RelativeLayout mLoading;
	private ImageView mFoto;
	private ImageView mFotoFull;
	private Usuario mUsuario;

	private static final int MAX_PHOTO_SIZE = 600;
	private static final int SELECT_PHOTO = 100;
	private static final int CAMERA_REQUEST = 1888; 
	
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
		if (mUsuario.getFoto().length > 0) {
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editar_perfil);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mController = new UsuarioController(this);
		mController.getLoggedUser(new OnRequestListener(this) {
			
			@Override
			public void onSuccess(Object result) {
				mUsuario = (Usuario) result;
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
				
				final EditText nome = (EditText) findViewById(R.id.etEditName);
				final EditText biografia = (EditText) findViewById(R.id.etEditBio);
				final EditText senha = (EditText) findViewById(R.id.etEditPassword);
				final EditText editarSenha = (EditText) findViewById(R.id.etEditPasswordAgain);
				final Button salvarPerfil = (Button) findViewById(R.id.btnSalvarPerfil);
				if(AccessToken.getCurrentAccessToken()!=null){
					senha.setVisibility(View.INVISIBLE);
					editarSenha.setVisibility(View.INVISIBLE);
				}
				salvarPerfil.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mLoading.setVisibility(View.VISIBLE);
						String sNome = nome.getText().toString();
						String sBiografia = biografia.getText().toString();
						String sSenha = senha.getText().toString();
						String seditarSenha = editarSenha.getText().toString();
						mController.editUser(sNome, sBiografia, sSenha, seditarSenha, new OnRequestListener(EditarPerfilActivity.this) {
							
							@Override
							public void onSuccess(Object result) {
								mLoading.setVisibility(View.GONE);
								new AlertDialog.Builder(EditarPerfilActivity.this)
									.setTitle("Sucesso!")
									.setMessage("Usuário alterado com sucesso.")
									.setNeutralButton("OK", null)
									.create().show();
							}
							
							@Override
							public void onError(String errorMessage) {
								mLoading.setVisibility(View.GONE);
								new AlertDialog.Builder(EditarPerfilActivity.this)
									.setTitle("Um erro ocorreu")
									.setMessage(errorMessage)
									.setNeutralButton("OK", null)
									.create().show();	
							}
						});
					
					}
				});
				setPhoto(mUsuario.getFoto());
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
				nome.setText(mUsuario.getNome());
				biografia.setText(mUsuario.getBiografia());
				mLoading = (RelativeLayout) findViewById(R.id.editarPerfilLoading);
				mLoading.setVisibility(View.GONE);
			}
			
			@Override
			public void onError(String errorMessage) {
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(EditarPerfilActivity.this, BiografiaActivity.class);
		i.putExtra("usuario", mUsuario);
		i.putExtra("callback", MainActivity.class);
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
        	//mLoading.setVisibility(View.VISIBLE);
        	mController.addFoto(UsuarioController.usuarioLogado, b, new OnRequestListener(this) {
				
				@Override
				public void onSuccess(Object result) {
					mLoading.setVisibility(View.GONE);
					setPhoto(mUsuario.getFoto());
					mProfilePhotoContent.setVisibility(View.GONE);
				}
				
				@Override
				public void onError(String errorMessage) {
					mLoading.setVisibility(View.GONE);
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
