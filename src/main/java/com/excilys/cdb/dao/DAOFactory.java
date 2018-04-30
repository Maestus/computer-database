package main.java.com.excilys.cdb.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOFactory implements AutoCloseable {

    private static final String FILE_PROPERTIES = "dao.properties";
    private Properties props;
    private String url;
    static DAOFactory instance;
    private Connection connection;
    private static DAOFactory d;

    /**
     * Création d'une DAO factory. (method privé)
     * @param url
     *            une URL qui sera utilisé pour la connection.
     * @param userName
     *            Nom de l'utilisateur pour la connection à la base de données.
     * @param password
     *            Mot de passe pour la connection à la base de données.
     */
    private DAOFactory(String url, String userName, String password) {
        this.url = url;
        this.props = new Properties();
        props.put("user", userName);
        props.put("password", password);
    }

    /**
     * Constructeur par defaut.
     */
    private DAOFactory() {
    }

    /**
     * Construit la DAO en appelant le constructeur.
     * @return une DAO Factory.
     */
    public static DAOFactory getInstance() {
        if (instance == null) {
            Properties properties = new Properties();
            String url = null;
            String driver = null;
            String nomUtilisateur = null;
            String motDePasse = null;

            d = new DAOFactory();
            ClassLoader classLoader = d.getClass().getClassLoader();

            InputStream fichierProperties = classLoader.getResourceAsStream(FILE_PROPERTIES);

            if (fichierProperties == null) {
                throw new DAOException("Impossible de trouver le fichier " + FILE_PROPERTIES);
            }

            try {
                properties.load(fichierProperties);
                url = properties.getProperty("url");
                driver = properties.getProperty("driver");
                nomUtilisateur = properties.getProperty("username");
                motDePasse = properties.getProperty("password");
                Class.forName(driver);
            } catch (IOException e) {
                throw new DAOException("Impossible de charger le fichier " + FILE_PROPERTIES, e);
            } catch (ClassNotFoundException e) {
                throw new DAOException("Impossible de trouver le driver donné dans le fichier " + FILE_PROPERTIES);
            }

            instance = new DAOFactory(url, nomUtilisateur, motDePasse);
        }
        return instance;
    }

    /**
     * Permet l'initialisation d'une connexion.
     * @throws SQLException
     *             Methode suceptible de retourner une erreur.
     */
    public void setConnection() throws SQLException {
        this.connection = DriverManager.getConnection(url, props);
    }

    /**
     * Retourne une connexion.
     * @return une connection à la base de données.
     * @throws SQLException
     *             Erreur suceptible d'etre renvoyée.
     */
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
