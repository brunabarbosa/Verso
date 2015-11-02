package com.projetoles.model;

import java.util.Comparator;

public class NumSeguidoresComparator implements Comparator<PreloadedObject<Usuario> > {

	@Override
	public int compare(PreloadedObject<Usuario> arg0, PreloadedObject<Usuario> arg1) {
		if (arg0.isLoaded() && arg1.isLoaded()) {
			return (int)(arg1.getPureLoadedObj().getNumSeguidores() - arg0.getPureLoadedObj().getNumSeguidores());
		} else if (!arg0.isLoaded() && !arg1.isLoaded()) {
			return arg0.compareTo(arg1);
		} else if (!arg0.isLoaded()) {
			return 1;
		} else {
			return -1;
		}
	}

}
