package com.ubik.formation.entities;

import java.time.LocalDateTime;

public class Livre {
	
	private Long id;
	private Auteur auteur;
	private String titre;
	private LocalDateTime dateDeParution;
	
	public Livre() {
		
	}
	
	public Livre(Auteur auteur, String titre, LocalDateTime ldt) {
		this.auteur = auteur;
		this.titre = titre;
		this.dateDeParution = ldt;
	}
	
	public Livre(Long id, Auteur auteur, String titre, LocalDateTime ldt) {
		this.id = id;
		this.auteur = auteur;
		this.titre = titre;
		this.dateDeParution = ldt;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Auteur getAuteur() {
		return auteur;
	}
	
	public void setAuteur(Auteur auteur) {
		this.auteur = auteur;
	}
	
	public String getTitre() {
		return titre;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public LocalDateTime getDateDeParution() {
		return dateDeParution;
	}
	
	public void setDateDeParution(LocalDateTime dateDeParution) {
		this.dateDeParution = dateDeParution;
	}
	
	@Override
	public String toString() {
		return "Livre : " + titre + " écrit par " + auteur + " en " + dateDeParution.toString();
	}
	
}
