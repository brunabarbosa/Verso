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
import com.projetoles.model.ImageUtils;
import com.projetoles.model.Seguida;
 
public class ListSeguidoresAdapter extends BaseAdapter  {
 
    private Context mContext;
    private List<Seguida> mListSeguidas;
 
    public ListSeguidoresAdapter(Context context, List<Seguida> listCurtidas) {
    	this.mContext = context;
        this.mListSeguidas = listCurtidas;
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
		final Seguida s = this.mListSeguidas.get(position);
		if (s != null) {
			ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
			TextView nome = (TextView) convertView.findViewById(R.id.mensagem);
			TextView comentario = (TextView) convertView.findViewById(R.id.comment);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			ImageView excluir = (ImageView) convertView.findViewById(R.id.excluir);
			excluir.setVisibility(View.GONE);
			if(SeguidoresActivity.mSeguindo){
				nome.setText( s.getSeguido().getNome());
			}else{
				nome.setText(s.getSeguidor().getNome());
			}	
			
			comentario.setVisibility(View.GONE);
			data.setText("Seguiu em " + CalendarUtils.getDataFormada(s.getDataCriacao()));
			setPhoto(foto, s.getSeguidor().getFoto());
			OnClickListener clicaUsuario = new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext, UserProfileActivity.class);
					if(SeguidoresActivity.mSeguindo){
						intent.putExtra("usuario", s.getSeguido());
					}else{
						intent.putExtra("usuario", s.getSeguidor());
					}
					
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
		return this.mListSeguidas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return this.mListSeguidas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}