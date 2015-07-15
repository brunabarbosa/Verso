package com.projetoles.dao;


public class CurtidaDAO extends DAO {

	private static CurtidaDAO sInstance;
	
	public static CurtidaDAO getInstance() {
		if (sInstance == null) {
			sInstance = new CurtidaDAO();
		}
		return sInstance;
	}

}
