package com.projetoles.adapter;

import java.util.Comparator;

import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Usuario;
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
 
public class ListUsuarioAdapter extends ScrollableList<Usuario> {
 
    public ListUsuarioAdapter(Activity context, View loading, ListView listView, 
    		ObjectListID<Usuario> listUsuarios, Comparator<PreloadedObject<Usuario> > comparator) {
    	super(context, loading, listView, listUsuarios, new UsuarioController(context), comparator);
    }
	
    public View getView(int position, View convertView, ViewGroup parent){
    	if (convertView == null) {
  			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  			convertView = inflater.inflate(R.layout.list_group_comentario, parent, false);
  		}
    	final ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
    	final TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
    	final TextView comentario = (TextView) convertView.findViewById(R.id.comment);
    	final TextView data = (TextView) convertView.findViewById(R.id.date);
    	return super.getView(position, convertView, parent, new OnRequestListener<Usuario>(mContext) {
			
			@Override
			public void onSuccess(final Usuario usuario) {
				nome.setText(usuario.getNome());
				comentario.setVisibility(View.GONE);
				data.setText("Seguido por " + usuario.getNumSeguidores() + " pessoa(s).");
				setPhoto(foto, usuario.getFoto());
				OnClickListener clicaUsuario = new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(mContext, UserProfileActivity.class);
						intent.putExtra("usuario", usuario);
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
