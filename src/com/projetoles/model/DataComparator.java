package com.projetoles.model;

import java.util.Comparator;

public class DataComparator<T extends TemporalModel> implements Comparator<PreloadedObject<T>> {

	@Override
	public int compare(PreloadedObject<T> lhs, PreloadedObject<T> rhs) {
		return lhs.compareTo(rhs);
	}

}
