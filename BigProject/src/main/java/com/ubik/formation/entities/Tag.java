package com.ubik.formation.entities;

public class Tag {
	private Long id;
	private String libelle;
	
	public Tag() {
		
	}
	
	public Tag(Long id, String libelle) {
		this.id = id;
		this.libelle = libelle;
	}
	
	public Tag(String libelle) {
		this.libelle = libelle;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
