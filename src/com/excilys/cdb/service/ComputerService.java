package com.excilys.cdb.service;

import com.excilys.cdb.dao.CompanyDAO;
import com.excilys.cdb.dao.ComputerDAO;
import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

public class ComputerService {
	
	private ComputerDAO computerDao;
	private CompanyDAO companyDao;

	
	public void init(DAOFactory dao) {
		this.computerDao = new ComputerDAO(dao);
		this.companyDao = new CompanyDAO(dao);
	}
	
	public Page<Computer> getListComputer(int offset, int nbElem) {
		return computerDao.findAll(offset, nbElem);
	}
	
	public Page<Computer> getListComputerByCompany(int offset, int nbElem, long id) {
		return computerDao.findByCompanyId(offset, nbElem, id);
	}

	public Computer getComputerById(Long id) {
		return (Computer) computerDao.findById(id);
	}
	
	public void addComputer(Computer c) {
		if(checkDate(c)) {
			if(c.getCompanyId() != null) {
				if(companyDao.findById((long) c.getCompanyId()) != null) {
					computerDao.create(c);
				} else {
					System.err.println("Campany inexistante");
				}
			} else {
				computerDao.create(c);
			}
		} else {
			System.err.println("Dates incohérentes");
		}
	}
	
	private boolean checkDate(Computer c) {
		return !(c.getDiscontinued() != null && c.getDiscontinued().isBefore(c.getIntroduced()));
	}

	public void updateComputer(Computer c) {
		if(checkDate(c)) {
			if(c.getCompanyId() != null) {
				if(companyDao.findById((long) c.getCompanyId()) != null) {
					computerDao.update(c);
				} else {
					System.err.println("Campany inexistante");
				}
			} else {
				computerDao.update(c);
			}
		} else {
			System.err.println("Dates incohérentes");
		}
	}

	public void removeComputer(Computer computer) {
		System.out.println(computer.getId());
		computerDao.delete(computer.getId());
	}
}
 