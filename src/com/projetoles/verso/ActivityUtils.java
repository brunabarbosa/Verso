package com.projetoles.verso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

public class ActivityUtils  {

	public static void showMessageDialog(final Activity context, final String title, final String message, 
			final View loading) {
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (loading != null) {
					loading.setVisibility(View.VISIBLE);
					new AlertDialog.Builder(context)
						.setTitle(title)
						.setMessage(message)
						.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								loading.setVisibility(View.GONE);
							}
						})
						.create().show();
				} else {
					new AlertDialog.Builder(context)
						.setTitle(title)
						.setMessage(message)
						.setNeutralButton("OK", null)
						.create().show();
				}
			}
		});
	}
	
}
