package com.projetoles.verso;

import java.text.DateFormat;
import java.util.List;

import com.projetoles.model.Notificacao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListNotificacoesAdapter extends BaseAdapter {

	private Context mContext;
	private List<Notificacao> mListNotificacoes;

	public ListNotificacoesAdapter(Context context, List<Notificacao> listNotificacoes) {
    	this.mContext = context;
        this.mListNotificacoes = listNotificacoes;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_notificacao, parent, false);
		}
		Notificacao n = this.mListNotificacoes.get(position);
		if (n != null) {
			TextView nome = (TextView) convertView.findViewById(R.id.userName);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			nome.setText(n.getTitulo() + " " + n.getMensagem());
			data.setText("Postado em " + DateFormat.getDateInstance(DateFormat.SHORT).format(n.getDataCriacao().getTime()));
		}
		return convertView;
	}

}
