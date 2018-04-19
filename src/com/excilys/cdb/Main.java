package com.excilys.cdb;

import java.util.Scanner;

import com.excilys.cdb.controller.UIController;
import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.ui.Interface;

public class Main {

	public static void main(String [] args) {

		try(DAOFactory dao = DAOFactory.getInstance();) {
			dao.setConnection();
			ComputerService computerserv = new ComputerService();
			computerserv.init(dao);
			CompanyService companyserv = new CompanyService();
			companyserv.init(dao);
			Scanner sc = new Scanner(System.in);
			Interface iu = new Interface(sc);
			UIController controller = new UIController(iu, computerserv, companyserv, sc);
			controller.launchUI();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
}
