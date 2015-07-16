package com.projetoles.verso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.projetoles.model.Comentario;
 
public class ExpandableComentarioAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<Comentario> _listComentarios;
 
    public ExpandableComentarioAdapter(Context context, List<Comentario> _listComentarios) {
        this._context = context;
        this._listComentarios = _listComentarios;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listComentarios.get(groupPosition).getComentario();
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
 
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(this._listComentarios.get(groupPosition).getComentario());
        
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
 
    @Override
    public Object getGroup(int groupPosition) {
    	return this._listComentarios.get(groupPosition).getPostador();
    }
 
    @Override
    public int getGroupCount() {
        return this._listComentarios.size();
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
            convertView = infalInflater.inflate(R.layout.list_group_comentario, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeaderComentario);
        lblListHeader.setText(this._listComentarios.get(groupPosition).getPostador());

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        TextView lblListData = (TextView) convertView.findViewById(R.id.lblListHeaderData);
        lblListData.setText("Postado em " + DateFormat.getDateInstance(DateFormat.SHORT).format(this._listComentarios.get(groupPosition).getDataCriacao().getTime()));
        
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
