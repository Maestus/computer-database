package com.excilys.cdb.service;

import com.excilys.cdb.dao.CompanyDAO;
import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;

public class CompanyService {
	
	private CompanyDAO companyDao;
	
	public void init(DAOFactory dao) {
		this.companyDao = new CompanyDAO(dao);
	}
	
	public Page<Company> getListCompany(int offset, int nbElem) {
		return companyDao.findAll(offset, nbElem);
	}	
	
	public Company getCompany(long id) {
		return (Company) companyDao.findById(id);
	}
}