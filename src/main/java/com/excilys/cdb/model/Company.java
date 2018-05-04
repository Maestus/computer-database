package main.java.com.excilys.cdb.model;

public class Company implements Model {

    private long id;
    private String nom;

    public Long getId() {
        return id;
    }

    @Override
    public Company setId(long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public Company setNom(String nom) {
        this.nom = nom;
        return this;
    }

    @Override
    public String toString() {
        return nom;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
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
        Company other = (Company) obj;
        if (id != other.id) {
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
