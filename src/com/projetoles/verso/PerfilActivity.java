package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;

public class PerfilActivity extends Activity {

	private UsuarioController usuarioController;
	private PoesiaController poemaController;
	private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<Poesia> listPoesias;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		//change fonts
		usuarioController = new UsuarioController(this);
		poemaController = new PoesiaController(this);

		// get the listview 
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		
		//preparing list data
		listPoesias = new ArrayList<Poesia>();
        listAdapter = new ExpandableListAdapter(PerfilActivity.this, listPoesias);
		
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
	        int previousGroup = -1;

	        @Override
	        public void onGroupExpand(int groupPosition) {
	            if(groupPosition != previousGroup)
	            	expListView.collapseGroup(previousGroup);
	            previousGroup = groupPosition;
	        }
	    });
		
		//setting the list adapter
		expListView.setAdapter(listAdapter);
		listPoesias.addAll(usuarioController.usuarioLogado.getPoemasCarregados());
		Collections.sort(listPoesias);
		listAdapter.notifyDataSetChanged();
		for (String id : usuarioController.usuarioLogado.getPoemas()) {
			poemaController.getPoesia(id, new OnRequestListener(this) {
				
				@Override
				public void onSuccess(Object result) {
					Poesia p = (Poesia) result;
					listPoesias.add(p);
					Collections.sort(listPoesias);
			        listAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
				}
			});
		}
	}
	
public void gerarNotificacao(View view){
		
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
