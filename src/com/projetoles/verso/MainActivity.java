package com.projetoles.verso;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.projetoles.controller.UsuarioController;
import com.projetoles.model.Usuario;

public class MainActivity extends TabActivity {
	
	public static TabActivity sInstance;
	
	private UsuarioController mController;
	private TabHost mTabHost;
	private ImageView mBtnCriarPoema;
	private ImageView mBtnPesquisar;
	private Usuario mUsuario;
	private View mLoading;
	private CameraActivityBundle mCameraBundle;
	
	private void setTabs() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		TabSpec homeSpec = mTabHost.newTabSpec("Home");
		TabSpec comporSpec = mTabHost.newTabSpec("Compor");
		TabSpec sobreSpec = mTabHost.newTabSpec("Sobre");
		TabSpec buscaSpec = mTabHost.newTabSpec("Home");
		
		homeSpec.setIndicator("", getResources().getDrawable(R.drawable.home));
		Intent homeIntent = new Intent(MainActivity.this, PerfilActivity.class);
		homeSpec.setContent(homeIntent);
		 
		comporSpec.setIndicator("", getResources().getDrawable(R.drawable.compor));
		Intent comporIntent = new Intent(MainActivity.this, CriarPoesiaActivity.class);
		comporSpec.setContent(comporIntent);

		sobreSpec.setIndicator("", getResources().getDrawable(R.drawable.sobre));
		Intent sobreIntent = new Intent(MainActivity.this, NotificacoesTelaActivity.class);
		sobreSpec.setContent(sobreIntent);

		buscaSpec.setIndicator("", getResources().getDrawable(R.drawable.busca));
		Intent buscaIntent = new Intent(MainActivity.this, PesquisarActivity.class);
		buscaSpec.setContent(buscaIntent);
		
		mTabHost.addTab(homeSpec);
		mTabHost.addTab(comporSpec);
		mTabHost.addTab(sobreSpec);
		mTabHost.addTab(buscaSpec);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				if (mTabHost.getCurrentTab() == 1) {
					mBtnCriarPoema.setVisibility(View.VISIBLE);
				} else {
					mBtnCriarPoema.setVisibility(View.GONE);
				}
				if (mTabHost.getCurrentTab() == 3) {
					mBtnPesquisar.setVisibility(View.VISIBLE);
				} else {
					mBtnPesquisar.setVisibility(View.GONE);
				}
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
		setContentView(R.layout.activity_tabbed_main);
		
		sInstance = this;
		
		getActionBar().hide(); 

		mController = new UsuarioController(this);
		mUsuario = UsuarioController.usuarioLogado;
		
		setTabs();

		mBtnCriarPoema = (ImageView) findViewById(R.id.btnCriarPoema);
		mBtnPesquisar = (ImageView) findViewById(R.id.btnPesquisar);
		mLoading = findViewById(R.id.mainLoading);
		
		View profilePhotoContent = findViewById(R.id.profilePhotoContent);
		Button btnEditPhoto = (Button) findViewById(R.id.btnProfilePhotoEdit);
		ImageView fotoFull = (ImageView) findViewById(R.id.profilePhoto);
		ImageView fotoPreview = (ImageView) findViewById(R.id.userPicture);
		
		mCameraBundle = new CameraActivityBundle(this, fotoPreview, fotoFull, profilePhotoContent);
		mCameraBundle.setFoto(UsuarioController.usuarioLogado.getFoto());
		mCameraBundle.editarFoto(btnEditPhoto);
		
		//set userName
		TextView usuarioName = (TextView) findViewById(R.id.userName);
		usuarioName.setText(mUsuario.getNome());
		
		// submenu
		TextView biografia = (TextView) findViewById(R.id.textBiografia);
		biografia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, BiografiaActivity.class);
				intent.putExtra("usuario", mUsuario);
				intent.putExtra("callback", MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		} else if (id == R.id.action_logout) {
			mController.logout();
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
	    mCameraBundle. onActivityResult(requestCode, resultCode, imageReturnedIntent, mLoading);
	}
	
}
