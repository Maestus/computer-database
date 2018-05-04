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

    @Override
    public Computer setId(long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public Computer setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public LocalDate getIntroduced() {
        return introduced;
    }

    /**
     * Modification du champs introduced.
     * @param introduced un objet LocalDate
     * @throws DateTimeException Peut retourner un erreur si la date n'est pas correct.
     * @return L'objet modifié
     */
    public Computer setIntroduced(LocalDate introduced) throws DateTimeException {
        this.introduced = introduced;
        return this;
    }

    public LocalDate getDiscontinued() {
        return discontinued;
    }

    /**
     * Modification du champs discontinued.
     * @param discontinued un objet LocalDate
     * @throws DateTimeException Peut retourner un erreur si la date n'est pas correct.
     * @return L'objet modifié
     */
    public Computer setDiscontinued(LocalDate discontinued) throws DateTimeException {
        this.discontinued = discontinued;
        return this;
    }

    public Object getCompanyId() {
        return company_id;
    }

    /**
     * Modification du cahmps company_id.
     * @param companyid identifiant de la company auquel est rattaché le computer.
     * @return L'objet modifié
     */
    public Computer setCompanyId(long companyid) {
        this.company_id = companyid;
        return this;
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
