package com.projetoles.verso;

import com.projetoles.adapter.ExpandablePoesiaAdapter;
import com.projetoles.controller.SeguidaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Seguida;
import com.projetoles.model.Usuario;

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

public class UserProfileActivity extends Activity {
	
	private SeguidaController mSeguidaController;
	private UsuarioController mUsuarioController;
	private Class mCallback;
	private Usuario mUsuario; 
	private Bundle mBundle;
	private ImageView mUserPicturePreview;
	private ImageView mUserPicture;
	private RelativeLayout mProfilePhotoContent;
	private CameraActivityBundle mCameraBundle;
	private ExpandableListView mExpListView;
	private ObjectListID<Poesia> mListPoesias;
	private ExpandablePoesiaAdapter mAdapter;
	private View mLoading;
	private ImageView mGoBack;
	private View mEmpty;

	private void fillPoesias() {
		mExpListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					mExpListView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});
		
		mAdapter = new ExpandablePoesiaAdapter(this, mExpListView, mListPoesias, mBundle, mLoading, mEmpty);

		mExpListView.setAdapter(mAdapter);
	}
	
	private void setUp() {
		setContentView(R.layout.activity_user_profile);
		getActionBar().setTitle("Perfil de " + mUsuario.getNome());
		getActionBar().hide(); 

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
		
		TextView seguindo = (TextView) findViewById(R.id.txtOtherNumSeguindo);
		TextView seguidores = (TextView) findViewById(R.id.txtOtherNumSeguidores);
		
		seguindo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserProfileActivity.this, SeguidoresActivity.class);
				intent.putExtra("usuario", mUsuario);
				intent.putExtra("callback", UserProfileActivity.class);
				intent.putExtra("seguindo", true);
				startActivity(intent);
				finish();
			}
		});
		
		seguidores.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserProfileActivity.this, SeguidoresActivity.class);
				intent.putExtra("usuario", mUsuario);
				intent.putExtra("callback", UserProfileActivity.class);
				intent.putExtra("seguindo", false);
				startActivity(intent);
				finish();
			}
		});

		mGoBack = (ImageView) findViewById(R.id.otherUseGoBack);
		mGoBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed(); 
			}
		});
		
		// set userPicture, precisa consertar nessa classe
		mUserPicturePreview = (ImageView) findViewById(R.id.otherUserPicture);
		mUserPicture = (ImageView) findViewById(R.id.otherProfilePhoto);
		mProfilePhotoContent = (RelativeLayout) findViewById(R.id.otherProfilePhotoContent);
		mLoading = findViewById(R.id.loading);
		mEmpty = findViewById(R.id.sem_poesia);

		mCameraBundle = new CameraActivityBundle(this, mUsuario, mUserPicturePreview, mUserPicture, mProfilePhotoContent);
		mCameraBundle.setFoto(mUsuario.getFoto());
		
		mExpListView = (ExpandableListView) findViewById(R.id.lvPoesiasDoUserExp);

		// preparing list data
		mListPoesias = new ObjectListID<Poesia>();
		
		for (PreloadedObject<Poesia> id : mUsuario.getPoesias().getList()) {
			mListPoesias.add(id);
		}
		
		fillPoesias();
		
		final Button seguir = (Button) findViewById(R.id.seguir);
		if (UsuarioController.usuarioLogado.equals(mUsuario)) {
			seguir.setVisibility(View.GONE);
		}

		PreloadedObject<Seguida> seguidaId = mUsuario.getSeguidores().getIntersecction(UsuarioController.usuarioLogado.getSeguindo());
		if (seguidaId != null) {
			seguir.setText("Seguindo");
			seguir.setBackgroundResource(R.drawable.seguir_ativo);
		}
		
		// listener para seguir
		seguir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PreloadedObject<Seguida> seguidaId = mUsuario.getSeguidores().getIntersecction(UsuarioController.usuarioLogado.getSeguindo());
				if (seguidaId != null) {
					mSeguidaController.delete(seguidaId.getId(), new OnRequestListener<String>(UserProfileActivity.this) {
						@Override
						public void onSuccess(String result) {
							UsuarioController.usuarioLogado.getSeguindo().remove(result);
							mUsuario.getSeguidores().remove(result);
							//mUsuarioController.save(mUsuario);
							seguir.setText("Seguir");
							seguir.setBackgroundResource(R.drawable.seguir);
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
							UsuarioController.usuarioLogado.getSeguindo().add(result.getId(), result.getDataCriacao().getTimeInMillis());
							mUsuario.getSeguidores().add(result.getId(), result.getDataCriacao().getTimeInMillis());
							//mUsuarioController.save(mUsuario);
							seguir.setText("Seguindo");
							seguir.setBackgroundResource(R.drawable.seguir_ativo);
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().hide(); 
		
		Bundle b = getIntent().getExtras();
		mUsuario = (Usuario) b.getParcelable("usuario");
		if (mUsuario == null) {
			b = b.getBundle("bundle");
			mUsuario = (Usuario) b.getParcelable("usuario");
		}
		mCallback = (Class) b.get("callback");
		mBundle = b;
						
		mSeguidaController = new SeguidaController(this);
		mUsuarioController = new UsuarioController(this);
		
		mUsuarioController.update(mUsuario, new OnRequestListener<Usuario>(this) {

			@Override
			public void onSuccess(Usuario result) {
				mUsuario = result;
				mUsuarioController.save(result);
				
				setUp();
			}

			@Override
			public void onError(String errorMessage) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						finish();
					}
				});
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
