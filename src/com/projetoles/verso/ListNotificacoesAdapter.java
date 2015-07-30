package com.projetoles.verso;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projetoles.controller.NotificacaoController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.ImageUtils;
import com.projetoles.model.Notificacao;

public class ListNotificacoesAdapter extends BaseAdapter {

	private Activity mContext;
	private List<Notificacao> mListNotificacoes;

	public ListNotificacoesAdapter(Activity context, List<Notificacao> listNotificacoes) {
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
		final Notificacao n = this.mListNotificacoes.get(position);
		if (n != null) {
			ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
			TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
			TextView comentario = (TextView) convertView.findViewById(R.id.comment);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			ImageView excluir = (ImageView) convertView.findViewById(R.id.excluir);
			excluir.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					v.setVisibility(View.GONE);
					NotificacaoController controller = new NotificacaoController(mContext);
					controller.delete(n.getId(), new OnRequestListener<String>(mContext) {
						
						@Override
						public void onSuccess(String result) {
							mListNotificacoes.remove(n);
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
			nome.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext, UserProfileActivity.class);
					intent.putExtra("usuario", n.getTitulo());
					mContext.startActivity(intent);
				}
			});
		}
		return convertView;
	}

}
