package test.java.com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;

//import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
        //dao = DAOFactory.getInstance();
        /*try {
            dao.setConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        companyDao = new CompanyDAO();
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
        Optional<Company> c1 = companyDao.findById(1L);
        assertEquals(true, c1.isPresent());
        assertEquals("ACER", c1.get().getNom());
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
        long i = companyDao.create(computer).get();
        Optional<Company> c1 = companyDao.findById(6L);
        assertEquals(c1.get().getNom(), computer.getNom());
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
        Optional<Company> c1 = companyDao.findById(2L);
        assertEquals(true, c1.isPresent());
        c1.get().setNom("updateComputerTest");
        companyDao.update(c1.get());

        c1 = companyDao.findById(2L);
        Optional<Company> c2 = companyDao.findById(2L);
        assertEquals("updateComputerTest", c1.get().getNom());

        assertEquals(c1.get().getId(), c2.get().getId());
        assertEquals(c1.get().getNom(), c2.get().getNom());

        c1.get().setNom(null);
        companyDao.update(c1.get());
        c1 = companyDao.findById(2L);
        assertEquals(null, c1.get().getNom());

        c1.get().setNom("HP");
        companyDao.update(c1.get());
        assertEquals(c1.get().getNom(), "HP");
    }

    /**
     * Teste la suppression d'un element de la table computer.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void deleteComputerTest() throws Exception {
        companyDao.delete(1L);
        Optional<Company> c1 = companyDao.findById(1L);
        assertEquals(false, c1.isPresent());
    }
}
