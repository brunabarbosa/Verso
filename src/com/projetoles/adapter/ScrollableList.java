package com.projetoles.adapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.projetoles.controller.Controller;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.ImageUtils;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.TemporalModel;
import com.projetoles.model.Usuario;
import com.projetoles.verso.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ScrollableList<T extends TemporalModel> extends BaseAdapter {

	private static final int NUMBERS_TO_LOAD = 10;
	private static final int VISIBLE_THRESHOLD = 3;

	private int mPreviousTotalItemCount = 0;
	private boolean mLoadingItems = false;
	private int mAlreadyLoaded;
	private int mExpectedLoaded;

	protected Activity mContext;
	protected View mLoading;
	protected ListView mListView;
	protected ObjectListID<T> mList;
	protected Controller<T> mController;
	protected Usuario mUsuario;
	protected Comparator<PreloadedObject<T> > mComparator;
	protected List<T> mUpdatedList;
	
	public ScrollableList(final Activity context, final View loading, final ListView listView, final ObjectListID<T> list,
			final Controller<T> controller, final Comparator<PreloadedObject<T> > comparator) {
		this.mContext = context;
		this.mLoading = loading;
		this.mListView = listView;
		this.mController = controller;
		this.mUpdatedList = new ArrayList<T>();
		this.mComparator = comparator;
		this.mList = list;
		this.mList.sort(this.mComparator);
		UsuarioController usuarioController = new UsuarioController(context);
		usuarioController.getUsuarioLogado(new OnRequestListener<Usuario>(context) {

			@Override
			public void onSuccess(Usuario usuarioLogado) {
				ScrollableList.this.mUsuario = usuarioLogado;
				mListView.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						
					}
					
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
						if (totalItemCount < mPreviousTotalItemCount) {
							mPreviousTotalItemCount = totalItemCount;
							if (totalItemCount == 0) mLoadingItems = true;
						}
						if (!mLoadingItems) {
							mLoading.setVisibility(View.GONE);
						}
						if (!mLoadingItems && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
							loadNextPage(totalItemCount + NUMBERS_TO_LOAD);
						}
					}
				});
				if (!mList.isEmpty())
					loadNextPage(NUMBERS_TO_LOAD);
			}

			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
		}, null);
	}

	private void loadNextPage(int itemsToLoad) {
		if (mPreviousTotalItemCount >= mList.size()) {
			mLoadingItems = false;
		} else {
			mAlreadyLoaded = 0;
			mExpectedLoaded = 0;
			mLoadingItems = true;
			for (int i = 0; i < Math.min(mList.size(), itemsToLoad); i++) {
				if (!mList.get(i).isLoaded()) {
					mExpectedLoaded++;
					final PreloadedObject<T> id = mList.get(i);
					mList.get(i).load(mController, new OnRequestListener<T>(mContext) {

						@Override
						public void onSuccess(T result) {
							mAlreadyLoaded++;
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingItems = false;
							}
							mList.sort(mComparator);
							notifyDataSetChanged();
						}
 
						@Override
						public void onError(String errorMessage) {
							mAlreadyLoaded++;
							System.out.println("ON ERROR " + id.getId() + " " + errorMessage + " " + mAlreadyLoaded + " " + mExpectedLoaded);
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingItems = false;
							}
						}

						@Override
						public void onTimeout() {
							System.out.println("TIMEOUT " + id.getId() + " " + mAlreadyLoaded + " " + mExpectedLoaded);
							mAlreadyLoaded++;
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingItems = false;
							}
						}
					});
				}
				if (mExpectedLoaded > 0) {
					mLoading.setVisibility(View.VISIBLE);
					mLoadingItems = true;
				} else {
					mLoading.setVisibility(View.GONE);
					mLoadingItems = false;
				}
			}
		}
	}

	@Override
	public int getCount() {
		int count = 0;
		for (PreloadedObject<T> id : mList.getList()) {
			if (id.isLoaded()) ++count;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return this.mList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

    protected void setPhoto(ImageView imview, byte[] photo) {
		if (photo != null && photo.length > 0) {
			Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
			bmp = ImageUtils.getCroppedBitmap(bmp);
			imview.setImageBitmap(bmp);
		} else {
			imview.setImageResource(R.drawable.icone_foto);
		}
	}
    
    public View getView(int position, View convertView, ViewGroup parent, final OnRequestListener<T> callback) {
    	if (convertView == null) {
  			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  			convertView = inflater.inflate(R.layout.list_group_comentario, parent, false);
  		}
    	final ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
    	final TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
    	final TextView comentario = (TextView) convertView.findViewById(R.id.comment);
    	final TextView data = (TextView) convertView.findViewById(R.id.date);
		final ImageView excluir = (ImageView) convertView.findViewById(R.id.excluir);
		setPhoto(foto, null);
		nome.setText("");
		comentario.setText("");
		data.setText("");
		excluir.setVisibility(View.GONE);
  		//convertView.setVisibility(View.GONE);
  		T loadedObj = mList.get(position).getPureLoadedObj();
  		if (loadedObj != null) {
  			if (mUpdatedList.contains(loadedObj)) {
  				callback.onSuccess(loadedObj);
  			} else {
  				callback.onSuccess(loadedObj);
  				mList.get(position).getLoadedObj(mController, new OnRequestListener<T>(callback.getContext()) {

					@Override
					public void onSuccess(T result) {
						//fConvertView.setVisibility(View.VISIBLE);
						mUpdatedList.add(result);
						callback.onSuccess(result); 
					}

					@Override
					public void onError(String errorMessage) {
						callback.onError(errorMessage);
					}

					@Override
					public void onTimeout() {
						callback.onTimeout();
					}
				});
  			}
  		}
  		return convertView;
  	}
  			
}
