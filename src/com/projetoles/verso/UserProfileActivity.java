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
		setContentView(R.layout.activity_user_profile);
		
		//change fonts
		setFonts();
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	
	private final void setFonts() {
 
        // text view label
        TextView txtBiografia = (TextView) findViewById(R.id.biografia);
        TextView txtPoesias = (TextView) findViewById(R.id.txtNumPoesias);
        
        TextView txtSeguidores = (TextView) findViewById(R.id.txtNumSeguidores);
        TextView txtSeguindo = (TextView) findViewById(R.id.txtNumSeguindo);
        
        TextView numComents = (TextView) findViewById(R.id.num_comments);
        TextView numLikes = (TextView) findViewById(R.id.num_likes);
        
        txtPoesias.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));
        txtSeguidores.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));
        txtSeguindo.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));
        txtBiografia.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));
        numComents.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));
        numLikes.setTypeface(FontsOverride.setAltheaRegular(getApplicationContext()));
        
 
	}
}
