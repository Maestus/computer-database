package main.java.com.excilys.cdb.dto;

import java.util.List;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;

public class CompanyDTO {

    Company company;
    List<Computer> computers;
    boolean hasComputer;

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setHasComputer(boolean hasComputer) {
        this.hasComputer = hasComputer;
    }

    public void setComputers(List<Computer> computers) {
        this.computers = computers;
    }

    public List<Computer> getComputers() {
        return computers;
    }

    public Company getCompany() {
        return company;
    }
}
