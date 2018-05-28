package main.java.com.excilys.cdb.formValidator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import main.java.com.excilys.cdb.model.Computer;

public class ComputerFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> paramClass) {
		return Computer.class.equals(paramClass);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Computer computer = (Computer) obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "user.name.empty");

		if (computer.getDiscontinued() != null) {
			if(computer.getIntroduced() != null && computer.getIntroduced().isAfter(computer.getDiscontinued())) {
				errors.rejectValue("discontinued", "user.date.invalid");
			}
		}
	}

}
