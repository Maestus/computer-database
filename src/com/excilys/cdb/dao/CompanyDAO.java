package com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Model;
import com.mysql.jdbc.PreparedStatement;

public class CompanyDAO implements ModelDAO {

	private static final String SQL_SELECT_PAR_ID = "SELECT id, name FROM company WHERE id = ?;";

	private DAOFactory daoFactory;

	public CompanyDAO(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public boolean create(Model model) {
		return false;
	}

	@Override
	public Company map(ResultSet resultSet) throws SQLException {
		Company company = new Company();
		company.setId(resultSet.getLong("id"));
		company.setNom(resultSet.getString("name"));
		return company;
	}

	@Override
	public Model find(long id) {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Company company = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_PAR_ID, false, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				company = map(resultSet);
			}
			resultSet.close();
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			System.err.println("DAO find");
			e.printStackTrace();
		}
		
		return company;
	}

}
