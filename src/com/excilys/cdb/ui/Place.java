package com.excilys.cdb.ui;

public enum Place {
	MENU_PRINCIPAL("menuPrincipal"), 
	MENU_COMPANY("menuCompany"), 
	MENU_COMPUTER("menuComputer");
	
    private String alias;
    
	private Place(String name){
        this.alias = name;
    }
	
	public String getAlias() {
		return alias;
	}
} 