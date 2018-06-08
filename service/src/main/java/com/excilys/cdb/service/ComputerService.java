package main.java.com.excilys.cdb.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.exception.DAOException;
import main.java.com.excilys.cdb.exception.ValidatorException;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;
import main.java.com.excilys.cdb.validator.ComputerValidator;

public class ComputerService {

	public ComputerDAO computerdao;
	public ComputerValidator computervalidator;

	public ComputerService(ComputerDAO computerdao, ComputerValidator computervalidator) {
		this.computerdao = computerdao;
		this.computervalidator = computervalidator;
	}

	public void setComputerDAO(ComputerDAO computerdao) {
		this.computerdao = computerdao;
	}

	public void setComputerValidator(ComputerValidator computervalidator) {
		this.computervalidator = computervalidator;
	}

	/**
	 * Retourne un ensemble d'element de computers.
	 * 
	 * @param cURRENT_INIT_ElEM
	 *            Determine à partir de quel element on stocke dans la liste.
	 * @param nB_ElEM
	 *            Nombre d'element à stocker.
	 * @return Une Page.
	 */
	public Page<Computer> getList(long offset, long nbElem) {
		System.out.println("ok " + computerdao);
		return computerdao.findAll(offset, nbElem);
	}

	/**
	 * Retourne un ensemble d'element de computers à partir d'une company.
	 * 
	 * @param offset
	 *            Determine à partir de quel element on stocke dans la liste.
	 * @param nbElem
	 *            Nombre d'element à stocker.
	 * @param id
	 *            Identifiant de la company.
	 * @return Une Page.
	 */
	public Page<Computer> getListComputerByCompany(int offset, int nbElem, long id) {
		return computerdao.findByCompanyId(offset, nbElem, id);
	}

	/**
	 * Retourne un Computer à partir d'un identifiant.
	 * 
	 * @param id
	 *            Identifiant du computer.
	 * @return Un computer.
	 */
	public Optional<Computer> get(Long id) {
		return computerdao.findById(id);
	}

	/**
	 * Ajoute un computer dans la base de données.
	 * 
	 * @param c
	 *            Computer à ajouter.
	 * @return True si ajout ok
	 */
	public boolean add(Computer c) {
		try {
			computervalidator.checkBeforeCreation(c);
			computerdao.create(c).get();
			return true;
		} catch (ValidatorException | DAOException e) {
			Logger logger = LoggerFactory.getLogger("ComputerService.addComputer");
			logger.info(e.getMessage());
			return false;
		}
	}

	/**
	 * Met à jour un computer en base de données.
	 * 
	 * @param c
	 *            Un computer.
	 * @return True si la mise à jour à lieu
	 */
	public boolean update(Computer c) {
		try {
			computervalidator.checkBeforeUpdate(c);
			computerdao.update(c);
			return true;
		} catch (ValidatorException | DAOException e) {
			Logger logger = LoggerFactory.getLogger("ComputerService.addComputer");
			logger.info(e.getMessage());
			return false;
		}
	}

	/**
	 * Supprime un computer.
	 * 
	 * @param computer
	 *            Un Computer.
	 */
	public void remove(Long id) {
		computerdao.delete(new Object[] { String.valueOf(id) });
	}

	/**
	 * Pour obtenir la company qui à crée le computer.
	 * 
	 * @param id
	 *            Identifiant du computer.
	 * @return Une company
	 */
	public Optional<Company> getCompany(Long id) {
		return computerdao.findCompanyLink(id);
	}

	/**
	 * Pour obtenir le nombre de computer dans la base données.
	 * 
	 * @return Un nombre de computer
	 */
	public Long getNumberComputer() {
		Optional<Long> res = computerdao.getCount();
		if (res.isPresent()) {
			return res.get();
		}

		return 0L;
	}

	/**
	 * Pour obtenir le nombre de computer dans la base données.
	 * 
	 * @param parameter
	 *            Une chaine de caractere.
	 * @return Un nombre de computer
	 */
	public Long getNumberComputerByName(String parameter) {
		Optional<Long> res = computerdao.getCountByName(parameter);
		if (res.isPresent()) {
			return res.get();
		}

		return 0L;
	}

	/**
	 * Récuperation des computers qui comporte dans leur nom la chaine parameter.
	 * 
	 * @param parameter
	 *            Une chaine de caractere.
	 * @param offset
	 *            Determine à partir de quel element on stocke dans la liste.
	 * @param nbElem
	 *            Nombre d'element à stocker.
	 * @return Un Page.
	 */
	public Page<Computer> getComputerByName(String parameter, int offset, int nbElem) {
		return computerdao.findComputerByName(offset, nbElem, parameter);
	}

	/**
	 * Récuperation des computers qui comporte dans leur nom la chaine parameter.
	 * 
	 * @param parameter
	 *            Une chaine de caractere.
	 * @param offset
	 *            Determine à partir de quel element on stocke dans la liste.
	 * @param nbElem
	 *            Nombre d'element à stocker.
	 * @return Un Page.
	 */
	public Page<Computer> getComputerByCompanyName(String parameter, int offset, int nbElem) {
		return computerdao.findComputerByCompany(offset, nbElem, parameter);
	}

	public void removeList(String[] split) {
		computerdao.delete(split);
	}

}
