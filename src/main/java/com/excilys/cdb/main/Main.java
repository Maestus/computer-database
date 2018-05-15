package main.java.com.excilys.cdb.main;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.service.CompanyService;

public class Main {

    /**
     * @param args
     *            tab
     */
    public static void main(String[] args) {

        CompanyService companyserv = new CompanyService();
        companyserv.init();
        
        System.out.print("Quel company voulez-vous supprimer ? : ");
        
           
    	try (Scanner sc = new Scanner(System.in);) {
    		int idCompany = Integer.parseInt(sc.nextLine());
    		companyserv.removeCompany(idCompany);
    	} catch (NumberFormatException e) {
            Logger logger = LoggerFactory.getLogger("Main");
            logger.debug("Probleme de conversion en entier.");
    	}
    }
}
