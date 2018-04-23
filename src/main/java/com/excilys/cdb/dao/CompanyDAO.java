package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.utils.Page;

public class CompanyDAO implements ModelDAO {

    private static final String SQL_SELECT_PAR_ID = "SELECT id, name FROM company WHERE id = ?;";
    private static final String SQL_SELECT_ALL = "SELECT id, name FROM company LIMIT ? OFFSET ?;";
    private static final String SQL_INSERT = "INSERT INTO company (name) values (?);";
    private static final String SQL_UPDATE = "UPDATE company SET name = ? WHERE id = ?;";

    private DAOFactory daoFactory;

    /**
     * Création d'une CompanyDAO à l'aide d'une DAO.
     * @param daoFactory DAO qui permet de ce connecter à la base de donnée.
     */
    public CompanyDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public long create(Model model) throws Exception {
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

        return idAuto.getLong(1);
    }

    @Override
    public Company map(ResultSet resultSet) throws SQLException {
        Company company = new Company();
        company.setId(resultSet.getLong("id"));
        company.setNom(resultSet.getString("name"));
        return company;
    }

    @Override
    public Model findById(long id) throws DAOException {
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
        } catch (SQLException e) {
            // throw new DAOException("Impossible de trouver l'element demandé.", e);
            return null;
        }
        return company;
    }

    @Override
    public Page<Company> findAll(int offset, int nbElem) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Page<Company> p = new Page<Company>(offset, nbElem);

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL, false, nbElem,
                    offset);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                p.addElem(map(resultSet));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new DAOException("Probleme dans l'obtention de tout les tuples de la table company.", e);

        }

        return p;
    }

    @Override
    public void update(Model m) {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_UPDATE, false, m.getNom(),
                    m.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
