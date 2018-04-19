package com.excilys.cdb.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOFactory implements AutoCloseable {

	private static final String fileProperties = "com/excilys/cdb/dao/dao.properties";
	private Properties props;
	private String url;
	static DAOFactory instance;
	private Connection connection;

	private DAOFactory(String url, String userName, String password) {
		this.url = url;
		this.props = new Properties();
		props.put("user", userName);
		props.put("password", password);
	}

	public static DAOFactory getInstance() {
		if(instance == null) {
			Properties properties = new Properties();
			String url = null;
			String driver = null;
			String nomUtilisateur = null;
			String motDePasse = null;
	
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream fichierProperties = classLoader.getResourceAsStream(fileProperties);
	
			if (fichierProperties == null) {
				throw new DAOException("Impossible de trouver le fichier " + fileProperties);
			}
	
			try {
				properties.load(fichierProperties);
				url = properties.getProperty("url");
				driver = properties.getProperty("driver");
				nomUtilisateur = properties.getProperty("username");
				motDePasse = properties.getProperty("password");
				Class.forName(driver);
			} catch (IOException e) {
				throw new DAOException("Impossible de charger le fichier " + fileProperties, e);
			} catch (ClassNotFoundException e) {
				throw new DAOException("Impossible de trouver le driver donné dans le fichier " + fileProperties);
			}
	
			instance = new DAOFactory(url, nomUtilisateur, motDePasse);
		}
		return instance;
	}

	public void setConnection() throws SQLException {
		 this.connection = DriverManager.getConnection(url, props);
	}
	
	public Connection getConnection() throws SQLException {
		 return connection;
	}

	@Override
	public void close() throws Exception {
		connection.close();
	}
}
