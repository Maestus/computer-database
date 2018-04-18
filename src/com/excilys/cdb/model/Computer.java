package com.excilys.cdb.model;

import java.sql.Timestamp;
import java.time.DateTimeException;

public class Computer implements Model {

	private long id;
	private String nom;
	private Timestamp introduced;
	private Timestamp discontinued;
	private Object company_id;
	
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}

	public Timestamp getIntroduced() {
		return introduced;
	}

	public void setIntroduced(Timestamp introduced) throws DateTimeException {
		if(introduced != null) {
			if(discontinued != null && discontinued.before(introduced)) {
				throw new DateTimeException("Date 'introduced' situé après la date 'discontinued'.");
			}
			this.introduced = introduced;
		}
	}
	
	public Timestamp getDiscontinued() {
		return discontinued;
	}
	
	public void setDiscontinued(Timestamp discontinued) throws DateTimeException {
		if(discontinued != null) {
			if(introduced != null && introduced.after(discontinued)) {
				throw new DateTimeException("Date 'discontinued' situé avant la date 'introduced'.");
			}
			this.discontinued = discontinued;
		}
	}

	public Object getCompanyId() {
		return company_id;
	}

	public void setCompanyId(long company_id) {
		this.company_id = company_id;
	}

	@Override
	public String toString() {
		return "[nom = " + nom + ", introduced = " + introduced + ", discontinued = " + discontinued + ", company_id = " + company_id + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + (int) (company_id ^ (company_id >>> 32));
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (company_id != other.company_id)
			return false;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id != other.id)
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}
}
