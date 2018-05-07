package main.java.com.excilys.cdb.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HikariT {

    static HikariConfig cfg;
    static HikariDataSource ds;

    private static final String FILE_PROPERTIES = "dao.properties";

    /**
     * Pour initialiser la datasource.
     */
    public static void init() {

        ClassLoader classLoader = new HikariT().getClass().getClassLoader();

        URL fichierProperties = classLoader.getResource(FILE_PROPERTIES);
        cfg = new HikariConfig(fichierProperties.getFile());
        ds = new HikariDataSource(cfg);
    }

    /**
     * Pour obtenir une connection à la base de données.
     * @return Une Connection
     */
    public static Connection getConnexion() {
        Connection connection = null;

        try {
            connection = ds.getConnection();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(HikariT.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return connection;
    }
}