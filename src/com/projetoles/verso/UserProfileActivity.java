package com.projetoles.verso;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.SeguidaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.Poesia;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

public class UserProfileActivity extends Activity {
	
	private PoesiaController mPoesiaController;
	private ImageView mUserPicturePreview;
	private ImageView mUserPicture;
	private RelativeLayout mProfilePhotoContent;
	private Usuario mUsuario; 
	private Class mCallback;
	private CameraActivityBundle mCameraBundle;
	private ExpandableListView mExpListView;
	private ArrayList<Poesia> mListPoesias;
	private ExpandablePoesiaAdapter mAdapter; 
	private SeguidaController mSeguidaController;

	private void setUp() {
		TextView usuarioName = (TextView) findViewById(R.id.otherUserName);
		usuarioName.setText(mUsuario.getNome());

		// submenu
		TextView biografia = (TextView) findViewById(R.id.otherTextBiografia);
		biografia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserProfileActivity.this, BiografiaActivity.class);
				intent.putExtra("usuario", mUsuario);
				intent.putExtra("callback", UserProfileActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// set userPicture, precisa consertar nessa classe
		mUserPicturePreview = (ImageView) findViewById(R.id.otherUserPicture);
		mUserPicture = (ImageView) findViewById(R.id.otherProfilePhoto);
		mProfilePhotoContent = (RelativeLayout) findViewById(R.id.otherProfilePhotoContent);
		
		mCameraBundle = new CameraActivityBundle(this, mUserPicturePreview, mUserPicture, mProfilePhotoContent);
		mCameraBundle.setFoto(mUsuario.getFoto());
		
		TextView semPoesia = (TextView) findViewById(R.id.sem_poesia);
		if (mUsuario.getPoesias().isEmpty()) {
			semPoesia.setVisibility(View.VISIBLE);
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
	    getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle b = getIntent().getExtras();
		mUsuario = (Usuario) b.getParcelable("usuario");
		mCallback = (Class) b.get("callback");
						
		mSeguidaController = new SeguidaController(this);
		
		setUp();
		
		mPoesiaController = new PoesiaController(this);
		mExpListView = (ExpandableListView) findViewById(R.id.lvPoesiasDoUserExp);

		// preparing list data
		mListPoesias = new ArrayList<Poesia>();
		mAdapter = new ExpandablePoesiaAdapter(MainActivity.sInstance, mListPoesias, null);

		mExpListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					mExpListView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});

		mExpListView.setAdapter(mAdapter);
		for (String id : mUsuario.getPoesias().getList()) {
			mPoesiaController.get(id, new OnRequestListener<Poesia>(this) {

				@Override
				public void onSuccess(Poesia p) {
					mListPoesias.add(p);
					Collections.sort(mListPoesias);
					mAdapter.notifyDataSetChanged();

				}

				@Override
				public void onError(String errorMessage) {
					System.out.println(errorMessage);
				}
			});
		}
		
		// listener para seguir
		Button seguir = (Button) findViewById(R.id.seguir);

		seguir.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				final String seguidaId = UsuarioController.usuarioLogado.getSeguindo().getIntersecction(mUsuario.getSeguidores());
				if (seguidaId != null) {
					mSeguidaController.delete(seguidaId, new OnRequestListener<String>(UserProfileActivity.this) {
						@Override
						public void onSuccess(String result) {
							mUsuario.getSeguidores().remove(seguidaId);
							UsuarioController.usuarioLogado.getSeguindo().remove(seguidaId);
							Button seguir = (Button) findViewById(R.id.seguir);
							seguir.setVisibility(View.VISIBLE);
						}

						@Override
						public void onError(String errorMessage) {
							System.out.println(errorMessage);

						}
					});
				} else {
					mSeguidaController.post(UsuarioController.usuarioLogado, mUsuario, new OnRequestListener<Seguida>(UserProfileActivity.this) {
						@Override
						public void onSuccess(Seguida result) {
							mUsuario.getSeguidores().add(result.getId());
							UsuarioController.usuarioLogado.getSeguindo().add(result.getId());
							Button seguir = (Button) findViewById(R.id.seguir);
							seguir.setVisibility(View.GONE);
						}

						@Override
						public void onError(String errorMessage) {
							System.out.println(errorMessage);

						}
					});
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		finish();
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
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
