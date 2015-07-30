package com.projetoles.verso;

import java.util.List;

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

import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Comentario;
import com.projetoles.model.ImageUtils;
 
public class ListComentarioAdapter extends BaseAdapter  {
 
    private Context mContext;
    private List<Comentario> mListComentarios;
 
    public ListComentarioAdapter(Context context, List<Comentario> listComentarios) {
    	this.mContext = context;
        this.mListComentarios = listComentarios;
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
    
    public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_comentario, parent, false);
		}
		final Comentario c = this.mListComentarios.get(position);
		if (c != null) {
			ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
			TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
			TextView comentario = (TextView) convertView.findViewById(R.id.comment);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			nome.setText(c.getPostador().getNome() + " diz:");
			comentario.setText(c.getComentario());
			data.setText("Postado em " + CalendarUtils.getDataFormada(c.getDataCriacao()));
			setPhoto(foto, c.getPostador().getFoto());
			nome.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext, UserProfileActivity.class);
					intent.putExtra("usuario", c.getPostador());
					mContext.startActivity(intent);
				}
			});
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return this.mListComentarios.size();
	}

	@Override
	public Object getItem(int arg0) {
		return this.mListComentarios.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
