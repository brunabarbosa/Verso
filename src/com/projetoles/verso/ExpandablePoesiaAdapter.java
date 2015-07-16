package com.projetoles.verso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projetoles.controller.PoesiaController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;
 
public class ExpandablePoesiaAdapter extends BaseExpandableListAdapter {
 
    private Activity _context;
    private List<Poesia> _listPoesias;
    private PoesiaController _controller;
    
    public ExpandablePoesiaAdapter(Activity context, List<Poesia> _listPoesias) {
        this._context = context;
        this._listPoesias = _listPoesias;
        this._controller = _controller;
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
        TextView numLikes = (TextView) convertView.findViewById(R.id.num_likes);
        numLikes.setText("" + _listPoesias.get(groupPosition).getCurtidas().size());
        TextView numComments = (TextView) convertView.findViewById(R.id.num_comments);
        numComments.setText("" + _listPoesias.get(groupPosition).getComentarios().size());
        
        ImageView btnComment = (ImageView) convertView.findViewById(R.id.commentIcon);
        btnComment.setTag(this._listPoesias.get(groupPosition));
        btnComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ExpandablePoesiaAdapter.this._context, ComentarioActivity.class);
				i.putExtra("poesia", (Poesia)v.getTag());
				ExpandablePoesiaAdapter.this._context.startActivity(i);
			}
		});
        
        ImageView btnLike = (ImageView) convertView.findViewById(R.id.facebookIcon);
        btnLike.setTag(this._listPoesias.get(groupPosition));
        btnLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				ExpandablePoesiaAdapter.this._controller.curtir((Poesia)v.getTag(),
					new OnRequestListener(ExpandablePoesiaAdapter.this._context) {
						
						@Override
						public void onSuccess(Object result) {
							((ImageView)v).setImageResource(R.drawable.like_icon_ativo);
						}
						
						@Override
						public void onError(String errorMessage) {
							System.out.println(errorMessage);
						}
					});
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
