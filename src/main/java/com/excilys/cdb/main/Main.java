package main.java.com.excilys.cdb.main;

import java.util.Scanner;

import main.java.com.excilys.cdb.controller.UIController;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.service.ComputerService;
import main.java.com.excilys.cdb.ui.Interface;

public class Main {

    /**
     * @param args
     *            tab
     */
    public static void main(String[] args) {

        try (DAOFactory dao = DAOFactory.getInstance();) {
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
