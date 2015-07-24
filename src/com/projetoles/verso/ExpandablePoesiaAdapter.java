package com.projetoles.verso;

import java.text.DateFormat;
import java.util.Calendar;
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
import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Curtida;
import com.projetoles.model.Poesia;
 
public class ExpandablePoesiaAdapter extends BaseExpandableListAdapter {
 
    private Activity mContext;
    private List<Poesia> mListPoesias;
    private PoesiaController mPoesiaController;
    private CurtidaController mCurtidaController;
    private Bundle mBundle;
    
    public ExpandablePoesiaAdapter(Activity context, List<Poesia> listPoesias, 
    		Bundle bundle) {
        this.mContext = context;
        this.mListPoesias = listPoesias;
        this.mPoesiaController = new PoesiaController(context);
        this.mCurtidaController = new CurtidaController(context);
        this.mBundle = bundle;
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
        
        Calendar gc = mListPoesias.get(groupPosition).getDataDeCriacao();
        TextView tags = (TextView) convertView.findViewById(R.id.tags);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        String poesiasTagss = "";
        String[] poesiasTags = mListPoesias.get(groupPosition).getTags().split(",");
        for (String tag : poesiasTags) { 
        	poesiasTagss += "#" + tag;
        }
        tags.setText(poesiasTagss);
        date.setText("Postado em " + DateFormat.getDateInstance(DateFormat.SHORT).format(gc.getTime()));
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

        TextView autor = (TextView) convertView.findViewById(R.id.author);
        autor.setText(mListPoesias.get(groupPosition).getAutor());
        final TextView numLikes = (TextView) convertView.findViewById(R.id.num_likes);
        final int numCurtidas = mListPoesias.get(groupPosition).getCurtidas().size();
        numLikes.setText("" + numCurtidas);
        TextView numComments = (TextView) convertView.findViewById(R.id.num_comments);
        numComments.setText("" + (mListPoesias.get(groupPosition).getComentarios().size()));
        
        numLikes.setTag(this.mListPoesias.get(groupPosition));
        numLikes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ExpandablePoesiaAdapter.this.mContext, CurtidaActivity.class);
				i.putExtra("poesia", (Poesia)v.getTag());
				ExpandablePoesiaAdapter.this.mContext.startActivity(i);
			}
		});
        ImageView btnComment = (ImageView) convertView.findViewById(R.id.commentIcon);
        btnComment.setTag(this.mListPoesias.get(groupPosition));
        btnComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ExpandablePoesiaAdapter.this.mContext, ComentarioActivity.class);
				i.putExtra("poesia", (Poesia)v.getTag());
				i.putExtra("callback", ExpandablePoesiaAdapter.this.mContext.getClass());
				i.putExtra("bundle", ExpandablePoesiaAdapter.this.mBundle);
				ExpandablePoesiaAdapter.this.mContext.startActivity(i);
				ExpandablePoesiaAdapter.this.mContext.finish();
			}
		});
        
        final ImageView btnLike = (ImageView) convertView.findViewById(R.id.facebookIcon);
        if (UsuarioController.usuarioLogado.getCurtidas().contains(this.mListPoesias.get(groupPosition).getId())) {
        	btnLike.setImageResource(R.drawable.like_icon_ativo);
        } else {
        	btnLike.setImageResource(R.drawable.like_icon);
        }
        btnLike.setTag(this.mListPoesias.get(groupPosition));
        btnLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				btnLike.setClickable(false);
				final Poesia poesia = (Poesia) v.getTag();
				if (UsuarioController.usuarioLogado.getCurtidas().contains(poesia.getId())) {
					ExpandablePoesiaAdapter.this.mCurtidaController.descurtir(poesia, new OnRequestListener(ExpandablePoesiaAdapter.this.mContext) {
							
							@Override
							public void onSuccess(Object result) {
								String id = (String) result;
								UsuarioController.usuarioLogado.removeCurtida(poesia.getId());
								poesia.removeCurtida(id);
								((ImageView)v).setImageResource(R.drawable.like_icon);
								numLikes.setText(String.valueOf(Integer.valueOf(poesia.getCurtidas().size())));
								btnLike.setClickable(true);
							}
							 
							@Override
							public void onError(String errorMessage) {
								System.out.println("ERROR descurtir: " + errorMessage);
								btnLike.setClickable(true);
							}
						});
				} else {
					ExpandablePoesiaAdapter.this.mCurtidaController.curtir(poesia,
						new OnRequestListener(ExpandablePoesiaAdapter.this.mContext) {
							
							@Override
							public void onSuccess(Object result) {
								Curtida curtida = (Curtida) result;
								UsuarioController.usuarioLogado.addCurtida(poesia.getId());
								poesia.addCurtida(curtida.getId());
								((ImageView)v).setImageResource(R.drawable.like_icon_ativo);
								numLikes.setText(String.valueOf(Integer.valueOf(poesia.getCurtidas().size())));
								btnLike.setClickable(true);
							}
							
							@Override
							public void onError(String errorMessage) {
								System.out.println("ERROR curtir: " + errorMessage);
								btnLike.setClickable(true);
							}
						});
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
