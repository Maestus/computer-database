package test.java.com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.utils.Page;

public class CompanyDAOTest {
    DAOFactory dao;
    CompanyDAO companyDao;
    final DateTimeFormatter frenchPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Initialisation du mock.
     */
    @Before
    public void setUp() {
        dao = DAOFactory.getInstance();
        try {
            dao.setConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        companyDao = new CompanyDAO(dao);
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Teste l'obtention d'un tuple dans la table computer via ComputerDao
     * et ComputerService.
     * @throws Exception
     *             Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void getComputerTest() throws Exception {
        Company c1 = (Company) companyDao.findById(1L);
        assertEquals("ACER", c1.getNom());
    }

    /**
     * Teste la création d'un nouveau tuple dans la table computer via ComputerService.
     * @throws Exception
     *             Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void createComputerTest() throws Exception {
        Company computer = new Company();
        computer.setNom("Testing");
        long i = companyDao.create(computer);
        Company c1 = (Company) companyDao.findById(6L);
        assertEquals(c1.getNom(), computer.getNom());
        companyDao.delete(i);
    }

    /**
     * Teste l'obtention de tout les element de la table computer.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void getAllComputerTest() throws Exception {
        Page<Company> c1 = companyDao.findAll(0, Page.NO_LIMIT);

        if (c1.elems.get(0).getId() == 1) {
            assertEquals(c1.elems.get(0).getNom(), "ACER");
            assertEquals(c1.elems.get(1).getNom(), "HP");
            assertEquals(c1.elems.get(2).getNom(), "ASUS");
            assertEquals(c1.elems.get(3).getNom(), "ALSTOM");
            assertEquals(c1.elems.get(4).getNom(), "APPLE");
        } else {
            assertEquals(c1.elems.get(0).getNom(), "HP");
            assertEquals(c1.elems.get(1).getNom(), "ASUS");
            assertEquals(c1.elems.get(2).getNom(), "ALSTOM");
            assertEquals(c1.elems.get(3).getNom(), "APPLE");

        }
    }

    /**
     * Teste la mise à jour d'un element de la table computer.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void updateNomComputerTest() throws Exception {
        Company c1 = (Company) companyDao.findById(2L);
        c1.setNom("updateComputerTest");
        companyDao.update(c1);

        c1 = (Company) companyDao.findById(2L);
        Company c2 = (Company) companyDao.findById(2L);
        assertEquals("updateComputerTest", c1.getNom());

        assertEquals(c1.getId(), c2.getId());
        assertEquals(c1.getNom(), c2.getNom());

        c1.setNom(null);
        companyDao.update(c1);
        c1 = (Company) companyDao.findById(2L);
        assertEquals(null, c1.getNom());

        c1.setNom("HP");
        companyDao.update(c1);
        assertEquals(c1.getNom(), "HP");
    }

    /**
     * Teste la suppression d'un element de la table computer.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void deleteComputerTest() throws Exception {
        companyDao.delete(1L);
        Company c1 = (Company) companyDao.findById(1L);
        assertEquals(0L, (long) c1.getId());
    }
}
