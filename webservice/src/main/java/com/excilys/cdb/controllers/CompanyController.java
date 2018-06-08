package main.java.com.excilys.cdb.controllers;

import java.util.Optional;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.utils.Page;
 
@RestController
@RequestMapping("/company")
public class CompanyController {

	private CompanyService companyservice;
	private AbstractApplicationContext context;
	public static long NB_ElEM = 100;
	public static long CURRENT_INIT_ElEM = 0;
	
	public CompanyController() {
		context = new ClassPathXmlApplicationContext("classpath:serviceContext.xml");
		companyservice = (CompanyService) context.getBean("companyservice");
	}

    @GetMapping(path = "/{id}")
    public Company getCompany(@PathVariable("id") Long id) {
 
    	Optional<Company> company = companyservice.get(id);
  
        return company.get();
    }
 
    @GetMapping(path = "/page/{page}")
    public Page<Company> listCompany(@PathVariable("page") Long page) {
    	if (page != null) {
    		CURRENT_INIT_ElEM = (page - 1) * NB_ElEM;
    		return companyservice.getList(CURRENT_INIT_ElEM, NB_ElEM);
    	}
        return companyservice.getList(0, Page.NO_LIMIT);
    }

    @DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Long id) {
		StringBuffer output = new StringBuffer();
		companyservice.remove(id);
		return new ResponseEntity<String>(output.toString(), HttpStatus.OK);
	} 
    
}