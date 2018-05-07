package test.java.com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;

//import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

public class ComputerDAOTest {

    DAOFactory dao;
    ComputerDAO computerDao;
    final DateTimeFormatter frenchPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Initialisation du mock.
     */
    @Before
    public void setUp() {
        dao = DAOFactory.getInstance();
        /*try {
            dao.setConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        computerDao = new ComputerDAO();
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
        Optional<Computer> c1 = computerDao.findById(1L);
        assertEquals("c1", c1.get().getNom());
        assertEquals(c1.get().getIntroduced().toString(), "1971-01-01");
        assertEquals(c1.get().getDiscontinued(), null);
    }

    /**
     * Teste l'obtention de tout les element de la table computer.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void getAllComputerTest() throws Exception {
        Page<Computer> c1 = computerDao.findAll(0, Page.NO_LIMIT);
        /*Stream.of(c1.elems.stream(), c2.elems.stream()).flatMap(s -> s).forEach(s1 -> {
            System.out.println(s1);
        });*/
        if (c1.elems.get(0).getId() == 1) {
            assertEquals("c1", c1.elems.get(0).getNom());
            assertEquals("c2", c1.elems.get(1).getNom());
            assertEquals("c3", c1.elems.get(2).getNom());
            assertEquals("c4", c1.elems.get(3).getNom());
            assertEquals("c5", c1.elems.get(4).getNom());
            assertEquals("c6", c1.elems.get(5).getNom());
        } else {
            assertEquals("c2", c1.elems.get(0).getNom());
            assertEquals("c3", c1.elems.get(1).getNom());
            assertEquals("c4", c1.elems.get(2).getNom());
            assertEquals("c5", c1.elems.get(3).getNom());
            assertEquals("c6", c1.elems.get(4).getNom());
        }
    }

