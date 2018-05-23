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