package com.projetoles.verso;

import com.projetoles.adapter.ListNotificacoesAdapter;
import com.projetoles.controller.UsuarioController;
import com.projetoles.model.Notificacao;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.PreloadedObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class NotificacoesTelaActivity extends Activity {

	private ListNotificacoesAdapter mListAdapter;
	private ObjectListID<Notificacao> mListNotificacoes;
	private ListView mListView;
	private RelativeLayout mLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificacoes);

		// Get widgets from layout
		mListView = (ListView) findViewById(R.id.lvExpNotificacao);
		mLoading = (RelativeLayout) findViewById(R.id.loadNotificacao);
		
		mListNotificacoes = new ObjectListID<Notificacao>();
	
		for (PreloadedObject<Notificacao> id : UsuarioController.usuarioLogado.getNotificacoes().getList()) {
			mListNotificacoes.add(id);
		}
		
		mListAdapter = new ListNotificacoesAdapter(NotificacoesTelaActivity.this, mListNotificacoes, mLoading, mListView);
		
		mListView.setAdapter(mListAdapter);
	}

}
