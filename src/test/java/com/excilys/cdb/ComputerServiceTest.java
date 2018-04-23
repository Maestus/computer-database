package test.java.com.excilys.cdb;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.ComputerService;

public class ComputerServiceTest {

    @InjectMocks
    private ComputerService computerService;
    DAOFactory dao = mock(DAOFactory.class);
    ComputerDAO computerDao = mock(ComputerDAO.class);
    @Mock
    public Computer computer;

    /**
     * Initialisation du mock.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Teste la création d'un nouveau tuple dans la table computer via ComputerDao
     * et ComputerService.
     * @throws Exception
     *             Peut generer une erreur de connection à la base de données.
     */
    @Test
    public void createComputerTest() throws Exception {
        when(computerDao.create(any(Computer.class))).thenReturn(1L);
        assertEquals(computerService.addComputer(computer), 1L);
    }
}
