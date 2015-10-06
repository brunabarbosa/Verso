package com.projetoles.adapter;

import com.projetoles.controller.NotificacaoController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Notificacao;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
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
			ObjectListID<Notificacao> listNotificacoes) {
		super(context, loading, listView, listNotificacoes, new NotificacaoController(context));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_comentario, parent, false);
		}
		final Notificacao n = this.mList.get(position).getLoadedObj();
		if (n != null) {
			convertView.setVisibility(View.VISIBLE);
			ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
			TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
			TextView comentario = (TextView) convertView.findViewById(R.id.comment);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			ImageView excluir = (ImageView) convertView.findViewById(R.id.excluir);
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
						intent.putExtra("usuario", UsuarioController.usuarioLogado);
						intent.putExtra("callback", MainActivity.class);
						mContext.startActivity(intent);
						mContext.finish();
					}	
				}
			};
			nome.setOnClickListener(clicaUsuario);
			foto.setOnClickListener(clicaUsuario);
		} else {
			convertView.setVisibility(View.GONE);
		}
		return convertView;
	}

}
