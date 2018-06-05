package main.java.com.excilys.cdb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import main.java.com.excilys.cdb.dto.CompanyDTO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

@EnableWebMvc
@Controller
public class AddComputerController {

	ArrayList<CompanyDTO> companyDTOs;
	final DateTimeFormatter englishPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		Page<Company> pCompany = DashboardController.companyService.getList(0, Page.NO_LIMIT);
		companyDTOs = new ArrayList<>();

		for (Company comp : pCompany.elems) {
			CompanyDTO newObj = new CompanyDTO();
			newObj.setCompany(comp);

			companyDTOs.add(newObj);
		}

		model.addAttribute("companies", companyDTOs);

		return "addComputer";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String dashboard(@RequestParam(value = "computerName") String computerName,
			String introduced,
			String discontinued,
			String companyId, ModelMap model) {

		Computer computer = new Computer();

		computer.setName(computerName);

		LocalDate ti = null, td = null;

		if (!"".equals(introduced)) {
			System.out.println(introduced);
			ti = LocalDate.parse(introduced, englishPattern);
			computer.setIntroduced(ti);
		}

		if (!"".equals(discontinued)) {
			td = LocalDate.parse(discontinued, englishPattern);
			computer.setDiscontinued(td);
		}

		computer.setCompanyId(Long.parseLong(companyId));

		System.out.println(computer);
		if (DashboardController.computerService.addComputer(computer)) {
			model.addAttribute("created", true);
		} else {
			model.addAttribute("dateError", true);
		}

		DashboardController.nbComputers = DashboardController.computerService.getNumberComputer();

		model.addAttribute("companies", companyDTOs);

		return "addComputer";
	}
}
