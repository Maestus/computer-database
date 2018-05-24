package main.java.com.excilys.cdb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import main.java.com.excilys.cdb.dto.CompanyDTO;
import main.java.com.excilys.cdb.dto.ComputerDTO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

@EnableWebMvc
@Controller
public class EditComputerController {

	ArrayList<CompanyDTO> companyDTOs;
	final DateTimeFormatter englishPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@RequestMapping(value = "/edit")
	public String edit(@RequestParam(value = "id") Long id, ModelMap model) {
		Optional<Computer> optComp;

		ComputerDTO computer = new ComputerDTO();
		optComp = DashboardController.computerService.getComputerById(id);
		if(optComp.isPresent()) {
			computer.setComputer(optComp.get());
			if (computer.getComputer().getCompanyId() != null) {
				computer.setCompany(
						DashboardController.companyService.get((long) computer.getComputer().getCompanyId()).get());
			}
	
			Page<Company> pCompany = DashboardController.companyService.getList(0, Page.NO_LIMIT);
			companyDTOs = new ArrayList<>();
	
			for (Company comp : pCompany.elems) {
				CompanyDTO newObj = new CompanyDTO();
				newObj.setCompany(comp);
	
				companyDTOs.add(newObj);
			}
	
			model.addAttribute("companies", companyDTOs);
			model.addAttribute("computer", computer);
	
			return "editComputer";
		}
		
		return "404";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editRequest(@RequestParam(value = "id") Long id,
			@RequestParam(value = "computerName") String computerName,
			@RequestParam(value = "introduced") String introduced,
			@RequestParam(value = "discontinued") String discontinued,
			@RequestParam(value = "companyId") Long companyId, ModelMap model) {

		Computer computer = new Computer();

		computer.setId(id);
		computer.setNom(computerName);

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

		computer.setCompanyId(companyId);

		System.out.println(computer);

		if (DashboardController.computerService.updateComputer(computer)) {
			model.addAttribute("updated", true);
			return edit(id, model);
		}

		model.addAttribute("id", id);
		model.addAttribute("dateError", true);

		return edit(id, model);
	}

}
