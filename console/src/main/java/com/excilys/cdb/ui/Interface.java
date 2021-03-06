package main.java.com.excilys.cdb.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

public class Interface {

    static final int UI_SIZE = 100;
    static final String UI_MESSAGE_HEADER = "LISTS OF COMPANIES & COMPUTERS";
    static Logger LOGGER = LoggerFactory.getLogger(Interface.class);

    public Map<String, List<List<String>>> menu;
    public Place emplacementMenu;
    Scanner sc;
    final DateTimeFormatter frenchPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Initialisation d'une interface.
     * @param sc Un Scanner.
     */
    public Interface(Scanner sc) {
        this.sc = sc;
        this.emplacementMenu = Place.MENU_PRINCIPAL;
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        TypeReference<HashMap<String, List<List<String>>>> typeRef = new TypeReference<HashMap<String, List<List<String>>>>() {
        };
        try {
            menu = mapper.readValue(classLoader.getResourceAsStream("main/java/com/excilys/cdb/ui/menu.json"), typeRef);
        } catch (IOException e) {
        	LOGGER.debug("Fichier non trouvé");
        }
    }

    /**
     * Affichage du menu.
     */
    public void menu() {
        Interface.displayHeader();

        for (List<String> s : menu.get(emplacementMenu.toString())) {
            System.out.println(s.get(0) + " - " + s.get(1));
        }

        Interface.displayFooter();

    }

    /**
     * Affichage de la liste de computers.
     * @param lcomputer Une liste de computers.
     */
    public void displayComputerList(Page<Computer> lcomputer) {
        int i = 1;
        for (Computer c : lcomputer.elems) {
            System.out.println(i++ + " " + c.getName());
        }
        emplacementMenu = Place.MENU_COMPUTER;
    }

    /**
     * Affichage d'une liste de companies.
     * @param lcompany Une liste de companies.
     */
    public void displayCompanyList(Page<Company> lcompany) {
        int i = 1;
        for (Company c : lcompany.elems) {
            System.out.println(i++ + " " + c);
        }
        emplacementMenu = Place.MENU_COMPANY;
    }

    /**
     * Création/édition d'un computer.
     * @param computer Un computer.
     * @return Un objet Computer.
     */
    public Computer editOrCreateComputer(Computer computer) {

        boolean getNomDone = false, getIntroDone = false, getDiscDone = false, getCompanyId = false;
        LocalDate ti = null, td = null;

        while (!getNomDone) {
            System.out.print("Nom : ");
            String newNom = sc.nextLine();
            if (newNom.equals("")) {
                if (computer.getName() == null) {
                	LOGGER.info("Pour la création un nom est necessaire");
                } else {
                    computer.setName(computer.getName());
                    getNomDone = true;
                }
            } else {
                computer.setName(newNom);
                getNomDone = true;
            }
        }

        while (!getIntroDone) {
            try {
                System.out.print("Introduced : ");
                String newIntroduced = sc.nextLine();
                if (newIntroduced.equals("null")) {
                    computer.setIntroduced(null);
                } else if (newIntroduced.equals("")) {
                    computer.setIntroduced(computer.getIntroduced());
                } else {
                    ti = LocalDate.parse(newIntroduced, frenchPattern);
                    computer.setIntroduced(ti);
                }
                getIntroDone = true;
            } catch (DateTimeParseException e) {
            	LOGGER.info("Mauvais format, entrez une date de la forme dd/mm/yyyy");
            }
        }

        while (!getDiscDone) {
            try {
                System.out.print("Discountinued : ");
                String newDiscontinued = sc.nextLine();
                if (newDiscontinued.equals("null")) {
                    computer.setDiscontinued(null);
                } else if (newDiscontinued.equals("")) {
                    computer.setDiscontinued(computer.getDiscontinued());
                } else {
                    td = LocalDate.parse(newDiscontinued, frenchPattern);
                    if (!td.isBefore(ti)) {
                        computer.setDiscontinued(td);
                        getDiscDone = true;
                    } else {
                        System.err.println("Date non correcte");
                    }
                }
            } catch (DateTimeParseException e) {
            	LOGGER.info("Mauvais format, entrez une date de la forme dd/mm/yyyy");
            }
        }

        while (!getCompanyId) {
            try {
                System.out.print("Company ID : ");
                String campanyID = sc.nextLine();
                if (!campanyID.equals("null")) {
                    computer.setCompanyId(Long.parseLong(campanyID));
                }
                getCompanyId = true;
            } catch (IllegalArgumentException e) {
                LOGGER.info("Mauvais format, entrez une date de la forme yyyy-mm-dd hh:mm:ss");
            }
        }
        return computer;
    }

    /**
     * Affiche l'entete de menu.
     */
    private static void displayHeader() {

        for (int i = 0; i < UI_SIZE; i++) {
            System.out.print("-");
        }

        System.out.println();

        for (int i = 0; i < ((UI_SIZE / 2) - UI_MESSAGE_HEADER.length() / 2); i++) {
            System.out.print(" ");
        }

        System.out.println(UI_MESSAGE_HEADER);

        for (int i = 0; i < UI_SIZE; i++) {
            System.out.print("-");
        }
        System.out.println();

    }

    /**
     * Affiche le footer du menu.
     */
    private static void displayFooter() {
        for (int i = 0; i < UI_SIZE; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
