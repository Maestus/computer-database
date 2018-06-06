package main.java.com.excilys.cdb.ui;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.service.ComputerService;
import main.java.com.excilys.cdb.utils.Page;

public class UIController {
    Scanner sc;
    private String choix;

    static Logger LOGGER = LoggerFactory.getLogger(UIController.class);
    CompanyService companyservice;
    ComputerService computerservice;
    Interface ui;

    Page<Company> lcompany;
    Page<Computer> lcomputer;
    Computer computer;

    public int pageSize = 10;
    public int offset = 0;
    private boolean quit;

    /**
     * @param ui
     *            Interface utilisateur
     * @param computerserv
     *            Service fesant appel à la DAO Computer
     * @param companyservice
     *            Service fesant appel à la DAO Company
     * @param sc
     *            Scanner
     */
    public UIController(CompanyService companyservice, ComputerService computerservice, Interface ui, Scanner sc) {
        computer = new Computer();
        this.sc = sc;
        this.ui = ui;
		this.companyservice = companyservice;
		this.computerservice = computerservice;
    }


    /**
     * Démarre l'interaction utilisateur.
     */
    public void launchUI() {
        ui.menu();
        waitChoice();
        displayRes();
    }

    /**
     * Attends input utilisateur.
     */
    private void waitChoice() {
        boolean goodChoice = false;
        while (!goodChoice) {
            try {
                System.out.print("Votre choix : ");
                choix = sc.nextLine();
                if (choix.equals("quit")) {
                    quit = true;
                    return;
                }
                goodChoice = true;
            } catch (Exception e) {
                LOGGER.debug("Mauvaise entrée", e);
            }
        }
    }

    /**
     * Gestion de l'affichage centralisé.
     */
    public void displayRes() {
        if (!quit) {
            try {
            	System.out.println(ui.emplacementMenu + " " + ui.menu.get(ui.emplacementMenu.toString()).size() + " " +Integer.parseInt(choix));
                if (ui.emplacementMenu == Place.MENU_SETTING) {
                    setting();
                } else if (ui.emplacementMenu == Place.MENU_PRINCIPAL) {
                    if (ui.menu.get(ui.emplacementMenu.toString()).size() >= Integer.parseInt(choix) && Integer.parseInt(choix) > 0) {
                        System.out.println("OK la");
                    	displayList();
                    }
                } else if (ui.emplacementMenu == Place.MENU_COMPANY) {
                    displayComputerListByCompanyId();
                } else if (ui.emplacementMenu == Place.MENU_COMPUTER) {
                    displayComputerDetail();
                } else {
                    controlComputer();
                }
            } catch (Exception e) {
                LOGGER.debug("Mauvaise entrée " + ui.emplacementMenu);
            }
            launchUI();
        }
    }

    /**
     * Obtenir la liste des computers.
     */
    public void setPageComputer() {
    	System.out.println("bon : " + companyservice);
        lcomputer = computerservice.getListComputer(offset, pageSize);
        if (lcomputer.elems.size() == 0) {
            offset -= pageSize;
        }
        lcomputer = computerservice.getListComputer(offset, pageSize);
    }

    /**
     * Obtenir la liste des companies.
     */
    public void setPageCompany() {
        lcompany = companyservice.getList(offset, pageSize);
        if (lcompany.elems.size() == 0) {
            offset -= pageSize;
        }
        lcompany = companyservice.getList(offset, pageSize);
    }

    /**
     * Montre la liste (company/computer).
     */
    private void displayList() {
        try {
            if (!checkIfGoBack()) {
                if (ui.menu.get(ui.emplacementMenu.toString()).get(Integer.parseInt(choix) - 1).get(2)
                        .equals(CompanyService.class.getSimpleName())) {
                    offset = 0;
                    setPageCompany();
                    ui.displayCompanyList(lcompany);
                } else if (ui.menu.get(ui.emplacementMenu.toString()).get(Integer.parseInt(choix) - 1).get(2)
                        .equals(ComputerService.class.getSimpleName())) {
                	offset = 0;
                    setPageComputer();
                    System.out.println("blablou");
                    ui.displayComputerList(lcomputer);
                } else if (ui.menu.get(ui.emplacementMenu.toString()).get(Integer.parseInt(choix) - 1).get(2)
                        .equals("Setting")) {
                    setting();
                    ui.emplacementMenu = Place.MENU_PRINCIPAL;
                }
            }
        } catch (NumberFormatException e) {
            LOGGER.debug("Mauvais type d'entrée, entrer un entier.");
        }
    }

