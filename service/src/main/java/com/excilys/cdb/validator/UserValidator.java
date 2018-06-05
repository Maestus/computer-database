package main.java.com.excilys.cdb.validator;

import main.java.com.excilys.cdb.dao.UserDAO;
import main.java.com.excilys.cdb.exception.ValidatorDateException;
import main.java.com.excilys.cdb.exception.ValidatorException;
import main.java.com.excilys.cdb.model.Model;

public class UserValidator extends Validator {
	public UserDAO userdao;

    public UserValidator(UserDAO userdao) {
    	this.userdao = userdao;
    }
    
    public void setComputerDAO(UserDAO userdao) {
 		this.userdao = userdao;
 	}
    
    @Override
    public boolean checkBeforeUpdate(Model c) throws ValidatorException {
        return true;
    }

    @Override
    public boolean checkBeforeCreation(Model c) throws ValidatorDateException {
        return true;
    }

}
