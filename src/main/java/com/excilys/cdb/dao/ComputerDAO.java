package main.java.com.excilys.cdb.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import main.java.com.excilys.cdb.exception.DAOException;
import main.java.com.excilys.cdb.mapper.CompanyMapper;
import main.java.com.excilys.cdb.mapper.ComputerMapper;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.utils.Page;

public class ComputerDAO extends ModelDAO {

	private static final String SQL_SELECT_PAR_ID = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = ?;";
	private static final String SQL_SELECT_ALL = "SELECT id, name, introduced, discontinued, company_id FROM computer LIMIT ? OFFSET ?;";
	private static final String SQL_COUNT = "SELECT COUNT(*) as number FROM computer;";
	private static final String SQL_SELECT_ALL_NOLIMIT = "SELECT id, name, introduced, discontinued, company_id FROM computer;";
	private static final String SQL_SELECT_BY_COMPANY = "SELECT computer.id as id, company.name as company_name, computer.name as name, introduced, discontinued, company_id FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE company_id = ?;";
	private static final String SQL_SELECT_COMPANY_OF_COMPUTER = "SELECT company.id as id, company.name as name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE computer.id = ?;";
	private static final String SQL_UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
	private static final String SQL_DELETE = "DELETE FROM computer WHERE id = ?;";
	private static final String SQL_SEARCH_BY_NAME = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name LIKE ? LIMIT ? OFFSET ?;";
	private static final String SQL_SEARCH_BY_COMPANY_NAME = "SELECT computer.id, computer.name, introduced, discontinued, company_id FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE company.name LIKE ? LIMIT ? OFFSET ?;";
	private static final String SQL_COUNT_BY_NAME = "SELECT COUNT(*) as number FROM computer WHERE name LIKE ?;";
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDAO.class);

	public JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Optional<Long> create(Model model) throws DAOException {

		Optional<Long> res = Optional.empty();
		
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("computer")
				.usingGeneratedKeyColumns("id");
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("name", ((Computer) model).getName());
		
		if (((Computer) model).getIntroduced() != null) {
			parameters.put("introduced", Timestamp.valueOf(((Computer) model).getIntroduced().atStartOfDay()));
		}
		
		if (((Computer) model).getDiscontinued() != null) {
			parameters.put("discontinued", Timestamp.valueOf(((Computer) model).getDiscontinued().atStartOfDay()));
		}
		
		parameters.put("company_id", ((Computer) model).getCompanyId());
		
		try {
			res = Optional.ofNullable((Long) jdbcInsert.executeAndReturnKey(parameters));
		} catch (Exception e) {
			LOGGER.debug("[create] Probleme lors de la création de l'element.", e);
		}
		
		return res;
	}

	@Override
	public Optional<Computer> findById(long id) {

		try {
			return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_PAR_ID, new Long[] { id }, new ComputerMapper()));
		} catch (DataAccessException dae) {
			LOGGER.debug("[findById] Probleme lors de la recherche de l'element.", dae);
		}

		return Optional.empty();
	}

	/**
	 * Trouver un ensemble de computers en fonction d'une company.
	 * 
	 * @param offset
	 *            Determine à partir de quel element en commence à stocker ce qui
	 *            sera retourné
	 * @param nbElem
	 *            Nombre d'element à retourner
	 * @param id
	 *            Identifiant de la company à partir de laquelle on obtient
	 *            l'ensemble de computers
	 * @return l'ensemble de computers
	 */
	public Page<Computer> findByCompanyId(int offset, int nbElem, long id) {

		Page<Computer> p = new Page<Computer>(offset, nbElem);
		try {
			p.elems = jdbcTemplate.query(SQL_SELECT_BY_COMPANY, new Long[] { id }, new ComputerMapper());
		} catch (DataAccessException dae) {
			LOGGER.debug("[findByCompanyId] Probleme lors de la recherche de l'element.", dae);			
		}
		return p;
	}

	@Override
	public Page<Computer> findAll(int offset, int nbElem) {

		Page<Computer> p = new Page<Computer>(offset, nbElem);
		
		try {
			if (nbElem == Page.NO_LIMIT) {
				p.elems = jdbcTemplate.query(SQL_SELECT_ALL_NOLIMIT, new ComputerMapper());
			} else {
				p.elems = jdbcTemplate.query(SQL_SELECT_ALL, new Integer[] { nbElem, offset }, new ComputerMapper());
			}
		} catch (DataAccessException dae) {
			LOGGER.debug("[findAll] Probleme lors de la recherche des elements.", dae);
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
			LOGGER.debug("[update] Parse erreur");
		}

		try {
			jdbcTemplate.update(SQL_UPDATE, ((Computer)m).getName(), dateIntroDB, dateDisDB, ((Computer) m).getCompanyId(), ((Computer)m).getId());
		} catch (DataAccessException dae) {
			LOGGER.debug("[update] Probleme lors de la mise à jour de l'element.", dae);
		}
	}

	/**
	 * Permet la suppression d'un tuple.
	 * 
	 * @param id
	 *            identitifiant du tuple à supprimer.
	 * @throws DAOException
	 *             Envoyé si rien trouvé.
	 */
	public void delete(Object[] elems) throws DAOException {

		try {
			jdbcTemplate.batchUpdate(SQL_DELETE,
				Arrays.asList(elems).stream().map(n -> new Object[] { n }).collect(Collectors.toList()));
		} catch (DataAccessException dae) {
			LOGGER.debug("[delete] Probleme lors de la suppression de l'element.", dae);
		}
	}

	/**
	 * Obtenir la company qui à crée le computer.
	 * 
	 * @param id
	 *            Identifiant du computer
	 * @return Une company
	 */
	public Optional<Company> findCompanyLink(long id) {

		Optional<Company> company = Optional.empty();
		
		try {
			company = Optional.ofNullable(
				jdbcTemplate.queryForObject(SQL_SELECT_COMPANY_OF_COMPUTER, new Long[] { id }, new CompanyMapper()));
		} catch (DataAccessException dae) {
			LOGGER.debug("[findCompanyLink] Probleme lors de la recherche de l'element.", dae);
		}
		
		return company;
	}

	/**
	 * Obtenir le nombre de computer dans la base de données.
	 * 
	 * @return Un nombre de computer
	 */
	public Optional<Long> getCount() {

		Optional<Long> res = Optional.empty();
		
		try {
			res = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_COUNT, new RowMapper<Long>() {
					@Override
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong("number");
					}
				}));
		} catch (DataAccessException dae) {
			LOGGER.debug("[getCount] Probleme lors du décompte du nombre d'element.", dae);
		}
		
		return res;
	}

	/**
	 * Obtenir le nombre de computer dans la base de données.
	 * 
	 * @param parameter
	 *            Une chaine de caractere
	 * @return Un nombre de computer
	 */
	public Optional<Long> getCountByName(String parameter) {

		Optional<Long> res = Optional.empty();
		
		try {
			res = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_COUNT_BY_NAME, new String[] { "%" + parameter + "%" },
					new RowMapper<Long>() {
						@Override
						public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getLong("number");
						}
					}));
		} catch (DataAccessException dae) {
			LOGGER.debug("[getCountByName] Probleme lors du décompte du nombre d'element.", dae);
		}
		
		return res;
	}

	/**
	 * Récuperation des computers dont le nom est similaire à la chaine parameter.
	 * 
	 * @param offset
	 *            Determine à partir de quel element en commence à stocker ce qui
	 *            sera retourné
	 * @param nbElem
	 *            Nombre d'element à retourner
	 * @param parameter
	 *            Une chaine de caractere
	 * @return Une page
	 */
	public Page<Computer> findComputerByName(int offset, int nbElem, String parameter) {

		Page<Computer> p = new Page<Computer>(offset, nbElem);
		
		try {
			p.elems = jdbcTemplate.query(SQL_SEARCH_BY_NAME, new Object[] { "%" + parameter + "%", nbElem, offset },
				new ComputerMapper());
		} catch (DataAccessException dae) {
			LOGGER.debug("[findComputerByName] Probleme lors de la recherche des computers.", dae);
		}
		
		return p;
	}

	/**
	 * Récuperation des computers dont le nom de company est similaire à la chaine
	 * parameter.
	 * 
	 * @param offset
	 *            Determine à partir de quel element en commence à stocker ce qui
	 *            sera retourné
	 * @param nbElem
	 *            Nombre d'element à retourner
	 * @param parameter
	 *            Une chaine de caractere
	 * @return Une page
	 */
	public Page<Computer> findComputerByCompany(int offset, int nbElem, String parameter) {

		Page<Computer> p = new Page<Computer>(offset, nbElem);
		
		try {
			p.elems = jdbcTemplate.query(SQL_SEARCH_BY_COMPANY_NAME, new Object[] { "%" + parameter + "%", nbElem, offset },
					new ComputerMapper());
		} catch (DataAccessException dae) {
			LOGGER.debug("[findComputerByCompany] Probleme lors de la recherche des computers.", dae);
		}
		
		return p;

	}
}