    /**
     * Teste la création d'un nouveau tuple dans la table computer via ComputerService.
     * @throws Exception
     *             Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void createComputerTest() throws Exception {
        Computer computer = new Computer();
        computer.setNom("c6");
        computerDao.create(computer);
        Computer c1 = computerDao.findComputerByName(0, 1, "c6").elems.get(0);
        assertEquals(c1.getId(), computer.getId());
        assertEquals(c1.getNom(), computer.getNom());
        assertEquals(c1.getIntroduced(), computer.getIntroduced());
        assertEquals(c1.getDiscontinued(), computer.getDiscontinued());
    }

    /**
     * Teste la mise à jour du champs Nom d'un element de la table computer.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void updateNomComputerTest() throws Exception {
        Optional<Computer> c1 = computerDao.findById(2L);
        c1.get().setNom("updateComputerTest");
        computerDao.update(c1.get());

        Optional<Computer> c2 = computerDao.findById(2L);
        assertEquals(c2.get().getId(), c1.get().getId());
        assertEquals("updateComputerTest", c2.get().getNom());

        c2.get().setNom("c2");
        computerDao.update(c2.get());
    }

    /**
     * Teste la mise à jour du champs Introduced d'un element de la table computer.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void updateIntroducedComputerTest() throws Exception {
        Optional<Computer> c1 = computerDao.findById(2L);
        c1.get().setIntroduced(LocalDate.parse("10/10/2018", frenchPattern));
        computerDao.update(c1.get());

        Optional<Computer> c2 = computerDao.findById(2L);
        assertEquals(c2.get().getId(), c1.get().getId());
        assertEquals(c2.get().getNom(), c1.get().getNom());
        assertEquals("2018-10-10", c2.get().getIntroduced().toString());
        assertEquals(c2.get().getDiscontinued().toString(), c1.get().getDiscontinued().toString());
    }

    /**
     * Teste la mise à jour du champs Discontinued d'un element de la table computer.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void updateDiscontinuedComputerTest() throws Exception {
        Optional<Computer> c1 = computerDao.findById(2L);
        c1.get().setDiscontinued(LocalDate.parse("10/12/2018", frenchPattern));
        computerDao.update(c1.get());

        Optional<Computer> c2 = computerDao.findById(2L);
        assertEquals(c2.get().getId(), c1.get().getId());
        assertEquals(c2.get().getNom(), c1.get().getNom());
        assertEquals(c2.get().getIntroduced().toString(), c1.get().getIntroduced().toString());
        assertEquals("2018-12-10", c2.get().getDiscontinued().toString());
    }

    /**
     * Teste la mise à jour du champs Introduced d'un element de la table computer à NULL.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void updateNullIntroducedComputerTest() throws Exception {
        Optional<Computer> c1 = computerDao.findById(2L);
        c1.get().setIntroduced(null);
        computerDao.update(c1.get());

        Optional<Computer> c2 = computerDao.findById(2L);
        assertEquals(c2.get().getId(), c1.get().getId());
        assertEquals(c2.get().getNom(), c1.get().getNom());
        assertEquals(null, c2.get().getIntroduced());
    }

    /**
     * Teste la mise à jour du champs Discontinued d'un element de la table computer à NULL.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void updateNullDiscontinuedComputerTest() throws Exception {
        Optional<Computer> c1 = computerDao.findById(2L);
        c1.get().setDiscontinued(null);
        computerDao.update(c1.get());

        Optional<Computer> c2 = computerDao.findById(2L);
        assertEquals(c2.get().getId(), c1.get().getId());
        assertEquals(c2.get().getNom(), c1.get().getNom());
        assertEquals(null, c1.get().getDiscontinued());
    }

    /**
     * Teste la mise à jour du champs Nom d'un element de la table computer à NULL.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void updateNullNomComputerTest() throws Exception {
        Optional<Computer> c1 = computerDao.findById(2L);
        c1.get().setNom(null);
        computerDao.update(c1.get());

        Optional<Computer> c2 = computerDao.findById(2L);
        assertEquals(c2.get().getId(), c1.get().getId());
        assertEquals(null, c2.get().getNom());
    }

    /**
     * Teste la mise à jour du champs Nom d'un element de la table computer à NULL.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void updateIdChangedInexistantComputerTest() throws Exception {
        Optional<Computer> c1 = computerDao.findById(2L);
        c1.get().setId(10);
        c1.get().setDiscontinued(LocalDate.parse("20/12/2018", frenchPattern));
        computerDao.update(c1.get());

        Optional<Computer> c2 = computerDao.findById(10);
        assertEquals(false, c2.isPresent());
    }

    /**
     * Teste la mise à jour du champs Nom d'un element de la table computer à NULL.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void updateIdChangedExistantComputerTest() throws Exception {
        Optional<Computer> c1 = computerDao.findById(2L);
        c1.get().setId(5);
        c1.get().setIntroduced(LocalDate.parse("20/12/2018", frenchPattern));
        computerDao.update(c1.get());

        Optional<Computer> c2 = computerDao.findById(5);
        assertEquals(c1.get().getId(), c2.get().getId());
        assertEquals("2018-12-20", c2.get().getIntroduced().toString());

        c2.get().setNom("c5");
        computerDao.update(c2.get());
    }

    /**
     * Teste l'obtention des elements de la table computer qui dependent d'une company.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void getComputersByCompanyIdTest() throws Exception {
        Page<Computer> c1 = computerDao.findByCompanyId(0, Page.NO_LIMIT, 2L);
        assertEquals(2, c1.elems.size());

        assertEquals("c3", c1.elems.get(0).getNom());
        assertEquals("c4", c1.elems.get(1).getNom());
    }

    /**
     * Teste la suppression d'un element de la table computer.
     * @throws Exception Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void deleteComputerTest() throws Exception {
        computerDao.delete(1L);
        Optional<Computer> c1 = computerDao.findById(1L);
        assertEquals(false, c1.isPresent());
    }
}
