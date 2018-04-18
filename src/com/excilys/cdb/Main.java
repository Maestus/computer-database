package com.excilys.cdb;

import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.ui.Interface;

public class Main {

	public static void main(String [] args) {
		DAOFactory dao = DAOFactory.getInstance();
		ComputerService computerserv = new ComputerService();
		computerserv.init(dao);
		CompanyService companyserv = new CompanyService();
		companyserv.init(dao);
		Interface i = new Interface(computerserv, companyserv);
		i.menu();
	} 
}
