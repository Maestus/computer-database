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
	private static final String SQL_SELECT_BY_COMPANY = "SELECT computer.id as id, company.name as company_name, computer.name as name, introduced, discontinued FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE company_id = ?;";
	private static final String SQL_INSERT = "INSERT INTO computer (name, introduced, discontinued, company_id) values (?, ?, ?, ?);";
	private static final String SQL_UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";

	private DAOFactory daoFactory;

	public ComputerDAO(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void create(Model model) throws DAOException {
	
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet idAuto = null;

		try {
			
			connexion = daoFactory.getConnection();
			preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_INSERT, true,
					((Computer) model).getNom(), ((Computer) model).getIntroduced(),
					((Computer) model).getDiscontinued(), ((Computer)model).getCompanyId());
			int statut = preparedStatement.executeUpdate();

			if (statut == 0) {
				throw new DAOException("Insertion non possible");
			}

			idAuto = preparedStatement.getGeneratedKeys();

			if (idAuto.next()) {
				model.setId(idAuto.getLong(1));
			} else {
				throw new DAOException("Probleme dans la récupération de l'id du tuple nouvellement inseré.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Model findById(long id) {
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
			//throw new DAOException("Impossible de trouver l'element demandé.", e);
			return null;
		}

		return computer;
	}
	
	public List<Computer> findByCompanyId(long id) {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Computer> computerList = new ArrayList<>();

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_BY_COMPANY, false, id);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				computerList.add(map(resultSet));
			}
			resultSet.close();
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			//throw new DAOException("Impossible de trouver l'element demandé.", e);
			return null;
		}

		return computerList;
	}

	public List<Computer> findAll() {
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
			e.printStackTrace();
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

	@Override
	public void update(Model m) {
		
		Connection connexion = null;
		PreparedStatement preparedStatement = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_UPDATE, false, m.getNom(),
					((Computer) m).getIntroduced(), ((Computer) m).getDiscontinued(), ((Computer) m).getCompanyId(),
					m.getId());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
