package com.projetoles.adapter;

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
import com.projetoles.model.Curtida;
import com.projetoles.model.ImageUtils;
import com.projetoles.verso.R;
import com.projetoles.verso.UserProfileActivity;
import com.projetoles.verso.R.drawable;
import com.projetoles.verso.R.id;
import com.projetoles.verso.R.layout;
 
public class ListCurtidaAdapter extends BaseAdapter  {
 
    private Context mContext;
    private List<Curtida> mListCurtidas;
 
    public ListCurtidaAdapter(Context context, List<Curtida> listCurtidas) {
    	this.mContext = context;
        this.mListCurtidas = listCurtidas;
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
		final Curtida c = this.mListCurtidas.get(position);
		if (c != null) {
			ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
			TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
			TextView comentario = (TextView) convertView.findViewById(R.id.comment);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			ImageView excluir = (ImageView) convertView.findViewById(R.id.excluir);
			excluir.setVisibility(View.GONE);
			nome.setText(c.getPostador().getNome() + " curtiu esta poesia.");
			comentario.setVisibility(View.GONE);
			data.setText("Curtido em " + CalendarUtils.getDataFormada(c.getDataCriacao()));
			setPhoto(foto, c.getPostador().getFoto());
			OnClickListener clicaUsuario = new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext, UserProfileActivity.class);
					intent.putExtra("usuario", c.getPostador());
					mContext.startActivity(intent);
				}
			};
			nome.setOnClickListener(clicaUsuario);
			foto.setOnClickListener(clicaUsuario);
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return this.mListCurtidas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return this.mListCurtidas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
