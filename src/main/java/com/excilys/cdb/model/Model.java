package main.java.com.excilys.cdb.model;

public interface Model {

    /**
     * Obtenir l'identifiant du model.
     * @return identifiant (un Long).
     */
    Long getId();

    /**
     * Definir l'identifiant d'un model.
     * @param id un Long
     */
    void setId(long id);

    /**
     * Obtenir la valeur du champs nom d'un model.
     * @return une chaine de caractere.
     */
    String getNom();

    /**
     * Definir le nom d'un model.
     * @param nom une chaine de caractere.
     */
    void setNom(String nom);

}
