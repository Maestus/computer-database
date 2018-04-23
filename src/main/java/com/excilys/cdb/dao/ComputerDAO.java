package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;

import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.utils.Page;

public class ComputerDAO implements ModelDAO {

    private static final String SQL_SELECT_PAR_ID = "SELECT id, name, introduced, discontinued FROM computer WHERE id = ?;";
    private static final String SQL_SELECT_ALL = "SELECT id, name, introduced, discontinued FROM computer LIMIT ? OFFSET ?;";
    private static final String SQL_SELECT_BY_COMPANY = "SELECT computer.id as id, company.name as company_name, computer.name as name, introduced, discontinued FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE company_id = ?;";
    private static final String SQL_INSERT = "INSERT INTO computer (name, introduced, discontinued, company_id) values (?, ?, ?, ?);";
    private static final String SQL_UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
    private static final String SQL_DELETE = "DELETE FROM computer WHERE id = ?;";

    private DAOFactory daoFactory;

    /**
     * Création d'une ComputerDAO à l'aide d'une DAO.
     * @param daoFactory
     *            DAO qui permet de ce connecter à la base de donnée.
     */
    public ComputerDAO(DAOFactory daoFactory) {
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
                    ((Computer) model).getNom(), ((Computer) model).getIntroduced(),
                    ((Computer) model).getDiscontinued(), ((Computer) model).getCompanyId());

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
        } catch (SQLException e) {
            // throw new DAOException("Impossible de trouver l'element demandé.", e);
            return new Computer();
        }

        return computer;
    }

    /**
     * Trouver un ensemble de computers en fonction d'une company.
     * @param offset Determine à partir de quel element en commence à stocker ce qui sera retourné
     * @param nbElem Nombre d'element à retourner
     * @param id Identifiant de la company à partir de laquelle on obtient l'ensemble de computers
     * @return l'ensemble de computers
     */
    public Page<Computer> findByCompanyId(int offset, int nbElem, long id) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Page<Computer> p = new Page<Computer>(offset, nbElem);

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_BY_COMPANY, false, id);
            resultSet = preparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next() && i < p.nbElem) {
                if (p.getOffset() == 0) {
                    p.addElem(map(resultSet));
                } else {
                    p.decrOffset();
                }
                i++;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            // throw new DAOException("Impossible de trouver l'element demandé.", e);
            return new Page<Computer>(0, 0);
        }

        return p;
    }

    @Override
    public Page<Computer> findAll(int offset, int nbElem) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Page<Computer> p = new Page<Computer>(offset, nbElem);

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
            e.printStackTrace();
        }

        return p;
    }

    @Override
    public Computer map(ResultSet resultSet) throws SQLException {
        Computer computer = new Computer();
        computer.setId(resultSet.getLong("id"));
        computer.setNom(resultSet.getString("name"));
        computer.setIntroduced(resultSet.getTimestamp("introduced") == null ? null
                : resultSet.getTimestamp("introduced").toLocalDateTime().toLocalDate());
        computer.setDiscontinued(resultSet.getTimestamp("discontinued") == null ? null
                : resultSet.getTimestamp("discontinued").toLocalDateTime().toLocalDate());
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet la suppression d'un tuple.
     * @param id
     *            identitifiant du tuple à supprimer.
     */
    public void delete(long id) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_DELETE, false, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new DAOException("Impossible de trouver l'element demandé.", e);
            // return new Computer();
        }
    }
}
