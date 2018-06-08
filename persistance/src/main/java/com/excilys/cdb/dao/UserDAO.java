package main.java.com.excilys.cdb.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;

import main.java.com.excilys.cdb.exception.DAOException;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.model.QUser;
import main.java.com.excilys.cdb.utils.Page;

public class UserDAO extends ModelDAO {
	QUser quser = QUser.user;
	Session session;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
	public HibernateQueryFactory queryFactory;

	@Override
	Optional<Long> create(Model model) throws Exception {
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
	Optional<? extends Model> findById(long id) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void update(Model m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	Page<? extends Model> findAll(long offset, long nbElem) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
