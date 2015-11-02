package com.projetoles.model;

import java.util.Comparator;

public class NumComentariosComparator implements Comparator<PreloadedObject<Poesia> > {

	@Override
	public int compare(PreloadedObject<Poesia> arg0, PreloadedObject<Poesia> arg1) {
		if (arg0.isLoaded() && arg1.isLoaded()) {
			return (int)(arg1.getPureLoadedObj().getNumComentarios() - arg0.getPureLoadedObj().getNumComentarios());
		} else if (!arg0.isLoaded() && !arg1.isLoaded()) {
			return arg0.compareTo(arg1);
		} else if (!arg0.isLoaded()) {
			return 1;
		} else {
			return -1;
		}
	}

}
