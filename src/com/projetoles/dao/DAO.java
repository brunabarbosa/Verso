package com.projetoles.dao;

import java.util.List;



/**
 * Interface gen�rica para um Data Access Object
 */
public abstract class DAO {

	public static final String DOMAIN  = "http://verso-projetoles.rhcloud.com";
	
	/**
	 * Retorna a string sem espa�os em branco em lugares que n�o s�o cercados por aspas
	 * Necess�rio para criar um objeto JSON funcional
	 * @param obj
	 * 		A string
	 * @return
	 * 		String sem espa�os em branco em lugares que n�o s�o cercados por aspas
	 */
	protected String getNormalizedString(Object obj) {
		String s = (String) obj;
		String regex = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";
		s = s.replaceAll(regex, "");
		return s;
	}

	protected String join(List<Long> ids) {
		String s = "";
		for (int i = 0; i < ids.size(); i++) {
			s += ids.get(i);
			if (i != ids.size() - 1) {
				s += ",";
			}
		}
		return s;
	} 
	
}
