package com.projetoles.verso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;

public class PerfilActivity extends Activity {

	private UsuarioController usuarioController;
	private PoesiaController poemaController;
	private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private int countCarregados;
    private HashMap<String, List<String>> listDataChild;
    
    private void setData() {
    	
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		//change fonts
		
		usuarioController = new UsuarioController(this);
		poemaController = new PoesiaController(this);

		// get the listview 
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		
		//preparing list data
		listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listAdapter = new ExpandableListAdapter(PerfilActivity.this, 
				listDataHeader, listDataChild);
		
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
	        int previousGroup = -1;

	        @Override
	        public void onGroupExpand(int groupPosition) {
	            if(groupPosition != previousGroup)
	            	expListView.collapseGroup(previousGroup);
	            previousGroup = groupPosition;
	        }
	    });
		
		//setting the list adapter
		expListView.setAdapter(listAdapter);
		
		for (Poesia p : usuarioController.usuarioLogado.getPoemasCarregados()) {
			// Adding child data
	        listDataHeader.add(p.getTitulo());
	 
	        // Adding child data
	        List<String> poesia = new ArrayList<String>();
	        poesia.add(p.getPoesia());

	        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), poesia); // Header, Child data
		}
		listAdapter.notifyDataSetChanged();
		for (String id : usuarioController.usuarioLogado.getPoemas()) {
			poemaController.getPoesia(id, new OnRequestListener(this) {
				
				@Override
				public void onSuccess(Object result) {
					Poesia p = (Poesia) result;
					// Adding child data
			        listDataHeader.add(p.getTitulo());
			 
			        // Adding child data
			        List<String> poesia = new ArrayList<String>();
			        poesia.add(p.getPoesia());

			        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), poesia); // Header, Child data
			        listAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
				}
			});
		}
	}

}
