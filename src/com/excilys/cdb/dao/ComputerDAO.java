package com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Model;
import com.mysql.jdbc.PreparedStatement;

public class ComputerDAO implements ModelDAO {

	private static final String SQL_SELECT_PAR_ID = "SELECT id, name, introduced, discontinued FROM computer WHERE id = ?;";
	
	private DAOFactory daoFactory;

	public ComputerDAO(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public boolean create(Model model) {
		return false;
	}

	@Override
	public Model find(long id) {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Computer computer = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_PAR_ID, false, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				computer = map(resultSet);
			}
			resultSet.close();
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			System.err.println("DAO find");
			e.printStackTrace();
		}
		
		return computer;
	}

	@Override
	public Computer map(ResultSet resultSet) throws SQLException {
		Computer computer = new Computer();
		computer.setId(resultSet.getLong("id"));
		computer.setNom(resultSet.getString("name"));
		computer.setIntroduced(resultSet.getTimestamp("introduced"));
		computer.setDiscontinued(resultSet.getTimestamp("discontinued"));
		return computer;
	}

}
