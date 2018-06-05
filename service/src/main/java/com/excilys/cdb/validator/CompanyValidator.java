package main.java.com.excilys.cdb.validator;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.exception.ValidatorDateException;
import main.java.com.excilys.cdb.exception.ValidatorException;
import main.java.com.excilys.cdb.model.Model;

public class CompanyValidator extends Validator {

    public CompanyDAO companydao;

    public void setCompanyDAO(CompanyDAO companydao) {
		this.companydao = companydao;
	}
    
	@Override
	public boolean checkBeforeUpdate(Model m) throws ValidatorException {
		return true;
	}

	@Override
	public boolean checkBeforeCreation(Model m) throws ValidatorDateException {		
		if (m == null)
			return false;
		return true;
	}

}
