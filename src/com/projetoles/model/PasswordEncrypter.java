package com.projetoles.model;

import java.security.MessageDigest;

public class PasswordEncrypter {

	/**
	 * Converte uma array de bytes em uma string
	 * @param bArr
	 * 		Array de bytes que se deseja converter para string
	 * @return
	 * 		A string 
	 */
	public static String toHexString (byte[] bArr) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < bArr.length; i++) {
			int unsigned = bArr[i] & 0xff;
			if (unsigned < 0x10)
			  sb.append("0");
			sb.append(Integer.toHexString((unsigned)));
		}

		return sb.toString();
	}

	/**
	 * Retorna a string encriptada usando MD5
	 * @param input
	 * 		String que se deseja encriptar
	 * @return
	 * 		String encriptada
	 */
	public static String getEncryptedPassword(String input) {
		try {
			byte[] bytesOfMessage = input.getBytes(); // Maybe you're not using a charset here
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] theDigest = md5.digest(bytesOfMessage);
			return toHexString(theDigest).substring(0, 20);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}           
	}
	
}
