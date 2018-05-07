package main.java.com.excilys.cdb.exception;

public class ValidatorException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Création d'une exception.
     * @param message Message à envoyer
     */
    public ValidatorException(String message) {
        super(message);
    }

}
