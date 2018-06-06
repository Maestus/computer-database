package main.java.com.excilys.cdb.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import main.java.com.excilys.cdb.dao.UserDAO;
import main.java.com.excilys.cdb.validator.UserValidator;

public class UserService implements UserDetailsService {
	public UserDAO userdao;
	public UserValidator uservalidator;

	public void setUserDAO(UserDAO userdao) {
		this.userdao = userdao;
	}

	public void setComputerValidator(UserValidator uservalidator) {
		this.uservalidator = uservalidator;
	}

	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
