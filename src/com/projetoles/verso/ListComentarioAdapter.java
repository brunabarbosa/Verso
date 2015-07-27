package com.projetoles.verso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projetoles.model.Comentario;
 
public class ListComentarioAdapter extends BaseAdapter  {
 
    private Context mContext;
    private List<Comentario> mListComentarios;
 
    public ListComentarioAdapter(Context context, List<Comentario> listComentarios) {
    	this.mContext = context;
        this.mListComentarios = listComentarios;
    }

	public Bitmap getCroppedBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	            bitmap.getWidth() / 2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
	    //return _bmp;
	    return output;
	}
	
    private void setPhoto(ImageView imview, byte[] photo) {
		if (photo.length > 0) {
			Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
			bmp = getCroppedBitmap(bmp);
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
		Comentario c = this.mListComentarios.get(position);
		if (c != null) {
			ImageView foto = (ImageView) convertView.findViewById(R.id.userPicture);
			TextView nome = (TextView) convertView.findViewById(R.id.userName);
			TextView comentario = (TextView) convertView.findViewById(R.id.comment);
			TextView data = (TextView) convertView.findViewById(R.id.date);
			nome.setText(c.getPostador().getNome() + " diz:");
			comentario.setText(c.getComentario());
			data.setText("Postado em " + DateFormat.getDateInstance(DateFormat.SHORT).format(c.getDataCriacao().getTime()));
			setPhoto(foto, c.getPostador().getFoto());
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
