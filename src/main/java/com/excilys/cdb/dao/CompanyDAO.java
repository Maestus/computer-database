package main.java.com.excilys.cdb.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionException;

import main.java.com.excilys.cdb.exception.DAOException;
import main.java.com.excilys.cdb.mapper.CompanyMapper;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.utils.Page;
import main.java.com.excilys.cdb.validator.CompanyValidator;

public class CompanyDAO extends ModelDAO {

	private static final String SQL_SELECT_PAR_ID = "SELECT id, name FROM company WHERE id = ?;";
	private static final String SQL_SELECT_ALL = "SELECT id, name FROM company LIMIT ? OFFSET ?;";
	private static final String SQL_SELECT_ALL_NOLIMIT = "SELECT id, name FROM company;";
	private static final String SQL_UPDATE = "UPDATE company SET name = ? WHERE id = ?;";
	private static final String SQL_DELETE = "DELETE FROM company WHERE id = ?;";
	private static final String SQL_DELETE_COMPUTER = "DELETE FROM computer WHERE company_id = ?;";
	private static final String SQL_COUNT_BY_COMPANY_NAME = "SELECT COUNT(*) as number FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE company.name LIKE ?;";
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDAO.class);

	public JdbcTemplate jdbcTemplate;
	public DataSourceTransactionManager txManager;
	public CompanyValidator companyvalidator;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setDataSourceTransactionManager(DataSourceTransactionManager txManager) {
		this.txManager = txManager;
	}

	public void setValidator(CompanyValidator companyvalidator) {
		this.companyvalidator = companyvalidator;
	}

	@Override
	public Optional<Long> create(Model model) throws Exception {

		Optional<Long> res = Optional.empty();

		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("company")
				.usingGeneratedKeyColumns("id");
		Map<String, Object> parameters = new HashMap<String, Object>();

		if (companyvalidator.checkBeforeCreation(model)) {

			parameters.put("name", ((Company) model).getName());

			try {
				res = Optional.ofNullable((Long) jdbcInsert.executeAndReturnKey(parameters));
			} catch (Exception e) {
				LOGGER.debug("[create] Probleme lors de la création de l'element.", e);
			}
		} else {
			LOGGER.info("[create] Impossible d'inserer un element vide.");
		}

		return res;
	}

	@Override
	public Optional<Company> findById(long id) throws DAOException {
		Optional<Company> company = Optional.empty();

		try {
			company = Optional
					.of(jdbcTemplate.queryForObject(SQL_SELECT_PAR_ID, new Long[] { id }, new CompanyMapper()));
		} catch (DataAccessException dae) {
			LOGGER.debug("[findById] Probleme lors de la recherche de l'element.", dae);
		}

		return company;
	}

	@Override
	public Page<Company> findAll(int offset, int nbElem) throws DAOException {

		Page<Company> p = new Page<Company>(offset, nbElem);

		try {
			if (nbElem == Page.NO_LIMIT) {
				p.elems = jdbcTemplate.query(SQL_SELECT_ALL_NOLIMIT, new CompanyMapper());
			} else {
				p.elems = jdbcTemplate.query(SQL_SELECT_ALL, new Object[] { nbElem, offset }, new CompanyMapper());
			}
		} catch (DataAccessException dae) {
			LOGGER.debug("[findAll] Probleme lors de la recherche des elements.", dae);
		}

		return p;
	}

	@Override
	public void update(Model m) {

		try {
			jdbcTemplate.update(SQL_UPDATE, ((Company)m).getName(), ((Company)m).getId());
		} catch (DataAccessException dae) {
			LOGGER.debug("[update] Probleme lors de la mise à jour de l'element.", dae);
		}
	}

	/**
	 * Obtenir le nombre de computer dans la base de données.
	 * 
	 * @param parameter
	 *            Une chaine de caractere
	 * @return Un nombre de computer
	 */
	public Optional<Long> getCountByCompanyName(String parameter) {

		Optional<Long> res = Optional.empty();

		try {
			res = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_COUNT_BY_COMPANY_NAME,
					new String[] { "%" + parameter + "%" }, new RowMapper<Long>() {
						@Override
						public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getLong("number");
						}
					}));

		} catch (DataAccessException dae) {
			LOGGER.debug("[getCountByCompanyName] Probleme lors du décompte du nombre d'element.", dae);
		}

		return res;
	}

	/**
	 * Permet la suppression d'un tuple.
	 * 
	 * @param id
	 *            identitifiant du tuple à supprimer.
	 * @throws DAOException
	 *             Envoyé si rien trouvé.
	 */
	public void delete(long id) throws DAOException {

		try {

			jdbcTemplate.update(SQL_DELETE_COMPUTER, id);

			jdbcTemplate.update(SQL_DELETE, id);

		} catch (DataAccessException | TransactionException e) {
			LOGGER.debug("[getCountByCompanyName] Probleme lors du décompte du nombre d'element.", e);
		}
	}
}