    /**
     * Montre les computers en fonction d'une company.
     */
    private void displayComputerListByCompanyId() {
        try {
            if (!checkIfGoBack()) {
                if (choix.equals("s")) {
                    offset += pageSize;
                    setPageCompany();
                    ui.displayCompanyList(lcompany);
                } else if (choix.equals("p")) {
                    offset -= pageSize;
                    if (offset < 0) {
                        offset = 0;
                    }
                    setPageCompany();
                    ui.displayCompanyList(lcompany);
                } else {
                    int i = 1;
                    lcomputer = computerservice.getListComputerByCompany(offset, pageSize, Integer.parseInt(choix));
                    for (Computer c : lcomputer.elems) {
                        System.out.println(i++ + " " + c);
                    }
                }
            }
        } catch (NumberFormatException e) {
            LOGGER.debug("Mauvais type d'entrée, entrer un entier.");
        }
    }

    /**
     * @return boolean
     */
    private boolean checkIfGoBack() {
        if (choix.equals("q")) {
            List<String> l = ui.menu.get(ui.emplacementMenu.toString())
                    .get(ui.menu.get(ui.emplacementMenu.toString()).size() - 1);
            if (Place.MENU_COMPUTER.toString().equals(l.get(l.size() - 1))) {
                ui.emplacementMenu = Place.MENU_COMPUTER;
            } else {
                ui.emplacementMenu = Place.MENU_PRINCIPAL;
            }
            return true;
        }
        return false;
    }

    /**
     * Configurer le nombre de ligne par page d'affichage.
     */
    public void setting() {

        boolean goodValue = false;
        while (!goodValue) {
            try {
                System.out.print("Page size [" + pageSize + "] : ");
                pageSize = Integer.parseInt(sc.nextLine());
                goodValue = true;

            } catch (NumberFormatException e) {
                LOGGER.debug("Mauvais type d'entrée, entrer un entier.");
            }
        }
    }

    /**
     * Montre un computer en détail.
     */
    private void displayComputerDetail() {
        if (!checkIfGoBack()) {
            if (choix.equals("c")) {
                computer = new Computer();
                ui.editOrCreateComputer(computer);
                computerservice.addComputer(computer);
            } else if (choix.equals("s")) {
                offset += pageSize;
                setPageComputer();
                ui.displayComputerList(lcomputer);
            } else if (choix.equals("p")) {
                offset -= pageSize;
                if (offset < 0) {
                    offset = 0;
                }
                setPageComputer();
                ui.displayComputerList(lcomputer);
            } else {
                try {
                    if ((Integer.parseInt(choix) - 1) < lcomputer.elems.size()) {
                        computer = computerservice
                                .getComputerById(lcomputer.elems.get(Integer.parseInt(choix) - 1).getId()).get();
                        System.out.println(computer);
                        ui.emplacementMenu = Place.MENU_COMPUTER_DETAIL;
                    }
                } catch (NumberFormatException e) {
                    LOGGER.debug("Mauvais type d'entrée, entrer un entier.");
                }
            }
        }
    }

    /**
     * Appel le Service Computer pour ajouter / modifier un objet du metier.
     */
    public void controlComputer() {
        try {
            if (!checkIfGoBack()) {
                if (Integer.parseInt(choix) == 1) {
                    computer = ui.editOrCreateComputer(computer);
                    computerservice.updateComputer(computer);
                } else if (Integer.parseInt(choix) == 2) {
                    computerservice.remove(computer);
                    ui.emplacementMenu = Place.MENU_COMPUTER;
                    displayList();
                }
            }
        } catch (NumberFormatException e) {
            LOGGER.debug("Mauvais type d'entrée, entrer un entier.");
        }
    }

}
