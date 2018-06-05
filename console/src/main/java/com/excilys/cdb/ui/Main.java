package main.java.com.excilys.cdb.ui;

import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.service.ComputerService;

public class Main {

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
		
    	new UIController(companyservice, computerservice, new Interface(new Scanner(System.in)), new Scanner(System.in)).launchUI();
    	/*
        System.out.print("Quel company voulez-vous supprimer ? : ");
           
    	try (Scanner sc = new Scanner(System.in);) {
    		int idCompany = Integer.parseInt(sc.nextLine());
    		companyservice.remove(idCompany);
    	} catch (NumberFormatException e) {
            Logger logger = LoggerFactory.getLogger("Main");
            logger.debug("Probleme de conversion en entier.");
    	}*/
    }
}
