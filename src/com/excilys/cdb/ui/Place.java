package com.excilys.cdb.ui;

public enum Place {
	MENU_SETTING("menuSetting"),
	MENU_PRINCIPAL("menuPrincipal"), 
	MENU_COMPANY("menuCompany"), 
	MENU_COMPUTER("menuComputer"),
	MENU_COMPUTER_DETAIL("menuComputerDetail");
	
    private String alias;
    
	private Place(String name){
        this.alias = name;
    }
	
	public String getAlias() {
		return alias;
	}
} 