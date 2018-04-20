package main.java.com.excilys.cdb.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

public class Interface {

	final static int UI_SIZE = 100;
	final static String UI_MESSAGE_HEADER = "LISTS OF COMPANIES & COMPUTERS";
	
	public Map<String, List<List<String>>> menu;
	public Place emplacementMenu;
	Scanner sc;
	final DateTimeFormatter frenchPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");			
	
	public Interface(Scanner sc) {
		this.sc = sc;
		this.emplacementMenu = Place.MENU_PRINCIPAL;
		ObjectMapper mapper = new ObjectMapper();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		TypeReference<HashMap<String, List<List<String>>>> typeRef = 
				new TypeReference<HashMap<String, List<List<String>>>>() {};
		try {
			menu = mapper.readValue(classLoader.getResourceAsStream("main/java/com/excilys/cdb/ui/menu.json"), typeRef);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void menu() {
		Interface.displayHeader();
		
		for (List<String> s : menu.get(emplacementMenu.toString())) {
			System.out.println(s.get(0) + " - " + s.get(1));
		}

		Interface.displayFooter();

	}

	public void displayComputerList(Page<Computer> lcomputer) {
		int i = 1;
		for (Computer c : lcomputer.elems) {
			System.out.println(i++ + " " + c.getNom());
		}
		emplacementMenu = Place.MENU_COMPUTER;		
	}

	public void displayCompanyList(Page<Company> lcompany) {
		int i = 1;
		for (Company c : lcompany.elems) {
			System.out.println(i++ + " " + c);
		}
		emplacementMenu = Place.MENU_COMPANY;		
	}

	public Computer editOrCreateComputer(Computer computer) {
		
		boolean getNomDone = false, getIntroDone = false, getDiscDone = false, getCompanyId = false;
		LocalDate ti = null, td = null;
		
		while(!getNomDone) {
			System.out.print("Nom : ");
			String newNom = sc.nextLine();
			if (newNom.equals("")) {
				if(computer.getNom() == null) {
					System.err.println("Pour la création un nom est necessaire");
				} else {
					computer.setNom(computer.getNom());
					getNomDone = true;
				}
			} else {
				computer.setNom(newNom);
				getNomDone = true;
			}
		}
				
		while(!getIntroDone) {
			try {
				System.out.print("Introduced : ");
				String newIntroduced = sc.nextLine();
				if(newIntroduced.equals("null")) {
					computer.setIntroduced(null);
				} else if (newIntroduced.equals("")) {
					computer.setIntroduced(computer.getIntroduced());
				} else {
					ti = LocalDate.parse(newIntroduced, frenchPattern);
					computer.setIntroduced(ti);
				}
				getIntroDone = true;
			} catch (DateTimeParseException e) {
				System.err.println("Mauvais format, entrez une date de la forme dd/mm/yyyy");
			}
		}		
		
		while(!getDiscDone) {
			try {
				System.out.print("Discountinued : ");
				String newDiscontinued = sc.nextLine();
				if(newDiscontinued.equals("null")) {
					computer.setDiscontinued(null);
				} else if (newDiscontinued.equals("")) {
					computer.setDiscontinued(computer.getDiscontinued());
				} else {
					td = LocalDate.parse(newDiscontinued, frenchPattern);
					if(!td.isBefore(ti)) {
						computer.setDiscontinued(td);
						getDiscDone = true;
					} else {
						System.err.println("Date non correcte");
					}
				} 
			} catch (DateTimeParseException e) {
				System.err.println("Mauvais format, entrez une date de la forme dd/mm/yyyy");
			}
		}

		while(!getCompanyId) {
			try {
				System.out.print("Company ID : ");
				String campanyID = sc.nextLine();
				if(!campanyID.equals("null")) {
					computer.setCompanyId(Integer.parseInt(campanyID));
				}
				getCompanyId = true;
			} catch (IllegalArgumentException e) {
				System.err.println("Mauvais format, entrez une date de la forme yyyy-mm-dd hh:mm:ss");
			}	
		}
		return computer;
	}


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

	private static void displayFooter() {
		for (int i = 0; i < UI_SIZE; i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
