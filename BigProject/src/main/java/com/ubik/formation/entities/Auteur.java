package com.ubik.formation.entities;

public class Auteur {
	
	private Long id;
	private String nom;
	private String prenom;
	
	public Auteur() {
		
	}

	public Auteur(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
	}
	
	public Auteur(Long id, String nom, String prenom) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
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
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

}
