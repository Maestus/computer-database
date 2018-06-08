package main.java.com.excilys.cdb.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import main.java.com.excilys.cdb.dto.CompanyDTO;
import main.java.com.excilys.cdb.formValidator.ComputerFormValidator;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

@EnableWebMvc
@Controller
public class EditComputerController {

	ArrayList<CompanyDTO> companyDTOs;
	final DateTimeFormatter englishPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final Logger logger = LoggerFactory.getLogger(EditComputerController.class);

	private ComputerFormValidator formValidator;

	public void setFormValidator(ComputerFormValidator formValidator) {
		this.formValidator = formValidator;
	}

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(formValidator);
	}

	@ModelAttribute("computer")
	public Computer createComputerModel() {
		return new Computer();
	}

	@GetMapping("/edit")
	public String edit(@RequestParam(value = "id") Long id, ModelMap model) {
		Optional<Computer> optComp;

		if (id != null) {
			optComp = DashboardController.computerService.get(id);
			if (optComp.isPresent()) {

				System.out.println(optComp.get());

				Page<Company> pCompany = DashboardController.companyService.getList(0, Page.NO_LIMIT);
				companyDTOs = new ArrayList<>();

				for (Company comp : pCompany.elems) {
					CompanyDTO newObj = new CompanyDTO();
					newObj.setCompany(comp);

					companyDTOs.add(newObj);
				}

				model.addAttribute("companies", companyDTOs);
				model.addAttribute("computer", optComp.get());
				return "editComputer";
			}
		}

		return "404";
	}

	@PostMapping("/edit")
	public String editRequest(@ModelAttribute("computer") @Validated Computer computer, BindingResult bindingResult,
			ModelMap model) {

		System.out.println(computer);
		model.addAttribute("companies", companyDTOs);

		if (bindingResult.hasErrors()) {
			logger.info("\nPas bon\n");
			return "editComputer";
		} else {
			logger.info("\nReturning custSaveSuccess.jsp page\n");

			if (DashboardController.computerService.update(computer)) {
				model.addAttribute("updated", true);
			}

			return "editComputer";
		}
	}

}
