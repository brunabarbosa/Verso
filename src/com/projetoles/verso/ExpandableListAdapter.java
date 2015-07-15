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

import com.projetoles.model.Poesia;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<Poesia> _listPoesias;
 
    public ExpandableListAdapter(Context context, List<Poesia> _listPoesias) {
        this._context = context;
        this._listPoesias = _listPoesias;
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
        date.setText( DateFormat.getDateInstance(DateFormat.SHORT).format(gc.getTime()));
        
       
 
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
        /*TextView tags = (TextView) convertView.findViewById(R.id.tags);
        String poesiasTagss = "";
        String[] poesiasTags = _listPoesias.get(groupPosition).getTags().split(",");
        for (String tag : poesiasTags) { 
        	poesiasTagss += "#" + tag;
        }
        tags.setText(poesiasTagss);*/
        
        TextView autor = (TextView) convertView.findViewById(R.id.author);
        autor.setText(_listPoesias.get(groupPosition).getAutor());
        
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
