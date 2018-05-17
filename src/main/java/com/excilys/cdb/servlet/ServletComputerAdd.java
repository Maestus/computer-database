package main.java.com.excilys.cdb.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import main.java.com.excilys.cdb.dto.CompanyDTO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

/**
 * Servlet implementation class ServletComputerSearch.
 */
@WebServlet("/ComputerAdd")
@Configuration
public class ServletComputerAdd extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServletContext sc;
    final DateTimeFormatter englishPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    ArrayList<CompanyDTO> companyDTOs;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletComputerAdd() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Page<Company> pCompany = ServletComputer.companyServ.getListCompany(0, Page.NO_LIMIT);
            companyDTOs = new ArrayList<>();

            for (Company comp : pCompany.elems) {
                CompanyDTO newObj = new CompanyDTO();
                newObj.setCompany(comp);

                companyDTOs.add(newObj);
            }

            request.setAttribute("companies", companyDTOs);
            RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/addComputer.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Computer computer = new Computer();

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

        if (ServletComputer.computerServ.addComputer(computer)) {
            request.setAttribute("created", true);
        } else {
            request.setAttribute("dateError", true);
        }

        ServletComputer.nbComputers = ServletComputer.computerServ.getNumberComputer();

        request.setAttribute("companies", companyDTOs);
        
        RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/addComputer.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
        sc = getServletContext();
        System.out.println("Servlet " + this.getServletName() + " has started");
    }
}
