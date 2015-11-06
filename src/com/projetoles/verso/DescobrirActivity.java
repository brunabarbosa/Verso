package com.projetoles.verso;

import com.projetoles.controller.UsuarioController;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class DescobrirActivity extends TabActivity {
	
	private UsuarioController mController;
	private TabHost mTabHost;
	public View mLoading;
	
	private void setTabs() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		TabSpec poesiasCurtidasSpec = mTabHost.newTabSpec("Poesias mais curtidas");
		TabSpec poesiasComentadasSpec = mTabHost.newTabSpec("Poesias mais comentadas");
		TabSpec pessoasRecentesSpec = mTabHost.newTabSpec("Poesias mais recentes");
		TabSpec pessoasSeguidasSpec = mTabHost.newTabSpec("Pessoas mais seguidas");
		
		poesiasCurtidasSpec.setIndicator("Top curtidas");
		Intent poesiasCurtidasIntent = new Intent(DescobrirActivity.this, MaisCurtidasActivity.class);
		poesiasCurtidasSpec.setContent(poesiasCurtidasIntent);
		 
		poesiasComentadasSpec.setIndicator("Top comentadas");
		Intent poesiasComentadasIntent = new Intent(DescobrirActivity.this, MaisComentadasActivity.class);
		poesiasComentadasSpec.setContent(poesiasComentadasIntent);

		pessoasRecentesSpec.setIndicator("Mais recentes");
		Intent poesiasRecentesIntent = new Intent(DescobrirActivity.this, MaisRecentesActivity.class);
		pessoasRecentesSpec.setContent(poesiasRecentesIntent);
		
		pessoasSeguidasSpec.setIndicator("Top seguidos");
		Intent pessoasSeguidasIntent = new Intent(DescobrirActivity.this, MaisSeguidosActivity.class);
		pessoasSeguidasSpec.setContent(pessoasSeguidasIntent);

		mTabHost.addTab(poesiasCurtidasSpec);
		mTabHost.addTab(poesiasComentadasSpec);
		mTabHost.addTab(pessoasRecentesSpec);
		mTabHost.addTab(pessoasSeguidasSpec);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				getActionBar().setTitle(arg0);
				setTabColor(mTabHost);
			}
		});
		setTabColor(mTabHost);
	}

	private void setTabColor(TabHost tabhost) {
		for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
			tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#f8f5af")); //unselected
		}
		if(tabhost.getCurrentTab() == 0) {
			tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#F5DA81")); //1st tab selected
		} else {
			tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#F5DA81")); //2nd tab selected
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descobrir);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Poesias mais curtidas");

		mController = new UsuarioController(this);
		
		setTabs();

		mLoading = findViewById(R.id.mainLoading);
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
