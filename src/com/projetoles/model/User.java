package com.projetoles.model;

import android.annotation.SuppressLint;

/**
 * Classe responsável por guardar informações sobre o usuário
 */
public class User {

	public static final int MIN_LENGTH_LOGIN = 5;
	public static final int MAX_LENGTH_LOGIN = 20;
	private static final int MAX_LENGTH_NAME = 30;
	private static final int MIN_LENGTH_PASSWORD = 6;
	private static final int MAX_LENGTH_PASSWORD = 20;
	private static final int MAX_LENGTH_EMAIL = 30;
	private static final String EMAIL_REGEX = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$";

	private String mLogin;
	private String mEmail;
	private String mName;
	private String mPassword;

	public User(String login, String email, String name, String password ) throws IllegalArgumentException {
		setLogin(login);
		setEmail(email);
		setName(name);
		setPassword(password);
	}

	public String getLogin() {
		return mLogin;
	}

	@SuppressLint("DefaultLocale") 
	public void setLogin(String login) throws IllegalArgumentException {
		if (login == null || login.trim().equals("")) {
			throw new IllegalArgumentException("Login é obrigatório.");
		} else if (login.contains(" ")) {
			throw new IllegalArgumentException("Login não pode conter espaços em branco.");
		} else if (login.length() < MIN_LENGTH_LOGIN) {
			throw new IllegalArgumentException("Tamanho do login é menor que o permitido.");
		} else if (login.length() > MAX_LENGTH_LOGIN) {
			throw new IllegalArgumentException("Tamanho do login é maior que o permitido.");
		}
		this.mLogin = login.toLowerCase();
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) throws IllegalArgumentException {
		if (email.length() > MAX_LENGTH_EMAIL || !email.matches(EMAIL_REGEX)) {
			throw new IllegalArgumentException("E-mail inválido.");
		}
		this.mEmail = email;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) throws IllegalArgumentException {
		if (name != null && name.length() > MAX_LENGTH_NAME) {
			throw new IllegalArgumentException("Tamanho do nome é maior que o permitido.");
		}
		this.mName = name;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) throws IllegalArgumentException {
		if (password == null || password.trim().equals("")) {
			throw new IllegalArgumentException("Senha é obrigatória.");
		} else if (password.length() < MIN_LENGTH_PASSWORD) {
			throw new IllegalArgumentException("Tamanho da senha é menor que o permitido.");
		} else if (password.length() > MAX_LENGTH_PASSWORD) {
			throw new IllegalArgumentException("Tamanho da senha é maior que o permitido.");
		}
		this.mPassword = password;
	}



	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return other.getLogin().equals(this.getLogin());
	}

	@Override
	public String toString() {
		if (mName != null && !mName.trim().equals("")) {
			return mName.split(" ")[0];
		}
		return mEmail;
	}
	
}
