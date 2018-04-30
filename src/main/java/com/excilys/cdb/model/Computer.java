package main.java.com.excilys.cdb.model;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Computer implements Model {

    private long id;
    private String nom;
    private LocalDate introduced;
    private LocalDate discontinued;
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

    public LocalDate getIntroduced() {
        return introduced;
    }

    /**
     * Modification du champs introduced.
     * @param introduced un objet LocalDate
     * @throws DateTimeException Peut retourner un erreur si la date n'est pas correct.
     */
    public void setIntroduced(LocalDate introduced) throws DateTimeException {
        this.introduced = introduced;
    }

    public LocalDate getDiscontinued() {
        return discontinued;
    }

    /**
     * Modification du champs discontinued.
     * @param discontinued un objet LocalDate
     * @throws DateTimeException Peut retourner un erreur si la date n'est pas correct.
     */
    public void setDiscontinued(LocalDate discontinued) throws DateTimeException {
        this.discontinued = discontinued;
    }

    public Object getCompanyId() {
        return company_id;
    }

    /**
     * Modification du cahmps company_id.
     * @param companyid identifiant de la company auquel est rattaché le computer.
     */
    public void setCompanyId(long companyid) {
        this.company_id = companyid;
    }

    @Override
    public String toString() {
        return "[nom = " + nom + ", introduced = " + introduced + ", discontinued = " + discontinued + ", company_id = "
                + company_id + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) ((long) company_id ^ ((long) company_id >>> 32));
        result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Computer other = (Computer) obj;
        if (company_id != other.company_id) {
            return false;
        }
        if (discontinued == null) {
            if (other.discontinued != null) {
                return false;
            }
        } else if (!discontinued.equals(other.discontinued)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (introduced == null) {
            if (other.introduced != null) {
                return false;
            }
        } else if (!introduced.equals(other.introduced)) {
            return false;
        }
        if (nom == null) {
            if (other.nom != null) {
                return false;
            }
        } else if (!nom.equals(other.nom)) {
            return false;
        }
        return true;
    }
}
