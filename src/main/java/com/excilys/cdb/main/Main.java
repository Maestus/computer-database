package main.java.com.excilys.cdb.main;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.service.CompanyService;

public class Main {

	static CompanyService companyservice;
  
	public void setCompanyService(CompanyService companyservice) {
		Main.companyservice = companyservice;
	}
	
    public static void main(String[] args) {
        
        System.out.print("Quel company voulez-vous supprimer ? : ");
           
    	try (Scanner sc = new Scanner(System.in);) {
    		int idCompany = Integer.parseInt(sc.nextLine());
    		companyservice.remove(idCompany);
    	} catch (NumberFormatException e) {
            Logger logger = LoggerFactory.getLogger("Main");
            logger.debug("Probleme de conversion en entier.");
    	}
    }
}
