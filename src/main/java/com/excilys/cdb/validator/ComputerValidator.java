package main.java.com.excilys.cdb.validator;

import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.exception.ValidatorDateException;
import main.java.com.excilys.cdb.exception.ValidatorException;
import main.java.com.excilys.cdb.exception.ValidatorIdException;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.model.Model;


public class ComputerValidator extends Validator {

    public ComputerDAO computerdao;

    public ComputerValidator(ComputerDAO computerdao) {
    	this.computerdao = computerdao;
    }
    
    public void setComputerDAO(ComputerDAO computerdao) {
 		this.computerdao = computerdao;
 	}
    
    @Override
    public boolean checkBeforeUpdate(Model c) throws ValidatorException {
        checkDates((Computer) c);
        checkId((Computer) c);
        return true;
    }

    @Override
    public boolean checkBeforeCreation(Model c) throws ValidatorDateException {
        checkDates((Computer) c);
        return true;
    }

    /**
     * Vérification que l'id n'existe pas.
     * @param comp Un objet Computer
     * @throws ValidatorIdException Pour les id deja existant
     */
    private void checkId(Computer comp) throws ValidatorIdException {
        if (comp.getCompanyId() != null && !computerdao.findById((long) comp.getId()).isPresent()) {
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
