package main.java.com.excilys.cdb.validator;

import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.exception.ValidatorDateException;
import main.java.com.excilys.cdb.exception.ValidatorException;
import main.java.com.excilys.cdb.exception.ValidatorIdException;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.model.Model;

public class ComputerValidator extends Validator {

    ComputerDAO computerDao;

    /**
     * Constructeur.
     * @param computerDao Un accesseur à la table
     */
    public ComputerValidator(ComputerDAO computerDao) {
        this.computerDao = computerDao;
    }

    @Override
    public void checkBeforeUpdate(Model c) throws ValidatorException {
        checkDates((Computer) c);
        checkId((Computer) c);
    }

    @Override
    public void checkBeforeCreation(Model c) throws ValidatorDateException {
        checkDates((Computer) c);
    }

    /**
     * Vérification que l'id n'existe pas.
     * @param comp Un objet Computer
     * @throws ValidatorIdException Pour les id deja existant
     */
    private void checkId(Computer comp) throws ValidatorIdException {
        if (comp.getCompanyId() != null && !computerDao.findById((long) comp.getCompanyId()).isPresent()) {
            throw new ValidatorIdException("Computer inexistant");
        }
    }

    /**
     * Vérification des dates.
     * @param comp Un objet Computer
     * @throws ValidatorDateException Pour les dates incohérente
     */
    private void checkDates(Computer comp) throws ValidatorDateException {
        if (comp.getDiscontinued() != null && comp.getIntroduced() != null) {
            if (comp.getDiscontinued().isBefore(comp.getIntroduced())) {
                throw new ValidatorDateException("Date discontinued avant la date introduced");
            }
        }
    }
}
