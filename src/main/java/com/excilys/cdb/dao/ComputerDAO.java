package main.java.com.excilys.cdb.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;

import main.java.com.excilys.cdb.exception.DAOException;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.model.QCompany;
import main.java.com.excilys.cdb.model.QComputer;
import main.java.com.excilys.cdb.persistance.HibernateUtil;
import main.java.com.excilys.cdb.utils.Page;

public class ComputerDAO extends ModelDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDAO.class);
	public HibernateQueryFactory queryFactory;
	QComputer qcomputer = QComputer.computer;
	QCompany qcompany = QCompany.company;
	Session session;

	@Override
	public Optional<Long> create(Model model) throws DAOException {

		Optional<Long> res = Optional.empty();

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.save(model);
			session.getTransaction().commit();
			session.close();
			res = Optional.ofNullable(((Computer) model).getId());
		} catch (Exception e) {
			LOGGER.debug("[create] Probleme lors de la création de l'element.", e);
		}

		return res;
	}

	@Override
	public Optional<Computer> findById(long id) {

		Optional<Computer> computer = Optional.empty();

		try {
			session = HibernateUtil.getSession();
			queryFactory = new HibernateQueryFactory(session);
			computer = Optional.ofNullable(queryFactory.selectFrom(qcomputer).where(qcomputer.id.eq(id)).fetchOne());
			session.close();
		} catch (Exception e) {
			LOGGER.debug("[findById] Probleme lors de la recherche de l'element.", e);
		}

		return computer;
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
			session = HibernateUtil.getSession();
			queryFactory = new HibernateQueryFactory(session);
			p.elems = queryFactory.selectFrom(qcomputer).leftJoin(qcompany).where(qcomputer.companyId.eq(id))
					.offset(offset).limit(nbElem).fetch();
			session.close();
		} catch (Exception dae) {
			LOGGER.debug("[findByCompanyId] Probleme lors de la recherche de l'element.", dae);
		}
		return p;
	}

	@Override
	public Page<Computer> findAll(int offset, int nbElem) {

		Page<Computer> p = new Page<Computer>(offset, nbElem);

		try {
			if (nbElem == Page.NO_LIMIT) {
				session = HibernateUtil.getSession();
				queryFactory = new HibernateQueryFactory(session);
				p.elems = queryFactory.selectFrom(qcomputer).fetch();
				session.close();
			} else {
				session = HibernateUtil.getSession();
				queryFactory = new HibernateQueryFactory(session);
				p.elems = queryFactory.selectFrom(qcomputer).offset(offset).limit(nbElem).fetch();
				session.close();
			}
		} catch (Exception dae) {
			LOGGER.debug("[findAll] Probleme lors de la recherche des elements.", dae);
		}
		return p;
	}

	@Override
	public void update(Model m) {

		try {
			session = HibernateUtil.getSession();
			Transaction tx = session.beginTransaction();
			queryFactory = new HibernateQueryFactory(session);

			queryFactory.update(qcomputer).where(qcomputer.id.eq(((Computer) m).getId()))
					.set(qcomputer.name, ((Computer) m).getName())
					.set(qcomputer.introduced, ((Computer) m).getIntroduced())
					.set(qcomputer.discontinued, ((Computer) m).getDiscontinued())
					.set(qcomputer.companyId, ((Computer) m).getCompanyId()).execute();

			session.flush();
			tx.commit();
			session.close();
		} catch (Exception dae) {
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
			session = HibernateUtil.getSession();
			Transaction tx = session.beginTransaction();
			queryFactory = new HibernateQueryFactory(session);

			for (Object o : elems) {
				queryFactory.delete(qcomputer).where(qcomputer.id.eq(Long.parseLong((String) o))).execute();
				session.flush();
			}

			tx.commit();
			session.close();
		} catch (Exception dae) {
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
			session = HibernateUtil.getSession();
			queryFactory = new HibernateQueryFactory(session);
			company = Optional.ofNullable(queryFactory.selectFrom(qcompany).leftJoin(qcomputer)
					.on(qcompany.id.eq(qcomputer.companyId)).where(qcomputer.id.eq(id)).fetchOne());
			session.close();
		} catch (Exception dae) {
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
			session = HibernateUtil.getSession();
			queryFactory = new HibernateQueryFactory(session);
			res = Optional.ofNullable(queryFactory.selectFrom(qcomputer).fetchCount());
			session.close();
		} catch (Exception dae) {
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
			session = HibernateUtil.getSession();
			queryFactory = new HibernateQueryFactory(session);
			res = Optional.ofNullable(
					queryFactory.selectFrom(qcomputer).where(qcomputer.name.like("%" + parameter + "%")).fetchCount());
			session.close();
		} catch (Exception dae) {
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
			session = HibernateUtil.getSession();
			queryFactory = new HibernateQueryFactory(session);
			p.elems = queryFactory.selectFrom(qcomputer).where(qcomputer.name.like("%" + parameter + "%"))
					.offset(offset).limit(nbElem).fetch();
			session.close();
		} catch (Exception dae) {
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
			session = HibernateUtil.getSession();
			queryFactory = new HibernateQueryFactory(session);
			p.elems = queryFactory.selectFrom(qcomputer).leftJoin(qcompany).on(qcomputer.companyId.eq(qcompany.id)).where(qcompany.name.like("%" + parameter + "%")).offset(offset)
					.limit(nbElem).fetch();
			session.close();
		} catch (Exception dae) {
			LOGGER.debug("[findComputerByCompany] Probleme lors de la recherche des computers.", dae);
		}

		return p;

	}
}
