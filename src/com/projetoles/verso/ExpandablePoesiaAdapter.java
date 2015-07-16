package com.projetoles.verso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;
 
public class ExpandablePoesiaAdapter extends BaseExpandableListAdapter {
 
    private Activity _context;
    private List<Poesia> _listPoesias;
    private PoesiaController _controller;
    private Map<View, Boolean> _loading;
    private Bundle _bundle;
    
    public ExpandablePoesiaAdapter(Activity context, List<Poesia> _listPoesias, Bundle _bundle) {
        this._context = context;
        this._listPoesias = _listPoesias;
        this._controller = new PoesiaController(context);
        this._loading = new HashMap<View, Boolean>();
        this._bundle = _bundle;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listPoesias.get(groupPosition).getPoesia();
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        
        Calendar gc = new GregorianCalendar();
        gc = _listPoesias.get(groupPosition).getDataDeCriacao();
        TextView tags = (TextView) convertView.findViewById(R.id.tags);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        String poesiasTagss = "";
        String[] poesiasTags = _listPoesias.get(groupPosition).getTags().split(",");
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
    	return this._listPoesias.get(groupPosition).getTitulo();
    }
 
    @Override
    public int getGroupCount() {
        return this._listPoesias.size();
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        TextView autor = (TextView) convertView.findViewById(R.id.author);
        autor.setText(_listPoesias.get(groupPosition).getAutor());
        final TextView numLikes = (TextView) convertView.findViewById(R.id.num_likes);
        final int numCurtidas = _listPoesias.get(groupPosition).getCurtidas().size();
        numLikes.setText("" + numCurtidas);
        TextView numComments = (TextView) convertView.findViewById(R.id.num_comments);
        numComments.setText("" + (_listPoesias.get(groupPosition).getComentarios().size()));
        
        numLikes.setTag(this._listPoesias.get(groupPosition));
        numLikes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ExpandablePoesiaAdapter.this._context, CurtidaActivity.class);
				i.putExtra("poesia", (Poesia)v.getTag());
				ExpandablePoesiaAdapter.this._context.startActivity(i);
			}
		});
        ImageView btnComment = (ImageView) convertView.findViewById(R.id.commentIcon);
        btnComment.setTag(this._listPoesias.get(groupPosition));
        btnComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ExpandablePoesiaAdapter.this._context, ComentarioActivity.class);
				i.putExtra("poesia", (Poesia)v.getTag());
				i.putExtra("callback", ExpandablePoesiaAdapter.this._context.getClass());
				i.putExtra("bundle", ExpandablePoesiaAdapter.this._bundle);
				ExpandablePoesiaAdapter.this._context.startActivity(i);
				ExpandablePoesiaAdapter.this._context.finish();
			}
		});
        
        ImageView btnLike = (ImageView) convertView.findViewById(R.id.facebookIcon);
        if (this._listPoesias.get(groupPosition) != null && this._listPoesias.get(groupPosition).getId() != null && !this._listPoesias.get(groupPosition).getId().isEmpty() && UsuarioController.usuarioLogado.getCurtidas().contains(this._listPoesias.get(groupPosition).getId())) {
        	btnLike.setImageResource(R.drawable.like_icon_ativo);
        } else {
        	btnLike.setImageResource(R.drawable.like_icon);
        }
        btnLike.setTag(this._listPoesias.get(groupPosition));
        this._loading.put(btnLike, false);
        btnLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				if (!ExpandablePoesiaAdapter.this._loading.get(v)) {
					ExpandablePoesiaAdapter.this._loading.put(v, true);
					final Poesia poesia = (Poesia) v.getTag();
					if (poesia != null && poesia.getId() != null && !poesia.getId().isEmpty() && UsuarioController.usuarioLogado.getCurtidas().contains(poesia.getId())) {
						ExpandablePoesiaAdapter.this._controller.descurtir(poesia, new OnRequestListener(ExpandablePoesiaAdapter.this._context) {
								
								@Override
								public void onSuccess(Object result) {
									UsuarioController.usuarioLogado.removeCurtida(poesia.getId());
									((ImageView)v).setImageResource(R.drawable.like_icon);
									//numLikes.setText(String.valueOf(Integer.valueOf(numLikes.getText().toString())-1));
									ExpandablePoesiaAdapter.this._loading.put(v, false);
								}
								 
								@Override
								public void onError(String errorMessage) {
									System.out.println("ERROR descurtir: " + errorMessage);
								}
							});
					} else {
						ExpandablePoesiaAdapter.this._controller.curtir(poesia,
							new OnRequestListener(ExpandablePoesiaAdapter.this._context) {
								
								@Override
								public void onSuccess(Object result) {
									UsuarioController.usuarioLogado.addCurtida(poesia.getId());
									((ImageView)v).setImageResource(R.drawable.like_icon_ativo);
									//numLikes.setText(String.valueOf(Integer.valueOf(numLikes.getText().toString())+1));
									ExpandablePoesiaAdapter.this._loading.put(v, false);
								}
								
								@Override
								public void onError(String errorMessage) {
									System.out.println("ERROR curtir: " + errorMessage);
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
