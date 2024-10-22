package com.ubik.formation.entities;

import org.mindrot.jbcrypt.BCrypt;

public class Utilisateur {
	
	private Long id;
	private String nom;
	private String prenom;
	private String login;
	private String password;
	// En dur pour l'exercice, j'ai conscience que ce n'est pas une bonne pratique, 
	// Benoit m'a dit de ne pas me casser la tête pour ça
	public final static String SALT = "$2a$10$9IilKTwP44k5e.UvNefBNO";
	
	public Utilisateur() {
		
	}
	
	
	/**
	 * @param id
	 * @param nom
	 * @param prenom
	 * @param login
	 * @param password, it will be hashed
	 */
	public Utilisateur(String nom, String prenom, String login, String password) {
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.password = BCrypt.hashpw(password, SALT);
	}
	/**
	 * @param id
	 * @param nom
	 * @param prenom
	 * @param login
	 * @param password, it will be hashed
	 */
	public Utilisateur(Long id, String nom, String prenom, String login, String password) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.password = BCrypt.hashpw(password, SALT);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, SALT);
	}
}
