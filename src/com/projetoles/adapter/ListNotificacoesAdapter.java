package com.projetoles.adapter;

import com.projetoles.controller.NotificacaoController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.ImageUtils;
import com.projetoles.model.Notificacao;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;
import com.projetoles.verso.ComentarioActivity;
import com.projetoles.verso.MainActivity;
import com.projetoles.verso.R;
import com.projetoles.verso.UserProfileActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListNotificacoesAdapter extends BaseAdapter {

	private static final int NUMBERS_TO_LOAD = 10;
	private static final int VISIBLE_THRESHOLD = 3;
	
	private Activity mContext;
	private ObjectListID<Notificacao> mListNotificacoes;
	private View mLoading;
	private NotificacaoController mNotificacaoController;
	private ListView mListView;
	
	private int mPreviousTotalItemCount = 0;
	private boolean mLoadingPoesias = false;
	private int mAlreadyLoaded;
	private int mExpectedLoaded;
	
	public ListNotificacoesAdapter(Activity context, ObjectListID<Notificacao> listNotificacoes, View loading,
			ListView listView) {
    	this.mContext = context;
        this.mListNotificacoes = listNotificacoes;
        this.mLoading = loading;
        this.mNotificacaoController = new NotificacaoController(context);
        this.mListView = listView;
        this.mListNotificacoes.sort();
        mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (totalItemCount < mPreviousTotalItemCount) {
					mPreviousTotalItemCount = totalItemCount;
					if (totalItemCount == 0) mLoadingPoesias = true;
				}
				if (!mLoadingPoesias) {
					mLoading.setVisibility(View.GONE);
				}
				if (!mLoadingPoesias && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
					loadNextPage(totalItemCount + NUMBERS_TO_LOAD);
				}
			}
		});
        loadNextPage(NUMBERS_TO_LOAD);
	}

	private void loadNextPage(int itemsToLoad) {
		if (mPreviousTotalItemCount >= mListNotificacoes.size()) {
			mLoadingPoesias = false;
		} else {
			mAlreadyLoaded = 0;
			mExpectedLoaded = 0;
			mLoadingPoesias = true;
			for (int i = 0; i < Math.min(mListNotificacoes.size(), itemsToLoad); i++) {
				if (!mListNotificacoes.get(i).isLoaded()) {
					mExpectedLoaded++;
					mListNotificacoes.get(i).load(mNotificacaoController, new OnRequestListener<Notificacao>(mContext) {

						@Override
						public void onSuccess(Notificacao result) {
							mAlreadyLoaded++;
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingPoesias = false;
							}
							notifyDataSetChanged();
						}

						@Override
						public void onError(String errorMessage) {
							mAlreadyLoaded++;
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingPoesias = false;
							}
						}
					});
				}
				if (mExpectedLoaded > 0) {
					mLoading.setVisibility(View.VISIBLE);
					mLoadingPoesias = true;
				} else {
					mLoading.setVisibility(View.GONE);
					mLoadingPoesias = false;
				}
			}
		}
	}

	@Override
	public int getCount() {
		int count = 0;
		for (PreloadedObject<Notificacao> id : mListNotificacoes.getList()) {
			if (id.isLoaded()) ++count;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return this.mListNotificacoes.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	private void setPhoto(ImageView imview, byte[] photo) {
		if (photo.length > 0) {
			Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
			bmp = ImageUtils.getCroppedBitmap(bmp);
			imview.setImageBitmap(bmp);
		} else {
			imview.setImageResource(R.drawable.icone_foto);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_comentario, parent, false);
		}
		final Notificacao n = this.mListNotificacoes.get(position).getLoadedObj();
		if (n != null) {
			ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
			TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
			TextView comentario = (TextView) convertView.findViewById(R.id.comment);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			ImageView excluir = (ImageView) convertView.findViewById(R.id.excluir);
			excluir.setVisibility(View.VISIBLE);
			excluir.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					v.setVisibility(View.GONE);
					NotificacaoController controller = new NotificacaoController(mContext);
					controller.delete(n.getId(), new OnRequestListener<String>(mContext) {
						
						@Override
						public void onSuccess(String result) {
							mListNotificacoes.remove(n.getId());
							notifyDataSetChanged();
						} 
						
						@Override
						public void onError(String errorMessage) {
							System.out.println(errorMessage);
						}
					});
				}
			});
			nome.setText(n.getTitulo().getNome() + n.getMensagem());
			comentario.setVisibility(View.GONE);
			data.setText("Em " + CalendarUtils.getDataFormada(n.getDataCriacao()));
			setPhoto(foto, n.getTitulo().getFoto());
			OnClickListener clicaUsuario = new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(n.getTipo().equals("usuario")){
						Intent intent = new Intent(mContext, UserProfileActivity.class);
						intent.putExtra("usuario", n.getTitulo());
						mContext.startActivity(intent);
					} else {
						Intent intent = new Intent(mContext, ComentarioActivity.class);
						intent.putExtra("poesia", (Poesia) n.getPoesia());
						intent.putExtra("usuario", UsuarioController.usuarioLogado);
						intent.putExtra("callback", MainActivity.class);
						mContext.startActivity(intent);
						mContext.finish();
					}	
				}
			};
			nome.setOnClickListener(clicaUsuario);
			foto.setOnClickListener(clicaUsuario);
		}
		return convertView;
	}

}
