package com.projetoles.adapter;

import java.util.ArrayList;
import java.util.List;

import com.projetoles.controller.CurtidaController;
import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Curtida;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.Usuario;
import com.projetoles.verso.ActivityUtils;
import com.projetoles.verso.ComentarioActivity;
import com.projetoles.verso.CurtidaActivity;
import com.projetoles.verso.MainActivity;
import com.projetoles.verso.PesquisarActivity;
import com.projetoles.verso.R;
import com.projetoles.verso.ResultadoPesquisaActivity;
import com.projetoles.verso.SharingActivity;
import com.projetoles.verso.UserProfileActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandablePoesiaAdapter extends BaseExpandableListAdapter {

	private Activity mContext;
	private List<Poesia> mListPoesias;
	private CurtidaController mCurtidaController;
	private PoesiaController mPoesiaController;
	private Bundle mBundle;
	private Usuario mUsuario;
	private View mLoading;
	private Button btnCompartilharFacebook;
	private Button btnCompartilharApp;

	public ExpandablePoesiaAdapter(Activity context, List<Poesia> listPoesias,
			Bundle bundle) {
		this.mContext = context;
		this.mListPoesias = listPoesias;
		this.mCurtidaController = new CurtidaController(context);
		this.mPoesiaController = new PoesiaController(context);
		this.mBundle = bundle;
		this.mUsuario = UsuarioController.usuarioLogado;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.mListPoesias.get(groupPosition).getPoesia();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	private static class ClickableString extends ClickableSpan {  
	    private View.OnClickListener mListener;          
	    public ClickableString(View.OnClickListener listener) {              
	        mListener = listener;  
	    }          
	    @Override  
	    public void onClick(View v) {  
	        mListener.onClick(v);  
	    }        
	}
	
	private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
	    SpannableString link = new SpannableString(text);
	    link.setSpan(new ClickableString(listener), 0, text.length(), 
	        SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
	    return link;
	}
	
	private void makeLinksFocusable(TextView tv) {
	    MovementMethod m = tv.getMovementMethod();  
	    if ((m == null) || !(m instanceof LinkMovementMethod)) {  
	        if (tv.getLinksClickable()) {  
	            tv.setMovementMethod(LinkMovementMethod.getInstance());  
	        }  
	    }  
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

		final Poesia poesia = mListPoesias.get(groupPosition);

		TextView tags = (TextView) convertView.findViewById(R.id.tags);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		mLoading = MainActivity.sInstance.findViewById(R.id.mainLoading);

		if (poesia.getTags().trim().isEmpty()) {
			tags.setVisibility(View.GONE);
		} else {
			tags.setText("");
			String[] poesiasTags = poesia.getTags().split(" ");
			for (int i = 0; i < poesiasTags.length; i++) {
				final String poesiaTag = poesiasTags[i];
				poesiasTags[i] = "#" + poesiasTags[i];
				SpannableString link = makeLinkSpan(poesiasTags[i], new View.OnClickListener() {
					private String tag = poesiaTag;
					@Override
					public void onClick(View arg0) {
						mPoesiaController.pesquisar("", "", tag, "", new OnRequestListener<ArrayList<String>>(mContext) {
							
							@Override
							public void onSuccess(ArrayList<String> resultados) {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								Intent i = new Intent(mContext, ResultadoPesquisaActivity.class);
								i.putStringArrayListExtra("resultados", resultados);
								mContext.startActivity(i);
							}
							
							@Override
							public void onError(String errorMessage) {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								ActivityUtils.showMessageDialog(mContext, "Um erro ocorreu", errorMessage, null);
							}
						});
					}
				});
				tags.append(link);
				tags.append(" ");
			}
			makeLinksFocusable(tags);
		}

		date.setText("Postado em " + CalendarUtils.getDataFormada(poesia.getDataCriacao()) + " por ");
		SpannableString link = makeLinkSpan(poesia.getPostador().getNome(), new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserProfileActivity.class);
				intent.putExtra("usuario", poesia.getPostador());
				mContext.startActivity(intent);
			}
		});
		date.append(link);
		date.append(".");
		makeLinksFocusable(date);
		/*Spannable dateSpan = new SpannableString("Postado em "
				+ CalendarUtils.getDataFormada(poesia.getDataCriacao())
				+ " por " + poesia.getPostador().getNome());
		dateSpan.setSpan(new UnderlineSpan(),
				(dateSpan.length() - poesia.getPostador().getNome().length()),
				dateSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		date.setText(dateSpan);
		date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, UserProfileActivity.class);
				intent.putExtra("usuario", poesia.getPostador());
				mContext.startActivity(intent);
			}
		});*/

		btnCompartilharFacebook = (Button) convertView.findViewById(R.id.btnCompartilharFacebook);

		// Compartilhar no facebook
		btnCompartilharFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent share = new Intent(mContext, SharingActivity.class);

				String saida = poesia.getTitulo().toString() + "\n" + "\n"
						+ poesia.getPoesia().toString() + "\n" + "\n"
						+ poesia.getAutor() + "\n" + "\n" + "#appVer(Só)";

				share.putExtra("titulo", poesia.getTitulo().toString() + "\n"
						+ "\n");
				share.putExtra("texto", saida);

				mContext.startActivity(share);

			}
		});

		btnCompartilharApp = (Button) convertView
				.findViewById(R.id.btnCompartilharApp);
		
		if (mUsuario.getPoesias().contains(poesia.getId())) {
			btnCompartilharApp.setVisibility(View.GONE);
			btnCompartilharApp.setVisibility(View.GONE);
		} else {
			btnCompartilharApp.setVisibility(View.VISIBLE);
			btnCompartilharApp.setVisibility(View.VISIBLE);
		}

		// Compartilhar na aplicação
		btnCompartilharApp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPoesiaController.post(poesia.getTitulo(), poesia.getAutor(),
					poesia.getPostador(), poesia.getPoesia(),
					poesia.getDataCriacao(), poesia.getTags(),
					new OnRequestListener<Poesia>(mContext) {

						@Override
						public void onSuccess(Poesia poesia) {
							if (!mUsuario.getPoesias().contains(poesia.getId())) {
								mUsuario.getPoesias().add(poesia.getId());
							} else {
								ActivityUtils.showMessageDialog(mContext,
										"Um erro ocorreu", "t", mLoading);
							}

						}

						@Override
						public void onError(final String errorMessage) {
							ActivityUtils.showMessageDialog(mContext, "Um erro ocorreu", errorMessage, mLoading);
						}
					});
			}
		});

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.mListPoesias.get(groupPosition).getTitulo();
	}

	@Override
	public int getGroupCount() {
		return this.mListPoesias.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	private void excluirPoesia(final ImageView deleteButton, final Poesia poesia) {
		// Excluir poesia
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mContext)
					.setTitle("Você realmente deseja excluir essa poesia?")
					.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mPoesiaController.delete(poesia.getId(), new OnRequestListener<String>(mContext) {

								@Override
								public void onSuccess(String result) {
									mListPoesias.remove(poesia);
									notifyDataSetChanged();
								}

								@Override
								public void onError(String errorMessage) {
									ActivityUtils.showMessageDialog(mContext, "Um erro ocorreu", "Não foi possível excluir esta poesia. Tente novamente", null);
								}
							});
						}
					}).setNegativeButton("Não", null)
					.create().show();
			}
		});
	}
	
	private void editarPoesia(final ImageView editButton, final Poesia poesia) {
		editButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				final View alertView = inflater.inflate(R.layout.activity_cria_poesia, null);
				final EditText alertTitulo = (EditText) alertView.findViewById(R.id.poemaTitulo);
				final EditText alertAutor = (EditText) alertView.findViewById(R.id.poemaAutor);
				final EditText alertTags = (EditText) alertView.findViewById(R.id.poemaTags);
				final EditText alertPoesia = (EditText) alertView.findViewById(R.id.poema);
				alertTitulo.setText(poesia.getTitulo());
				alertAutor.setText(poesia.getAutor());
				alertTags.setText(poesia.getTags());
				alertPoesia.setText(poesia.getPoesia());
				new AlertDialog.Builder(mContext)
					.setTitle("Editar poesia")
					.setView(alertView)
					.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Poesia novaPoesia;
							try {
								novaPoesia = new Poesia(poesia.getId(), poesia.getDataCriacao(), 
										alertTitulo.getText().toString(), poesia.getPostador(), 
										alertAutor.getText().toString(), alertPoesia.getText().toString(), 
										alertTags.getText().toString(), new ObjectListID(), new ObjectListID());
								mPoesiaController.put(novaPoesia, new OnRequestListener<Poesia>(mContext) {

									@Override
									public void onSuccess(Poesia result) {
										poesia.setTitulo(alertTitulo.getText().toString());
										poesia.setAutor(alertAutor.getText().toString());
										poesia.setTags(alertTags.getText().toString());
										poesia.setPoesia(alertPoesia.getText().toString());
										notifyDataSetChanged();
									}

									@Override
									public void onError(String errorMessage) {
										new AlertDialog.Builder(mContext)
											.setTitle("Um erro ocorreu")
											.setMessage(errorMessage)
											.create().show();
									}
								});
							} catch (Exception e) {
								new AlertDialog.Builder(mContext)
									.setTitle("Um erro ocorreu")
									.setMessage(e.getMessage())
									.create().show();
							}
						}
					})
					.setNegativeButton("Cancelar", null)
					.create().show();
			}
		});
	}
	
	private void curtirPoesia(final ImageView btnLike, final TextView numLikes, final Poesia poesia) {
		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (v.isClickable()) {
					final Poesia clicada = (Poesia) v.getTag();
					v.setClickable(false);
					String curtidaId = mUsuario.getCurtidas().getIntersecction(clicada.getCurtidas());
					if (curtidaId != null) {
						mCurtidaController.delete(curtidaId,
							new OnRequestListener<String>(mContext) {
								@Override
								public void onSuccess(String id) {
									mUsuario.getCurtidas().remove(id);
									clicada.getCurtidas().remove(id);
									((ImageView) v).setImageResource(R.drawable.like_icon);
									numLikes.setText(String.valueOf(clicada.getCurtidas().size()));
									v.setClickable(true);
								}
	
								@Override
								public void onError(String errorMessage) {
									System.out.println("ERROR descurtir: " + errorMessage);
									v.setClickable(true);
								}
							});
					} else {
						mCurtidaController.post(mUsuario, poesia,
							new OnRequestListener<Curtida>(mContext) {
								@Override
								public void onSuccess(Curtida curtida) {
									mUsuario.getCurtidas().add(curtida.getId());
									clicada.getCurtidas().add(curtida.getId());
									((ImageView) v).setImageResource(R.drawable.like_icon_ativo);
									numLikes.setText(String.valueOf(clicada.getCurtidas().size()));
									v.setClickable(true);
								}

								@Override
								public void onError(String errorMessage) {
									System.out.println("ERROR curtir: " + errorMessage);
									v.setClickable(true);
								}
							});
					}
				}
			}
		});
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);

		final Poesia poesia = mListPoesias.get(groupPosition);

		final TextView autor = (TextView) convertView.findViewById(R.id.author);
		autor.setText(poesia.getAutor());
		final TextView numLikes = (TextView) convertView.findViewById(R.id.num_likes);
		numLikes.setText(String.valueOf(poesia.getCurtidas().size()));
		final TextView numComments = (TextView) convertView.findViewById(R.id.num_comments);
		numComments.setText(String.valueOf(poesia.getComentarios().size()));
		final ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
		final ImageView edit = (ImageView) convertView.findViewById(R.id.edit);

		if (!poesia.getPostador().equals(UsuarioController.usuarioLogado)) {
			delete.setVisibility(View.GONE);
			edit.setVisibility(View.GONE);
		} else {
			delete.setVisibility(View.VISIBLE);
			edit.setVisibility(View.VISIBLE);
		}

		// Botão tela de curtidas
		numLikes.setTag(poesia);
		numLikes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, CurtidaActivity.class);
				intent.putExtra("poesia", (Poesia) v.getTag());
				mContext.startActivity(intent);
			}
		});

		// Botao tela de comentarios
		ImageView btnComment = (ImageView) convertView.findViewById(R.id.commentIcon);
		btnComment.setTag(poesia);
		btnComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ComentarioActivity.class);
				intent.putExtra("poesia", (Poesia) v.getTag());
				intent.putExtra("callback", mContext.getClass());
				intent.putExtra("bundle", mBundle);
				mContext.startActivity(intent);
				mContext.finish();
			}
		});

		// Botao curtir
		final ImageView btnLike = (ImageView) convertView.findViewById(R.id.facebookIcon);
		if (mUsuario.getCurtidas().getIntersecction(poesia.getCurtidas()) != null) {
			btnLike.setImageResource(R.drawable.like_icon_ativo);
		} else {
			btnLike.setImageResource(R.drawable.like_icon);
		}
		btnLike.setTag(poesia);
		
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		editarPoesia(edit, poesia);
		
		excluirPoesia(delete, poesia);

		curtirPoesia(btnLike, numLikes, poesia);
		
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
