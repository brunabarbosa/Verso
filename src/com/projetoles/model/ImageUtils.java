package com.projetoles.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;

public class ImageUtils {

	public static Bitmap getCroppedBitmap(Bitmap bitmap) {
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
	
	public static byte[] fileToByteArray(File file) throws IOException {
	    ByteArrayOutputStream ous = null;
	    InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ous = new ByteArrayOutputStream();
	        ios = new FileInputStream(file);
	        int read = 0;
	        while ( (read = ios.read(buffer)) != -1 ) {
	            ous.write(buffer, 0, read);
	        }
	    } finally { 
	        try {
	             if ( ous != null ) 
	                 ous.close();
	        } catch ( IOException e) {
	        }

	        try {
	             if ( ios != null ) 
	                  ios.close();
	        } catch ( IOException e) {
	        }
	    }
	    return ous.toByteArray();
	}

	public static byte[] getPhotoFromURL(String photo) throws IOException {
		 URL url = new URL(photo);
		 InputStream in = new BufferedInputStream(url.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n); 
		 }
		 out.close(); 
		 in.close();
		 return out.toByteArray();
	}
	
	public static String encode(byte[] data) {
		return Base64.encodeToString(data, Base64.DEFAULT);
	}
	 
	public static byte[] decode(String encodedImage) {
		return Base64.decode(encodedImage, Base64.DEFAULT);
	}
	
	public static Bitmap drawTextToBitmap(Context gContext, 
  		  int gResId, 
  		  String gText) {
  		  Resources resources = gContext.getResources();
  		  float scale = resources.getDisplayMetrics().density;
  		  Bitmap bitmap = 
  		      BitmapFactory.decodeResource(resources, gResId);
  		 
  		  android.graphics.Bitmap.Config bitmapConfig =
  		      bitmap.getConfig();
  		  // set default bitmap config if none
  		  if(bitmapConfig == null) {
  		    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
  		  }
  		  // resource bitmaps are imutable, 
  		  // so we need to convert it to mutable one
  		  bitmap = bitmap.copy(bitmapConfig, true);
  		 
  		  Canvas canvas = new Canvas(bitmap);
  		  // new antialised Paint
  		  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
  		  // text color - #3D3D3D
  		  paint.setColor(Color.rgb(61, 61, 61));
  		  // text size in pixels
  		  paint.setTextSize((int) (14 * scale));
  		  // text shadow
  		  paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
  		 
  		  // draw text to the Canvas center
  		  Rect bounds = new Rect();
  		  paint.getTextBounds(gText, 0, gText.length(), bounds);
  		  int x = (bitmap.getWidth() - bounds.width())/2;
  		  int y = (bitmap.getHeight() + bounds.height())/2;
  		 
  		  canvas.drawText(gText, x, y, paint);
  		 
  		  return bitmap;
  		}

	
}
