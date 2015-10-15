package com.projetoles.verso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.provider.MediaStore.Images;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.projetoles.model.ImageUtils;
import com.projetoles.verso.ApplicationData;
import com.projetoles.verso.InstagramApp;
import com.projetoles.verso.InstagramApp.OAuthAuthenticationListener;

public class SharingInstagramActivity extends Activity {
	private InstagramApp mApp;
	private LinearLayout llAfterLoginView;
	private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == InstagramApp.WHAT_FINALIZE) {
				userInfoHashmap = mApp.getUserInfo();
			} else if (msg.what == InstagramApp.WHAT_FINALIZE) {
				Toast.makeText(SharingInstagramActivity.this,
						"Check your network.", Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	});

	private void createInstagramIntent(String titulo, String texto) {

		Bitmap image = ImageUtils.drawTextToBitmap(SharingInstagramActivity.this, R.drawable.share, texto );

		 // Create the new Intent using the 'Send' action.
	    Intent share = new Intent(Intent.ACTION_SEND);

	    // Set the MIME type
	    share.setType("image/*");
	    
	 // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri uri = getImageUri(getApplicationContext(), image);

	    // Add the URI to the Intent.
	    share.putExtra(Intent.EXTRA_STREAM, uri);
	    
	    startActivity(Intent.createChooser(share, titulo));
	}

	private Uri getImageUri(Context applicationContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(SharingInstagramActivity.this.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
				ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);

		if (!mApp.hasAccessToken())
			mApp.authorize();

		Bundle b = getIntent().getExtras();
		final String titulo = (String) b.get("titulo");
		String texto = (String) b.get("texto");
		createInstagramIntent(titulo, texto);
		
		if (texto.length() > 500) {
			texto = texto.substring(0, 500) + "... Veja mais em: https://play.google.com/store/apps/details?id=com.projetoles.verso";
		}

		mApp.setListener(new OAuthAuthenticationListener() {

			@Override
			public void onSuccess() {
				mApp.fetchUserName(handler);
			}

			@Override
			public void onFail(String error) {
				Toast.makeText(SharingInstagramActivity.this, error,
						Toast.LENGTH_SHORT).show();
			}
		});

	}
}
