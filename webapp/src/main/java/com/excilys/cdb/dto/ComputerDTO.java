package main.java.com.excilys.cdb.dto;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;

public class ComputerDTO {

    Computer computer;
    Company company;
    boolean hasCompany;

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public void setHasCompany(boolean company) {
        this.hasCompany = company;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }
}
