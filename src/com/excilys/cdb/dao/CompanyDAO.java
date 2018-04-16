package com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Model;
import com.mysql.jdbc.PreparedStatement;

public class CompanyDAO implements ModelDAO {

	private static final String SQL_SELECT_PAR_ID = "SELECT id, name FROM company WHERE id = ?;";
	private static final String SQL_SELECT_ALL = "SELECT id, name FROM company;";
	private static final String SQL_INSERT = "INSERT INTO company (name) values (?);";

	private DAOFactory daoFactory;

	public CompanyDAO(DAOFactory daoFactory) {
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
					((Company) model).getNom());
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
	public Company map(ResultSet resultSet) throws SQLException {
		Company company = new Company();
		company.setId(resultSet.getLong("id"));
		company.setNom(resultSet.getString("name"));
		return company;
	}

	@Override
	public Model find(long id) throws DAOException {
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
			throw new DAOException("Impossible de trouver l'element demandé.", e);

		}

		return company;
	}

	public List<Company> findAll() throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Company> companyList = new ArrayList<Company>();

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL, false);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				companyList.add(map(resultSet));
			}
			resultSet.close();
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			throw new DAOException("Probleme dans l'obtention de tout les tuples de la table company.", e);

		}

		return companyList;
	}

}
