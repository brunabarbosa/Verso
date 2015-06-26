package com.projetoles.model;

public enum Categoria {
	
	JUVENIL, INFANTIL;
	
	@Override
	public String toString() {
		switch (this) {
		case JUVENIL:
			return "Juvenil";
		default:
			return "Infantil";
		}
	}
	
}
