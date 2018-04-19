package com.excilys.cdb.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.ui.Interface;
import com.excilys.cdb.ui.Place;
import com.excilys.cdb.utils.Page;

public class UIController {
	Scanner sc;
	private String choix;

	CompanyService companyserv;
	ComputerService computerserv;
	Interface ui;
	
	Page<Company> lcompany;
	Page<Computer> lcomputer;
	Computer computer;
	
	public int pageSize = 10;
	public int offset = 0;

	
	public UIController(Interface ui, ComputerService computerserv, CompanyService companyserv, Scanner sc) {
		this.companyserv = companyserv;
		this.computerserv = computerserv;
		computer = new Computer();
		this.sc = sc;
		this.ui = ui;
	}

	public void launchUI() {
		ui.menu();
		waitChoice();
		displayRes();		
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
				System.err.println("Choix non valide -");
			}
		}
	}
	
	public void displayRes() {
		if (ui.p == Place.MENU_SETTING) {
			setting();
		} else if (ui.p == Place.MENU_PRINCIPAL) {
			displayList();
		} else if (ui.p == Place.MENU_COMPANY) {
			displayComputerListByCompanyId();
		} else if (ui.p == Place.MENU_COMPUTER) {
			displayComputerDetail();
		} else {
			controlComputer();
		}
		
		launchUI();
	}
	
	public void setPageComputer() {
		lcomputer = computerserv.getListComputer(offset, pageSize);
		if(lcomputer.elems.size() == 0) {
			offset -= pageSize;
		}
		lcomputer = computerserv.getListComputer(offset, pageSize);
	}
	
	public void setPageCompany() {
		lcompany = companyserv.getListCompany(offset, pageSize);
		if(lcompany.elems.size() == 0) {
			offset -= pageSize;
		}
		lcompany = companyserv.getListCompany(offset, pageSize);
	}
	
	private void displayList() {
		try {
			if(!checkIfGoBack()) { 
				if(ui.menu.get(ui.p.getAlias()).get(Integer.parseInt(choix) - 1).get(2).equals(CompanyService.class.getSimpleName())){
					offset = 0;
					setPageCompany();
					ui.displayCompanyList(lcompany);
				} else if(ui.menu.get(ui.p.getAlias()).get(Integer.parseInt(choix) - 1).get(2).equals(ComputerService.class.getSimpleName())){
					offset = 0;
					setPageComputer();
					ui.displayComputerList(lcomputer);
				} else if (ui.menu.get(ui.p.getAlias()).get(Integer.parseInt(choix) - 1).get(2).equals("Setting")) {
					setting();
					ui.p = Place.MENU_PRINCIPAL;
				}
			}		
		} catch(NumberFormatException e) {
			System.err.println("Choix non valide -2");
		}
	}
	
	private void displayComputerListByCompanyId() {
		try {
			if(!checkIfGoBack()) {
				if(choix.equals("s")) {
					offset += pageSize;
					setPageCompany();
					ui.displayCompanyList(lcompany);
				} else if(choix.equals("p")) {
					offset -= pageSize;
					if(offset < 0) {
						offset = 0;
					}
					setPageCompany();
					ui.displayCompanyList(lcompany);
				} else {
					int i = 1;
					lcomputer = computerserv.getListComputerByCompany(offset, pageSize, Integer.parseInt(choix));
					for (Computer c : lcomputer.elems) {
						System.out.println(i++ + " " + c);
					}
				}
			}	
		} catch(NumberFormatException e) {
			System.err.println("Choix non valide");
		}
	}
	
	private boolean checkIfGoBack() {
		if(choix.equals("q")) {
			List<String> l = ui.menu.get(ui.p.getAlias()).get(ui.menu.get(ui.p.getAlias()).size() - 1);
			if(Place.MENU_COMPUTER.getAlias().equals(l.get(l.size() - 1))) {
				ui.p = Place.MENU_COMPUTER;
			} else {
				ui.p = Place.MENU_PRINCIPAL;
			}
			return true;
		}
		return false;
	}
	
	public void setting() {
		
		boolean goodValue = false;
		while(!goodValue) {
			try {
				System.out.print("Page size [" + pageSize  + "] : ");
				pageSize = Integer.parseInt(sc.nextLine());
				goodValue = true;
				
			} catch (NumberFormatException e) {
				System.err.println("Mauvais format entrez un entier");
			}
		}
	}
	
	private void displayComputerDetail() {
		if(!checkIfGoBack()) {
			if(choix.equals("c")) {
				computer = new Computer();
				ui.editOrCreateComputer(computer);
				computerserv.addComputer(computer);
			} else if(choix.equals("s")) {
				offset += pageSize;
				setPageComputer();
				ui.displayComputerList(lcomputer);
			} else if(choix.equals("p")) {
				offset -= pageSize;
				if(offset < 0) {
					offset = 0;
				}
				setPageComputer();
				ui.displayComputerList(lcomputer);
			} else {	
				try {
					if((Integer.parseInt(choix) - 1)  < lcomputer.elems.size()) {
						computer = computerserv.getComputerById(lcomputer.elems.get(Integer.parseInt(choix) - 1).getId());
						System.out.println(computer);
						ui.p = Place.MENU_COMPUTER_DETAIL;
					}
				} catch(NumberFormatException e) {
					System.err.println("Choix non valide - 3");
				}
			}
		}		
	}

	public void controlComputer() {
		try {
			if(!checkIfGoBack()) {
				if(Integer.parseInt(choix) == 1) {
					computer = ui.editOrCreateComputer(computer);
					computerserv.updateComputer(computer);
				} else if(Integer.parseInt(choix) == 2) {
					computerserv.removeComputer(computer);
					ui.p = Place.MENU_COMPUTER;
					displayList();
				}
			}		
		} catch(NumberFormatException e) {
			System.err.println("Choix non valide");
		}
	}

	
}
