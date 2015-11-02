package com.projetoles.adapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.projetoles.controller.CompartilhamentoController;
import com.projetoles.controller.CurtidaController;
import com.projetoles.controller.PoesiaController;
import com.projetoles.controller.UsuarioController;
import com.projetoles.dao.OnRequestListener;
import com.projetoles.model.CalendarUtils;
import com.projetoles.model.Comentario;
import com.projetoles.model.Compartilhamento;
import com.projetoles.model.Curtida;
import com.projetoles.model.ObjectListID;
import com.projetoles.model.Poesia;
import com.projetoles.model.PreloadedObject;
import com.projetoles.model.Usuario;
import com.projetoles.verso.ActivityUtils;
import com.projetoles.verso.ClickableString;
import com.projetoles.verso.ComentarioActivity;
import com.projetoles.verso.CurtidaActivity;
import com.projetoles.verso.MainActivity;
import com.projetoles.verso.R;
import com.projetoles.verso.ResultadoPesquisaActivity;
import com.projetoles.verso.SharingInstagramActivity;
import com.projetoles.verso.UserProfileActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandablePoesiaAdapter extends BaseExpandableListAdapter {
 
	private static final int NUMBERS_TO_LOAD = 10;
	private static final int VISIBLE_THRESHOLD = 3;
	
	private Activity mContext;
	private ObjectListID<Poesia> mListPoesias;
	private CurtidaController mCurtidaController;
	private PoesiaController mPoesiaController;
	private CompartilhamentoController mCompartilhamentoController;
	private Bundle mBundle;
	private Usuario mUsuario;
	private View mLoading;
	private Button btnCompartilharFacebook;
	private Button btnCompartilharApp;
	private Button btnCompartilhar;
	private ExpandableListView mExpListView;
	private View mEmpty;
	private boolean mIsOnFeed;
	private List<Poesia> mUpdatedList;
	private Comparator<PreloadedObject<Poesia>> mComparator;
	
	private int mPreviousTotalItemCount = 0;
	private boolean mLoadingPoesias = false;
	private int mAlreadyLoaded;
	private int mExpectedLoaded;
	
	public ExpandablePoesiaAdapter(final Activity context, final ExpandableListView expListView, 
			final ObjectListID<Poesia> listPoesias, final Bundle bundle, final View loading, final View empty, 
			final boolean isOnFeed, Comparator<PreloadedObject<Poesia>> comparator) {
		this.mContext = context;
		this.mExpListView = expListView;
		this.mListPoesias = listPoesias;
		this.mCurtidaController = new CurtidaController(context);
		this.mPoesiaController = new PoesiaController(context);
		this.mCompartilhamentoController = new CompartilhamentoController(context);
		this.mComparator = comparator;
		this.mBundle = bundle;
		this.mLoading = loading;
		this.mEmpty = empty;
		this.mIsOnFeed = isOnFeed;
		this.mUpdatedList = new ArrayList<Poesia>();
		this.mListPoesias.sort(this.mComparator);
		UsuarioController controller = new UsuarioController(context);
		controller.getUsuarioLogado(new OnRequestListener<Usuario>(context) {
 
			@Override
			public void onSuccess(Usuario usuarioLogado) {
				ExpandablePoesiaAdapter.this.mUsuario = usuarioLogado;
				mLoading.setVisibility(View.INVISIBLE);
				if (!mListPoesias.isEmpty()) {
					loadNextPage(NUMBERS_TO_LOAD);
					mEmpty.setVisibility(View.INVISIBLE);
					mExpListView.setOnScrollListener(new OnScrollListener() {
						
						@Override
						public void onScrollStateChanged(AbsListView view, int scrollState) {
							
						}
						
						@Override
						public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
							if (totalItemCount < mPreviousTotalItemCount) {
								mPreviousTotalItemCount = totalItemCount;
								if (totalItemCount == 0) mLoadingPoesias = true;
							}
							if (!mLoadingPoesias) {
								mLoading.setVisibility(View.INVISIBLE);
							}
							if (!mLoadingPoesias && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
								loadNextPage(totalItemCount + NUMBERS_TO_LOAD);
							}
						}
					});
				} else {
					mEmpty.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
		}, null);
	}

	public void hideEmptyMessage() {
		mEmpty.setVisibility(View.GONE);
	}
	
	private void loadNextPage(int itemsToLoad) {
		if (mPreviousTotalItemCount >= mListPoesias.size()) {
			mLoadingPoesias = false;
		} else {
			mAlreadyLoaded = 0;
			mExpectedLoaded = 0;
			mLoadingPoesias = true;
			for (int i = 0; i < Math.min(mListPoesias.size(), itemsToLoad); i++) {
				if (!mListPoesias.get(i).isLoaded()) {
					mExpectedLoaded++;
					mListPoesias.get(i).load(mPoesiaController, new OnRequestListener<Poesia>(mContext) {

						@Override
						public void onSuccess(Poesia result) {
							mAlreadyLoaded++;
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingPoesias = false;
							}
							mListPoesias.sort(mComparator);
							notifyDataSetChanged();
						}

						@Override
						public void onError(String errorMessage) {
							mAlreadyLoaded++;
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingPoesias = false;
							}
						}

						@Override
						public void onTimeout() {
							mAlreadyLoaded++;
							if (mAlreadyLoaded >= mExpectedLoaded) {
								mLoading.setVisibility(View.GONE);
								mLoadingPoesias = false;
							}
						}
					});
				}
				if (mExpectedLoaded > 0) {
					mLoading.setVisibility(View.VISIBLE);
					mLoadingPoesias = true;
				} else {
					mLoading.setVisibility(View.GONE);
					mLoadingPoesias = false;
				}
			}
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.mListPoesias.get(groupPosition).getPureLoadedObj().getPoesia();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	public static void setPoesiaData(TextView date, final Poesia poesia, final Activity context) {
		date.setText((poesia.getCompartilhadoPor().isEmpty() ? "Postado em " : "Compartilhado em ") + CalendarUtils.getDataFormada(poesia.getDataCriacao()) 
			+ (poesia.getCompartilhadoPor().isEmpty() ? "" : " e postado ") + " por ");
		SpannableString link = ClickableString.makeLinkSpan(poesia.getPostador().getNome(), new View.OnClickListener() {
			  
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserProfileActivity.class);
				intent.putExtra("usuario", poesia.getPostador());
		 		context.startActivity(intent);
			}
		});
		date.append(link);
		date.append(".");
		ClickableString.makeLinksFocusable(date);
	}
	
	public static void setPoesiaTags(TextView tags, Poesia poesia, final PoesiaController controller, final Activity context) {
		if (poesia.getTags().trim().isEmpty()) {
			tags.setVisibility(View.GONE);
		} else {
			tags.setVisibility(View.VISIBLE);
			tags.setText("");
			String[] poesiasTags = poesia.getTags().replaceAll("  ", " ").split(" ");
			for (int i = 0; i < poesiasTags.length; i++) {
				final String poesiaTag = poesiasTags[i]; 
				poesiasTags[i] = "#" + poesiasTags[i];
				SpannableString link = ClickableString.makeLinkSpan(poesiasTags[i], new View.OnClickListener() {
					private String tag = poesiaTag;
					@Override
					public void onClick(View arg0) {
						controller.pesquisar("", "", tag, "", new OnRequestListener<ObjectListID<Poesia>>(context) {
							
							@Override
							public void onSuccess(ObjectListID<Poesia> resultados) {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								Intent i = new Intent(context, ResultadoPesquisaActivity.class);
								i.putExtra("resultados", resultados);
								context.startActivity(i);
							}
							
							@Override
							public void onError(String errorMessage) {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								ActivityUtils.showMessageDialog(context, "Um erro ocorreu", errorMessage, null);
							}

							@Override
							public void onTimeout() {
								((MainActivity)MainActivity.sInstance).mLoading.setVisibility(View.GONE);
								ActivityUtils.showMessageDialog(context, "Ops", "Ocorreu um problema com sua requisição. Verifique sua conexão com a internet.", null);
							}
						});
					}
				});
				tags.append(link);
				tags.append(" ");
			}
			ClickableString.makeLinksFocusable(tags);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent, int layout, final OnRequestListener<Pair<View, Poesia> > callback) {
    	if (convertView == null) {
  			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  			convertView = inflater.inflate(layout, parent, false);
  		}
  		convertView.setVisibility(View.GONE);
  		final View fConvertView = convertView;
  		Poesia loadedObj = mListPoesias.get(position).getPureLoadedObj();
  		if (loadedObj != null) {
  			if (mUpdatedList.contains(loadedObj)) {
  				fConvertView.setVisibility(View.VISIBLE);
  				callback.onSuccess(new Pair<View, Poesia>(fConvertView, loadedObj));
  			} else {
  				callback.onSuccess(new Pair<View, Poesia>(fConvertView, loadedObj));
  				mListPoesias.get(position).getLoadedObj(mPoesiaController, new OnRequestListener<Poesia>(callback.getContext()) {

					@Override
					public void onSuccess(Poesia result) {
						fConvertView.setVisibility(View.VISIBLE);
						mUpdatedList.add(result);
						callback.onSuccess(new Pair<View, Poesia>(fConvertView, result)); 
					}

					@Override
					public void onError(String errorMessage) {
						callback.onError(errorMessage);
					}

					@Override
					public void onTimeout() {
						callback.onTimeout();
					}
				});
  			}
  		}
  		return convertView;
  	}
	
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition);
		return getView(groupPosition, convertView, parent, R.layout.list_item, new OnRequestListener<Pair<View,Poesia>>(mContext) {

			@Override
			public void onSuccess(Pair<View, Poesia> result) {
				final View convertView = result.first;
				final Poesia poesia = result.second;
				TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
				TextView txtListChildTitle = (TextView) convertView.findViewById(R.id.lblListItemTitle);
				TextView tags = (TextView) convertView.findViewById(R.id.tags);
				TextView date = (TextView) convertView.findViewById(R.id.date);
				
				setPoesiaTags(tags, poesia, mPoesiaController, mContext);

				setPoesiaData(date, poesia, mContext);
			
				btnCompartilhar = (Button) convertView.findViewById(R.id.btnCompartilhar);

				// Compartilhar
				btnCompartilhar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent share = new Intent(mContext, SharingInstagramActivity.class);

						String saida = poesia.getTitulo().toString() + "\n\n"
								+ poesia.getPoesia().toString() + "\n" + "\n"
								+ poesia.getAutor();

						share.putExtra("titulo", poesia.getTitulo().toString() + "\n" + "\n");
						share.putExtra("texto", saida);

						mContext.startActivity(share);
					}
				});

				btnCompartilharApp = (Button) convertView.findViewById(R.id.btnCompartilharApp);
				
				if (poesia.getPostador().equals(mUsuario) || poesia.isCompartilhado(mUsuario)) {
					btnCompartilharApp.setVisibility(View.GONE);
				} else {
					btnCompartilharApp.setVisibility(View.VISIBLE);
				}

				// Compartilhar na aplicação
				btnCompartilharApp.setOnClickListener(new OnClickListener() {
					@Override 
					public void onClick(View v) {
						System.out.println("Compartilhando...");
						mLoading.setVisibility(View.VISIBLE);
						btnCompartilharApp.setEnabled(false);
						mCompartilhamentoController.post(mUsuario, poesia, new OnRequestListener<Compartilhamento>(mContext) {

							@Override
							public void onSuccess(Compartilhamento result) {
								mLoading.setVisibility(View.GONE);
								btnCompartilharApp.setEnabled(true);
								if (!poesia.isCompartilhado(mUsuario)) {
									poesia.getCompartilhadoPor().add(result);
								}
								notifyDataSetChanged();
								ActivityUtils.showMessageDialog(mContext, "Sucesso!", "Poesia compartilhada com sucesso!", mLoading);
							}

							@Override
							public void onError(String errorMessage) {
								mLoading.setVisibility(View.GONE);
								btnCompartilharApp.setEnabled(true);
								ActivityUtils.showMessageDialog(mContext, "Um erro ocorreu", errorMessage, mLoading);
							}

							@Override
							public void onTimeout() {
								mLoading.setVisibility(View.GONE);
								btnCompartilharApp.setEnabled(true);
								ActivityUtils.showMessageDialog(mContext, "Ops", "Ocorreu um erro com sua requisição. Verifique sua conexão com a internet.", mLoading);
							}
						});
					}
				});

				txtListChild.setText(childText);
				txtListChildTitle.setText(poesia.getTitulo());
			}

			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) { 
		return mListPoesias.get(groupPosition).getPureLoadedObj() == null ? "" : 
			this.mListPoesias.get(groupPosition).getPureLoadedObj().getTitulo();
	}

	@Override
	public int getGroupCount() {
		int count = 0;
		for (PreloadedObject<Poesia> p : mListPoesias.getList()) {
			if (!p.isLoaded()) break;
			count++;
		}
		return count;
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
							Compartilhamento comp = null;
							for (Compartilhamento compartilhamento : poesia.getCompartilhadoPor()) {
								if (compartilhamento.getPostador().equals(mUsuario)) {
									comp = compartilhamento;
								}
							}
							if (comp == null) {
								System.out.println("deletando poesia");
								mPoesiaController.delete(poesia.getId(), new OnRequestListener<String>(mContext) {
	
									@Override
									public void onSuccess(String result) {
										mListPoesias.remove(poesia.getId());
										notifyDataSetChanged();
									}
	
									@Override
									public void onError(String errorMessage) {
										ActivityUtils.showMessageDialog(mContext, "Um erro ocorreu", "Não foi possível excluir esta poesia. Tente novamente", null);
									}
	
									@Override
									public void onTimeout() {
										ActivityUtils.showMessageDialog(mContext, "Ops", "Ocorreu um erro com sua requisição. Verifique sua conexão com a internet.", null);
									}
								});
						} else {
							System.out.println("deletando compartilhado");
							mCompartilhamentoController.delete(comp.getId(), new OnRequestListener<String>(mContext) {

								@Override
								public void onSuccess(String result) {
									Compartilhamento comp = null;
									for (Compartilhamento c : poesia.getCompartilhadoPor()) {
										if (c.getPostador().equals(mUsuario)) {
											comp = c;
										}
									}
									poesia.getCompartilhadoPor().remove(comp);
									if (poesia.getCompartilhadoPor().isEmpty()) {
										mListPoesias.remove(poesia.getId());
									}
									notifyDataSetChanged();
								}

								@Override
								public void onError(String errorMessage) {
									ActivityUtils.showMessageDialog(mContext, "Um erro ocorreu", "Não foi possível excluir esta poesia. Tente novamente", null);
								}

								@Override
								public void onTimeout() {
									ActivityUtils.showMessageDialog(mContext, "Ops", "Ocorreu um erro com sua requisição. Verifique sua conexão com a internet.", null);
								}
							});
						}
							
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
				alertAutor.setText(poesia.getAutor().isEmpty() ? poesia.getPostador().getNome() : poesia.getAutor());
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
										alertTags.getText().toString(), new ObjectListID<Comentario>(), new ObjectListID<Curtida>());
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

									@Override
									public void onTimeout() {
										new AlertDialog.Builder(mContext)
										.setTitle("Ops")
										.setMessage("Ocorreu um erro com sua requisição. Verifique sua conexão com a internet.")
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
					PreloadedObject<Curtida> curtidaId = mUsuario.getCurtidas().getIntersecction(clicada.getCurtidas());
					if (curtidaId != null) {
						mCurtidaController.delete(curtidaId.getId(),
							new OnRequestListener<String>(mContext) {
								@Override
								public void onSuccess(String id) {
									mUsuario.getCurtidas().remove(id);
									clicada.getCurtidas().remove(id);
									clicada.setNumCurtidas(clicada.getNumCurtidas() - 1);
									((ImageView) v).setImageResource(R.drawable.like_icon);
									numLikes.setText(String.valueOf(clicada.getNumCurtidas()));
									v.setClickable(true);
								}
	
								@Override
								public void onError(String errorMessage) {
									System.out.println("ERROR descurtir: " + errorMessage);
									v.setClickable(true);
								}

								@Override
								public void onTimeout() {
									System.out.println("TIMEOUT descurtir!");
									v.setClickable(true);
								}
							});
					} else {
						mCurtidaController.post(mUsuario, poesia,
							new OnRequestListener<Curtida>(mContext) {
								@Override
								public void onSuccess(Curtida curtida) {
									mUsuario.getCurtidas().add(curtida.getId(), curtida.getDataCriacao().getTimeInMillis());
									clicada.getCurtidas().add(curtida.getId(), curtida.getDataCriacao().getTimeInMillis());
									clicada.setNumCurtidas(clicada.getNumCurtidas() + 1);
									((ImageView) v).setImageResource(R.drawable.like_icon_ativo);
									numLikes.setText(String.valueOf(clicada.getNumCurtidas()));
									v.setClickable(true);
								}

								@Override
								public void onError(String errorMessage) {
									System.out.println("ERROR curtir: " + errorMessage);
									v.setClickable(true);
								}

								@Override
								public void onTimeout() {
									System.out.println("TIMEOUT descurtir");
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
		final String headerTitle = (String) getGroup(groupPosition);
		return getView(groupPosition, convertView, parent, R.layout.list_group, new OnRequestListener<Pair<View, Poesia>>(mContext) {
			
			@Override
			public void onSuccess(final Pair<View, Poesia> result) {
				final View convertView = result.first;
				final Poesia poesia = result.second;
				final TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
				final TextView autor = (TextView) convertView.findViewById(R.id.author);
				autor.setText(poesia.getAutor());
				final TextView numLikes = (TextView) convertView.findViewById(R.id.num_likes);
				numLikes.setText(String.valueOf(poesia.getNumCurtidas()));
				final TextView numComments = (TextView) convertView.findViewById(R.id.num_comments);
				numComments.setText(String.valueOf(poesia.getNumComentarios()));
				final ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
				final ImageView edit = (ImageView) convertView.findViewById(R.id.edit);
				final TextView msgShare = (TextView) convertView.findViewById(R.id.msgShare);
				
				if (!poesia.getPostador().equals(mUsuario)) {
					edit.setVisibility(View.GONE);
				} else {
					edit.setVisibility(View.VISIBLE);
				}
				
				boolean compartilhado = false;
				for (Compartilhamento compartilhamento : poesia.getCompartilhadoPor()) {
					if (compartilhamento.getPostador().equals(mUsuario)) compartilhado = true;
				}
				if ((poesia.getPostador().equals(mUsuario)) || (compartilhado && mIsOnFeed)) {
					delete.setVisibility(View.VISIBLE);
				} else {
					delete.setVisibility(View.GONE);
				}
				
				if (poesia.getCompartilhadoPor().isEmpty() || !mIsOnFeed) {
					msgShare.setVisibility(View.GONE);
				} else {
					msgShare.setVisibility(View.VISIBLE);
					msgShare.setText("");
					boolean first = true;
					for (Compartilhamento compartilhamento : poesia.getCompartilhadoPor()) {
						if (!first) {
							if (compartilhamento.equals(poesia.getCompartilhadoPor().get(poesia.getCompartilhadoPor().size() - 1))) {
								msgShare.append(" e ");
							} else {
								msgShare.append(", ");
							}
						}
						first = false;
						final Usuario postador = compartilhamento.getPostador();
						SpannableString link = ClickableString.makeLinkSpan(compartilhamento.getPostador().equals(mUsuario) ? "Você" : 
							compartilhamento.getPostador().getNome(), new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(mContext, UserProfileActivity.class);
									intent.putExtra("usuario", postador);
									mContext.startActivity(intent);
								}
							});
						msgShare.append(link);
						msgShare.append(" ");
					}
					msgShare.append((poesia.getCompartilhadoPor().size() == 1 ? " compartilhou " : " compartilharam ") + ((poesia.getPostador().equals(mUsuario)) ? "sua" : "uma") + " poesia.");
					//ClickableString.makeLinksFocusable(msgShare);
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
			}

			@Override
			public void onTimeout() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		});
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
