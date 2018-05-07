package main.java.com.excilys.cdb.service;

import java.util.Optional;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.utils.Page;
import main.java.com.excilys.cdb.validator.CompanyValidator;
import main.java.com.excilys.cdb.validator.Validator;

public class CompanyService extends ModelService {

    public CompanyDAO companyDao;
    private Validator validate;

    /**
     * Initialise un CompanyService à partir d'une DAO.
     * @param dao Une Objet DAOFactory.
     */
    public void init(DAOFactory dao) {
        this.companyDao = new CompanyDAO();
        this.validate = new CompanyValidator(companyDao);
    }

    /**
     * Obtenir la liste des companies.
     * @param offset Determine à partir de quel element on commence à stocker.
     * @param nbElem Nombre d'element à stocker.
     * @return Une Page.
     */
    public Page<Company> getListCompany(int offset, int nbElem) {
        return companyDao.findAll(offset, nbElem);
    }

    /**
     * Obtenir une Company.
     * @param id Identifiant de la company.
     * @return Un objet Company.
     */
    public Optional<Company> getCompany(long id) {
        return companyDao.findById(id);
    }

}