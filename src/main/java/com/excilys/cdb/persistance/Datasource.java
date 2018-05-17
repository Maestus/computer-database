package main.java.com.excilys.cdb.persistance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Datasource {

    static HikariConfig cfg;
    static Optional<HikariDataSource> dataSource = Optional.empty();

    private static final String FILE_PROPERTIES = "spring-servlet.xml";
	private static ApplicationContext context;

    /**
     * Pour initialiser la datasource.
     */
    public static void init() {

        context = new ClassPathXmlApplicationContext(FILE_PROPERTIES);
    	dataSource = Optional.of((HikariDataSource) context.getBean("dataSource"));

    }

    /**
     * Pour obtenir une connection à la base de données.
     * @return Une Connection
     */
    public static Connection getConnexion() {
        Connection connection = null;
        
        if (!dataSource.isPresent()) {
        	init();
        }
        
        try {
            connection = dataSource.get().getConnection();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Datasource.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return connection;
    }
}

/*
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
 
@Configuration
public class HikariT {
    @Bean
    public DataSource dataSource() {
    	// initialize Spring's Application context
    	ApplicationContext context = new ClassPathXmlApplicationContext("spring-core-config.xml");
            // get bean declared with name "dataSource" in the configuration file
    	DriverManagerDataSource dataSource = (DriverManagerDataSource) context.getBean("dataSource");
            // get connection
    	Connection connection = dataSource.getConnection();
            // let's assume table name is student
    	PreparedStatement preparedStatement = connection.prepareStatement("Select * from student");
    	ResultSet resultSet = preparedStatement.executeQuery();
    	while (resultSet.next()) {
                    // student table has a column name
    		String name = resultSet.getString("name");
                    // student table also has a column rollno
    		int rollNumber = resultSet.getInt("rollno");
    		System.out.println("Name of student :: " + name);
    		System.out.println("Roll Number of student ::" + rollNumber);
    	}
            resultSet.close();
    	connection.close();
    }
 
    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }
 
    @Bean
    public EmployeeDAO employeeDAO(){
        EmployeeDAOImpl empDao = new EmployeeDAOImpl();
        empDao.setJdbcTemplate(jdbcTemplate());
        return empDao;
    }
 
}*/
 