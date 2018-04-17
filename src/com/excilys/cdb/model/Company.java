package com.excilys.cdb.model;

public class Company implements Model {

	private long id;
	private String nom;

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	@Override
	public String toString() {
		return "Company [nom = " + nom + "]";
	}
	
}
