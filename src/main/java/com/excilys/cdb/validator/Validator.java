package main.java.com.excilys.cdb.validator;

import main.java.com.excilys.cdb.exception.ValidatorDateException;
import main.java.com.excilys.cdb.exception.ValidatorException;
import main.java.com.excilys.cdb.model.Model;

public abstract class Validator {

    /**
     * Verifie que tout les champs d'un objet sont comformes pour une mise à jour.
     * @param m L'objet à tester
     * @throws ValidatorException Retourne une exception si l'objet n'est pas valide
     */
    public abstract void checkBeforeUpdate(Model m) throws ValidatorException;

    /**
     * Verifie que tout les champs d'un objet sont comformes pour une creation.
     * @param m L'objet à tester
     * @throws ValidatorDateException Retourne une exception si l'objet n'est pas valide
     */
    public abstract void checkBeforeCreation(Model m) throws ValidatorDateException;

}
