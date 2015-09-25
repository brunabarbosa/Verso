package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.projetoles.adapter.ListNotificacoesAdapter;
import com.projetoles.controller.NotificacaoController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Notificacao;
import com.projetoles.verso.R;

public class NotificacoesTelaActivity extends Activity {
	
	private NotificacaoController mNotificacaoController;
	private ListNotificacoesAdapter mListAdapter;
	private ListView mListView;
	private static List<Notificacao> mListNotificacoes;
	private RelativeLayout mLoading;
	private int mCountCarregados;
	
	protected void criarNotificacaoTela(String nId){
		mNotificacaoController.get(nId, new OnRequestListener<Notificacao>(NotificacoesTelaActivity.this) {

			@Override
			public void onSuccess(Notificacao result) {
				mListNotificacoes.add(result);
				Collections.sort(mListNotificacoes);
				mListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(String errorMessage) {
				System.out.println(errorMessage);
			}
		});
		
		
	}
	
	/*public static void criarNotificacoes(final Usuario enderecado, Usuario postador, String mensagem, Poesia poesia, String tipo, Activity a){
		mNotificacaoController.post(new Notificacao(null, Calendar.getInstance(), enderecado, postador, mensagem, poesia, tipo), 
				new OnRequestListener<Notificacao>(a) {

					@Override
					public void onSuccess(Notificacao result) {
						enderecado.getNotificacoes().add(result.getId());
					}

					@Override
					public void onError(String errorMessage) {
						System.out.println(errorMessage);
					}
				});
	}*/
	
	private void carregarNotificacoes() {
		if (!UsuarioController.usuarioLogado.getNotificacoes().isEmpty()) {
			mLoading.setVisibility(View.VISIBLE);
		}
		for (String id : UsuarioController.usuarioLogado.getNotificacoes().getList()) {
			mNotificacaoController.get(id, new OnRequestListener<Notificacao>(this) {

				@Override
				public void onSuccess(Notificacao notificacao) {
					mListNotificacoes.add(notificacao);
					Collections.sort(mListNotificacoes);
					mListAdapter.notifyDataSetChanged();
					mCountCarregados++;
					runOnUiThread(new Runnable() {
						public void run() {
							if (mCountCarregados == UsuarioController.usuarioLogado.getNotificacoes().size()) {
								mLoading.setVisibility(View.GONE);
							}
						}
					});
				}

				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
					mCountCarregados++;
				}
			});
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificacoes);

		mNotificacaoController = new NotificacaoController(this);

		// Get widgets from layout
		mListView = (ListView) findViewById(R.id.lvExpNotificacao);
		mLoading = (RelativeLayout) findViewById(R.id.loadNotificacao);
		
		// Preparing list view
		mListNotificacoes = new ArrayList<Notificacao>();
		mListAdapter = new ListNotificacoesAdapter(NotificacoesTelaActivity.this, mListNotificacoes);
		mListView.setAdapter(mListAdapter);

		carregarNotificacoes();
	}
	
}
