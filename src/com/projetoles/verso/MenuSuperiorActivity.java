package com.projetoles.verso;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MenuSuperiorActivity extends TabActivity{
	TabHost mTabHost;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_menu_superior);
		mTabHost = getTabHost();
		
		TabSpec compondoSpec = mTabHost.newTabSpec("Compondo");
		compondoSpec.setIndicator("", getResources().getDrawable(R.drawable.compondo));
		Intent compondoIntent = new Intent(this, CriarPoemaActivity.class);
		compondoSpec.setContent(compondoIntent);

		TabSpec sanfonaSpec = mTabHost.newTabSpec("Sanfona");
		sanfonaSpec.setIndicator("", getResources().getDrawable(R.drawable.sanfona));
		Intent sanfonaIntent = new Intent(this, CriarPoemaActivity.class);
		sanfonaSpec.setContent(sanfonaIntent);
		
		mTabHost.addTab(compondoSpec);
		mTabHost.addTab(sanfonaSpec);
		
	}

}
