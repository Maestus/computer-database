package com.excilys.cdb.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOFactory {


	private static final String fileProperties = "com/excilys/cdb/dao/dao.properties";
	private Properties props;
	private String url;

	private DAOFactory(String url, String userName, String password) 
	{
		this.url = url;
		this.props = new Properties();
		props.put("user", userName);
		props.put("password", password);
	}

	public static DAOFactory getInstance() 
	{
		Properties properties = new Properties();
		String url = null;
		String driver = null;
		String nomUtilisateur = null;
		String motDePasse = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream(fileProperties);


		if (fichierProperties == null) {
			System.out.println("Le fichier properties " + fileProperties + " est introuvable." );
		}

		try {
			properties.load(fichierProperties);
			url = properties.getProperty("url");
			driver = properties.getProperty("driver");
			nomUtilisateur = properties.getProperty("username");
			motDePasse = properties.getProperty("password");
			Class.forName(driver);
		} catch ( IOException e ) {
			System.out.println("Impossible de charger le fichier properties" + fileProperties);
			System.exit(1);
		} catch ( ClassNotFoundException e ) {
			System.out.println("Le driver est introuvable dans le classpath.");
			System.exit(1);
		}
		
		DAOFactory instance = new DAOFactory( url, nomUtilisateur, motDePasse );
		return instance;
	}
	
	public Connection getConnection() throws SQLException 
	{
        return DriverManager.getConnection(url, props);
    }

}
