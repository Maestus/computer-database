package main.java.com.excilys.cdb.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.dto.CompanyDTO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.service.ComputerService;
import main.java.com.excilys.cdb.utils.Page;

/**
 * Servlet implementation class ServletComputerSearch.
 */
@WebServlet("/ComputerAdd")
public class ServletComputerAdd extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DAOFactory dao;
    private ComputerService computerServ;
    private CompanyService companyServ;
    private ServletContext sc;
    final DateTimeFormatter englishPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletComputerAdd() {
        super();
    }

    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            Page<Company> pCompany = companyServ.getListCompany(0, Page.NO_LIMIT);
            ArrayList<CompanyDTO> companyDTOs = new ArrayList<>();

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Computer computer = new Computer();

        computer.setNom(request.getParameter("computerName"));

        if (!request.getParameter("introduced").equals("")) {
            System.out.println(request.getParameter("introduced"));
            LocalDate ti = LocalDate.parse(request.getParameter("introduced"), englishPattern);
            computer.setIntroduced(ti);
        }

        if (!request.getParameter("discontinued").equals("")) {
            LocalDate td = LocalDate.parse(request.getParameter("discontinued"), englishPattern);
            computer.setDiscontinued(td);
        }

        computer.setCompanyId(Long.parseLong(request.getParameter("companyId")));

        computerServ.addComputer(computer);

        ServletComputer.nbComputers = computerServ.getNumberComputer();

        request.setAttribute("created", true);

        RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/addComputer.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet " + this.getServletName() + " has started");
        dao = DAOFactory.getInstance();
        try {
            dao.getConnection();
            dao.setConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        computerServ = new ComputerService();
        companyServ = new CompanyService();

        computerServ.init(dao);
        companyServ.init(dao);
        sc = getServletContext();
    }
}
