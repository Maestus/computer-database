package main.java.com.excilys.cdb.service;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

public class ComputerService {

    public ComputerDAO computerDao;
    public CompanyDAO companyDao;

    /**
     * Initialiastion du ComputerService.
     * @param dao un objet DAOFactory.
     */
    public void init(DAOFactory dao) {
        this.computerDao = new ComputerDAO(dao);
        this.companyDao = new CompanyDAO(dao);
    }

    /**
     * Retourne un ensemble d'element de computers.
     * @param offset Determine à partir de quel element on stocke dans la liste.
     * @param nbElem Nombre d'element à stocker.
     * @return Une Page.
     */
    public Page<Computer> getListComputer(int offset, int nbElem) {
        return computerDao.findAll(offset, nbElem);
    }

    /**
     * Retourne un ensemble d'element de computers à partir d'une company.
     * @param offset Determine à partir de quel element on stocke dans la liste.
     * @param nbElem Nombre d'element à stocker.
     * @param id Identifiant de la company.
     * @return Une Page.
     */
    public Page<Computer> getListComputerByCompany(int offset, int nbElem, long id) {
        return computerDao.findByCompanyId(offset, nbElem, id);
    }

    /**
     * Retourne un Computer à partir d'un identifiant.
     * @param id Identifiant du computer.
     * @return Un computer.
     */
    public Computer getComputerById(Long id) {
        return (Computer) computerDao.findById(id);
    }

    /**
     * Ajoute un computer dans la base de données.
     * @param c Computer à ajouter.
     * @return l'identifiant du computer.
     */
    public long addComputer(Computer c) {
        if (checkDate(c)) {
            if (c.getCompanyId() != null) {
                if (companyDao.findById((long) c.getCompanyId()) != null) {
                    try {
                        return computerDao.create(c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Campany inexistante");
                }
            } else {
                try {
                    return computerDao.create(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println("Dates incohérentes");
        }
        return 0;
    }

    /**
     * Verification de la date du Computer.
     * @param c Un computer.
     * @return true si discontinued > introduced
     */
    private boolean checkDate(Computer c) {
        return !(c.getDiscontinued() != null && c.getDiscontinued().isBefore(c.getIntroduced()));
    }

    /**
     * Met à jour un computer en base de données.
     * @param c Un computer.
     */
    public void updateComputer(Computer c) {
        if (checkDate(c)) {
            if (c.getCompanyId() != null) {
                if (companyDao.findById((long) c.getCompanyId()) != null) {
                    computerDao.update(c);
                } else {
                    System.err.println("Campany inexistante");
                }
            } else {
                computerDao.update(c);
            }
        } else {
            System.err.println("Dates incohérentes");
        }
    }

    /**
     * Supprime un computer.
     * @param computer Un Computer.
     */
    public void removeComputer(Computer computer) {
        System.out.println(computer.getId());
        computerDao.delete(computer.getId());
    }
}
