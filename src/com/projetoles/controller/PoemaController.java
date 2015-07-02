package com.projetoles.controller;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.dao.OnRequestListener;
import com.projetoles.dao.PoemaDAO;
import com.projetoles.model.Poema;

import android.app.Activity;

public class PoemaController extends Controller {
	
	private static PoemaDAO pDao = PoemaDAO.getInstance();

	public PoemaController(Activity context) {
		super(context);
	}

	public void CriarPoema(final String titulo, final String autor,
			final String poesia, final Calendar dataDeCriacao,
			final String tags, final OnRequestListener callback) {
		if (UsuarioController.usuarioLogado != null) {
			try {
				final Poema poema = new Poema(titulo, autor, poesia,
						dataDeCriacao, tags);
				pDao.criarPoema(poema, new OnRequestListener(callback.getContext()) {

					@Override
					public void onSuccess(Object result) {
						try {
							JSONObject json = new JSONObject(result.toString());
							boolean success = json.getBoolean("success");
							if (success) {
								callback.onSuccess(result);
							} else {
								callback.onError(json.getString("message"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
							callback.onError(e.getMessage());
						}
					}

					@Override
					public void onError(String errorMessage) {
						callback.onError(errorMessage);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				callback.onError(e.getMessage());
			}
		}else {
			callback.onError("Usuário não encontrado.");
		}
	}
	
}
