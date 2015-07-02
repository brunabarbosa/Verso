package com.projetoles.verso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.projetoles.controller.PoemaController;
import com.projetoles.controller.UsuarioController;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileActivity extends Activity {

	UsuarioController usuarioController;
	PoemaController poemaController;
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//getActionBar().hide();
		
		setContentView(R.layout.activity_user_profile);
		
		//change fonts
		//setFonts();
		
		usuarioController = new UsuarioController(this);
		poemaController = new PoemaController(this);

		//set userName
		TextView usuarioName = (TextView) findViewById(R.id.userName);
		usuarioName.setText(UsuarioController.usuarioLogado.getNome());
		
		//set userPicture
		ImageView userPicture = (ImageView) findViewById(R.id.userPicture);
		
		// get the listview 
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		
		//preparing list data
		prepareListData();
		
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		
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
	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Poesia 01");
        listDataHeader.add("Poesia 02");
 
        // Adding child data
        List<String> poesia1 = new ArrayList<String>();
        poesia1.add("Quantas vezes a gente, em busca da ventura, Procede tal e qual o avozinho infeliz: Em vão, por toda parte, os óculos procura Tendo-os na ponta do nariz!");

        List<String> poesia2 = new ArrayList<String>();
        poesia2.add("Minha energia é o desafio, minha motivação é o impossível, e é por isso que eu preciso ser, à força e a esmo, inabalável.");
       
        listDataChild.put(listDataHeader.get(0), poesia1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), poesia2);
	}

	private final void setFonts() {
        // text view label
        //TextView txtBiografia = (TextView) findViewById(R.id.biografia);
        //TextView txtPoesias = (TextView) findViewById(R.id.txtNumPoesias);
        
        TextView txtSeguidores = (TextView) findViewById(R.id.txtNumSeguidores);
        TextView txtSeguindo = (TextView) findViewById(R.id.txtNumSeguindo);
        
        //txtPoesias.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));
        txtSeguidores.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));
        txtSeguindo.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));
        //txtBiografia.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));       
	}
	
}
