package main.java.com.excilys.cdb.controllers;

import java.util.Optional;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.ComputerService;
import main.java.com.excilys.cdb.utils.Page;
 
@RestController
@RequestMapping("/computer")
public class ComputerController {
    
	private ComputerService computerservice;
	private AbstractApplicationContext context;
	public static long NB_ElEM = 100;
	public static long CURRENT_INIT_ElEM = 0;
	
	public ComputerController() {
		context = new ClassPathXmlApplicationContext("classpath:serviceContext.xml");
		computerservice = (ComputerService) context.getBean("computerservice");
	}

    @GetMapping(path = "/{id}")
    public Computer getComputer(@PathVariable("id") Long id) {
 
    	Optional<Computer> computer = computerservice.get(id);
  
        return computer.get();
    }
 
    @GetMapping(path = "/page/{page}")
    public Page<Computer> listComputer(@PathVariable("page") Long page) {
    	if (page != null) {
    		CURRENT_INIT_ElEM = (page - 1) * NB_ElEM;
    		return computerservice.getList(CURRENT_INIT_ElEM, NB_ElEM);
    	}
        return computerservice.getList(0, Page.NO_LIMIT);
    }
    
    @PostMapping(path = "/save")
	public ResponseEntity<String> save(@RequestBody Computer computer) {
		StringBuffer output = new StringBuffer();
		if (computerservice.add(computer)) {
			return new ResponseEntity<String>(output.toString(), HttpStatus.CREATED);
		}	
		return new ResponseEntity<String>(output.toString(), HttpStatus.BAD_REQUEST);
	} 
    
    @DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Long id) {
		StringBuffer output = new StringBuffer();
		computerservice.remove(id);
		return new ResponseEntity<String>(output.toString(), HttpStatus.OK);
	} 
    
}