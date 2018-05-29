package test.java.com.excilys.cdb.service;

//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.ComputerService;

public class ComputerServiceTest {
    @Mock ComputerDAO cDao;
    @Mock ComputerService cServ;
    Computer computer;

    /**
     * Set les mocks.
     * @throws Exception Erreur d'initialisation du mock
     */
    @Before
    public void setUp() throws Exception {
         MockitoAnnotations.initMocks(this);
    }

    /**
     * Teste de la création d'un computer.
     */
    @Test
    public void createTest() {
        /*try {
            //when(cServ.addComputer(any(Computer.class))).thenReturn(1L);
            //when(cServ.checkDate(any(Computer.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        computer = new Computer();
        //computer.setNom("test");

        //assertEquals(cServ.checkDate(computer), true);
        //assertEquals(cServ.addComputer(computer), 1L);

        //verify(cServ, times(1)).addComputer(any(Computer.class));
        //verify(cServ, times(1)).checkDate(any(Computer.class));
}
}
