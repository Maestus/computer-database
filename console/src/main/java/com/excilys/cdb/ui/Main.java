package main.java.com.excilys.cdb.ui;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.service.ComputerService;

public class Main {

    static Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	static CompanyService companyservice;
	static ComputerService computerservice;

	static ApplicationContext context;

	public void setCompanyService(CompanyService companyserv) {
		companyservice = companyserv;
	}

	public void setComputerService(ComputerService companyserv) {
		computerservice = companyserv;
	}

	public static void main(String[] args) {
		
		context = new ClassPathXmlApplicationContext("applicationConfig.xml");
		computerservice = (ComputerService) context.getBean("computerservice");
		companyservice = (CompanyService) context.getBean("companyservice");
		
		System.out.print("voulez-vous supprimer une compagnie ?");
		System.out.print("1 - Oui");
		System.out.print("2 - Non");

		Scanner sc = new Scanner(System.in);
		
		try {
    		int choix = Integer.parseInt(sc.nextLine());
    		if (choix == 1) {
    	        System.out.print("Quel company voulez-vous supprimer ? : ");
        		int idCompany = Integer.parseInt(sc.nextLine());
        		companyservice.remove(idCompany);
    		}
    	} catch (NumberFormatException e) {
    		LOGGER.debug("Probleme de conversion en entier.");
    	}
		
    	new UIController(companyservice, computerservice, new Interface(sc), new Scanner(System.in)).launchUI();
    	
    }
}
