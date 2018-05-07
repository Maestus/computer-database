package main.java.com.excilys.cdb.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.exception.DAOException;
import main.java.com.excilys.cdb.exception.ValidatorException;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;
import main.java.com.excilys.cdb.validator.ComputerValidator;
import main.java.com.excilys.cdb.validator.Validator;

public class ComputerService extends ModelService {

    public ComputerDAO computerDao;
    public CompanyDAO companyDao;
    private Validator validate;

    /**
     * Initialiastion du ComputerService.
     * @param dao un objet DAOFactory.
     */
    public void init(DAOFactory dao) {
        this.computerDao = new ComputerDAO();
        this.companyDao = new CompanyDAO();
        this.validate = new ComputerValidator(computerDao);
    }

    /**
     * Retourne un ensemble d'element de computers.
     * @param offset Determine à partir de quel element on stocke dans la liste.
     * @param nbElem Nombre d'element à stocker.
     * @return Une Page.
     */
    public Page<Computer> getListComputer(int offset, int nbElem) {
        return computerDao.findAll(offset, nbElem);
    }

    /**
     * Retourne un ensemble d'element de computers à partir d'une company.
     * @param offset Determine à partir de quel element on stocke dans la liste.
     * @param nbElem Nombre d'element à stocker.
     * @param id Identifiant de la company.
     * @return Une Page.
     */
    public Page<Computer> getListComputerByCompany(int offset, int nbElem, long id) {
        return computerDao.findByCompanyId(offset, nbElem, id);
    }

    /**
     * Retourne un Computer à partir d'un identifiant.
     * @param id Identifiant du computer.
     * @return Un computer.
     */
    public Optional<Computer> getComputerById(Long id) {
        return computerDao.findById(id);
    }

    /**
     * Ajoute un computer dans la base de données.
     * @param c Computer à ajouter.
     * @return True si ajout ok
     */
    public boolean addComputer(Computer c) {
        try {
            validate.checkBeforeCreation(c);
            computerDao.create(c).get();
            return true;
        } catch (ValidatorException | DAOException e) {
            Logger logger = LoggerFactory.getLogger("ComputerService.addComputer");
            logger.info(e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour un computer en base de données.
     * @param c Un computer.
     * @return True si la mise à jour à lieu
     */
    public boolean updateComputer(Computer c) {
        try {
            validate.checkBeforeUpdate(c);
            computerDao.update(c);
            return true;
        } catch (ValidatorException | DAOException e) {
            Logger logger = LoggerFactory.getLogger("ComputerService.addComputer");
            logger.info(e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un computer.
     * @param computer Un Computer.
     */
    public void removeComputer(Computer computer) {
        computerDao.delete(computer.getId());
    }

    /**
     * Pour obtenir la company qui à crée le computer.
     * @param id Identifiant du computer.
     * @return Une company
     */
    public Optional<Company> getCompany(Long id) {
        return computerDao.findCompanyLink(id);
    }

    /**
     * Pour obtenir le nombre de computer dans la base données.
     * @return Un nombre de computer
     */
    public Long getNumberComputer() {
        return computerDao.getCount();
    }

    /**
     * Récuperation des computers qui comporte dans leur nom la chaine parameter.
     * @param parameter Une chaine de caractere.
     * @return Un Page.
     */
    public Page<Computer> getComputerByName(String parameter) {
        return computerDao.findComputerByName(0, Page.NO_LIMIT, parameter);
    }

}
