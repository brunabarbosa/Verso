package com.projetoles.adapter;

import java.util.Comparator;

import com.projetoles.controller.SeguidaController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Seguida;
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
 
public class ListSeguidoresAdapter extends ScrollableList<Seguida>  {
 
    private boolean mSeguindo;
    
    public ListSeguidoresAdapter(Activity context, View loading, ListView listView, 
    		ObjectListID<Seguida> listCurtidas, Comparator<PreloadedObject<Seguida> > comparator, boolean seguindo) {
    	super(context, loading, listView, listCurtidas, new SeguidaController(context), comparator);
        this.mSeguindo = seguindo;
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
    	return super.getView(position, convertView, parent, new OnRequestListener<Seguida>(mContext) {
			
			@Override
			public void onSuccess(final Seguida s) {
				if (mSeguindo) {
					nome.setText(s.getSeguido().getNome());
					setPhoto(foto, s.getSeguido().getFoto());
					data.setText("Seguiu em " + CalendarUtils.getDataFormada(s.getDataCriacao()));
				} else {
					nome.setText(s.getSeguidor().getNome());
					setPhoto(foto, s.getSeguidor().getFoto());
					data.setText("Seguido desde " + CalendarUtils.getDataFormada(s.getDataCriacao()));
				}	
				comentario.setVisibility(View.GONE);
				OnClickListener clicaUsuario = new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(mContext, UserProfileActivity.class);
						if (mSeguindo) {
							intent.putExtra("usuario", s.getSeguido());
						} else {
							intent.putExtra("usuario", s.getSeguidor());
						}
						mContext.startActivity(intent);
					}
				};
				nome.setOnClickListener(clicaUsuario);
				foto.setOnClickListener(clicaUsuario);
			}

			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
