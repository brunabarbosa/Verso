package com.projetoles.verso;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.projetoles.model.Poesia;

public class SharingActivity extends Activity{
	 
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        
        Bundle b = getIntent().getExtras();
		final String titulo = (String) b.get("titulo");
		final String texto =  (String) b.get("texto");
 
        FacebookSdk.sdkInitialize(getApplicationContext());
 
        callbackManager = CallbackManager.Factory.create();
 
        List<String> permissionNeeds = Arrays.asList("publish_actions");
 
        //this loginManager helps you eliminate adding a LoginButton to your UI
        LoginManager manager = LoginManager.getInstance();
 
        manager.logInWithPublishPermissions(this, permissionNeeds);
 
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                sharePhotoToFacebook(titulo, texto);
            }
 
            @Override
            public void onCancel()
            {
                System.out.println("onCancel");
            }
 
            @Override
            public void onError(FacebookException exception)
            {
                System.out.println("onError");
            }
        });
      
    } 	      
       
       
    }
    
    public Bitmap drawTextToBitmap(Context gContext, 
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

    private void sharePhotoToFacebook(String titulo, String texto){
       Bitmap image = drawTextToBitmap(SharingActivity.this, R.drawable.share, texto );
       
/*       ShareLinkContent content = new ShareLinkContent.Builder()
       .setContentUrl(Uri.parse("https://developers.facebook.com"))
       .build();
*/       
       
       SharePhoto photo = new SharePhoto.Builder()
               .setBitmap(image)
               .setCaption(titulo) //Texto da publicação
               .build();

       SharePhotoContent content = new SharePhotoContent.Builder()
               .addPhoto(photo)
               .build();

       ShareApi.share(content, null);

   }

   @Override
   protected void onActivityResult(int requestCode, int responseCode, Intent data)
   {
       super.onActivityResult(requestCode, responseCode, data);
       callbackManager.onActivityResult(requestCode, responseCode, data);
   
    }

}