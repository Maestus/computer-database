package com.excilys.cdb.model;

import java.sql.Timestamp;

public class Computer implements Model {

	private long id;
	private String nom;
	private Timestamp introduced;
	private Timestamp discontinued;

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

	public void setIntroduced(Timestamp introduced) {
		this.introduced = introduced;
	}

	public Timestamp getIntroduced() {
		return introduced;
	}

	public void setDiscontinued(Timestamp discontinued) {
		this.discontinued = discontinued;
	}

	public Timestamp getDiscontinued() {
		return discontinued;
	}

}