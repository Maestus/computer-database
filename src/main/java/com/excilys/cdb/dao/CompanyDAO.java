package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.exception.DAOException;
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

    private Mapper mapper;

    /**
     * Création d'une CompanyDAO à l'aide d'une DAO.
     */
    public CompanyDAO() {
        this.mapper = new CompanyMapper();
    }

    @Override
    public Optional<Long> create(Model model) throws Exception {
        ResultSet idAuto = null;
        Optional<Long> id = Optional.empty();

        try (Connection connexion = DAOFactory.getConnection();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_INSERT, true,
                     ((Company) model).getNom());) {

            int statut = preparedStatement.executeUpdate();

            if (statut == 0) {
                throw new DAOException("Insertion non possible");
            }

            idAuto = preparedStatement.getGeneratedKeys();

            if (idAuto.next()) {
                model.setId(idAuto.getLong(1));
                id = Optional.of(model.getId());
            } else {
                throw new DAOException("Probleme dans la récupération de l'id du tuple nouvellement inseré.");
            }

        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("CompanyDAO.create.SQL");
            logger.debug("Erreur dans la connexion à la base de données.");
        }

        return id;
    }

    @Override
    public Optional<Company> findById(long id) throws DAOException {
        ResultSet resultSet = null;
        Company company = null;

        try (Connection connexion = DAOFactory.getConnection();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_PAR_ID, false, id);) {
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                company = (Company) mapper.map(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("CompanyDAO.findById.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element demandé.");
        }
        return Optional.ofNullable(company);
    }

    @Override
    public Page<Company> findAll(int offset, int nbElem) throws DAOException {
        ResultSet resultSet = null;
        Page<Company> p = new Page<Company>(offset, nbElem);

        if (nbElem == Page.NO_LIMIT) {
            try (Connection connexion = DAOFactory.getConnection();
                 PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL_NOLIMIT, false);) {
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
            } catch (SQLException e) {
                Logger logger = LoggerFactory.getLogger("CompanyDAO.findAll.SQL");
                logger.debug("Probleme de connection lors de l'obtention de tout les tuples de la table company.");
            }
        } else {
            try (Connection connexion = DAOFactory.getConnection();
                 PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL, false, nbElem, offset);) {
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
            } catch (SQLException e) {
                   Logger logger = LoggerFactory.getLogger("CompanyDAO.findAll.SQL");
                   logger.debug("Probleme de connection lors de l'obtention de tout les tuples de la table company.");
            }
        }

        return p;
    }

    @Override
    public void update(Model m) {

        try (Connection connexion = DAOFactory.getConnection();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_UPDATE, false, m.getNom(),
                        m.getId());) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("CompanyDAO.update.SQL");
            logger.debug("Probleme de connection lors de la mise à jour de l'element dans la table company.");
        }
    }

    /**
     * Permet la suppression d'un tuple.
     * @param id
     *            identitifiant du tuple à supprimer.
     * @throws DAOException Envoyé si rien trouvé.
     */
    public void delete(long id) throws DAOException {

        try (Connection connexion = DAOFactory.getConnection();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_DELETE, false, id);) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("CompanyDAO.update.SQL");
            logger.debug("Probleme de connection lors de la suppression de l'element dans la table company.");
        }
    }
}
