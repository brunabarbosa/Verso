package com.projetoles.verso;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Notificacao;

public class ListNotificacoesAdapter extends BaseAdapter {

	private Context mContext;
	private List<Notificacao> mListNotificacoes;

	public ListNotificacoesAdapter(Context context, List<Notificacao> listNotificacoes) {
    	this.mContext = context;
        this.mListNotificacoes = listNotificacoes;
	}

	@Override
	public int getCount() {
		return this.mListNotificacoes.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mListNotificacoes.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_notificacao, parent, false);
		}
		final Notificacao n = this.mListNotificacoes.get(position);
		if (n != null) {
			TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			nome.setText(n.getTitulo().getNome() + " " + n.getMensagem());
			data.setText("Em " + CalendarUtils.getDataFormada(n.getDataCriacao()));
			nome.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, UserProfileActivity.class);
					intent.putExtra("usuario", n.getTitulo());
					mContext.startActivity(intent);
				}
			});
		}
		return convertView;
	}

}
