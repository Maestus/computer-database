package main.java.com.excilys.cdb.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.RedirectView;

import main.java.com.excilys.cdb.dto.ComputerDTO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.service.ComputerService;
import main.java.com.excilys.cdb.utils.Page;

@EnableWebMvc
@Controller
public class DashboardController {

	public static ApplicationContext context;

	private Page<Computer> pComputer;
	ArrayList<ComputerDTO> computerDTOs;

	public static ComputerService computerService;
	public static CompanyService companyService;

	public static int offset = 0;
	public static int paging = 10;
	public static int currentPage = 1;
	static long nbComputers = 0;

	public DashboardController() {
		context = new ClassPathXmlApplicationContext("application-context.xml");
		computerService = (ComputerService) context.getBean("computerservice");
		companyService = (CompanyService) context.getBean("companyservice");

		nbComputers = computerService.getNumberComputer();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public RedirectView changePagging(@RequestParam(value = "selection", required = false) String selection,
			ModelMap model) {
		try {
			String[] split = selection.split(",");
			DashboardController.computerService.removeList(split);
			DashboardController.nbComputers = DashboardController.computerService.getNumberComputer();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return new RedirectView("dashboard");
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.POST)
	public String changePagging(@RequestParam(value = "b1", required = false) String b1,
			@RequestParam(value = "b2", required = false) String b2,
			@RequestParam(value = "b3", required = false) String b3,
			ModelMap model) {

		if(b1 != null || b2 != null || b3 != null) {
			changePagging(b1, b2, b3);
		}
		
		return dashboard(null, model);
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard(@RequestParam(value = "page", required = false) Integer page, ModelMap model) {

		if (page != null) {
			offset = (page - 1) * paging;
			model.addAttribute("page", page);
		}

		pComputer = new Page<>(offset, paging);

		pComputer = computerService.getListComputer(offset, paging);
		model.addAttribute("searchProcess", false);

		computerDTOs = new ArrayList<>();

		for (Computer comp : pComputer.elems) {

			ComputerDTO newObj = new ComputerDTO();
			newObj.setComputer(comp);

			Optional<Company> company;

			if ((company = computerService.getCompany(comp.getId())).isPresent()) {
				newObj.setCompany(company.get());
			}

			if (newObj.getCompany() == null) {
				newObj.setHasCompany(false);
			} else {
				newObj.setHasCompany(true);
			}

			computerDTOs.add(newObj);
		}

		long nbElem = nbComputers;
		model.addAttribute("companyWithComputers", computerDTOs);
		model.addAttribute("numberOfElement", nbElem);

		double nbPage = (double) nbElem / (double) paging;

		model.addAttribute("nbPage", Math.ceil(nbPage));

		if (page == null) {
			model.addAttribute("page", 1);
		}
		model.addAttribute("message", "Hello Spring MVC Framework!");
		return "dashboard";
	}

	private void changePagging(String b1, String b2, String b3) {
		if (b1 != null) {
			paging = Integer.parseInt(b1);
		} else if (b2 != null) {
			paging = Integer.parseInt(b2);
		} else if (b3 != null) {
			paging = Integer.parseInt(b3);
		}

		offset = 0;
	}
}
