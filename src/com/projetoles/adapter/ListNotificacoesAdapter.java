package com.projetoles.adapter;

import java.util.Comparator;

import com.projetoles.controller.NotificacaoController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Notificacao;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;
import com.projetoles.verso.ComentarioActivity;
import com.projetoles.verso.MainActivity;
import com.projetoles.verso.R;
import com.projetoles.verso.UserProfileActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListNotificacoesAdapter extends ScrollableList<Notificacao> {

	public ListNotificacoesAdapter(Activity context, View loading, ListView listView, 
			ObjectListID<Notificacao> listNotificacoes, Comparator<PreloadedObject<Notificacao> > comparator) {
		super(context, loading, listView, listNotificacoes, new NotificacaoController(context), comparator);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null) {
  			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  			convertView = inflater.inflate(R.layout.list_group_comentario, parent, false);
  		}
		final ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
		final TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
		final TextView comentario = (TextView) convertView.findViewById(R.id.comment);
		final TextView data = (TextView) convertView.findViewById(R.id.date);
		final ImageView excluir = (ImageView) convertView.findViewById(R.id.excluir);
		return super.getView(position, convertView, parent, new OnRequestListener<Notificacao>(mContext) {

			@Override
			public void onSuccess(final Notificacao n) {
				excluir.setVisibility(View.VISIBLE);
				excluir.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						v.setVisibility(View.GONE);
						NotificacaoController controller = new NotificacaoController(mContext);
						controller.delete(n.getId(), new OnRequestListener<String>(mContext) {
							
							@Override
							public void onSuccess(String result) {
								mList.remove(n.getId());
								notifyDataSetChanged();
							} 
							
							@Override
							public void onError(String errorMessage) {
								System.out.println(errorMessage);
							}

							@Override
							public void onTimeout() {
								// TODO Auto-generated method stub
								
							}
						});
					}
				});
				nome.setText(n.getTitulo().getNome() + n.getMensagem());
				comentario.setVisibility(View.GONE);
				data.setText("Em " + CalendarUtils.getDataFormada(n.getDataCriacao()));
				setPhoto(foto, n.getTitulo().getFoto());
				OnClickListener clicaUsuario = new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(n.getTipo().equals("usuario")){
							Intent intent = new Intent(mContext, UserProfileActivity.class);
							intent.putExtra("usuario", n.getTitulo());
							mContext.startActivity(intent);
						} else {
							Intent intent = new Intent(mContext, ComentarioActivity.class);
							intent.putExtra("poesia", (Poesia) n.getPoesia());
							intent.putExtra("usuario", mUsuario);
							intent.putExtra("callback", MainActivity.class);
							mContext.startActivity(intent);
							mContext.finish();
						}	
					}
				};
				nome.setOnClickListener(clicaUsuario);
				foto.setOnClickListener(clicaUsuario);
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
