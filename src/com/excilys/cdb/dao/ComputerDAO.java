package com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Model;
import com.mysql.jdbc.PreparedStatement;

public class ComputerDAO implements ModelDAO {

	private static final String SQL_SELECT_PAR_ID = "SELECT id, name, introduced, discontinued FROM computer WHERE id = ?;";
	private static final String SQL_SELECT_ALL = "SELECT id, name, introduced, discontinued FROM computer;";
	private static final String SQL_INSERT = "INSERT INTO computer (name, introduced, discontinued, company_id) values (?, ?, ?, ?);";

	private DAOFactory daoFactory;

	public ComputerDAO(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void create(Model model) throws DAOException {
	
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;

		try {
			
			connexion = daoFactory.getConnection();
			preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_INSERT, true,
					((Computer) model).getNom(), ((Computer) model).getIntroduced(),
					((Computer) model).getDiscontinued(), ((Computer)model).getCompanyId());
			int statut = preparedStatement.executeUpdate();

			if (statut == 0) {
				throw new DAOException("Insertion non possible");
			}

			valeursAutoGenerees = preparedStatement.getGeneratedKeys();

			if (valeursAutoGenerees.next()) {
				model.setId(valeursAutoGenerees.getLong(1));
			} else {
				throw new DAOException("Probleme dans la récupération de l'id du tuple nouvellement inseré.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Model find(long id) throws DAOException {
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
			throw new DAOException("Impossible de trouver l'element demandé.", e);
		}

		return computer;
	}

	public List<Computer> findAll() throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Computer> computerList = new ArrayList<>();

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL, false);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				computerList.add(map(resultSet));
			}
			resultSet.close();
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			throw new DAOException("Probleme dans l'obtention de tout les tuples de la table computer.", e);
		}

		return computerList;
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
