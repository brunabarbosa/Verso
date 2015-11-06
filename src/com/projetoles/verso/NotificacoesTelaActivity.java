package com.projetoles.verso;

import com.projetoles.adapter.ListNotificacoesAdapter;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.DataComparator;
import com.projetoles.model.Notificacao;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Usuario;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class NotificacoesTelaActivity extends Activity {

	private ListNotificacoesAdapter mListAdapter;
	private ListView mListView;
	private RelativeLayout mLoading;
	private ObjectListID<Notificacao> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificacoes);

		// Get widgets from layout
		mListView = (ListView) findViewById(R.id.lvExpNotificacao);
		mLoading = (RelativeLayout) findViewById(R.id.loadNotificacao);
		mList = new ObjectListID<Notificacao>();
		
		UsuarioController controller = new UsuarioController(this);
		controller.getUsuarioLogado(new OnRequestListener<Usuario>(this) {

			@Override
			public void onSuccess(final Usuario usuarioLogado) {
				for (PreloadedObject<Notificacao> id :  usuarioLogado.getNotificacoes().getList()) {
					mList.add(id);
				}
				
				mListAdapter = new ListNotificacoesAdapter(NotificacoesTelaActivity.this, mLoading, mListView, 
						mList, new DataComparator<Notificacao>());
				
				mListView.setAdapter(mListAdapter);
			}
			
			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		}, null);
	}

}
