package com.projetoles.verso;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projetoles.controller.CurtidaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Curtida;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;
 
public class ExpandablePoesiaAdapter extends BaseExpandableListAdapter {
 
    private Activity mContext;
    private List<Poesia> mListPoesias;
    private CurtidaController mCurtidaController;
    private Bundle mBundle;
    private Usuario mUsuario;
    
    public ExpandablePoesiaAdapter(Activity context, List<Poesia> listPoesias, 
    		Bundle bundle) {
        this.mContext = context;
        this.mListPoesias = listPoesias;
        this.mCurtidaController = new CurtidaController(context);
        this.mBundle = bundle;	
        this.mUsuario = UsuarioController.usuarioLogado;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mListPoesias.get(groupPosition).getPoesia();
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        
        final Poesia poesia = mListPoesias.get(groupPosition);
        
        TextView tags = (TextView) convertView.findViewById(R.id.tags);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        if (poesia.getTags().trim().isEmpty()) {
        	tags.setVisibility(View.GONE);
        } else {
			String poesiasTagss = "";
			String[] poesiasTags = poesia.getTags().split(",");
			for (String tag : poesiasTags) { 
				poesiasTagss += "#" + tag;
			}
			tags.setText(poesiasTagss);
        }
        date.setText("Postado em " + CalendarUtils.getDataFormada(poesia.getDataCriacao()) + " por " 
        		+ poesia.getPostador().getNome());
        date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, UserProfileActivity.class);
				intent.putExtra("usuario", poesia.getPostador());
				mContext.startActivity(intent);
			}
		});
        txtListChild.setText(childText);
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
 
    @Override
    public Object getGroup(int groupPosition) {
    	return this.mListPoesias.get(groupPosition).getTitulo();
    }
 
    @Override
    public int getGroupCount() {
        return this.mListPoesias.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        final Poesia poesia = mListPoesias.get(groupPosition);
        
        final TextView autor = (TextView) convertView.findViewById(R.id.author);
        autor.setText(poesia.getAutor());
        final TextView numLikes = (TextView) convertView.findViewById(R.id.num_likes);
        numLikes.setText(String.valueOf(poesia.getCurtidas().size()));
        final TextView numComments = (TextView) convertView.findViewById(R.id.num_comments);
        numComments.setText(String.valueOf(poesia.getComentarios().size()));
        
        // Botão tela de curtidas
        numLikes.setTag(poesia);
        numLikes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, CurtidaActivity.class);
				intent.putExtra("poesia", (Poesia)v.getTag());
				mContext.startActivity(intent);
			}
		});
        
        // Botao tela de comentarios
        ImageView btnComment = (ImageView) convertView.findViewById(R.id.commentIcon);
        btnComment.setTag(poesia);
        btnComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ComentarioActivity.class);
				intent.putExtra("poesia", (Poesia)v.getTag());
				intent.putExtra("callback", mContext.getClass());
				intent.putExtra("bundle", mBundle);
				mContext.startActivity(intent);
				mContext.finish();
			}
		});
        
        // Botao curtir
        final ImageView btnLike = (ImageView) convertView.findViewById(R.id.facebookIcon);
        if (mUsuario.getCurtidas().getIntersecction(poesia.getCurtidas()) != null) {
        	btnLike.setImageResource(R.drawable.like_icon_ativo);
        } else {
        	btnLike.setImageResource(R.drawable.like_icon);
        }
        btnLike.setTag(poesia);
        btnLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				if (v.isClickable()) {
					final Poesia clicada = (Poesia) v.getTag();
					v.setClickable(false);
					String curtidaId = mUsuario.getCurtidas().getIntersecction(clicada.getCurtidas());
					if (curtidaId != null) {
						mCurtidaController.delete(curtidaId, new OnRequestListener<String>(mContext) {
							 
							@Override
							public void onSuccess(String id) {
								mUsuario.getCurtidas().remove(id);
								clicada.getCurtidas().remove(id);
								((ImageView)v).setImageResource(R.drawable.like_icon);
								numLikes.setText(String.valueOf(clicada.getCurtidas().size()));
								v.setClickable(true);
							}
							 
							@Override
							public void onError(String errorMessage) {
								System.out.println("ERROR descurtir: " + errorMessage);
								v.setClickable(true);
							}
						});
					} else {
						mCurtidaController.post(mUsuario, poesia, new OnRequestListener<Curtida>(mContext) {
							
							@Override
							public void onSuccess(Curtida curtida) {
								mUsuario.getCurtidas().add(curtida.getId());
								clicada.getCurtidas().add(curtida.getId());
								((ImageView)v).setImageResource(R.drawable.like_icon_ativo);
								numLikes.setText(String.valueOf(clicada.getCurtidas().size()));
								v.setClickable(true);
							}
							
							@Override
							public void onError(String errorMessage) {
								System.out.println("ERROR curtir: " + errorMessage);
								v.setClickable(true);
							}
						});
					}
				}
			}
		});
        
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
}
