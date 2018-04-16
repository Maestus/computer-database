package com.excilys.cdb.model;

import java.sql.Timestamp;
import java.time.DateTimeException;

public class Computer implements Model {

	private long id;
	private String nom;
	private Timestamp introduced;
	private Timestamp discontinued;
	private long company_id;
	
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

	public void setIntroduced(Timestamp introduced) throws DateTimeException {
		if(introduced != null) {
			if(discontinued != null && discontinued.before(introduced)) {
				throw new DateTimeException("Date 'introduced' situé après la date 'discontinued'.");
			}
			this.introduced = introduced;
		}
	}

	public Timestamp getIntroduced() {
		return introduced;
	}

	public void setDiscontinued(Timestamp discontinued) throws DateTimeException {
		if(discontinued != null) {
			if(introduced != null && introduced.after(discontinued)) {
				throw new DateTimeException("Date 'discontinued' situé avant la date 'introduced'.");
			}
			this.discontinued = discontinued;
		}
	}

	public Timestamp getDiscontinued() {
		return discontinued;
	}
	
	public Long getCompanyId() {
		return company_id;
	}

	public void setCompanyId(long company_id) {
		this.company_id = company_id;
	}

}
