package com.projetoles.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import com.projetoles.verso.R;

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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.StyleSpan;
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
	
	public static int generateRandomColor() {
	    Random random = new Random();
	    int red = (random.nextInt(256) + 255 * 2) / 3;
	    int green = (random.nextInt(256) + 255 * 2) / 3;
	    int blue = (random.nextInt(256) + 255 * 2) / 3;
	    return Color.rgb(red, green, blue);
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    Bitmap bitmap = null;
	    if (drawable instanceof BitmapDrawable) {
	        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
	        if(bitmapDrawable.getBitmap() != null) {
	            return bitmapDrawable.getBitmap();
	        }
	    }
	    if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
	        bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
	    } else {
	        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
	    }

	    Canvas canvas = new Canvas(bitmap);
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);
	    return bitmap;
	}
	
	public static Bitmap drawTextToBitmap(Context gContext, int gResId, String gText, int titleSize) {
  		  Resources resources = gContext.getResources();
  		  float scale = resources.getDisplayMetrics().density;
  		  Bitmap bitmap =  BitmapFactory.decodeResource(resources, gResId);
  
  		  android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
  		  // set default bitmap config if none
  		  if(bitmapConfig == null) {
  		    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
  		  }
  		  // resource bitmaps are imutable, 
  		  // so we need to convert it to mutable one
  		  bitmap = bitmap.copy(bitmapConfig, true);
  		 
  		  Canvas canvas = new Canvas(bitmap);
  		  Paint paint = new Paint();
  		  paint.setStyle(Paint.Style.FILL);
  		  paint.setColor(generateRandomColor()); 
  		  canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

  		  int padding = ((int)(10 * scale));
  		  
  		  Drawable logoDrawable = gContext.getResources().getDrawable(R.drawable.logo2);
  		  Bitmap logo = drawableToBitmap(logoDrawable);
  		  logo = Bitmap.createScaledBitmap(logo, logo.getWidth() / 3, logo.getHeight() / 3, true);
  		  Paint logoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  		  canvas.drawBitmap(logo, canvas.getWidth() - logo.getWidth() - padding, 
  				  canvas.getHeight() - logo.getHeight() - padding, logoPaint);
  		  
  		  TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
  		  textPaint.setColor(Color.rgb(61, 61, 61));
  		  textPaint.setTextSize((int) (24 * scale));
  		  textPaint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
  		  textPaint.setTypeface(Typeface.createFromAsset(gContext.getAssets(), "fonts/cordelina.otf"));
  		  
  		  Spannable span = new SpannableString(gText);
  		  span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, titleSize, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
  		  
  		  StaticLayout textLayout = new StaticLayout(span, textPaint, canvas.getWidth() - 2 * padding, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
  		  canvas.save();
 
 		  canvas.translate(padding, padding);
 		  textLayout.draw(canvas);
 		  canvas.restore();
  		  //canvas.drawText(gText, x, y, paint);
  		 
  		  return bitmap;
  		}

}
