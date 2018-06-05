package main.java.com.excilys.cdb.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import main.java.com.excilys.cdb.dto.ComputerDTO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

@EnableWebMvc
@Controller
public class SearchController {

	private Page<Computer> pComputer;
	ArrayList<ComputerDTO> computerDTOs;

	static long nbComputersSearch = 0;
	public static int offset = 0;
	public static int currentPage = 1;
	static long nbComputers = 0;
	boolean isComputerResearch = false;
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String dashboard(@RequestParam(value = "search") String search,
			@RequestParam(value = "searchBy") String searchBy,
			@RequestParam(value = "page", required = false) Integer page, ModelMap model) {

		if (page != null) {
			offset = (page - 1) * DashboardController.paging;
			model.addAttribute("page", page);
		}

		pComputer = new Page<>(offset, DashboardController.paging);

		if ("forComputer".equals(searchBy)) {
			if(!isComputerResearch) {
				offset = 0;
				isComputerResearch = true;
			}
			pComputer = DashboardController.computerService.getComputerByName(search, offset, DashboardController.paging);
		} else {
			if(isComputerResearch) {
				offset = 0;
				isComputerResearch = false;
			}
			pComputer = DashboardController.computerService.getComputerByCompanyName(search, offset, DashboardController.paging);
		}

		model.addAttribute("searchProcess", true);
		model.addAttribute("search", search);
		model.addAttribute("searchBy", searchBy);

		computerDTOs = new ArrayList<>();

		for (Computer comp : pComputer.elems) {

			ComputerDTO newObj = new ComputerDTO();
			newObj.setComputer(comp);

			Optional<Company> company;

			if ((company = DashboardController.computerService.getCompany(comp.getId())).isPresent()) {
				newObj.setCompany(company.get());
			}

			if (newObj.getCompany() == null) {
				newObj.setHasCompany(false);
			} else {
				newObj.setHasCompany(true);
			}

			computerDTOs.add(newObj);
		}

		if ("forCompany".equals(searchBy)) {
			nbComputersSearch = DashboardController.companyService.getNumberComputerByCompanyName(search);
		} else {
			nbComputersSearch = DashboardController.computerService.getNumberComputerByName(search);
		}

		long nbElem = nbComputersSearch;
		model.addAttribute("companyWithComputers", computerDTOs);
		model.addAttribute("numberOfElement", nbElem);

		double nbPage = (double) nbElem / (double) DashboardController.paging;

		model.addAttribute("nbPage", Math.ceil(nbPage));

		if (page == null) {
			model.addAttribute("page", 1);
		}

		return "dashboard";
	}
}
