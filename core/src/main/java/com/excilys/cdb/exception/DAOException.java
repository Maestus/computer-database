package main.java.com.excilys.cdb.exception;

public class DAOException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Création d'une exception.
     * @param message Message à envoyer
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Création d'une exception.
     * @param message Message à envoyer
     * @param e information sur l'erreur
     */
    public DAOException(String message, Throwable e) {
        super(message, e);
    }

}
