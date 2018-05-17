package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;

import main.java.com.excilys.cdb.exception.DAOException;
import main.java.com.excilys.cdb.mapper.CompanyMapper;
import main.java.com.excilys.cdb.mapper.ComputerMapper;
import main.java.com.excilys.cdb.mapper.Mapper;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.persistance.Datasource;
import main.java.com.excilys.cdb.utils.Page;

public class ComputerDAO extends ModelDAO {

    private static final String SQL_SELECT_PAR_ID = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = ?;";
    private static final String SQL_SELECT_ALL = "SELECT id, name, introduced, discontinued, company_id FROM computer LIMIT ? OFFSET ?;";
    private static final String SQL_COUNT = "SELECT COUNT(*) as number FROM computer;";
    private static final String SQL_SELECT_ALL_NOLIMIT = "SELECT id, name, introduced, discontinued FROM computer;";
    private static final String SQL_SELECT_BY_COMPANY = "SELECT computer.id as id, company.name as company_name, computer.name as name, introduced, discontinued FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE company_id = ?;";
    private static final String SQL_SELECT_COMPANY_OF_COMPUTER = "SELECT company.id as id, company.name as name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE computer.id = ?;";
    private static final String SQL_INSERT = "INSERT INTO computer (name, introduced, discontinued, company_id) values (?, ?, ?, ?);";
    private static final String SQL_UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
    private static final String SQL_DELETE = "DELETE FROM computer WHERE id = ?;";
    private static final String SQL_SEARCH_BY_NAME = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name LIKE ? LIMIT ? OFFSET ?;";
    private static final String SQL_SEARCH_BY_COMPANY_NAME = "SELECT computer.id, computer.name, introduced, discontinued, company_id FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE company.name LIKE ? LIMIT ? OFFSET ?;";
    private static final String SQL_COUNT_BY_NAME = "SELECT COUNT(*) as number FROM computer WHERE name LIKE ?;";
    private static final String SQL_COUNT_BY_COMPANY_NAME = "SELECT COUNT(*) as number FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE company.name LIKE ?;";

	public JdbcTemplate jdbcTemplate;
    
    private Mapper mapper;
    private Mapper mapperCompany;

    /**
     * Création d'une ComputerDAO à l'aide d'une DAO.
     */
    public ComputerDAO() {
    	super();
        this.mapper = new ComputerMapper();
        this.mapperCompany = new CompanyMapper();
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
        this.jdbcTemplate = jdbcTemplate;  
    }  
    
