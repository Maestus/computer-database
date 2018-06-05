package test.java.com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.utils.Page;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CompanyDAOTest {

	public static ApplicationContext context;
	CompanyDAO companyDao;

	final DateTimeFormatter frenchPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	/**
	 * Initialisation du mock.
	 */
	@Before
	public void setUp() {
		context = new ClassPathXmlApplicationContext("application-context.xml");
		companyDao = (CompanyDAO) context.getBean("companyDao");
		
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Teste l'obtention d'un tuple dans la table company via CompanyDao.
	 * 
	 * @throws Exception
	 *             Peut generer une erreur de connection à la base de données.
	 */
	@Test
	public void getCompanyTest() throws Exception {
		Optional<Company> c1 = companyDao.findById(1L);
		assertEquals(true, c1.isPresent());
		assertEquals("ACER", c1.get().getName());
	}

	/**
	 * Teste la création d'un nouveau tuple dans la table computer via
	 * ComputerService.
	 * 
	 * @throws Exception
	 *             Peut generer une erreur de connection à la base de données.
	 */
	@Test
	public void createCompanyTest() throws Exception {
		Company company = new Company();
		company.setName("Testing");
		long i = companyDao.create(company).get();
		Optional<Company> c1 = companyDao.findById(6L);
		assertEquals(c1.get().getName(), company.getName());
		companyDao.delete(i);
	}

	/**
	 * Teste la mise à jour d'un element de la table computer.
	 * 
	 * @throws Exception
	 *             Peut generer une erreur de connection à la base de données.
	 */
	@Test
	public void updateNomComputerTest() throws Exception {
		Optional<Company> c1 = companyDao.findById(2L);
		assertEquals(true, c1.isPresent());
		c1.get().setName("updateComputerTest");
		companyDao.update(c1.get());

		c1 = companyDao.findById(2L);
		Optional<Company> c2 = companyDao.findById(2L);
		assertEquals("updateComputerTest", c1.get().getName());

		assertEquals(c1.get().getId(), c2.get().getId());
		assertEquals(c1.get().getName(), c2.get().getName());

		c1.get().setName(null);
		companyDao.update(c1.get());
		c1 = companyDao.findById(2L);
		assertEquals(null, c1.get().getName());

		c1.get().setName("HP");
		companyDao.update(c1.get());
		assertEquals(c1.get().getName(), "HP");
	}

	/**
	 * Teste l'obtention de tout les element de la table computer.
	 * 
	 * @throws Exception
	 *             Peut generer une erreur de connection à la base de données.
	 */
	@Test
	public void getAllComputerTest() throws Exception {
		Page<Company> c1 = companyDao.findAll(0, Page.NO_LIMIT);

		if (c1.elems.get(0).getId() == 1) {
			assertEquals(c1.elems.get(0).getName(), "ACER");
			assertEquals(c1.elems.get(1).getName(), "HP");
			assertEquals(c1.elems.get(2).getName(), "ASUS");
			assertEquals(c1.elems.get(3).getName(), "ALSTOM");
			assertEquals(c1.elems.get(4).getName(), "APPLE");
		} else {
			assertEquals(c1.elems.get(0).getName(), "HP");
			assertEquals(c1.elems.get(1).getName(), "ASUS");
			assertEquals(c1.elems.get(2).getName(), "ALSTOM");
			assertEquals(c1.elems.get(3).getName(), "APPLE");

		}
	}

	/**
	 * Teste la suppression d'un element de la table computer.
	 * 
	 * @throws Exception
	 *             Peut generer une erreur de connection à la base de données.
	 */
	@Test
	public void deleteComputerTest() throws Exception {
		companyDao.delete(1L);
		Optional<Company> c1 = companyDao.findById(1L);
		assertEquals(false, c1.isPresent());
	}
}
