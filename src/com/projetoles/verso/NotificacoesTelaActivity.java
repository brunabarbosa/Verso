package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.projetoles.adapter.ListNotificacoesAdapter;
import com.projetoles.controller.NotificacaoController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Notificacao;
import com.projetoles.model.PreloadedObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class NotificacoesTelaActivity extends Activity {

	private NotificacaoController mNotificacaoController;
	private ListNotificacoesAdapter mListAdapter;
	private List<Notificacao> mListNotificacoes;
	private ListView mListView;
	private RelativeLayout mLoading;
	private int mCountCarregados;

	protected void criarNotificacaoTela(String nId) {
		inicializaVars();
		NotificacaoController mNotificacaoController = new NotificacaoController(this);
		mNotificacaoController.get(nId, new OnRequestListener<Notificacao>(
				NotificacoesTelaActivity.this) {

			@Override
			public void onSuccess(Notificacao result) {
				if(!mListNotificacoes.contains(result)){
					mListNotificacoes.add(result);
					Collections.sort(mListNotificacoes);
					mListAdapter.notifyDataSetChanged();
				}
				
			}

			@Override
			public void onError(String errorMessage) {
				System.out.println(errorMessage);
			}
		});

	}

	private void carregarNotificacoes() {
		if (!UsuarioController.usuarioLogado.getNotificacoes().isEmpty()) {
			mLoading.setVisibility(View.VISIBLE);
		}
		for (PreloadedObject<Notificacao> id : UsuarioController.usuarioLogado.getNotificacoes().getList()) {
			mNotificacaoController.get(id.getId(), new OnRequestListener<Notificacao>(this) {

						@Override
						public void onSuccess(Notificacao notificacao) {
							if (!mListNotificacoes.contains(notificacao)) {
								mListNotificacoes.add(notificacao);
								Collections.sort(mListNotificacoes);
								mListAdapter.notifyDataSetChanged();
								mCountCarregados++;
								runOnUiThread(new Runnable() {
									public void run() {
										if (mCountCarregados == UsuarioController.usuarioLogado
												.getNotificacoes().size()) {
											mLoading.setVisibility(View.GONE);
										}
									}
								});
							}
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

		inicializaVars();
		mListView.setAdapter(mListAdapter);

		carregarNotificacoes();
	}

	private void inicializaVars() {
		if (mListNotificacoes == null && mListAdapter == null) {
			// Preparing list view
			mListNotificacoes = new ArrayList<Notificacao>();
			mListAdapter = new ListNotificacoesAdapter(NotificacoesTelaActivity.this, mListNotificacoes);
		}
	}

}
