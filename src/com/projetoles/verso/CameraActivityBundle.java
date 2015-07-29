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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.ImageUtils;
import com.projetoles.model.Usuario;

public class CameraActivityBundle {

	private static final int MAX_PHOTO_SIZE = 600;
	private static final int SELECT_PHOTO = 100;
	private static final int CAMERA_REQUEST = 1888; 
	
	private Activity mContext;
	private ImageView mFotoPreview;
	private ImageView mFotoFull;
	private View mMask;
	
	public CameraActivityBundle(Activity context, ImageView fotoPreview, ImageView fotoFull, View mask) {
		this.mFotoPreview = fotoPreview;
		this.mFotoFull = fotoFull;
		this.mMask = mask;
		this.mContext = context;
		mFotoPreview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mMask.setVisibility(View.VISIBLE);
			}
		});
		mMask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mMask.setVisibility(View.GONE);
			}
		});
	}
	
	public ImageView getFotoPreview() {
		return this.mFotoPreview;
	}
	
	public ImageView getFotoFull() {
		return this.mFotoFull;
	}
	
	public View getMask() {
		return this.mMask;
	}
	
	public void setFoto(byte[] foto) {
		if (foto.length > 0) {
			Bitmap bmp = BitmapFactory.decodeByteArray(foto, 0, foto.length);
			bmp = ImageUtils.getCroppedBitmap(bmp);
			mFotoPreview.setImageBitmap(bmp);
			if (mFotoFull != null) {
				DisplayMetrics dm = new DisplayMetrics();
				mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
				if (bmp.getHeight() > dm.heightPixels / 2) {
					int width = (int)((float)bmp.getWidth() / bmp.getHeight() * dm.heightPixels / 2);
					int height = dm.heightPixels / 2;
					bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
				}
				mFotoFull.setImageBitmap(bmp);
			}
		} else {
			mFotoPreview.setImageResource(R.drawable.icone_foto);
			if (mFotoFull != null) {
				mFotoFull.setImageResource(R.drawable.icone_foto);
			}
		}
	}
	
	
	public void editarFoto(final Button editarFoto) {
		editarFoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mContext)
					.setTitle("Editar foto")
					.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
							photoPickerIntent.setType("image/*");
							mContext.startActivityForResult(photoPickerIntent, SELECT_PHOTO);    
						}
					})
					.setNeutralButton("Câmera", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
			                mContext.startActivityForResult(cameraIntent, CAMERA_REQUEST); 
						}
					})
					.create().show();
			}
		
		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent, final View loading) { 
		loading.setVisibility(View.VISIBLE);
        if (resultCode == mContext.RESULT_OK) { 
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
					imageStream = mContext.getContentResolver().openInputStream(selectedImage);
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
        	bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        	byte[] b = stream.toByteArray();
        	//mLoading.setVisibility(View.VISIBLE);
        	UsuarioController controller = new UsuarioController(mContext);
        	controller.setFoto(UsuarioController.usuarioLogado, b, new OnRequestListener<Usuario>(mContext) {
				
				@Override
				public void onSuccess(Usuario usuario) { 
					loading.setVisibility(View.GONE);
					setFoto(usuario.getFoto());
				}
				
				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
					ActivityUtils.showMessageDialog(mContext, "Um erro ocorreu", errorMessage, loading);
				}
			});
        }
    }
    
}
