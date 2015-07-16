package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.projetoles.controller.NotificacaoController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Comentario;
import com.projetoles.model.Notificacao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RelativeLayout;

public class CriaNotificacoesActivity extends Activity {

	private UsuarioController mUsuarioController;
	private NotificacaoController mNotificacaoController;
	private List<Notificacao> notificacoes;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_group);

		mUsuarioController = new UsuarioController(this);
		mNotificacaoController = new NotificacaoController(this);

		final RelativeLayout loading = (RelativeLayout) MainActivity.sInstance
				.findViewById(R.id.mainLoading);

		notificacoes = new ArrayList<Notificacao>();
		
		for (String n1 : mUsuarioController.usuarioLogado
				.getNotificacaoes()) {
			loading.setVisibility(View.VISIBLE);
			
			mNotificacaoController.getNotificacao(n1, new OnRequestListener(this) {
				
				@Override
				public void onSuccess(Object result) {
					Notificacao n2 = (Notificacao) result;
					gerarNotificacao(n2.getEnderecado(), n2.getTitulo(), n2.getMensagem());
					gerarNotificacao();
				}

				@Override
				public void onError(String errorMessage) {
					// TODO Auto-generated method stub
					System.out.println(errorMessage);
				}
			});
			
		}
		

	}
	
	public void gerarNotificacao(String texto, String titulo, String mensagem){
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, PerfilActivity.class), 0);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setTicker("Ticker Texto");
		builder.setContentTitle("Título");
		builder.setContentText("Descrição");
		builder.setSmallIcon(R.drawable.logo2);
		builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.like_icon));
		builder.setContentIntent(p);
		
		Notification n = builder.build();
		n.vibrate = new long[]{150, 300, 150, 600};
		n.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(R.drawable.like_icon, n);
		
		try{
			Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone toque = RingtoneManager.getRingtone(this, som);
			toque.play();
		}
		catch(Exception e){}
	}
	
	public void gerarNotificacao(){
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, PerfilActivity.class), 0);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setTicker("Ticker Texto");
		builder.setContentTitle("Título");
		builder.setContentText("Descrição");
		builder.setSmallIcon(R.drawable.logo2);
		builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.like_icon));
		builder.setContentIntent(p);
		
		Notification n = builder.build();
		n.vibrate = new long[]{150, 300, 150, 600};
		n.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(R.drawable.like_icon, n);
		
		try{
			Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone toque = RingtoneManager.getRingtone(this, som);
			toque.play();
		}
		catch(Exception e){}
	}
}