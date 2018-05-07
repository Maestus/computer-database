package main.java.com.excilys.cdb.exception;

public class ValidatorDateException extends ValidatorException {

    private static final long serialVersionUID = 1L;

    /**
     * Création d'une exception.
     * @param message Message à envoyer
     */
    public ValidatorDateException(String message) {
        super(message);
    }

}
