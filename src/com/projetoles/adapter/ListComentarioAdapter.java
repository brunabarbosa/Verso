package com.projetoles.adapter;

import java.util.Comparator;

import com.projetoles.controller.ComentarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Comentario;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.PreloadedObject;
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

public class ListComentarioAdapter extends ScrollableList<Comentario> {
 
    public ListComentarioAdapter(Activity context, View loading, ListView listView,
    		ObjectListID<Comentario> listComentarios, Comparator<PreloadedObject<Comentario>> comparator) {
    	super(context, loading, listView, listComentarios, new ComentarioController(context), comparator);
    }
	 
    public View getView(int position, View convertView, ViewGroup parent) {
    	if (convertView == null) {
  			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  			convertView = inflater.inflate(R.layout.list_group_comentario, parent, false);
  		}
    	final ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
    	final TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
    	final TextView comentario = (TextView) convertView.findViewById(R.id.comment);
    	final TextView data = (TextView) convertView.findViewById(R.id.date);
		final ImageView excluir = (ImageView) convertView.findViewById(R.id.excluir);
    	return super.getView(position, convertView, parent, new OnRequestListener<Comentario>(mContext) {

			@Override
			public void onSuccess(final Comentario c) {
				if (c.getPostador().equals(mUsuario)) {
					excluir.setVisibility(View.VISIBLE);
					excluir.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							v.setVisibility(View.GONE);
							ComentarioController controller = new ComentarioController(mContext);
							controller.delete(c.getId(), new OnRequestListener<String>(mContext) {
								
								@Override
								public void onSuccess(String result) {
									mList.remove(c.getId());
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
				} else {
					excluir.setVisibility(View.GONE);
				}
				nome.setText(c.getPostador().getNome() + " diz:");
				comentario.setText(c.getComentario());
				data.setText("Postado em " + CalendarUtils.getDataFormada(c.getDataCriacao()));
				setPhoto(foto, c.getPostador().getFoto());
				OnClickListener clicaUsuario = new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(mContext, UserProfileActivity.class);
						intent.putExtra("usuario", c.getPostador());
						mContext.startActivity(intent);
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
