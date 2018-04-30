package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import main.java.com.excilys.cdb.mapper.CompanyMapper;
import main.java.com.excilys.cdb.mapper.Mapper;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.utils.Page;

public class CompanyDAO implements ModelDAO {

    private static final String SQL_SELECT_PAR_ID = "SELECT id, name FROM company WHERE id = ?;";
    private static final String SQL_SELECT_ALL = "SELECT id, name FROM company LIMIT ? OFFSET ?;";
    private static final String SQL_SELECT_ALL_NOLIMIT = "SELECT id, name FROM company;";
    private static final String SQL_INSERT = "INSERT INTO company (name) values (?);";
    private static final String SQL_UPDATE = "UPDATE company SET name = ? WHERE id = ?;";
    private static final String SQL_DELETE = "DELETE FROM company WHERE id = ?;";

    private DAOFactory daoFactory;
    private Mapper mapper;

    /**
     * Création d'une CompanyDAO à l'aide d'une DAO.
     * @param daoFactory DAO qui permet de ce connecter à la base de donnée.
     */
    public CompanyDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.mapper = new CompanyMapper();
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
            Logger logger = LoggerFactory.getLogger("CompanyDAO.create.SQL");
            logger.debug("Erreur dans la connexion à la base de données.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
        }

        return idAuto.getLong(1);
    }

    @Override
    public Model findById(long id) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Company company = new Company();

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_PAR_ID, false, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                company = (Company) mapper.map(resultSet);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("CompanyDAO.findById.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element demandé.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
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
            if (nbElem == Page.NO_LIMIT) {
                preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL_NOLIMIT, false);
            } else {
                preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL, false, nbElem, offset);
            }
            resultSet = preparedStatement.executeQuery();
            if (nbElem != Page.NO_LIMIT) {
                while (resultSet.next()) {
                   p.addElem((Company) mapper.map(resultSet));
                }
            } else {
                while (resultSet.next()) {
                    p.addElem((Company) mapper.map(resultSet));
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("CompanyDAO.findAll.SQL");
            logger.debug("Probleme de connection lors de l'obtention de tout les tuples de la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
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
            Logger logger = LoggerFactory.getLogger("CompanyDAO.update.SQL");
            logger.debug("Probleme de connection lors de la mise à jour de l'element dans la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
        }
    }

    /**
     * Permet la suppression d'un tuple.
     * @param id
     *            identitifiant du tuple à supprimer.
     * @throws DAOException Envoyé si rien trouvé.
     */
    public void delete(long id) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_DELETE, false, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("CompanyDAO.update.SQL");
            logger.debug("Probleme de connection lors de la suppression de l'element dans la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
        }
    }
}
