package com.example.enums;

public enum EnumCategoria {
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
