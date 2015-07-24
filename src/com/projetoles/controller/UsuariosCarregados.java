package com.projetoles.controller;

import java.util.HashMap;
import java.util.Map;

import com.projetoles.model.Usuario;

public class UsuariosCarregados {

	private static Map<String, Usuario> sCarregados = new HashMap<String, Usuario>();
	
	public static void salvaUsuario(Usuario usuario) {
		sCarregados.put(usuario.getEmail(), usuario);
	}
	
	public static boolean temUsuario(String email) {
		return sCarregados.containsKey(email);
	}
	
	public static Usuario getUsuario(String email) {
		return sCarregados.get(email);
	}
	
}
