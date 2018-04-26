package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.PreparedStatement;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import main.java.com.excilys.cdb.mapper.CompanyMapper;
import main.java.com.excilys.cdb.mapper.ComputerMapper;
import main.java.com.excilys.cdb.mapper.Mapper;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.utils.Page;

public class ComputerDAO implements ModelDAO {

    private static final String SQL_SELECT_PAR_ID = "SELECT id, name, introduced, discontinued FROM computer WHERE id = ?;";
    private static final String SQL_SELECT_ALL = "SELECT id, name, introduced, discontinued FROM computer LIMIT ? OFFSET ?;";
    private static final String SQL_COUNT = "SELECT COUNT(*) as number FROM computer;";
    private static final String SQL_SELECT_ALL_NOLIMIT = "SELECT id, name, introduced, discontinued FROM computer;";
    private static final String SQL_SELECT_BY_COMPANY = "SELECT computer.id as id, company.name as company_name, computer.name as name, introduced, discontinued FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE company_id = ?;";
    private static final String SQL_SELECT_COMPANY_OF_COMPUTER = "SELECT company.id as id, company.name as name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE computer.id = ?;";
    private static final String SQL_INSERT = "INSERT INTO computer (name, introduced, discontinued, company_id) values (?, ?, ?, ?);";
    private static final String SQL_UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
    private static final String SQL_DELETE = "DELETE FROM computer WHERE id = ?;";

    private DAOFactory daoFactory;
    private Mapper mapper;
    private Mapper mapperCompany;

    /**
     * Création d'une ComputerDAO à l'aide d'une DAO.
     * @param daoFactory
     *            DAO qui permet de ce connecter à la base de donnée.
     */
    public ComputerDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.mapper = new ComputerMapper();
        this.mapperCompany = new CompanyMapper();
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
            Logger logger = LoggerFactory.getLogger("ComputerDAO.create.SQL");
            logger.debug("Probleme de connection lors de la création de l'element dans la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
        }
        return idAuto.getLong(1);

    }

    @Override
    public Model findById(long id) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Computer computer = new Computer();

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_PAR_ID, false, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                computer = (Computer) mapper.map(resultSet);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.findById.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element dans la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
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
            if (nbElem == Page.NO_LIMIT) {
                while (resultSet.next()) {
                    p.addElem((Computer) mapper.map(resultSet));
                }
            } else {
                while (resultSet.next() && i < p.nbElem) {
                    if (p.getOffset() == 0) {
                        p.addElem((Computer) mapper.map(resultSet));
                        i++;
                    } else {
                        p.decrOffset();
                    }
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.findByCompanyId.SQL");
            logger.debug("Probleme de connection lors de la recherche des elements dans la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
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
            if (nbElem == Page.NO_LIMIT) {
                preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL_NOLIMIT, false);
            } else {
                preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL, false, nbElem,
                    offset);
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                p.addElem((Computer) mapper.map(resultSet));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.findAll.SQL");
            logger.debug("Probleme de connection lors de la recherche de tout les elements dans la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
        }

        return p;
    }

    @Override
    public void update(Model m) {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        java.sql.Date dateIntroDB = null, dateDisDB = null;

        try {
            connexion = daoFactory.getConnection();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            if (((Computer) m).getIntroduced() != null) {
                java.util.Date dateIntro = formatter.parse(((Computer) m).getIntroduced().toString());
                dateIntroDB = new java.sql.Date(dateIntro.getTime());
            }

            if (((Computer) m).getDiscontinued() != null) {
                java.util.Date dateDis = formatter.parse(((Computer) m).getDiscontinued().toString());
                dateDisDB = new java.sql.Date(dateDis.getTime());
            }

            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_UPDATE, false, m.getNom(),
                    dateIntroDB, dateDisDB, ((Computer) m).getCompanyId(), m.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ParseException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.update.SQL");
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
            Logger logger = LoggerFactory.getLogger("ComputerDAO.delete.SQL");
            logger.debug("Probleme de connection lors de la suppression d'un element dans la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
        }
    }

    /**
     * Obtenir la company qui à crée le computer.
     * @param id Identifiant du computer
     * @return Une company
     */
    public Company findCompanyLink(long id) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Company company = new Company();

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_COMPANY_OF_COMPUTER, false, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                company = (Company) mapperCompany.map(resultSet);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.findCompanyLink.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element dans la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
        }

        return company;
    }

    /**
     * Obtenir le nombre de computer dans la base de données.
     * @return Un nombre de computer
     */
    public Long getCount() {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Long number = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_COUNT, false);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                number = resultSet.getLong("number");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger logger = LoggerFactory.getLogger("ComputerDAO.getCount.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element dans la table company.");

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
        }

        return number;
    }
}