    @Override
    public Optional<Long> create(Model model) throws DAOException {

        Optional<Long> id = Optional.empty();

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_INSERT, true,
             ((Computer) model).getNom(), ((Computer) model).getIntroduced(),
             ((Computer) model).getDiscontinued(), ((Computer) model).getCompanyId());) {


            int statut = preparedStatement.executeUpdate();

            if (statut == 0) {
                throw new DAOException("Insertion non possible");
            }

            ResultSet idAuto = preparedStatement.getGeneratedKeys();

            if (idAuto.next()) {
                model.setId(idAuto.getLong(1));
                id = Optional.of(model.getId());
            } else {
                throw new DAOException("Probleme dans la récupération de l'id du tuple nouvellement inseré.");
            }
            idAuto.close();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.create.SQL");
            logger.debug("Probleme de connection lors de la création de l'element dans la table company.");
        }

        return id;
    }

    @Override
    public Optional<Computer> findById(long id) {

        Optional<Computer> computer = Optional.empty();

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_PAR_ID, false, id);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            if (resultSet.next()) {
                computer = Optional.of((Computer) mapper.map(resultSet));
            }
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.findById.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element dans la table company.");
        }

        return  computer;/*
        
        return Optional.of(jdbcTemplate.query(SQL_SELECT_PAR_ID,  new PreparedStatementSetter() {
 		   
 		   public void setValues(PreparedStatement preparedStatement) throws SQLException {
 		      preparedStatement.setLong(1, id);
 		   }
        }, new ComputerMapper()).get(0));*/
    }

    /**
     * Trouver un ensemble de computers en fonction d'une company.
     * @param offset Determine à partir de quel element en commence à stocker ce qui sera retourné
     * @param nbElem Nombre d'element à retourner
     * @param id Identifiant de la company à partir de laquelle on obtient l'ensemble de computers
     * @return l'ensemble de computers
     */
    public Page<Computer> findByCompanyId(int offset, int nbElem, long id) {

        Page<Computer> p = new Page<Computer>(offset, nbElem);

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_BY_COMPANY, false, id);
             ResultSet resultSet = preparedStatement.executeQuery();) {

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
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.findByCompanyId.SQL");
            logger.debug("Probleme de connection lors de la recherche des elements dans la table company.");
        }

        return p;
    }

    @Override
    public Page<Computer> findAll(int offset, int nbElem) {

        Page<Computer> p = new Page<Computer>(offset, nbElem);

        if (nbElem == Page.NO_LIMIT) {
            try (Connection connexion = Datasource.getConnexion();
                 PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL_NOLIMIT, false);
                 ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    p.addElem((Computer) mapper.map(resultSet));
                }
            } catch (SQLException e) {
                Logger logger = LoggerFactory.getLogger("ComputerDAO.findAll.SQL");
                logger.debug("Probleme de connection lors de la recherche de tout les elements dans la table company.");
            }
        } else {
            try (Connection connexion = Datasource.getConnexion();
                 PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_ALL, false, nbElem, offset);
                 ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    p.addElem((Computer) mapper.map(resultSet));
                }
            } catch (SQLException e) {
                Logger logger = LoggerFactory.getLogger("ComputerDAO.findAll.SQL");
                logger.debug("Probleme de connection lors de la recherche de tout les elements dans la table company.");
            }
        }
        return p;
    }

    @Override
    public void update(Model m) {

        java.sql.Date dateIntroDB = null, dateDisDB = null;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            if (((Computer) m).getIntroduced() != null) {
                java.util.Date dateIntro = formatter.parse(((Computer) m).getIntroduced().toString());
                dateIntroDB = new java.sql.Date(dateIntro.getTime());
            }

            if (((Computer) m).getDiscontinued() != null) {
                java.util.Date dateDis = formatter.parse(((Computer) m).getDiscontinued().toString());
                dateDisDB = new java.sql.Date(dateDis.getTime());
            }
        } catch (ParseException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.update.Parse");
            logger.debug("Parse erreur");
        }

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_UPDATE, false, m.getNom(),
                dateIntroDB, dateDisDB, ((Computer) m).getCompanyId(), m.getId());) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.update.SQL");
            logger.debug("Probleme de connection lors de la mise à jour de l'element dans la table computer.");
        }
    }

    /**
     * Permet la suppression d'un tuple.
     * @param id
     *            identitifiant du tuple à supprimer.
     * @throws DAOException Envoyé si rien trouvé.
     */
    public void delete(long id) throws DAOException {

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_DELETE, false, id);) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.delete.SQL");
            logger.debug("Probleme de connection lors de la suppression d'un element dans la table computer.");
        }
    }

    /**
     * Obtenir la company qui à crée le computer.
     * @param id Identifiant du computer
     * @return Une company
     */
    public Optional<Company> findCompanyLink(long id) {

        Optional<Company> company = Optional.empty();

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SELECT_COMPANY_OF_COMPUTER, false, id);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            if (resultSet.next()) {
                company = Optional.of((Company) mapperCompany.map(resultSet));
            }
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.findCompanyLink.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element dans la table computer.");
        }

        return  company;
    }
    
    /**
     * Obtenir le nombre de computer dans la base de données.
     * @return Un nombre de computer
     */
    public Long getCount() {

        Long number = null;

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_COUNT, false);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                number = resultSet.getLong("number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logger logger = LoggerFactory.getLogger("ComputerDAO.getCount.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element dans la table computer.");
        }

        return number;
    }

    /**
     * Obtenir le nombre de computer dans la base de données.
     * @param parameter Une chaine de caractere
     * @return Un nombre de computer
     */
    public Long getCountByName(String parameter) {

        Long number = null;

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_COUNT_BY_NAME, false, "%" + parameter + "%");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                number = resultSet.getLong("number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logger logger = LoggerFactory.getLogger("ComputerDAO.getCount.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element dans la table computer.");
        }

        return number;
    }

    /**
     * Obtenir le nombre de computer dans la base de données.
     * @param parameter Une chaine de caractere
     * @return Un nombre de computer
     */
    public Long getCountByCompanyName(String parameter) {

        Long number = null;

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_COUNT_BY_COMPANY_NAME, false, "%" + parameter + "%");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                number = resultSet.getLong("number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logger logger = LoggerFactory.getLogger("ComputerDAO.getCount.SQL");
            logger.debug("Probleme de connection lors de la recherche de l'element dans la table computer.");
        }

        return number;
    }

    /**
     * Récuperation des computers dont le nom est similaire à la chaine parameter.
     * @param offset Determine à partir de quel element en commence à stocker ce qui sera retourné
     * @param nbElem Nombre d'element à retourner
     * @param parameter Une chaine de caractere
     * @return Une page
     */
    public Page<Computer> findComputerByName(int offset, int nbElem, String parameter) {
        Page<Computer> p = new Page<Computer>(offset, nbElem);

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SEARCH_BY_NAME, false, "%" + parameter + "%", nbElem, offset);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                p.addElem((Computer) mapper.map(resultSet));
            }
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.findComputerByName.SQL");
            logger.debug("Probleme de connection lors de la recherche de tout les elements dans la table computer.", e);
        }

        return p;
    }

    /**
     * Récuperation des computers dont le nom de company est similaire à la chaine parameter.
     * @param offset Determine à partir de quel element en commence à stocker ce qui sera retourné
     * @param nbElem Nombre d'element à retourner
     * @param parameter Une chaine de caractere
     * @return Une page
     */
    public Page<Computer> findComputerByCompany(int offset, int nbElem, String parameter) {
        Page<Computer> p = new Page<Computer>(offset, nbElem);

        try (Connection connexion = Datasource.getConnexion();
             PreparedStatement preparedStatement = ModelDAO.initialisationRequetePreparee(connexion, SQL_SEARCH_BY_COMPANY_NAME, false, "%" + parameter + "%", nbElem, offset);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                p.addElem((Computer) mapper.map(resultSet));
            }
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger("ComputerDAO.findComputerByName.SQL");
            logger.debug("Probleme de connection lors de la recherche de tout les elements dans la table computer.", e);
        }

        return p;
    }
}
