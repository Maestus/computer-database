package main.java.com.excilys.cdb.validator;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.exception.ValidatorDateException;
import main.java.com.excilys.cdb.exception.ValidatorException;
import main.java.com.excilys.cdb.model.Model;

public class CompanyValidator extends Validator {

    private CompanyDAO companyDao;

    /**
     * Constructeur.
     * @param companyDao Un accesseur à la table
     */
    public CompanyValidator(CompanyDAO companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public void checkBeforeUpdate(Model m) throws ValidatorException {
    }

    @Override
    public void checkBeforeCreation(Model m) throws ValidatorDateException {
    }
}
