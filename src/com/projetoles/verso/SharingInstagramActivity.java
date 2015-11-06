package com.projetoles.verso;

import java.io.ByteArrayOutputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import com.projetoles.model.ImageUtils;

public class SharingInstagramActivity extends Activity {

	private void createInstagramIntent(String titulo, String texto) {
		Bitmap image = ImageUtils.drawTextToBitmap(SharingInstagramActivity.this, R.drawable.share, texto, titulo.length());

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
	    inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		String path = Images.Media.insertImage(SharingInstagramActivity.this.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		Bundle b = getIntent().getExtras();
		final String titulo = (String) b.get("titulo");
		String texto = (String) b.get("texto");

		if (texto.length() > 300) {
			texto = texto.substring(0, 300) + "...";
		}
		texto += "\n\n#AppVer(só)\nBaixe agora mesmo em:\nhttps://goo.gl/g8f0Lj";

		createInstagramIntent(titulo, texto);
		

	}
}
