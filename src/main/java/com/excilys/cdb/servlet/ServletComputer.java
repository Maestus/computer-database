package main.java.com.excilys.cdb.servlet;

import main.java.com.excilys.cdb.dto.ComputerDTO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.service.ComputerService;
import main.java.com.excilys.cdb.utils.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Servlet.
 */
@WebServlet("/")
public class ServletComputer extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static long nbComputers = 0;
    static long nbComputersSearch = 0;

    enum SearchType {
        COMPUTER, COMPANY;
    }

    SearchType type;
    static String searchedString;
    private int paging;
    private int offset;
    static ComputerService computerServ;
    static CompanyService companyServ;
    private ServletContext sc;
    private boolean typeChanged;
    Page<Computer> pComputer;
    ArrayList<ComputerDTO> computerDTOs;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletComputer() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            if (request.getParameter("direct") != null) {
                if (request.getParameter("direct").equals("index")) {
                    RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/index.jsp");
                    dispatcher.forward(request, response);
                }
            } else {

                if (request.getParameter("page") != null) {
                    offset = (Integer.parseInt(request.getParameter("page")) - 1) * paging;
                    request.setAttribute("page", request.getParameter("page"));
                }

                pComputer = new Page<>(offset, paging);

                if (request.getAttribute("search") != null || request.getParameter("search") != null) {
                    String searchString = (request.getParameter("search") == null)
                            ? (String) request.getAttribute("search")
                            : request.getParameter("search");

                    if (request.getParameter("searchBy") != null) {
                        if (request.getParameter("searchBy").equals("forComputer")) {
                            offset = 0;
                            type = SearchType.COMPUTER;
                            typeChanged = true;
                        } else {
                            offset = 0;
                            type = SearchType.COMPANY;
                            typeChanged = true;
                        }
                    }
                    if (type == SearchType.COMPUTER) {
                        pComputer = computerServ.getComputerByName(searchString, offset, paging);
                    } else {
                        pComputer = computerServ.getComputerByCompanyName(searchString, offset, paging);
                    }
                    request.setAttribute("searchProcess", true);
                    request.setAttribute("search", searchString);
                } else {
                    searchedString = null;
                    pComputer = computerServ.getListComputer(offset, paging);
                    request.setAttribute("searchProcess", false);
                }

                computerDTOs = new ArrayList<>();

                for (Computer comp : pComputer.elems) {

                    ComputerDTO newObj = new ComputerDTO();
                    newObj.setComputer(comp);

                    Optional<Company> company;

                    if ((company = computerServ.getCompany(comp.getId())).isPresent()) {
                        newObj.setCompany(company.get());
                    }

                    if (newObj.getCompany() == null) {
                        newObj.setHasCompany(false);
                    } else {
                        newObj.setHasCompany(true);
                    }

                    computerDTOs.add(newObj);
                }

                long nbElem = 0;

                if (request.getParameter("search") != null
                        && (typeChanged || !request.getParameter("search").equals(searchedString))) {
                    if (type == SearchType.COMPANY) {
                        nbComputersSearch = computerServ.getNumberComputerByCompanyName(request.getParameter("search"));
                    } else {
                        nbComputersSearch = computerServ.getNumberComputerByName(request.getParameter("search"));
                    }
                    searchedString = request.getParameter("search");
                    typeChanged = false;
                }

                if (request.getAttribute("search") != null || request.getParameter("search") != null) {
                    nbElem = nbComputersSearch;
                } else {
                    nbElem = nbComputers;
                }

                request.setAttribute("companyWithComputers", computerDTOs);
                request.setAttribute("numberOfElement", nbElem);

                double nbPage = (double) nbElem / (double) paging;

                request.setAttribute("nbPage", Math.ceil(nbPage));

                if (request.getParameter("page") == null) {
                    request.setAttribute("page", 1);
                }

                RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/dashboard.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet " + this.getServletName() + " has started");
        computerServ = new ComputerService();
        companyServ = new CompanyService();

        computerServ.init();
        companyServ.init();
        sc = getServletContext();
        nbComputers = computerServ.getNumberComputer();
        paging = Integer.parseInt(getServletContext().getInitParameter("paging"));
        offset = Integer.parseInt(getServletContext().getInitParameter("offset"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("b1") != null) {
            paging = Integer.parseInt(request.getParameter("b1"));
        } else if (request.getParameter("b2") != null) {
            paging = Integer.parseInt(request.getParameter("b2"));
        } else if (request.getParameter("b3") != null) {
            paging = Integer.parseInt(request.getParameter("b3"));
        }
        offset = 0;
        if (searchedString != null) {
            request.setAttribute("search", searchedString);
        }
        doGet(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("Servlet " + this.getServletName() + " has stopped");
    }
}
