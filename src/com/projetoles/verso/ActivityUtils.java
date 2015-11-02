package com.projetoles.verso;

import com.projetoles.adapter.ExpandablePoesiaAdapter;
import com.projetoles.controller.CompartilhamentoController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Compartilhamento;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedListener;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class ActivityUtils  {

	public static void loadSharedPoetries(Context context, final Usuario usuario, 
			final ObjectListID<Poesia> listPoesias, final ExpandablePoesiaAdapter adapter, final boolean isOnFeed) {
		for (PreloadedObject<Compartilhamento> id : usuario.getCompartilhamentos().getList()) {
			id.load(new CompartilhamentoController(context), new OnRequestListener<Compartilhamento>(context) {

				@Override
				public void onSuccess(final Compartilhamento result) {
					if (listPoesias.contains(result.getPoesia().getId())) {
						PreloadedObject<Poesia> poesia = listPoesias.getById(result.getPoesia().getId());
						if (poesia.isLoaded()) {
							if (result.getDataCriacao().after(poesia.getPureLoadedObj().getDataCriacao()) || !isOnFeed) {
								poesia.getPureLoadedObj().setDataCriacao(result.getDataCriacao());
								poesia.getPureLoadedObj().getCompartilhadoPor().add(result);
							}
						} else {
							poesia.getListeners().add(new PreloadedListener<Poesia>() {
								
								@Override
								public void onLoad(Poesia obj) {
									if (result.getDataCriacao().after(obj.getDataCriacao()) || !isOnFeed) {
										obj.setDataCriacao(result.getDataCriacao());
										obj.getCompartilhadoPor().add(result);
									}
								}
							}); 
						}
					} else {
						PreloadedObject<Poesia> poesia = new PreloadedObject<Poesia>(result.getPoesia().getDataCriacao(), result.getPoesia().getId());
						result.getPoesia().setDataCriacao(result.getDataCriacao());
						poesia.setLoadedObj(result.getPoesia());
						listPoesias.add(poesia);
					}
					listPoesias.sort(); 
					adapter.hideEmptyMessage();
					adapter.notifyDataSetChanged();
				}

				@Override
				public void onError(String errorMessage) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTimeout() {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
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
