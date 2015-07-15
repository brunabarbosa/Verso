package com.projetoles.verso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Usuario;

public class MainActivity extends TabActivity {
	
	public static TabActivity sInstance;
	
	private static final int MAX_PHOTO_SIZE = 600;
	private static final int SELECT_PHOTO = 100;
	private static final int CAMERA_REQUEST = 1888; 
	
	private UsuarioController mController;
	private TabHost mTabHost;
	private ImageView mBtnCriarPoema;
	private ImageView mBtnPesquisar;
	private ImageView mUserPicturePreview;
	private ImageView mUserPicture;
	private RelativeLayout mProfilePhotoContent;
	private Usuario mUsuario;
	
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
		ImageView userPicture = (ImageView) findViewById(R.id.userPicture);
		if (mUsuario.getFoto().length > 0) {
			Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
			bmp = getCroppedBitmap(bmp);
			userPicture.setImageBitmap(bmp);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			if (bmp.getHeight() > dm.heightPixels / 2) {
				int width = (int)((float)bmp.getWidth() / bmp.getHeight() * dm.heightPixels / 2);
				int height = dm.heightPixels / 2;
				bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
			}
			mUserPicturePreview.setImageBitmap(bmp);
		} else {
			mUserPicturePreview.setImageResource(R.drawable.icone_foto);
		}
	}
	
	private void setUp() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		TabSpec homeSpec = mTabHost.newTabSpec("Home");
		TabSpec comporSpec = mTabHost.newTabSpec("Compor");
		TabSpec sobreSpec = mTabHost.newTabSpec("Sobre");
		TabSpec buscaSpec = mTabHost.newTabSpec("Home");
		
		homeSpec.setIndicator("", getResources().getDrawable(R.drawable.home));
		Intent homeIntent = new Intent(MainActivity.this, PerfilActivity.class);
		homeSpec.setContent(homeIntent);
		 
		comporSpec.setIndicator("", getResources().getDrawable(R.drawable.compor));
		Intent comporIntent = new Intent(MainActivity.this, CriarPoesiaActivity.class);
		comporSpec.setContent(comporIntent);

		sobreSpec.setIndicator("", getResources().getDrawable(R.drawable.sobre));
		Intent sobreIntent = new Intent(MainActivity.this, CriarPoesiaActivity.class);
		sobreSpec.setContent(sobreIntent);

		buscaSpec.setIndicator("", getResources().getDrawable(R.drawable.busca));
		Intent buscaIntent = new Intent(MainActivity.this, PesquisarActivity.class);
		buscaSpec.setContent(buscaIntent);
		
		mTabHost.addTab(homeSpec);
		mTabHost.addTab(comporSpec);
		mTabHost.addTab(sobreSpec);
		mTabHost.addTab(buscaSpec);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				if (mTabHost.getCurrentTab() == 1) {
					mBtnCriarPoema.setVisibility(View.VISIBLE);
				} else {
					mBtnCriarPoema.setVisibility(View.GONE);
				}
				if (mTabHost.getCurrentTab() == 3) {
					mBtnPesquisar.setVisibility(View.VISIBLE);
				} else {
					mBtnPesquisar.setVisibility(View.GONE);
				}
				setTabColor(mTabHost);
			}
		});
		setTabColor(mTabHost);
		
		mBtnCriarPoema = (ImageView) findViewById(R.id.btnCriarPoema);
		mBtnPesquisar = (ImageView) findViewById(R.id.btnPesquisar);
		
		//set userName
		Usuario usuario = mUsuario;
		TextView usuarioName = (TextView) findViewById(R.id.userName);
		usuarioName.setText(usuario.getNome());
		
		// submenu
		TextView biografia = (TextView) findViewById(R.id.textBiografia);
		biografia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, BiografiaActivity.class);
				intent.putExtra("usuario", mUsuario);
				intent.putExtra("callback", MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		//set userPicture
		mUserPicturePreview = (ImageView) findViewById(R.id.profilePhoto);
		mUserPicture = (ImageView) findViewById(R.id.userPicture);
		mProfilePhotoContent = (RelativeLayout) findViewById(R.id.profilePhotoContent);
		setPhoto(usuario.getFoto());
		mUserPicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mProfilePhotoContent.setVisibility(View.VISIBLE);
			}
		});
		mProfilePhotoContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mProfilePhotoContent.setVisibility(View.GONE);
			}
		});
		final Button btnEditPhoto = (Button) findViewById(R.id.btnProfilePhotoEdit);
		btnEditPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(MainActivity.this)
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
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabbed_main);
		
		sInstance = this;
		
		getActionBar().hide(); 

		mController = new UsuarioController(this);
		mController.getLoggedUser(new OnRequestListener(this) {
			
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
	
	private void setTabColor(TabHost tabhost) {
		for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
			tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#f8f5af")); //unselected
		}
		if(tabhost.getCurrentTab() == 0) {
			tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#F5DA81")); //1st tab selected
		} else {
			tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#F5DA81")); //2nd tab selected
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		} else if (id == R.id.action_logout) {
			mController.logout();
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
        if(resultCode == RESULT_OK){ 
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
        	mController.addFoto(mUsuario, b, new OnRequestListener(this) {
				
				@Override
				public void onSuccess(Object result) {
					setPhoto(mUsuario.getFoto());
					mProfilePhotoContent.setVisibility(View.GONE);
				}
				
				@Override
				public void onError(String errorMessage) {
					new AlertDialog.Builder(MainActivity.this)
						.setTitle("Um erro ocorreu")
						.setMessage(errorMessage)
						.setNeutralButton("OK", null)
						.create().show();
				}
			});
        }
	}
	
}
