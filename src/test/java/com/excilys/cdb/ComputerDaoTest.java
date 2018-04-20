package test.java.com.excilys.cdb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mysql.jdbc.PreparedStatement;

import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.model.Computer;

@RunWith(MockitoJUnitRunner.class)
public class ComputerDaoTest {

    @Mock
    private DAOFactory daoFactory = DAOFactory.getInstance();

    @Mock
    private PreparedStatement stmt;

    @Mock
    private Connection c;
    
    @Mock
    private ResultSet rs;

    @Mock
    private Computer computer;

	final DateTimeFormatter frenchPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");			

    
    @Before
    public void setUp() throws Exception {

        assertNotNull(daoFactory);
        
        daoFactory.setConnection();

        //when(daoFactory.getConnection().prepareStatement(any(String.class))).thenReturn(stmt);

        when(daoFactory.getConnection()).thenReturn(c);

        computer = new Computer();

        computer.setId(1);

        computer.setNom("Johannes");

        computer.setIntroduced(LocalDate.parse("20/10/2010", frenchPattern));
        
        computer.setDiscontinued(LocalDate.parse("20/12/2010", frenchPattern));

        when(rs.first()).thenReturn(true);

        when(rs.getInt(1)).thenReturn(1);

        when(rs.getString(2)).thenReturn(computer.getNom());

        when(rs.getTimestamp(3).toLocalDateTime().toLocalDate()).thenReturn(computer.getIntroduced());

        when(rs.getTimestamp(4).toLocalDateTime().toLocalDate()).thenReturn(computer.getDiscontinued());

        when(stmt.executeQuery()).thenReturn(rs);

    }

    @Test(expected=IllegalArgumentException.class)
    public void nullCreateThrowsException() {

        new ComputerDAO(daoFactory).create(null);

    }

    @Test
    public void createComputer() {

        new ComputerDAO(daoFactory).create(computer);

    }

    @Test
    public void createAndRetrieveComputer() throws Exception {

        ComputerDAO dao = new ComputerDAO(daoFactory);

        dao.create(computer);

        Computer r = (Computer) dao.findById(1L);

        assertEquals(computer, r);

    }
}
