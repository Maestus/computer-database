package main.java.com.excilys.cdb.service;

import java.util.Optional;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.utils.Page;

public class CompanyService {

	public CompanyDAO companydao;

	public CompanyService(CompanyDAO companydao) {
		this.companydao = companydao;
	}

	public void setComputerDAO(CompanyDAO companydao) {
		this.companydao = companydao;
	}

	/**
	 * Obtenir la liste des companies.
	 * 
	 * @param offset
	 *            Determine à partir de quel element on commence à stocker.
	 * @param nbElem
	 *            Nombre d'element à stocker.
	 * @return Une Page.
	 */
	public Page<Company> getList(int offset, int nbElem) {
		return companydao.findAll(offset, nbElem);
	}

	/**
	 * Obtenir une Company.
	 * 
	 * @param id
	 *            Identifiant de la company.
	 * @return Un objet Company.
	 */
	public Optional<Company> get(long id) {
		return companydao.findById(id);
	}

	/**
	 * Obtenir une Company.
	 * 
	 * @param id
	 *            Identifiant de la company.
	 */
	public void remove(long id) {
		companydao.delete(id);
	}

	/**
	 * Pour obtenir le nombre de computer dans la base données.
	 * 
	 * @param parameter
	 *            Une chaine de caractere.
	 * @return Un nombre de computer
	 */
	public long getNumberComputerByCompanyName(String parameter) {
		Optional<Long> res = companydao.getCountByCompanyName(parameter);
		if (res.isPresent()) {
			return res.get();
		}

		return 0L;
	}

}