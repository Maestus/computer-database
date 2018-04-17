package com.excilys.cdb.ui;

import java.io.IOException;
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
	private int choix;

	CompanyService companyserv;
	ComputerService computerserv;
	
	public Interface(ComputerService computerserv, CompanyService companyserv) {
		this.companyserv = companyserv;
		this.computerserv = computerserv;
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
			if(menu.get(p.getAlias()).get(choix - 1).get(2).equals(CompanyService.class.getSimpleName())){
				List<Company> lcompany = companyserv.getListCompany();
				for (Company c : lcompany) {
					System.out.println(c);
				}
				p = Place.MENU_COMPANY;
				menu();
			} else if(menu.get(p.getAlias()).get(choix - 1).get(2).equals(ComputerService.class.getSimpleName())){
				List<Computer> lcomputer = computerserv.getListComputer();
				for (Computer c : lcomputer) {
					System.out.println(c);
				}
				p = Place.MENU_COMPUTER;
				menu();
			}
		}
	}
 
	private void waitChoice() {
		try {
			System.out.print("Votre choix : ");
			choix = sc.nextInt();
		} catch (NoSuchElementException | IllegalStateException e) {
			e.printStackTrace();
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
