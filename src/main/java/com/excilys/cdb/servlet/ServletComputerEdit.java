package main.java.com.excilys.cdb.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.dto.CompanyDTO;
import main.java.com.excilys.cdb.dto.ComputerDTO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.service.ComputerService;
import main.java.com.excilys.cdb.utils.Page;

@WebServlet("/edit")
public class ServletComputerEdit extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServletContext sc;
    private DAOFactory dao;
    private ComputerService computerServ;
    private CompanyService companyServ;
    ArrayList<CompanyDTO> companyDTOs;
    final DateTimeFormatter englishPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletComputerEdit() {
        super();
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet " + this.getServletName() + " has started");
        dao = DAOFactory.getInstance();

        computerServ = new ComputerService();
        companyServ = new CompanyService();

        computerServ.init(dao);
        companyServ.init(dao);
        sc = getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("id") != null) {
            try {
                Optional<Computer> optComp;
                long id = Long.parseLong(request.getParameter("id"));
                if ((optComp = computerServ.getComputerById(id)).isPresent()) {
                    ComputerDTO computer = new ComputerDTO();
                    computer.setComputer(optComp.get());
                    if (computer.getComputer().getCompanyId() != null) {
                        computer.setCompany(companyServ.getCompany((long) computer.getComputer().getCompanyId()).get());
                    }

                    Page<Company> pCompany = companyServ.getListCompany(0, Page.NO_LIMIT);
                    companyDTOs = new ArrayList<>();

                    for (Company comp : pCompany.elems) {
                        CompanyDTO newObj = new CompanyDTO();
                        newObj.setCompany(comp);

                        companyDTOs.add(newObj);
                    }

                    request.setAttribute("companies", companyDTOs);
                    request.setAttribute("computer", computer);

                } else {
                    RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/404.jsp");
                    dispatcher.forward(request, response);
                }
            } catch (NumberFormatException e) {
                RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/404.jsp");
                dispatcher.forward(request, response);
            }
            RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/editComputer.jsp");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/404.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	if(request.getParameter("selection") != null) {
            System.out.println(request.getParameter("selection"));
    		delete(request.getParameter("selection").split(","));
            RequestDispatcher dispatcher = sc.getRequestDispatcher("/ComputerList?page=1");
            dispatcher.forward(request, response);
    	} else {
	
	        Computer computer = new Computer();
	
	        computer.setId(Long.parseLong(request.getParameter("id")));
	        computer.setNom(request.getParameter("computerName"));
	
	        LocalDate ti = null, td = null;
	
	        if (!request.getParameter("introduced").equals("")) {
	            System.out.println(request.getParameter("introduced"));
	            ti = LocalDate.parse(request.getParameter("introduced"), englishPattern);
	            computer.setIntroduced(ti);
	        }
	
	        if (!request.getParameter("discontinued").equals("")) {
	            td = LocalDate.parse(request.getParameter("discontinued"), englishPattern);
	            computer.setDiscontinued(td);
	        }
	
	        computer.setCompanyId(Long.parseLong(request.getParameter("companyId")));
	
	        System.out.println(computer);
	
	        if (computerServ.updateComputer(computer)) {
	            request.setAttribute("updated", true);
	            response.sendRedirect("ComputerList?page=1");
	        } else {
	            request.setAttribute("id", request.getParameter("id"));
	            request.setAttribute("dateError", true);
	            doGet(request, response);
	        }
    	}
    }

	private void delete(String[] split) {
		try {
			Computer c;
			for(String s : split) {
				c = new Computer();
				c.setId(Long.parseLong(s));
				computerServ.removeComputer(c);
			}	
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

}
