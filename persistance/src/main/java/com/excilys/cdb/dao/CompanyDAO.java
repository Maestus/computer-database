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
import main.java.com.excilys.cdb.utils.Page;

public class CompanyDAO extends ModelDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDAO.class);

	public HibernateQueryFactory queryFactory;
	QCompany qcompany = QCompany.company;
	QComputer qcomputer = QComputer.computer;
	Session session;

	public void setQueryFactory(Session session) {
		this.queryFactory = new HibernateQueryFactory(session);
	}

	@Override
	public Optional<Long> create(Model model) throws Exception {

		Optional<Long> res = Optional.empty();

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.save(model);
			session.getTransaction().commit();
			session.close();
			res = Optional.ofNullable(((Company) model).getId());
		} catch (Exception e) {
			LOGGER.debug("[create] Probleme lors de la création de l'element.", e);
		}

		return res;
	}

	@Override
	public Optional<Company> findById(long id) throws DAOException {
		Optional<Company> company = Optional.empty();

		try {
			session = HibernateUtil.getSession();
			queryFactory = new HibernateQueryFactory(session);
			company = Optional.ofNullable(queryFactory.selectFrom(qcompany).where(qcompany.id.eq(id)).fetchOne());
			session.close();
		} catch (Exception dae) {
			LOGGER.debug("[findById] Probleme lors de la recherche de l'element.", dae);
		}

		return company;
	}

	@Override
	public Page<Company> findAll(int offset, int nbElem) throws DAOException {

		Page<Company> p = new Page<Company>(offset, nbElem);

		try {
			if (nbElem == Page.NO_LIMIT) {
				session = HibernateUtil.getSession();
				queryFactory = new HibernateQueryFactory(session);
				p.elems = queryFactory.selectFrom(qcompany).fetch();
				session.close();
			} else {
				session = HibernateUtil.getSession();
				queryFactory = new HibernateQueryFactory(session);
				p.elems = queryFactory.selectFrom(qcompany).offset(offset).limit(nbElem).fetch();
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

			queryFactory.update(qcompany).where(qcompany.id.eq(((Computer) m).getId()))
					.set(qcompany.name, ((Computer) m).getName()).execute();

			session.flush();
			tx.commit();
			session.close();
		} catch (Exception dae) {
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
			session = HibernateUtil.getSession();
			queryFactory = new HibernateQueryFactory(session);
			res = Optional.ofNullable(
					queryFactory.selectFrom(qcomputer).leftJoin(qcompany).on(qcomputer.companyId.eq(qcompany.id)).where(qcompany.name.like("%" + parameter + "%")).fetchCount());
			session.close();
		} catch (Exception dae) {
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

			session = HibernateUtil.getSession();
			Transaction tx = session.beginTransaction();
			queryFactory = new HibernateQueryFactory(session);

			queryFactory.delete(qcomputer).where(qcomputer.companyId.eq(id)).execute();
			session.flush();

			queryFactory.delete(qcompany).where(qcompany.id.eq(id)).execute();
			session.flush();
			
			tx.commit();
			session.close();
			
		} catch (Exception e) {
			LOGGER.debug("[getCountByCompanyName] Probleme lors du décompte du nombre d'element.", e);
		}
	}
}
