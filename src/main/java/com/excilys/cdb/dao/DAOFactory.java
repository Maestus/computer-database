package main.java.com.excilys.cdb.dao;

//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Properties;

//import main.java.com.excilys.cdb.exception.DAOException;
//import main.java.com.excilys.cdb.hikari.HikariT;

public class DAOFactory {

    //private static final String FILE_PROPERTIES = "dao.properties";
    //private Properties props;
    //private String url;
    //static DAOFactory instance = new DAOFactory();
    //private Connection connection;
    //private static DAOFactory d;

    /**
     * Création d'une DAO factory. (method privé)
     * @param url
     *            une URL qui sera utilisé pour la connection.
     * @param userName
     *            Nom de l'utilisateur pour la connection à la base de données.
     * @param password
     *            Mot de passe pour la connection à la base de données.
     */
    /*private DAOFactory(String url, String userName, String password) {
        this.url = url;
        this.props = new Properties();
        props.put("user", userName);
        props.put("password", password);
    }*/

    /**
     * Constructeur par defaut.
     */
    /*private DAOFactory() {
    	HikariT.init();
        connection = HikariT.getConnexion();
    }*/


    /**
     * Permet l'initialisation d'une connexion.
     * @throws SQLException
     *             Methode suceptible de retourner une erreur.
     */
    /*public void setConnection() throws SQLException {
        this.connection = DriverManager.getConnection(url, props);
    }*/

    /**
     * Retourne une connexion.
     * @return une connection à la base de données.
     * @throws SQLException
     *             Erreur suceptible d'etre renvoyée.
     */
    /*public static Connection getConnection() {
        //instance.connection = HikariT.getConnexion();
        //return instance.connection;
    }*/
}
