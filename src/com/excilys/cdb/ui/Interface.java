package com.excilys.cdb.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Interface {

	Map<String, List<List<String>>> menu;
	Place p;
	Scanner sc;
	private String choix;

	CompanyService companyserv;
	ComputerService computerserv;
	
	List<Company> lcompany;
	List<Computer> lcomputer;
	Computer computer;
	
	public Interface(ComputerService computerserv, CompanyService companyserv) {
		this.companyserv = companyserv;
		this.computerserv = computerserv;
		lcompany = new ArrayList<>();
		lcomputer = new ArrayList<>();
		computer = new Computer();
		this.p = Place.MENU_PRINCIPAL;
		sc = new Scanner(System.in);
		ObjectMapper mapper = new ObjectMapper();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		TypeReference<HashMap<String, List<List<String>>>> typeRef = 
				new TypeReference<HashMap<String, List<List<String>>>>() {};
		try {
			menu = mapper.readValue(classLoader.getResourceAsStream("com/excilys/cdb/ui/menu.json"), typeRef);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	final static int UI_SIZE = 100;
	final static String UI_MESSAGE_HEADER = "LISTS OF COMPANIES & COMPUTERS";

	public void menu() {
		Interface.displayHeader();

		for (List<String> s : menu.get(p.getAlias())) {
			System.out.println(s.get(0) + " - " + s.get(1));
		}

		Interface.displayFooter();

		waitChoice();
		displayRes();
	}

	private void displayRes() {
		if (p == Place.MENU_PRINCIPAL) {
			displayList();
		} else if (p == Place.MENU_COMPANY) {
			displayComputerListByCompanyId();
		} else if (p == Place.MENU_COMPUTER) {
			displayComputerDetail();
		} else {
			controlComputer();
		}
		menu();
	}
 
	private void controlComputer() {
		try {
			if(!checkIfGoBack()) {
				if(Integer.parseInt(choix) == 1) {
					editOrCreateComputer();
					computerserv.updateComputer(computer);
				} else if(Integer.parseInt(choix) == 2) {
					computerserv.removeComputer(computer);
					p = Place.MENU_COMPUTER;
					displayList();
				}
			}		
		} catch(NumberFormatException e) {
			System.err.println("Choix non valide");
		}
	}

	private void displayList() {
		try {
			if(!checkIfGoBack()) { 
				if(menu.get(p.getAlias()).get(Integer.parseInt(choix) - 1).get(2).equals(CompanyService.class.getSimpleName())){
					lcompany = companyserv.getListCompany();
					for (Company c : lcompany) {
						System.out.println(c);
					}
					p = Place.MENU_COMPANY;
				} else if(menu.get(p.getAlias()).get(Integer.parseInt(choix) - 1).get(2).equals(ComputerService.class.getSimpleName())){
					int i = 1;
					lcomputer = computerserv.getListComputer();
					for (Computer c : lcomputer) {
						System.out.println(i++ + " " + c.getNom());
					}
					p = Place.MENU_COMPUTER;
				}
			}		
		} catch(NumberFormatException e) {
			System.err.println("Choix non valide");
		}
	}

	private void displayComputerListByCompanyId() {
		try {
			if(!checkIfGoBack()) {
				int i = 1;
				lcomputer = computerserv.getListComputerByCompany(Integer.parseInt(choix));
				for (Computer c : lcomputer) {
					System.out.println(i++ + " " + c);
				}
			}	
		} catch(NumberFormatException e) {
			System.err.println("Choix non valide");
		}
	}

	private void displayComputerDetail() {
		if(!checkIfGoBack()) {
			if(choix.equals("c")) {
				editOrCreateComputer();
				computerserv.addComputer(computer);
			} else {	
				try {
					if((Integer.parseInt(choix) - 1)  < lcomputer.size()) {
						computer = computerserv.getComputerById(lcomputer.get(Integer.parseInt(choix) - 1).getId());
						System.out.println(computer);
						p = Place.MENU_COMPUTER_DETAIL;
					}
				} catch(NumberFormatException e) {
					System.err.println("Choix non valide");
				}
			}
		}		
	}

	private void editOrCreateComputer() {
		System.out.print("Nom [" + computer.getNom() + "] : ");
		String newNom = sc.nextLine();
		computer.setNom(newNom);
		
		boolean getIntroDone = false, getDiscDone = false;
		
		while(!getIntroDone) {
			try {
				System.out.print("Introduced [" + computer.getIntroduced() + "] : ");
				String newIntroduced = sc.nextLine();
				if(newIntroduced.equals("null")) {
					computer.setIntroduced(null);
				} else {
					java.sql.Timestamp ti = java.sql.Timestamp.valueOf(newIntroduced);
					computer.setIntroduced(ti);
				}
				getIntroDone = true;
			} catch (IllegalArgumentException e) {
				System.err.println("Mauvais format, entrez une date de la forme yyyy-mm-dd hh:mm:ss");
			}
		}		
		
		while(!getDiscDone) {
			try {
				System.out.print("Discountinued [" + computer.getDiscontinued() + "] : ");
				String newDiscontinued = sc.nextLine();
				if(newDiscontinued.equals("null")) {
					computer.setDiscontinued(null);
				} else {
					java.sql.Timestamp td = java.sql.Timestamp.valueOf(newDiscontinued);
					computer.setDiscontinued(td);
				}
				getDiscDone = true;
			} catch (IllegalArgumentException e) {
				System.err.println("Mauvais format, entrez une date de la forme yyyy-mm-dd hh:mm:ss");
			}	
		}
	}

	private boolean checkIfGoBack() {
		if(choix.equals("q")) {
			List<String> l = menu.get(p.getAlias()).get(menu.get(p.getAlias()).size() - 1);
			if(Place.MENU_PRINCIPAL.getAlias().equals(l.get(l.size() - 1))) {
				p = Place.MENU_PRINCIPAL;
			} else if(Place.MENU_COMPUTER.getAlias().equals(l.get(l.size() - 1))) {
				p = Place.MENU_COMPUTER;
			}
			return true;
		}
		return false;
	}

	private void waitChoice() {
		boolean goodChoice = false;
		while(!goodChoice) {
			try {
				System.out.print("Votre choix : ");
				choix = sc.nextLine();
				if(choix.equals("quit")) {
					System.exit(0);
				}
				goodChoice = true;
			} catch (NoSuchElementException | IllegalStateException e) {
				System.err.println("Choix non valide");
			}
		}
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
