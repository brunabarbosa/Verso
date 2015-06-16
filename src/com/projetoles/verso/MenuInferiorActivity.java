package com.projetoles.verso;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MenuInferiorActivity extends TabActivity{
	TabHost mTabHost;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_menu_inferior);
		mTabHost = getTabHost();
		
		TabSpec homeSpec = mTabHost.newTabSpec("Home");
		homeSpec.setIndicator("", getResources().getDrawable(R.drawable.home));
		Intent homeIntent = new Intent(this, CriarPoemaActivity.class);
		homeSpec.setContent(homeIntent);
		
		TabSpec comporSpec = mTabHost.newTabSpec("Compor");
		comporSpec.setIndicator("", getResources().getDrawable(R.drawable.compor));
		Intent comporIntent = new Intent(this, CriarPoemaActivity.class);
		comporSpec.setContent(comporIntent);

		TabSpec sobreSpec = mTabHost.newTabSpec("Sobre");
		sobreSpec.setIndicator("", getResources().getDrawable(R.drawable.sobre));
		Intent sobreIntent = new Intent(this, CriarPoemaActivity.class);
		sobreSpec.setContent(sobreIntent);

		TabSpec buscaSpec = mTabHost.newTabSpec("Home");
		buscaSpec.setIndicator("", getResources().getDrawable(R.drawable.busca));
		Intent buscaIntent = new Intent(this, CriarPoemaActivity.class);
		buscaSpec.setContent(buscaIntent);
		
		mTabHost.addTab(homeSpec);
		mTabHost.addTab(comporSpec);
		mTabHost.addTab(sobreSpec);
		mTabHost.addTab(buscaSpec);
		
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

		    @Override
		    public void onTabChanged(String arg0) {

		        setTabColor(mTabHost);
		    }
		     });
		     setTabColor(mTabHost);
		
	}
	
	private void setTabColor(TabHost tabhost) {

	    for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
	        tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.CYAN); //unselected

	    if(tabhost.getCurrentTab()==0)
	           tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.RED); //1st tab selected
	    else
	           tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.BLUE); //2nd tab selected
	}

}
