package com.projetoles.adapter;

import com.projetoles.controller.CurtidaController;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Curtida;
import com.projetoles.model.ObjectListID;
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
 
public class ListCurtidaAdapter extends ScrollableList<Curtida> {
 
    public ListCurtidaAdapter(Activity context, View loading, ListView listView, ObjectListID<Curtida> listCurtidas) {
    	super(context, loading, listView, listCurtidas, new CurtidaController(context));
    }
	
    public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_comentario, parent, false);
		}
		final Curtida c = this.mList.get(position).getLoadedObj();
		if (c != null) {
			convertView.setVisibility(View.VISIBLE);
			ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
			TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
			TextView comentario = (TextView) convertView.findViewById(R.id.comment);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			ImageView excluir = (ImageView) convertView.findViewById(R.id.excluir);
			excluir.setVisibility(View.GONE);
			nome.setText(c.getPostador().getNome() + " curtiu esta poesia.");
			comentario.setVisibility(View.GONE);
			data.setText("Curtido em " + CalendarUtils.getDataFormada(c.getDataCriacao()));
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
		} else {
			convertView.setVisibility(View.GONE);
		}
		return convertView;
	}

}
